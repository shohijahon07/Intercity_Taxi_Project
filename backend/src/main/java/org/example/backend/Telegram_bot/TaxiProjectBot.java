package org.example.backend.Telegram_bot;

import lombok.SneakyThrows;
import org.example.backend.entity.*;
import org.example.backend.repository.*;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Component
public class TaxiProjectBot extends TelegramLongPollingBot {
    private final UserRepo userRepo;
    private final RouteDriverRepo routeDriverRepo;
    private final FromCityRepo fromCityRepo;
    private final ToCityRepo toCityRepo;
    private final CommentRepo commentRepo;
    private final CommentRepo1 commentRepo1;
    private final RoleRepo roleRepo;
    private String language;
    private String id;
    private String idDriver;



    public TaxiProjectBot(UserRepo userRepo, RouteDriverRepo routeDriverRepo1, FromCityRepo fromCityRepo, ToCityRepo toCityRepo, CommentRepo commentRepo, RoleRepo roleRepo, CommentRepo1 commentRepo1) {
        this.userRepo = userRepo;
        this.routeDriverRepo = routeDriverRepo1;
        this.fromCityRepo = fromCityRepo;
        this.toCityRepo = toCityRepo;
        this.commentRepo=commentRepo;
        this.roleRepo=roleRepo;
        this.commentRepo1 = commentRepo1;
    }

    @Override
    public String getBotToken() {
        return "BOT_TOKEN";
    }

