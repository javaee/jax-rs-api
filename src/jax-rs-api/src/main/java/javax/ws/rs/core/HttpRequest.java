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

import java.net.URI;
import java.util.List;

/**
 * Defines a runtime contract for a HTTP request.
 *
 * @author Marek Potociar
 * @since 2.0
 */
public interface HttpRequest extends RequestHeaders, RequestHeaders.Builder<HttpRequest>, Cloneable {

    // Getters
    /**
     * Get the request method, e.g. GET, POST, etc.
     *
     * @return the request method.
     * @see javax.ws.rs.HttpMethod
     */
    String getMethod();

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
    UriBuilder getUriBuilder();

    /**
     * Get the absolute path of the request. This includes everything preceding
     * the path (host, port etc) but excludes query parameters and fragment.
     * <p/>
     *
     * @return the absolute path of the request.
     */
    URI getPath();

    /**
     * Get the absolute path of the request in the form of a {@link UriBuilder}.
     * This includes everything preceding the path (host, port etc) but excludes
     * query parameters and fragment.
     *
     * @return a {@code UriBuilder} initialized with the absolute path of the request.
     */
    UriBuilder getPathBuilder();

    /**
     * Get the absolute path of the request in the form of a {@link String}.
     *
     * @param decode controls whether sequences of escaped octets are decoded
     * ({@code true}) or not ({@code false}).
     * @return the {@link String} containing the absolute path of the request.
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
     * Get the message entity, returns {@code null} if the message does not
     * contain an entity body.
     * 
     * @return the message entity or {@code null}.
     */
    Object getEntity();

    /**
     * Get the message entity, returns {@code null} if the message does not
     * contain an entity body.
     * 
     * @param <T> entity type.
     * @param type the type of entity.
     * @return the message entity or {@code null}.
     * @throws MessageProcessingException if the content of the message
     *     cannot be mapped to an entity of the requested type.
     */
     <T> T getEntity(Class<T> type) throws MessageProcessingException;

    /**
     * Get the message entity, returns {@code null} if the message does not
     * contain an entity body.
     * 
     * @param <T> entity type.
     * @param entityType the generic type of the entity.
     * @return the message entity or {@code null}.
     * @throws MessageProcessingException if the content of the message
     *     cannot be mapped to an entity of the requested type.
     */
     <T> T getEntity(TypeLiteral<T> entityType) throws MessageProcessingException;

    /**
     * Check if there is an entity available in the request.
     *
     * @return {@code true} if there is an entity present in the request.
     */
    boolean hasEntity();

    // Modifiers
    HttpRequest redirect(String uri);

    HttpRequest redirect(URI uri);

    HttpRequest redirect(UriBuilder uri);

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
    HttpRequest method(String httpMethod);

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
     * can be {@link #entity(java.lang.Object, java.lang.annotation.Annotation[]) attached}
     * to the entity too.
     *
     * @param entity the request entity.
     * @return updated request instance.
     *
     * @see #type(javax.ws.rs.core.MediaType)
     * @see #type(java.lang.String) 
     * @see #entity(java.lang.Object, java.lang.annotation.Annotation[])
     */
    HttpRequest entity(Object entity);
}
