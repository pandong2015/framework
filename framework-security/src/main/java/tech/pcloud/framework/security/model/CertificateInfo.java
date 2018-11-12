package tech.pcloud.framework.security.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

@Data
@Builder
@Slf4j
public class CertificateInfo {
  private String alias;
  private X509Certificate certificate;
  private PrivateKey privateKey;

  public PublicKey getPublicKey() {
    return certificate.getPublicKey();
  }
}
