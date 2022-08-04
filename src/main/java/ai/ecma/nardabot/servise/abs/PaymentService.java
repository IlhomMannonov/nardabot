package ai.ecma.nardabot.servise.abs;

import ai.ecma.nardabot.entity.User;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface PaymentService {
    void deposit(Update update, User user);

    void withdraw(Update update, User user);

    void enterSum(Update update);

    void enterWithdrawSum(Update update);
}
