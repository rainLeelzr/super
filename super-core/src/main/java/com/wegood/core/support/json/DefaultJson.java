package com.wegood.core.support.json;


import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.wegood.core.entity.Json;
import com.wegood.core.support.JsonUtil;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


/**
 * @author Rain
 */
@Slf4j
@Accessors(chain = true)
public class DefaultJson implements Json {

    private String value;

    private JsonNode jsonNode;

    public void setValue(String value) {
        fromString(value);
    }

    @Override
    public DefaultJson fromObject(Object obj) {
        if (obj == null) {
            this.jsonNode = null;
            this.value = null;
            return this;
        }

        try {
            this.value = JsonUtil.NOT_NULL_INSTANCE.writeValueAsString(obj);
            this.jsonNode = JsonUtil.DEFAULT_INSTANCE.readValue(this.value, JsonNode.class);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            this.jsonNode = null;
            this.value = null;
        }
        return this;
    }

    @Override
    public Json fromJsonNode(JsonNode jsonNode) {
        if (jsonNode == null) {
            this.jsonNode = null;
            this.value = null;
            return this;
        }
        this.jsonNode = jsonNode;
        try {
            this.value = JsonUtil.NOT_NULL_INSTANCE.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            this.jsonNode = null;
            this.value = null;
        }
        return this;
    }

    @Override
    public DefaultJson fromString(String str) {
        if (str == null) {
            this.jsonNode = null;
            this.value = null;
            return this;
        }
        try {
            this.jsonNode = JsonUtil.DEFAULT_INSTANCE.readValue(str, JsonNode.class);
            this.value = str;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            this.jsonNode = null;
            this.value = null;
        }
        return this;
    }

    @Override
    public Json fromJson(Json json) {
        if (json == null) {
            this.jsonNode = null;
            this.value = null;
            return this;
        }

        return fromJsonNode(json.getJsonNode());
    }

    @Override
    @JsonValue
    public JsonNode getJsonNode() {
        return jsonNode;
    }

}
