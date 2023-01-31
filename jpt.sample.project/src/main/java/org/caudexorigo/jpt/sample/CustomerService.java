package org.caudexorigo.jpt.sample;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {
  private static final XorShift GENERATOR = new XorShift();
  private static final long LOWER_RANGE = -631152000000l;
  private static final long UPPER_RANGE = System.currentTimeMillis();
  private static final String[] FIRST_NAMES = new String[] {"Emily", "Jacob", "Michael", "Emma",
      "Joshua", "Madison", "Matthew", "Hannah", "Andrew", "Olivia", "Cletus", "Warner", "Sarah",
      "Billy", "Brittany", "Daniel", "David", "Cristman", "Colin", "Royalle"};
  private static final String[] LAST_NAMES = new String[] {"Aaron", "Bolingbroke", "Crounse",
      "Duff", "Drake", "Downs", "Driver", "Jasper", "Jetter", "O'Leary", "O'Malley", "Neville",
      "Towers", "Tripp", "Trull", "Wakefield", "Waller", "Badger", "Bagley", "Baker"};
  private static final String[] STATES = new String[] {"Alabama", "Alaska", "Arizona", "Arkansas",
      "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho",
      "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland",
      "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska",
      "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina",
      "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island",
      "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia",
      "Washington", "West Virginia", "Wisconsin", "Wyoming"};

  public static List<Customer> fetch(int count) {
    List<Customer> lst = new ArrayList<Customer>(count);

    for (int i = 0; i < count; i++) {
      Customer customer = new Customer();
      customer.setFirstName(randomString(FIRST_NAMES));
      customer.setLastName(randomString(LAST_NAMES));
      customer.setState(randomString(STATES));
      customer.setBirthDate(randomDate());
      customer.setId(i);
      lst.add(customer);
    }

    return lst;
  }

  private static String randomString(String[] items) {
    return items[GENERATOR.nextInt(items.length)];
  }

  private static LocalDate randomDate() {
    long rnd_unixtime = LOWER_RANGE + (long) (GENERATOR.nextDouble() * (UPPER_RANGE - LOWER_RANGE));
    Instant instant = Instant.ofEpochMilli(rnd_unixtime);
    return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
  }
}
