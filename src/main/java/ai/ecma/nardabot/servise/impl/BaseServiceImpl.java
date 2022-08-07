package ai.ecma.nardabot.servise.impl;

import ai.ecma.nardabot.entity.Role;
import ai.ecma.nardabot.entity.User;
import ai.ecma.nardabot.enums.RoleNames;
import ai.ecma.nardabot.enums.State;
import ai.ecma.nardabot.repository.RoleRepo;
import ai.ecma.nardabot.repository.UserRepo;
import ai.ecma.nardabot.servise.abs.*;
import ai.ecma.nardabot.utills.CommonUtils;
import ai.ecma.nardabot.utills.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BaseServiceImpl implements BaseService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final Btn btn;
    private final Execute execute;



    @Override
    public void setState(User user, State state) {
        user.setState(state);
        userRepo.save(user);
    }



}
