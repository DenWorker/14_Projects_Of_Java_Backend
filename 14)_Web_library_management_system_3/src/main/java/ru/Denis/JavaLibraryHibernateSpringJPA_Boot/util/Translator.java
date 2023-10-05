package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class Translator {
    public String translate(String sentenceToTranslate, String selectedLanguage) throws IOException {
        String escapedText = escapeSpecialCharacters(sentenceToTranslate);
        String requestBody = "[{\"Text\": \"" + escapedText + "\"}]";

        HttpRequest request = createHttpRequest(selectedLanguage, requestBody);

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return extractTranslatedText(response.body());
            } else {
                return "Ошибка при переводе";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Ошибка при выполнении запроса";
        }
    }

    public TranslationResponse[] translateREST(String sentenceToTranslate, String selectedLanguage) {
        String escapedText = escapeSpecialCharacters(sentenceToTranslate);
        String requestBody = "[{\"Text\": \"" + escapedText + "\"}]";

        HttpRequest request = createHttpRequest(selectedLanguage, requestBody);

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(response.body(), TranslationResponse[].class);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private HttpRequest createHttpRequest(String selectedLanguage, String requestBody) {
        String baseUrl = "https://microsoft-translator-text.p.rapidapi.com/translate";
        return HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?to%5B0%5D=" + selectedLanguage + "&api-version=3.0&profanityAction=NoAction&textType=plain"))
                .header("content-type", "application/json")
                .header("X-RapidAPI-Key", readApiKeyFromFile())
                .header("X-RapidAPI-Host", "microsoft-translator-text.p.rapidapi.com")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
    }

    private String extractTranslatedText(String responseBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TranslationResponse[] translationResponses = objectMapper.readValue(responseBody, TranslationResponse[].class);
        return translationResponses[0].getTranslations().get(0).getText();
    }

    private String escapeSpecialCharacters(String input) {
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private String readApiKeyFromFile() {
        try {
            return Files.readString(Paths.get("src/main/java/ru/Denis/JavaLibraryHibernateSpringJPA_Boot/util/rapidapi-key.txt")).trim();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
