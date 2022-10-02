package ai.ecma.nardabot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class NardaApplication {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {

//        SpringApplication.run(NardaApplication.class, args);

        Arr arr = new Arr();
        arr.add(2);
        arr.add(2);
        arr.add(2);
        arr.add(2);
        arr.add(2);
        arr.add(2);
        arr.add(2);
        arr.add(2);
        arr.add(2);
        arr.add(2);
        arr.add(2);
        arr.add(2);
        System.out.println(arr);
    }

}


class Arr {
    static class Node {
        int value;
        Node next;


        public Node(int value) {
            this.value = value;
        }
    }

    private Node first;
    private Node last;
    private int size;

    public void add(int item) {
        if (size == 0) {
            first = new Node(item);
            last = new Node(item);
            first.next = last;
        } else {
            last.next = new Node(item);

        }
        size++;

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node custom = first;
        while (custom.next != null) {
            sb.append(first.value);
            custom = custom.next;
            sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }


}