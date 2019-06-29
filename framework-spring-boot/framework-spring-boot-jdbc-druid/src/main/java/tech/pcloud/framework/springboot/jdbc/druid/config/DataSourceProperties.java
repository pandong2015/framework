package tech.pcloud.framework.springboot.jdbc.druid.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import tech.pcloud.framework.security.callback.EncryptionCallback;

/**
 * Created by pandong on 17-3-20.
 */
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceProperties {

    /**
     * Fully qualified name of the JDBC driver. Auto-detected based on the URL by default.
     */
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private boolean userNameEncryption;
    private boolean userPasswordEncryption;
    private String encryptionCallbackClass;

    private String decryptUserName;
    private String decryptPassword;
    private EncryptionCallback encryptionCallback;

    private int initialSize = 5;
    private int minIdle = 20;
    private int maxIdle = 30;
    private int maxActive = 50;

    private int maxWait = 1000;

    private long validateInterval = 30;
    private int timeBetweenEvictionRunsMillis = 60000;

    private long minEvictableIdleTimeMillis = 40000;

    private String validationQuery;
    private boolean testWhileIdle = true;
    private boolean testOnBorrow = false;
    private boolean testOnReturn;
    private boolean testOnConnect;

    private boolean poolPreparedStatements = true;
    private int maxPoolPreparedStatementPerConnectionSize = 20;

    public boolean isTestOnConnect() {
        return testOnConnect;
    }

    public void setTestOnConnect(boolean testOnConnect) {
        this.testOnConnect = testOnConnect;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isPoolPreparedStatements() {
        return poolPreparedStatements;
    }

    public void setPoolPreparedStatements(boolean poolPreparedStatements) {
        this.poolPreparedStatements = poolPreparedStatements;
    }

    public int getMaxPoolPreparedStatementPerConnectionSize() {
        return maxPoolPreparedStatementPerConnectionSize;
    }

    public void setMaxPoolPreparedStatementPerConnectionSize(int maxPoolPreparedStatementPerConnectionSize) {
        this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public int getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public long getValidateInterval() {
        return validateInterval;
    }

    public void setValidateInterval(long validateInterval) {
        this.validateInterval = validateInterval;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public boolean isUserNameEncryption() {
        return userNameEncryption;
    }

    public void setUserNameEncryption(boolean userNameEncryption) {
        this.userNameEncryption = userNameEncryption;
    }

    public boolean isUserPasswordEncryption() {
        return userPasswordEncryption;
    }

    public void setUserPasswordEncryption(boolean userPasswordEncryption) {
        this.userPasswordEncryption = userPasswordEncryption;
    }

    public String getEncryptionCallbackClass() {
        return encryptionCallbackClass;
    }

    public void setEncryptionCallbackClass(String encryptionCallbackClass) throws Exception {
        this.encryptionCallbackClass = encryptionCallbackClass;
        Object obj = Class.forName(this.encryptionCallbackClass).newInstance();
        if (obj instanceof EncryptionCallback) {
            encryptionCallback = (EncryptionCallback) obj;
        }
    }

    public EncryptionCallback getEncryptionCallback() {
        return encryptionCallback;
    }


    public String getDriverClassName() {
        return this.driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }


    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        if (userNameEncryption && getEncryptionCallback() != null) {
            if (decryptUserName == null) {
                decryptUserName = getEncryptionCallback().decrypt(this.username);
            }
            return decryptUserName;
        }
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        if (userPasswordEncryption && getEncryptionCallback() != null) {
            if (decryptPassword == null) {
                decryptPassword = getEncryptionCallback().decrypt(this.password);
            }
            return decryptPassword;
        }
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
