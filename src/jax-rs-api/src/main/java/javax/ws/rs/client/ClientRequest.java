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

import java.net.URI;
import java.util.Locale;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

/**
 * A mutable client (out-bound) HTTP request.
 * <p>
 * Instances may be created by using one of the Client HTTP prepareMethod-specific
 * {@code ClientRequest} factory methods.
 * For example,
 * <blockquote><pre>
 * Client client = Client.create("http://jaxrs.example.com/jaxrsApplication/customers");
 *
 * ClientRequest postRequest = client.preparePost();
 * postRequest.entity(new Customer("Marek")).type("application/xml");
 *
 * ClientResponse response = postRequest.invoke();
 * </pre></blockquote>
 *
 * {@link Client} and {@code ClientRequest} API support the fluent invocation
 * builder DSL. The example above can be therefore shortened into the following
 * chain of prepareMethod invocations:
 * <blockquote><pre>
 * ClientResponse response = Client.create("http://jaxrs.example.com/jaxrsApplication/customers")
 *     .preparePost().entity(new Customer("Marek")).type("application/xml").invoke();
 * </pre></blockquote>
 *
 * @param <T> TODO
 * @author Marek Potociar
 * @since 2.0
 */
public interface ClientRequest<T extends ClientRequest> extends Cloneable {
    
    public static interface Builder<T extends ClientRequest> {

        T prepareGet();

        T preparePut();

        T preparePost();

        T prepareDelete();

        T prepareHead();

        T prepareOptions();
        
        T prepareTrace();

        T prepareMethod(String name);
    }

    // Getters
    /**
     * Get the URI of the request. The URI shall contain sufficient
     * components to correctly dispatch a request
     *
     * @return the URI of the request.
     */
    URI getURI();

    /**
     * Get the HTTP prepareMethod of the request.
     *
     * @return the HTTP prepareMethod.
     */
    String getMethod();

    /**
     * Get the entity of the request.
     *
     * @return the entity of the request.
     */
    Object getEntity();

    /**
     * Get the HTTP headers of the request.
     *
     * @return the HTTP headers of the request.
     */
    MultivaluedMap<String, Object> getHeaders();

    // URI builder methods
    T pathParam(String name, Object value) throws IllegalArgumentException;

    T pathParams(MultivaluedMap<String, Object> parameters) throws IllegalArgumentException;

    T formParam(String name, Object value) throws IllegalArgumentException;

    T formParams(MultivaluedMap<String, Object> parameters) throws IllegalArgumentException;

    T queryParam(String name, Object value) throws IllegalArgumentException;

    T queryParams(MultivaluedMap<String, Object> parameters) throws IllegalArgumentException;
    
    T redirect(String uri);
    
    T redirect(URI uri);
    
    T redirect(UriBuilder uri);
       
    // Request modifiers    
    /**
     * Add acceptable media types.
     *
     * @param types an array of the acceptable media types
     * @return the builder.
     */
    T accept(MediaType... types);

    /**
     * Add acceptable media types.
     *
     * @param types an array of the acceptable media types
     * @return the builder.
     */
    T accept(String... types);

    /**
     * Add acceptable languages.
     *
     * @param locales an array of the acceptable languages
     * @return the builder.
     */
    T acceptLanguage(Locale... locales);

    /**
     * Add acceptable languages.
     *
     * @param locales an array of the acceptable languages
     * @return the builder.
     */
    T acceptLanguage(String... locales);

    /**
     * Add a cookie to be set.
     *
     * @param cookie to be set.
     * @return the builder
     */
    T cookie(Cookie cookie);

    /**
     * Set the request entity.
     * <p>
     * Any Java type instance for a request entity, that is supported by the client
     * configuration of the client, can be passed. If generic information is
     * required then an instance of {@link javax.ws.rs.core.GenericEntity} may
     * be used.
     *
     * @param entity the request entity
     * @return the builder.
     */
    T entity(Object entity);

    /**
     * Add an HTTP header and value.
     *
     * @param name the HTTP header name.
     * @param value the HTTP header value.
     * @return the builder.
     */
    T header(String name, Object value);
//    
//    private static final RuntimeDelegate RUNTIME_DELEGATE = RuntimeDelegate.getInstance();
//
//    /**
//     * Convert a header value, represented as a general object, to the
//     * string value.
//     * <p>
//     * This prepareMethod defers to {@link RuntimeDelegate#createHeaderDelegate(java.lang.Class)}
//     * to obtain a {@link HeaderDelegate} to convert the value to a string. If
//     * a {@link HeaderDelegate} is not found then the {@link Object#toString() toString()}
//     * prepareMethod is utilized.
//     *
//     * @param headerValue the header value as an object.
//     * @return the string value
//     */
//    @SuppressWarnings("unchecked")
//    protected static String convertHeaderValueToString(final Object headerValue) {
//        HeaderDelegate hp = RUNTIME_DELEGATE.createHeaderDelegate(headerValue.getClass());
//
//        return (hp != null) ? hp.toString(headerValue) : headerValue.toString();
//    }

    T method(String httpMethod);
    
    /**
     * Set the media type.
     *
     * @param type the media type
     * @return the builder.
     */
    T type(MediaType type);

    /**
     * Set the media type.
     *
     * @param type the media type
     * @return the builder.
     */
    T type(String type);
}
