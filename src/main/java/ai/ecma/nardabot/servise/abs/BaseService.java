package ai.ecma.nardabot.servise.abs;

import ai.ecma.nardabot.entity.User;
import ai.ecma.nardabot.enums.State;
import org.telegram.telegrambots.meta.api.objects.Update;


public interface BaseService {

    void setState(User user, State state);

}
