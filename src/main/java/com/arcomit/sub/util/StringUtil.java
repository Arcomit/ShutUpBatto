package com.arcomit.sub.util;

import net.minecraft.resources.ResourceLocation;

import java.util.regex.Pattern;

public class StringUtil {

    protected static final Pattern NUMBER_PATTERN = Pattern.compile("-?/d+(\\./d+)?");

    /***
     * 利用正则表达式来判断字符串是否为数字
     */
    public static boolean checkStrIsNum(String str) {
        if (str.isEmpty()) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '.') {
                continue;
            }
            if (c == '-') {
                continue;
            }
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static String formatLang(ResourceLocation resourceLocation) {
        return formatLang(resourceLocation.getNamespace(), resourceLocation.getPath());
    }

    public static String formatLang(String... namespace) {
        if (namespace.length == 0) {
            return "";
        }
        return String.join(".", namespace);
    }

}
