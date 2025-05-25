package com.konnect.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class CustomNullableString extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctx)
            throws IOException {
        JsonToken token = p.currentToken();

        // 1) null 이면 그대로 null
        if (token == JsonToken.VALUE_NULL) {
            return null;
        }
        // 2) 빈 객체 {} 나 빈 배열 [] → null 로 간주
        if (token == JsonToken.START_OBJECT || token == JsonToken.START_ARRAY) {
            p.skipChildren();       // 내용 무시
            return null;
        }
        // 3) 정상 문자열이면 그대로 반환
        return p.getValueAsString();
    }
}