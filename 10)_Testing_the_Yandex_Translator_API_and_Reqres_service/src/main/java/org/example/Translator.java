package org.example;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Translator {
    public static void main(String[] args) {

        // Предложение для перевода.
        System.out.println("Введите предложение на русском языке:");
        Scanner scanner = new Scanner(System.in);
        String sentenceToTranslate = scanner.nextLine();

        // Создание заголовков для пост-запроса.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + "полученный токен");

        // Создание данных для пост-запроса.
        Map<String, String> jsonData = new HashMap<>();
        jsonData.put("folderID", "нужный folder");
        jsonData.put("targetLanguageCode", "en");
        jsonData.put("texts", "[" + sentenceToTranslate + "]");

        // Запрос формируем из заголовков и данных (будут в теле запроса).
        HttpEntity<Map<String, String>> request = new HttpEntity<>(jsonData, headers);

        // Совершаем сам запрос на сервис.
        String response1 = new RestTemplate().postForObject("https://translate.api.cloud.yandex.net/translate/v2/translate", request, String.class);


//        // Парсим полученный JSON с помощью Jackson.
//        ObjectMapper mapper = new ObjectMapper();
//
//        JsonNode obj = mapper.readTree(response);
//        System.out.println("Перевод:");
//
//        // Достаём массив переводов.
//        // translations - ключ, 0 - номер элемента, text - тоже ключ.
//        System.out.println(obj.get("translations").get(0).get("text"));

        // Так вот парсим самостоятельно.
        YandexResponse response2 = new RestTemplate().postForObject("https://translate.api.cloud.yandex.net/translate/v2/translate", request, YandexResponse.class);
        // Достаём данные.
        if (response2 != null && response2.getTranslations() != null && !response2.getTranslations().isEmpty()) {
            System.out.println(response2.getTranslations().get(0).getText());
        } else {
            System.out.println("Response or translations list is null or empty.");
        }


    }
}
