package ai.ecma.nardabot.servise.abs;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface NardaService {
    void nardaGame(Update update);


    //BU YOL NARDA GA KELGAN SOROVLARNI ROUT QILADI
    void nardaRouter(Update update);

    void choiceButtonsWithCallbackQuery(Update update);

    void editBalanceWithMessage(Update update);

}