    @Override
    public String getBotUsername() {
        return "BOT_USERNAME";
    }
    private String[] driver_data = new String[6];
    private String[] driver_data_path = new String[3];
    private String[] band_delete_data = new String[3];
    private  String[] status = new String[6];
    private String name="";
    private String[] dataParts;
    private Integer count=1;
    private UUID idPassenger;
     Map<String, String> ruToUzTranslations = Map.ofEntries(
            Map.entry("Ташкент", "Toshkent"),
            Map.entry("Самарканд", "Samarqand"),
            Map.entry("Бухара", "Buxoro"),
            Map.entry("Наманган", "Namangan"),
            Map.entry("Андижан", "Andijon"),
            Map.entry("Фергана", "Farg'ona"),
            Map.entry("Карши", "Qarshi"),
            Map.entry("Нукус", "Nukus"),
            Map.entry("Джизак", "Jizzax"),
            Map.entry("Хорезм", "Xorazm"),
            Map.entry("Сурхандарья", "Surxondaryo"),
            Map.entry("Сырдарья", "Sirdaryo"),
            Map.entry("Каракалпакстан", "Qaraqalpog'iston"),
            Map.entry( "Урганч","Urganch"),
            Map.entry(  "Термиз","Termiz"),
            Map.entry("Денов" ,"Denov"),
            Map.entry("Хива","Xiva"),
            Map.entry("Қўқон ","Qo'qon")


    );
    Map<String, String> uzToRuTranslations = Map.ofEntries(
            Map.entry("Toshkent", "Ташкент"),
            Map.entry("Samarqand", "Самарканд"),
            Map.entry("Buxoro", "Бухара"),
            Map.entry("Namangan", "Наманган"),
            Map.entry("Andijon", "Андижан"),
            Map.entry("Farg'ona", "Фергана"),
            Map.entry("Qarshi", "Карши"),
            Map.entry("Nukus", "Нукус"),
            Map.entry("Jizzax", "Джизак"),
            Map.entry("Xorazm", "Хорезм"),
            Map.entry("Surxondaryo", "Сурхандарья"),
            Map.entry("Sirdaryo", "Сырдарья"),
            Map.entry("Qaraqalpog'iston", "Каракалпакстан"),
            Map.entry("Urganch", "Урганч"),
            Map.entry("Termiz", "Термиз")
    );

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId.toString());
            System.out.println(Arrays.toString(new String[]{status[0]}));
            User foundUser = userRepo.findByChatId(chatId).orElse(null);
            if (foundUser == null) {
                foundUser = new User();
                foundUser.setChatId(chatId);
            } else {
                foundUser.setChatId(chatId);
            }
            userRepo.save(foundUser);
            System.out.println(foundUser);

            if (message.hasText()) {
                System.out.println("array: " + Arrays.toString(driver_data));
                for (FromCity fromCity : fromCityRepo.findAll()) {
                    name = fromCity.getName();
                }

                    if (message.getText().equalsIgnoreCase("/start") && foundUser.getIsDriver().equals(false)) {
                    sendMessage.setText("Iltimos tilni tanlang! Пожалуйста, выберите язык!");
                    sendMessage.setReplyMarkup(selectLanguageButtons());
                    sendMessage.setChatId(chatId);
                    execute(sendMessage);
                }
                else if (foundUser.getStatus().equals(Status.SET_CITY_FROM_SAVE)) {
                    foundUser.setStatus(Status.GET_PASSENGER_PATH);
                    userRepo.save(foundUser);
                    driver_data_path[0] = message.getText();
                    if(foundUser.getLanguage().equals("uz")){
                        sendMessage.setText("Qayerga bormoqchisiz  \uD83D\uDE97");
                    }else if (foundUser.getLanguage().equals("ru")){
                        sendMessage.setText("Куда вы хотите пойти?   \uD83D\uDE97");
                    }
                    sendMessage.setReplyMarkup(toCitysButtonsReply(foundUser));
                    sendMessage.setChatId(chatId);
                    Message execute = execute(sendMessage);
                    band_delete_data[1]= String.valueOf(execute.getMessageId());
                }
                else if (foundUser.getStatus().equals(Status.GET_PASSENGER_PATH)) {

                    System.out.println(driver_data_path[0]);
                    System.out.println(driver_data_path[1]);
                    driver_data_path[1] = message.getText();
                    String fromCity = translateIfNeeded(driver_data_path[0]);
                    String toCity = translateIfNeeded(driver_data_path[1]);

                    List<Route_Driver> routeDrivers = routeDriverRepo.findAllByFromCityAndToCity(fromCity, toCity);
                    System.out.println(routeDrivers);

                    if (!routeDrivers.isEmpty()) {
                        Route_Driver routeDriver = routeDrivers.get(0);


                        Long chatId1 = routeDriver.getUser().getChatId();
                        if(foundUser.getLanguage().equals("uz")){
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("uz"));
                            String formattedDate = routeDriver.getDay().format(formatter);

                            sendMessage.setText(
                                    "📱 Telefon raqami: " + routeDriver.getUser().getPhoneNumber() + " \n" +
                                            "📅 Sana: " + formattedDate + "\n" +
                                            "⏰ Soati: " + routeDriver.getHour() + "\n" +
                                            "🔢 Bo'sh joy soni: " + routeDriver.getCountSide() + " ta\n" +
                                            "💵 Narxi: " + routeDriver.getPrice() + " So'm"
                            );

                            sendMessage.setReplyMarkup(Passsenger(foundUser,chatId1));

                        }
                        else if(foundUser.getLanguage().equals("ru")) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("ru"));
                            String formattedDate = routeDriver.getDay().format(formatter);
                            sendMessage.setText(
                                    "📱 Номер телефона: " + routeDriver.getUser().getPhoneNumber() + " \n" +
                                            "📅 Дата: " + routeDriver.getDay() + "\n" +
                                            "⏰ Час: " + formattedDate + "\n" +
                                            "🔢 Количество мест: " + routeDriver.getCountSide() + " ta\n" +
                                            "💵 Цена: " + routeDriver.getPrice() + " So'm");
                            sendMessage.setReplyMarkup(Passsenger(foundUser, chatId1));

                        }

                        sendMessage.setChatId(chatId);
                        execute(sendMessage);
                        DeleteMessage deleteMessage = new DeleteMessage();
                        deleteMessage.setMessageId(Integer.valueOf(band_delete_data[1]));
                        deleteMessage.setChatId(chatId);
                        execute(deleteMessage);
                        band_delete_data[1]="";
                    } else {
                        foundUser.setStatus(Status.BACK);
                        userRepo.save(foundUser);
                        sendMessage.setChatId(chatId);
                        if(foundUser.getLanguage().equals("uz")){
                            sendMessage.setText("🚫 Yo’nalish bo’yicha qatnovlar mavjud emas");

                        }else if(foundUser.getLanguage().equals("ru")){
                            sendMessage.setText("🚫 По данному направлению услуг нет.");
                        }
                        sendMessage.setReplyMarkup(NotPath(foundUser));
                        execute(sendMessage);

                        DeleteMessage deleteMessage = new DeleteMessage();
                        deleteMessage.setMessageId(Integer.valueOf(band_delete_data[1]));
                        deleteMessage.setChatId(chatId);

                        execute(deleteMessage);
                    }
                }

                else if (foundUser.getStatus().equals(Status.COMMENT_CREATE)) {
                    System.out.println(foundUser.getStatus());

                    String chatIdStr = foundUser.getCommen_id();
                    System.out.println("Haydovchini ID: " + chatIdStr);
                    System.out.println("Yo'lovchini ID: " + chatId);
                    System.out.println("Text: " + message.getText());

                    try {
                        UUID driverUUID = UUID.fromString(chatIdStr);
                        Optional<User> byChatId1 = userRepo.findById(driverUUID);
                        User user1 = byChatId1.orElse(null);

                        if (user1 != null) {
                            UUID driver_id = user1.getId();
                            Optional<User> byChatId = userRepo.findByChatId(chatId);
                            User user = byChatId.orElse(null);

                            if (user != null) {


                                if (foundUser.getLanguage().equals("uz")) {
                                    String idPassenger = user.getFullName();
                                    String phoneNumber = user.getPhoneNumber();
                                    String name = message.getText();
                                    Comment comment = new Comment(name,phoneNumber, idPassenger, new User(driver_id));
                                    commentRepo.save(comment);
                                    sendMessage.setText("✅ Sizning izohingiz qo'shildi");
                                } else if (foundUser.getLanguage().equals("ru")) {
                                    String idPassenger = user.getFullName();
                                    String phoneNumber = user.getPhoneNumber();
                                    String name = message.getText();
                                    Comment1 comment1 = new Comment1(name,phoneNumber, idPassenger, new User(driver_id));
                                    commentRepo1.save(comment1);
                                    sendMessage.setText("✅ Ваш комментарий добавлен");
                                }

                                sendMessage.setReplyMarkup(NotPath2(foundUser));
                                foundUser.setStatus(Status.HOME_PAGE);
                                userRepo.save(foundUser);
                                sendMessage.setChatId(chatId);
                                execute(sendMessage);

                            } else {
                                sendMessage.setText(foundUser.getLanguage().equals("uz") ? "🔍 Foydalanuvchi topilmadi." : "🔍 Пользователь не найден.");
                                execute(sendMessage);
                            }
                        } else {
                            sendMessage.setText(foundUser.getLanguage().equals("uz") ? "🚫 Haydovchi topilmadi." : "🚫 Драйвер не найден.");
                            execute(sendMessage);
                        }
                    } catch (IllegalArgumentException e) {
                        sendMessage.setText(foundUser.getLanguage().equals("uz") ? "Noto'g'ri formatdagi chat ID: " + chatIdStr : "Идентификатор чата в неправильном формате: " + chatIdStr);
                        execute(sendMessage);
                    }
                    userRepo.save(foundUser);
                }

                else if (foundUser.getStatus().equals(Status.BACK)) {
                    if(foundUser.getLanguage().equals("uz")){
                        sendMessage.setText("📍 Qayerdan");
                    }else if(foundUser.getLanguage().equals("ru")){
                        sendMessage.setText("📍 Откуда");

                    }

                    sendMessage.setReplyMarkup(fromCitysButtonsReply(foundUser));

                    foundUser.setStatus(Status.SET_CITY_FROM_SAVE);
                    userRepo.save(foundUser);
                    sendMessage.setChatId(chatId);
                    execute(sendMessage);
                }
                Optional<User> byChatId = userRepo.findByChatId(chatId);
                List<Route_Driver> all = routeDriverRepo.findAll();

                if (message.getText().equalsIgnoreCase("/start") & foundUser.getIsDriver().equals(true)) {
                    language = "uz";
                    System.out.println(language);
                    foundUser.setStatus(Status.SET_FROM);
                    userRepo.save(foundUser);
                    Route_Driver byUser = routeDriverRepo.findByUser(foundUser);
                    if(byUser!=null){
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("uz"));
                        String formattedDate = byUser.getDay().format(formatter);
                        if (foundUser.getLanguage().equals("uz")) {
                            String fromCityUzbek = translateCityToUzbek(byUser.getFromCity(), ruToUzTranslations);
                            String toCityUzbek = translateCityToUzbek(byUser.getToCity(), ruToUzTranslations);

                            sendMessage.setText(
                                    fromCityUzbek + " 🚖 " + toCityUzbek + "\n" +
                                            "🛋️ Bo'sh-joylar soni: " + byUser.getCountSide() + "\n" +
                                            "💰 Narxi: " + byUser.getPrice() + " so'm\n" +
                                            "📅 Sana: " + formattedDate + " ⏰ " + byUser.getHour()
                            );
                        } else if (foundUser.getLanguage().equals("ru")) {
                            // O'zbek shahar nomlarini ruscha tarjimaga o'girish
                            String fromCityRussian = translateCityToRussian(byUser.getFromCity(), uzToRuTranslations);
                            String toCityRussian = translateCityToRussian(byUser.getToCity(), uzToRuTranslations);

                            sendMessage.setText(
                                    fromCityRussian + " 🚖 " + toCityRussian + "\n" +
                                            "🛋️ Количество вакансий: " + byUser.getCountSide() + "\n" +
                                            "💰 Цена: " + byUser.getPrice() + " сум\n" +
                                            "📅 Дата: " + formattedDate + " ⏰ " + byUser.getHour()
                            );
                        }



                        sendMessage.setReplyMarkup(directionData(byUser.getId(), foundUser));



                    }else{
                        if(foundUser.getLanguage().equals("uz")){
                            sendMessage.setText("🗺️ Yo'nalishingizni kiriting \n 📍 Qayerdan?");

                        }else if(foundUser.getLanguage().equals("ru")){
                            sendMessage.setText("🗺️ Введите пункт назначения\n" +
                                    " \uD83D\uDCCDОткуда?");

                        }
                        sendMessage.setReplyMarkup(fromCitysButtons(foundUser));
                    }
                    execute(sendMessage);
                }
                else if (message.getText().equals("🗺️ Yo'nalishlarim") || message.getText().equals("🗺️ Мои маршруты")) {

                    List<UUID> userIds = userRepo.findAllUserIdsByChatId(chatId);
                    for (Route_Driver routeDriver : routeDriverRepo.findAll()) {
                        if (userIds.contains(routeDriver.getUser().getId())) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("uz"));
                            String formattedDate = routeDriver.getDay().format(formatter);

                            String routeInfo = null;
                            if (foundUser.getLanguage().equals("uz")) {
                                String fromCityUzbek = translateCityToUzbek(routeDriver.getFromCity(), ruToUzTranslations);
                                String toCityUzbek = translateCityToUzbek(routeDriver.getToCity(), ruToUzTranslations);

                                routeInfo = fromCityUzbek + " ➡️ " + toCityUzbek + "\n" +
                                        "🪑 Bo'sh-joylar soni: " + routeDriver.getCountSide() + " \n" +
                                        "💲 Narxi: " + routeDriver.getPrice() + " so'm \n" +
                                        "📅 Sana: " + formattedDate + "\n" +
                                        "⏰ Soat: " + routeDriver.getHour();
                            }
                            else if (foundUser.getLanguage().equals("ru")) {
                                String fromCityRussian = translateCityToRussian(routeDriver.getFromCity(), uzToRuTranslations);
                                String toCityRussian = translateCityToRussian(routeDriver.getToCity(), uzToRuTranslations);

                                routeInfo = fromCityRussian + " ➡️ " + toCityRussian + "\n" +
                                        "🪑 Количество вакансий: " + routeDriver.getCountSide() + " \n" +
                                        "💲 Цена: " + routeDriver.getPrice() + " сум \n" +
                                        "📅 Дата: " + formattedDate + "\n" +
                                        "⏰ Час: " + routeDriver.getHour();
                            }


                            sendMessage.setReplyMarkup(directionData(routeDriver.getId(), foundUser));
                            sendMessage.setText(routeInfo);
                            sendMessage.setChatId(chatId);
                            Message sentMessage = execute(sendMessage);



                            band_delete_data[1] = String.valueOf(sentMessage.getMessageId());
                        }
                    }
                }
                else if(message.getText().equals("👤 O'zim haqimda")||message.getText().equals("\uD83D\uDC64 Обо мне")){
                    Optional<User> byChatId1 = userRepo.findByChatId(chatId);
                    if(foundUser.getLanguage().equals("uz")){
                        sendMessage.setText(
                                "👤 Ism familyangiz: " + byChatId1.get().getFullName() + "\n" +
                                        "📞 Telefon raqamingiz: " + byChatId1.get().getPhoneNumber() + "\n" +
                                        "🚗 Mashinangiz: " + byChatId1.get().getCarType() + "\n" +
                                        "\uD83D\uDCDD  O'zim haqimda: " + byChatId1.get().getAbout() + "\n"
                        );
                    }else {
                        sendMessage.setText(
                                "👤 Ваша фамилия: " + byChatId1.get().getFullName() + "\n" +
                                        "📞 Ваш номер телефона: " + byChatId1.get().getPhoneNumber() + "\n" +
                                        "🚗 Ваша машина: " + byChatId1.get().getCarType() + "\n" +
                                        "\uD83D\uDCDD  О себе: " + byChatId1.get().getAbout() + "\n"
                        );
                    }

                    sendMessage.setReplyMarkup(directionDataPassenger(byChatId.get().getId(),foundUser));
                    sendMessage.setChatId(chatId);
                    execute(sendMessage);
                }

                else if (foundUser.getStatus().equals(Status.NEW_NUMBER)) {
                    System.out.println("kirdi");
                    String countS = message.getText();
                    Optional<Route_Driver> byId = routeDriverRepo.findById(UUID.fromString(id));
                    if (byId.isPresent()) {
                        Route_Driver routeDriver = byId.get();

                        try {
                            int count = Integer.parseInt(countS);

                            if (routeDriver.getUser().isCount()) {
                                if (count > 6) {
                                    if (foundUser.getLanguage().equals("uz")) {
                                        sendMessage.setText("⚠️ Joylar soni 6 dan katta bo'lmasligi kerak. Qayta kiriting.");
                                    } else if (foundUser.getLanguage().equals("ru")) {
                                        sendMessage.setText("⚠️ Количество мест не должно превышать 6. Пожалуйста, введите снова.");
                                    }
                                    execute(sendMessage);
                                    return;
                                }
                            } else {
                                if (count > 12) {
                                    if (foundUser.getLanguage().equals("uz")) {
                                        sendMessage.setText("⚠️ Joylar soni 12 dan katta bo'lmasligi kerak. Qayta kiriting.");
                                    } else if (foundUser.getLanguage().equals("ru")) {
                                        sendMessage.setText("⚠️ Количество мест не должно превышать 12. Пожалуйста, введите снова.");
                                    }
                                    execute(sendMessage);
                                    return;
                                }
                            }

                            routeDriver.setCountSide(count);
                            routeDriverRepo.save(routeDriver);

                            if (foundUser.getLanguage().equals("uz")) {
                                sendMessage.setText("✅ Joylar soni muvaffaqiyatli qo'shildi");
                            } else if (foundUser.getLanguage().equals("ru")) {
                                sendMessage.setText("✅ Количество успешно добавленных мест");
                            }
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("uz"));
                            String formattedDate = routeDriver.getDay().format(formatter);
                            String routeInfo = null;
                            if (foundUser.getLanguage().equals("uz")) {
                                String fromCityUzbek = translateCityToUzbek(routeDriver.getFromCity(), ruToUzTranslations);
                                String toCityUzbek = translateCityToUzbek(routeDriver.getToCity(), ruToUzTranslations);

                                routeInfo = fromCityUzbek + " ➡️ " + toCityUzbek + "\n" +
                                        "🪑 Bo'sh-joylar soni: " + routeDriver.getCountSide() + " \n" +
                                        "💲 Narxi: " + routeDriver.getPrice() + " so'm \n" +
                                        "📅 Sana: " + formattedDate + "\n" +
                                        "⏰ Soat: " + routeDriver.getHour();
                            } else if (foundUser.getLanguage().equals("ru")) {
                                String fromCityRussian = translateCityToRussian(routeDriver.getFromCity(), uzToRuTranslations);
                                String toCityRussian = translateCityToRussian(routeDriver.getToCity(), uzToRuTranslations);

                                routeInfo = fromCityRussian + " ➡️ " + toCityRussian + "\n" +
                                        "🪑 Количество вакансий: " + routeDriver.getCountSide() + " \n" +
                                        "💲 Цена: " + routeDriver.getPrice() + " сум \n" +
                                        "📅 Дата: " + formattedDate + "\n" +
                                        "⏰ Час: " + routeDriver.getHour();
                            }
                            sendMessage.setText(routeInfo);
                            sendMessage.setReplyMarkup(directionData(routeDriver.getId(), foundUser));
                            execute(sendMessage);

                        } catch (NumberFormatException e) {
                            if (foundUser.getLanguage().equals("uz")) {
                                sendMessage.setText("⚠️ Kechirasiz, noto'g'ri qiymat kiritildi. Faqat sonlarni kiriting.");
                            } else if (foundUser.getLanguage().equals("ru")) {
                                sendMessage.setText("⚠️ Извините, было введено неверное значение. Просто введите цифры.");
                            }
                            execute(sendMessage);
                        }

                    } else {
                        if (foundUser.getLanguage().equals("uz")) {
                            sendMessage.setText("❌ Xatolik.");
                        } else if (foundUser.getLanguage().equals("ru")) {
                            sendMessage.setText("❌ Ошибка.");
                        }
                        execute(sendMessage);
                    }
                }
                else if (foundUser.getStatus().equals(Status.NEW_PRICE)) {
                    String countS = message.getText();
                    Optional<Route_Driver> byId = routeDriverRepo.findById(UUID.fromString(id));
                    if (byId.isPresent()) {
                        Route_Driver routeDriver = byId.get();
                        try {
                            int count = Integer.parseInt(countS);
                            routeDriver.setPrice(count);
                            routeDriverRepo.save(routeDriver);

                            if (foundUser.getLanguage().equals("uz")) {
                                sendMessage.setText("✅ Narxi muvaffaqiyatli qo'shildi\n");
                            } else if (foundUser.getLanguage().equals("ru")) {
                                sendMessage.setText("✅ Цена успешно добавлена\n");
                            }

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("uz"));
                            String formattedDate = routeDriver.getDay().format(formatter);
                            String routeInfo = null;
                            if (foundUser.getLanguage().equals("uz")) {
                                String fromCityUzbek = translateCityToUzbek(routeDriver.getFromCity(), ruToUzTranslations);
                                String toCityUzbek = translateCityToUzbek(routeDriver.getToCity(), ruToUzTranslations);

                                routeInfo = fromCityUzbek + " ➡️ " + toCityUzbek + "\n" +
                                        "🪑 Bo'sh-joylar soni: " + routeDriver.getCountSide() + " \n" +
                                        "💲 Narxi: " + routeDriver.getPrice() + " so'm \n" +
                                        "📅 Sana: " + formattedDate + "\n" +
                                        "⏰ Soat: " + routeDriver.getHour();
                            } else if (foundUser.getLanguage().equals("ru")) {
                                String fromCityRussian = translateCityToRussian(routeDriver.getFromCity(), uzToRuTranslations);
                                String toCityRussian = translateCityToRussian(routeDriver.getToCity(), uzToRuTranslations);

                                routeInfo = fromCityRussian + " ➡️ " + toCityRussian + "\n" +
                                        "🪑 Количество вакансий: " + routeDriver.getCountSide() + " \n" +
                                        "💲 Цена: " + routeDriver.getPrice() + " сум \n" +
                                        "📅 Дата: " + formattedDate + "\n" +
                                        "⏰ Час: " + routeDriver.getHour();
                            }


                            sendMessage.setText(sendMessage.getText() + routeInfo);

                            sendMessage.setReplyMarkup(directionData(routeDriver.getId(), foundUser));
                            execute(sendMessage);

                        } catch (NumberFormatException e) {
                            if (foundUser.getLanguage().equals("uz")) {
                                sendMessage.setText("⚠️ Kechirasiz, noto'g'ri qiymat kiritildi. Faqat sonlarni kiriting.");
                            } else {
                                sendMessage.setText("⚠️ Извините, было введено неверное значение. Просто введите цифры.");
                            }
                            execute(sendMessage);
                        }
                    } else {
                        if (foundUser.getLanguage().equals("uz")) {
                            sendMessage.setText("❌ Xatolik.");
                        } else {
                            sendMessage.setText("❌ Ошибка.");
                        }
                        execute(sendMessage);
                    }
                }
                else if (foundUser.getStatus().equals(Status.NEW_DAY)) {
                    try {
                        System.out.println("kirdi");

                        LocalDate inputDate = validateAndParseDate(message.getText(), foundUser);

                        Optional<Route_Driver> byId = routeDriverRepo.findById(UUID.fromString(id));
                        if (byId.isPresent()) {
                            Route_Driver routeDriver = byId.get();
                            routeDriver.setDay(inputDate);
                            routeDriverRepo.save(routeDriver);

                            String successMessage = "";
                            if (foundUser.getLanguage().equals("uz")) {
                                successMessage = "📅 Sana muvaffaqiyatli yangilandi\n";
                            } else if (foundUser.getLanguage().equals("ru")) {
                                successMessage = "📅 Дата успешно обновлена\n";
                            }
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("uz"));
                            String formattedDate = routeDriver.getDay().format(formatter);
                            String routeInfo = null;
                            if (foundUser.getLanguage().equals("uz")) {
                                String fromCityUzbek = translateCityToUzbek(routeDriver.getFromCity(), ruToUzTranslations);
                                String toCityUzbek = translateCityToUzbek(routeDriver.getToCity(), ruToUzTranslations);

                                routeInfo = fromCityUzbek + " ➡️ " + toCityUzbek + "\n" +
                                        "🪑 Bo'sh-joylar soni: " + routeDriver.getCountSide() + " \n" +
                                        "💲 Narxi: " + routeDriver.getPrice() + " so'm \n" +
                                        "📅 Sana: " + formattedDate + "\n" +
                                        "⏰ Soat: " + routeDriver.getHour();
                            } else if (foundUser.getLanguage().equals("ru")) {
                                String fromCityRussian = translateCityToRussian(routeDriver.getFromCity(), uzToRuTranslations);
                                String toCityRussian = translateCityToRussian(routeDriver.getToCity(), uzToRuTranslations);

                                routeInfo = fromCityRussian + " ➡️ " + toCityRussian + "\n" +
                                        "🪑 Количество вакансий: " + routeDriver.getCountSide() + " \n" +
                                        "💲 Цена: " + routeDriver.getPrice() + " сум \n" +
                                        "📅 Дата: " + formattedDate + "\n" +
                                        "⏰ Час: " + routeDriver.getHour();
                            }


                            sendMessage.setText(successMessage + routeInfo);
                            sendMessage.setReplyMarkup(directionData(routeDriver.getId(), foundUser));
                            execute(sendMessage);
                            DeleteMessage deleteMessage = new DeleteMessage();
                            deleteMessage.setMessageId(Integer.valueOf(band_delete_data[1]));
                            deleteMessage.setChatId(chatId);

                            execute(deleteMessage);
                        } else {
                            sendMessage.setText("❌ Xatolik.");
                            execute(sendMessage);
                        }

                    } catch (DateTimeParseException e) {
                        sendMessage.setText("⚠️ Noto'g'ri format.");
                        execute(sendMessage);
                    } catch (Exception e) {
                        sendMessage.setText("❌ Xatolik yuz berdi.");
                        execute(sendMessage);
                    }
                }
                else if (foundUser.getStatus().equals(Status.NEW_TIME)) {
                    System.out.println("kirdi");
                    System.out.println(id);
                    String time = message.getText().trim();
                    Optional<Route_Driver> byId = routeDriverRepo.findById(UUID.fromString(id));
                    try {
                        if (byId.isPresent()) {
                            Route_Driver routeDriver = byId.get();
                            LocalDate day = routeDriver.getDay();

                            validateTime(time, String.valueOf(day), foundUser);

                            routeDriver.setHour(time);
                            routeDriverRepo.save(routeDriver);

                            String successMessage = "";
                            if (foundUser.getLanguage().equals("uz")) {
                                successMessage = "📲 Soat muvaffaqiyatli yangilandi\n";
                            } else if (foundUser.getLanguage().equals("ru")) {
                                successMessage = "📲 Часы успешно обновлены\n";
                            }
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("uz"));
                            String formattedDate = routeDriver.getDay().format(formatter);
                            String routeInfo = "";


                            if (foundUser.getLanguage().equals("uz")) {
                                String fromCityUzbek = translateCityToUzbek(routeDriver.getFromCity(), ruToUzTranslations);
                                String toCityUzbek = translateCityToUzbek(routeDriver.getToCity(), ruToUzTranslations);

                                routeInfo = fromCityUzbek + " ➡️ " + toCityUzbek + "\n" +
                                        "🪑 Bo'sh-joylar soni: " + routeDriver.getCountSide() + " \n" +
                                        "💲 Narxi: " + routeDriver.getPrice() + " so'm \n" +
                                        "📅 Sana: " + formattedDate + "\n" +
                                        "⏰ Soat: " + routeDriver.getHour();
                            } else if (foundUser.getLanguage().equals("ru")) {
                                String fromCityRussian = translateCityToRussian(routeDriver.getFromCity(), uzToRuTranslations);
                                String toCityRussian = translateCityToRussian(routeDriver.getToCity(), uzToRuTranslations);

                                routeInfo = fromCityRussian + " ➡️ " + toCityRussian + "\n" +
                                        "🪑 Количество вакансий: " + routeDriver.getCountSide() + " \n" +
                                        "💲 Цена: " + routeDriver.getPrice() + " сум \n" +
                                        "📅 Дата: " + formattedDate + "\n" +
                                        "⏰ Час: " + routeDriver.getHour();
                            }



                            sendMessage.setText(successMessage + routeInfo);
                            sendMessage.setReplyMarkup(directionData(routeDriver.getId(), foundUser));
                            execute(sendMessage);

                        } else {
                            if (foundUser.getLanguage().equals("uz")) {
                                sendMessage.setText("❌ Xatolik mavjud.");
                            } else if (foundUser.getLanguage().equals("ru")) {
                                sendMessage.setText("❌ Ошибка существует.");
                            }
                            execute(sendMessage);
                        }

                    } catch (DateTimeParseException e) {
                        sendMessage.setText("⚠️ Noto'g'ri vaqt formati kiritildi. Faqat soat va minutni kiriting, masalan '14:30'.");
                        execute(sendMessage);
                    } catch (Exception e) {
                        sendMessage.setText("❌ Xatolik yuz berdi. Iltimos, qayta urinib ko'ring.");
                        execute(sendMessage);
                    }
                }


                else if (foundUser.getStatus().equals(Status.SET_GO_MONEY)) {
                    try {
                        foundUser.setStatus(Status.SET_DAY_MONTH);
                        userRepo.save(foundUser);
                        Integer.parseInt(message.getText()); // Ensuring the input is a number
                        driver_data[3] = message.getText();


                        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                        List<KeyboardRow> keyboard = new ArrayList<>();

                        // Get today's, tomorrow's, and the day after tomorrow's dates
                        LocalDate today = LocalDate.now();
                        LocalDate tomorrow = today.plusDays(1);
                        LocalDate dayAfterTomorrow = today.plusDays(2);

                        // Define the formatter and locale dynamically based on user language
                        DateTimeFormatter dayMonthFormatter;
                        if (foundUser.getLanguage().equals("uz")) {
                            // Use Uzbek locale for Uzbek month names
                            dayMonthFormatter = DateTimeFormatter.ofPattern("dd-MMMM", Locale.forLanguageTag("uz"));
                        } else if (foundUser.getLanguage().equals("ru")) {
                            // Use Russian locale for Russian month names
                            dayMonthFormatter = DateTimeFormatter.ofPattern("dd-MMMM", Locale.forLanguageTag("ru"));
                            System.out.println(dayMonthFormatter);
                        } else {
                            // Fallback if language is neither Uzbek nor Russian (optional)
                            dayMonthFormatter = DateTimeFormatter.ofPattern("dd-MMMM", Locale.getDefault());
                        }

                        // Row 1 - Button for today's date
                        KeyboardRow row1 = new KeyboardRow();
                        row1.add(today.format(dayMonthFormatter));

                        // Row 2 - Button for tomorrow's date
                        KeyboardRow row2 = new KeyboardRow();
                        row2.add(tomorrow.format(dayMonthFormatter));

                        // Row 3 - Button for the day after tomorrow's date
                        KeyboardRow row3 = new KeyboardRow();
                        row3.add(dayAfterTomorrow.format(dayMonthFormatter));

                        // Add all rows to the keyboard layout
                        keyboard.add(row1);
                        keyboard.add(row2);
                        keyboard.add(row3);

                        // Set the keyboard layout and resize it for mobile
                        replyKeyboardMarkup.setKeyboard(keyboard);
                        replyKeyboardMarkup.setResizeKeyboard(true);

                        if (foundUser.getLanguage().equals("uz")) {
                            sendMessage.setText("🔄 Iltimos, sanani tanlang:");
                        } else if (foundUser.getLanguage().equals("ru")) {
                            sendMessage.setText("🔄 Пожалуйста, выберите дату:");
                        }

                        sendMessage.setReplyMarkup(replyKeyboardMarkup);
                        sendMessage.setChatId(chatId);
                        Message execute = execute(sendMessage);
band_delete_data[1]= String.valueOf(execute.getMessageId());

                    } catch (NumberFormatException e) {
                        // Handle invalid number input
                        foundUser.setStatus(Status.SET_GO_MONEY);
                        userRepo.save(foundUser);
                        if (foundUser.getLanguage().equals("uz")) {
                            sendMessage.setText("⚠️ Noto'g'ri format. Iltimos, faqat son kiritishingiz kerak.");
                        } else if (foundUser.getLanguage().equals("ru")) {
                            sendMessage.setText("⚠️ Неверный формат. Пожалуйста, введите только число.");
                        }
                        sendMessage.setChatId(chatId);
                        execute(sendMessage);
                    }
                }
                else if (foundUser.getStatus().equals(Status.SET_DAY_MONTH)) {
                    try {
                        foundUser.setStatus(Status.SET_TIME);
                        userRepo.save(foundUser);
                        System.out.println(message.getText());
                        LocalDate inputDate = validateAndParseDate(message.getText(), foundUser);
                        System.out.println("saa"+inputDate);
                        driver_data[4] = inputDate.toString();

                        if (foundUser.getLanguage().equals("uz")) {
                            sendMessage.setText("🕒 Soatni kiriting (masalan, 01:50)");
                        } else if (foundUser.getLanguage().equals("ru")) {
                            sendMessage.setText("🕒 Введите время (например, 01:50)");
                        }

                        sendMessage.setChatId(chatId);
                        execute(sendMessage);
                        DeleteMessage deleteMessage = new DeleteMessage();
                        deleteMessage.setMessageId(Integer.valueOf(band_delete_data[1]));
                        deleteMessage.setChatId(chatId);
                        execute(deleteMessage);


                    } catch (DateTimeParseException e) {
                        foundUser.setStatus(Status.SET_DAY_MONTH);
                        userRepo.save(foundUser);

                        if (foundUser.getLanguage().equals("uz")) {
                            sendMessage.setText("⚠️ Noto'g'ri format.");
                        } else if (foundUser.getLanguage().equals("ru")) {
                            sendMessage.setText("⚠️ Неверный формат.");
                        }

                        sendMessage.setChatId(chatId);
                        execute(sendMessage);

                    } catch (Exception e) {
                        foundUser.setStatus(Status.SET_DAY_MONTH);
                        userRepo.save(foundUser);

                        System.out.println("Error: " + e.getMessage());
                        e.printStackTrace();

                        if (foundUser.getLanguage().equals("uz")) {
                            sendMessage.setText("❗ Xatolik yuz berdi. Iltimos, qayta urinib ko'ring.");
                        } else if (foundUser.getLanguage().equals("ru")) {
                            sendMessage.setText("❗ Произошла ошибка. Пожалуйста, попробуйте еще раз.");
                        }

                        sendMessage.setChatId(chatId);
                        execute(sendMessage);
                    }
                }
                else if (foundUser.getStatus().equals(Status.SET_TIME)) {
                    try {
                        List<UUID> userIds = userRepo.findAllUserIdsByChatId(chatId);
                        String timeText = message.getText().trim();

                        validateTime(timeText, driver_data[4],foundUser);

                        driver_data[5] = timeText;

                        if (!userIds.isEmpty()) {
                            User user = userRepo.findById(userIds.get(0)).orElseThrow(() -> new IllegalStateException("User not found"));

                            String fromCity = driver_data[0];
                            String toCity = driver_data[1];

                            String countSideStr = driver_data[2].replaceAll("[^0-9]", "");
                            Integer countSide = Integer.valueOf(countSideStr);

                            String priceStr = driver_data[3].replaceAll("[^0-9]", "");
                            Integer price = Integer.valueOf(priceStr);

                            LocalDate day = LocalDate.parse(driver_data[4]);

                            Route_Driver routeDriver = new Route_Driver(fromCity, toCity, countSide, price, day, timeText, user);
                            routeDriverRepo.save(routeDriver);
                            if(foundUser.getLanguage().equals("uz")){
                                sendMessage.setText("✅ Muvaffaqiyatli qo'shildi");

                            }else if(foundUser.getLanguage().equals("ru"
                            )){
                                sendMessage.setText("✅ Добавлено успешно");

                            }

                            sendMessage.setReplyMarkup(directions(foundUser));
                            Message execute = execute(sendMessage);
                            driver_data = new String[6];
                        } else {
                            sendMessage.setText("User not found for the given chat ID");
                            execute(sendMessage);
                        }
                    } catch (IllegalArgumentException e) {
                        sendMessage.setText(e.getMessage());
                        execute(sendMessage);
                    }

                }
                if(message.getText().equals("\uD83C\uDFE0  Bosh sahifa") & foundUser.getStatus().equals(Status.HOME_PAGE)){
                    foundUser.setStatus(Status.SET_CITY_FROM_SAVE);
                    userRepo.save(foundUser);
                    if(foundUser.getLanguage().equals("uz")){
                        sendMessage.setText("Qayerdan \uD83C\uDF0D");
                    }else if(foundUser.getLanguage().equals("ru")){
                        sendMessage.setText("Откуда \uD83C\uDF0D");
                    }
                    sendMessage.setReplyMarkup(fromCitysButtonsReply(foundUser));
                    sendMessage.setChatId(chatId);
                    execute(sendMessage);
                }
                else if(foundUser.getStatus().equals(Status.HOME_PAGE_DRIVER)){
                    foundUser.setStatus(Status.SET_FROM);
                    userRepo.save(foundUser);
                    if(foundUser.getLanguage().equals("uz")){
                        sendMessage.setText("🗺️ Yo'nalishingizni kiriting \n 📍 Qayerdan?");
                    }else if(foundUser.getLanguage().equals("ru")){
                        sendMessage.setText("🗺️ Введите направление \n 📍 Откуда?");
                    }
                    sendMessage.setReplyMarkup(fromCitysButtons(foundUser));
                    sendMessage.setChatId(chatId);
                    execute(sendMessage);
                    DeleteMessage deleteMessage = new DeleteMessage();
                    deleteMessage.setMessageId(Integer.valueOf(band_delete_data[2]));
                    deleteMessage.setChatId(chatId);
                    execute(deleteMessage);
                }
