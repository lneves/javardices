package jpt.test;

import java.util.Arrays;

public class SimpleContext {

  public String getValue() {
    return "Hello World!!";
  }

  public String getLink() {
    return "http://www.yahoo.com/";
  }

  public String getCssLink() {
    return "link1";
  }

  public boolean isBoolt() {
    return true;
  }

  public Person[] getPersons() {
    Person[] p = new Person[4];
    p[0] = new Person(32, "luis");
    p[1] = new Person(34, "ana");
    p[2] = new Person(2, "elisa");
    p[3] = new Person(1, "xavier");

    return p;
  }

  public String getTitle() {
    return "SimpleContext Title";
  }

  @Override
  public String toString() {
    return String.format(
        "SimpleContext [calue()=%s, link()=%s, cssLink()=%s, boolt()=%s, persons()=%s, title()=%s]",
        getValue(), getLink(), getCssLink(), isBoolt(), Arrays.toString(getPersons()), getTitle());
  }

}
