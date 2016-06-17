package com.sfirestone.servit.comm;

import java.io.Serializable;

/**
 * Created by firestone on 6/16/16.
 */
public abstract class Communication implements Serializable {
    public enum Type {
        CACHE,
    }

    protected final Type type;
    protected String username;

    protected Communication(Type type) {
        this.type = type;
    }

    public final Type getType() {
        return this.type;
    }

    public final String getUsername() {
        return this.username;
    }
}
