package com.cbox.base.utils;

import java.util.Random;
import java.util.UUID;

public class RandomUtil {
    private static final Random rd = new Random();
    static final String INT = "0123456789";
    static final String STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static final String ALL = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String randomStr(int length) {
        return random(length, RandomUtil.RndType.STRING);
    }

    public static String randomInt(int length) {
        return random(length, RandomUtil.RndType.INT);
    }

    public static String randomAll(int length) {
        return random(length, RandomUtil.RndType.ALL);
    }

    private static String random(int length, RandomUtil.RndType rndType) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            char var5;
            if (rndType.equals(RandomUtil.RndType.INT)) {
                var5 = "0123456789".charAt(rd.nextInt("0123456789".length()));
            } else if (rndType.equals(RandomUtil.RndType.STRING)) {
                var5 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(rd.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length()));
            } else {
                var5 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(rd.nextInt("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length()));
            }
            s.append(var5);
        }
        return s.toString();
    }

    public static String getSequenceNumber() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static enum RndType {
        INT, STRING, ALL;
    }
}