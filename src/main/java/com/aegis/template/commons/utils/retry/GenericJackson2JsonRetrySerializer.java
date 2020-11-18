package com.aegis.template.commons.utils.retry;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.cache.support.NullValue;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 序列化类
 *
 * @author wg
 * @version 1.0
 * @date 2020/4/17 18:06
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
public class GenericJackson2JsonRetrySerializer {
  private final ObjectMapper mapper;

  public GenericJackson2JsonRetrySerializer() {
    mapper = new ObjectMapper();
    registerNullValueSerializer(this.mapper, null);

    mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.DEFAULT);

    mapper.activateDefaultTyping(
        LaissezFaireSubTypeValidator.instance,
        ObjectMapper.DefaultTyping.NON_FINAL,
        JsonTypeInfo.As.WRAPPER_ARRAY);

    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.registerModule(new ParameterNamesModule())
        .registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule());
  }

  public static void registerNullValueSerializer(ObjectMapper objectMapper, @Nullable String classPropertyTypeName) {
    objectMapper.registerModule((new SimpleModule()).addSerializer(new GenericJackson2JsonRetrySerializer.NullValueSerializer(classPropertyTypeName)));
  }

  private static class NullValueSerializer extends StdSerializer<NullValue> {
    private static final long serialVersionUID = 1999052150548658808L;
    private final String classIdentifier;

    NullValueSerializer(@Nullable String classIdentifier) {
      super(NullValue.class);
      this.classIdentifier = StringUtils.hasText(classIdentifier) ? classIdentifier : "@class";
    }

    @Override
    public void serialize(NullValue value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
      jgen.writeStartObject();
      jgen.writeStringField(this.classIdentifier, NullValue.class.getName());
      jgen.writeEndObject();
    }
  }

  public byte[] serialize(@Nullable Object source) throws SerializationException {
    if (source == null) {
      return new byte[0];
    }

    try {
      return this.mapper.writeValueAsBytes(source);
    } catch (JsonProcessingException var3) {
      throw new SerializationException("Could not write JSON: " + var3.getMessage(), var3);
    }

  }

  @Nullable
  public <T> T deserialize(@Nullable byte[] source, TypeReference<T> valueTypeRef) throws SerializationException {
    Assert.notNull(valueTypeRef, "Deserialization valueTypeRef must not be null! Please provide Object.class to make use of Jackson2 default typing.");
    if (source == null || source.length == 0) {
      return null;
    }

    try {
      return this.mapper.readValue(source, valueTypeRef);
    } catch (Exception var4) {
      throw new SerializationException("Could not read JSON: " + var4.getMessage(), var4);
    }
  }

  @Nullable
  public <T> T deserialize(@Nullable byte[] source, Class<T> valueType) throws SerializationException {
    Assert.notNull(valueType, "Deserialization valueType must not be null! Please provide Object.class to make use of Jackson2 default typing.");
    if (source == null || source.length == 0) {
      return null;
    }

    try {
      return this.mapper.readValue(source, valueType);
    } catch (Exception var4) {
      throw new SerializationException("Could not read JSON: " + var4.getMessage(), var4);
    }
  }
}
