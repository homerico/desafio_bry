package com.bry.desafio.signature.certificate;

import com.bry.desafio.exceptions.SignerCertificateException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.util.Selector;
import org.bouncycastle.util.Store;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.*;
import java.util.*;

import static com.bry.desafio.exceptions.SignerCertificateException.DIFFERENT_SIGNER_CERTIFICATE_FOUND;
import static com.bry.desafio.exceptions.SignerCertificateException.SIGNER_CERTIFICATE_NOT_FOUND;

/**
 * Classe wrapper para manipulação de certificados de assinantes.
 * Permite a obtenção e validação do certificado do assinante.
 */
public class SignerCertificateWrapper {
    
    private final X509Certificate signerCertificate;
    
    public SignerCertificateWrapper(Store<X509CertificateHolder> certStore, SignerInformation signer) throws SignerCertificateException, CertificateException {
        Collection<X509CertificateHolder> certCollection = certStore.getMatches(signer.getSID());

        if (certCollection.isEmpty()) {
            throw new SignerCertificateException(SIGNER_CERTIFICATE_NOT_FOUND);
        }

        this.signerCertificate = this.checkMultipleCertificates(certCollection);
    }

    /**
     * Verifica se há múltiplos certificados na coleção e se há certificados diferentes.
     * Se houver múltiplos certificados, itera sobre eles para garantir que são todos iguais.
     * Se encontrar certificados diferentes, significa que há um problema.
     * Por fim, se tiver multiplos certificados iguais, retorna um deles.
     *
     * @param certCollection Coleção de certificados a serem verificados.
     * @return O certificado selecionado.
     * @throws CertificateException Se ocorrer um erro ao processar os certificados.
     */
    private X509Certificate checkMultipleCertificates(Collection<X509CertificateHolder> certCollection) throws CertificateException, SignerCertificateException {
        Iterator<X509CertificateHolder> iterator = certCollection.iterator();
        
        X509Certificate prevCert = new JcaX509CertificateConverter().getCertificate(iterator.next());
        while (iterator.hasNext()) {
            X509Certificate actualCert = new JcaX509CertificateConverter().getCertificate(iterator.next());
            if (!prevCert.equals(actualCert)) {
                throw new SignerCertificateException(DIFFERENT_SIGNER_CERTIFICATE_FOUND);
            }
            prevCert = actualCert;
        }
        return prevCert;
    }

    public X509Certificate getSignerCertificate() {
        return signerCertificate;
    }

    /**
     * Valida o certificado do assinante levando em consideração as âncoras de confiança e os certificados intermediários.
     *
     * @param certStore Store de certificados contendo o certificado do assinante e talvez outros.
     * @throws CertificateException                   Se ocorrer um erro ao processar os certificados.
     * @throws InvalidAlgorithmParameterException     Se os parâmetros para validação do caminho de certificação forem inválidos.
     * @throws CertPathValidatorException             Se a validação do caminho de certificação falhar.
     * @throws NoSuchAlgorithmException               Se o algoritmo de validação não for encontrado.
     */
    public void validate(Store<X509CertificateHolder> certStore) throws CertificateException, InvalidAlgorithmParameterException, CertPathValidatorException, NoSuchAlgorithmException {
        Set<TrustAnchor> trustAnchors = TrustAnchors.getTrustAnchors();
        Set<X509Certificate> intermediateCertificates = TrustAnchors.getIntermediateCertificates();

        List<Certificate> certChain = new ArrayList<>();

        // Adiciona todos os certificados presentes na assinatura
        // (Sabe-se que há apenas o certificado do assinante, mas poderia haver os intermediários também)
        Collection<X509CertificateHolder> holders = certStore.getMatches(new SignerCertificateWrapper.AllCertSelector());
        for (X509CertificateHolder holder : holders) {
            certChain.add(new JcaX509CertificateConverter().getCertificate(holder));
        }

        // Adiciona os certificados intermediários da cadeia de confiança, caso não estiverem presentes ainda
        for (X509Certificate intermediateCertificate : intermediateCertificates) {
            if (!certChain.contains(intermediateCertificate)) {
                certChain.add(intermediateCertificate);
            }
        }

        CertPath certPath = CertificateFactory.getInstance("X.509").generateCertPath(certChain);

        PKIXParameters pkixParams = new PKIXParameters(trustAnchors);
        pkixParams.setRevocationEnabled(false);

        CertPathValidator validator = CertPathValidator.getInstance("PKIX");
        validator.validate(certPath, pkixParams);
    }

    /**
     * Um Selector que seleciona todos os certificados
     */
    private static class AllCertSelector implements Selector<X509CertificateHolder> {

        /**
         * @return Sempre é true, selecionando todos os certificados
         *
         * @see org.bouncycastle.util.Selector#match(java.lang.Object)
         */
        @Override
        public boolean match(X509CertificateHolder holder) {
            return true;
        }

        @Override
        public Object clone() {
            return new AllCertSelector();
        }
    }
}
