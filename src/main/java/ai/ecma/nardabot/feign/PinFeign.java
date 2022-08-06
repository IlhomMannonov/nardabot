package ai.ecma.nardabot.feign;

import ai.ecma.nardabot.utills.Constant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.telegram.telegrambots.meta.api.objects.Update;

@FeignClient(name = "pin")
public interface PinFeign {

    @GetMapping(Constant.URL + Constant.TOKEN + "/getUpdates")
    Update getUpdate();


    @PostMapping("http/localhost:8080/client")
    void sendUpdate(@RequestBody Update update);

}
