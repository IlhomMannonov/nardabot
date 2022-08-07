package ai.ecma.nardabot.controller.abs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.servlet.http.HttpServletRequest;

@RequestMapping(ClientController.CLIENT_CONTROLLER_PATH)
public interface ClientController {
    String CLIENT_CONTROLLER_PATH = "/client";

    @PostMapping
    void getUpdate(@RequestBody Update update, HttpServletRequest req);

    @GetMapping
    String pin();
}
