package org.caudexorigo.cli;

interface ArgumentPresenter<O> {
  O presentArguments(TypedArguments arguments);
}
