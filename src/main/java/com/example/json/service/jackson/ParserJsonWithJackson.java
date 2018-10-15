package com.example.json.service.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

@Component
@Log4j2
@Data
public class ParserJsonWithJackson {

    String json =
            "{\n" +
                    "  \"token\": \"token_boken\",\n" +
                    "  \"request_id\": \"simle_request\",\n" +
                    "  \"data\": {\n" +
                    "  \t \"settings\": {\n" +
                    "      \"desktop_id\": \"abc1234hj\",\n" +
                    "      \"desktop_id3\": \"55\",\n" +
                    "      \"process_id\": \"java_proc\",\n" +
                    "      \"class_id\": \"425\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";

    public Map<String, String> parserJson(String json) {
        Map<String, String> requestDataKeyValueMap = new TreeMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            LinkedHashMap<String, Object> keyValueMap = null;
            keyValueMap = mapper.readValue(json, LinkedHashMap.class);
            parserJsonByConventionOnlyPrimitiveType(requestDataKeyValueMap, keyValueMap, mapper);
            keyValueMap = (LinkedHashMap) mapper.readValue(json, LinkedHashMap.class).get("data");
            parserJsonByConventionAndSaveKeyValueToMap(requestDataKeyValueMap, keyValueMap, mapper);
        } catch (Exception e) {
            log.error("Error parse json, {}", e);
        }
        return requestDataKeyValueMap;

    }


    private void parserJsonByConventionOnlyPrimitiveType(Map<String, String> requestDataKeyValueMap, LinkedHashMap<String, Object> keyValueMap, ObjectMapper mapper) throws IOException {
        for (LinkedTreeMap.Entry<String, Object> entry : keyValueMap.entrySet()) {
            JsonNode node = mapper.readTree(mapper.writeValueAsString(entry.getValue()));
            if (!node.isObject()) {
                requestDataKeyValueMap.put(entry.getKey(), entry.getValue().toString());
            }
        }
    }

    private void parserJsonByConventionAndSaveKeyValueToMap(Map<String, String> requestDataKeyValueMap, LinkedHashMap<String, Object> keyValueMap, ObjectMapper mapper) throws IOException {
        for (LinkedTreeMap.Entry<String, Object> entry : keyValueMap.entrySet()) {
            JsonNode node = mapper.readTree(mapper.writeValueAsString(entry.getValue()));
            if (node.isObject() && node.isNull()) {
                log.info("JSON NULL, because value is empty, key = {} - value = {},", entry.getKey(), entry.getValue());
                //entry.getValue().toString() = null;
                log.info("set null to value, k = {}, value = {}", entry.getKey(), entry.getValue());
            } else if (node.isObject() && !node.isNull()) {
                convertObjectKeyValueByConventions(requestDataKeyValueMap, entry.getKey(), entry.getValue());
            }
            else if (node.isArray()&&!entry.getValue().equals("[]")){
                requestDataKeyValueMap.put(entry.getKey().toString(),entry.getValue().toString());
            }
            else if(node.isDouble()||node.isBoolean()||node.isTextual()||node.isInt()){
                requestDataKeyValueMap.put("data"+"."+entry.getKey().toString(),entry.getValue().toString());
            }
        }
    }

    private void convertObjectKeyValueByConventions(Map<String, String> requestDataKeyValueMap, String key, Object value) {
        String val = value.toString().replaceAll("[{}]", "");
        //берем значение и парсим  в массив, где получаем массив
        String[] valueMass = val.split(",");
        String docKey = null;
        String docVal = null;
        for (String mass : valueMass) {
            // после чего парсим по знаку "=" и ложим соответственно как ключ значение
            if (setEmptyStringIfValueNotPresent(mass)) {
                String[] inMass = mass.split("=");
                docKey = key.toString() + "." + inMass[0].trim();
                docVal = inMass[1].trim();
                requestDataKeyValueMap.put(docKey, docVal);
            }
        }
    }

    private boolean setEmptyStringIfValueNotPresent(String mass) {
        if (mass.endsWith("=")) {
            return false;
        }
        return true;
    }

    public Object createJson() {
        Map<String, String> mainMap = parserJson(json);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode parentNode = mapper.createObjectNode();
        Map<String, String> primitiveMap = new HashMap<>();
        Map<String, String> objectMap = new HashMap<>();
        for (Map.Entry<String, String> entry : mainMap.entrySet()) {
            if (entry.getKey().contains("settings.")) {
                String key = entry.getKey().replaceAll("settings.", "");
                objectMap.put(key, entry.getValue());
            } else {
                primitiveMap.put(entry.getKey(), entry.getValue());
            }
        }

        ObjectNode dataObject = mapper.createObjectNode();
        ObjectNode settingsObject = mapper.createObjectNode();
        dataObject.put("settings", settingsObject);
        for (Map.Entry<String, String> entry : objectMap.entrySet()) {
            settingsObject.put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, String> entry : primitiveMap.entrySet()) {
            parentNode.put(entry.getKey(), entry.getValue());
        }

        parentNode.put("data", dataObject);
        System.out.println(parentNode);
        return parentNode;
    }
}






