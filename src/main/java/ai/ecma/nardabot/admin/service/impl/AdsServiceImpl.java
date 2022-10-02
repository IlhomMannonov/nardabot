package ai.ecma.nardabot.admin.service.impl;

import ai.ecma.nardabot.admin.service.abs.AdsService;
import ai.ecma.nardabot.entity.Ads;
import ai.ecma.nardabot.entity.User;
import ai.ecma.nardabot.enums.Lang;
import ai.ecma.nardabot.enums.State;
import ai.ecma.nardabot.payload.SendPhoto;
import ai.ecma.nardabot.payload.SendVideo;
import ai.ecma.nardabot.repository.AdsRepo;
import ai.ecma.nardabot.repository.UserRepo;
import ai.ecma.nardabot.servise.abs.BaseService;
import ai.ecma.nardabot.servise.abs.Btn;
import ai.ecma.nardabot.servise.abs.ButtonService;
import ai.ecma.nardabot.servise.abs.Execute;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {
    private final Execute execute;
    private final BaseService baseService;
    private final ButtonService buttonService;
    private final AdsRepo adsRepo;
    private final Btn btn;
    private final UserRepo userRepo;

    @Override
    public void adsHome(Update update, User user) {
    }

    @Override
    public void addAdsText(Update update, User user) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getChatId());
        sendMessage.enableHtml(true);
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Ads ads = Ads.builder().caption(text).done(false).user(user).build();
            adsRepo.save(ads);

            baseService.setState(user, State.ADS_ADD_MEDIA);
            sendMessage.setText("Yaxshi endi rasm Yoki video yuboring");
            sendMessage.setReplyMarkup(buttonService.getAdminButton(user));

        } else {
            sendMessage.setText("Iltimos text yuboring");
        }
        execute.sendMessage(sendMessage);
    }

    @Override
    public void addMedia(Update update, User user) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getChatId());
        sendMessage.enableHtml(true);
        if (update.getMessage().hasVideo() || update.getMessage().hasPhoto()) {
            Ads ads = adsRepo.findFirstByUserIdOrderByCreatedAtDesc(user.getId());
            if (update.getMessage().hasPhoto()) {
                List<PhotoSize> photo = update.getMessage().getPhoto();
                String fileId = photo.get(photo.size() - 1).getFileId();
                ads.setMedia(fileId);
                ads.setIsPhoto(true);
            } else if (update.getMessage().hasVideo()) {
                Video video = update.getMessage().getVideo();
                ads.setMedia(video.getFileId());
                ads.setIsPhoto(false);
            }
            sendMessage.setText("Media muvoffaqiyatli qo'shildi\nEndi button textlarini yuboring" +
                    "\n\nMisol: <b>u.button:url \n m.button:text </b>" +
                    "\n U = Url uchun \n M = Modal uchun");
            adsRepo.save(ads);
            baseService.setState(user, State.ADS_ADD_BUTTON);
        } else {
            baseService.setState(user, State.ADS_ADD_BUTTON);
            sendMessage.setText("Ok. Media qo'shilmadi");
        }

        execute.sendMessage(sendMessage);

    }

    @Override
    public void addButton(Update update, User user) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getChatId());
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            //BU YERNI REGEX QILISH KK
            String regex = "^(u.|m.)([a-z]).{2,}(:)";
            Pattern pattern = Pattern.compile(regex);
            List<String> url = new LinkedList<>();
            List<String> txt = new LinkedList<>();
            //probellar bo'yicha qirqib olamiz
            String[] probels = text.split("\n");


            if (!pattern.matcher(text).matches()) {
                //url va modall buttonlar ni ajratamiz
                for (String probel : probels) {
                    if (probel.startsWith("u"))
                        url.add(probel);
                    else if (probel.startsWith("m"))
                        txt.add(probel);
                }
                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();

                for (String ur : url) {
                    String[] split = ur.split(",");
                    List<InlineKeyboardButton> buttons = new ArrayList<>();
                    for (String s : split) {
                        int index = s.indexOf(":");
                        InlineKeyboardButton button = new InlineKeyboardButton();
                        button.setText(s.substring(2, index));
                        button.setUrl(s.substring(index + 1));
                        buttons.add(button);
                    }
                    inlineButtons.add(buttons);
                }

                Ads ads = adsRepo.findFirstByUserIdOrderByCreatedAtDesc(user.getId());
                markup.setKeyboard(inlineButtons);
                ads.setMarkup(markup);

                ads.setDone(true);
                adsRepo.save(ads);
                sendMessage.setReplyMarkup(markup);
                createADS(user);

                sendMessage.setText("Reklamani kimlarga yuborasiz\n\nAgar 1 kishi bo'lsa chat id yuboring!");
                sendMessage.setReplyMarkup(buttonService.getAdminButton(user));
                baseService.setState(user, State.ADS_CHOICE_SENDING);
            } else {
                sendMessage.setText("Iltimos faqat matinli shakl yuboring");
            }
        }
        execute.sendMessage(sendMessage);


    }

    private void createADS(User user) {
        Ads ads = adsRepo.findFirstByUserIdOrderByCreatedAtDesc(user.getId());
        if (ads.isDone()) {
            if (ads.getIsPhoto()) {
                SendPhoto build = SendPhoto.builder()
                        .chatId(user.getChatId())
                        .caption(ads.getCaption())
                        .photo(ads.getMedia())
                        .replyMarkup(ads.getMarkup())
                        .build();
                execute.sendPhoto(build);
            } else {


                SendVideo sendVideo = SendVideo.builder()
                        .chatId(user.getChatId())
                        .caption(ads.getCaption())
                        .video(new InputFile(ads.getMedia()))
                        .replyMarkup(ads.getMarkup())
                        .build();
                execute.sendVideo(sendVideo);
            }
        }
    }

    @Override
    public void adsRouter(Update update, User user) {
        String text = update.getMessage().getText();
        switch (text) {
            case "New ADS":
                baseService.setState(user, State.ADS_ADD_TEXT);
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(user.getChatId())
                        .text("Reklama uchun matn kiriting")
                        .replyMarkup(buttonService.getAdminButton(user)).build();
                execute.sendMessage(sendMessage);
                break;
            case "history":

                break;
        }
    }

    @Override
    public void checkSending(Update update, User user) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            switch (text) {
                case "Uzbeklarga":
                    sendAds(Lang.UZ, user, null);
                    break;

                case "Ruslarga":
                    sendAds(Lang.RU, user, null);

                    break;

                case "Inglizlarga":
                    sendAds(Lang.EN, user, null);

                    break;
                case "Hammaga":
                    sendAds(null, user, null);
                    break;
                default:
                    try {
                        sendAds(null, user, text);
                    } catch (Exception e) {
                        SendMessage build = SendMessage.builder()
                                .text("Iltimos tog'ri chat id kiriting")
                                .chatId(user.getChatId())
                                .build();
                        execute.sendMessage(build);
                    }
                    break;
            }
            baseService.setState(user, State.ADS_HOME);
            SendMessage sendMessage = SendMessage.builder()
                    .text("reklama yuborildi")
                    .chatId(user.getChatId())
                    .replyMarkup(buttonService.getAdminButton(user))
                    .build();
            execute.sendMessage(sendMessage);
        }

    }

    private void sendAds(Lang lang, User user, String chatId) {
        Ads ads = adsRepo.findFirstByUserIdOrderByCreatedAtDesc(user.getId());
        List<User> userList = null;
        if (lang != null)
            userList = userRepo.findAllByLanguageAndPhoneIsNotNull(lang);
        else if (chatId == null)
            userList = userRepo.findAllByPhoneIsNotNull();

        SendPhoto sendPhoto = new SendPhoto();
        SendVideo sendVideo = new SendVideo();
        if (ads.getIsPhoto()) {
            sendPhoto.setPhoto(ads.getMedia());
            sendPhoto.setReplyMarkup(ads.getMarkup());
            sendPhoto.setCaption(ads.getCaption());
        } else {
            sendVideo.setVideo(new InputFile(ads.getMedia()));
            sendVideo.setReplyMarkup(ads.getMarkup());
            sendVideo.setCaption(ads.getCaption());
        }
        if (chatId != null) {
            if (ads.getIsPhoto()) {
                sendPhoto.setChatId(chatId);
                execute.sendPhoto(sendPhoto);
            } else {
                sendVideo.setChatId(chatId);
                execute.sendVideo(sendVideo);
            }
            return;
        }

        int i = 0;
        for (User adsUser : userList) {
            i++;
            if (ads.getIsPhoto()) {
                sendPhoto.setChatId(adsUser.getChatId());
                execute.sendPhoto(sendPhoto);
            } else {
                sendVideo.setChatId(adsUser.getChatId());
                execute.sendVideo(sendVideo);
            }
            if (i == 30) {
                try {
                    i = 0;
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

