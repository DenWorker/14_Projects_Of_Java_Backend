package org.example;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // get, post, put - для всех есть свой метод.
        // Делаем гет запрос на сервис (он тестовый специальный - https://reqres.in).
        // И получаем ответ Json, который конвертируется в строку.
        String response1 = restTemplate.getForObject("https://reqres.in/api/users/2", String.class);

        // Выводим полученные данные.
        System.out.println(response1);
        System.out.println();

        //////////////////////////////////////////////////////////////////////////////

        // Создаём мэпу для отправки.
        Map<String, String> jsonToSend = new HashMap<>();
        jsonToSend.put("name", "Test name");
        jsonToSend.put("job", "Test job");

        // Нашу мэпу пакуем в Http запрос.
        HttpEntity<Map<String, String>> request = new HttpEntity<>(jsonToSend);
        // Делаем пост-запрос на сервис.
        String response2 = restTemplate.postForObject("https://reqres.in/api/users/", request, String.class);
        // Выводим ответ.
        System.out.println(response2);
        System.out.println();
    }
}
