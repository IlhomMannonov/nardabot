package ai.ecma.nardabot.feign;


import ai.ecma.nardabot.utills.Constant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.telegram.telegrambots.meta.api.objects.Update;

@FeignClient(url = Constant.SEND_UPDATE_PATH, name = "telegram-bot")
public interface UpdaterFeign {
    @PostMapping
    void sendUpdate(@RequestBody Update update);
}
