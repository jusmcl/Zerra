package com.zerra.common;

import java.util.Arrays;
import java.util.Iterator;

public class ArgsBuilder {

    public static final boolean IS_DEVELOPMENT_BUILD = true;

    private final boolean isServer;

    private final boolean isClient;

    private final String username;

    private final String loginKey;

    public ArgsBuilder(boolean isServer, String username, String loginKey){
        this.isServer = isServer;
        this.isClient = !isServer;
        this.username = username;
        this.loginKey = loginKey;
    }


    public static ArgsBuilder deserialize(String[] args){
        if(args.length == 0){
            if(IS_DEVELOPMENT_BUILD){
                return new ArgsBuilder(false, "player", "null");
            } else {
                System.exit(-1);
            }
        }
        boolean isServer = false;
        String username = null, loginKey = null;
        Iterator<String> iterator = Arrays.asList(args).iterator();
        while(iterator.hasNext()){
            String value = iterator.next();
            switch(value){
                case "--server":
                    isServer = true;
                    break;
                case "--client":
                    break;
                case "--loginkey":
                    if(!iterator.hasNext()){
                        throw new IllegalArgumentException("after --loginkey a login key should be specified");
                    }
                    String key = iterator.next();
                    if(key.startsWith("--")){
                        throw new IllegalArgumentException("after --loginkey a login key should be specified");
                    }
                    loginKey = key;
                    break;
                case "--username":
                    if(!iterator.hasNext()){
                        throw new IllegalArgumentException("after --username a username should be specified");
                    }
                    String name = iterator.next();
                    if(name.startsWith("--")){
                        throw new IllegalArgumentException("after --username a username should be specified");
                    }
                    username = name;
                    break;
                default:
                    break;
            }
        }
        if(username == null){
            if(IS_DEVELOPMENT_BUILD) {
                username = "player";
            } else {
                System.exit(-1);
            }
        }
        if(loginKey == null){
            if(IS_DEVELOPMENT_BUILD) {
                loginKey = "null";
            } else {
                System.exit(-1);
            }
        }
        return new ArgsBuilder(isServer, username, loginKey);
    }

    public boolean isClient() {
        return isClient;
    }

    public boolean isServer() {
        return isServer;
    }

    public String getLoginKey() {
        return loginKey;
    }

    public String getUsername() {
        return username;
    }
}
