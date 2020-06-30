package com.hungts.internetbanking.define;

public class Constant {
    public static final String ACCOUNT_NUMBER_PREFIX = "53210000";
    public static final String DEFAULT_PASSWORD = "123456aA@";
    public static final long OTP_AVAILABLE_TIME = 5*60*1000;
    public static final class AccountType {
        public static final Integer SPEND_ACCOUNT = 1;
        public static final Integer SAVING_ACCOUNT = 2;
    }

    public static final class EmailConfig {
        public static final String HOST = "smtp.gmail.com";
        public static final String PORT = "587";
        public static final String USERNAME = "hungtspubg@gmail.com";
        public static final String PASSWORD = "Tau0biet";
        public static final String SUBJECT_RESET_EMAIL = "Group 30 Internet Banking - Email reset mật khẩu";
    }
}
