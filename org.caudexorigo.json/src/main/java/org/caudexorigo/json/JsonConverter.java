package org.caudexorigo.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

public class JsonConverter<T> {
  private final ObjectMapper mapper;

  private final Class<T> clazz;

  public JsonConverter(Class<T> clazz) {
    this(clazz, false);
  }

  public JsonConverter(Class<T> clazz, boolean prettyPrint) {
    super();
    this.clazz = clazz;
    this.mapper = new ObjectMapper();
    this.mapper.registerModule(new AfterburnerModule());

    if (prettyPrint) {
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
  }

  public JsonConverter(Class<T> clazz, ObjectMapper mapper) {
    super();
    this.clazz = clazz;
    this.mapper = mapper;
    this.mapper.registerModule(new AfterburnerModule());
  }

  public void toJson(T object, OutputStream out) {
    try {
      mapper.writeValue(out, object);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  public void toJson(T object, Writer out) {
    try {
      mapper.writeValue(out, object);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  public String toJson(T object) {
    try {
      JsonStringWriter w = new JsonStringWriter();
      toJson(object, w);

      return w.toString();
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  public T fromJson(InputStream json) {
    try {
      T ev = mapper.readValue(json, clazz);
      return ev;
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  public T fromJson(byte[] json) {
    try {
      T ev = mapper.readValue(json, clazz);
      return ev;
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  public T fromJson(String json_string) {
    try {
      T object = mapper.readValue(json_string, clazz);
      return object;
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }
}
