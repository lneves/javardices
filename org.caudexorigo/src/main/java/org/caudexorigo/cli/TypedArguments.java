package org.caudexorigo.cli;

interface TypedArguments {
  Object getValue(ArgumentSpecification specification);

  boolean contains(ArgumentSpecification specification);

  Object getUnparsedValue();
}
