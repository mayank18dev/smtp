package com.qvc.thirdpartycontentservice.util;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.qvc.thirdpartycontentservice.enums.JoltSpec;

import java.util.List;




public class JoltUtil {

    public static JsonNode getJoltSpec(JoltSpec joltSpec) throws Exception {
        JsonNode joltJsonNode = null;
        try {
            if (joltSpec.getJoltSpecPath() != null && !joltSpec.getJoltSpecPath().isEmpty()) {
                joltJsonNode = JsonUtil.readFile(joltSpec.getJoltSpecPath());
            } else {
                throw new Exception(
                        "Jolt spec is not available {}");
            }

        } catch (Exception e) {
            throw new Exception("Unknown Error occured in schema processing");
        }
        return joltJsonNode;
    }

    public static ObjectNode transform(JsonNode request, JsonNode joltInputStream) throws Exception {
        ObjectNode responseNode = null;
        try {
            if (joltInputStream != null) {
                List<Object> specs = JsonUtils.jsonToList(joltInputStream.toString());
                Chainr chainr = Chainr.fromSpec(specs);
                Object inputJSON = JsonUtils.jsonToObject(request.toString());
                Object transformedOutput = chainr.transform(inputJSON);
                ObjectMapper objectMapper = JsonUtil.getObjectMapper();
                objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
                responseNode = objectMapper.convertValue(transformedOutput, ObjectNode.class);
            } else {
                throw new Exception("Schema not Found");
            }
        } catch (Exception cause) {
            throw cause;
        }
        return responseNode;
    }
}
