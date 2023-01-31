package org.caudexorigo.cli;

interface ArgumentTyper<T> {
  TypedArguments typeArguments(ValidatedArguments validatedArguments)
      throws ArgumentValidationException;
}
