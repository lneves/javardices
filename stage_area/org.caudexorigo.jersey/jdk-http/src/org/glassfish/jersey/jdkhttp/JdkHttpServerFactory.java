/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.jersey.jdkhttp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.Executors;

import org.glassfish.jersey.internal.ProcessingException;
import org.glassfish.jersey.jdkhttp.internal.LocalizationMessages;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.internal.ConfigHelper;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsServer;

/**
 * Factory for creating {@link HttpServer JDK HttpServer} instances adapted to the {@link ApplicationHandler}.
 * 
 * @author Miroslav Fuksa (miroslav.fuksa at oracle.com)
 */
public class JdkHttpServerFactory
{

	/**
	 * Creates and starts the {@link HttpServer JDK HttpServer} with the Jersey application deployed on the given {@link URI}.
	 * 
	 * <p>
	 * The returned {@link HttpServer JDK HttpServer} is started.
	 * </p>
	 * 
	 * @param uri
	 *            The {@link URI uri} on which the Jersey application will be deployed.
	 * @param configuration
	 *            The Jersey server-side application configuration.
	 * @return Newly created {@link HttpServer}.
	 * @throws ProcessingException
	 *             Thrown when problems during server creation occurs.
	 */
	public static HttpServer createHttpServer(final URI uri, final ResourceConfig configuration) throws ProcessingException
	{
		// final JdkHttpHandlerContainer handler = ContainerFactory.createContainer(JdkHttpHandlerContainer.class, configuration);
		ApplicationHandler application = new ApplicationHandler(configuration);
		final JdkHttpHandlerContainer handler = new JdkHttpHandlerContainer(application);
		return createHttpServer(uri, handler, ConfigHelper.getContainerLifecycleListener(new ApplicationHandler(configuration)));
	}

	/**
	 * Creates and starts the {@link HttpServer JDK HttpServer} with the Jersey application deployed on the given {@link URI}.
	 * 
	 * <p>
	 * The returned {@link HttpServer JDK HttpServer} is started.
	 * </p>
	 * 
	 * @param uri
	 *            The {@link URI uri} on which the Jersey application will be deployed.
	 * @param appHandler
	 *            The Jersey server-side application handler.
	 * @return Newly created {@link HttpServer}.
	 * @throws ProcessingException
	 *             Thrown when problems during server creation occurs.
	 */
	public static HttpServer createHttpServer(final URI uri, final ApplicationHandler appHandler) throws ProcessingException
	{
		return createHttpServer(uri, new JdkHttpHandlerContainer(appHandler), ConfigHelper.getContainerLifecycleListener(appHandler));
	}

	private static HttpServer createHttpServer(final URI uri, final JdkHttpHandlerContainer handler, final ContainerLifecycleListener containerListener) throws ProcessingException
	{

		if (uri == null)
		{
			throw new IllegalArgumentException(LocalizationMessages.ERROR_CONTAINER_URI_NULL());
		}

		final String scheme = uri.getScheme();
		if (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https"))
		{
			throw new IllegalArgumentException(LocalizationMessages.ERROR_CONTAINER_URI_SCHEME_UNKNOWN(uri));
		}

		final String path = uri.getPath();
		if (path == null)
		{
			throw new IllegalArgumentException(LocalizationMessages.ERROR_CONTAINER_URI_PATH_NULL(uri));
		}
		else if (path.length() == 0)
		{
			throw new IllegalArgumentException(LocalizationMessages.ERROR_CONTAINER_URI_PATH_EMPTY(uri));
		}
		else if (path.charAt(0) != '/')
		{
			throw new IllegalArgumentException(LocalizationMessages.ERROR_CONTAINER_URI_PATH_START(uri));
		}

		final int port = (uri.getPort() == -1) ? 80 : uri.getPort();
		HttpServer server;
		try
		{
			server = (scheme.equalsIgnoreCase("http")) ? HttpServer.create(new InetSocketAddress(port), 0) : HttpsServer.create(new InetSocketAddress(port), 0);
		}
		catch (IOException ioe)
		{
			throw new ProcessingException(LocalizationMessages.ERROR_CONTAINER_EXCEPTION_IO(), ioe);
		}

		server.setExecutor(Executors.newCachedThreadPool());
		server.createContext(path, handler);
		server.start();

		containerListener.onStartup(handler);

		return server;
	}

	/**
	 * Prevents instantiation.
	 */
	private JdkHttpServerFactory()
	{
	}
}
