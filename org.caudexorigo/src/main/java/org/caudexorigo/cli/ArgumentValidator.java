package org.caudexorigo.cli;

interface ArgumentValidator<O>
{
	ValidatedArguments validateArguments(ParsedArguments arguments) throws ArgumentValidationException;
}