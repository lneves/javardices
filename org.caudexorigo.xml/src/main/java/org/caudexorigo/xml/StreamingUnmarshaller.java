package org.caudexorigo.xml;

import static javax.xml.stream.XMLStreamConstants.CHARACTERS;
import static javax.xml.stream.XMLStreamConstants.DTD;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_DOCUMENT;

import java.io.Closeable;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class StreamingUnmarshaller<T> implements Iterable<T>, Iterator<T>, Closeable {
  XMLStreamReader reader;
  Class<T> clazz;
  Unmarshaller unmarshaller;

  public StreamingUnmarshaller(InputStream stream, Class<T> clazz) throws XMLStreamException,
      FactoryConfigurationError, JAXBException {
    this.clazz = clazz;
    this.unmarshaller = JAXBContext.newInstance(clazz).createUnmarshaller();
    this.reader = XMLInputFactory.newInstance().createXMLStreamReader(stream);

    /* ignore headers */
    skipElements(START_DOCUMENT, DTD);
    /* ignore root element */
    reader.nextTag();
    /* if there's no tag, ignore root element's end */
    skipElements(END_ELEMENT);
  }

  @Override
  public T next() {
    if (!hasNext())
      throw new NoSuchElementException();

    try {
      T value = unmarshaller.unmarshal(reader, clazz).getValue();
      skipElements(CHARACTERS, END_ELEMENT);
      return value;
    } catch (XMLStreamException | JAXBException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean hasNext() {
    try {
      return reader.hasNext();
    } catch (XMLStreamException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() {
    try {
      reader.close();
    } catch (XMLStreamException e) {
      // ignore since we are closing the reader
    }
  }

  private void skipElements(int... elements) throws XMLStreamException {
    int eventType = reader.getEventType();

    while (arrayContains(elements, eventType)) {
      eventType = reader.next();
    }
  }

  private boolean arrayContains(int[] searchArray, int value) {
    for (int ix : searchArray) {
      if (ix == value) {
        return true;
      }
    }

    return false;
  }

  @Override
  public Iterator<T> iterator() {
    return this;
  }
}
