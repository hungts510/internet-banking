package com.hungts.internetbanking.util;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Random;

public class Utils {
    public static void close(AutoCloseable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
            closeable = null;
        } catch (Exception ex) {
            //todo log.error(ex.getMessage());
        }
    }

    public static boolean isEmpty(Object object) {
        return ObjectUtils.isEmpty(object);
    }

    public static boolean isBlank(String string) {
        return StringUtils.isBlank(string);
    }

    public static boolean isNotEmpty(Object object) {
        return ObjectUtils.isNotEmpty(object);
    }

    public static boolean isNumeric(String strNum) {
        return NumberUtils.isCreatable(strNum);
    }

    public static String nullToEmpty(String s) {
        return StringUtils.trimToEmpty(s);
    }

    public static String trimWithMask(String s, int maxLength) {
        if (s.length() > maxLength) {
            return ".. " + s.substring(s.length() - maxLength - 3, s.length());
        }
        return s;
    }

    public static String concatString(Object... content) {
        StringBuilder builder = new StringBuilder();
        for (Object str : content) {
            if (str instanceof Object[]) {
                builder.append(concatString((Object[]) str));
            } else {
                builder.append(str);
            }
        }
        return builder.toString();
    }

    public static String randomOTP() {
        String numbers = "0123456789";
        Random randomMethod = new Random();
        char[] otp = new char[6];
        for (int i = 0; i < 6; i++) {
            otp[i] = numbers.charAt(randomMethod.nextInt(numbers.length()));
        }

        return new String(otp);
    }
}