package ai.ecma.nardabot.admin.service.abs;

import ai.ecma.nardabot.entity.User;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface AdminHomeService {
    void home(Update update, User user);


    void dashboard(Update update, User user);
    void ads(Update update, User user);
    void topUsers(Update update, User user);



    void homeRouter(Update update, User user);
}
