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
package org.glassfish.jersey.server;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.internal.inject.Providers;
import org.glassfish.jersey.server.spi.ContainerProvider;

import org.glassfish.hk2.api.ServiceLocator;

/**
 * Factory for creating specific HTTP-based containers.
 *
 * @author Paul Sandoz
 * @author Jakub Podlesak (jakub.podlesak at oracle.com)
 * @author Marek Potociar (marek.potociar at oracle.com)
 */
public final class ContainerFactory {

    private ContainerFactory() {
    }

    /**
     * Create a container according to the class requested.
     * <p>
     * The list of service-provider supporting the {@link ContainerProvider}
     * service-provider will be iterated over until one returns a non-null
     * container instance.
     * <p>
     *
     * @param <T> container type
     * @param type Type of the container
     * @param application Jersey application.
     * @return the container.
     * @throws ContainerException if there is an error creating the container.
     * @throws IllegalArgumentException if no container provider supports the type.
     */
    @SuppressWarnings("unchecked")
    public static <T> T createContainer(Class<T> type, Application application) {

        ApplicationHandler handler = new ApplicationHandler(application);

        final ServiceLocator locator = handler.getServiceLocator();
        for (ContainerProvider cp : Providers.getCustomProviders(locator, ContainerProvider.class)) {
            T c = cp.createContainer(type, handler);
            if (c != null) {
                return c;
            }
        }

        for (ContainerProvider cp : Providers.getProviders(locator, ContainerProvider.class)) {
            T c = cp.createContainer(type, handler);
            if (c != null) {
                return c;
            }
        }

        throw new IllegalArgumentException("No container provider supports the type " + type);
    }
}
