package ai.ecma.nardabot.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/refresh")
@RequiredArgsConstructor
public class RefreshController {
    private final RestTemplate restTemplate;

    @GetMapping
    public String refresh() {
        return "ok";
    }

        @Scheduled(fixedDelay = 1200000) // 20 minut
        public void refreshing() {
            restTemplate.getForEntity("https://narda.herokuapp.com/refresh", String.class);
        }
}
