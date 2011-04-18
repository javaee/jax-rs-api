/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2011 Oracle and/or its affiliates. All rights reserved.
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

import javax.ws.rs.core.GenericType;

/**
 * A uniform interface for synchronous invocation of HTTP methods. The interface
 * defines all supported variants of synchronous HTTP method invocations.
 * <p>
 * Where applicable, any Java type instance representing a request entity, that
 * is supported by the client configuration, can be passed as an input parameter.
 * If generic information is required then an instance of
 * {@link javax.ws.rs.core.GenericEntity} may be used.
 * <p>
 * Any Java type representing a response entity, that is supported by the client
 * configuration, may be declared using <code>Class&lt;T&gt;</code> where
 * {@code T} is the Java type, or using {@link GenericType} where the generic
 * parameter is the Java type.
 * <p>
 * A type of {@link ClientResponse} declared for the response entity
 * may be used to obtain the status, headers and response entity. If any other
 * type is declared and the response status is greater than or equal to
 * {@code 300} then a {@link HttpMethodInvocationException} exception will be thrown,
 * from which the {@link ClientResponse} instance can be accessed.
 *
 * @author Paul Sandoz
 * @author Marek Potociar
 * @since 2.0
 */
public interface HttpMethodInvoker {

    /**
     * Invoke the DELETE method with no request entity or response.
     * <p>
     * If the status code is less than 300 and a representation is present
     * then that representation is ignored.
     *
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300.
     */
    void delete() throws HttpMethodInvocationException;

    /**
     * Invoke the DELETE method with a request entity but no response.
     * <p>
     * If the status code is less than 300 and a representation is present
     * then that representation is ignored.
     *
     * @param requestEntity the request entity.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300.
     */
    void delete(Object requestEntity) throws HttpMethodInvocationException;

    /**
     * Invoke the DELETE method with no request entity that returns a response.
     *
     * @param <T> the type of the response.
     * @param c the type of the returned response.
     * @return an instance of type <code>c</code>.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300 and <code>c</code> is not the type
     * {@link ClientResponse}.
     */
    <T> T delete(Class<T> c) throws HttpMethodInvocationException;

    /**
     * Invoke the DELETE method with a request entity that returns a response.
     *
     * @param <T> the type of the response.
     * @param gt the generic type of the returned response.
     * @return an instance of type represented by the generic type.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300 and <code>gt</code> does not
     * represent the type {@link ClientResponse}.
     */
    <T> T delete(GenericType<T> gt) throws HttpMethodInvocationException;

    /**
     * Invoke the DELETE method with a request entity that returns a response.
     *
     * @param <T> the type of the response.
     * @param c the type of the returned response.
     * @param requestEntity the request entity.
     * @return an instance of type <code>c</code>.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300 and <code>c</code> is not the type
     * {@link ClientResponse}.
     */
    <T> T delete(Class<T> c, Object requestEntity) throws HttpMethodInvocationException;

    /**
     * Invoke the DELETE method with a request entity that returns a response.
     *
     * @param <T> the type of the response.
     * @param gt the generic type of the returned response.
     * @param requestEntity the request entity.
     * @return an instance of type represented by the generic type.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300 and <code>gt</code> does not
     * represent the type {@link ClientResponse}.
     */
    <T> T delete(GenericType<T> gt, Object requestEntity) throws HttpMethodInvocationException;

    /**
     * Invoke the GET method.
     *
     * @param <T> the type of the response.
     * @param c the type of the returned response.
     * @return an instance of type <code>c</code>.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300 and <code>c</code> is not the type
     * {@link ClientResponse}.
     */
    <T> T get(Class<T> c) throws HttpMethodInvocationException;

    /**
     * Invoke the GET method.
     *
     * @param <T> the type of the response.
     * @param gt the generic type of the returned response.
     * @return an instance of type represented by the generic type.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300 and <code>gt</code> does not
     * represent the type {@link ClientResponse}.
     */
    <T> T get(GenericType<T> gt) throws HttpMethodInvocationException;

    /**
     * Invoke the HEAD method.
     *
     * @return the HTTP response.
     */
    ClientResponse head();

    /**
     * Invoke a HTTP method with no request entity or response.
     * <p>
     * If the status code is less than 300 and a representation is present
     * then that representation is ignored.
     *
     * @param method the HTTP method.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300.
     */
    void method(String method) throws HttpMethodInvocationException;

    /**
     * Invoke a HTTP method with a request entity but no response.
     * <p>
     * If the status code is less than 300 and a representation is present
     * then that representation is ignored.
     *
     * @param method the HTTP method.
     * @param requestEntity the request entity.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300.
     */
    void method(String method, Object requestEntity) throws HttpMethodInvocationException;

    /**
     * Invoke a HTTP method with no request entity that returns a response.
     *
     * @param <T> the type of the response.
     * @param method the HTTP method.
     * @param c the type of the returned response.
     * @return an instance of type <code>c</code>.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300 and <code>c</code> is not the type
     * {@link ClientResponse}.
     */
    <T> T method(String method, Class<T> c) throws HttpMethodInvocationException;

    /**
     * Invoke a HTTP method with no request entity that returns a response.
     *
     * @param <T> the type of the response.
     * @param method the HTTP method.
     * @param gt the generic type of the returned response.
     * @return an instance of type represented by the generic type.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300 and <code>gt</code> does not
     * represent the type {@link ClientResponse}.
     */
    <T> T method(String method, GenericType<T> gt) throws HttpMethodInvocationException;

