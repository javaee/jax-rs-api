/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (responseType) 2010-2011 Oracle and/or its affiliates. All rights reserved.
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

import java.util.concurrent.Future;

import javax.ws.rs.core.GenericType;

/**
 * An asynchronous uniform interface for invoking HTTP methods. The interface
 * defines all supported variants of asynchronous HTTP method invocations.
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
public interface AsyncHttpMethodInvoker {

    /**
     * Invoke the HEAD method.
     *
     * @return the HTTP response.
     */
    Future<ClientResponse> head();

    /**
     * Invoke the HEAD method.
     *
     * @param responseListener the listener to receive asynchronous callbacks.
     * @return a future that may be used to wait until the future completes and
     *         obtain the client response state, or cancel the request.
     */
    Future<ClientResponse> head(TypeListener<ClientResponse> responseListener);

    /**
     * Invoke the OPTIONS method.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseType} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseType the type of the returned response.
     * @return an instance of type <code>c</code>.
     */
     <T> Future<T> options(Class<T> responseType);

    /**
     * Invoke the OPTIONS method.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseType} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseType the generic type of the returned response.
     * @return an instance of type represented by the generic type.
     */
     <T> Future<T> options(GenericType<T> responseType);

    /**
     * Invoke the OPTIONS method.
     * <p />
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseListener} is not for the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseListener the listener to receive asynchronous callbacks.
     * @return a future that may be used to wait until the future completes and
     *         obtain the client response state, or cancel the request.
     */
     <T> Future<T> options(TypeListener<T> responseListener);

    /**
     * Invoke the GET method.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseType} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseType the type of the returned response.
     * @return an instance of type <code>c</code>.
     */
     <T> Future<T> get(Class<T> responseType);

    /**
     * Invoke the GET method.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseType} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseType the generic type of the returned response.
     * @return an instance of type represented by the generic type.
     */
     <T> Future<T> get(GenericType<T> responseType);

    /**
     * Invoke the GET method.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseListener} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseListener the listener to receive asynchronous callbacks.
     * @return a future that may be used to wait until the future completes and
     *         obtain the client response state, or cancel the request.
     */
     <T> Future<T> get(TypeListener<T> responseListener);

    /**
     * Invoke the PUT method with no request entity or response.
     * <p>
     * If the status of the HTTP response is less than 300 and a representation
     * is present then that representation is ignored.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300.
     *
     * @return a void future.
     */
    Future<?> put();

    /**
     * Invoke the PUT method with a request entity but no response.
     * <p>
     * If the status of the HTTP response is less than 300 and a representation
     * is present then that representation is ignored.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300.
     *
     * @param requestEntity the request entity.
     * @return a void future.
     */
    Future<?> put(Object requestEntity);

    /**
     * Invoke the PUT method with no request entity that returns a response.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseType} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseType the type of the returned response.
     * @return an instance of type <code>c</code>.
     */
     <T> Future<T> put(Class<T> responseType);

    /**
     * Invoke the PUT method with a request entity that returns a response.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseType} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseType the generic type of the returned response.
     * @return an instance of type represented by the generic type.
     */
     <T> Future<T> put(GenericType<T> responseType);

    /**
     * Invoke the PUT method.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseType} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseListener the listener to receive asynchronous callbacks.
     * @return a future that may be used to wait until the future completes and
     *         obtain the client response state, or cancel the request.
     */
     <T> Future<T> put(TypeListener<T> responseListener);

    /**
     * Invoke the PUT method with a request entity that returns a response.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseType} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseType the type of the returned response.
     * @param requestEntity the request entity.
     * @return an instance of type <code>c</code>.
     */
     <T> Future<T> put(Class<T> responseType, Object requestEntity);

    /**
     * Invoke the PUT method with a request entity that returns a response.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseType} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseType the generic type of the returned response.
     * @param requestEntity the request entity.
     * @return an instance of type represented by the generic type.
     */
     <T> Future<T> put(GenericType<T> responseType, Object requestEntity);

    /**
     * Invoke the PUT method.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseListener} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseListener the listener to receive asynchronous callbacks.
     * @param requestEntity the request entity.
     * @return a future that may be used to wait until the future completes and
     *         obtain the client response state, or cancel the request.
     */
     <T> Future<T> put(TypeListener<T> responseListener, Object requestEntity);

    /**
     * Invoke the POST method with no request entity or response.
     * <p>
     * If the status of the HTTP response is less than 300 and a representation
     * is present then that representation is ignored.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300.
     *
     * @return a void future.
     */
    Future<?> post();

    /**
     * Invoke the POST method with a request entity but no response.
     * <p>
     * If the status of the HTTP response is less than 300 and a representation
     * is present then that representation is ignored.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300.
     *
     * @return a void future.
     * @param requestEntity the request entity.
     */
    Future<?> post(Object requestEntity);

    /**
     * Invoke the POST method with no request entity that returns a response.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseType} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseType the type of the returned response.
     * @return an instance of type <code>c</code>.
     */
     <T> Future<T> post(Class<T> responseType);

    /**
     * Invoke the POST method with a request entity that returns a response.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseType} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseType the generic type of the returned response.
     * @return an instance of type represented by the generic type.
     */
     <T> Future<T> post(GenericType<T> responseType);

    /**
     * Invoke the POST method.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseListener} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseListener the listener to receive asynchronous callbacks.
     * @return a future that may be used to wait until the future completes and
     *         obtain the client response state, or cancel the request.
     */
     <T> Future<T> post(TypeListener<T> responseListener);

    /**
     * Invoke the POST method with a request entity that returns a response.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseType} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseType the type of the returned response.
     * @param requestEntity the request entity.
     * @return an instance of type <code>c</code>.
     */
     <T> Future<T> post(Class<T> responseType, Object requestEntity);

    /**
     * Invoke the POST method with a request entity that returns a response.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseType} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseType the generic type of the returned response.
     * @param requestEntity the request entity.
     * @return an instance of type represented by the generic type.
     */
     <T> Future<T> post(GenericType<T> responseType, Object requestEntity);

    /**
     * Invoke the POST method.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseListener} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseListener the listener to receive asynchronous callbacks.
     * @param requestEntity the request entity.
     * @return a future that may be used to wait until the future completes and
     *         obtain the client response state, or cancel the request.
     */
     <T> Future<T> post(TypeListener<T> responseListener, Object requestEntity);

    /**
     * Invoke the DELETE method with no request entity or response.
     * <p>
     * If the status of the HTTP response is less than 300 and a representation
     * is present then that representation is ignored.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300.
     *
     * @return a void future.
     */
    Future<?> delete();

    /**
     * Invoke the DELETE method with a request entity but no response.
     * <p>
     * If the status of the HTTP response is less than 300 and a representation
     * is present then that representation is ignored.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300.
     *
     * @return a void future.
     * @param requestEntity the request entity.
     */
    Future<?> delete(Object requestEntity);

    /**
     * Invoke the DELETE method with no request entity that returns a response.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseType} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseType the type of the returned response.
     * @return an instance of type <code>c</code>.
     */
     <T> Future<T> delete(Class<T> responseType);

    /**
     * Invoke the DELETE method with a request entity that returns a response.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseType} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseType the generic type of the returned response.
     * @return an instance of type represented by the generic type.
     */
     <T> Future<T> delete(GenericType<T> responseType);

    /**
     * Invoke the DELETE method.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseListener} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseListener the listener to receive asynchronous callbacks.
     * @return a future that may be used to wait until the future completes and
     *         obtain the client response state, or cancel the request.
     */
     <T> Future<T> delete(TypeListener<T> responseListener);

    /**
     * Invoke the DELETE method with a request entity that returns a response.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseType} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseType the type of the returned response.
     * @param requestEntity the request entity.
     * @return an instance of type <code>c</code>.
     */
     <T> Future<T> delete(Class<T> responseType, Object requestEntity);

    /**
     * Invoke the DELETE method with a request entity that returns a response.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseType} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseType the generic type of the returned response.
     * @param requestEntity the request entity.
     * @return an instance of type represented by the generic type.
     */
     <T> Future<T> delete(GenericType<T> responseType, Object requestEntity);

    /**
     * Invoke the DELETE method.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseListener} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param responseListener the listener to receive asynchronous callbacks.
     * @param requestEntity the request entity.
     * @return a future that may be used to wait until the future completes and
     *         obtain the client response state, or cancel the request.
     */
     <T> Future<T> delete(TypeListener<T> responseListener, Object requestEntity);

    /**
     * Invoke a HTTP method with no request entity or response.
     * <p>
     * If the status of the HTTP response is less than 300 and a representation
     * is present then that representation is ignored.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300.
     *
     * @return a void future.
     * @param method the HTTP method.
     */
    Future<?> method(String method);

    /**
     * Invoke a HTTP method with a request entity but no response.
     * <p>
     * If the status of the HTTP response is less than 300 and a representation
     * is present then that representation is ignored.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300.
     *
     * @return a void future.
     * @param method the HTTP method.
     * @param requestEntity the request entity.
     */
    Future<?> method(String method, Object requestEntity);

    /**
     * Invoke a HTTP method with no request entity that returns a response.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseType} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param method the HTTP method.
     * @param responseType the type of the returned response.
     * @return an instance of type <code>c</code>.
     */
     <T> Future<T> method(String method, Class<T> responseType);

    /**
     * Invoke a HTTP method with no request entity that returns a response.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseType} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param method the HTTP method.
     * @param responseType the generic type of the returned response.
     * @return an instance of type represented by the generic type.
     */
     <T> Future<T> method(String method, GenericType<T> responseType);

    /**
     * Invoke a HTTP method with no request entity that returns a response.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseListener} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param method the HTTP method.
     * @param responseListener the listener to receive asynchronous callbacks.
     * @return a future that may be used to wait until the future completes and
     *         obtain the client response state, or cancel the request.
     */
     <T> Future<T> method(String method, TypeListener<T> responseListener);

    /**
     * Invoke a HTTP method with a request entity that returns a response.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseType} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param method the HTTP method.
     * @param responseType the type of the returned response.
     * @param requestEntity the request entity.
     * @return an instance of type <code>c</code>.
     */
     <T> Future<T> method(String method, Class<T> responseType, Object requestEntity);

    /**
     * Invoke a HTTP method with a request entity that returns a response.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseType} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param method the HTTP method.
     * @param responseType the generic type of the returned response.
     * @param requestEntity the request entity.
     * @return an instance of type represented by the generic type.
     */
     <T> Future<T> method(String method, GenericType<T> responseType, Object requestEntity);

    /**
     * Invoke a HTTP method with a request entity that returns a response.
     * <p>
     * The {@link Future#get} method will throw a {@link HttpMethodInvocationException}
     * if the status of the HTTP response is greater than or equal to 300 and
     * {@code responseListener} is not the type {@link ClientResponse}.
     *
     * @param <T> the type of the response.
     * @param method the HTTP method.
     * @param responseListener the listener to receive asynchronous callbacks.
     * @param requestEntity the request entity.
     * @return a future that may be used to wait until the future completes and
     *         obtain the client response state, or cancel the request.
     */
     <T> Future<T> method(String method, TypeListener<T> responseListener, Object requestEntity);
}
