package ai.ecma.nardabot.servise.impl;

import ai.ecma.nardabot.entity.User;
import ai.ecma.nardabot.entity.narda.Narda;
import ai.ecma.nardabot.enums.Lang;
import ai.ecma.nardabot.enums.NardaType;
import ai.ecma.nardabot.enums.State;
import ai.ecma.nardabot.repository.NardaRepo;
import ai.ecma.nardabot.servise.abs.Btn;
import ai.ecma.nardabot.servise.abs.ButtonService;
import ai.ecma.nardabot.servise.abs.LangTextService;
import ai.ecma.nardabot.utills.Constant;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ButtonServiceImpl implements ButtonService {
    private final Btn btn;
    private final LangTextService langTextService;
    private final NardaRepo nardaRepo;

    @Override
    public InlineKeyboardMarkup getInlineBtn(User user, String id) {
        InlineKeyboardMarkup markup = null;
        State value = State.values()[user.getState().ordinal()];
        switch (value) {
            case CHOICE_LANG:
            case EDIT_LANG:
                markup = btn.markup(
                        btn.row(
                                btn.button(Lang.EN.name(), Lang.EN.name()),
                                btn.button(Lang.RU.name(), Lang.RU.name()),
                                btn.button(Lang.UZ.name(), Lang.UZ.name())
                        )
                );
                break;
            case NARDA:
                Narda narda = nardaRepo.findByUserId(user.getId()).orElse(
                        Narda.builder()
                                .amount(Constant.MIN_BET)
                                .type(NardaType.OVER)
                                .user(user)
                                .build()
                );
                markup = btn.markup(
                        btn.row(
                                btn.button("7 ⬆️" + (narda.getType() != null && narda.getType().equals(NardaType.OVER) ? "✅" : ""), "BACKGAMMON:game:over"),
                                btn.button("7 " + (narda.getType() != null && narda.getType().equals(NardaType.MIDDLE) ? "✅" : ""), "BACKGAMMON:game:middle"),
                                btn.button("7 ⬇️" + (narda.getType() != null && narda.getType().equals(NardaType.LOWER) ? "✅" : ""), "BACKGAMMON:game:lower")
                        ), btn.row(
                                btn.button("MIN", "BACKGAMMON:edit:min"),
                                btn.button("X2", "BACKGAMMON:edit:x2"),
                                btn.button("X/2", "BACKGAMMON:edit:x/2"),
                                btn.button("MAX", "BACKGAMMON:edit:max")
                        ), btn.row(
                                btn.button(langTextService.getTxt(user, "Hisobingiz: " + user.getBalance() + " so'm", "Balance: "+ user.getBalance() + " сум", "баланс: " + user.getBalance() + " sum"), "none"),
                                btn.button(langTextService.getTxt(user, "Stavka ", "Stavka ", "Ставка " ) + narda.getAmount(), "none")
                        ), btn.row(
                                btn.button(langTextService.getTxt(user, "Boshlash", "Start", "Начало "), "BACKGAMMON:start")
                        )
                );
                break;
            case PAYMENT_ENTER_SUM:
                markup = btn.markup(
                        btn.row(
                                btn.button(langTextService.getTxt(user, "", "", ""), "")
                        )
                );
                break;
            case HISTORY:
                markup = btn.markup(btn.row(
                        btn.button(langTextService.getTxt(user, "\uD83D\uDDD1 O'chirish", "\uD83D\uDDD1 Delete", "\uD83D\uDDD1 Удалить"), id)
                ));
                break;
            case SETTINGS:
                if (Objects.equals(id, "add")) {
                    markup = btn.markup(btn.row(
                            btn.button(langTextService.getTxt(user, "Karta qoshish", "Add card", "Добавить карту"), "addCard")
                    ));
                } else if (Objects.equals(id, "edit")) {
                    markup = btn.markup(btn.row(
                            btn.button(langTextService.getTxt(user, "Kartani taxrirlash", "Edit card", "Изменить карту"), "editCard")
                    ));
                }
                break;
        }

        return markup;
    }

    @Override
    public ReplyKeyboardMarkup getBtn(User user) {
        State value = State.values()[user.getState().ordinal()];
        switch (value) {
            case SEND_PHONE:
                return btn.markupReplay(
                        btn.rowList(
                                btn.row(btn.button(langTextService.buttonText(user), true, false))
                        )
                );
            case CHOICE_LANG:
                break;
            case UPDATE_LANG:
            case HOME:
                String homeButtons = langTextService.buttonText(user);
                String[] row = homeButtons.split("\n");
                String[] row2 = row[1].split(",");
                String[] row3 = row[2].split(",");
                String[] row4 = row[3].split(",");
                return btn.markupReplay(
                        btn.rowList(
                                btn.row(
                                        btn.button(row[0], false, false)),
                                btn.row(
                                        btn.button(row2[0], false, false), btn.button(row2[1], false, false)),
                                btn.row(
                                        btn.button(row3[0], false, false), btn.button(row3[1], false, false)),
                                btn.row(
                                        btn.button(row4[0], false, false)
                                )
                        )

                );
            case GAMES:
                String[] split = langTextService.buttonText(user).split("\n");
                return btn.markupReplay(
                        btn.rowList(
                                btn.row(btn.button(split[0], false, false)),
                                btn.row(btn.button(split[1], false, false))
                        )
                );

            case EDIT_CARD:
            case ADD_CARD:
            case WITHDRAW:
            case PAYMENT_ENTER_SUM:
                return btn.markupReplay(
                        btn.rowList(
                                btn.row(btn.button(langTextService.getTxt(user, "Ortga", "Back", "Назад"), false, false))
                        )
                );
            case SETTINGS:
                return btn.markupReplay(
                        btn.rowList(
                                btn.row(
                                        btn.button(langTextService.getTxt(user, "Mening Kartam", "My Card", "Моя карточка"), false, false)
                                ),
                                btn.row(btn.button(langTextService.getTxt(user, "Tilni o'zgartirish", "Change language", "Изменение языка"), false, false)),
                                btn.row(btn.button(langTextService.getTxt(user, "Ortga", "Back", "Назад"), false, false))

                        )
                );

        }
        return null;
    }

}
