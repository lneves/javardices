package org.caudexorigo.cli;

interface ValidationErrorBuilder {
  void unexpectedOption(String name);

  void unexpectedValue(ArgumentSpecification optionSpecification);

  void missingValue(ArgumentSpecification optionSpecification);

  void unexpectedAdditionalValues(ArgumentSpecification optionSpecification);

  void missingOption(ArgumentSpecification optionSpecification);

  void unableToConstructType(ArgumentSpecification optionSpecification, String message);

  void invalidValueForType(ArgumentSpecification optionSpecification, String message);

  void patternMismatch(ArgumentSpecification optionSpecification, String value);

  void helpRequested(OptionsSpecification specification);

  void validate() throws ArgumentValidationException;
}
