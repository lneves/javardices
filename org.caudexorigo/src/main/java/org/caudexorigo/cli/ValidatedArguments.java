package org.caudexorigo.cli;

import java.util.List;

interface ValidatedArguments extends ArgumentCollection {
  List<String> getValues(String... options);

  List<String> getValues(List<String> options);
}
