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
package javax.ws.rs.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.WebApplicationException;

/**
 * A mutable HTTP request.
 *
 * @param <T> 
 * @author Marek Potociar
 * @since 2.0
 */
public interface HttpRequest<T extends HttpRequest> extends HttpHeaders, Cloneable {

    /**
     * TODO javadoc.
     *
     * @param <T> HTTP request type specialization.
     * @since 2.0
     */
    public static interface Builder<T extends HttpRequest> {

        T get();

        T put();

        T post();

        T delete();

        T head();

        T options();

        T trace();

        /**
         * Configures a HTTP request with an arbitrary HTTP method name.
         * <p />
         * The method name parameter can be any arbitrary, non-empty string, containing
         * but NOT limited to the command verbs of HTTP, WebDAV and other protocols.
         * An implementation MUST NOT expect the method to be part of any particular set
         * of methods. Any provided method name MUST be forwarded to the resource without
         * any limitations.
         *
         * @param name HTTP method name to be used for the request.
         * @return configured HTTP request or its more specialized version.
         * @see javax.ws.rs.client.Invocation
         */
        T method(String name);
    }

    // Getters
    /**
     * Get the base URI of the application. URIs of root resource classes
     * are all relative to this base URI.
     *
     * @return the base URI of the application.
     */
    URI getBaseUri();

    /**
     * Get the base URI of the application in the form of a {@link UriBuilder}.
     *
     * @return a {@code UriBuilder} initialized with the base URI of the application.
     */
    UriBuilder getBaseUriBuilder();

    /**
     * Get the absolute request URI. This includes query parameters and
     * any supplied fragment.
     *
     * @return the absolute request URI.
     */
    URI getUri();

    /**
     * Get the absolute request URI in the form of a {@link UriBuilder}.
     *
     * @return a {@code UriBuilder} initialized with the absolute request URI.
     */
    UriBuilder getUriAsBuilder();

    /**
     * Get the absolute path of the request. This includes everything preceding
     * the path (host, port etc) but excludes query parameters and fragment.
     * <p/>
     * This is a shortcut for
     * {@code uriInfo.getBase().resolve(uriInfo.getPath()).}
     *
     * @return the absolute path of the request.
     */
    URI getAbsolutePath();

    /**
     * Get the absolute path of the request in the form of a {@link UriBuilder}.
     * This includes everything preceding the path (host, port etc) but excludes
     * query parameters and fragment.
     *
     * @return a {@code UriBuilder} initialized with the absolute path of the request.
     */
    UriBuilder getAbsolutePathBuilder();

    /**
     * Get the path of the current request relative to the base URI as
     * a string. All sequences of escaped octets are decoded, equivalent to
     * {@code getPath(true)}.
     * 
     * @return the relative URI path.
     */
    String getPath();

    /**
     * Get the path of the current request relative to the base URI as
     * a string.
     *
     * @param decode controls whether sequences of escaped octets are decoded
     * ({@code true}) or not ({@code false}).
     * @return the relative URI path.
     */
    String getPath(boolean decode);

    /**
     * Get the path of the current request relative to the base URI as a list
     * of {@link PathSegment}. This method is useful when the path needs to be
     * parsed, particularly when matrix parameters may be present in the path.
     * All sequences of escaped octets in path segments and matrix parameter names
     * and values are decoded, equivalent to {@code getPathSegments(true)}.
     *
     * @return an unmodifiable list of {@link PathSegment}. The matrix parameter
     *     map of each path segment is also unmodifiable.
     * @see PathSegment
     * @see <a href="http://www.w3.org/DesignIssues/MatrixURIs.html">Matrix URIs</a>
     */
    List<PathSegment> getPathSegments();

    /**
     * Get the path of the current request relative to the base URI as a list
     * of {@link PathSegment}. This method is useful when the path needs to be
     * parsed, particularly when matrix parameters may be present in the path.
     *
     * @param decode controls whether sequences of escaped octets in path segments
     *     and matrix parameter names and values are decoded ({@code true})
     *     or not ({@code false}).
     * @return an unmodifiable list of {@link PathSegment}. The matrix parameter
     *     map of each path segment is also unmodifiable.
     * @see PathSegment
     * @see <a href="http://www.w3.org/DesignIssues/MatrixURIs.html">Matrix URIs</a>
     */
    List<PathSegment> getPathSegments(boolean decode);

    /**
     * Get the URI query parameters of the current request. All sequences of
     * escaped octets in parameter names and values are decoded,
     * equivalent to {@code getQueryParameters(true)}.
     * 
     * @return an unmodifiable map of query parameter names and values.
     */
    MultivaluedMap<String, String> getQueryParameters();

    /**
     * Get the URI query parameters of the current request.
     *
     * @param decode controls whether sequences of escaped octets in parameter
     * names and values are decoded ({@code true}) or not ({@code false}).
     * @return an unmodifiable map of query parameter names and values.
     */
    MultivaluedMap<String, String> getQueryParameters(boolean decode);

    /**
     * Get a HTTP header value.
     *
     * @param name the HTTP header.
     * @return the HTTP header value. If the HTTP header is not present then
     * {@code null} is returned. If the HTTP header is present but has no value
     * then the empty string is returned. If the HTTP header is present more than
     * once then the values of joined together and separated by a ',' character.
     */
    String getHeaderValue(String name);

    /**
     * Get the cookie name value map.
     *
     * @return the cookie name value map.
     */
    MultivaluedMap<String, String> getCookieNameValueMap();

