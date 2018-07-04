package com.along101.contract.context;

import com.along101.auth.common.identity.Identity;

import java.util.Optional;

/**
 * @author zhangchengxi
 * Date 2018/6/7
 */
public class IdentityContext {

    private static ThreadLocal<Identity> identity = new ThreadLocal<>();

    public static Optional<Identity> getIdentity() {
        return Optional.ofNullable(identity.get());
    }

    public static void setIdentity(Identity identity){
        IdentityContext.identity.set(identity);
    }

    public static void remove(){
        identity.remove();
    }
}
