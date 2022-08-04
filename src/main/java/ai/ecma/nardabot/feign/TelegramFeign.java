package ai.ecma.nardabot.feign;

import ai.ecma.nardabot.payload.RestTelegram;
import ai.ecma.nardabot.payload.SendPhoto;
import ai.ecma.nardabot.payload.SendVideo;
import ai.ecma.nardabot.utills.Constant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@FeignClient(url = Constant.URL, name = "telegram-bot")
public interface TelegramFeign {
    @PostMapping("{path}/sendMessage")
    RestTelegram sendMessage(@PathVariable String path, @RequestBody SendMessage sendMessage);

    @PostMapping("{path}/editMessageText")
    RestTelegram editMessageText(@PathVariable String path, @RequestBody EditMessageText editMessageText);

    @PostMapping("{path}/editMessageCaption")
    RestTelegram editMessageCaption(@PathVariable String path, @RequestBody EditMessageCaption editMessageCaption);

    @PostMapping("{path}/deleteMessage")
    void deleteMessage(@PathVariable String path, @RequestBody DeleteMessage deleteMessage);

    @PostMapping("{path}/sendPhoto")
    void sendPhoto(@PathVariable String path, SendPhoto sendPhoto);

    @PostMapping("{path}/sendVideo")
    void sendVideo(@PathVariable String path, SendVideo sendVideo);

    @PostMapping("{path}/answerCallbackQuery")
    void answerCallbackQuery(@PathVariable String path, @RequestBody AnswerCallbackQuery answerCallbackQuery);

}
