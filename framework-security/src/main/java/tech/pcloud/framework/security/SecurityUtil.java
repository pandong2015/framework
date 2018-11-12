package tech.pcloud.framework.security;

import lombok.extern.slf4j.Slf4j;
import sun.security.tools.keytool.CertAndKeyGen;
import sun.security.x509.*;
import tech.pcloud.framework.exception.StoreNotFoundException;
import tech.pcloud.framework.security.model.CertificateInfo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Slf4j
public class SecurityUtil {
    public static final String KEY_STORE_TYPE_JKS = "jks";
    public static final String KEY_STORE_TYPE_PKCS12 = "pkcs12";
    public static final String DEFAULT_KEY_STORE_TYPE = KEY_STORE_TYPE_PKCS12;
    public static final String DEFAULT_TRUST_STORE_TYPE = KEY_STORE_TYPE_JKS;

    public static CertificateInfo getCertificate(Path keyStorePath, String keyStoreType, String alias, String password)
            throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        if (!Files.exists(keyStorePath)) {
            throw new StoreNotFoundException(keyStorePath.toString() + " not exist.");
        }
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        try (InputStream in = Files.newInputStream(keyStorePath)) {
            keyStore.load(in, password.toCharArray());
        }
        X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
        return CertificateInfo.builder().alias(alias).certificate(certificate).privateKey(privateKey).build();
    }

    public static CertificateInfo createCertificateKeyStore(boolean signed, Path keyStorePath, String keyStoreType, String alias,
                                                            String password, int keySize, String subject,
                                                            CertificateInfo issuerCertificate, long validity)
            throws CertificateException, NoSuchAlgorithmException, IOException, SignatureException, NoSuchProviderException,
            InvalidKeyException, KeyStoreException {
        int certificateSize = issuerCertificate == null ? 1 : 2;
        CertificateInfo certificateInfo = createCertificate(keySize, subject, validity);
        if (signed) {
            X509Certificate signedX509Certificate = createSigned(issuerCertificate == null ? false : true,
                    certificateInfo.getCertificate(),
                    issuerCertificate == null ? certificateInfo.getCertificate() : issuerCertificate.getCertificate(),
                    issuerCertificate == null ? certificateInfo.getPrivateKey() : issuerCertificate.getPrivateKey());
            certificateInfo.setCertificate(signedX509Certificate);
        }

        certificateInfo.setAlias(alias);
        KeyStore keyStore = saveStore(false, keyStoreType, keyStorePath, password, certificateInfo);
        return certificateInfo;
    }

    public static X509Certificate createSigned(boolean isClient, X509Certificate cetrificate,
                                               X509Certificate issuerCertificate,
                                               PrivateKey issuerPrivateKey) {
        try {
            Principal issuer = issuerCertificate.getSubjectDN();
            String issuerSigAlg = issuerCertificate.getSigAlgName();

            byte[] inCertBytes = cetrificate.getTBSCertificate();
            X509CertInfo info = new X509CertInfo(inCertBytes);
            info.set(X509CertInfo.ISSUER, (X500Name) issuer);

            //No need to add the BasicContraint for leaf cert
            if (!isClient) {
                CertificateExtensions exts = new CertificateExtensions();
                BasicConstraintsExtension bce = new BasicConstraintsExtension(true, -1);
                exts.set(BasicConstraintsExtension.NAME, new BasicConstraintsExtension(false, bce.getExtensionValue()));
                info.set(X509CertInfo.EXTENSIONS, exts);
            }

            X509CertImpl outCert = new X509CertImpl(info);
            outCert.sign(issuerPrivateKey, issuerSigAlg);

            return outCert;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static CertificateInfo createCertificate(int length, String subject, long validity)
            throws NoSuchProviderException, NoSuchAlgorithmException,
            InvalidKeyException, CertificateException, SignatureException, IOException {
        CertAndKeyGen keyGen = new CertAndKeyGen("RSA", "SHA1WithRSA", null);
        keyGen.generate(length);
        PrivateKey privateKey = keyGen.getPrivateKey();

        X509Certificate certificate = keyGen.getSelfCertificate(new X500Name(subject), validity);

        return CertificateInfo.builder().certificate(certificate).privateKey(privateKey).build();
    }

    public static KeyStore saveStore(boolean append, String storeType, String storePath, String passwd, CertificateInfo... certificateInfos)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        return saveStore(append, storeType, Paths.get(storePath), passwd, certificateInfos);
    }

    public static KeyStore saveStore(boolean append, String storeType, Path storePath, String passwd, CertificateInfo... certificateInfos)
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        KeyStore keyStore = KeyStore.getInstance(storeType);
        if (append && Files.exists(storePath)) {
            try (InputStream in = Files.newInputStream(storePath)) {
                keyStore.load(in, passwd.toCharArray());
            }
        } else {
            keyStore.load(null, passwd.toCharArray());
        }
        for (CertificateInfo certificateInfo : certificateInfos) {
            if (keyStore.containsAlias(certificateInfo.getAlias())) {
                log.warn("alias is exist, alias:" + certificateInfo.getAlias());
                continue;
            } else {
                keyStore
                        .setKeyEntry(certificateInfo.getAlias(), certificateInfo.getPrivateKey(), passwd.toCharArray(),
                                new Certificate[]{certificateInfo.getCertificate()});
            }
        }
        writeKeyStore(keyStore, storePath, passwd);
        return keyStore;
    }

    public static void writeKeyStore(KeyStore keyStore, Path path, String passwd)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        try (OutputStream out = Files.newOutputStream(path)) {
            keyStore.store(out, passwd.toCharArray());
        }
    }

    public static X509Certificate readCertificate(byte[] data) throws CertificateException, IOException {
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        try (InputStream in = new ByteArrayInputStream(data)) {
            return (X509Certificate) certFactory.generateCertificate(in);
        }
    }
}
