package tech.pcloud.framework.utility.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.pcloud.framework.exception.DigestException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public enum MessageDigestFactory {
  SHA1("SHA-1"), SHA256("SHA-256"), SHA512("SHA-512"), MD5("MD5");

  private Logger logger = LoggerFactory.getLogger(MessageDigestFactory.class);
  private MessageDigest messageDigest;

  MessageDigestFactory(String name) {
    try {
      messageDigest = MessageDigest.getInstance(name);
    } catch (NoSuchAlgorithmException e) {
      logger.error(e.getMessage(), e);
      throw new DigestException(e.getMessage(), e);
    }
  }

  public byte[] digest(String str) {
    return digest(str.getBytes(Charset.forName("UTF-8")));
  }

  public byte[] digest(byte[] bytes) {
    messageDigest.update(bytes);
    return messageDigest.digest();
  }

  public byte[] digest(Path path) {
    try {
      return digest(Files.newInputStream(path));
    } catch (IOException e) {
      throw new DigestException(e.getMessage(), e);
    }
  }

  public byte[] digest(File file) {
    try {
      return digest(new FileInputStream(file));
    } catch (FileNotFoundException e) {
      throw new DigestException(e.getMessage(), e);
    }
  }

  public byte[] digest(InputStream in) {
    try (BufferedInputStream bin = new BufferedInputStream(in)) {
      byte[] data = new byte[1024];
      int len = -1;
      while ((len = bin.read(data)) != -1) {
        messageDigest.update(data);
      }
      return messageDigest.digest();
    } catch (IOException e) {
      throw new DigestException(e.getMessage(), e);
    }
  }

  public static void main(String[] args){
    String s = "123456789";
    System.out.println(SHA1.digest(s).length + " - "+  Base64.getEncoder().encodeToString(SHA1.digest(s)));
    System.out.println(SHA256.digest(s).length+ " - "+  Base64.getEncoder().encodeToString(SHA256.digest(s)));
    System.out.println(SHA512.digest(s).length+ " - "+  Base64.getEncoder().encodeToString(SHA512.digest(s)));
    System.out.println(MD5.digest(s).length+ " - "+  Base64.getEncoder().encodeToString(MD5.digest(s)));
  }
}
