package com.hungts.internetbanking.define;

public enum ResultCode {
    SUCCESS(10, "Thành công"),
    INTERNAL_ERROR(11, "Lỗi hệ thống"),
    INVALID_DATA(12, "Invalid data"),
    MISSING_REQUIRED_HEADER(13, "Request thiếu header"),
    SOURCE_ID_DOES_NOT_EXIST(14, "Source id không tồn tại"),
    ACCOUNT_DOES_EXIST(15, "Account đã tồn tại"),
    ACCOUNT_DOES_NOT_EXIST(16, "Account không tồn tại"),
    PENDING(17, "Đang chờ xác nhận"),
    INVALID_AMOUNT(18, "Số dư không đủ"),
    FAILED_TRANSACTION(19, "Giao dịch thất bại"),
    LIMIT_AMOUNT_PER_DATE(20, "Đã đạt hạn mức giao dịch trong ngày"),
    LIMIT_AMOUNT_PER_MONTH(21, "Đã đạt hạn mức giao dịch trong tháng"),
    AMOUNT_LESS_THAN_MINIMUM(22, "Số tiền giao dịch nhỏ hơn mức tối thiểu"),
    BAD_REQUEST_FORMAT(23, "Định dạng không đúng"),
    INVALID_SIGNATURE(24, "Chữ ký không đúng"),
    REQUEST_EXPIRED(25, "Thời hạn của request đã hết hoặc không chính xác"),
    REQUEST_ID_DOES_EXIST(26, "Request id đã tồn tại"),
    TRANSACTION_ID_DOES_NOT_EXIST(27, "Transaction id không tồn tại"),
    TRANSACTION_NOT_PENDING(28, "Transaction không trong trạng thái chờ xác nhận"),
    CAPTURE(29, "Commit"),
    REVERSAL(30, "Đã huỷ");

    int code;
    String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ResultCode findByCode(int code) {
        for (ResultCode resultCode : values()) {
            if (resultCode.code == code) {
                return resultCode;
            }
        }
        return INTERNAL_ERROR;
    }
}