else if(foundUser.getStatus().equals(Status.SET_FULL_NAME)){
                    System.out.println("aaaww");
    foundUser.setFullName(message.getText());
    foundUser.setStatus(Status.NONE);
    userRepo.save(foundUser);
    if(foundUser.getLanguage().equals("uz")){
        sendMessage.setText(
                "✅ Muvaffaqiyat yangilandi \n 👤 Ism familyangiz: " + foundUser.getFullName() + "\n" +
                        "📞 Telefon raqamingiz: " + foundUser.getPhoneNumber() + "\n" +
                        "🚗 Mashinangiz: " + foundUser.getCarType() + "\n" +
                        "\uD83D\uDCDD  O'zim haqimda: " + foundUser.getAbout() + "\n"
        );
    }
    else {

        sendMessage.setText(
                "✅ Успех обновлен \n 👤 Ваша фамилия: " + foundUser.getFullName() + "\n" +
                        "📞 Ваш номер телефона: " + foundUser.getPhoneNumber() + "\n" +
                        "🚗Ваша машина: " + foundUser.getCarType() + "\n" +
                        "\uD83D\uDCDD  О себе: " + foundUser.getAbout() + "\n"
        );
    }

                    sendMessage.setReplyMarkup(directionDataPassenger(foundUser.getId(),foundUser));
                    sendMessage.setChatId(chatId);
    execute(sendMessage);
                }
