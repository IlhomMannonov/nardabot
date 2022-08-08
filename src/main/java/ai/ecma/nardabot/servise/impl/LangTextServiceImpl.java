package ai.ecma.nardabot.servise.impl;

import ai.ecma.nardabot.entity.User;
import ai.ecma.nardabot.enums.Lang;
import ai.ecma.nardabot.enums.State;
import ai.ecma.nardabot.servise.abs.LangTextService;
import ai.ecma.nardabot.utills.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LangTextServiceImpl implements LangTextService {
    @Override
    public String inlineText(User user) {
        State value = State.values()[user.getState().ordinal()];
        String text = null;
        switch (value) {
            case SEND_PHONE:
                text = getTxt(user, "Iltimos telefon raqamingizni yuboring", "Please send your phone number", "Пожалуйста, пришлите свой номер телефона");
                break;
        }
        return text;
    }

    @Override
    public String buttonText(User user) {
        State value = State.values()[user.getState().ordinal()];
        String text = null;
        switch (value) {
            case SEND_PHONE:
                text = getTxt(user, "Raqamni yuborish", "Share phone number", "Поделитесь номером телефона");
                break;
            case HOME:
                text = getTxt(user, "O'yinlar\nProfil, To'lovlar tarixi\n Pul Solish, Pul chiqarish\nSozlamalar", "Games\nProfile, Payment History\n Deposit, Withdraw\nSettings", "Игры\nПрофиль, История платежей\nДепозит, вывод\nНастройки");
                break;
            case GAMES:
                text = getTxt(user, "Narda\nOrtga", "Backgammon\nBack", "Нарды\nНазад");
                break;
        }
        return text;
    }

    @Override
    public String text(User user) {
        State value = State.values()[user.getState().ordinal()];
        String text = null;
        switch (value) {
            case HOME:
                text = getTxt(user, "Salom " + user.getName() + " Botimizga hush kelibsiz", "Hello " + user.getName() + " Welcome to our bot ", "Привет " + user.getName() + " Добро пожаловать в наш бот");
                break;
            case GAMES:
                text = getTxt(user, "O'yinlar bo'limi", "Games section", "Раздел игр");
                break;
            case NARDA:
                text = getTxt(user,
                        " Kerakli usulni tanlang va pul tiking\n❕ Summani qolda yozishingiz ham mumkin\n⚜️ Agar tanlagan usulingizga nardalar mos kelsa siz g'olibsiz\n\n" + "⬆️ 7 dan yuqori (" + Constant.NARDA_KF_OVER + ")KF,\n⏺ 7 -> (" + Constant.NARDA_KF_MIDDLE + ")KF,\n⬇️ 7 dan quyi (" + Constant.NARDA_KF_LOWER + ")KF",
                        " Choose the method you want and place your bet\n❕ You can also write the sum \n⚜️ If backgammon matches your chosen method, you win +\n\n" + "⬆️ over 7 (" + Constant.NARDA_KF_OVER + ")KF,\n⏺️ 7 -> (" + Constant.NARDA_KF_MIDDLE + ")KF,\n⬇️ under 7 (" + Constant.NARDA_KF_LOWER + ")KF",
                        " Выберите нужный метод и сделайте ставку\n❕ Вы также можете написать сумму\n⚜️ Если нарды соответствуют выбранному вами методу, вы выиграли\n\n" + "⬆️ Больше 7 (" + Constant.NARDA_KF_OVER + ")KF,\n⏺ 7 -> (" + Constant.NARDA_KF_MIDDLE + ")KF,\n⬇️ Меньше 7 (" + Constant.NARDA_KF_LOWER + ")KF");
        }
        return text;
    }

    @Override
    public String getTxt(User user, String uz, String en, String ru) {
        if (user.getLanguage() == null)
            user.setLanguage(Lang.UZ);

        Lang value = Lang.values()[user.getLanguage().ordinal()];
        switch (value) {
            case UZ:
                return uz;
            case EN:
                return en;
            case RU:
                return ru;
        }
        //DEFAULT ENGLISHCHA TIL
        return en;
    }
}