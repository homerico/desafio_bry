package com.bry.desafio;


import com.bry.desafio.report.Report;
import com.bry.desafio.signer.CMSSigner;
import com.bry.desafio.signer.SignerException;
import com.bry.desafio.utils.KeyStoreUtils;
import com.bry.desafio.verifier.CMSVerifier;
import com.bry.desafio.verifier.VerifierException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static org.junit.jupiter.api.Assertions.*;

public class CMSTest {

    // Hash SHA-512 de referência do conteúdo em doc.txt obtido com a ferramenta online https://www.dcode.fr/sha512-hash
    private static final String digestReference = "dc1a7de77c59a29f366a4b154b03ad7d99013e36e08beb50d976358bea7b045884fe72111b27cf7d6302916b2691ac7696c1637e1ab44584d8d6613825149e35";
    private static final String certificateAlias = "{e2618a8b-20de-4dd2-b209-70912e3177f4}";
    private static final String certificatePassword = "bry123456";
    private static final String digestAlgorithm = Algorithms.DIGEST_ALGORITHMS.SHA512.name();
    private static final String signatureAlgorithm = Algorithms.SIGNATURE_ALGORITHMS.SHA512withRSA.name();
    private static final String docToBeSigned = "arquivos/doc.txt";
    private static final String trustAnchorPath = "cadeia/";
    private static final String keyStoreFile = "pkcs12/certificado_teste_hub.pfx";
    private static final String artifactsPath = "artefatos/";
    private static final String digestFile = "resumo.txt";
    private static final String signatureFile = "assinatura.p7s";
    private static KeyStore keyStore;
    private static String digestHexString;
    private static byte[] content;
    private static byte[] signedContent;

    @BeforeAll
    public static void setup() {
        // Adiciona o provider Bouncy Castle como o primeiro da lista, assim sempre será ele a prover os serviços
        Security.insertProviderAt(new BouncyCastleProvider(), 1);

        // Carrega as âncoras de confiança
        try {
            TrustAnchors.loadTrustAnchors(trustAnchorPath);
        } catch (CertificateException | IOException | URISyntaxException e) {
            fail("Falha ao carregar as âncoras de confiança.", e);
        }

        // Carrega o conteúdo a ser assinado
        try (InputStream is = CMSTest.class.getClassLoader().getResourceAsStream(docToBeSigned)){
            assertNotNull(is);
            content = is.readAllBytes();
        } catch (IOException e) {
            fail("Falha ao carregar documento a ser assinado.", e);
        }

        // Carrega o repositório de chaves
        try (InputStream is = CMSTest.class.getClassLoader().getResourceAsStream(keyStoreFile)) {
            assertNotNull(is);
            keyStore = KeyStoreUtils.getKeyStore(is, certificatePassword.toCharArray());
        } catch (IOException | CertificateException | KeyStoreException | NoSuchAlgorithmException e) {
            fail("Falha ao carregar o repositório de chaves.", e);
        }
    }

    /** Armazena os artefatos gerados (resumo e assinatura) em arquivos na pasta "artefatos" apenas se
     * não estiver em ambiente CI e se ambos os artefatos foram gerados com sucesso.
     */
    @AfterAll
    public static void storeArtifacts() {
        if (digestHexString == null || signedContent == null || System.getenv("CI") != null) {
            return;
        }
        File artifactsDirectory = new File(artifactsPath);
        if (!artifactsDirectory.exists()) {
            artifactsDirectory.mkdirs();
        }
        try {
            Path digestPath = Paths.get(artifactsPath + digestFile);
            try (OutputStream os = new BufferedOutputStream(Files.newOutputStream(digestPath))) {
                os.write(digestHexString.getBytes());
            }

            Path signaturePath = Paths.get(artifactsPath + signatureFile);
            try (OutputStream os = new BufferedOutputStream(Files.newOutputStream(signaturePath))) {
                os.write(signedContent);
            }
        } catch (IOException e) {
            System.out.println("Falha ao gerar os artefatos.");
            e.printStackTrace();
        }
    }

    /**
     * Verifica se o hash do conteúdo é gerado corretamente e corresponde ao valor de referência.
     */
    @Test
    public void isDigestValid() {
        ToBeSignedContent toBeSignedContent = new ToBeSignedContent(content);
        byte[] hashedContent = null;

        try {
            hashedContent = toBeSignedContent.getHash(digestAlgorithm);
        } catch (SignerException e) {
            fail(e.getMessage(), e.getCause());
        }

        digestHexString = Hex.toHexString(hashedContent);
        assertEquals(digestReference, digestHexString);
    }

    /** Verifica se a assinatura CMS é gerada corretamente e se é possível validá-la com sucesso.
     * Também testa se o certificado do assinante pode ser validado até uma âncora de confiança.
     */
    @Test
    public void isSignatureValid() {
        try {
            PrivateKey privateKey = KeyStoreUtils.getPrivateKeyData(keyStore, certificateAlias, certificatePassword.toCharArray());

            X509Certificate x509Certificate = KeyStoreUtils.getCertificateData(keyStore, certificateAlias);

            CMSSigner cmsSigner = new CMSSigner(x509Certificate, privateKey, signatureAlgorithm);
            signedContent = cmsSigner.sign(content);

            assertNotNull(signedContent);
            assertTrue(signedContent.length > 0);

            CMSVerifier signatureVerifier = new CMSVerifier();
            signatureVerifier.verify(signedContent);

            Report report = signatureVerifier.getVerificationReport();
            assertTrue(report.isIntegrityValid());
            assertTrue(report.isCertificateTrusted());
        } catch (SignerException | VerifierException e) {
            fail(e.getMessage(), e.getCause());
        } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException |
                 CertificateException e) {
            fail("Falha ao obter os dados do certificado ou da chave privada.", e);
        }
    }
}
