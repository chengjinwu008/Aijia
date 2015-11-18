package com.cjq.aijia.util;

public class Validator {
    public static boolean checkVerify(String verifyText) {
        return verifyText.matches("^\\d{6}$");
    }

    public static boolean checkMobile(String mobileText) {
        return mobileText.matches("^1[3|4|5|8|7][0-9]\\d{8}$");
    }

    public static boolean checkName(String userNameText) {
        int len = userNameText.length();
        return !(len < 3 || len > 15) && !"_".equals(userNameText.substring(0, 1)) && !userNameText.matches("^\\d*$");
    }

    public static boolean checkPassword(String pn) {
        return pn!=null && !"".equals(pn);
    }
}