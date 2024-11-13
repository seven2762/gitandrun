package com.sparta.gitandrun.googleAi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.gitandrun.googleAi.dto.AiDto;
import com.sparta.gitandrun.googleAi.repository.AiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Service
@Slf4j
public class AiService {


    @Value("${google-api-key}")
    private String SECRET_KEY;

    String projectId = "gen-lang-client-0589077725";

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    //CREATE
    public String createQuestion(String text) throws IOException {

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + SECRET_KEY;

        JSONObject jsonObject0 = new JSONObject(); // contents
        JSONArray contents = new JSONArray();

        JSONObject jsonObject1 = new JSONObject(); // parts
        JSONArray parts = new JSONArray();

        JSONObject jsonObject2 = new JSONObject(); // text

        jsonObject2.put("text",text);
        parts.put(jsonObject2);
        jsonObject1.put("parts",parts);
        contents.put(jsonObject1);
        jsonObject0.put("contents",contents);


        System.out.println(jsonObject0);

        RequestBody body = RequestBody.create(
                jsonObject0.toString(), MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .post(body)
                .build();


        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            JsonNode jsonResponse = objectMapper.readTree(response.body().string());
            return jsonResponse.path("response").path("text").asText();


        }

    }
}