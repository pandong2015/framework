package tech.pcloud.framework.security.model;

import lombok.Builder;
import lombok.Data;

import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;

@Data
@Builder
public class DSAKeyPair {
  private DSAPublicKey publicKey;
  private DSAPrivateKey privateKey;
}
