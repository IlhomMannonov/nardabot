package ai.ecma.nardabot.controller.impl;

import ai.ecma.nardabot.feign.UpdaterFeign;
import ai.ecma.nardabot.utills.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
@RequiredArgsConstructor
public class LongPolling extends TelegramLongPollingBot {
    private final UpdaterFeign updaterFeign;

    @Override
    public String getBotUsername() {
        return "@GammerRobot";
//        return "@gammer_testobot";
    }

    @Override
    public String getBotToken() {
        return Constant.TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {

        updaterFeign.sendUpdate(update);
    }
}
