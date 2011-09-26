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
/**
 * <h1>The JAX-RS client API</h1>
 *
 * The JAX-RS client API is a Java based API used to access Web resources.
 * It is not restricted to resources implemented using JAX-RS.
 * It's goal is to provide a higher-level abstraction compared to a {@link java.net.HttpURLConnection
 * plain HTTP communication API} as well as integration with the existing JAX-RS
 * providers, in order to enable concise and efficient implementation of
 * reusable client-side solutions that leverage existing and well
 * established client-side implementations of HTTP-based communication.
 * <p />
 * The JAX-RS Client API encapsulates the Uniform Interface Constraint &ndash;
 * a key constraint of the REST architectural style &ndash; and associated data
 * elements as client-side Java artifacts and supports a pluggable architecture
 * by defining multiple extension points.
 *
 * <h2>Client API Bootstrapping</h2>
 * The main entry point to the API is a {@link javax.ws.rs.client.ClientFactory}
 * that is used to bootstrap {@link javax.ws.rs.client.Client} instances,
 * {@link javax.ws.rs.client.Configuration configurable}, heavy-weight objects
 * that manage the underlying communication infrastructure and serve as the root
 * objects for accessing any Web resource. The following example illustrates the
 * bootstrapping and configuration of a {@code Client} instance:
 * <pre>
 *   Client client = ClientFactory.newClient();
 *
 *   client.configuration()
 *       .setProperty("MyProperty", "MyValue")
 *       .register(MyProvider.class)
 *       .enable(MyFeature.class);
 * </pre>
 * Custom {@code Client} extensions are possible via {@link javax.ws.rs.ext.ClientBuilderFactory}
 * SPI. Providing a custom client builder factory implementation at client bootstrapping
 * allows for a type-safe instantiation of the custom clients:
 * <pre>
 *   MyClient myClient = ClientFactory.newClientBy(MyClientBuilderFactory.class).build();
 *   myClient.enableCaching(true);
 * </pre>
 *
 * <h2>Accessing Web Resources</h2>
 * A Web resource can be accessed using a fluent API in which methods invocations
 * are chained to configure and ultimately submit an HTTP request. The following
 * example gets a {@code text/plain} representation of the resource identified by
 * {@code "http://example.org/hello"}:
 * <pre>
 *   Client client = ClientFactory.newClient();
 *   Response res = client.target("http://example.org/hello").request("text/plain").get();
 * </pre>
 * Conceptually, the steps required to submit a request are the following:
 * <ol>
 *   <li>obtain an client instance</li>
 *   <li>create a {@link javax.ws.rs.client.Target resource target}</li>
 *   <li>{@link javax.ws.rs.client.Invocation.Builder build} a request</li>
 *   <li>submit a request to directly retrieve a response or get a prepared
 *       {@link javax.ws.rs.client.Invocation} for later submission</li>
 * </ol>
 *
 * As illustrated above, individual Web resources are in the JAX-RS Client API
 * represented as resource targets. Each {@code Target} instance is bound to a
 * resource URI, e.g. {@code "http://example,org/foo/123"},
 * or a resource URI template, e.g. {@code "http://example,org/foo/{id}"}.
 * That way a single target can either point at a particular resource or represent
 * a larger group of resources (that e.g. share a common configuration) from which
 * concrete resources can be later derived:
 * <pre>
 *   Target foos = client.target("http://example.org/foo/{id}");
 *   Target foo123 = foos.path("id", 123); // http://example,org/foo/123
 *   Target foo456 = foos.path("id", 456); // http://example,org/foo/456
 * </pre>
 *
 *
 *
 * <p />
 * TODO:
 * - what are the main API concepts and how to use them?
 */
package javax.ws.rs.client;
