package com.acme.app;

import com.acme.util.StringUtils;

public class Main {
    public static void main(String[] args) {
        String msg = (args.length > 0) ? args[0] : "dependency demo";
        System.out.println(StringUtils.shout(msg));
    }
}
