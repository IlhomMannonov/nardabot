package ai.ecma.nardabot.utills;

import java.math.BigDecimal;

public interface Constant {
    String URL = "https://api.telegram.org/";

    String TOKEN = "5302826377:AAHroBH8OFrofD7G9k6OOeqNQ7PaWLik7aE";

    String KEY = "--de-g--v4-ef5-v888-vd";

    BigDecimal MIN_BET = BigDecimal.valueOf(2000.0);
    BigDecimal MAX_BET = BigDecimal.valueOf(3000000.0);

    BigDecimal NARDA_KF_LOWER = BigDecimal.valueOf(2.3);
    BigDecimal NARDA_KF_OVER = BigDecimal.valueOf(2.3);
    BigDecimal NARDA_KF_MIDDLE = BigDecimal.valueOf(5.8);
    BigDecimal MIN_WITHDRAW = BigDecimal.valueOf(10000.0);
    BigDecimal MIN_DEPOSIT = BigDecimal.valueOf(10000.0);

    Long REJECT_TIME = 600000L;
    String USERNAME = "@payer_game";
}
