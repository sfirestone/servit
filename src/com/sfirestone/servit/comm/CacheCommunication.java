package com.sfirestone.servit.comm;

import java.util.Map;

/**
 * Created by firestone on 6/16/16.
 */
public class CacheCommunication extends Communication {

    protected String key;
    protected String value;
    protected Boolean success;
    protected Map<String, String> response;

    private CacheCommunication() {
        super(Type.CACHE);
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return value;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Map<String, String> getResponse() {
        return response;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private CacheCommunication cacheCommunication;
        private Builder() {
            this.cacheCommunication = new CacheCommunication();
        }

        public Builder withUsername(String username) {
            this.cacheCommunication.username = username;
            return this;
        }

        public Builder withKey(String key) {
            this.cacheCommunication.key = key;
            return this;
        }

        public Builder withValue(String value) {
            this.cacheCommunication.value = value;
            return this;
        }

        public Builder withSuccess(boolean success) {
            this.cacheCommunication.success = success;
            return this;
        }

        public Builder response(Map<String, String> response) {
            this.cacheCommunication.response = response;
            return this;
        }

        public CacheCommunication build() {
            return this.cacheCommunication;
        }
    }

    @Override
    public String toString() {
        return "CacheCommunication{" + type + "(" + username + "): " +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", success=" + success +
                ", response=" + response +
                '}';
    }
}
