package org.example.backend.service.Advertising;

import lombok.RequiredArgsConstructor;
import org.example.backend.DTO.AdvertsiningDTO;
import org.example.backend.entity.User;
import org.example.backend.repository.UserRepo;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertisingServiceImpl implements AdvertisingService {

    private final UserRepo userRepo;
    private final RestTemplate restTemplate;
    private final String apiToken = "7516605771:AAFXsTzRzd2aqoUNFX2TdnSlsGQ3yOAAyjk";
    private final String fileDirectory = "files/"; // .jar fayl joylashgan joyda

    @Override
    public void saveAdvertising(AdvertsiningDTO advertsiningDTO) {
        List<Long> usersChatIds = getAllChatIds();
        for (Long chatId : usersChatIds) {
            try {
                sendMessage(chatId, advertsiningDTO);
            } catch (Exception e) {
                System.err.println("Xabar yuborishda xatolik: " + chatId + " - " + e.getMessage());
            }
        }
    }

    private List<Long> getAllChatIds() {
        return userRepo.findAllByChatIdIsNotNull()
                .stream()
                .map(user -> ((User) user).getChatId())
                .collect(Collectors.toList());
    }

    private void sendMessage(Long chatId, AdvertsiningDTO advertsiningDTO) throws IOException {
        String text = advertsiningDTO.getText();
        String img = advertsiningDTO.getImg();
        String buttonName = advertsiningDTO.getButtonName();
        String link = advertsiningDTO.getLink();

        File imageFile = new File(fileDirectory + img);
        if (!imageFile.exists()) {
            throw new IOException("Rasm fayli mavjud emas: " + imageFile.getAbsolutePath());
        }

        String sendPhotoUrl = "https://api.telegram.org/bot" + apiToken + "/sendPhoto";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("chat_id", chatId.toString());
        body.add("photo", new FileSystemResource(imageFile));
        body.add("caption", text);

        // Inline tugmalarni tayyorlash
        if (buttonName != null && link != null) {
            String[] buttonNames = buttonName.split(",");
            String[] links = link.split(",");

            StringBuilder inlineKeyboard = new StringBuilder("[[");
            for (int i = 0; i < buttonNames.length; i++) {
                inlineKeyboard.append("{\"text\": \"").append(buttonNames[i]).append("\", \"url\": \"").append(links[i]).append("\"}");
                if (i != buttonNames.length - 1) {
                    inlineKeyboard.append(",");
                }
            }
            inlineKeyboard.append("]]");

            // `reply_markup` qismiga inline tugmalarni qo'shish
            body.add("reply_markup", "{\"inline_keyboard\": " + inlineKeyboard + "}");
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                sendPhotoUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Xabar muvaffaqiyatli yuborildi: " + response.getBody());
        } else {
            System.err.println("Xabar yuborishda xatolik: " + response.getStatusCode() + " - " + response.getBody());
        }
    }

    private void sendInlineKeyboard(Long chatId, String buttonText, String buttonLink) {
        String sendInlineKeyboardUrl = "https://api.telegram.org/bot" + apiToken + "/sendMessage";

        try {
            // Inline klaviaturani to'g'ri JSON formatida yaratish
            String inlineKeyboard = "[[{\"text\": \"" + buttonText + "\", \"url\": \"" + buttonLink + "\"}]]";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // JSON formatida to'g'ri so'rov yaratish
            String requestBody = String.format(
                    "{\"chat_id\": %d, \"text\": \"Iltimos, tugmani bosing:\", \"reply_markup\": {\"inline_keyboard\": %s}}",
                    chatId, inlineKeyboard
            );

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    sendInlineKeyboardUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                System.err.println("Inline tugma yuborishda xatolik: " + response.getStatusCode() + " - " + response.getBody());
            } else {
                System.out.println("Inline tugma muvaffaqiyatli yuborildi: " + response.getBody());
            }
        } catch (Exception e) {
            System.err.println("Inline tugma yuborishda xatolik: " + e.getMessage());
        }
    }

}
