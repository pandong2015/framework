package tech.pcloud.framework.security;


import tech.pcloud.framework.security.model.KeyPair;
import tech.pcloud.framework.utility.common.Cache;

import java.util.Map;

public class SecurityCache {
    public enum SecurityCacheName {
        KEY_PAIR("key-pair"),
        TRUST_KEY_PAIR("trust-key-pair");

        private String name;

        SecurityCacheName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static Cache cache = new Cache("security");

    public static void addKeyPair(String alias, KeyPair keyPair) {
        addKeyPair(SecurityCacheName.KEY_PAIR, alias, keyPair);
    }

    public static void addTrustKeyPair(String alias, KeyPair keyPair) {
        addKeyPair(SecurityCacheName.TRUST_KEY_PAIR, alias, keyPair);
    }

    private static void addKeyPair(SecurityCacheName cacheName, String alias, KeyPair keyPair) {
        Map<String, KeyPair> keyPairMap = cache.map(cacheName.getName());
        keyPairMap.put(alias, keyPair);
    }

    public static KeyPair getKeyPair(String alias) {
        return getKeyPair(SecurityCacheName.KEY_PAIR, alias);
    }

    public static KeyPair getTrustKeyPair(String alias) {
        return getKeyPair(SecurityCacheName.TRUST_KEY_PAIR, alias);
    }

    private static KeyPair getKeyPair(SecurityCacheName cacheName, String alias) {
        Map<String, KeyPair> keyPairMap = cache.map(cacheName.getName());
        return keyPairMap.get(alias);
    }

}
