package ai.ecma.nardabot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;

@SpringBootApplication
@EnableFeignClients

public class NardaApplication {
    @Bean
    public Timer getTimer() {
        return new Timer();
    }
    public static void main(String[] args) {
//        String s = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Timestamp(System.currentTimeMillis()));
//        System.out.println(s);


        SpringApplication.run(NardaApplication.class, args);
//        BigDecimal a = new BigDecimal(5);
//        System.out.println(a.multiply(new BigDecimal(2)));
//
//        NumberFormat numberFormat = NumberFormat.getNumberInstance();
//        String format = numberFormat.format(45646.2);
//        System.out.println(format);

    }

}