    /**
     * Get the externally attached entity annotations.
     * <p />
     * This method returns the annotations that have been externally attached 
     * to the request entity using one of the {@code entity(...)} methods.
     * 
     * @return externally attached entity annotations.
     * 
     * @see #attach(java.lang.annotation.Annotation[])
     */
    List<Annotation> getAttachedEntityAnnotations();

    /**
     * Get the request entity, returns {@code null} if the request does not
     * contain an entity body.
     * 
     * @return the request entity or {@code null}.
     */
    Object getEntity();

    /**
     * Get the request entity, returns {@code null} if the request does not
     * contain an entity body.
     * 
     * @param <T> entity type.
     * @param type the type of entity.
     * @return the request entity or {@code null}.
     * @throws WebApplicationException if the content of the request
     *     cannot be mapped to an entity of the requested type.
     */
     <T> T getEntity(Class<T> type) throws WebApplicationException;

    /**
     * Get the request entity, returns {@code null} if the request does not
     * contain an entity body.
     * 
     * TODO do we need to pass annotations here? Do we need this method?
     * 
     * @param <T> entity type.
     * @param type the type of entity.
     * @param genericType type the generic type of entity, it is the responsibility
     *     of the callee to ensure that the type and generic type are consistent
     *     otherwise the behavior of this method is undefined.
     * @param annotations the annotations associated with the type.
     * @return the request entity or {@code null}.
     * @throws WebApplicationException if the content of the request cannot be
     *     mapped to an entity of the requested type
     */
     <T> T getEntity(Class<T> type, Type genericType, Annotation[] annotations) throws WebApplicationException;

    /**
     * Get the request method, e.g. GET, POST, etc.
     *
     * @return the request method.
     * @see javax.ws.rs.HttpMethod
     */
    String getMethod();

    @Override
    @Deprecated
    public List<String> getRequestHeader(String name);

    @Override
    @Deprecated
    public MultivaluedMap<String, String> getRequestHeaders();

    // URI builder methods
    T pathParam(String name, Object value) throws IllegalArgumentException;

    T pathParams(MultivaluedMap<String, Object> parameters) throws IllegalArgumentException;

    T formParam(String name, Object value) throws IllegalArgumentException;

    T formParams(MultivaluedMap<String, Object> parameters) throws IllegalArgumentException;

    T matrixParam(String name, Object... values) throws IllegalArgumentException;

    T queryParam(String name, Object value) throws IllegalArgumentException;

    T queryParams(MultivaluedMap<String, Object> parameters) throws IllegalArgumentException;

    T redirect(String uri);

    T redirect(URI uri);

    T redirect(UriBuilder uri);

    // Message modifiers    
    /**
     * Add acceptable media types.
     *
     * @param types an array of the acceptable media types
     * @return updated request instance.
     */
    T accept(MediaType... types);

    /**
     * Add acceptable media types.
     *
     * @param types an array of the acceptable media types
     * @return updated request instance.
     */
    T accept(String... types);

    /**
     * Add acceptable languages.
     *
     * @param locales an array of the acceptable languages
     * @return updated request instance.
     */
    T acceptLanguage(Locale... locales);

    /**
     * Add acceptable languages.
     *
     * @param locales an array of the acceptable languages
     * @return updated request instance.
     */
    T acceptLanguage(String... locales);

    /**
     * Add a cookie to be set.
     *
     * @param cookie to be set.
     * @return updated request instance.
     */
    T cookie(Cookie cookie);

    /**
     * Set the request entity.
     * <p />
     * Any Java type instance for a request entity, that is supported by the client
     * configuration of the client, can be passed. If generic information is
     * required then an instance of {@link javax.ws.rs.core.GenericEntity} may
     * be used.
     * <p />
     * A specific entity media type can be set using one of the {@code type(...)}
     * methods. If required (e.g. for validation purposes), external annotations 
     * can be {@link #attach(java.lang.annotation.Annotation[]) attached} to the 
     * entity too.
     *
     * @param entity the request entity.
     * @return updated request instance.
     *
     * @see #type(javax.ws.rs.core.MediaType)
     * @see #type(java.lang.String) 
     * @see #attach(java.lang.annotation.Annotation[])
     */
    T entity(Object entity);

    /**
     * Attach external annotations to the request entity.
     * 
     * @param annotations annotations to be externally attached to the entity.
     * @return updated request instance.
     * 
     * @see #entity(java.lang.Object)
     */
    T attach(Annotation... annotations);

    /**
     * Add an HTTP header and value.
     *
     * @param name the HTTP header name.
     * @param value the HTTP header value.
     * @return updated request instance.
     */
    T header(String name, Object value);

    /**
     * Modify the HTTP method of the request.
     * <p />
     * The method name parameter can be any arbitrary, non-empty string, containing
     * but NOT limited to the command verbs of HTTP, WebDAV and other protocols.
     * An implementation MUST NOT expect the method to be part of any particular set
     * of methods. Any provided method name MUST be forwarded to the resource without
     * any limitations.
     *
     * @param httpMethod new method to be set on the request.
     * @return updated request instance.
     */
    T method(String httpMethod);

    /**
     * Set the request entity media type.
     *
     * @param type the media type.
     * @return updated request instance.
     *
     * @see #entity(java.lang.Object)
     */
    T type(MediaType type);

    /**
     * Set the request entity media type.
     *
     * @param type the media type.
     * @return updated request instance.
     */
    T type(String type);
}
