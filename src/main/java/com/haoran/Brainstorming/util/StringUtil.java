package com.haoran.Brainstorming.util;

import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtil {

    private StringUtil() {
    }


    public static final String MOBILEREGEX = "^1[0-9]{10}";

    public static final String EMAILREGEX = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";

    public static final String URLREGEX = "^((https|http)?:\\/\\/)[^\\s]+";

    public static final String USERNAMEREGEX = "[a-z0-9A-Z]{2,16}";

    public static final String PASSWORDREGEX = "[a-z0-9A-Z]{6,32}";

    private static final char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c',
            'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            'z'};

    private static final char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    public static final Random random = new Random();

    public static boolean check(String text, String regex) {
        if (StringUtils.isEmpty(text)) {
            return false;
        } else {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            return matcher.matches();
        }
    }

    /**
     *Une chaîne aléatoire de longueur spécifiée
     * @param length
     * @return
     */
    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int loop = 0; loop < length; ++loop) {
            sb.append(hexDigits[random.nextInt(hexDigits.length)]);
        }
        return sb.toString();
    }

    /**
     *nombre aléatoire de longueur spécifiée
     * @param length
     * @return
     */
    public static String randomNumber(int length) {
        StringBuilder sb = new StringBuilder();
        for (int loop = 0; loop < length; ++loop) {
            sb.append(digits[random.nextInt(digits.length)]);
        }
        return sb.toString();
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }


    /**
     * Vérifiez s'il s'agit d'un token d'accès utilisateur
     */
    public static boolean isUUID(String accessToken) {
        if (StringUtils.isEmpty(accessToken)) {
            return false;
        } else {
            try {
                UUID.fromString(accessToken);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }


    public static Map<String, Object> formatParams(String params) {
        if (StringUtils.isEmpty(params)) return null;
        Map<String, Object> map = new HashMap<>();
        for (String s : params.split("&")) {
            String[] ss = s.split("=");
            map.put(ss[0], ss[1]);
        }
        return map;
    }

    // Trouver le nom d'utilisateur de @ dans le commentaire
    public static List<String> fetchAtUser(String content) {
        if (StringUtils.isEmpty(content)) return Collections.emptyList();
        content = content.replaceAll("```([\\s\\S]*)```", "");
        content = content.replaceAll("`([\\s\\S]*)`", "");
        String atRegex = "@[a-z0-9-_]+\\b?";
        List<String> atUsers = new ArrayList<>();
        Pattern regex = Pattern.compile(atRegex);
        Matcher regexMatcher = regex.matcher(content);
        while (regexMatcher.find()) {
            atUsers.add(regexMatcher.group());
        }
        return atUsers;
    }

    // supprimer les nulls et les doublons du tableau
    public static Set<String> removeEmpty(String[] strs) {
        if (strs == null || strs.length == 0) return Collections.emptySet();
        Set<String> set = new HashSet<>();
        for (String str : strs) {
            if (!StringUtils.isEmpty(str)) {
                set.add(str);
            }
        }
        return set;
    }

}
