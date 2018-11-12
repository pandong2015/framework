package tech.pcloud.framework.security;

import lombok.extern.slf4j.Slf4j;
import tech.pcloud.framework.exception.ExceptionCode;
import tech.pcloud.framework.exception.SecurityException;
import tech.pcloud.framework.security.model.CertificateInfo;

import java.nio.file.Paths;

@Slf4j
public class SecurityTool {
    public static final long DEFAULT_CERTIFICATE_VALIDITY_YEAR = 365 * 24 * 60 * 60;
    public static final long DEFAULT_CERTIFICATE_VALIDITY_DAY = 24 * 60 * 60;
    public static final long DEFAULT_CERTIFICATE_VALIDITY_MOUTH = 365 * 30 * 60 * 60;
    public static final long DEFAULT_CERTIFICATE_VALIDITY_HOUR = 60 * 60;

    public static CertificateInfo signed(String storePath, String storeType, String alias, String password, int keySize,
                                         String subject, CertificateInfo issuerCertificate, long validity) {
        try {
            return SecurityUtil.createCertificateKeyStore(true, Paths.get(storePath), storeType, alias, password, keySize, subject,
                    issuerCertificate, validity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new SecurityException(e.getMessage(), e, ExceptionCode.SECURITY_FAIL.getCode());
        }
    }
}
