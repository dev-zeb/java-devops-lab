package com.acme.hello;

import java.time.Instant;

public class Main {
    public static void main(String[] args) {
        String name = (args.length > 0) ? args[0] : "World";
        System.out.println("Hello, " + name + "!");
        System.out.println("Build/run time: " + Instant.now());
        System.out.println("Java version: " + System.getProperty("java.version"));
    }
}
