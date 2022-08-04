package ai.ecma.nardabot.utills;

import ai.ecma.nardabot.entity.User;
import ai.ecma.nardabot.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
public class CommonUtils {
    private static UserRepo userRepo;

    @Autowired
    void init(UserRepo userRepo) {
        CommonUtils.userRepo = userRepo;
    }

    public static User getUser() {
        String chatId = (String) currentRequest().getAttribute(Constant.KEY);
        return userRepo.getByChatId(chatId);

    }

    public static HttpServletRequest currentRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Optional.ofNullable(servletRequestAttributes).map(ServletRequestAttributes::getRequest).orElse(null);
    }

}
