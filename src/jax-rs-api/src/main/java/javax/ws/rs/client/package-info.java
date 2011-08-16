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
 * JAX-RS client API.
 * <p />
 * The JAX-RS client API is a Java based API for interoperating with RESTful
 * web services. It's goal is to enable developers to concisely and efficiently
 * implement reusable client-side solutions that leverage existing and well
 * established client-side HTTP implementations.
 * <p />
 * The JAX-RS client API can be utilized to interoperate with any RESTful web service.
 * It is not restricted to services implemented using JAX-RS. The goals of the
 * JAX-RS client API are threefold:
 * <ol>
 *     <li>Encapsulate a key constraint of the REST architectural style, namely
 *         the Uniform Interface Constraint and associated data elements, as
 *         client-side Java artifacts;</li>
 *     <li>Make it as easy to interoperate with RESTful Web services as JAX-RS
 *         makes it easy to build RESTful Web services; and</li>
 *     <li>Leverage artifacts of the JAX-RS API for the client side.</li>
 * </ol>
 *
 * The JAX-RS Client API supports a pluggable architecture by defining multiple
 * extension points.
 * 
 * 
 * 
 * TODO:
 * - what are the main API concepts?
 *   Client - the main entry point
 *   Target - covers request URI construction and modification
 *   Invocation - command pattern; generic request invocation interface
 *   RequestBuilder? - request building & fluent invocation
 * 
 * - what use cases should break the fluency of the API?
 *   Request method change
 *   Request URI change
 */
package javax.ws.rs.client;
