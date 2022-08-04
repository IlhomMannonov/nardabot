package ai.ecma.nardabot.controller.impl;

import ai.ecma.nardabot.controller.abs.ClientController;
import ai.ecma.nardabot.servise.abs.RouterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class ClientControllerImpl implements ClientController {
    @Autowired
    private RouterService routerService;

    @Override
    public void getUpdate(Update update, HttpServletRequest req) {
//        System.out.println(
//                update.getMessage().getPhoto()!=null?update.getMessage().getPhoto():11);
        routerService.getUpdate(update);
    }
}
