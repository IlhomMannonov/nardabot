package ai.ecma.nardabot.controller.impl;

import ai.ecma.nardabot.controller.abs.ClientController;
import ai.ecma.nardabot.feign.UpdaterFeign;
import ai.ecma.nardabot.utills.Constant;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Request;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.support.AbstractMultipartHttpServletRequest;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

@Component
@RequiredArgsConstructor
public class LongPolling extends TelegramLongPollingBot {
    private final UpdaterFeign updaterFeign;

    @Override
    public String getBotUsername() {
        return "@GammerRobot";
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
