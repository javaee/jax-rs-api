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
import java.util.Map;
import java.util.concurrent.Future;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.RuntimeDelegate;
import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

/**
 * A mutable client (out-bound) HTTP request.
 * <p>
 * Instances may be created by using one of the Client HTTP method-specific
 * {@code ClientRequest} factory methods.
 * For example,
 * <blockquote><pre>
 * Client client = Client.create("http://jaxrs.example.com/jaxrsApplication/customers");
 *
 * ClientRequest postRequest = client.post();
 * postRequest.entity(new Customer("Marek")).type("application/xml");
 *
 * ClientResponse response = postRequest.invoke();
 * </pre></blockquote>
 *
 * {@link Client} and {@code ClientRequest} API support the fluent invocation
 * builder DSL. The example above can be therefore shortened into the following
 * chain of method invocations:
 * <blockquote><pre>
 * ClientResponse response = Client.create("http://jaxrs.example.com/jaxrsApplication/customers")
 *     .post().entity(new Customer("Marek")).type("application/xml").invoke();
 * </pre></blockquote>
 *
 * @author Marek Potociar
 * @since 2.0
 */
public abstract class ClientRequest {

    // Getters
    /**
     * Get the URI of the request. The URI shall contain sufficient
     * components to correctly dispatch a request
     *
     * @return the URI of the request.
     */
    public abstract URI getURI();

    /**
     * Get the HTTP method of the request.
     *
     * @return the HTTP method.
     */
    public abstract String getMethod();

    /**
     * Get the entity of the request.
     *
     * @return the entity of the request.
     */
    public abstract Object getEntity();

    /**
     * Get the HTTP headers of the request.
     *
     * @return the HTTP headers of the request.
     */
    public abstract MultivaluedMap<String, Object> getHeaders();

    /**
     * Get the mutable property bag.
     *
     * @return the property bag.
     */
    public abstract Map<String, Object> getProperties();

    /**
     * Determine if a feature is enabled.
     *
     * @param featureName the name of the feature.
     * @return {@code true} if the feature value is present in the property bag
     *     and is an instance of {@link java.lang.Boolean} and that value is {@code true},
     *     otherwise {@code false}.
     */
    public boolean isEnabled(final String featureName) {
        return getPropertyAsFeature(featureName, false);
    }

    /**
     * Get a feature that is boolean property of the property bag.
     *
     * @param name the name of the feature.
     * @param defaultValue the default boolean value if the property is absent.
     * @return true if the feature value is present and is an instance of
     *         {@link java.lang.Boolean} and that value is true, otherwise the
     *         <code>defaultValue</code>.
     */
    private boolean getPropertyAsFeature(final String name, final boolean defaultValue) {
        Boolean v = (Boolean) getProperties().get(name);
        return (v != null) ? v : defaultValue;
    }

    // Path builder methods
    public abstract ClientRequest formParam(String name, Object value) throws IllegalArgumentException;

    public abstract ClientRequest formParams(MultivaluedMap<String, Object> parameters) throws IllegalArgumentException;

    public abstract ClientRequest queryParam(String name, Object value) throws IllegalArgumentException;

    public abstract ClientRequest queryParams(MultivaluedMap<String, Object> parameters)
            throws IllegalArgumentException;

    public abstract ClientRequest pathParam(String name, Object value) throws IllegalArgumentException;

    public abstract ClientRequest pathParams(MultivaluedMap<String, Object> parameters) throws IllegalArgumentException;

    // Request builder methods
    /**
     * TODO javadoc.
     *
     * @param name property name.
     * @param value property value.
     * @return the updated request.
     */
    public abstract ClientRequest property(String name, Object value);

    /**
     * TODO javadoc.
     *
     * @param name feature name.
     * @return the updated request.
     */
    public abstract ClientRequest enableFeature(String name);

    /**
     * TODO javadoc.
     *
     * @param name feature name.
     * @return the updated request.
     */
    public abstract ClientRequest disableFeature(String name);

    /**
     * Sets properties (replaces everything previously set).
     *
     * @param properties set of properties for the client request. The content of
     *     the map will replace any existing properties set on the client request.
     * @return the updated request
     */
    public abstract ClientRequest properties(Map<String, Object> properties);

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
    public abstract ClientRequest entity(Object entity);

    /**
     * Set the media type.
     *
     * @param type the media type
     * @return the builder.
     */
    public abstract ClientRequest type(MediaType type);

    /**
     * Set the media type.
     *
     * @param type the media type
     * @return the builder.
     */
    public abstract ClientRequest type(String type);

    /**
     * Add acceptable media types.
     *
     * @param types an array of the acceptable media types
     * @return the builder.
     */
    public abstract ClientRequest accept(MediaType... types);

    /**
     * Add acceptable media types.
     *
     * @param types an array of the acceptable media types
     * @return the builder.
     */
    public abstract ClientRequest accept(String... types);

    /**
     * Add acceptable languages.
     *
     * @param locales an array of the acceptable languages
     * @return the builder.
     */
    public abstract ClientRequest acceptLanguage(Locale... locales);

    /**
     * Add acceptable languages.
     *
     * @param locales an array of the acceptable languages
     * @return the builder.
     */
    public abstract ClientRequest acceptLanguage(String... locales);

    /**
     * Add a cookie to be set.
     *
     * @param cookie to be set.
     * @return the builder
     */
    public abstract ClientRequest cookie(Cookie cookie);

    /**
     * Add an HTTP header and value.
     *
     * @param name the HTTP header name.
     * @param value the HTTP header value.
     * @return the builder.
     */
    public abstract ClientRequest header(String name, Object value);
    
    private static final RuntimeDelegate RUNTIME_DELEGATE = RuntimeDelegate.getInstance();

    /**
     * Convert a header value, represented as a general object, to the
     * string value.
     * <p>
     * This method defers to {@link RuntimeDelegate#createHeaderDelegate(java.lang.Class)}
     * to obtain a {@link HeaderDelegate} to convert the value to a string. If
     * a {@link HeaderDelegate} is not found then the {@link Object#toString() toString()}
     * method is utilized.
     *
     * @param headerValue the header value as an object.
     * @return the string value
     */
    @SuppressWarnings("unchecked")
    protected static String getHeaderValue(final Object headerValue) {
        HeaderDelegate hp = RUNTIME_DELEGATE.createHeaderDelegate(headerValue.getClass());

        return (hp != null) ? hp.toString(headerValue) : headerValue.toString();
    }

    // Invocation methods
    public abstract ClientResponse invoke() throws HttpInvocationException;

    public abstract <T> T invoke(Class<T> responseType) throws HttpInvocationException;

    public abstract <T> T invoke(GenericType<T> responseType) throws HttpInvocationException;

    public abstract Future<ClientResponse> start();

    public abstract <T> Future<T> start(Class<T> responseType);

    public abstract <T> Future<T> start(GenericType<T> responseType);

    public abstract <T> Future<T> start(InvocationCallback<T> callback);
}
