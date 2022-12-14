package ai.ecma.nardabot.servise.impl;

import ai.ecma.nardabot.feign.TelegramFeign;
import ai.ecma.nardabot.payload.RestTelegram;
import ai.ecma.nardabot.payload.SendPhoto;
import ai.ecma.nardabot.payload.SendVideo;
import ai.ecma.nardabot.servise.abs.Execute;
import ai.ecma.nardabot.utills.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExecuteImpl implements Execute {
    private final String BOT = "bot" + Constant.TOKEN;

    private final TelegramFeign feign;

    @Override
    public RestTelegram sendMessage(SendMessage sendMessage) {
        RestTelegram restTelegram = new RestTelegram(false, null);
        try {
            return feign.sendMessage(BOT, sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestTelegram(false, null);
        }
    }

    @Override
    public void deleteMessage(Integer messageId, String chatId) {
        try {
            feign.deleteMessage(BOT, new DeleteMessage(chatId, messageId));
        } catch (Exception e) {
            log.error("dont deleted");
        }

    }

    @Override
    public void editMessageText(EditMessageText editMessageText) {
        feign.editMessageText(BOT, editMessageText);
    }

    @Override
    public void editMessageCaption(EditMessageCaption editMessageCaption) {
        try {
            feign.editMessageCaption(BOT, editMessageCaption);
        } catch (Exception ignore) {

        }

    }

    @Override
    public void answerCallbackQuery(AnswerCallbackQuery answerCallbackQuery) {
        feign.answerCallbackQuery(BOT, answerCallbackQuery);
    }

    @Override
    public void sendPhoto(SendPhoto sendPhoto) {
        try {

            feign.sendPhoto(BOT, sendPhoto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendVideo(SendVideo sendVideo) {
        try {
            feign.sendVideo(BOT, sendVideo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendInvoice(SendInvoice sendInvoice) {
        feign.sendInvoice(BOT, sendInvoice);
    }
}
