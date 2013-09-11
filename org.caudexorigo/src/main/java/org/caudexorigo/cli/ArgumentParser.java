package org.caudexorigo.cli;

interface ArgumentParser
{
	ParsedArguments parseArguments() throws ArgumentValidationException;
}