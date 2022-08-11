package ai.ecma.nardabot.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PayDTO {
    private UUID id;
    private String userName;
    private String phone;
    private String orderCode;
    private String chatId;
    private String userBalance;
    private String withdraw;
    private String card;
    private String lasInMoney;
    private String time;
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance();

    public void setTime(Timestamp time) {
        this.time = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(new Date(time.getTime()));
    }

    public void setCard(String card) {
        if (card != null && card.length() == 16)
            this.card = card.substring(0, 4) + "-" + card.substring(4, 8) + "-" + card.substring(8, 12) + "-" + card.substring(12);
    }

    public void setWithdraw(BigDecimal amount) {
        if (amount != null)
            this.withdraw = numberFormat.format(amount) + " SUM";
    }

    public void setUserBalance(BigDecimal balance) {
        if (balance != null)
            this.userBalance = numberFormat.format(balance) + " SUM";
    }
}
