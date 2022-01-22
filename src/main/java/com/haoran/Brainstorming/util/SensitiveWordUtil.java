package com.haoran.Brainstorming.util;

import com.aliyuncs.utils.StringUtils;

import java.util.*;


public class SensitiveWordUtil {

    public static final int MinMatchType = 1;
    public static final int MaxMatchType = 2;


    public static HashMap sensitiveWordMap;

    /**
     * @param sensitiveWordSet
     */
    public static synchronized void init(Set<String> sensitiveWordSet) {
        initSensitiveWordMap(sensitiveWordSet);
    }

    /**
     * @param sensitiveWordSet
     */
    private static void initSensitiveWordMap(Set<String> sensitiveWordSet) {
        sensitiveWordMap = new HashMap(sensitiveWordSet.size());
        String key;
        Map nowMap;
        Map<String, String> newWorMap;
        Iterator<String> iterator = sensitiveWordSet.iterator();
        while (iterator.hasNext()) {
            key = iterator.next();
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);
                Object wordMap = nowMap.get(keyChar);
                if (wordMap != null) {
                    nowMap = (Map) wordMap;
                } else {
                    newWorMap = new HashMap<>();
                    newWorMap.put("isEnd", "0");
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1");
                }
            }
        }
    }

    /**
     *
     * @param txt
     * @param matchType
     */
    public static boolean contains(String txt, int matchType) {
        boolean flag = false;
        for (int i = 0; i < txt.length(); i++) {
            int matchFlag = checkSensitiveWord(txt, i, matchType);
            if (matchFlag > 0) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     *
     * @param txt
     * @return
     */
    public static boolean contains(String txt) {
        return contains(txt, MaxMatchType);
    }

    /**
     *
     *
     * @param txt
     * @param matchType
     * @return
     */
    public static Set<String> getSensitiveWord(String txt, int matchType) {
        Set<String> sensitiveWordList = new HashSet<>();

        for (int i = 0; i < txt.length(); i++) {
            int length = checkSensitiveWord(txt, i, matchType);
            if (length > 0) {
                sensitiveWordList.add(txt.substring(i, i + length));
                i = i + length - 1;
            }
        }

        return sensitiveWordList;
    }

    /**
     *
     * @param txt
     * @return
     */
    public static Set<String> getSensitiveWord(String txt) {
        return getSensitiveWord(txt, MaxMatchType);
    }

    /**
     *
     * @param txt
     * @param replaceChar
     * @param matchType
     * @return
     */
    public static String replaceSensitiveWord(String txt, char replaceChar, int matchType) {
        String resultTxt = txt;
        Set<String> set = getSensitiveWord(txt, matchType);
        Iterator<String> iterator = set.iterator();
        String word;
        String replaceString;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }

        return resultTxt;
    }

    /**
     *
     * @param txt
     * @param replaceChar
     * @return
     */
    public static String replaceSensitiveWord(String txt, char replaceChar) {
        return replaceSensitiveWord(txt, replaceChar, MaxMatchType);
    }

    /**
     *
     * @param txt
     * @param replaceStr
     * @param matchType
     * @return
     */
    public static String replaceSensitiveWord(String txt, String replaceStr, int matchType) {
        if (StringUtils.isEmpty(txt)) return null;
        String resultTxt = txt;
        Set<String> set = getSensitiveWord(txt, matchType);
        Iterator<String> iterator = set.iterator();
        String word;
        while (iterator.hasNext()) {
            word = iterator.next();
            resultTxt = resultTxt.replaceAll(word, replaceStr);
        }

        return resultTxt;
    }

    /**
     *
     * @param txt
     * @param replaceStr
     * @return
     */
    public static String replaceSensitiveWord(String txt, String replaceStr) {
        return replaceSensitiveWord(txt, replaceStr, MaxMatchType);
    }

    /**
     *
     * @param replaceChar
     * @param length
     * @return
     */
    private static String getReplaceChars(char replaceChar, int length) {
        String resultReplace = String.valueOf(replaceChar);
        for (int i = 1; i < length; i++) {
            resultReplace += replaceChar;
        }

        return resultReplace;
    }

    /**
     *
     * @param txt
     * @param beginIndex
     * @param matchType
     * @return
     */
    private static int checkSensitiveWord(String txt, int beginIndex, int matchType) {
        boolean flag = false;
        int matchFlag = 0;
        char word;
        Map nowMap = sensitiveWordMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);
            if (nowMap != null) {
                matchFlag++;
                if ("1".equals(nowMap.get("isEnd"))) {
                    flag = true;
                    if (MinMatchType == matchType) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
        if (matchFlag < 2 || !flag) {
            matchFlag = 0;
        }
        return matchFlag;
    }



}
