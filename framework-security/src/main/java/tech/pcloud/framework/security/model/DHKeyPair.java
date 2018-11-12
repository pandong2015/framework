package tech.pcloud.framework.security.model;

import lombok.Builder;
import lombok.Data;

import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;

@Data
@Builder
public class DHKeyPair {
  private DHPublicKey publicKey;
  private DHPrivateKey privateKey;
}
