package com.example.json;

import com.example.json.service.gson.ParserJsonWithGson;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class ParserJsonWithGsonTest {

    ParserJsonWithGson parserJsonWithGson;

    @Before
    public void set() {
        parserJsonWithGson = new ParserJsonWithGson();
    }

    String json =
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

    @Test
    public void test() {
        Map<String, String> stringMap = new TreeMap<>();
        stringMap.put("request_id", "simple_request");
        stringMap.put("settings.class_id", "425");
        stringMap.put("settings.desktop_id", "abc1234hj");
        stringMap.put("settings.process_id", "java_proc");
        stringMap.put("token", "token_boken");
        System.out.println(stringMap);
        assertEquals(stringMap, parserJsonWithGson.parserJson(json));
    }
}