package ai.ecma.nardabot.controller.impl;

import ai.ecma.nardabot.controller.abs.Pin;
import ai.ecma.nardabot.feign.TelegramFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PinImpl implements Pin {

    private final TelegramFeign feign;

    @Override
    public String getPin() {
        return "ok";
    }

    @Scheduled(fixedDelay = 1200000) // 20 minut
    public void refreshing() {
        feign.pin();
    }

}
