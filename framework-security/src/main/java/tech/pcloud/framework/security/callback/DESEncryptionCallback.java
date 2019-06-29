package tech.pcloud.framework.security.callback;


import tech.pcloud.framework.security.BASE64Util;
import tech.pcloud.framework.security.DESCoderUtil;

/**
 * Created by pandong on 17-3-20.
 */
public class DESEncryptionCallback implements EncryptionCallback {

    @Override
    public String decrypt(String value) {
        return DESCoderUtil.decrypt(value);
    }


}