    /**
     * Invoke a HTTP method with a request entity that returns a response.
     *
     * @param <T> the type of the response.
     * @param method the HTTP method.
     * @param c the type of the returned response.
     * @param requestEntity the request entity.
     * @return an instance of type <code>c</code>.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300 and <code>c</code> is not the type
     * {@link ClientResponse}.
     */
    <T> T method(String method, Class<T> c, Object requestEntity) throws HttpMethodInvocationException;

    /**
     * Invoke a HTTP method with a request entity that returns a response.
     *
     * @param <T> the type of the response.
     * @param method the HTTP method.
     * @param gt the generic type of the returned response.
     * @param requestEntity the request entity.
     * @return an instance of type represented by the generic type.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300 and <code>gt</code> does not
     * represent the type {@link ClientResponse}.
     */
    <T> T method(String method, GenericType<T> gt, Object requestEntity) throws HttpMethodInvocationException;

    /**
     * Invoke the OPTIONS method.
     *
     * @param <T> the type of the response.
     * @param c the type of the returned response.
     * @return an instance of type <code>c</code>.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300 and <code>c</code> is not the type
     * {@link ClientResponse}.
     */
    <T> T options(Class<T> c) throws HttpMethodInvocationException;

    /**
     * Invoke the OPTIONS method.
     *
     * @param <T> the type of the response.
     * @param gt the generic type of the returned response.
     * @return an instance of type represented by the generic type.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300 and <code>gt</code> does not
     * represent the type {@link ClientResponse}.
     */
    <T> T options(GenericType<T> gt) throws HttpMethodInvocationException;

    /**
     * Invoke the POST method with no request entity or response.
     * <p>
     * If the status code is less than 300 and a representation is present
     * then that representation is ignored.
     *
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300.
     */
    void post() throws HttpMethodInvocationException;

    /**
     * Invoke the POST method with a request entity but no response.
     * <p>
     * If the status code is less than 300 and a representation is present
     * then that representation is ignored.
     *
     * @param requestEntity the request entity.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300.
     */
    void post(Object requestEntity) throws HttpMethodInvocationException;

    /**
     * Invoke the POST method with no request entity that returns a response.
     *
     * @param <T> the type of the response.
     * @param c the type of the returned response.
     * @return an instance of type <code>c</code>.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300 and <code>c</code> is not the type
     * {@link ClientResponse}.
     */
    <T> T post(Class<T> c) throws HttpMethodInvocationException;

    /**
     * Invoke the POST method with a request entity that returns a response.
     *
     * @param <T> the type of the response.
     * @param gt the generic type of the returned response.
     * @return an instance of type represented by the generic type.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300 and <code>gt</code> does not
     * represent the type {@link ClientResponse}.
     */
    <T> T post(GenericType<T> gt) throws HttpMethodInvocationException;

    /**
     * Invoke the POST method with a request entity that returns a response.
     *
     * @param <T> the type of the response.
     * @param c the type of the returned response.
     * @param requestEntity the request entity.
     * @return an instance of type <code>c</code>.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300 and <code>c</code> is not the type
     * {@link ClientResponse}.
     */
    <T> T post(Class<T> c, Object requestEntity) throws HttpMethodInvocationException;

    /**
     * Invoke the POST method with a request entity that returns a response.
     *
     * @param <T> the type of the response.
     * @param gt the generic type of the returned response.
     * @param requestEntity the request entity.
     * @return an instance of type represented by the generic type.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300 and <code>gt</code> does not
     * represent the type {@link ClientResponse}.
     */
    <T> T post(GenericType<T> gt, Object requestEntity) throws HttpMethodInvocationException;

    /**
     * Invoke the PUT method with no request entity or response.
     * <p>
     * If the status code is less than 300 and a representation is present
     * then that representation is ignored.
     *
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300.
     */
    void put() throws HttpMethodInvocationException;

    /**
     * Invoke the PUT method with a request entity but no response.
     * <p>
     * If the status code is less than 300 and a representation is present
     * then that representation is ignored.
     *
     * @param requestEntity the request entity.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300.
     */
    void put(Object requestEntity) throws HttpMethodInvocationException;

    /**
     * Invoke the PUT method with no request entity that returns a response.
     *
     * @param <T> the type of the response.
     * @param c the type of the returned response.
     * @return an instance of type <code>c</code>.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300 and <code>c</code> is not the type
     * {@link ClientResponse}.
     */
    <T> T put(Class<T> c) throws HttpMethodInvocationException;

    /**
     * Invoke the PUT method with a request entity that returns a response.
     *
     * @param <T> the type of the response.
     * @param gt the generic type of the returned response.
     * @return an instance of type represented by the generic type.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300 and <code>gt</code> does not
     * represent the type {@link ClientResponse}.
     */
    <T> T put(GenericType<T> gt) throws HttpMethodInvocationException;

    /**
     * Invoke the PUT method with a request entity that returns a response.
     *
     * @param <T> the type of the response.
     * @param c the type of the returned response.
     * @param requestEntity the request entity.
     * @return an instance of type <code>c</code>.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300 and <code>c</code> is not the type
     * {@link ClientResponse}.
     */
    <T> T put(Class<T> c, Object requestEntity) throws HttpMethodInvocationException;

    /**
     * Invoke the PUT method with a request entity that returns a response.
     *
     * @param <T> the type of the response.
     * @param gt the generic type of the returned response.
     * @param requestEntity the request entity.
     * @return an instance of type represented by the generic type.
     * @throws HttpMethodInvocationException if the status of the HTTP response is
     * greater than or equal to 300 and <code>gt</code> does not
     * represent the type {@link ClientResponse}.
     */
    <T> T put(GenericType<T> gt, Object requestEntity) throws HttpMethodInvocationException;
}
