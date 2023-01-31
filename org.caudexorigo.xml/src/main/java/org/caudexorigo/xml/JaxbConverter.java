package org.caudexorigo.xml;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JaxbConverter<T> {
  private Marshaller jaxb_marshaller;
  private Unmarshaller jaxb_unmarshaller;

  public JaxbConverter(Class<T> clazz) {
    this(clazz, false);
  }

  public JaxbConverter(Class<T> clazz, boolean prettyPrint) {
    super();

    try {
      JAXBContext jaxb_ctx = JAXBContext.newInstance(clazz);

      jaxb_marshaller = jaxb_ctx.createMarshaller();
      jaxb_unmarshaller = jaxb_ctx.createUnmarshaller();

      if (prettyPrint) {
        jaxb_marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      }
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }

  public void toXml(T object, OutputStream out) {
    try {
      jaxb_marshaller.marshal(object, out);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  public void toXml(T object, Writer out) {
    try {
      jaxb_marshaller.marshal(object, out);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  public String toXml(T object) {
    try {
      UnsynchronizedStringWriter w = new UnsynchronizedStringWriter();
      toXml(object, w);

      return w.toString();
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  public T fromXml(InputStream xml) {
    try {
      @SuppressWarnings("unchecked")
      T object = (T) jaxb_unmarshaller.unmarshal(xml);
      return object;
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  public T fromXml(byte[] xml) {
    try {
      return fromXml(new UnsynchronizedByteArrayInputStream(xml));
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  public T fromXml(String xml_string) {
    try {
      return fromXml(xml_string.getBytes());
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }
}
