package com.hungts.internetbanking.define;

public class ContextPath {
    public static final class User {
        public static final String USER = "/user";
        public static final String CREATE = "/create";
        public static final String INFO = "/info";
        public static final String SAVE_RECEIVER = "/save-receiver";
        public static final String LIST_RECEIVER = "/list-receiver";
        public static final String LIST_ACCOUNT = "/list-account";
        public static final String UPDATE_RECEIVER = "/update-receiver";
        public static final String REMOVE_RECEIVER = "/remove-receiver";
        public static final String CHANGE_PASSWORD = "/change-password";
        public static final String FORGOT_PASSWORD = "/forgot-password";
        public static final String RESET_PASSWORD = "/reset-password";
        public static final String SAVE_DEBTOR = "/save-debtor";
        public static final String LIST_DEBTS = "/list-debts";
        public static final String CANCEL_DEBT = "/cancel-debt";
        public static final String PAY_DEBT = "/pay-debt";
    }

    public static final class Account {
        public static final String ACCOUNT = "/account";
        public static final String INFO = "/info";
        public static final String PAY_IN = "/pay-in";
        public static final String TRANSFER = "/transfer";
        public static final String CAPTURE_TRANSFER = "/capture-transfer";
        public static final String LIST_TRANSACTIONS = "/list-transactions";
    }

    public static final class Employee {
        public static final String EMPLOYEE = "/employee";
        public static final String CREATE = "/create";
        public static final String INFO = "/info";
        public static final String UPDATE = "/update";
        public static final String DELETE = "/delete";
        public static final String LIST_EMPLOYEE = "/list-employee";
    }
}
