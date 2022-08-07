package ai.ecma.nardabot.servise.impl;

import ai.ecma.nardabot.feign.TelegramFeign;
import ai.ecma.nardabot.payload.RestTelegram;
import ai.ecma.nardabot.payload.SendPhoto;
import ai.ecma.nardabot.payload.SendVideo;
import ai.ecma.nardabot.servise.abs.Execute;
import ai.ecma.nardabot.utills.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Service
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
        feign.deleteMessage(BOT, new DeleteMessage(chatId, messageId));
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
        feign.sendPhoto(BOT, sendPhoto);
    }

    @Override
    public void sendVideo(SendVideo sendVideo) {
        feign.sendVideo(BOT, sendVideo);
    }

    @Override
    public void sendInvoice(SendInvoice sendInvoice) {
        feign.sendInvoice(BOT, sendInvoice);
    }
}
