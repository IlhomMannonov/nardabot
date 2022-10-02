package ai.ecma.nardabot.servise.impl;

import ai.ecma.nardabot.admin.service.abs.AdminHomeService;
import ai.ecma.nardabot.admin.service.abs.AdsService;
import ai.ecma.nardabot.entity.User;
import ai.ecma.nardabot.enums.State;
import ai.ecma.nardabot.repository.AdsRepo;
import ai.ecma.nardabot.servise.abs.*;
import ai.ecma.nardabot.utills.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BackServiceImpl implements BackService {
    private final BaseService baseService;
    private final LangTextService langTextService;
    private final NardaService nardaService;
    private final AdminHomeService adminHomeService;
    private final AuthService authService;
    private final HomeService homeService;
    private final SettingsService settingsService;
    private final AdsService adsService;
    private final AdsRepo adsRepo;

    @Override
    public void back(Update update) {
        if (update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            User user = CommonUtils.getUser();
            if (Objects.equals(text, langTextService.getTxt(user, "Ortga", "Back", "Назад"))) {
                switch (user.getState()) {
                    case NARDA:
                        baseService.setState(user, State.GAMES);
                        homeService.games(user);
                        break;
                    case GAMES:
                    case PAYMENT_ENTER_SUM:
                    case SETTINGS:
                    case WITHDRAW:
                        baseService.setState(user, State.HOME);
                        authService.getHome(update);
                        break;
                    case EDIT_CARD:
                    case ADD_CARD:
                        baseService.setState(user, State.SETTINGS);
                        homeService.settings(update, user);
                        break;
                    case ADS_HOME:
                        adminHomeService.home(update, user);
                        break;
                    case ADS_ADD_TEXT:
                    case ADS_ADD_MEDIA:
                        adminHomeService.ads(update, user);
                        break;

                    case ADS_ADD_BUTTON:
                        baseService.setState(user, State.ADS_ADD_MEDIA);
                        adminHomeService.ads(update, user);
                        break;

                }
            }
        }
    }
}
