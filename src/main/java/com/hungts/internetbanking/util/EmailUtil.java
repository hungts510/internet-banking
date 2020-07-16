package com.hungts.internetbanking.util;

import com.hungts.internetbanking.define.Constant;
import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.model.entity.Transaction;
import com.hungts.internetbanking.model.info.UserInfo;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class EmailUtil {
    private static void sendHtmlEmail(String toAddress, String subject, String message) throws AddressException, MessagingException {

        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", Constant.EmailConfig.HOST);
        properties.put("mail.smtp.port", Constant.EmailConfig.PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Constant.EmailConfig.USERNAME, Constant.EmailConfig.PASSWORD);
            }
        };

        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(Constant.EmailConfig.USERNAME));
        InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        // set plain text message
        msg.setContent(message, "text/html; charset=UTF-8");

        // sends the e-mail
        Transport.send(msg);
    }

    public static void sendResetPasswordEmail(UserInfo userInfo, String otp) {
        String message = "Xin chào, " + userInfo.getFullName() + ".<br>";
        message += "<b>Mã OTP sử dụng để reset mật khẩu của bạn là: </b><br>";
        message += "<h2>" + otp + "</h2>";
        message += "Mã OTP có hiệu lực trong vòng 5 phút.<br>";
        message += "Để đảm bảo an toàn. Vui lòng không chia sẻ mã này cho người khác!";

        try {
            sendHtmlEmail(userInfo.getEmail(), Constant.EmailConfig.SUBJECT_RESET_EMAIL, message);
        } catch (Exception e) {
            throw new EzException("Send reset email fail:" + e.getMessage());
        }
    }

    public static void sendTransferOTP(UserInfo userInfo, Transaction transaction) {
        String message = "Xin chào, " + userInfo.getFullName() + ".<br>";
        message += "<b>Bạn đang thực hiện một giao dịch chuyển tiền đến tài khoản " + transaction.getToAccountNumber() + ". Ngân hàng: " + transaction.getToBank() + "</b><br>";
        message += "<b>Tài khoản của bạn sẽ bị trừ: " + transaction.getAmount() + "đ. </b><br>";
        message += "<b>Mã OTP sử dụng để xác nhận giao dịch của bạn là: </b><br>";
        message += "<h2>" + transaction.getOtp() + "</h2>";
        message += "Mã OTP có hiệu lực trong vòng 5 phút.<br>";
        message += "Để đảm bảo an toàn. Vui lòng không chia sẻ mã này cho người khác!";

        try {
            sendHtmlEmail(userInfo.getEmail(), Constant.EmailConfig.SUBJECT_TRANSFER_EMAIL, message);
        } catch (Exception e) {
            throw new EzException("Send transfer email fail: " + e.getMessage());
        }
    }
}
