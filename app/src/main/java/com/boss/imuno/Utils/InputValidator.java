package com.boss.imuno.Utils;

public class InputValidator {
    public static boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public static boolean isPasswordValid(String password) {
        String regex = "^(?=\\S+$).{8,}$";
        return password.matches(regex);
    }
}
