package ai.ecma.nardabot;

import org.glassfish.grizzly.http.util.TimeStamp;

import javax.xml.bind.DatatypeConverter;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        long l = System.currentTimeMillis() + 100000;
        System.out.println(getHash(17756,68,"d4as6d54654sf4sda","RUB",68));

    }

    private static String getHash(Object... item) throws NoSuchAlgorithmException {
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
