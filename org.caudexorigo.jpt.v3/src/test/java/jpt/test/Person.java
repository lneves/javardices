package jpt.test;

import java.util.ArrayList;
import java.util.List;

public class Person {
  private int age;

  private String name;

  public Person(int age, String name) {
    super();
    this.age = age;
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Integer> getSubItems() {
    List<Integer> l = new ArrayList<>();
    l.add(1);
    l.add(2);
    return l;
  }

  @Override
  public String toString() {
    return String.format("Person [age=%s, name=%s, getSubItems()=%s]", age, name, getSubItems());
  }
}
