package org.caudexorigo.jpt.web;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorHandler
{
	private final Throwable _th;

	private final String _pagePath;

	public ErrorHandler(Throwable ex, String pagePath)
	{
		_th = findRootCause(ex);
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

	private final Throwable findRootCause(Throwable ex)
	{
		Throwable error_ex = new Exception(ex);
		while (error_ex.getCause() != null)
		{
			error_ex = error_ex.getCause();
		}
		return error_ex;
	}
}
