package com.qvc.thirdpartycontentservice.util;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qvc.thirdpartycontentservice.ThirdPartyContentService;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class JsonUtil {

    public static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }


    public static void addKeyIfPresent(JsonNode input, List<String> keysList, ObjectNode output) {

        keysList.forEach(key -> {
            if (input.has(key)) {
                output.set(key, input.get(key));
            }
        });
    }

    public static void addKeyIfPresent(JsonNode input, String key, ObjectNode output) {
        if (input.has(key)) {
            output.set(key, input.get(key));
        }
    }


    public static JsonNode getJsonValue(JsonNode jsonNode, String key) {
        return jsonNode.has(key) ? jsonNode.get(key) : null;
    }

    public static String getTextValue(JsonNode jsonNode, String key) {
        return jsonNode.has(key) ? jsonNode.get(key).asText() : "";
    }

    public static Boolean getBooleanValue(JsonNode jsonNode, String key) {
        return jsonNode.has(key) && jsonNode.get(key).asBoolean();
    }

    public static JsonNode getJsonNode(JsonNode jsonNode, String path) {
        return jsonNode.at(path);
    }

    public static Boolean isNodeMissingOrEmpty(JsonNode node) {
        return node.isMissingNode() || node.isNull() || (node.isTextual() && node.asText().isEmpty());
    }

    public static JsonNode extractNodeValueFromPath(JsonNode inputNode,String fieldPath) {
        JsonPointer pointer = JsonPointer.compile(fieldPath);
        return inputNode.at(pointer);
    }

    public static void removeNestedField(ObjectNode objectNode, JsonPointer pointer) {
        JsonNode parentNode = objectNode.at(pointer.head());
        if (parentNode.isObject()) {
            String fieldName = pointer.last().toString().substring(1);
            ((ObjectNode) parentNode).remove(fieldName);
        }
    }

    public static void setJsonProperty(JsonNode jsonNode, String key, String value) {
        if (jsonNode instanceof ObjectNode) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            objectNode.put(key, value);
        }
    }

    public static boolean compareKeyValue(JsonNode jsonNode, String key, String requiredValue) {
        return Optional.ofNullable(jsonNode.get(key))
                .map(JsonNode::asText)
                .map(value -> value.equals(requiredValue))
                .orElse(false);
    }
    public static JsonNode readFile(String filePath) throws IOException {
        InputStream inputStream = ThirdPartyContentService.class.getClassLoader().getResourceAsStream(filePath);
        ObjectMapper objectMapper = JsonUtil.getObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));
        return jsonNode;
    }

}
