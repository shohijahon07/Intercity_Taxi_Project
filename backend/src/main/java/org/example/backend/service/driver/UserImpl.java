package org.example.backend.service.driver;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend.DTO.DriverDto;
import org.example.backend.DTO.IsDriving;
import org.example.backend.DTO.PessengerDto;
import org.example.backend.DTO.UserDto;
import org.example.backend.entity.Role;
import org.example.backend.entity.Status;
import org.example.backend.entity.User;
import org.example.backend.repository.RoleRepo;
import org.example.backend.repository.RouteDriverRepo;
import org.example.backend.repository.UserRepo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserImpl implements UserService{
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final RouteDriverRepo routeDriverRepo;
    private final FileService fileService;

    @Override
    public ResponseEntity<?> getDriverOne(UUID id) {
        List<User> users = userRepo.findAllById(id);
        return ResponseEntity.ok(users);
    }

    @Override
    public HttpEntity<?> saveAboutDriver(UUID id, String driverAboutDto) {
        User user = userRepo.findById(id).orElseThrow();
        user.setAbout(driverAboutDto);
        userRepo.save(user);
        return null;
    }

    @Override
    public HttpEntity<?> editDriver(UUID id, DriverDto driverDto) {

        User user = userRepo.findById(id).orElseThrow();
        user.setDriverImg(driverDto.getDriverImg());
        user.setAbout(driverDto.getAbout());
        user.setCardDocument(driverDto.getCardDocument());
        user.setCarImg(driverDto.getCarImg());
        user.setCarType(driverDto.getCarType());
        user.setFullName(driverDto.getFullName());
        user.setPhoneNumber(driverDto.getPhoneNumber());
        userRepo.save(user);
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<?> getDriversAll(List<Role> roleDriver, Boolean isDriver) {
        List<User> all = userRepo.findAllByRolesAndIsDriver(roleDriver,isDriver);
        System.out.println(all);
        return ResponseEntity.ok(all);
    }

    @Override
    public HttpEntity<?> editDriverIsDriving(UUID id, IsDriving isDriving) {
        try {
            User user = userRepo.findById(id).orElseThrow();

            // Parolni encode qilmasdan oldingi original holati
            String plainPassword = isDriving.getPassword();

            // Parolni encode qilish
            user.setPassword(passwordEncoder.encode(plainPassword));
            user.setIsDriver(isDriving.getIsDriver());
            user.setStatus(Status.START);

            userRepo.save(user);

            String apiToken ="BOT_TOKEN";
            String chatId = String.valueOf(user.getChatId());
            String text = "";

            // Uz tilidagi xabar
            if (user.getLanguage().equals("uz")) {
                text = "🎉 Tabriklaymiz! Siz muvaffaqiyatli tasdiqlandingiz. Telegram botdan davom etish uchun /start buyrug'ini bering.\n" +
                        "🌐 Veb saytdan davom etish uchun: https://backenddeploymend-1.onrender.com/login\n" +
                        "🔑 Login: " + user.getPhoneNumber() + " | Parol: " + plainPassword;
            }
            // Rus tilidagi xabar
            else {
                text = "🎉 Поздравляем! Вы успешно подтверждены. Для начала введите команду /start.\n" +
                        "🌐 Продолжить на сайте: https://backenddeploymend-1.onrender.com/login\n" +
                        "🔑 Логин: " + user.getPhoneNumber() + " | Пароль: " + plainPassword;
            }

            // Telegram API orqali xabar jo'natish
            String urlString = "https://api.telegram.org/bot" + apiToken + "/sendMessage?chat_id=" + chatId + "&text=" + URLEncoder.encode(text, "UTF-8");

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            conn.disconnect();

            System.out.println(content.toString());

            return ResponseEntity.ok("Edit Successful");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send message");
        }
    }


    @Override
    public ResponseEntity<?> countDriver(List<Role> roleDriver) {
        Integer count = userRepo.countAllByRoles(roleDriver);
        return ResponseEntity.ok(count);
    }

    @Override
    public ResponseEntity<?> CountUserAll(List<Role> roleDriver) {
        Integer users = userRepo.countAllByRoles(roleDriver);
        return ResponseEntity.ok(users);
    }




    public ResponseEntity<?> saveUser(String userDtoString, MultipartFile carImg, MultipartFile driverImg, MultipartFile cardDocument) {
        try {
            // Convert the userDto string back to an object
            ObjectMapper objectMapper = new ObjectMapper();
            UserDto userDto = objectMapper.readValue(userDtoString, UserDto.class);

            // Handle file uploads
            if (carImg != null && !carImg.isEmpty()) {
                String carImgPath = fileService.saveFile(carImg);
                userDto.setCarImg(carImgPath);
            }
            if (driverImg != null && !driverImg.isEmpty()) {
                String driverImgPath = fileService.saveFile(driverImg);
                userDto.setDriverImg(driverImgPath);
            }
            if (cardDocument != null && !cardDocument.isEmpty()) {
                String cardDocumentPath = fileService.saveFile(cardDocument);
                userDto.setCardDocument(cardDocumentPath);
            }

            // Handle roles and user saving
            List<Role> roles = new ArrayList<>();
            Role driverRole = roleRepo.findByName("ROLE_DRIVER");
            if (driverRole == null) {
                driverRole = new Role("ROLE_DRIVER");
                roleRepo.save(driverRole);
            }
            roles.add(driverRole);

            Optional<User> byChatId = userRepo.findByChatId(userDto.getChatId());

            if (byChatId.isPresent()) {
                User user = byChatId.get();
                user.setCarType(userDto.getCarType());
                user.setCarImg(userDto.getCarImg());
                user.setDriverImg(userDto.getDriverImg());
                user.setCardDocument(userDto.getCardDocument());
                user.setAbout(userDto.getAbout());
                user.setChatId(userDto.getChatId());
                user.setCount(userDto.isCount());
                user.setRoles(roles);

                userRepo.save(user);
                return ResponseEntity.ok("User updated successfully");
            } else {
                return ResponseEntity.ok("Please register first through the bot");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred while saving the user");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteUser(UUID id) {
        routeDriverRepo.deleteByUserId(id);

        userRepo.deleteById(id);

        return ResponseEntity.ok("delete successfull");
    }

    @Override
    public HttpEntity<?> savePessenger(PessengerDto pessengerDto) throws IOException {
        System.out.println(pessengerDto);

        List<Role> roles = new ArrayList<>();
        Role driverRole = roleRepo.findByName("ROLE_USER");
        if (driverRole == null) {
            driverRole = new Role("ROLE_USER");
            roleRepo.save(driverRole);
        }
        roles.add(driverRole);

        User user = new User(pessengerDto.getName(), pessengerDto.getPhoneNumber(), roles);
        userRepo.save(user);

        String apiToken = "BOT_TOKEN";
        String chatId = String.valueOf(pessengerDto.getDriverChatId());
        String text="";
        Optional<User> byChatId = userRepo.findByChatId(Long.valueOf(chatId));

        if(byChatId.get().getLanguage().equals("uz")){
            text = "👤 Siz " + user.getFullName() + " yo'lovchini qabul qilasizmi? " +
                    "📞 Telefon raqami: " + user.getPhoneNumber();
        } else if (byChatId.get().getLanguage().equals("ru")) {
            text="👤 Вы принимаете " + user.getFullName() + " пассажира?" +
                    "📞 Номер телефона: " + user.getPhoneNumber();
        }

        InlineKeyboardMarkup markup = sendBusy(user.getId(), Long.valueOf(pessengerDto.getDriverChatId()));
        ObjectMapper objectMapper = new ObjectMapper();
        String inlineKeyboardJson = objectMapper.writeValueAsString(markup);

        // Send message with inline buttons
        String urlString = "https://api.telegram.org/bot" + apiToken + "/sendMessage?chat_id=" + chatId +
                "&text=" + URLEncoder.encode(text, "UTF-8") +
                "&reply_markup=" + URLEncoder.encode(inlineKeyboardJson, "UTF-8");

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        conn.disconnect();
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<?> SearchNameDriver(String name) {

        List<Role> roles = new ArrayList<>();
        Role driverRole = roleRepo.findByName("ROLE_DRIVER");
        if (driverRole == null) {
            driverRole = new Role("ROLE_DRIVER");
            roleRepo.save(driverRole);
        }
        roles.add(driverRole);

        List<User> users = userRepo.findAllByFullNameContainingIgnoreCaseAndRoles(name, roles);

        return ResponseEntity.ok(users);
    }




    private InlineKeyboardMarkup sendBusy(UUID userId, Long driverChatId) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        // First button: Accept
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("✅ Ha:");
        button1.setCallbackData("accept_" + userId.toString() + "_" + driverChatId);  // Include userId and driverChatId in callback data
        firstRow.add(button1);
        rows.add(firstRow);

        // Second button: Decline
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("❌ Yo'q");
        button2.setCallbackData("decline_" + userId.toString() + "_" + driverChatId);  // Include userId and driverChatId in callback data
        secondRow.add(button2);
        rows.add(secondRow);

        return new InlineKeyboardMarkup(rows);
    }

}






