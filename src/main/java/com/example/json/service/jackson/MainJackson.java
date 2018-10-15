package com.example.json.service.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class MainJackson {
    private static final String json =
            "{\n" +
                    "  \"token\": \"token_boken\",\n" +
                    "  \"request_id\": \"simple_request\",\n" +
                    "  \"data\": {\n" +
                    "  \t \"settings\": {\n" +
                    "      \"desktop_id\": \"abc1234hj\",\n" +
                    "      \"process_id\": \"java_proc\",\n" +
                    "      \"class_id\": \"425\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";

    public static void main(String[] args) throws IOException {
        Map<String, String> requestDataKeyValueMap = new TreeMap<>();
        ObjectMapper mapper = new ObjectMapper();

        LinkedTreeMap keyValueMap = null;
        //keyValueMap = gson.fromJson(json, LinkedTreeMap.class);
//        keyValueMap = mapper.readValue(json, LinkedTreeMap.class);
//        System.out.println(keyValueMap);
        keyValueMap = (LinkedTreeMap) mapper.readValue(json, LinkedTreeMap.class).get("data");
        System.out.println(keyValueMap);


    }
}



