/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
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
package javax.ws.rs.client;

import javax.ws.rs.core.Configuration;

import javax.net.ssl.SSLContext;

/**
 * A client instance builder contract.
 *
 * A client builder is produced by {@link ClientFactory} and provides a way
 * of configuring various client runtime aspects (such as SSL configuration)
 * prior to building an actual client instance.
 *
 * @author Marek Potociar (marek.potociar at oracle.com)
 * @since 2.0
 */
public interface ClientBuilder {
    /**
     * Set the SSL context that will be used when creating secured transport connections
     * to server endpoints from {@link WebTarget web targets} created by the client
     * instance that is using this SSL context.
     *
     * @param sslContext secure socket protocol implementation which acts as a factory
     *                   for secure socket factories or {@link javax.net.ssl.SSLEngine
     *                   SSL engines}.
     * @return an updated client builder instance.
     */
    public ClientBuilder sslContext(final SSLContext sslContext);

    /**
     * Set the initial configuration for the client instance that is being built.
     *
     * @param configuration data used to provide initial configuration for
     *                      the new client instance.
     * @return an updated client builder instance.
     */
    public ClientBuilder configuration(final Configuration configuration);

    /**
     * Build a new client instance using all the configuration previously specified
     * in this client builder.
     *
     * @return a new client instance.
     */
    public Client build();
}
