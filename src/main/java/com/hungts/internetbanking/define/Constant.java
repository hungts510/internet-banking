package com.hungts.internetbanking.define;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Constant {
    public static final String ACCOUNT_NUMBER_PREFIX = "53210000";
    public static final String DEFAULT_PASSWORD = "123456aA@";
    public static final long OTP_AVAILABLE_TIME = 5*60*1000;
    public static final String BANK_NAME = "30Bank";
    public static final String BANK_SECRET_KEY = "30Bank";
    public static final String SDF_FORMAT = "yyyy/MM/dd HH:mm:ss";

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
        public static final int ALL = 4;
    }

    public static final class TransactionStatus {
        public static final int SUCCESS = 0;
        public static final int FAIL = 1;
        public static final int PENDING = 2;
    }
    
    public static final class NotificationStatus {
        public static final int NOT_READ = 0;
        public static final int READ = 1;
    }

    public static final class EmailConfig {
        public static final String HOST = "smtp.gmail.com";
        public static final String PORT = "587";
        public static final String USERNAME = "hungtspubg@gmail.com";
        public static final String PASSWORD = "Tau0biet";
        public static final String SUBJECT_RESET_EMAIL = "Group 30 Internet Banking - Email reset mật khẩu";
        public static final String SUBJECT_TRANSFER_EMAIL = "Group 30 Internet Banking - Xác nhận giao dịch";
    }

    public static final class UserRole {
        public static final Integer ROLE_CUSTOMER = 1;
        public static final Integer ROLE_EMPLOYEE = 2;
        public static final Integer ROLE_ADMIN = 3;
        public static final Integer ROLE_DISABLE = 4;
    }

    public static final class ExternalRequest {
        public static final class Header {
            public static final String PARTNER_CODE = "Partner-Code";
            public static final String SIGNATURE = "signature";
        }
    }

    public static final class PartnerType {
        public static final String PGP = "PGP";
        public static final String RSA = "RSA";
    }

    public static final class PartnerName {
        public static final String BANK25 = "Bank25";
        public static final String PGP30BANK = "PGP30Bank";
        public static final String BANK34 = "Bank34";
    }

    public static final class PartnerAPI {
       public static final String BANK_25_ACCOUNT_INFO = "https://bank25.herokuapp.com/api/partner/account-bank/destination-account";
       public static final String BANK_25_RECHARGE_ACCOUNT = "https://bank25.herokuapp.com/api/partner/account-bank/destination-account/recharge";
       public static final String BANK_34_ACCOUNT_INFO = "https://banking34.herokuapp.com/api/user/";
       public static final String BANK_34_RECHARGE_ACCOUNT = "https://banking34.herokuapp.com/api/transfer/update";

       public static final String BANK_30_SUB_ACCOUNT_INFO = "http://localhost:9808/api/account/info";
       public static final String BANK_30_SUB_RECHARGE_ACCOUNT = "localhost:9808/api/account/recharge";
    }

    public static final class PartnerSecretKey {
        public static final String BANK34_SECRET_KEY = "nhom34banking";
    }

    public static final String SOURCE_PASS_PHRASE = "12345";
    public static final String DEST_PASS_PHRASE = "123456";
    public static final String SOURCE_USER_EMAIL = "hungts@test.com";
    public static final String DEST_USER_EMAIL = "hungtsdest@test.com";
    public static final String BANK25_USER_EMAIL = "25bank@25bank.com";
    public static final String BANK34_USER_EMAIL = "ronin32014@gmail.com";

    public static final String RSA_PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIIEowIBAAKCAQEAk1AOEwhvRWXC+um7CibbbwMLlCn8IwR4O5yF9LFTGYFiYTEd\n" +
            "gJAeICpiCSAV6fyTtbiWGRuVLSm8yC5jFyXW7ziwfGaNP4myxhMa/9RBK7Fippzb\n" +
            "BIRejJLqc0BsFTfOzob2IP5rFiso9MpmgbLjhIgLDdHJBqegLVQcQc6N7MRl/XFz\n" +
            "vvLobiX+6Pry2d0o5WCghv2oyba2FSJhfmVAGhb6SkqHVDth5jhwBxEo385zRvVo\n" +
            "oJRPOwAOm1EkdffNWU7mm5CgqFqxvD2bsDLCF9lq+ApEstSCFkOWRkbPCkHJp4cK\n" +
            "DpNNv934baeYzp4OBUwe2k+ExoMmpjMzYU3TWwIDAQABAoIBAQCPOVhVvO+iQ0wM\n" +
            "8848EodqKUXKqf9QdHoJSycaPoWS2K6z3DLY2kXP9CUYxi9ILZ/XID4e7yhcnqXC\n" +
            "4wbIbKvKLg25BWPzEIfV+KFZdh5ewrotT3fJroDWn8h+gXYUMwxI1ALdFT88cH4D\n" +
            "cf2zwB9grXxB1WNjqvoa2BoZCSAHv3hx21jacvbSotw4rdg7eTpKiH6lN7KqcTk6\n" +
            "y/8ozY0Llr/WuTUi8Nw4Kr/e93PZiyLo3IBREEvtbayGa/42EA1Jw96ho2TiwVhJ\n" +
            "NlyGQs1CWBwNfzWEpu/iD8xyTxnTzIaQUhHNU7Axw/sKVDvM+/8xs0coLFw3sCmL\n" +
            "UV60VN4hAoGBANPwT/XBizDZVRMJ7nZ9JdS+oT6QSSL38JQ3b+miA44F8/EOJ+rX\n" +
            "/0v7XxAsUVu63Rpo2qVci8c41A/h+lLJ7acD3LVPbae9z3uRaE1hN/E5Q1GKYfpQ\n" +
            "Xv7ofSTlipuD77wpmpFR3Nse2xcrB7SmfG2DcxbIV5tFaBeeSCx5La+ZAoGBALHw\n" +
            "QJ5TPXY7IEy5UHISUYdzSipcHl+khNysTG9O19c+8ToVka9slO2uEs2mw0sgn7CE\n" +
            "j3K8lgfikfj4YgKC0SQh/62r4Nzphbotw3fls9tucl1BvSNr5hgmtpxwbhzWTbQ/\n" +
            "TR7530kex91akrBEarNDWLPgnewLFpHr955NkwMTAoGARId042sknbP4fUJ919RZ\n" +
            "kjPdoYQ4EdWH9fUC+9GGQrsMCMrh/16+TwxFzc32tKl0auCyqjuFevKWJ1iTf4eJ\n" +
            "j7KBACNhupZ2a2c2CadA1oAEk24ihjTMsrHsHS0Xp3d+4iG1t/13Y2bTwucIA9K9\n" +
            "cw/I+/nl0fAcW16zyaWZLykCgYBHHQ22OO5HYjn2b9mGvWt4BsuNQmOhNc8jonip\n" +
            "w4jfrXgbDqO6yFp84yUAq7VBQTiRA2mRIW2UmShdqGcDOeT729qjPh4QLAQV9FQ6\n" +
            "hsLq36k1PCCrF2ROIqq4uvZG8B79+o6H4rUqM/MMtDYL3EwIeNHqkcqLm6LZjsTX\n" +
            "r4khoQKBgEgxVu2NnXWUQ0t0LXUvkO7TnyevNJ/riCDcW0ASQolXkHxqSX7CXmMY\n" +
            "AWOz6vPafMIEW1zoko32gwWWle7hFcgM/9GFHQ02o3O3edSTUb1SNdO3uLATPDGt\n" +
            "keiifTGOPSfYmJJkDUgsguQmAHTLzNMzg/8BqaZoNWCTf4E0OVCu\n" +
            "-----END RSA PRIVATE KEY-----";

    public static final String RSA_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk1AOEwhvRWXC+um7Cibb\n" +
            "bwMLlCn8IwR4O5yF9LFTGYFiYTEdgJAeICpiCSAV6fyTtbiWGRuVLSm8yC5jFyXW\n" +
            "7ziwfGaNP4myxhMa/9RBK7FippzbBIRejJLqc0BsFTfOzob2IP5rFiso9MpmgbLj\n" +
            "hIgLDdHJBqegLVQcQc6N7MRl/XFzvvLobiX+6Pry2d0o5WCghv2oyba2FSJhfmVA\n" +
            "Ghb6SkqHVDth5jhwBxEo385zRvVooJRPOwAOm1EkdffNWU7mm5CgqFqxvD2bsDLC\n" +
            "F9lq+ApEstSCFkOWRkbPCkHJp4cKDpNNv934baeYzp4OBUwe2k+ExoMmpjMzYU3T\n" +
            "WwIDAQAB\n" +
            "-----END PUBLIC KEY-----";

    public static final String BANK25_RSA_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlgYOdnw1EBNhzIiKP3Ep\n" +
            "ieY2sOhHhUYAdTKn7/kXX0DXdEdWU4Jnkkv6F8dtLhkGn6wL/tMsPuuLlms3ntoO\n" +
            "OfPyq3YCD6gpnVb2ns7058dI83AQMPEq8KLlf2JHbxOHIgdnhi8HF/q9D48eJR3m\n" +
            "V1BEOHzNpjN/URZ/1cF7x/FEAls5esotYle3NDeP31qIxGT/QSbEknBFwrDY73yj\n" +
            "BqRp/2nJ1ns6Nz2YFxlT/W8PLaq9g5rOh3HGg7bO8IuK8RubQqnSSEFOVShvNzmb\n" +
            "Q9G9IqqMyggY21r0Ft3e6WyntluVxIzVd8KkY9Gni/vWYC3MXTiGDLG0ABYnT44s\n" +
            "HwIDAQAB\n" +
            "-----END PUBLIC KEY-----";

    public static final String BANK25_PGP_PUBLIC_KEY = "-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
            "Version: Keybase OpenPGP v1.0.0\n" +
            "Comment: https://keybase.io/crypto\n" +
            "\n" +
            "xo0EXwtXJwEEALIYVpyK1WlpIIGO7kjEtpHyzjhlbz0Vs9YNxYKr625CsipOIxhW\n" +
            "874znOfo/EIqL0YAoOEiKUQAlCTYL4oaaVcrMbZ2t9JMSiJ0FZrroHCSCyiPYSSH\n" +
            "MhBVIYC507EBUBLgcCnSqGzXyOhk5OxLxTc2bnc0iGhbz84Pm/eEyO8vABEBAAHN\n" +
            "GjI1YmFuayA8MjViYW5rQDI1YmFuay5jb20+wq0EEwEKABcFAl8LVycCGy8DCwkH\n" +
            "AxUKCAIeAQIXgAAKCRDDuvEG5H/yCN4uA/4xA+bf4C8MYgarT0oleNcOQ/MnSIdj\n" +
            "xm/dvVzI4QEbSnN8YkedXksYL8vUZn1/S8YqYXUSot+rmtC+0x993ZOOFMjqtVCW\n" +
            "rh3xHWOj+mIGmYY4P4sfqwiPG7Btn3KMgJv2Hcq37n7PMRXahP0zZ7QerK0Gf+AA\n" +
            "HG4C6xXlMYO1h86NBF8LVycBBADjIfWLIBj99E0pC7ym2NXYEU2HxGGx7lTqX2Cp\n" +
            "OtQpS4YlcLMsOicQdM8ccVOzGguY3n7bno/sl8qkpIM9FbsgeJqPTkSV/0ISBneI\n" +
            "X4q2LcIyRPqG8r9xCqGYGRHbt6PJZQTMojK0owvgGYEWus6kEwKFoMhv3E9qKLnv\n" +
            "nx/OHwARAQABwsCDBBgBCgAPBQJfC1cnBQkPCZwAAhsuAKgJEMO68Qbkf/IInSAE\n" +
            "GQEKAAYFAl8LVycACgkQ6xu/je4jYIAZHAP/cb7wSIglqNWG9XMCWCtHkphm65HB\n" +
            "IufMxPIne4mLopLqHu0u8Xk3L8LcA/GFqJmOd6f8qiHuWeHA1vngnyhvNflWE11g\n" +
            "tBmcHM8YtIC6rRxlfNeg2uAy14VNIjp3vkl62u9XtbDFylzXuRbElk9oYIqoxm+U\n" +
            "x0g2G7S2OEO0vHVuQAP+O0RIU7YYtAyRJzk/C5fEcNACft66+0T2SyNbTjCeKNzz\n" +
            "y6eHx77zmsmSsdjVHe1udzNaNRzkNLGSfH3zsiP5QsR9wtRNu0QUyY6pTN1CHGtm\n" +
            "x+ggn/65sAA3CTjswzoyPcWEE7+QPDPrRISNq4CeKoXRQ7RpYQi6UMKWKGirsdbO\n" +
            "jQRfC1cnAQQAzKGNR8oILAOZgNWtIjK6sAUu2zYO3ssDuHNB/JXxIbUEfmfwDCbC\n" +
            "wT8fFFmwgP0OOqUvfvRTgkeb+UqST07Odx/+vQZCONEwZy9p/xMMAtO72cmtU7hB\n" +
            "TIsAw3sXasjUQFRAJwGlRgsgXQvGJzeHzB2UTyMra/kpj7tq9BdvzDEAEQEAAcLA\n" +
            "gwQYAQoADwUCXwtXJwUJDwmcAAIbLgCoCRDDuvEG5H/yCJ0gBBkBCgAGBQJfC1cn\n" +
            "AAoJEAPt/kJy6w689EMEAJ0fxI2bqQI4ksptszqcmf8aHAPENnJmArOOyc+AQ/ts\n" +
            "4c2wopVazdT/Cai9x5uXjgqMc4OVplVhOPw4XYfdynFHp6iJhlptT2QeXSLKuApy\n" +
            "Y7s+qRc19CureantXLXE5DgVSrCjbY4AGvNQoJV1FzYNhh0W3S1OQmGnWWR8vugp\n" +
            "IcUD+QF8F3wZeSnqL50XvraPsghJrbuYtimSFLr48igH5IujlfFc6ukQxy7MFm4B\n" +
            "xOhsYCCpwb7r5fmiOtzJ1QGQmz3q0+7Wc8mqsTP3SELpy1dXuqVK0Z8hyJD9ylXa\n" +
            "Vpoww1AiiZU1UdaIDNcyOngZ50z9jD18f2xwk3+I1Vtub7FE\n" +
            "=EsMv\n" +
            "-----END PGP PUBLIC KEY BLOCK-----\n";

    public static final String BANK34_PGP_PUBLIC_KEYS = "-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
            "Version: Keybase OpenPGP v1.0.0\n" +
            "Comment: https://keybase.io/crypto\n" +
            "\n" +
            "xo0EXsaK0AEEANKBWGQRobFMrwyuELsALiyhz+HJDgf+hiPF+4QXC1YCj5VVBlvR\n" +
            "K/JE2hs1gVjsAV6nDMcfgMbaguCPb5u3lp8ir/88qQOdyYMLYbUL5L4ir1MbfNhw\n" +
            "TpMMeCuXwoed/F2BTtgloedCkwmthcaEcrWLHDiV/MEYyq6V1kjHl0cDABEBAAHN\n" +
            "HkR1YyB0YWkgPHJvbmluMzIwMTRAZ21haWwuY29tPsKtBBMBCgAXBQJexorQAhsv\n" +
            "AwsJBwMVCggCHgECF4AACgkQCHaebKSUPRZ/zQP+M+zmhUMyWWNiU2yOZDRUsFHS\n" +
            "3qevkwHLIWHlvrq2rP44gPAk2Phvw7htYK19adJvoEAxFyDH66awha+yWNPi9uFB\n" +
            "3Tvkbasas2wTf0RZbB0QGnprBTqqf0a+Kj3PfifK/jFvm0zqXoXU5Yme7Xj5alJt\n" +
            "nvIL/+XKVsOoFMo2h7rOjQRexorQAQQAtFteY9E1lhsBJlD1uX2FnOIjkJoUNQ0o\n" +
            "hvHaP5ynsFivgKbjrTM5YSgdv8TopJeNqByUk991oLtimeITUQ9BLpWFkvCoQ+ey\n" +
            "pYk1eG1Nz4UhoGpYMPrT+nqSd6vz7pK+dQcJ7ZzPcgFSc/FtZZ9npGqc0IByqDiM\n" +
            "pn1RPB1GnNsAEQEAAcLAgwQYAQoADwUCXsaK0AUJDwmcAAIbLgCoCRAIdp5spJQ9\n" +
            "Fp0gBBkBCgAGBQJexorQAAoJEK+nKFfkuN+JodQEAIjlPDTpVNl2tgzUK7sxHNzC\n" +
            "kPlXJMtIaaXvWV5FSTOh/SNcpFpcUULgQ1cOyH8T5uzekNnxaJcoz3FajqJNpqGK\n" +
            "cgo+ESIJPoNxbkajM0uRkUYuWLzrrKDs8JBGRilQX1170sW+WhbZHs/GanSS5AEZ\n" +
            "jwM87cxvx2+/JnPugSTq/isEAKFRVG7U34pxKAwb4yp2po81zMPhTMnN3vlxtDDG\n" +
            "Ue+wBwIJWFDnvEhOyeDGWnCEyRlemTaNXVUcDfGYM8JS5Kwn9cfxyNDQ6ujG5mBP\n" +
            "w61c+Sh5wPPe8ZnsAlAiHmryCRY/K1pGQsCEsaVoUzLlRkpW8/A1JZYizpz9TxSV\n" +
            "NV3+zo0EXsaK0AEEAMdQbesY9hpC8lr/PrCXMSz61A5NFlbFq4O/Qc1KdthZTgdm\n" +
            "RRAnjzR2soq2kb2IEtMVY1Dgj+4vbtBbovIRbBegbhpcTQndKFRizDuCBbaFQmwj\n" +
            "3Ske/mqT5o88cy+ZhTI11BADLIo9L9NV0aJUjchDqXx97iKzAtbb/ce6xisRABEB\n" +
            "AAHCwIMEGAEKAA8FAl7GitAFCQ8JnAACGy4AqAkQCHaebKSUPRadIAQZAQoABgUC\n" +
            "XsaK0AAKCRD8JTRgXpBFOFU0A/wMOuI4OPA/aYhcvLa/o2A46KHvfK6Ugr3zt3f/\n" +
            "YW4DM1ZnadxAGOm1OVdaXCYpYHwL6U+Ndnr65pQkPXhISv9WSUR89bbKRfGQxx2L\n" +
            "AWfOnBxoAU+U8kbXkZWhwENQK8vEHBDm3xRRFlLp56M4SardG9xRI3HKwBQfvPaE\n" +
            "VCOKRcYyA/9jchZAQjEgyobBfDhDdWRNAbYUMfIcvFgfnXIvYnnFQI6h1m7SlisE\n" +
            "CaXJe8SNXyBlK+0EkVtdnbQyroWi4yzT/4Wgb4CGsraH2ZW7JkjoYm/XdgolYKEM\n" +
            "2DjM88WcSaX+3NPdMOSUNuCIfHit/2AxghMcCwpNkvlAz52UjySOag==\n" +
            "=sMbW\n" +
            "-----END PGP PUBLIC KEY BLOCK-----\n";

    public static final String SOURCE_PUBLIC_KEYS = "-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
            "Version: Keybase OpenPGP v1.0.0\n" +
            "Comment: https://keybase.io/crypto\n" +
            "\n" +
            "xsBNBF7+1GcBCACaZRxRqp8s5WvYOE2xWADuYvHl2r7HiEoQ7JFoamL+wYdij9cs\n" +
            "OHpViu/TzdsdgX+4Hdh8jHWRhFvYMpqudOLPExC0fdh418wtzh6CzPa5P/rgIrrp\n" +
            "jZ7shjLmxeOGjZDq3Jd+RRXjdF+A4bwRCZphitfdUc+Guerlr4d30dcp04lxa/R4\n" +
            "/9WczoKDbiiN+uBRkAIACrksp2D4uLb4w07uBQbyajP4KZSWpxpsPPEiVIm3YuPi\n" +
            "ri4gYtbnscanwd/LjX47cpd1sZuxW70uqqQAQ9aDwtalgwIo2xW5FNQYRKulKM0u\n" +
            "TsjEtaX4iMtSgZsk6CvXxwyhx7OPeA31VXNXABEBAAHNFnRlc3QgPGh1bmd0c0B0\n" +
            "ZXN0LmNvbT7CwG0EEwEKABcFAl7+1GcCGy8DCwkHAxUKCAIeAQIXgAAKCRBwGVIY\n" +
            "DFyn2GuwB/9hnUyvkp0TH06jYarHWJZztIOu6/x+JP5jvJ/v2IlDSvRYTPSf38x0\n" +
            "YkzdDAWK5QvaQGgicU5oCUr7ChkdanqSJucZMrmJS9GFssAx1u79xtGjSqyyQdQI\n" +
            "qjMrexV/lDAtBFjI3LDCvcnW5s4sB48kRqZHppvmk7OLix0nZ6TGlZnkwAnw8anK\n" +
            "q7TFZmuQOnwqAbW32Hv7WIpDX4R3LhccReCV1khFobJie/pki2+YvUqrlY6+zQC8\n" +
            "fKUPZ/NMF9wYo39wU4Hx3x3nfHkauR3aGYA7r83LLsFKW4J+ICBJ/gehaPRWJwKM\n" +
            "8RhI11mIEPZ+Ou17pwVuM95fb6GXSwtAzsBNBF7+1GcBCACfHLkinv8lYbrwypG6\n" +
            "4LqNp2ecJtU9ni9G4pG8uwsSV6AM/PIwqsyGBmZmVb6PgT7vtcOqOh1hPFvqOd13\n" +
            "3PnUHMyufK7e4haxWYdvRHrpglq44xGszrkx1XfVTLvtvmzVwgxCZl/34xk2DR04\n" +
            "LLaus8RNHFGGLku68TwhzW+NV/UXxmtNJVGMm/Oc/US/Db91ihklPwrESLfLRqFw\n" +
            "NoqHVA3TifGNd4Q84L+GqiY/s9Kt9ykFHXBaztXK4qoa86qp++zJS77KaAtJvR8j\n" +
            "1TlSk01uXAg+6r1MBk4BVaJ4sDAHtvwUgdWYDH9UHuFUjlEYW/1NovdCikqOkSKF\n" +
            "1wftABEBAAHCwYQEGAEKAA8FAl7+1GcFCQ8JnAACGy4BKQkQcBlSGAxcp9jAXSAE\n" +
            "GQEKAAYFAl7+1GcACgkQd8ZTiLjTTyleTQf+LB/VGnewz8xoSGJsDxV38FeDcrO2\n" +
            "olRJ7vVVNvQpLsqgybsFKMy66UDQoxwvaE6eNYTgif9XEa/4fExvVS9FXYd7f1c9\n" +
            "FHQdBOOL9JCVCs3rCrHus3d9Z74R3JAWzY59d/cgxcNgu4ldiX5UPVjJ0D43Husk\n" +
            "A/qDlLEq5u9CTGJakm+xTcah+uRDcnBcn0N9+uXBP4i2LM7FU/u8pPQG1H6lnBbn\n" +
            "D9g9lUoV6TdlwT+kAT/zG5ia01sev+lHQJACITghLTenBeOMlO5ulz1I+K1+nQl8\n" +
            "JCi3wDcDF/ogFlAq7AB3FJp/OI8sTMFodLyBJTfRL2XOSXXirbUIeWVLZrh+B/9r\n" +
            "y26UoxcFUuzaNLrX+xtgYke4Wt0zoR0Ae3jiGRcpGHqLHwucVqc0c6V0RixUGSw1\n" +
            "jnFwngZu5wdc+RzsQJoQxxRckmA2Dy32yTuljDrgg+++R8ykOxFiKVoNxLiEONGL\n" +
            "mddgL6CNUMdgnVkEj0aOHcdm/vRLRxBTA81JJOS2yo8PG1fZ4qkutV62nGY5pGWn\n" +
            "LigS0xgECrRrXfRo4a/OT/z20HKuuS5BQtf5XDpVMGfVbTQ+AIsaCyBTR/qAS8wK\n" +
            "g3YuQqCI5MzuBYEIqasC+oYjh5SJb8TBmy8z+0kj0McOmB9vuA0K0ZJdCA10oolr\n" +
            "jb1+jQm9LHFcktp5xYhDzsBNBF7+1GcBCACitbTPqu7vx03aGLlNKfCy/qbMO9Jg\n" +
            "O4eVeV+BcSqd8cBnd2Rnv+e87dPTctHzaeCk2vjolyLEP9cHChZqZvBdk1CPIA67\n" +
            "4anNI0t5xGotwXCfbqQ95cpCvtnGimwjhvr+6CAD5kqVt1Bd61OrdML0g1oh8WSd\n" +
            "QrBEUHJCMhubxIAGrffkE6rkN+iPfeo88vsc2ZRoykYnBLCrkhSmfDwFzl+uLKny\n" +
            "+jAqG+fo2PKoA8znSJWjukVGoCMLg4tWHbyzcV6f0FkS7fmW3ZcV/8yG3VkNuXn2\n" +
            "vXXfWkcZ98bQtBPMXPFFZHi7oo/W31x458lv2MuF3yxG165U944tds87ABEBAAHC\n" +
            "wYQEGAEKAA8FAl7+1GcFCQ8JnAACGy4BKQkQcBlSGAxcp9jAXSAEGQEKAAYFAl7+\n" +
            "1GcACgkQ35tg+Rag/66fqggAgHNUlN6XY3OZ8NXEfOXQwxbH9Jg/CzjoA6ntYLQO\n" +
            "RmOnXOd809YUMoR0s62VuDhuB9MxycqPQpMoIPETElZri9RuPqsacJGMqyp1TsIf\n" +
            "s0rzAsdLDpttBn26baTp0JQFJtud4jYONkRlsIXQGZTsdwxMZEu0v3VMBmQI8CIr\n" +
            "zOpumvFAZ7S1hNBeAUvypeG+ouRB1q5ouiUk8zR687dN9MG/Xu4oAcgWXevQx8jQ\n" +
            "OXhJtIY3inYcbvxs1zGka+Ze8zGar0M//z0hSri1gI4jxIMaa5yVYKRbGBGb0ilf\n" +
            "YYsdtkknWOSeBU9bo2W8yonSE5enMSc+A5+GxIxusjHqst4YB/9skgT88UvPDVLO\n" +
            "ztq+a85Ldu2KRV2fiDD8YhaIIo50muBGFGj9SUeZPGuX6VjsLXDZokuN1bJ0itqN\n" +
            "q7Hn8MD2GWAQseU5Xe//5B6p9nWuMFt+tw1OxeuEVLD0ZmMGnxkfokCN4gcYxO19\n" +
            "xMWJCQcxBm1O5IENKtF1JKPNHguC7pcbIXrVpnN19hm0aa17eQpw2wvhraiaXkjk\n" +
            "KeeUtUpDPT3HGV+XTAtmF5Isa0NuQGpjWlhtfGvrWa+422rrLXlY3T90nswzElBX\n" +
            "SP9nrLn97PFG1fHhykKJaTuRMElHJpjxtIB6v8P+2Hk1/tm7ksAHAzKyyh8NZ707\n" +
            "g9MUL847\n" +
            "=o6oi\n" +
            "-----END PGP PUBLIC KEY BLOCK-----\n";

    public static final String SOURCE_PRIVATE_KEYS = "-----BEGIN PGP PRIVATE KEY BLOCK-----\n" +
            "Version: Keybase OpenPGP v1.0.0\n" +
            "Comment: https://keybase.io/crypto\n" +
            "\n" +
            "xcMGBF7+1GcBCACaZRxRqp8s5WvYOE2xWADuYvHl2r7HiEoQ7JFoamL+wYdij9cs\n" +
            "OHpViu/TzdsdgX+4Hdh8jHWRhFvYMpqudOLPExC0fdh418wtzh6CzPa5P/rgIrrp\n" +
            "jZ7shjLmxeOGjZDq3Jd+RRXjdF+A4bwRCZphitfdUc+Guerlr4d30dcp04lxa/R4\n" +
            "/9WczoKDbiiN+uBRkAIACrksp2D4uLb4w07uBQbyajP4KZSWpxpsPPEiVIm3YuPi\n" +
            "ri4gYtbnscanwd/LjX47cpd1sZuxW70uqqQAQ9aDwtalgwIo2xW5FNQYRKulKM0u\n" +
            "TsjEtaX4iMtSgZsk6CvXxwyhx7OPeA31VXNXABEBAAH+CQMIeyAccQlbny1gUHQ0\n" +
            "pEe10VatLDjxsj3iAQCMZXRUZhLPVXQcnmjgfC4RJigp1/iNr2uHv48WBpqs1bCc\n" +
            "rREZvKX+taqxiTHBK9sacYdnnancMAK8/gAD06wBgFC0lpw5yOcp3+PH8AeBzQiY\n" +
            "s5ldxJKVsjPUBDdOvtyGiT121X6j1quV+AfXc8haf+StcQ6p87zXxNra1N5Uh8Vp\n" +
            "VTQ+NpW4EBC2zbOZPMpxCOla8GkdFGxgysJPrZicbBLn+9DMJv1ev6Iz1qLm4gQe\n" +
            "A00Ejw+iHFlUX0djgRxkUnc4/LNOhymHa1BcGFU1FmEHUJfKTdN5vr00MZcFq4wO\n" +
            "rtxdqQe7dPdR2g+kT3RUMutdEWWTLvvDAlw0LrQJXwVDxjSxqsAQO1FnKNFMGqkP\n" +
            "xJVVvamwdveQBu4oC05f48O+Tl1F3HYdxXmy1Nptw0e/bW+BrNGbWN7iwZOlRDr7\n" +
            "7Pv5OGcNIU44cweWxDSIk4Vogx+Rr7hOqej/MIvi1aAJ9EqQEUiBuT4hfP8wCYhD\n" +
            "9gb2RXQhr4rkUVjysX1uNmS8ulqPhHNr+aFayP7BAmXPYudZJdrgLiG8BeqyCOsy\n" +
            "0ZFBIfl1217O+NAsDbh6qgxgYx+Rnj0AC4vBeHe52Czn+LuIOoGytGOXrK4zAWrd\n" +
            "zszGMTr6PvM2ijZHy/VyIxppoTf9w1Xeg8mdjLdj5rb/lkkI464cHjpPqr2AWX/j\n" +
            "cBf1opDmqgCT2RrFZJsfwStdcCkQMfk14FcWbgnBnmQ+Qv5ns7PevSeJDD8y3GJY\n" +
            "m4MOgREJG6/VG03KjgLx/uKmX5OnyYCJjGH8Mx8/J5gaJ/EbNSuZV4nm1sxce3jr\n" +
            "BXs2ybftp73SuGkj0ZV4PSkuYZM0ZiQPcT/BrWuS6E71J2WyTqK9nffwHkHytJu0\n" +
            "DGI8xxjs0konzRZ0ZXN0IDxodW5ndHNAdGVzdC5jb20+wsBtBBMBCgAXBQJe/tRn\n" +
            "AhsvAwsJBwMVCggCHgECF4AACgkQcBlSGAxcp9hrsAf/YZ1Mr5KdEx9Oo2Gqx1iW\n" +
            "c7SDruv8fiT+Y7yf79iJQ0r0WEz0n9/MdGJM3QwFiuUL2kBoInFOaAlK+woZHWp6\n" +
            "kibnGTK5iUvRhbLAMdbu/cbRo0qsskHUCKozK3sVf5QwLQRYyNywwr3J1ubOLAeP\n" +
            "JEamR6ab5pOzi4sdJ2ekxpWZ5MAJ8PGpyqu0xWZrkDp8KgG1t9h7+1iKQ1+Edy4X\n" +
            "HEXgldZIRaGyYnv6ZItvmL1Kq5WOvs0AvHylD2fzTBfcGKN/cFOB8d8d53x5Grkd\n" +
            "2hmAO6/Nyy7BSluCfiAgSf4HoWj0VicCjPEYSNdZiBD2fjrte6cFbjPeX2+hl0sL\n" +
            "QMfDBgRe/tRnAQgAnxy5Ip7/JWG68MqRuuC6jadnnCbVPZ4vRuKRvLsLElegDPzy\n" +
            "MKrMhgZmZlW+j4E+77XDqjodYTxb6jndd9z51BzMrnyu3uIWsVmHb0R66YJauOMR\n" +
            "rM65MdV31Uy77b5s1cIMQmZf9+MZNg0dOCy2rrPETRxRhi5LuvE8Ic1vjVf1F8Zr\n" +
            "TSVRjJvznP1Evw2/dYoZJT8KxEi3y0ahcDaKh1QN04nxjXeEPOC/hqomP7PSrfcp\n" +
            "BR1wWs7VyuKqGvOqqfvsyUu+ymgLSb0fI9U5UpNNblwIPuq9TAZOAVWieLAwB7b8\n" +
            "FIHVmAx/VB7hVI5RGFv9TaL3QopKjpEihdcH7QARAQAB/gkDCNRG1rh7H0A1YIEj\n" +
            "8N2tAt3Snu0udKSsi/U1EXIC/03udbN/O8PPQ3TeFDZaudWCbn1Vi1cDQS0zDNQF\n" +
            "ZuzKHz/INByytNiDIyQyX++tk0Yk8TsKKExxeIGloowqAVh87NJHRSsfc8+TbmiW\n" +
            "IKbqiDPT/sc6xm9KcLZeoT4D1U9EmLtyTPqupqE4su3qMyHvZ/9uAOYRkGlaskcy\n" +
            "Npd/fGSLdpQ2j2VvPFtLDj+QkWN0IjPUXMGP9qsJa+Z2g7I7uD8qcmle9paJxILG\n" +
            "ivurksaqFLpHIitA4BuG48m418biIrwrOvqgrncWTL26VEPoEWSWGQreHqM+4Fe6\n" +
            "SmxDIHdAY/u9cRfG3FswLU7b3l7r1kIu7BYiMR8sJtZxLX5foybnDf1ZZUOFyXf/\n" +
            "s/fB52ewJeAVMSI0mSBd8HndCgqJdNznFXUnVlYevJpobjq2gpuwv4OwuEjEFUab\n" +
            "OU+fHl198dz+RolsOrXVzT9bRW3daLbS2xMsXMWWaR7nou0ufhItCT7Iy50vieoX\n" +
            "NN+FicbNlD8G+KwFHIbJFx9jkWpF3xY2R1JBoTbQpXZV2X8KuLHYJOPQbni+b04C\n" +
            "fDLK99O/kkxnsEXtU24DPJX29sxjSbVBpCzzV0XQanRNuVpA0hCM75ckMdFO0EUB\n" +
            "CLMHt7AxF9jOh76X/NinQwRHaTYKzfuM7DkGEAG/fHBS0+wll3y4g5T5IxhalG7B\n" +
            "bOz4WvjToXrIBnbZLpuiepkfr4KSO+oy7rWCXo/Zr9GdcMPDF5gTWx9WANOkoX6v\n" +
            "tcAcuvYChTzrxNH4gfbWpyXZXtpIdqcFbIbLQEPPOf4hiWi780hiIc4MsqRUvu+M\n" +
            "tgzgt8vaHuayfwdXt1SrqudL37r++HMdOT24uO69YkRxrCRzJsknLVDKWV42nmfy\n" +
            "lyIbTl0pOqhX38LBhAQYAQoADwUCXv7UZwUJDwmcAAIbLgEpCRBwGVIYDFyn2MBd\n" +
            "IAQZAQoABgUCXv7UZwAKCRB3xlOIuNNPKV5NB/4sH9Uad7DPzGhIYmwPFXfwV4Ny\n" +
            "s7aiVEnu9VU29CkuyqDJuwUozLrpQNCjHC9oTp41hOCJ/1cRr/h8TG9VL0Vdh3t/\n" +
            "Vz0UdB0E44v0kJUKzesKse6zd31nvhHckBbNjn139yDFw2C7iV2JflQ9WMnQPjce\n" +
            "6yQD+oOUsSrm70JMYlqSb7FNxqH65ENycFyfQ3365cE/iLYszsVT+7yk9AbUfqWc\n" +
            "FucP2D2VShXpN2XBP6QBP/MbmJrTWx6/6UdAkAIhOCEtN6cF44yU7m6XPUj4rX6d\n" +
            "CXwkKLfANwMX+iAWUCrsAHcUmn84jyxMwWh0vIElN9EvZc5JdeKttQh5ZUtmuH4H\n" +
            "/2vLbpSjFwVS7No0utf7G2BiR7ha3TOhHQB7eOIZFykYeosfC5xWpzRzpXRGLFQZ\n" +
            "LDWOcXCeBm7nB1z5HOxAmhDHFFySYDYPLfbJO6WMOuCD775HzKQ7EWIpWg3EuIQ4\n" +
            "0YuZ12AvoI1Qx2CdWQSPRo4dx2b+9EtHEFMDzUkk5LbKjw8bV9niqS61XracZjmk\n" +
            "ZacuKBLTGAQKtGtd9Gjhr85P/PbQcq65LkFC1/lcOlUwZ9VtND4AixoLIFNH+oBL\n" +
            "zAqDdi5CoIjkzO4FgQipqwL6hiOHlIlvxMGbLzP7SSPQxw6YH2+4DQrRkl0IDXSi\n" +
            "iWuNvX6NCb0scVyS2nnFiEPHwwYEXv7UZwEIAKK1tM+q7u/HTdoYuU0p8LL+psw7\n" +
            "0mA7h5V5X4FxKp3xwGd3ZGe/57zt09Ny0fNp4KTa+OiXIsQ/1wcKFmpm8F2TUI8g\n" +
            "Drvhqc0jS3nEai3BcJ9upD3lykK+2caKbCOG+v7oIAPmSpW3UF3rU6t0wvSDWiHx\n" +
            "ZJ1CsERQckIyG5vEgAat9+QTquQ36I996jzy+xzZlGjKRicEsKuSFKZ8PAXOX64s\n" +
            "qfL6MCob5+jY8qgDzOdIlaO6RUagIwuDi1YdvLNxXp/QWRLt+ZbdlxX/zIbdWQ25\n" +
            "efa9dd9aRxn3xtC0E8xc8UVkeLuij9bfXHjnyW/Yy4XfLEbXrlT3ji12zzsAEQEA\n" +
            "Af4JAwiRstp5/8cRBmDUcWXQocNb1zNBLluG/mgm9ZeQpzrQLmWMmSARyhOAX1WN\n" +
            "fBRl9/hBqaYQuh6khAfGD4lo74ckV2PgYsf7FxKPhN0+ZcxwhH/eEMnzEbjbhtyP\n" +
            "4YQsna52Ol2E8yHhU18iXRSxFx6ybSDOk9p6sG5CKx1A4K+O+LsP+/ic3j/Mg+5z\n" +
            "9RQiXE8+vv+YSnuos3R55ehlD0oMhEhbUDlWVx1fKa91sYd24/fCLiJKHsVaMCi1\n" +
            "te6GWlQB5RycbFtLDTJQvapu1DJRjROgVzdLLzPVidt+qFRxmYp4nlTePcbyiQq0\n" +
            "fkgxKPAV95h88WIjg0pOOd+PV4KP1WzzRoRAFUnV4E6oM2rlyVcCQNHFa1eOBHZ2\n" +
            "y9NYSQwGeiF1JSxA6a+KgQFZzZFihsGcI9iHgT/UNRCnc0qv1BVSC8LayVS6lYDM\n" +
            "LFdpSUoOU/B+RGFL/mm1mXNo7sESbcrraP/xI6zX8t4Z697rjbeKeWmvzie11agR\n" +
            "EAcHEOl9XFN5qKkr6AySwR0SNuUpjWNeYbsOvm4ED6T1ShHt75gdzALmbrsjGQFM\n" +
            "xWNlgbFd5HQMaOssWxKJS5YK0rENq1Dt9fsuJ9MRWUtZ8+t1IMM12wFMEwdx8IOA\n" +
            "K36/qriumLNNGqtZbKsyAiAlKond0TFFXEKGmbKQA82Oys8Ahb8IZ35U4FrUSqLG\n" +
            "63kUHih8bqTgehK2QAONE95B1fiDcy0CwMTq1c7S82qg9Mq8UZllLXrKPVCe4XAC\n" +
            "nbgaoD+aO3dawCgivyS/54ptxc2xb0gIt7X9M0HZXSN84q75SQhsNOweSYeoREqS\n" +
            "MNakQtKuKiyW1Ri48/cSierhplzQBIlXHOlyg9JkC2/rj0z3Bmf3sBIne485qqc+\n" +
            "PwVggEak14VPIKhzDYaUK6iscmiDhcxvlFvCwYQEGAEKAA8FAl7+1GcFCQ8JnAAC\n" +
            "Gy4BKQkQcBlSGAxcp9jAXSAEGQEKAAYFAl7+1GcACgkQ35tg+Rag/66fqggAgHNU\n" +
            "lN6XY3OZ8NXEfOXQwxbH9Jg/CzjoA6ntYLQORmOnXOd809YUMoR0s62VuDhuB9Mx\n" +
            "ycqPQpMoIPETElZri9RuPqsacJGMqyp1TsIfs0rzAsdLDpttBn26baTp0JQFJtud\n" +
            "4jYONkRlsIXQGZTsdwxMZEu0v3VMBmQI8CIrzOpumvFAZ7S1hNBeAUvypeG+ouRB\n" +
            "1q5ouiUk8zR687dN9MG/Xu4oAcgWXevQx8jQOXhJtIY3inYcbvxs1zGka+Ze8zGa\n" +
            "r0M//z0hSri1gI4jxIMaa5yVYKRbGBGb0ilfYYsdtkknWOSeBU9bo2W8yonSE5en\n" +
            "MSc+A5+GxIxusjHqst4YB/9skgT88UvPDVLOztq+a85Ldu2KRV2fiDD8YhaIIo50\n" +
            "muBGFGj9SUeZPGuX6VjsLXDZokuN1bJ0itqNq7Hn8MD2GWAQseU5Xe//5B6p9nWu\n" +
            "MFt+tw1OxeuEVLD0ZmMGnxkfokCN4gcYxO19xMWJCQcxBm1O5IENKtF1JKPNHguC\n" +
            "7pcbIXrVpnN19hm0aa17eQpw2wvhraiaXkjkKeeUtUpDPT3HGV+XTAtmF5Isa0Nu\n" +
            "QGpjWlhtfGvrWa+422rrLXlY3T90nswzElBXSP9nrLn97PFG1fHhykKJaTuRMElH\n" +
            "JpjxtIB6v8P+2Hk1/tm7ksAHAzKyyh8NZ707g9MUL847\n" +
            "=KEdT\n" +
            "-----END PGP PRIVATE KEY BLOCK-----\n";

    public static final String DEST_PRIVATE_KEYS = "-----BEGIN PGP PRIVATE KEY BLOCK-----\n" +
            "Version: Keybase OpenPGP v1.0.0\n" +
            "Comment: https://keybase.io/crypto\n" +
            "\n" +
            "xcMGBF7+/ksBCADZ1seGosfBQrHrKQK1VhU7/kZGtIK8KtQ06K74NQZDx6mbEYhh\n" +
            "U0PqjQPvpqV9wgnchdBnmtFpWX+vJfX+hu1QM3oOA1bAqC2VsUUL5ES35ewJ5cpM\n" +
            "nyRBlwhad2U7URIgTG8gy3YH6B0MWPeBXlODcc6ciQTDGh5MCzDKKxQBldycpEQF\n" +
            "ack9PwbcM03ma1qcA1evKb+uQ47FW3YClEOQVUK/lcKgMbiUnwxXg6NRwN4nH9nH\n" +
            "lcoZBdLTQPEvTXXVuKoObS/lIUze5VVJU9/Y7BUQBAqN6oXvQezaN8tVg02Pi6au\n" +
            "CyFwZ6683tc5t80Dm2Oju16DYD3833Pu13pTABEBAAH+CQMILWdGldMSpklgJYZC\n" +
            "0wraY7brpZKAVLESCUYst02WFFwyxdKZJo2TbCKLC93Qor40d+GmkemjoDg1hi1O\n" +
            "NbQAfCLkjwzs7g/3ESfngDxThyDLWMS1KEyQ2tmp+UWpKDNgFO+D57GKLZjkNzia\n" +
            "blcIy7Gf4aECteldcbQa1KX9/Hv4WVjUY++cLmGaaPqiF/puRC7P+TPsWpmalX8u\n" +
            "fkfsyrBvuzsQvAByI6o5xmrzMo0FapL8zmTgTniP6ga6/ucGdq/Uw2BsLuij5BMR\n" +
            "hPt/5j8dp/IX7mtHtLObPbVDj3oji91QSxh4E4Ho9eexzqYz+ZuadPgwOXEiYkud\n" +
            "fQsr6Enb2J1nSe8OK4sHhete+v15rAUwoemkP/xl/BDkvuxZhbUc1PSua0Q79Izv\n" +
            "FC6kW+khlaAoq+X2AU2gqgIgS0rLzC4oXC7WUGvS/QGyMRF7F7keC2S3iBScY7nC\n" +
            "D9ycyf5ClBl1G5vjfqPfWzEG7LzuskrElX91HtoNVyUA0S5PMm23ExuhX9OI2rp2\n" +
            "SKCjtjZjZXoaz1RnQAgxz1jQ4zfB2nRNm8ECXFZc0rA5bBNDB5PnN8Xw4bxJHbrQ\n" +
            "P3rk/wMctX7gFtgbfA4hrnf7DZwwBlAKf84JtPW7ccUdnkkP9EqJy09qkQNRvsC4\n" +
            "03CIbJw1IXH3yYWQhBTOq8FBsY0k17VNXhYftHFNPC2gi67gtSsMqiFBWTmkr/CN\n" +
            "ef/xUCHQyp8BwzOSh8pwnioM/AjyC2Paz8VR7rZRp3dQEk+q/1W8mvkUC2ymHVFI\n" +
            "9i/r032SAzNN60txlh2v6CQ4JqBuRoLkL0P9Aya4o9NgGuy9oXdL6oqU8P5a6SLT\n" +
            "qchWMHqZrUFdkDK1/z2BttZtjal5Aty5PXmjqSiD2VsW4g0PbVytWTP/pqrzrSQM\n" +
            "Cf8az9TEKuozzR90ZXN0IGRlc3QgPGh1bmd0c2Rlc3RAdGVzdC5jb20+wsBtBBMB\n" +
            "CgAXBQJe/v5LAhsvAwsJBwMVCggCHgECF4AACgkQSCMHYkx86HkOZQgAj8RnX6RG\n" +
            "/tRGUBQHLkk1/sdIYyCwnil67ALZvUhPwKjJEmRRP3cOrYltpK5kvlJdDwVG2klg\n" +
            "OUdK7M/WZcNzgGE8X0c6PRAJrBcWYpaiJc4Ng97ESbIyRr5BDIutKAV5PWxb0xmx\n" +
            "hcKTfMTA2w6vuzfgPpU/3FAWFv5P3tUSLS+/+9jkGPiw5Z2RDwVeHM9XMHyjXAEl\n" +
            "H1aK8jokkz0OvZ7a3MayUanzUAH80NX/ckJ9yCOczAl0h35n1I00WMmFmzyC80/e\n" +
            "k69cZSRwlIOhTCqGFjF2OktL26wutvqLMeom/Jaf+k+wIJFU1uaBEFGZRTYvT9fz\n" +
            "EmPC5GxTSNHgc8fDBgRe/v5LAQgAv+ExE1wEzfbm5eSTpCaQ0S9azgjEr18SK+Vo\n" +
            "Tg1L9/xmqAsbPcYsaCzN00lV9dTvqvJbpSFaqR/+iv5jCGvRHGGTupORI/ubtSWd\n" +
            "XknjgbTiXJWbZdRbl+1MG/XoF+YmE2RGW3lNSeEbETT9qWYHF5XKH0lp/FQIz8n/\n" +
            "t04QO/8l8fGnArE3PcT67K8BcvrVoJ+KS+1i7oIL6Ws9viStuu7Jlju5RRIG0+Ng\n" +
            "50RKehnMWj4laYWM69RJKsXvonWOYsM5vGjqOiQo3g/5p0McY65KuCUK9uvmkbjT\n" +
            "Uf4FI1eri/XO69wGgYjf77QbDTYrjjgusnrT4E3S/5zXkxmvDQARAQAB/gkDCNRB\n" +
            "OQLbyGxsYA5EAfE/nlx9nrzgtf/NQGb5Zr96Ke9dsPauxQraZOFHR70OrM/2s5bw\n" +
            "OTtUP5Mnd0Ml/JWMfx44UByf1vpgJHUr3wWTjEkWoNjsbNle65ttqy47MmrF3mFl\n" +
            "DGef8dq0yeyp4p+UEta8Z5RNT7oTWOevhkm9IUPOekhXPdrT6fOa9+wRwqJ5tB/p\n" +
            "QOw+hIIaskRqXyYF09zTRw7KnV8b0lACVJsGyHSvTgi7kLvCVUPoTYWEBubwtKZF\n" +
            "cr437mGYIQ6E5ENdzwOa4ridgUkPXdgX8lmscS7PRuSnV5CrmFNq+TDBZno+Ce6w\n" +
            "cY7yYN8cUyeKlVrBGUjuYqDn8IlHZNtFTPDJzbVQDUrUpezwH/FkALMBVNCvfUVL\n" +
            "zwCY1ktcHbrhM2UIExWHTPyefemzxosg8Xq+ZfeOsP4oWAuPtptD5IPBC3GG35qm\n" +
            "qbTQrAo/K6N9qhqXTpnNFaiLuBBzCgxje1PbHek6ZxlJbaBr9kAoXUzhqVtMxB9s\n" +
            "xIdg7IdbG4NZGVbBJttqjPUojBvR592WFMFKwBl4l59Tht1d29rz64hR8szwSxjl\n" +
            "4/7s64pmvVmO9Tmk/850lI8YLEWPRwq142Ubu7yldBlFfiLUcnBdjKaxc4Nb7Ui2\n" +
            "0oAyuE/yp9Z3agYU7EY02xvtbPqpu7HIIgiz327wzSK2vg/AWiRNvbGfmOZzlCmo\n" +
            "i5lUvRAbB1X3sWqPbUPUGd40dIy44AREGWSZeSVE5LCv2u0A05U6iqJOivmX9+/9\n" +
            "eIIXOgo6Jgk9aiOj9b4JfazT0nS2kCb6FEKkgcw/Ba3HNLtNtsX6KRFloLcRcmdd\n" +
            "qWYM9sqMe1e48a7Xc9Q+P+z/XEIwcOc16PzyZUZIOIPQ+Sc0ZJULiot7h0AXKi/L\n" +
            "bbm0M73NJo4j2J7juY4ZOkVh7MLBhAQYAQoADwUCXv7+SwUJDwmcAAIbLgEpCRBI\n" +
            "IwdiTHzoecBdIAQZAQoABgUCXv7+SwAKCRAf/R4UX/2qdlVOB/9rR30kdN4kGG7k\n" +
            "ECvH5Cl4PPhtNiuaOeg91FGOqki+TJYtzxykATpbz3oiAzMzNdrijW7zrlvEAz5v\n" +
            "2ulXdG0JdKglx45czKa3kyKSpFfrmrFiRvyUxwW5fCZNnmRiE1PfaY/q6p+TMw1K\n" +
            "pH6mDhBOqll6MuWEqAA5Ig3MAToRKG3Fo2+M+SJnwUH4cgvV/lm2eLzCHYnLAe27\n" +
            "w13df9qZsWyNEkyBfmVAt4RNbbN0FFouyNclDggHMM7arkzWYKNFlDIZ4yzSrjea\n" +
            "HU+z0VeEFoIXdvRtve1APFwdGthVEi+vV0i+a/Y29nTuAZDZ/HUEvrGQT2IlnNm+\n" +
            "oBpoqgx8wAQH/1AsMIJkN0asEGYil+VqfMD8tZlUoKLtMdPmOuN7p3ThFFBIQTZK\n" +
            "m769q1LO0Ascm/hOY2LppO/L44DKwt0FsdBc8Gcq/ZymtWxzW4RbBBuOSRQ95YnS\n" +
            "pxAhsQcftsQtmwsbScsuzLRH1hvaF5dMojUzIxUWa80vhvo7QmL3Ndt7vMgHHI3F\n" +
            "XRuvQw6Tt9rBhCqBZJE8lVqre95am1I0Iy7db/FFRr6yfRLa5UpMZqaakxDTFSQ6\n" +
            "bMFRn4PjhHEQDjYmtOWOWdRepWkX5OPxBEAzBtL3ZYGZ8P0XErKk/ZVDHyrEFkDI\n" +
            "X0ACOPvrjk7QvbqnyFlqkSXbwJ/B9AiOcQvHwwYEXv7+SwEIAMrOG1lm99F/QG0z\n" +
            "o44fg6s4QNp9lvq7CBAs5jAIdZGF3kh+1jK7Mz1gH+d3+p6DkZPomHLBhRpcTL7x\n" +
            "8E8vPLL4xkUKb9QZB0gVOQ4bOzKe5TEed6JTL3C/Xslpt+Br6f0aH6W2L/k8ETnK\n" +
            "mqHDT1nBERg8uKACnKrOqw96PaYAzYICMLy67sKnTWGHiS7LnV6y9L6No91Te4Qe\n" +
            "5r8P506WlmIse69i68DKboL11PN6BCpzdppDuJ/1oQkjpbw6S9kiC33zMI2YfEdO\n" +
            "waGIArKckJM0cZJy/COIYOchnegLDqbFfuJEb89uZ5Y+u0mjumeXCPpaMKtDIUXn\n" +
            "4YH+51kAEQEAAf4JAwj4JLP4fWj5NWDTxwwp13gijr62HBPznLqYU02RGrU5iHU3\n" +
            "bZQx4bQ2ZMc3U0BfxC6bl0c/a5HnKYp/GPFLLCXQz5i+nADUfmq+mtU3mpTF9Hd6\n" +
            "/8nQ4HWoVLaaKRx0z5oTOMo1zbT+JdAggtHfvLuvjzMn35nXA2ouvW+dSjQBz3TA\n" +
            "bPFRWyKttnAACOuyGxQ3+i748q+mPCEWQq9IE2Ep1XyEoJ3Qaw4mS2Z5P41PetbS\n" +
            "dcQx2avWuklT2wYumertI3jxYEPRr65SYyAp1HE+iWyubEsVGet8AgRAoWlj2fak\n" +
            "VG+rYcVrQph0WO9VxgHAIUR37ZpfSJbvDzGEqXWS+yZYm8R5mvYmX4FLM+mgvLgp\n" +
            "Gqwees6lG46RoBJTsS5vjMLEXhBrhWfhAdsnAGeuOZbAL4SH66Be0aeTtU2XOh3f\n" +
            "R6fVGzpTnwkMk7K3bdd67/CajwwTuSfhRWX2NzL50MHmSwNDrisHWUDIEoMND1K9\n" +
            "IDL56NG6lHGWEo0DfOBuH21V8U489af9z3IFtHmFLnbxTUXHlAleyEUkkoEVmuai\n" +
            "LDEI1+4iaGveazXzI71r1fy4GuyhcqIPBMoEVclIxLwRh3Q+sVRUfgjW5u79bnVz\n" +
            "hZ+IRd/MFmIi28z0W+XTJguddPU2ryVo5ye5v4lTnLXU39Myes3H7ZcQa9ltEKs3\n" +
            "gHx6QaOTNZ8f1oxRAFfDJ06V35wa25sMU/btcbK/qQFctaPAh94qSR7DVBj5Xy1L\n" +
            "8qkZ8ZAoxM+AH8L4o6sQIZDZ2bJoSBg2rE1V/H6WkKoLbneYUeiL0YZ0gJVzO02P\n" +
            "6Qy+dovABNti8ZU9UrKJ8ct3s0VGHPO8i4kWrj9PVM69teDH3adcAbPpRFd5TILX\n" +
            "vCZIUaxxpKpEFYUnBYtzA7rjwuXR5BIrTpQA0gsfD3kgib3CwYQEGAEKAA8FAl7+\n" +
            "/ksFCQ8JnAACGy4BKQkQSCMHYkx86HnAXSAEGQEKAAYFAl7+/ksACgkQFibeIK2V\n" +
            "DX1feAf/dd62IVS5m8SE457OXiX5sKMgcgDOzdJEY1wstfYgw0UZKfkUhEW02Rd9\n" +
            "OWq8EJAjLpbI9Jqn08fMzk3GEh2h3pkpg+tNxdxXHIg5zWdEJz8B3Sn/TqoA3FaN\n" +
            "xCIOe7B5MlnnHeqjjUJWYkkI9Ca3rK9oFnHj6A4apJ6WQNKDzNldhjW1k5BUeqQn\n" +
            "kRuYRSd4X+bJkZpoCYQxW7/1l80rfkZc4vnT9uS5Xv7pfGkJlJBFSnb5xxskiuL0\n" +
            "VW6tDsMEgOXG9lrOdg3PMEi5ZmGQkaobdVKA5+vch0TYdtqAgfR2532l1MsbimxA\n" +
            "3qPA3oz9XCB3hjJ5TmeuBcy8GLZrTTSuB/41IHYtivJ4QS/AOi1MPzi2E7v3KCqg\n" +
            "y7rAIkphbD8BNtFJZpgMdQAc5yWOXDdCVMoE9VkHFpmhleBwu3TDHV4K2cEe5vxR\n" +
            "tC6ssV+ippVuh1nWEMwvM9df36LJPm71c4okbWo+fgJa0/tb4OG7vGyPJFBIul8i\n" +
            "IRHh0IJt2QtXFKW83MKQS84slG6ZUDvhWoQlipE4eIBZhzakGHXrfqphAXl4WI8Y\n" +
            "neqEoioLRkjrrDv2eZhJZHJoQUz+4GuajwoE6A/soC+g4iDVprY4q73nNMhOaetG\n" +
            "L9RhUza3hxqGL6F/lHjX8VtirWiytRkldpoC1IO+04eK+MEUDx2OWnDl\n" +
            "=1VZe\n" +
            "-----END PGP PRIVATE KEY BLOCK-----\n";

    public static final String DEST_PUBLIC_KEYS = "-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
            "Version: Keybase OpenPGP v1.0.0\n" +
            "Comment: https://keybase.io/crypto\n" +
            "\n" +
            "xsBNBF7+/ksBCADZ1seGosfBQrHrKQK1VhU7/kZGtIK8KtQ06K74NQZDx6mbEYhh\n" +
            "U0PqjQPvpqV9wgnchdBnmtFpWX+vJfX+hu1QM3oOA1bAqC2VsUUL5ES35ewJ5cpM\n" +
            "nyRBlwhad2U7URIgTG8gy3YH6B0MWPeBXlODcc6ciQTDGh5MCzDKKxQBldycpEQF\n" +
            "ack9PwbcM03ma1qcA1evKb+uQ47FW3YClEOQVUK/lcKgMbiUnwxXg6NRwN4nH9nH\n" +
            "lcoZBdLTQPEvTXXVuKoObS/lIUze5VVJU9/Y7BUQBAqN6oXvQezaN8tVg02Pi6au\n" +
            "CyFwZ6683tc5t80Dm2Oju16DYD3833Pu13pTABEBAAHNH3Rlc3QgZGVzdCA8aHVu\n" +
            "Z3RzZGVzdEB0ZXN0LmNvbT7CwG0EEwEKABcFAl7+/ksCGy8DCwkHAxUKCAIeAQIX\n" +
            "gAAKCRBIIwdiTHzoeQ5lCACPxGdfpEb+1EZQFAcuSTX+x0hjILCeKXrsAtm9SE/A\n" +
            "qMkSZFE/dw6tiW2krmS+Ul0PBUbaSWA5R0rsz9Zlw3OAYTxfRzo9EAmsFxZilqIl\n" +
            "zg2D3sRJsjJGvkEMi60oBXk9bFvTGbGFwpN8xMDbDq+7N+A+lT/cUBYW/k/e1RIt\n" +
            "L7/72OQY+LDlnZEPBV4cz1cwfKNcASUfVoryOiSTPQ69ntrcxrJRqfNQAfzQ1f9y\n" +
            "Qn3II5zMCXSHfmfUjTRYyYWbPILzT96Tr1xlJHCUg6FMKoYWMXY6S0vbrC62+osx\n" +
            "6ib8lp/6T7AgkVTW5oEQUZlFNi9P1/MSY8LkbFNI0eBzzsBNBF7+/ksBCAC/4TET\n" +
            "XATN9ubl5JOkJpDRL1rOCMSvXxIr5WhODUv3/GaoCxs9xixoLM3TSVX11O+q8lul\n" +
            "IVqpH/6K/mMIa9EcYZO6k5Ej+5u1JZ1eSeOBtOJclZtl1FuX7Uwb9egX5iYTZEZb\n" +
            "eU1J4RsRNP2pZgcXlcofSWn8VAjPyf+3ThA7/yXx8acCsTc9xPrsrwFy+tWgn4pL\n" +
            "7WLuggvpaz2+JK267smWO7lFEgbT42DnREp6GcxaPiVphYzr1Ekqxe+idY5iwzm8\n" +
            "aOo6JCjeD/mnQxxjrkq4JQr26+aRuNNR/gUjV6uL9c7r3AaBiN/vtBsNNiuOOC6y\n" +
            "etPgTdL/nNeTGa8NABEBAAHCwYQEGAEKAA8FAl7+/ksFCQ8JnAACGy4BKQkQSCMH\n" +
            "Ykx86HnAXSAEGQEKAAYFAl7+/ksACgkQH/0eFF/9qnZVTgf/a0d9JHTeJBhu5BAr\n" +
            "x+QpeDz4bTYrmjnoPdRRjqpIvkyWLc8cpAE6W896IgMzMzXa4o1u865bxAM+b9rp\n" +
            "V3RtCXSoJceOXMymt5MikqRX65qxYkb8lMcFuXwmTZ5kYhNT32mP6uqfkzMNSqR+\n" +
            "pg4QTqpZejLlhKgAOSINzAE6EShtxaNvjPkiZ8FB+HIL1f5Ztni8wh2JywHtu8Nd\n" +
            "3X/ambFsjRJMgX5lQLeETW2zdBRaLsjXJQ4IBzDO2q5M1mCjRZQyGeMs0q43mh1P\n" +
            "s9FXhBaCF3b0bb3tQDxcHRrYVRIvr1dIvmv2NvZ07gGQ2fx1BL6xkE9iJZzZvqAa\n" +
            "aKoMfMAEB/9QLDCCZDdGrBBmIpflanzA/LWZVKCi7THT5jrje6d04RRQSEE2Spu+\n" +
            "vatSztALHJv4TmNi6aTvy+OAysLdBbHQXPBnKv2cprVsc1uEWwQbjkkUPeWJ0qcQ\n" +
            "IbEHH7bELZsLG0nLLsy0R9Yb2heXTKI1MyMVFmvNL4b6O0Ji9zXbe7zIBxyNxV0b\n" +
            "r0MOk7fawYQqgWSRPJVaq3veWptSNCMu3W/xRUa+sn0S2uVKTGammpMQ0xUkOmzB\n" +
            "UZ+D44RxEA42JrTljlnUXqVpF+Tj8QRAMwbS92WBmfD9FxKypP2VQx8qxBZAyF9A\n" +
            "Ajj7645O0L26p8hZapEl28CfwfQIjnELzsBNBF7+/ksBCADKzhtZZvfRf0BtM6OO\n" +
            "H4OrOEDafZb6uwgQLOYwCHWRhd5IftYyuzM9YB/nd/qeg5GT6JhywYUaXEy+8fBP\n" +
            "Lzyy+MZFCm/UGQdIFTkOGzsynuUxHneiUy9wv17Jabfga+n9Gh+lti/5PBE5ypqh\n" +
            "w09ZwREYPLigApyqzqsPej2mAM2CAjC8uu7Cp01hh4kuy51esvS+jaPdU3uEHua/\n" +
            "D+dOlpZiLHuvYuvAym6C9dTzegQqc3aaQ7if9aEJI6W8OkvZIgt98zCNmHxHTsGh\n" +
            "iAKynJCTNHGScvwjiGDnIZ3oCw6mxX7iRG/PbmeWPrtJo7pnlwj6WjCrQyFF5+GB\n" +
            "/udZABEBAAHCwYQEGAEKAA8FAl7+/ksFCQ8JnAACGy4BKQkQSCMHYkx86HnAXSAE\n" +
            "GQEKAAYFAl7+/ksACgkQFibeIK2VDX1feAf/dd62IVS5m8SE457OXiX5sKMgcgDO\n" +
            "zdJEY1wstfYgw0UZKfkUhEW02Rd9OWq8EJAjLpbI9Jqn08fMzk3GEh2h3pkpg+tN\n" +
            "xdxXHIg5zWdEJz8B3Sn/TqoA3FaNxCIOe7B5MlnnHeqjjUJWYkkI9Ca3rK9oFnHj\n" +
            "6A4apJ6WQNKDzNldhjW1k5BUeqQnkRuYRSd4X+bJkZpoCYQxW7/1l80rfkZc4vnT\n" +
            "9uS5Xv7pfGkJlJBFSnb5xxskiuL0VW6tDsMEgOXG9lrOdg3PMEi5ZmGQkaobdVKA\n" +
            "5+vch0TYdtqAgfR2532l1MsbimxA3qPA3oz9XCB3hjJ5TmeuBcy8GLZrTTSuB/41\n" +
            "IHYtivJ4QS/AOi1MPzi2E7v3KCqgy7rAIkphbD8BNtFJZpgMdQAc5yWOXDdCVMoE\n" +
            "9VkHFpmhleBwu3TDHV4K2cEe5vxRtC6ssV+ippVuh1nWEMwvM9df36LJPm71c4ok\n" +
            "bWo+fgJa0/tb4OG7vGyPJFBIul8iIRHh0IJt2QtXFKW83MKQS84slG6ZUDvhWoQl\n" +
            "ipE4eIBZhzakGHXrfqphAXl4WI8YneqEoioLRkjrrDv2eZhJZHJoQUz+4GuajwoE\n" +
            "6A/soC+g4iDVprY4q73nNMhOaetGL9RhUza3hxqGL6F/lHjX8VtirWiytRkldpoC\n" +
            "1IO+04eK+MEUDx2OWnDl\n" +
            "=hwI6\n" +
            "-----END PGP PUBLIC KEY BLOCK-----\n";
}
