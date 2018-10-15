package com.example.json.service.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.sun.jmx.snmp.ThreadContext.contains;

@Component
@Log4j2
@Data
public class ParserJsonWithGson {

    Gson gson = new Gson();
    String json =
            "{\n" +
                    "  \"token\": \"token_boken\",\n" +
                    "  \"request_id\": \"simle_request\",\n" +
                    "  \"data\": {\n" +
                    "  \t \"settings\": {\n" +
                    "      \"desktop_id\": \"abc1234hj\",\n" +
                    "      \"desktop_id111\": \"\",\n" +
                    "      \"process_id\": \"java_proc\",\n" +
                    "      \"class_id\": \"425\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";

    public Map<String, String> parserJson(String json) {
        Map<String, String> requestDataKeyValueMap = new TreeMap<>();
        try {
            LinkedTreeMap keyValueMap = null;
            keyValueMap = gson.fromJson(json, LinkedTreeMap.class);
            parserJsonByConventionOnlyPrimitiveType(requestDataKeyValueMap, keyValueMap);
            keyValueMap = (LinkedTreeMap) gson.fromJson(json, LinkedTreeMap.class).get("data");
            parseJsonByConventionAndSaveKeyValueToMap(requestDataKeyValueMap, keyValueMap);
        } catch (Exception e) {
            log.error("Error parse json, {}", e);
        }
        return requestDataKeyValueMap;

    }


    private void parserJsonByConventionOnlyPrimitiveType(Map<String, String> requestDataKeyValueMap, LinkedTreeMap keyValueMap) {
        keyValueMap.forEach((key, value) -> {
            JsonElement result = new JsonParser().parse(gson.toJson(value));
            // если это простое поле
            //System.out.println(result);
            if (result.isJsonPrimitive()) {
                requestDataKeyValueMap.put(key.toString(), value.toString());
            }
        });
    }

    private void parseJsonByConventionAndSaveKeyValueToMap(Map<String, String> requestDataKeyValueMap, LinkedTreeMap keyValueMap) {
        keyValueMap.forEach((key, value) -> {
            //System.out.println(key + "--" + value);
            JsonElement result = new JsonParser().parse(gson.toJson(value));
            // если это объект и он  пустой
            if (result.isJsonObject() && result.isJsonNull()) {
                log.info("JSON NULL, because value is empty, key = {} - value = {},", key, value);
                value = null;
                log.info("set null to value, k = {}, value = {}", key, value);
            }
            // если это объект и он не пустой
            else if (result.isJsonObject() && !result.isJsonNull()) {
                convertObjectKeyValueByConventions(requestDataKeyValueMap, key, value);
            }
            // если это коллекция
            else if (result.isJsonArray() && !value.toString().equals("[]")) {
                // значение сохраняем как коллекцию в джейсон формате
                requestDataKeyValueMap.put(key.toString(), gson.toJson(value));
            }
            // если это простое поле
            else if (result.isJsonPrimitive()) {
                requestDataKeyValueMap.put(key.toString(), value.toString());

            }
        });
    }

    private void convertObjectKeyValueByConventions(Map<String, String> requestDataKeyValueMap, Object k, Object v) {
        String value = v.toString().replaceAll("[{}]", "");
        //берем значение и парсим  в массив, где получаем массив
        String[] valueMass = value.split(",");
        String docKey = null;
        String docVal = null;
        for (String mass : valueMass) {
            // после чего парсим по знаку "=" и ложим соответственно как ключ значение
            //mass = setEmptyStringIfValueNotPresent(mass);
            if (setEmptyStringIfValueNotPresent(mass)) {
                String[] inMass = mass.split("=");
                docKey = k.toString() + "." + inMass[0].trim();
                docVal = inMass[1].trim();
                requestDataKeyValueMap.put(docKey, docVal);
            }
        }
    }

    private boolean setEmptyStringIfValueNotPresent(String mass) {
        if (mass.endsWith("=")) {
            //mass = mass + " ";
            return false;
        }
        return true;
    }

    public Object createJson() {
        Map<String, String> requestDataKeyValueMap = parserJson(json);
        Map<String, String> mapForPrimitive = new HashMap<>();
        Map<String, String> mapSettings = new HashMap<>();
        requestDataKeyValueMap.forEach((k, v) ->
                System.out.println(k + "--" + v));
        for (Map.Entry<String, String> entry : requestDataKeyValueMap.entrySet()) {
            if (entry.getKey().contains("settings")) {
                String key = entry.getKey().replaceAll("settings.", "");
                mapSettings.put(key, entry.getValue());
            } else {
                mapForPrimitive.put(entry.getKey(), entry.getValue());
            }
        }
        JsonObject settingsObject = new JsonObject();
        JsonObject dataObject = new JsonObject();
        JsonObject parentObject = new JsonObject();
        for (Map.Entry<String, String> entry : mapForPrimitive.entrySet()) {
            parentObject.addProperty(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, String> entry : mapSettings.entrySet()) {
            settingsObject.addProperty(entry.getKey(), entry.getValue());
        }
        dataObject.add("settings", settingsObject);
        parentObject.add("data", dataObject);

        System.out.println(parentObject);
        return parentObject;
    }
}