else if(foundUser.getStatus().equals(Status.SET_PHONE_NUMBER)){
                    System.out.println("aaaww");
                    foundUser.setPhoneNumber(message.getText());
                    foundUser.setStatus(Status.NONE);
                    userRepo.save(foundUser);
                    if(foundUser.getLanguage().equals("uz")){
                        sendMessage.setText(
                                "✅ Muvaffaqiyat yangilandi \n 👤 Ism familyangiz: " + foundUser.getFullName() + "\n" +
                                        "📞 Telefon raqamingiz: " + foundUser.getPhoneNumber() + "\n" +
                                        "🚗 Mashinangiz: " + foundUser.getCarType() + "\n" +
                                        "\uD83D\uDCDD  O'zim haqimda: " + foundUser.getAbout() + "\n"
                        );
                    }
                    else {

                        sendMessage.setText(
                                "✅ Успех обновлен \n 👤 Ваша фамилия: " + foundUser.getFullName() + "\n" +
                                        "📞 Ваш номер телефона: " + foundUser.getPhoneNumber() + "\n" +
                                        "🚗Ваша машина: " + foundUser.getCarType() + "\n" +
                                        "\uD83D\uDCDD  О себе: " + foundUser.getAbout() + "\n"
                        );
                    }
                    sendMessage.setReplyMarkup(directionDataPassenger(foundUser.getId(),foundUser));

                    sendMessage.setChatId(chatId);
                    execute(sendMessage);
                }
                else if(foundUser.getStatus().equals(Status.SET_CAR)){
                    System.out.println("aaaww");
                    foundUser.setCarType(message.getText());
                    foundUser.setStatus(Status.NONE);
                    userRepo.save(foundUser);
                    if(foundUser.getLanguage().equals("uz")){
                        sendMessage.setText(
                                "✅ Muvaffaqiyat yangilandi \n 👤 Ism familyangiz: " + foundUser.getFullName() + "\n" +
                                        "📞 Telefon raqamingiz: " + foundUser.getPhoneNumber() + "\n" +
                                        "🚗 Mashinangiz: " + foundUser.getCarType() + "\n" +
                                        "\uD83D\uDCDD  O'zim haqimda: " + foundUser.getAbout() + "\n"
                        );
                    }
                    else {

                        sendMessage.setText(
                                "✅ Успех обновлен \n 👤 Ваша фамилия: " + foundUser.getFullName() + "\n" +
                                        "📞 Ваш номер телефона: " + foundUser.getPhoneNumber() + "\n" +
                                        "🚗Ваша машина: " + foundUser.getCarType() + "\n" +
                                        "\uD83D\uDCDD  О себе: " + foundUser.getAbout() + "\n"
                        );
                    }
                    sendMessage.setReplyMarkup(directionDataPassenger(foundUser.getId(),foundUser));

                    sendMessage.setChatId(chatId);
                    execute(sendMessage);
                }
                else if(foundUser.getStatus().equals(Status.SET_MYSELF)){
                    System.out.println("aaaww");
                    foundUser.setAbout(message.getText());
                    foundUser.setStatus(Status.NONE);
                    userRepo.save(foundUser);
                    if(foundUser.getLanguage().equals("uz")){
                        sendMessage.setText(
                                "✅ Muvaffaqiyat yangilandi \n 👤 Ism familyangiz: " + foundUser.getFullName() + "\n" +
                                        "📞 Telefon raqamingiz: " + foundUser.getPhoneNumber() + "\n" +
                                        "🚗 Mashinangiz: " + foundUser.getCarType() + "\n" +
                                        "\uD83D\uDCDD  O'zim haqimda: " + foundUser.getAbout() + "\n"
                        );
                    }
                    else {

                        sendMessage.setText(
                                "✅ Успех обновлен \n 👤 Ваша фамилия: " + foundUser.getFullName() + "\n" +
                                        "📞 Ваш номер телефона: " + foundUser.getPhoneNumber() + "\n" +
                                        "🚗Ваша машина: " + foundUser.getCarType() + "\n" +
                                        "\uD83D\uDCDD  О себе: " + foundUser.getAbout() + "\n"
                        );
                    }
                    sendMessage.setReplyMarkup(directionDataPassenger(foundUser.getId(),foundUser));
                    sendMessage.setChatId(chatId);
                    execute(sendMessage);
                }

            }
            else if (message.hasContact()) {
                int contactMessageId = message.getMessageId();

                String phoneNumber;
                phoneNumber = message.getContact().getPhoneNumber();
                String firstName = message.getContact().getFirstName();
                String lastName = message.getContact().getLastName();
                String fullName;

                if (lastName == null || lastName.isEmpty()) {
                    fullName = firstName;
                    foundUser.setFullName(fullName);

                } else {
                    fullName = firstName + " " + lastName;
                    foundUser.setFullName(fullName);

                }

                foundUser.setStatus(Status.DIRECTIONS);
                foundUser.setPhoneNumber(phoneNumber);
                userRepo.save(foundUser);

                if (foundUser.getLanguage().equals("uz")) {
                    sendMessage.setText("Assalom eleykum botimizga xush kelibsiz! \uD83D\uDC4B\n" +
                            "Pastdagi knopkalardan birini tanlang. ⬇️ Siz haydovchimi \uD83D\uDE95 yoki yo'lovchi \uDDF3?");
                }
                else if (foundUser.getLanguage().equals("ru")) {
                    sendMessage.setText("Здравствуйте и добро пожаловать в наш бот! \uD83D\uDC4B\n"  +
                            " «Выберите одну из кнопок ниже. ⬇\uFE0F Вы водитель \uD83D\uDE95 или пассажир \uDDF3?");
                }
                sendMessage.setReplyMarkup(selectInlineRoleButtons(chatId));
                execute(sendMessage);

                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(chatId);
                deleteMessage.setMessageId(Integer.valueOf(band_delete_data[1]));
                execute(deleteMessage);

                DeleteMessage deleteContactMessage = new DeleteMessage();
                deleteContactMessage.setChatId(chatId);
                deleteContactMessage.setMessageId(contactMessageId);
                execute(deleteContactMessage);
            }

        }
        else if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            String data = update.getCallbackQuery().getData();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            Optional<User> userOptional = userRepo.findByChatId(chatId);
            User user = userOptional.orElseGet(() -> {
                User newUser = new User();
                newUser.setChatId(chatId);
                return userRepo.save(newUser);
            });
            System.out.println(data);
            List<FromCity> allFromCities = fromCityRepo.findAll();
            List<ToCity> allToCities = toCityRepo.findAll();
            if (data.equals("uz") && !user.getIsDriver()) {
                language = "uz";
                user.setLanguage("uz");
                userRepo.save(user);
                if(user.getPhoneNumber()!=null){
                    sendMessage.setText("Assalom eleykum botimizga xush kelibsiz! \uD83D\uDC4B\n" +
                            "Pastdagi knopkalardan birini tanlang. ⬇️ Siz haydovchimi \uD83D\uDE95 yoki yo'lovchi \uDDF3?");

                    sendMessage.setReplyMarkup(selectInlineRoleButtons(chatId));
                }else {
                    sendMessage.setText("☎️ Kontakkingizni yuboring ");
                    sendMessage.setReplyMarkup(genContactButtons(user));
                }


                Message executedMessage = execute(sendMessage);
                band_delete_data[1] = String.valueOf(executedMessage.getMessageId());
            }
            else if (data.equals("ru") && !user.getIsDriver()) {
                user.setLanguage("ru");
                userRepo.save(user);
                language = "ru";
                if(user.getPhoneNumber()!=null){
                    sendMessage.setText("Здравствуйте и добро пожаловать в наш бот! \uD83D\uDC4B\n"  +
                            " «Выберите одну из кнопок ниже. ⬇\uFE0F Вы водитель \uD83D\uDE95 или пассажир \uDDF3?");
                    sendMessage.setReplyMarkup(selectInlineRoleButtons(chatId));
                }else {
                    sendMessage.setText("☎\uFE0FОтправьте свой контакт");
                    sendMessage.setReplyMarkup(genContactButtons(user));
                }

                execute(sendMessage);
            }

            else if (data.equals("Passengers")) {
                user.setStatus(Status.SET_CITY_FROM_SAVE);
                List<Role> roles = new ArrayList<>();
                Role driverRole = roleRepo.findByName("ROLE_USER");
                if (driverRole == null) {
                    driverRole = new Role("ROLE_USER");
                    roleRepo.save(driverRole);
                }
                roles.add(driverRole);
                user.setRoles(roles);
                userRepo.save(user);

                if (user.getLanguage().equals("uz")) {
                    sendMessage.setText("Qayerdan \uD83C\uDF0D");
                } else if (user.getLanguage().equals("ru")) {
                    sendMessage.setText("Откуда \uD83C\uDF0D");
                }
                sendMessage.setReplyMarkup(fromCitysButtonsReply(user));
                sendMessage.setChatId(chatId);
                execute(sendMessage);

                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setMessageId(Integer.valueOf(band_delete_data[1]));
                execute(deleteMessage);
            }
else if(data.equals("fullName")){
    user.setStatus(Status.SET_FULL_NAME);
    userRepo.save(user);
                if (user.getLanguage().equals("uz")) {
                    sendMessage.setText("✏️ Ismingizni yangi qiymatini kiriting");
                } else if (user.getLanguage().equals("ru")) {
                    sendMessage.setText("✏️ Введите новое имя");
                }
                sendMessage.setChatId(chatId);
                execute(sendMessage);
            }
else if(data.equals("phoneNumber")){
                user.setStatus(Status.SET_PHONE_NUMBER);
                userRepo.save(user);
                if (user.getLanguage().equals("uz")) {
                    sendMessage.setText("✏️ Telefon yangi qiymatini kiriting");
                } else if (user.getLanguage().equals("ru")) {
                    sendMessage.setText("✏️ Телефон янги кийматини киритинг");
                }
                sendMessage.setChatId(chatId);
                execute(sendMessage);
            }
