package jpt.test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Customer implements Comparable<Customer> {
  private Integer id;
  private String firstName;
  private String lastName;
  private String state;
  private Date birthDate;

  private String ftBirthDate;
  private static final SimpleDateFormat FORMAT = new SimpleDateFormat("MMMM d, yyyy");

  public int compareTo(Customer rhs) {
    return id.compareTo(rhs.getId());
  }

  @Override
  public boolean equals(Object rhs) {
    if (rhs instanceof Customer) {
      return compareTo((Customer) rhs) == 0;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
    ftBirthDate = FORMAT.format(birthDate);
  }

  public String getFtBirthDate() {
    return ftBirthDate;
  }

  public String formattedDate(Customer customer) {
    return FORMAT.format(customer.getBirthDate());
  }

}
