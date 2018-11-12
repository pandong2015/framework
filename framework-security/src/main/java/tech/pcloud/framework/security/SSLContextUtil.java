package tech.pcloud.framework.security;

import lombok.Builder;
import lombok.Data;
import tech.pcloud.framework.security.model.KeyPair;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Enumeration;

import static tech.pcloud.framework.security.SecurityUtil.DEFAULT_KEY_STORE_TYPE;
import static tech.pcloud.framework.security.SecurityUtil.DEFAULT_TRUST_STORE_TYPE;


@Data
@Builder
public class SSLContextUtil {
  private SSLContext sslContext;
  private String protocol;

  public static SSLContextUtil getInstance(String protocol, String keyStorePassword, String keyStore,
                                           String trustStorePassword, String trustStore) {
    try {
      KeyStore ks = KeyStore.getInstance(DEFAULT_KEY_STORE_TYPE);
      ks.load(SSLContextUtil.class.getClassLoader().getResourceAsStream(keyStore), keyStorePassword.toCharArray());
      KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
      kmf.init(ks, keyStorePassword.toCharArray());
      Enumeration<String> ksAliases = ks.aliases();
      while (ksAliases.hasMoreElements()) {
        String alias = ksAliases.nextElement();
        SecurityCache.addKeyPair(alias,
                KeyPair.builder().privateKey((PrivateKey) ks.getKey(alias, keyStorePassword.toCharArray()))
                        .publicKey(ks.getCertificate(alias).getPublicKey()).build());
      }

      KeyStore ts = KeyStore.getInstance(DEFAULT_TRUST_STORE_TYPE);
      ts.load(SSLContextUtil.class.getClassLoader().getResourceAsStream(trustStore), trustStorePassword.toCharArray());
      TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      tmf.init(ts);
      Enumeration<String> tsAliases = ts.aliases();
      while (tsAliases.hasMoreElements()) {
        String alias = tsAliases.nextElement();
        SecurityCache.addTrustKeyPair(alias,
                KeyPair.builder().publicKey(ts.getCertificate(alias).getPublicKey()).build());
      }

      SSLContext sslContext = SSLContext.getInstance(protocol);
      sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
      return SSLContextUtil.builder().protocol(protocol).sslContext(sslContext).build();
    } catch (Exception e) {
      throw new Error("Failed to initialize the server-side SSLContext", e);
    }
  }
}
