/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
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

import java.net.URI;

/**
 * An encapsulation of a web resource capable of building HTTP requests to send
 * to the web resource, synchronous HTTP method invocation as well as processing
 * responses returned from the web resource.
 * <p>
 * A {@code WebResource} instance is obtained from the {@link Client} instance.
 * <p>
 * The web resource implements the {@link HttpMethodInvoker} interface to invoke
 * the HTTP methods on the web resource. A client request may be built before
 * the actual HTTP method invocation.
 * <p>
 * Methods to create a request and return a response are thread-safe. Methods
 * that modify filters are not guaranteed to be thread-safe.
 * <p>
 * JAX-RS client API providers are expected to extend this abstract class to provide
 * their own implementation of resource building and synchronous HTTP method
 * invocations.
 *
 * @author Paul Sandoz
 * @author Marek Potociar
 * @since 2.0
 */
public abstract class WebResource extends WebResourceBase<WebResource, WebResource.Builder> implements HttpMethodInvoker {

    /**
     * Construct using the root {@link ClientHandler} and web resource {@link URI}.
     *
     * @param rootHandler the root client handler responsible for handling the request
     *     and returning a response.
     * @param uri {@link URI} identifying the new web resource.
     */
    protected WebResource(ClientHandler rootHandler, URI uri) {
        super(rootHandler, uri);
    }

    /**
     * Construct from an existing web resource base instance using the provided
     * {@code URI} as an identifier of the newly constructed web resource.
     *
     * @param that the web resource base instance to copy.
     * @param uri {@link URI} identifying the new web resource.
     */
    protected WebResource(WebResource that, URI uri) {
        super(that, uri);
    }

    /**
     * The request builder interface specific to the {@link WebResource} implementation
     * of a web resource. The {@code Builder} instance is responsible for building
     * a {@link ClientRequest} instance and synchronous invocation of an HTTP method
     * through the {@link AsyncHttpMethodInvoker} interface using the built client
     * request.
     * <p />
     * The methods of the {@link HttpMethodInvoker} interface are terminal client
     * request building methods of the builder while producing a synchronous response
     * to the built request in a single step.
     */
    public static interface Builder extends HttpMethodInvoker, InvocationBuilder<Builder> {
    }
}
