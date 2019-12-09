package vip.isass.core.entity;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Rain
 */
public interface Json {

    JsonNode getJsonNode();

    Json fromString(String string);

    Json fromObject(Object object);

    Json fromJsonNode(JsonNode jsonNode);

    Json fromJson(Json json);

}
