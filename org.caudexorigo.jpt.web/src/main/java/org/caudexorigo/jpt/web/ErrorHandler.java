package org.caudexorigo.jpt.web;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.caudexorigo.ErrorAnalyser;

public class ErrorHandler
{
	private final Throwable _th;

	private final String _pagePath;

	public ErrorHandler(Throwable ex, String pagePath)
	{
		_th = ErrorAnalyser.findRootCause(ex);
		_pagePath = pagePath;
	}

	public String getExMessage()
	{
		return _th.getMessage();
	}

	public String getPagePath()
	{
		return _pagePath;
	}

	public String getStackTrace()
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		_th.printStackTrace(pw);
		pw.flush();
		return sw.toString();
	}
}
