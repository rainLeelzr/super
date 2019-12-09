package vip.isass.core.database.postgresql.entity;


import vip.isass.core.entity.Json;
import vip.isass.core.support.JsonUtil;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PGobject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * @author Rain
 */
@Slf4j
@Accessors(chain = true)
@Component
@Scope("prototype")
public class JsonPg extends PGobject implements Json {

    public JsonPg() {
        this.type = "jsonb";
    }

    private JsonNode jsonNode;

    @Override
    public void setValue(String value) {
        fromString(value);
    }

    @Override
    public JsonPg fromObject(Object obj) {
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
    public Json fromJson(Json json) {
        if (json == null) {
            this.jsonNode = null;
            this.value = null;
            return this;
        }

        return fromJsonNode(json.getJsonNode());
    }

    @Override
    public JsonPg fromString(String str) {
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
    @JsonValue
    public JsonNode getJsonNode() {
        return jsonNode;
    }

}
