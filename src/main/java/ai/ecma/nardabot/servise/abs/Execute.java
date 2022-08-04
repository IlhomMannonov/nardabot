package ai.ecma.nardabot.servise.abs;


import ai.ecma.nardabot.payload.RestTelegram;
import ai.ecma.nardabot.payload.SendPhoto;
import ai.ecma.nardabot.payload.SendVideo;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface Execute {
    RestTelegram sendMessage(SendMessage sendMessage);

    void deleteMessage(Integer messageId, String chatId);

    void editMessageText(EditMessageText editMessageText);

    void editMessageCaption(EditMessageCaption editMessageCaption);

    void answerCallbackQuery(AnswerCallbackQuery answerCallbackQuery);

    void sendPhoto(SendPhoto sendPhoto);

    void sendVideo(SendVideo sendVideo);
}
