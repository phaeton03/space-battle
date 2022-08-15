package org.example;

import org.springframework.util.StringUtils;

import java.util.Arrays;

public class SpaceBattle {
    public static void main(String[] args) {

        System.out.println("Hello world!");

        String s = "getP";
        String get = s.replaceFirst("^get", s.substring(3));


        System.out.println(Arrays.toString(s.split("getP")));
        System.out.println((s.split("get")).length);
    }
}