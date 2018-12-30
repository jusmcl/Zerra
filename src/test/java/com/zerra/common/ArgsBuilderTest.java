package com.zerra.common;

import org.junit.Test;

import static org.junit.Assert.*;

public class ArgsBuilderTest {

    @Test
    public void deserialize() {
        //Server
        String[] args$server = {
                "--server"
        };
        ArgsBuilder builder$server = ArgsBuilder.deserialize(args$server);
        if(builder$server.isClient()){
            System.err.println("Something went wrong");
        }

        //Client 1
        String[] args$client1 = {
                "--client",
                "--username",
                "tebreca",
                "--loginkey",
                "key"
        };
        ArgsBuilder builder$client1 = ArgsBuilder.deserialize(args$client1);
        if(builder$client1.isServer()){
            System.err.println("Something went wrong");
        }
        System.out.println(builder$client1.getLoginKey());
        System.out.println(builder$client1.getUsername());

        //Client 2 (development environment)
        String[] args$client2 = {};
        ArgsBuilder builder$client2 = ArgsBuilder.deserialize(args$client2);
        if(builder$client2.isServer()){
            System.err.println("Something went wrong");
        }
        System.out.println(builder$client2.getLoginKey());
        System.out.println(builder$client2.getUsername());
    }
}