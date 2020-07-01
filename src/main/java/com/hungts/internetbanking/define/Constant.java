package com.hungts.internetbanking.define;

public class Constant {
    public static final String ACCOUNT_NUMBER_PREFIX = "53210000";
    public static final String DEFAULT_PASSWORD = "123456aA@";
    public static final long OTP_AVAILABLE_TIME = 5*60*1000;
    public static final String BANK_NAME = "30Bank";

    public static final class AccountType {
        public static final Integer SPEND_ACCOUNT = 1;
        public static final Integer SAVING_ACCOUNT = 2;
    }

    public static final class DebtType {
        public static final Integer MY_DEBTORS = 1;
        public static final Integer MY_DEBTS = 2;
        public static final Integer ALL = 3;
    }

    public static final class DebtStatus {
        public static final int PAID = 0;
        public static final int PENDING = 1;
        public static final int CANCEL = 2;
    }

    public static final class TransactionType {
        public static final int TRANSFER = 1;
        public static final int DEBT = 2;
        public static final int PAY_IN = 3;
    }

    public static final class TransactionStatus {
        public static final int SUCCESS = 0;
        public static final int FAIL = 1;
        public static final int PENDING = 2;
    }

    public static final class EmailConfig {
        public static final String HOST = "smtp.gmail.com";
        public static final String PORT = "587";
        public static final String USERNAME = "hungtspubg@gmail.com";
        public static final String PASSWORD = "Tau0biet";
        public static final String SUBJECT_RESET_EMAIL = "Group 30 Internet Banking - Email reset mật khẩu";
    }
}