else if(data.equals("car")){
                user.setStatus(Status.SET_CAR);
                userRepo.save(user);
                if (user.getLanguage().equals("uz")) {
                    sendMessage.setText("✏️ Mashinani  yangi nomi kiriting");
                } else if (user.getLanguage().equals("ru")) {
                    sendMessage.setText("✏️ Введите новое имя машины");
                }
                sendMessage.setChatId(chatId);
                execute(sendMessage);
            }
            else if(data.equals("myself")){
                user.setStatus(Status.SET_MYSELF);
                userRepo.save(user);
                if (user.getLanguage().equals("uz")) {
                    sendMessage.setText("✏️ O'zingiz haqingizda yangi qiymatni kiriting  kiriting");
                } else if (user.getLanguage().equals("ru")) {
                    sendMessage.setText("✏️ Введите новое значение о себе");
                }
                sendMessage.setChatId(chatId);
                execute(sendMessage);
            }


            boolean cityFound = false;

            if (user.getStatus().equals(Status.SET_FROM)) {
                for (FromCity city : allFromCities) {
                    if (city.getName().equals(data)) {
                        cityFound = true;
                        driver_data[0] = data;
                        user.setStatus(Status.SET_TO);
                        if(user.getLanguage().equals("uz")){
                            sendMessage.setText("Yo'nalishingizni kiriting \n Qayerga?");

                        }else if(user.getLanguage().equals("ru")){
                            sendMessage.setText("Введите пункт назначения\n куда?");

                        }
                        sendMessage.setReplyMarkup(toCitysButtons(user));
                        userRepo.save(user);
                        execute(sendMessage);

                        return;
                    }
                }
            }
            if (data.startsWith("place")) {
                String[] dataParts = data.split(":");
                System.out.println("place kirdi");
                if(user.getLanguage().equals("uz")){
                    sendMessage.setText("Joylar soni yangi qiymatini kiriting:");
                }else if(user.getLanguage().equals("ru")){
                    sendMessage.setText("Введите новое значение количества слотов:");

                }
                status[0]="place";
                if (dataParts.length > 1) {
                    id = String.valueOf(UUID.fromString(dataParts[1]));
                    System.out.println(id);
                    user.setStatus(Status.NEW_NUMBER);
                    userRepo.save(user);
                    sendMessage.setChatId(chatId.toString());
                    sendMessage.setReplyMarkup(new ForceReplyKeyboard());
                    execute(sendMessage);
                }
            }
            else if(data.startsWith("money")){
                String[] dataParts = data.split(":");
                System.out.println("money kirdi");
                if(user.getLanguage().equals("uz")){
                    sendMessage.setText("Narxni yangi qiymatini kiriting:");
                }else if(user.getLanguage().equals("ru")){
                    sendMessage.setText("Введите новое значение цены:");

                }
                status[0]="money";
                if (dataParts.length > 1) {
                    id = String.valueOf(UUID.fromString(dataParts[1]));
                    System.out.println(id);
                    user.setStatus(Status.NEW_PRICE);
                    userRepo.save(user);
                    sendMessage.setChatId(chatId.toString());
                    sendMessage.setReplyMarkup(new ForceReplyKeyboard());
                    execute(sendMessage);
                }
            }
            else if(data.startsWith("day")){
                String[] dataParts = data.split(":");
                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                List<KeyboardRow> keyboard = new ArrayList<>();

                // Get today's, tomorrow's, and the day after tomorrow's dates
                LocalDate today = LocalDate.now();
                LocalDate tomorrow = today.plusDays(1);
                LocalDate dayAfterTomorrow = today.plusDays(2);

                // Define the formatter and locale dynamically based on user language
                DateTimeFormatter dayMonthFormatter;
                if (user.getLanguage().equals("uz")) {
                    // Use Uzbek locale for Uzbek month names
                    dayMonthFormatter = DateTimeFormatter.ofPattern("dd-MMMM", Locale.forLanguageTag("uz"));
                } else if (user.getLanguage().equals("ru")) {
                    // Use Russian locale for Russian month names
                    dayMonthFormatter = DateTimeFormatter.ofPattern("dd-MMMM", Locale.forLanguageTag("ru"));
                    System.out.println(dayMonthFormatter);
                } else {
                    // Fallback if language is neither Uzbek nor Russian (optional)
                    dayMonthFormatter = DateTimeFormatter.ofPattern("dd-MMMM", Locale.getDefault());
                }

                // Row 1 - Button for today's date
                KeyboardRow row1 = new KeyboardRow();
                row1.add(today.format(dayMonthFormatter));

                // Row 2 - Button for tomorrow's date
                KeyboardRow row2 = new KeyboardRow();
                row2.add(tomorrow.format(dayMonthFormatter));

                // Row 3 - Button for the day after tomorrow's date
                KeyboardRow row3 = new KeyboardRow();
                row3.add(dayAfterTomorrow.format(dayMonthFormatter));

                keyboard.add(row1);
                keyboard.add(row2);
                keyboard.add(row3);

                replyKeyboardMarkup.setKeyboard(keyboard);
                replyKeyboardMarkup.setResizeKeyboard(true);
                System.out.println("day kirdi");
                if(user.getLanguage().equals("uz")){
                    sendMessage.setText("Sanani yangi qiymatini tanlang");
                    sendMessage.setReplyMarkup(replyKeyboardMarkup);
                }else if(user.getLanguage().equals("ru")){
                    sendMessage.setText("Введите новое значение даты:");
                    sendMessage.setReplyMarkup(replyKeyboardMarkup);


                }

                status[0]="day";
                if (dataParts.length > 1) {
                    id = String.valueOf(UUID.fromString(dataParts[1]));
                    System.out.println(id);
                    user.setStatus(Status.NEW_DAY);
                    userRepo.save(user);
                    sendMessage.setChatId(chatId.toString());
//                    sendMessage.setReplyMarkup(new ForceReplyKeyboard());
                    Message execute = execute(sendMessage);
                    band_delete_data[1]= String.valueOf(execute.getMessageId());
                }
            }
            else if(data.startsWith("time")){
                String[] dataParts = data.split(":");
                System.out.println("time kirdi");
                if(user.getLanguage().equals("uz")){
                    sendMessage.setText("Soatni  yangi qiymatini kiriting:");
                }else if(user.getLanguage().equals("ru")){
                    sendMessage.setText("Введите новое значение часа:");

                }
                if (dataParts.length > 1) {
                    id = String.valueOf(UUID.fromString(dataParts[1]));
                    System.out.println(id);
                    user.setStatus(Status.NEW_TIME);
                    userRepo.save(user);
                    sendMessage.setChatId(chatId.toString());
                    sendMessage.setReplyMarkup(new ForceReplyKeyboard());
                    execute(sendMessage);
                }
            }
            else if (data.startsWith("next")) {
                String fromCity = driver_data_path[0];
                String toCity = driver_data_path[1];
                List<Route_Driver> routeDrivers = routeDriverRepo.findAllByFromCityAndToCity(fromCity, toCity);
                System.out.println(routeDrivers);

                if (routeDrivers.isEmpty()) {
                    sendMessage.setChatId(chatId);
                    if(user.getLanguage().equals("uz")){
                        sendMessage.setText("Yo'nalishlar topilmadi.");

                    }else if(user.getLanguage().equals("ru")){
                        sendMessage.setText("Маршруты не найдены.");
                    }
                    user.setStatus(Status.BACK);
                    userRepo.save(user);
                    execute(sendMessage);
                    return;
                }

                if (count >= routeDrivers.size()) {
                    sendMessage.setChatId(chatId);
                    if(user.getLanguage().equals("uz")){
                        sendMessage.setText("Bu yo'nalishga tegishlilar tugadi.");

                    }else if(user.getLanguage().equals("ru")){
                        sendMessage.setText("Относящиеся к этому направлению закончились.");

                    }
                    count=0;
                    user.setStatus(Status.BACK);
                    userRepo.save(user);
                    execute(sendMessage);
                } else {

                    Route_Driver routeDriver = routeDrivers.get(count);
                    count++;



                    Long chatId2 = routeDriver.getUser().getChatId();

                    String formattedDate = null;
                    if (user.getLanguage().equals("uz")) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("uz"));
                        formattedDate = routeDriver.getDay().format(formatter);
                        sendMessage.setText(
                                "Telefon raqami: " + routeDriver.getUser().getPhoneNumber() + " \n" +
                                        "Sana: " + formattedDate + "\n" +
                                        "Bo'sh joy soni: " + routeDriver.getCountSide() + " ta\n" +
                                        "Narxi: " + routeDriver.getPrice() + " So'm");
                    } else if (user.getLanguage().equals("ru")) {
                        sendMessage.setText(
                                "Номер телефона: " + routeDriver.getUser().getPhoneNumber() + " \n" +
                                        "Дата: " + formattedDate + "\n" +
                                        "Пустое число радости: " + routeDriver.getCountSide() + " ta\n" +
                                        "Цена: " + routeDriver.getPrice() + " So'm");
                    }

                    sendMessage.setReplyMarkup(Passsenger(user, chatId2));
                    execute(sendMessage);
                }
            }
            else if(data.startsWith("band")) {
                Optional<User> allByChatId = userRepo.findByChatId(chatId);
                User user1 = allByChatId.get();

                dataParts = data.split(":");
                idPassenger = user1.getId();

                Optional<User> byChatId2 = userRepo.findByChatId(Long.valueOf(dataParts[1]));

                if(dataParts.length > 1) {
                    if(byChatId2.get().getLanguage().equals("uz")) {
                        sendMessage.setParseMode("Markdown");
                        sendMessage.setText(
                                "👤 Siz [" + user1.getFullName() + "](tg://user?id=" + user1.getChatId() + ") yo'lovchini qabul qilasizmi? \n" +
                                        "📞 Telefon raqamlar: "  + user1.getPhoneNumber()
                        );
                    } else if(byChatId2.get().getLanguage().equals("ru")) {
                        sendMessage.setParseMode("Markdown");
                        sendMessage.setText(
                                "👤 Ты [" + user1.getFullName() + "](tg://user?id=" + user1.getChatId() + "), вы принимаете пассажиров? \n" +
                                        "📞 Номер телефона : "  + user1.getPhoneNumber()
                        );
                    }

                    sendMessage.setReplyMarkup(sendBusy(user1));
                    sendMessage.setChatId(dataParts[1]);
                    Message sentMessage = execute(sendMessage);

                    band_delete_data[1] = String.valueOf(sentMessage.getMessageId());

                    SendMessage passengerMessage = new SendMessage();
                    passengerMessage.setChatId(String.valueOf(user1.getChatId()));
                    if(user1.getLanguage().equals("uz")) {
                        passengerMessage.setText("Sizga  haydovchi  tez orada bog'lanadi.");
                    } else if(user1.getLanguage().equals("ru")) {
                        passengerMessage.setText("Водитель свяжется с вами в ближайшее время.");
                    }
                    execute(passengerMessage);
                }
            }

            else if (data.startsWith("comment")) {
                String[] splitData = data.split(":");
                Optional<User> byChatId = userRepo.findByChatId(Long.valueOf(splitData[1]));

                if (byChatId.isPresent()) {
                    List<Comment> allByUserId = commentRepo.findAllByUserId(byChatId.get().getId());
                    List<Comment1> allByUserId1 = commentRepo1.findAllByUserId(byChatId.get().getId());

                    sendMessage.setChatId(chatId);

                    StringBuilder messageText = new StringBuilder();

                    if (user.getLanguage().equals("uz")) {
                        if (allByUserId.isEmpty()) {
                            messageText.append("📭 Hozircha mijozlarni fikr yo'q.");
                        } else {
                            messageText.append("🚗 Haydovchi: ").append(allByUserId.get(0).getUser().getFullName()).append("\n")
                                    .append("📝 Mijozlarni fikri:  \n");

                            for (Comment comment : allByUserId) {
                                messageText.append("👤 Mijozni ismi: ").append(comment.getName()).append("\n")
                                        .append("💬 Mijozni fikr: ").append(comment.getText()).append("\n")
                                        .append("----------------------------------")
                                        .append("\n");
                            }
                        }
                    } else {
                        if (allByUserId1.isEmpty()) {
                            messageText.append("📭 Пока что мнений клиентов нет.");
                        } else {
                            messageText.append("🚗 Водитель: ").append(allByUserId1.get(0).getUser().getFullName()).append("\n")
                                    .append("📝 Мнение клиента:  \n");

                            for (Comment1 comment2 : allByUserId1) {
                                messageText.append("👤 Имя клиента: ").append(comment2.getName()).append("\n")
                                        .append("💬 Мнение клиента: ").append(comment2.getText()).append("\n")
                                        .append("----------------------------------")
                                        .append("\n");
                            }
                        }
                    }

                    sendMessage.setText(messageText.toString());
                    execute(sendMessage);
                }
            }


            else if (data.equals("ha")) {
                Optional<User> userOptional1 = userRepo.findByChatId(chatId);

                if (userOptional1.isPresent()) {
                    User user1 = userOptional1.get();
                    Route_Driver byUser = routeDriverRepo.findByUser(Optional.of(user1));

                    if (byUser != null && byUser.getCountSide() > 0) {
                        List<UUID> currentPassengers = byUser.getPassenger();

                        if (currentPassengers == null) {
                            currentPassengers = new ArrayList<>();
                        }

                        if (currentPassengers.contains(idPassenger)) {
                            if (user.getLanguage().equals("uz")) {
                                sendMessage.setText("Siz bu yo'lovchini qabul qilgansiz.");
                            } else if (user.getLanguage().equals("ru")) {
                                sendMessage.setText("Этот пассажир уже был принят.");
                            }
                        } else {
                            currentPassengers.add(idPassenger);
                            byUser.setPassenger(currentPassengers);

                            Integer countSide = byUser.getCountSide();
                            byUser.setCountSide(countSide - 1);
                            routeDriverRepo.save(byUser);

                            // Check if countSide is now 0
                           if (byUser.getCountSide() == 0) {
                                routeDriverRepo.delete(byUser);

                                if (user.getLanguage().equals("uz")) {
                                    sendMessage.setText("Siz so'ngi yo'lovchini qabul qildingiz. ✅ Sizda 🚫 jo'ylar soni tugadi.");
                                } else if (user.getLanguage().equals("ru")) {
                                    sendMessage.setText("Вы приняли последнего пассажира. ✅ На вашем маршруте 🚫 закончились места.");
                                }


                                  sendMessage.setReplyMarkup(NotPath3(user));
                                user.setStatus(Status.HOME_PAGE_DRIVER);
                                userRepo.save(user);
                                sendMessage.setChatId(chatId);
                               Message execute = execute(sendMessage);
                               Integer messageId = execute.getMessageId();
                               band_delete_data[2]= String.valueOf(messageId);
                           }

                                Optional<User> byId = userRepo.findById(idPassenger);
                                byId.get().setStatus(Status.COMMENT_CREATE);
                                byId.get().setCommen_id(String.valueOf(user.getId()));
                                sendMessage.setText("✍️ Haydovchiga izoh qoldiring!");
                                sendMessage.setChatId(byId.get().getChatId());
                                execute(sendMessage);
                                userRepo.save(byId.get());


                            if (byUser.getCountSide() > 0) {
                                List<UUID> userIds = userRepo.findAllUserIdsByChatId(chatId);
                                for (Route_Driver routeDriver : routeDriverRepo.findAll()) {
                                    if (userIds.contains(routeDriver.getUser().getId())) {
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("uz"));
                                        String formattedDate = routeDriver.getDay().format(formatter);
                                        if (user.getLanguage().equals("uz")) {
                                            String fromCityUzbek = translateCityToUzbek(byUser.getFromCity(), ruToUzTranslations);
                                            String toCityUzbek = translateCityToUzbek(byUser.getToCity(), ruToUzTranslations);

                                            sendMessage.setText(
                                                    fromCityUzbek + " 🚖 " + toCityUzbek + "\n" +
                                                            "🛋️ Bo'sh-joylar soni: " + byUser.getCountSide() + "\n" +
                                                            "💰 Narxi: " + byUser.getPrice() + " so'm\n" +
                                                            "📅 Sana: " + formattedDate + " ⏰ " + byUser.getHour()
                                            );
                                        } else if (user.getLanguage().equals("ru")) {
                                            // O'zbek shahar nomlarini ruscha tarjimaga o'girish
                                            String fromCityRussian = translateCityToRussian(byUser.getFromCity(), uzToRuTranslations);
                                            String toCityRussian = translateCityToRussian(byUser.getToCity(), uzToRuTranslations);

                                            sendMessage.setText(
                                                    fromCityRussian + " 🚖 " + toCityRussian + "\n" +
                                                            "🛋️ Количество вакансий: " + byUser.getCountSide() + "\n" +
                                                            "💰 Цена: " + byUser.getPrice() + " сум\n" +
                                                            "📅 Дата: " + formattedDate + " ⏰ " + byUser.getHour()
                                            );
                                        }

                                        sendMessage.setChatId(chatId);
                                        sendMessage.setReplyMarkup(directionData(routeDriver.getId(), user));
                                        execute(sendMessage);
                                    }
                                }
                            }
                        }
                    }

                    else {
                        if (user.getLanguage().equals("uz")) {
                            sendMessage.setText(
                                    "🚫 Sizda joylar soni tugadi. "
                            );
                            sendMessage.setReplyMarkup(NotPath3(user));
                            user.setStatus(Status.HOME_PAGE_DRIVER);
                            userRepo.save(user);
                        } else if (user.getLanguage().equals("ru")) {
                            sendMessage.setText(
                                    "🚫 У вас закончились места. "
                            );
                            sendMessage.setReplyMarkup(NotPath3(user));
                            user.setStatus(Status.HOME_PAGE_DRIVER);
                            userRepo.save(user);
                        }


                        routeDriverRepo.deleteById(byUser.getId());
                        execute(sendMessage);
                    }
                }
            }
            else if (data.startsWith("accept_")) {
                String[] parts = data.split("_");
                User byUser2 = null;
                UUID idPassenger1 = null;
                if (parts.length == 3) {
                    String action = parts[0];
                    idPassenger1 = UUID.fromString(parts[1]);
                    Long driverChatId = Long.parseLong(parts[2]);
                    byUser2 = userRepo.findByChatId(driverChatId).orElseThrow();

                    System.out.println(idPassenger1);
                    System.out.println(driverChatId);
                }

                Route_Driver byUser1 = routeDriverRepo.findByUser(Optional.ofNullable(byUser2));
                if (byUser1.getCountSide() == 0) {
                    routeDriverRepo.delete(byUser1);

                    if (user.getLanguage().equals("uz")) {
                        sendMessage.setText("Siz so'ngi yo'lovchini qabul qildingiz. ✅ Sizda 🚫 jo'ylar soni tugadi.");
                    } else if (user.getLanguage().equals("ru")) {
                        sendMessage.setText("Вы приняли последнего пассажира. ✅ На вашем маршруте 🚫 закончились места.");
                    }


                    sendMessage.setReplyMarkup(NotPath3(user));
                    user.setStatus(Status.HOME_PAGE_DRIVER);
                    userRepo.save(user);
                    sendMessage.setChatId(chatId);
                    execute(sendMessage);
                }

                if (byUser1.getCountSide() > 0) {
                    if (byUser1 != null) {
                        List<UUID> currentPassengers = byUser1.getPassenger();
                        if (currentPassengers == null) {
                            currentPassengers = new ArrayList<>();
                        }

                        if (currentPassengers.contains(idPassenger1)) {
                            if (user.getLanguage().equals("uz")) {
                                sendMessage.setText("❌ Siz bu yo'lovchini allaqachon qabul qilgansiz.");
                            } else if (user.getLanguage().equals("ru")) {
                                sendMessage.setText("❌ Вы уже приняли этого пассажира.");
                            }
                            sendMessage.setChatId(chatId);
                            execute(sendMessage);
                        } else {
                            currentPassengers.add(idPassenger1);
                            byUser1.setPassenger(currentPassengers);

                            Integer countSide = byUser1.getCountSide();
                            byUser1.setCountSide(countSide - 1);
                            routeDriverRepo.save(byUser1);

                            List<UUID> userIds = userRepo.findAllUserIdsByChatId(byUser2.getChatId());
                            for (Route_Driver routeDriver : routeDriverRepo.findAll()) {
                                if (userIds.contains(routeDriver.getUser().getId())) {
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("uz"));
                                    String formattedDate = routeDriver.getDay().format(formatter);
                                    if (user.getLanguage().equals("uz")) {
                                        String fromCityUzbek = translateCityToUzbek(routeDriver.getFromCity(), ruToUzTranslations);
                                        String toCityUzbek = translateCityToUzbek(routeDriver.getToCity(), ruToUzTranslations);

                                        sendMessage.setText(
                                                fromCityUzbek + " 🚖 " + toCityUzbek + "\n" +
                                                        "🛋️ Bo'sh-joylar soni: " + routeDriver.getCountSide() + "\n" +
                                                        "💰 Narxi: " + routeDriver.getPrice() + " so'm\n" +
                                                        "📅 Sana: " + formattedDate + " ⏰ " + routeDriver.getHour()
                                        );
                                    } else if (user.getLanguage().equals("ru")) {
                                        // O'zbek shahar nomlarini ruscha tarjimaga o'girish
                                        String fromCityRussian = translateCityToRussian(routeDriver.getFromCity(), uzToRuTranslations);
                                        String toCityRussian = translateCityToRussian(routeDriver.getToCity(), uzToRuTranslations);

                                        sendMessage.setText(
                                                fromCityRussian + " 🚖 " + toCityRussian + "\n" +
                                                        "🛋️ Количество вакансий: " + routeDriver.getCountSide() + "\n" +
                                                        "💰 Цена: " + routeDriver.getPrice() + " сум\n" +
                                                        "📅 Дата: " + formattedDate + " ⏰ " + routeDriver.getHour()
                                        );
                                    }


                                    sendMessage.setChatId(chatId);
                                    sendMessage.setReplyMarkup(directionData(routeDriver.getId(), user));
                                    execute(sendMessage);
                                }
                            }
                        }
                    }
                } else {
                    if (user.getLanguage().equals("uz")) {
                        sendMessage.setText(
                                "🚫 Sizda joylar soni tugadi. "

                        );
                        sendMessage.setReplyMarkup(NotPath3(user));
                        user.setStatus(Status.HOME_PAGE_DRIVER);
                        userRepo.save(user);
                    } else if (user.getLanguage().equals("ru")) {
                        sendMessage.setText(
                                "🚫 У вас закончились места. "

                        );
                        sendMessage.setReplyMarkup(NotPath3(user));
                        user.setStatus(Status.HOME_PAGE_DRIVER);
                        userRepo.save(user);
                    }

                    routeDriverRepo.deleteById(byUser1.getId());
                    execute(sendMessage);
                }
            }


            else if (data.startsWith("decline_")) {
                String[] parts = data.split("_");
                Optional<User> byChatId= null;
                UUID idPassenger1=null;
                if (parts.length == 3) {
                    String action = parts[0];
                    idPassenger1 = UUID.fromString(parts[1]);
                    Long driverChatId = Long.parseLong(parts[2]);
                    byChatId = userRepo.findByChatId(driverChatId);
                    System.out.println(idPassenger1);
                    System.out.println(driverChatId);
                }
                if (byChatId.isPresent()) {
                    Route_Driver byUser = routeDriverRepo.findByUser(Optional.of(byChatId.get()));

                    if (byUser != null) {
                        if (byUser.getPassenger() != null && byUser.getPassenger().contains(idPassenger1)) {
                            byUser.getPassenger().remove(idPassenger1);
                            Integer countSide = byUser.getCountSide();
                            byUser.setCountSide(countSide+1);
                            routeDriverRepo.save(byUser);

                            sendMessage.setChatId(byChatId.get().getChatId());
                            if(user.getLanguage().equals("uz")){
                                sendMessage.setText("✅ Siz yo'lovchini o'chirdiz");
                            }else if(user.getLanguage().equals("ru")){
                                sendMessage.setText("✅ Вы удалили пассажира");

                            }

                            execute(sendMessage);


                        } else {
                            sendMessage.setChatId(byChatId.get().getChatId());
                            if(user.getLanguage().equals("uz")){
                                sendMessage.setText("✅ Siz yo'lovchini o'chirdiz");
                            }else if(user.getLanguage().equals("ru")){
                                sendMessage.setText("✅ Вы удалили пассажира");

                            }
                            execute(sendMessage);


                        }
                    }
                } else {
                    sendMessage.setChatId(byChatId.get().getChatId());
                    if(user.getLanguage().equals("uz")){
                        sendMessage.setText("👤 Foydalanuvchi topilmadi.");

                    }else if(user.getLanguage().equals("ru")){
                        sendMessage.setText("👤 Foydalanuvchi topilmadi.");

                    }
                    execute(sendMessage);
                }
            }


            else if (data.equals("yo'q")) {
                Optional<User> byChatId = userRepo.findByChatId(chatId);
                if (byChatId.isPresent()) {
                    Route_Driver byUser = routeDriverRepo.findByUser(Optional.of(byChatId.get()));

                    if (byUser != null) {
                        if (byUser.getPassenger() != null && byUser.getPassenger().contains(idPassenger)) {
                            byUser.getPassenger().remove(idPassenger);
                            Integer countSide = byUser.getCountSide();
                            byUser.setCountSide(countSide+1);
                            routeDriverRepo.save(byUser);

                            sendMessage.setChatId(chatId);
                            if(user.getLanguage().equals("uz")){
                                sendMessage.setText("✅ Siz yo'lovchini o'chirdiz");
                            }else if(user.getLanguage().equals("ru")){
                                sendMessage.setText("✅ Вы удалили пассажира");

                            }

                            execute(sendMessage);

                            DeleteMessage deleteMessage = new DeleteMessage();
                            deleteMessage.setChatId(chatId);
                            deleteMessage.setMessageId(Integer.valueOf(band_delete_data[1]));
                            band_delete_data[1] = "";
                            execute(deleteMessage);
                        } else {
                            sendMessage.setChatId(chatId);
                            if(user.getLanguage().equals("uz")){
                                sendMessage.setText("✅ Siz yo'lovchini o'chirdiz");
                            }else if(user.getLanguage().equals("ru")){
                                sendMessage.setText("✅ Вы удалили пассажира");

                            }
                            execute(sendMessage);

                            if (!band_delete_data[1].isEmpty()) {
                                DeleteMessage deleteMessage = new DeleteMessage();
                                deleteMessage.setChatId(chatId);
                                deleteMessage.setMessageId(Integer.valueOf(band_delete_data[1]));
                                band_delete_data[1] = "";
                                execute(deleteMessage);
                            }
                        }
                    } else {
                        sendMessage.setChatId(chatId);
                        if(user.getLanguage().equals("uz")){
                            sendMessage.setText("🚗 Haydovchi topilmadi.");

                        }else if(user.getLanguage().equals("ru")){
                            sendMessage.setText("🚗 Драйвер не найден.");

                        }
                        execute(sendMessage);
                    }
                } else {
                    sendMessage.setChatId(chatId);
                    if(user.getLanguage().equals("uz")){
                        sendMessage.setText("👤 Foydalanuvchi topilmadi.");

                    }else if(user.getLanguage().equals("ru")){
                        sendMessage.setText("👤 Foydalanuvchi topilmadi.");

                    }
                    execute(sendMessage);
                }
            }


            if (user.getStatus().equals(Status.SET_TO)) {
                for (ToCity city : allToCities) {
                    if (city.getName().equals(data)) {
                        cityFound = true;
                        user.setStatus(Status.SET_COUNT_SIDE);
                        driver_data[1] = data;
                        if (user.getLanguage().equals("uz")) {
                            sendMessage.setText("🔢 Nechta joy bor?");

                        }else if(user.getLanguage().equals("ru")){
                            sendMessage.setText("🔢 Есть несколько мест?");

                        }
                        sendMessage.setReplyMarkup(countseatButtons(user));
                        userRepo.save(user);
                        execute(sendMessage);
                        return;
                    }
                }
            }
            else if (data.startsWith("del")) {
                String[] dataParts = data.split(":");
                id = String.valueOf(UUID.fromString(dataParts[1]));
                Optional<Route_Driver> byId = routeDriverRepo.findById(UUID.fromString(id));

                if(byId.isPresent()) {
                    Route_Driver routeDriver = byId.get();
                    UUID id1 = routeDriver.getId();
                    routeDriverRepo.deleteById(id1);
                    if(user.getLanguage().equals("uz")){
                        sendMessage.setText("✅ Ma'lumot muvaffaqiyatli o'chirildi. ");
                    }else if(user.getLanguage().equals("ru")){
                        sendMessage.setText("✅Данные успешно удалены. ");
                    }
                    sendMessage.setReplyMarkup(NotPath3(user));
                    user.setStatus(Status.HOME_PAGE_DRIVER);
                    userRepo.save(user);
                    Message execute = execute(sendMessage);
                    band_delete_data[2]= String.valueOf(execute.getMessageId());
                    sendMessage.setChatId(chatId.toString());
                    sendMessage.setReplyMarkup(new ForceReplyKeyboard());
                    DeleteMessage deleteMessage = new DeleteMessage();

                    if (band_delete_data[1] != null) {
                        System.out.println("keldi " + band_delete_data[1]);
                        deleteMessage.setMessageId(Integer.valueOf(band_delete_data[1]));
                        deleteMessage.setChatId(chatId.toString());
                        execute(deleteMessage);
                    } else {
                        System.out.println("No message ID to delete.");
                    }
                } else {
                    System.out.println("Route_Driver not found with ID: " + id);
                }

            }

            if (user.getStatus().equals(Status.SET_COUNT_SIDE)) {
                if (user.isCount()) {
                    switch (data) {
                        case "1 ta":
                        case "2 ta":
                        case "3 ta":
                        case "4 ta":
                        case "5 ta":
                        case "6 ta":
                        case "1 место":
                        case "2 места":
                        case "3 места":
                        case "4 места":
                        case "5 мест":
                        case "6 мест":
                            driver_data[2] = data;
                            user.setStatus(Status.SET_GO_MONEY);
                            if (user.getLanguage().equals("uz")) {
                                sendMessage.setText("💵 Necha pulga olib ketasiz?");
                            } else if (user.getLanguage().equals("ru")) {
                                sendMessage.setText("💵 Сколько вы возьмете?");
                            }
                            userRepo.save(user);
                            execute(sendMessage);
                            return;
                        default:
                            break;
                    }
                } else {
                    switch (data) {
                        case "1 ta":
                        case "2 ta":
                        case "3 ta":
                        case "4 ta":
                        case "5 ta":
                        case "6 ta":
                        case "7 ta":
                        case "8 ta":
                        case "9 ta":
                        case "10 ta":
                        case "11 ta":
                        case "12 ta":
                        case "1 место":
                        case "2 места":
                        case "3 места":
                        case "4 места":
                        case "5 мест":
                        case "6 мест":
                        case "7 мест":
                        case "8 мест":
                        case "9 мест":
                        case "10 мест":
                        case "11 мест":
                        case "12 мест":
                            driver_data[2] = data;
                            user.setStatus(Status.SET_GO_MONEY);
                            if (user.getLanguage().equals("uz")) {
                                sendMessage.setText("💵 Necha pulga olib ketasiz?");
                            } else if (user.getLanguage().equals("ru")) {
                                sendMessage.setText("💵 Сколько вы возьмете?");
                            }
                            userRepo.save(user);
                            execute(sendMessage);
                            return;
                        default:
                            break;
                    }
                }
            }


        }
    }
    private InlineKeyboardMarkup directionDataPassenger(UUID id, User foundUser) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        // First Row: Change Full Name
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        if (foundUser.getLanguage().equals("uz")) {
            button1.setText("✍️ Ism familyani o'zgartirish:");
        } else if (foundUser.getLanguage().equals("ru")) {
            button1.setText("✍️ Изменить имя семьи:");
        }
        button1.setCallbackData("fullName");
        firstRow.add(button1);
        rows.add(firstRow);

        // Second Row: Change Phone Number
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        if (foundUser.getLanguage().equals("uz")) {
            button2.setText("📞 Telefon raqamni o'zgartirish");
        } else if (foundUser.getLanguage().equals("ru")) {
            button2.setText("📞 Изменить номер телефона");
        }
        button2.setCallbackData("phoneNumber");
        secondRow.add(button2);
        rows.add(secondRow);

        // Third Row: Change Car Name
        List<InlineKeyboardButton> thirdRow = new ArrayList<>();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        if (foundUser.getLanguage().equals("uz")) {
            button3.setText("🚗 Mashinani nomini o'zgartirish");
        } else if (foundUser.getLanguage().equals("ru")) {
            button3.setText("🚗 Переименуйте машину");
        }
        button3.setCallbackData("car");
        thirdRow.add(button3);
        rows.add(thirdRow);

        // Fourth Row: Change About Info
        List<InlineKeyboardButton> fourthRow = new ArrayList<>();
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        if (foundUser.getLanguage().equals("uz")) {
            button4.setText("ℹ️ O'zingiz haqqingizdagi ma'lumotlarni o'zgartirish");
        } else if (foundUser.getLanguage().equals("ru")) {
            button4.setText("ℹ️ Измените информацию о себе");
        }
        button4.setCallbackData("myself");
        fourthRow.add(button4);
        rows.add(fourthRow);

        return new InlineKeyboardMarkup(rows);
    }
    private ReplyKeyboardMarkup NotPath3(User foundUser){
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton button1 = new KeyboardButton();
        if(foundUser.getLanguage().equals("uz")){
            button1.setText("\uD83C\uDFE0  Bosh sahifa");

        }else if(foundUser.getLanguage().equals("ru")){
            button1.setText("\uD83C\uDFE0  Домашняя страница");

        }

        row1.add(button1);


        rows.add(row1);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);

        return replyKeyboardMarkup;
    }
    private ReplyKeyboardMarkup NotPath2(User foundUser){
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton button1 = new KeyboardButton();
        if(foundUser.getLanguage().equals("uz")){
            button1.setText("\uD83C\uDFE0  Bosh sahifa");

        }else if(foundUser.getLanguage().equals("ru")){
            button1.setText("\uD83C\uDFE0  Домашняя страница");

        }

        row1.add(button1);


        rows.add(row1);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);

        return replyKeyboardMarkup;
    }
    private ReplyKeyboardMarkup NotPath(User foundUser){
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton button1 = new KeyboardButton();
        if(foundUser.getLanguage().equals("uz")){
            button1.setText("🔙 Orqaga qaytish");

        }else if(foundUser.getLanguage().equals("ru")){
            button1.setText("🔙 Возвращаться");

        }

        row1.add(button1);


        rows.add(row1);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);

        return replyKeyboardMarkup;
    }
    private InlineKeyboardMarkup sendBusy(User user) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        if(user.getLanguage().equals("uz")){
            button1.setText("✅ Ha:");

        }else if(user.getLanguage().equals("ru")){
            button1.setText(" ✅ Да:");

        }
        button1.setCallbackData("ha");
        firstRow.add(button1);
        rows.add(firstRow);

        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        if(user.getLanguage().equals("uz")){
            button2.setText("❌ Yo'q");

        }else if(user.getLanguage().equals("ru")){
            button2.setText("❌ Нет");

        }
        button2.setCallbackData("yo'q");
        secondRow.add(button2);
        rows.add(secondRow);


        return new InlineKeyboardMarkup(rows);
    }
    private InlineKeyboardMarkup Passsenger(User user, Long id) {
        System.out.println(id);
        System.out.println(this.id);
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        if(user.getLanguage().equals("uz")){
            button1.setText("📌 Band qilish");
        }else if(user.getLanguage().equals("ru")){
            button1.setText("📌Бронирование");
        }
        button1.setCallbackData("band:" + id);
        firstRow.add(button1);
        rows.add(firstRow);

        List<InlineKeyboardButton> firstRow2 = new ArrayList<>();
        InlineKeyboardButton buttonp = new InlineKeyboardButton();
        if(user.getLanguage().equals("uz")){
            buttonp.setText("👤 Haydovchini lichkasiga o'tish");
        }else if(user.getLanguage().equals("ru")){
            buttonp.setText("👤 Перейти на водительское удостоверение");

        }
        buttonp.setUrl("tg://user?id=" + id);
        firstRow2.add(buttonp);
        rows.add(firstRow2);

        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        if(user.getLanguage().equals("uz")){
            button2.setText("➡️ Keyingisi");
        }else if(user.getLanguage().equals("ru")){
            button2.setText("➡️ Следующий");
        }
        button2.setCallbackData("next:" + this.id);
        secondRow.add(button2);
        rows.add(secondRow);

        List<InlineKeyboardButton> secondRow3 = new ArrayList<>();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        if(user.getLanguage().equals("uz")){
            button3.setText("💬 Izohlar");
        }else if(user.getLanguage().equals("ru")){
            button3.setText("💬 Примечания");
        }
        button3.setCallbackData("comment:" + id);
        secondRow3.add(button3);
        rows.add(secondRow3);



        return new InlineKeyboardMarkup(rows);
    }
    private ReplyKeyboardMarkup toCitysButtonsReply(User foundUser) {
        // Define the translations inside the method using Map.ofEntries for more than 10 key-value pairs
        Map<String, String> uzToRuTranslations = Map.ofEntries(
                Map.entry("Toshkent", "Ташкент"),
                Map.entry("Samarqand", "Самарканд"),
                Map.entry("Buxoro", "Бухара"),
                Map.entry("Namangan", "Наманган"),
                Map.entry("Andijon", "Андижан"),
                Map.entry("Farg'ona", "Фергана"),
                Map.entry("Qarshi", "Карши"),
                Map.entry("Nukus", "Нукус"),
                Map.entry("Jizzax", "Джизак"),
                Map.entry("Xorazm", "Хорезм"),
                Map.entry("Surxondaryo", "Сурхандарья"),
                Map.entry("Sirdaryo", "Сырдарья")
                // Add other regions as needed
        );

        List<ToCity> all = toCityRepo.findAll();
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow currentRow = new KeyboardRow();

        // Determine the language and set the translations accordingly
        boolean isUzbek = foundUser.getLanguage().equals("uz");

        for (ToCity toCity : all) {
            KeyboardButton button = new KeyboardButton();
            String cityName = toCity.getName();
            String displayName = isUzbek ? cityName : uzToRuTranslations.getOrDefault(cityName, cityName);
            button.setText(displayName);

            currentRow.add(button);

            if (currentRow.size() == 2) {
                rows.add(new KeyboardRow(currentRow));  // Copy the current row
                currentRow.clear();  // Clear the row for new buttons
            }
        }

        // Add the last row if it's not empty
        if (!currentRow.isEmpty()) {
            rows.add(currentRow);
        }


        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        return replyKeyboardMarkup;
    }
    private ReplyKeyboardMarkup fromCitysButtonsReply(User user) {
        Map<String, String> uzToRuTranslations = Map.ofEntries(
                Map.entry("Toshkent", "Ташкент"),
                Map.entry("Samarqand", "Самарканд"),
                Map.entry("Buxoro", "Бухара"),
                Map.entry("Namangan", "Наманган"),
                Map.entry("Andijon", "Андижан"),
                Map.entry("Farg'ona", "Фергана"),
                Map.entry("Qarshi", "Карши"),
                Map.entry("Nukus", "Нукус"),
                Map.entry("Jizzax", "Джизак"),
                Map.entry("Xorazm", "Хорезм"),
                Map.entry("Surxondaryo", "Сурхандарья"),
                Map.entry("Sirdaryo", "Сырдарья")
                // Add other regions as needed
        );

        List<FromCity> fromCities = fromCityRepo.findAll();
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow currentRow = new KeyboardRow();

        // Determine the language and set the translations accordingly
        boolean isUzbek = user.getLanguage().equals("uz");

        for (FromCity fromCity : fromCities) {
            KeyboardButton button = new KeyboardButton();
            String cityName = fromCity.getName();
            String displayName = isUzbek ? cityName : uzToRuTranslations.getOrDefault(cityName, cityName);
            button.setText(displayName);

            currentRow.add(button);

            if (currentRow.size() == 2) {
                rows.add(currentRow);
                currentRow = new KeyboardRow();
            }
        }

        // Add the last row if it's not empty
        if (!currentRow.isEmpty()) {
            rows.add(currentRow);
        }

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        return replyKeyboardMarkup;
    }
    private void validateTime(String timeText, String driverDataDay, User foundUser) {
        if (!timeText.matches("\\d{2}:\\d{2}")) {
            if(foundUser.getLanguage().equals("uz")){

                throw new IllegalArgumentException("⏱️ Vaqt formati noto‘g‘ri. Soat: Minut formatida bo'lishi kerak.");

            }else if(foundUser.getLanguage().equals("ru")){
                throw new IllegalArgumentException("⏱️ Формат времени неправильный. Час: должен быть в минутном формате.");

            }
        }

        if (driverDataDay == null || driverDataDay.isEmpty()) {
            if(foundUser.getLanguage().equals("uz")){
                throw new IllegalArgumentException("🗓️ Sana ma'lumotlari mavjud emas.");

            }else if(foundUser.getLanguage().equals("ru")){
                throw new IllegalArgumentException("🗓️ Информация о дате отсутствует.");

            }

        }

        String[] timeParts = timeText.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        if (hour > 23 || minute > 59) {
            if(foundUser.getLanguage().equals("uz")){
                throw new IllegalArgumentException("🕒 Soat 23 dan kichik va daqiqa 59 dan kichik bo'lishi kerak.");

            } else if (foundUser.getLanguage().equals("ru")) {
                throw new IllegalArgumentException("🕒 Час должен быть меньше 23, а минуты должны быть меньше 59..");


            }
        }

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        LocalDate day = null;
        try {
            day = LocalDate.parse(driverDataDay);
        } catch (DateTimeParseException e) {
            if(foundUser.getLanguage().equals("uz")){
                throw new IllegalArgumentException("📅 Sana noto‘g‘ri formatda kiritilgan.");
            }else if(foundUser.getLanguage().equals("ru")){
                throw new IllegalArgumentException("📅 Дата введена в неправильном формате.");

            }

        }

        if (day.equals(today)) {
            LocalTime inputTime = LocalTime.of(hour, minute);
            if (!inputTime.isAfter(now)) {
                if(foundUser.getLanguage().equals("uz")){
                    throw new IllegalArgumentException("📅 Bugungi sanani kiritganingizda, vaqt hozirgi vaqtdan keyingi bo'lishi kerak.");

                }else if(foundUser.getLanguage().equals("ru")){
                    throw new IllegalArgumentException("📅 Когда вы вводите сегодняшнюю дату, время должно быть после текущего времени.");

                }
            }
        }
    }
    public LocalDate validateAndParseDate(String dateInput, User foundUser) throws DateTimeParseException {

        System.out.println("Input date: " + dateInput);

        Map<String, String> monthMap = new HashMap<>();

        if (foundUser.getLanguage().equals("uz")) {
            monthMap.put("yanvar", "01");
            monthMap.put("fevral", "02");
            monthMap.put("mart", "03");
            monthMap.put("aprel", "04");
            monthMap.put("may", "05");
            monthMap.put("iyun", "06");
            monthMap.put("iyul", "07");
            monthMap.put("avgust", "08");
            monthMap.put("sentabr", "09");
            monthMap.put("oktabr", "10");
            monthMap.put("noyabr", "11");
            monthMap.put("dekabr", "12");
        } else if (foundUser.getLanguage().equals("ru")) {
            // Add both nominative and genitive forms of Russian months
            monthMap.put("январь", "01");
            monthMap.put("января", "01");
            monthMap.put("февраль", "02");
            monthMap.put("февраля", "02");
            monthMap.put("март", "03");
            monthMap.put("марта", "03");
            monthMap.put("апрель", "04");
            monthMap.put("апреля", "04");
            monthMap.put("май", "05");
            monthMap.put("мая", "05");
            monthMap.put("июнь", "06");
            monthMap.put("июня", "06");
            monthMap.put("июль", "07");
            monthMap.put("июля", "07");
            monthMap.put("август", "08");
            monthMap.put("августа", "08");
            monthMap.put("сентябрь", "09");
            monthMap.put("сентября", "09");
            monthMap.put("октябрь", "10");
            monthMap.put("октября", "10");
            monthMap.put("ноябрь", "11");
            monthMap.put("ноября", "11");
            monthMap.put("декабрь", "12");
            monthMap.put("декабря", "12");
        }

        String[] dateParts = dateInput.split("-");

        if (dateParts.length != 2 || !monthMap.containsKey(dateParts[1])) {
            throw new DateTimeParseException("Invalid date format or month", dateInput, 0);
        }

        // Extract day and month
        String day = dateParts[0]; // e.g., "26"
        String month = monthMap.get(dateParts[1]);

        // Get the current year
        String currentYear = String.valueOf(LocalDate.now().getYear());

        String formattedDate = String.format("%02d-%s-%s", Integer.parseInt(day), month, currentYear);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate parsedDate = LocalDate.parse(formattedDate, formatter);

        System.out.println("Parsed date: " + parsedDate);
        return parsedDate;
    }

    private InlineKeyboardMarkup directionData(UUID id, User foundUser) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        if (foundUser.getLanguage().equals("uz")) {
            button1.setText("🔢 Joy sonni o'zgartirish:");
        } else if (foundUser.getLanguage().equals("ru")) {
            button1.setText("🔢 Изменить количество мест:");
        }
        button1.setCallbackData("place:" + id);
        firstRow.add(button1);
        rows.add(firstRow);

        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        if (foundUser.getLanguage().equals("uz")) {
            button2.setText("💵 Narxni o'zgartirish");
        } else if (foundUser.getLanguage().equals("ru")) {
            button2.setText("💵 Изменить цену");
        }
        button2.setCallbackData("money:" + id);
        secondRow.add(button2);
        rows.add(secondRow);

        List<InlineKeyboardButton> thirdRow = new ArrayList<>();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        if (foundUser.getLanguage().equals("uz")) {
            button3.setText("📅 Sana o'zgartirish");
        } else if (foundUser.getLanguage().equals("ru")) {
            button3.setText("📅 Изменить дату");
        }
        button3.setCallbackData("day:" + id);
        thirdRow.add(button3);
        rows.add(thirdRow);

        List<InlineKeyboardButton> fourthRow = new ArrayList<>();
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        if (foundUser.getLanguage().equals("uz")) {
            button4.setText("⏰ Soatni o'zgartirish");
        } else if (foundUser.getLanguage().equals("ru")) {
            button4.setText("⏰ Изменить время");
        }
        button4.setCallbackData("time:" + id);
        fourthRow.add(button4);
        rows.add(fourthRow);

        List<InlineKeyboardButton> fifthRow = new ArrayList<>();
        InlineKeyboardButton button5 = new InlineKeyboardButton();
        if (foundUser.getLanguage().equals("uz")) {
            button5.setText("🗑️ O'chirish");
        } else if (foundUser.getLanguage().equals("ru")) {
            button5.setText("🗑️ Удалить");
        }
        button5.setCallbackData("del:" + id);
        fifthRow.add(button5);
        rows.add(fifthRow);

        return new InlineKeyboardMarkup(rows);
    }
    private ReplyKeyboardMarkup directions(User foundUser) {
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();

        // Yo'nalishlarim button
        KeyboardButton button1 = new KeyboardButton();
        if (foundUser.getLanguage().equals("uz")) {
            button1.setText("🗺️ Yo'nalishlarim");
        } else if (foundUser.getLanguage().equals("ru")) {
            button1.setText("🗺️ Мои маршруты");
        }
        row1.add(button1);

        KeyboardButton button2 = new KeyboardButton();
        if (foundUser.getLanguage().equals("uz")) {
            button2.setText("\uD83D\uDC64 O'zim haqimda");
        } else if (foundUser.getLanguage().equals("ru")) {
            button2.setText("\uD83D\uDC64 Обо мне");
        }
        row1.add(button2);

        rows.add(row1);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);

        return replyKeyboardMarkup;
    }

    private InlineKeyboardMarkup fromCitysButtons(User foundUser) {
        Map<String, String> uzbekToRussianCities = new HashMap<>();
        uzbekToRussianCities.put("Toshkent", "Ташкент");
        uzbekToRussianCities.put("Andijon", "Андижан");
        uzbekToRussianCities.put("Buxoro", "Бухара");
        uzbekToRussianCities.put("Farg'ona", "Фергана");
        uzbekToRussianCities.put("Jizzax", "Джизак");
        uzbekToRussianCities.put("Xorazm", "Хорезм");
        uzbekToRussianCities.put("Namangan", "Наманган");
        uzbekToRussianCities.put("Navoiy", "Навои");
        uzbekToRussianCities.put("Qashqadaryo", "Кашкадарья");
        uzbekToRussianCities.put("Samarqand", "Самарканд");
        uzbekToRussianCities.put("Sirdaryo", "Сырдарья");
        uzbekToRussianCities.put("Surxondaryo", "Сурхандарья");
        uzbekToRussianCities.put("Qaraqalpog'iston", "Каракалпакстан");
        uzbekToRussianCities.put("Termiz" ,"Термиз");
        uzbekToRussianCities.put("Denov", "Денов");
        uzbekToRussianCities.put("Qo'qon", "Қўқон");
        uzbekToRussianCities.put("Urganch" , "Урганч");

        String userLanguage = foundUser.getLanguage();

        List<FromCity> fromCities = fromCityRepo.findAll();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> currentRow = new ArrayList<>();

        for (FromCity fromCity : fromCities) {
            String cityName = fromCity.getName();

            InlineKeyboardButton button = new InlineKeyboardButton();

            if ("uz".equals(userLanguage)) {
                button.setText(cityName);
            } else if ("ru".equals(userLanguage) && uzbekToRussianCities.containsKey(cityName)) {
                button.setText(uzbekToRussianCities.get(cityName));
            } else {
                button.setText(cityName);
            }

            button.setCallbackData(cityName);
            currentRow.add(button);

            if (currentRow.size() == 2) {
                rows.add(currentRow);
                currentRow = new ArrayList<>();
            }
        }

        if (!currentRow.isEmpty()) {
            rows.add(currentRow);
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private InlineKeyboardMarkup toCitysButtons(User foundUser) {
        Map<String, String> uzbekToRussianCities = new HashMap<>();
        uzbekToRussianCities.put("Toshkent", "Ташкент");
        uzbekToRussianCities.put("Andijon", "Андижан");
        uzbekToRussianCities.put("Buxoro", "Бухара");
        uzbekToRussianCities.put("Farg'ona", "Фергана");
        uzbekToRussianCities.put("Jizzax", "Джизак");
        uzbekToRussianCities.put("Xorazm", "Хорезм");
        uzbekToRussianCities.put("Namangan", "Наманган");
        uzbekToRussianCities.put("Navoiy", "Навои");
        uzbekToRussianCities.put("Qashqadaryo", "Кашкадарья");
        uzbekToRussianCities.put("Samarqand", "Самарканд");
        uzbekToRussianCities.put("Sirdaryo", "Сырдарья");
        uzbekToRussianCities.put("Surxondaryo", "Сурхандарья");
        uzbekToRussianCities.put("Qaraqalpog'iston", "Каракалпакстан");
        uzbekToRussianCities.put("Termiz" ,"Термиз");
        uzbekToRussianCities.put("Denov", "Денов");
        uzbekToRussianCities.put("Qo'qon", "Қўқон");
        uzbekToRussianCities.put("Urganch" , "Урганч");

        String userLanguage = foundUser.getLanguage();

        List<ToCity> all = toCityRepo.findAll();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> currentRow = new ArrayList<>();

        for (ToCity toCity : all) {
            String cityName = toCity.getName();
            InlineKeyboardButton button = new InlineKeyboardButton();

            if ("uz".equals(userLanguage)) {
                button.setText(cityName);
            } else if ("ru".equals(userLanguage) && uzbekToRussianCities.containsKey(cityName)) {
                button.setText(uzbekToRussianCities.get(cityName));
            } else {
                button.setText(cityName);
            }

            button.setCallbackData(cityName);
            currentRow.add(button);

            if (currentRow.size() == 2) {
                rows.add(new ArrayList<>(currentRow));
                currentRow.clear();
            }
        }

        if (!currentRow.isEmpty()) {
            rows.add(new ArrayList<>(currentRow));
        }

        return new InlineKeyboardMarkup(rows);
    }
    private InlineKeyboardMarkup countseatButtons(User user) {
        boolean count = user.isCount();
        String language1 = user.getLanguage();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        InlineKeyboardButton button5 = new InlineKeyboardButton();
        InlineKeyboardButton button6 = new InlineKeyboardButton();
        InlineKeyboardButton button7 = new InlineKeyboardButton();
        InlineKeyboardButton button8 = new InlineKeyboardButton();
        InlineKeyboardButton button9 = new InlineKeyboardButton();
        InlineKeyboardButton button10 = new InlineKeyboardButton();
        InlineKeyboardButton button11 = new InlineKeyboardButton();
        InlineKeyboardButton button12 = new InlineKeyboardButton();

        if (language1.equals("uz")) {

            button1.setText("1 ta");
            button2.setText("2 ta");
            button3.setText("3 ta");
            button4.setText("4 ta");
            button5.setText("5 ta");
            button6.setText("6 ta");
            button1.setCallbackData("1 ta");
            button2.setCallbackData("2 ta");
            button3.setCallbackData("3 ta");
            button4.setCallbackData("4 ta");
            button5.setCallbackData("5 ta");
            button6.setCallbackData("6 ta");
            if (!count) {
                button7.setText("1 ta");
                button7.setText("2 ta");
                button7.setText("3 ta");
                button7.setText("4 ta");
                button7.setText("5 ta");
                button7.setText("6 ta");
                button7.setText("7 ta");
                button8.setText("8 ta");
                button9.setText("9 ta");
                button10.setText("10 ta");
                button11.setText("11 ta");
                button12.setText("12 ta");
                button7.setCallbackData("1 ta");
                button7.setCallbackData("2 ta");
                button7.setCallbackData("3 ta");
                button7.setCallbackData("4 ta");
                button7.setCallbackData("5 ta");
                button7.setCallbackData("6 ta");
                button7.setCallbackData("7 ta");
                button8.setCallbackData("8 ta");
                button9.setCallbackData("9 ta");
                button10.setCallbackData("10 ta");
                button11.setCallbackData("11 ta");
                button12.setCallbackData("12 ta");
            }
        } else if (language1.equals("ru")) {
            button1.setText("1 место");
            button2.setText("2 места");
            button3.setText("3 места");
            button4.setText("4 места");
            button5.setText("5 мест");
            button6.setText("6 мест");
            button1.setCallbackData("1 место");
            button2.setCallbackData("2 места");
            button3.setCallbackData("3 места");
            button4.setCallbackData("4 места");
            button5.setCallbackData("5 мест");
            button6.setCallbackData("6 мест");

            if (!count) {
                button7.setText("1 мест");
                button7.setText("2 мест");
                button7.setText("3 мест");
                button7.setText("4 мест");
                button7.setText("5 мест");
                button7.setText("6 мест");
                button7.setText("7 мест");
                button8.setText("8 мест");
                button9.setText("9 мест");
                button10.setText("10 мест");
                button11.setText("11 мест");
                button12.setText("12 мест");
                button7.setCallbackData("1 мест");
                button7.setCallbackData("2 мест");
                button7.setCallbackData("3 мест");
                button7.setCallbackData("4 мест");
                button7.setCallbackData("5 мест");
                button7.setCallbackData("6 мест");
                button7.setCallbackData("7 мест");
                button8.setCallbackData("8 мест");
                button9.setCallbackData("9 мест");
                button10.setCallbackData("10 мест");
                button11.setCallbackData("11 мест");
                button12.setCallbackData("12 мест");
            }
        }

        row1.add(button1);
        row1.add(button2);
        row2.add(button3);
        row2.add(button4);
        row3.add(button5);
        row3.add(button6);

        rows.add(row1);
        rows.add(row2);
        rows.add(row3);

        if (!count) {
            List<InlineKeyboardButton> row4 = new ArrayList<>();
            List<InlineKeyboardButton> row5 = new ArrayList<>();

            row4.add(button7);
            row4.add(button8);
            row5.add(button9);
            row5.add(button10);

            rows.add(row4);
            rows.add(row5);

            if (language1.equals("uz")) {
                List<InlineKeyboardButton> row6 = new ArrayList<>();
                row6.add(button11);
                row6.add(button12);
                rows.add(row6);
            } else if (language1.equals("ru")) {
                List<InlineKeyboardButton> row6 = new ArrayList<>();
                row6.add(button11);
                row6.add(button12);
                rows.add(row6);
            }
        }

        return new InlineKeyboardMarkup(rows);
    }
    private ReplyKeyboardMarkup selectRoleButtons() {
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton button1 = new KeyboardButton();
        if ("uz".equals(language)) {
            button1.setText("Haydovchi  \uD83D\uDE95");
        }else{
            button1.setText("Драйверы  ");
        }
        row1.add(button1);

        KeyboardRow row2 = new KeyboardRow();
        KeyboardButton button2 = new KeyboardButton();
        if("uz".equals(language)){
            button2.setText("Yo'lovchi \uD83E\uDDF3");
        }else{
            button2.setText("Пассажиры");
        }
        row2.add(button2);

        rows.add(row1);
        rows.add(row2);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);

        return replyKeyboardMarkup;
    }
    private InlineKeyboardMarkup selectInlineRoleButtons(Long chatId) {
        Optional<User> byChatId = userRepo.findByChatId(chatId);
        User user = byChatId.get();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();


        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        if (user.getLanguage().equals("uz")) {
            button1.setText("🚖 Haydovchi");
            button1.setUrl("https://backenddeploymend-1.onrender.com/register?chatId=" + chatId + "&language=uz");
        } else {
            button1.setText("🚖 Драйверы");
            button1.setUrl("https://backenddeploymend-1.onrender.com/register?chatId=" + chatId + "&language=ru");
        }
        button1.setCallbackData("Drivers");
        row1.add(button1);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        if (user.getLanguage().equals("uz")) {
            button2.setText("🧳 Yo'lovchi");
        } else {
            button2.setText("🧳 Пассажиры");
        }
        button2.setCallbackData("Passengers");
        row2.add(button2);

        rows.add(row1);
        rows.add(row2);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }
    private ReplyKeyboardMarkup genContactButtons(User user) {
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        rows.add(row);

        KeyboardButton button = new KeyboardButton();
        String buttonText;

        if (user.getLanguage().equals("uz")) {
            buttonText = "☎\uFE0F Kontakni yuborish ";
        } else if (user.getLanguage().equals("ru")) {
            buttonText = "☎\uFE0F Отправить контакт \uD83D\uDCDE";
        } else {
            buttonText = "Share contact";
        }

        button.setText(buttonText);
        button.setRequestContact(true);
        row.add(button);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);

        return replyKeyboardMarkup;
    }
    private InlineKeyboardMarkup selectLanguageButtons() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        rows.add(row);

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("uz \uD83C\uDDFA\uD83C\uDDFF");
        button1.setCallbackData("uz");
        row.add(button1);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("ru \uD83C\uDDF7\uD83C\uDDFA");
        button2.setCallbackData("ru");
        row.add(button2);

        return new InlineKeyboardMarkup(rows);
    }
    private String translateIfNeeded(String cityName) {
        return ruToUzTranslations.getOrDefault(cityName, cityName);
    }

    public String translateCityToRussian(String city, Map<String, String> translations) {
        return translations.getOrDefault(city, city);
    }
    public String translateCityToUzbek(String city, Map<String, String> translations) {
        return translations.getOrDefault(city.trim().toLowerCase(), city);
    }

}