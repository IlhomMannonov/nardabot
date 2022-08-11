package ai.ecma.nardabot.payload.freekassa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@Data
public class CreateOrderDTO {
    private final String secretKey = "d4as6d54654sf4sda";
    private String shopId;
    private Integer nonce;
    private String signature;
    private Integer orderId;
    private Integer i;
    private String email;
    private String ip;
    private Double amount;
    private String currency;

    @SneakyThrows
    public CreateOrderDTO(String shopId, Integer nonce, Integer orderId, Integer i, String email, String ip, Double amount, String currency) {
        this.shopId = shopId;
        this.nonce = nonce;
        this.signature = getHash(shopId, amount,currency, orderId);
        this.orderId = orderId;
        this.i = i;
        this.email = email;
        this.ip = ip;
        this.amount = amount;
        this.currency = currency;
    }

    private  String getHash(Object... item) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        List<Object> items = new ArrayList<>(Arrays.asList(item));
        StringBuilder text = new StringBuilder();
        for (Object o : items) {
            text.append(o.toString()).append(":");
        }
        text = new StringBuilder(text.substring(0, text.length() - 1));
        System.out.println(text);
        md5.update(text.toString().getBytes());
        byte[] digest = md5.digest();
        return DatatypeConverter.printHexBinary(digest).toLowerCase();
    }
}
