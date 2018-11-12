package tech.pcloud.framework.security.model;

import lombok.Builder;
import lombok.Data;

import java.security.PrivateKey;
import java.security.PublicKey;

@Data
@Builder
public class KeyPair {
  private PublicKey publicKey;
  private PrivateKey privateKey;
}
