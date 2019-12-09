package vip.isass.core.serialization;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author Rain
 */
public interface JacksonSerializable<T> {

    TypeReference<T> typeReference();

}
