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

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.client.InvocationException;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

/**
 * Defines a client side runtime contract for HTTP response.
 *
 * TODO consider merging with core.Response
 *
 * @author Marek Potociar
 * @since 2.0
 */
public interface HttpResponse {
    /**
     * Get the map of response properties.
     * <p>
     * A response property is an application-defined property that may be
     * added by the user, a filter, or the handler that is managing the
     * connection.
     *
     * @return the map of response properties.
     */
    Map<String, Object> getProperties();

    /**
     * Get the allowed HTTP methods from the Allow HTTP header.
     * <p />
     * Note that the Allow HTTP header will be returned by a response to an OPTIONS
     * request.
     *
     * @return the allowed HTTP methods, all methods will returned as upper case
     *     strings.
     */
    Set<String> getAllow();

    /**
     * Get the response status represented as a response {@link Status} enumeration
     * value.
     *
     * @return the status type instance, or {@code null} if the underlying status
     * code was set using the method {@link #setStatusCode(int)} and there is no
     * mapping between the integer value and the
     * {@link javax.ws.rs.core.Response.Status response status enumeration} value.
     */
    Status getStatus();

    /**
     * Get the set of cookies.
     *
     * @return the cookies.
     */
    Set<NewCookie> getCookies();

    /**
     * Get the externally attached entity annotations.
     * <p />
     * This method returns the annotations that have been externally attached 
     * to the message entity using one of the {@code entity(...)} methods.
     * 
     * @return an unmodifiable list of externally attached entity annotations.
     * 
     * @see #attach(java.lang.annotation.Annotation[])
     */
    List<Annotation> getAttachedAnnotations();

    /**
     * Get the entity of the response as a generic {@link Object} instance.
     * <p>
     * The entity input stream is closed prior to returning from this method.
     *
     * @return a response entity instance as a generic Java object.
     *
     * @throws InvocationException if there is an error processing the response
     *     or if the response status is 204 (No Content).
     */
    Object getEntity() throws InvocationException;

    /**
     * Get the entity of the response.
     * <p>
     * If the entity is not an instance of Closeable then the entity input stream
     * is closed prior to returning from this method.
     *
     * @param <T> the type of the response entity.
     * @param entityType the type of the entity.
     * @return a response entity instance of the specified type.
     *
     * @throws InvocationException if there is an error processing the response
     *     or if the response status is 204 (No Content).
     */
    <T> T getEntity(Class<T> entityType) throws InvocationException;

    /**
     * Get the entity of the response represented by a generic type.
     * <p>
     * If the entity is not an instance of Closeable then the entity input stream
     * is closed prior to returning from this method.
     *
     * @param <T> the type of the response entity.
     * @param entityType the generic type of the entity.
     * @return a response entity instance of the specified generic type.
     *
     * @throws InvocationException if there is an error processing the response
     *     or if the response status is 204 (No Content).
     */
    <T> T getEntity(GenericType<T> entityType) throws InvocationException;

    /**
     * Get the response input stream.
     *
     * @return the input stream of the response.
     */
    InputStream getEntityInputStream();

    /**
     * Get the entity tag.
     *
     * @return the entity tag, otherwise {@code null} if not present.
     */
    EntityTag getEntityTag();

    /**
     * Get the values of a single HTTP message header. The returned List is read-only.
     * This is a convenience shortcut for {@code getHeaders().get(name)}.
     *
     * @param name the header name, case insensitive.
     * @return a read-only list of header values.
     * @throws java.lang.IllegalStateException if called outside of the message
     *     processing scope.
     */
    public List<String> getHeader(String name);
    
    /**
     * Get the values of HTTP message headers. The returned Map is case-insensitive
     * wrt. keys and is read-only.
     *
     * @return a read-only map of header names and values.
     * @throws java.lang.IllegalStateException if called outside of the message
     *     processing scope.
     */
    MultivaluedMap<String, Object> getHeaders();

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
    

    // TODO add support for link headers

    /**
     * Get the language.
     *
     * @return the language, otherwise {@code null} if not present.
     */
    String getLanguage();

    /**
     * Get the last modified date.
     *
     * @return the last modified date, otherwise {@code null} if not present.
     */
    Date getLastModified();

    /**
     * Get Content-Length value.
     *
     * @return Content-Length as integer if present and valid number. In other
     * cases returns -1.
     */
    int getLength();

    /**
     * Get the location.
     *
     * @return the location URI, otherwise {@code null} if not present.
     */
    URI getLocation();

    /**
     * Get response date (server side).
     *
     * @return the server side response date, otherwise {@code null} if not present.
     */
    Date getDate();

    /**
     * Get the response status code.
     *
     * @return the response status code.
     *
     */
    int getStatusCode();

    /**
     * Get the media type of the response.
     *
     * @return the response media type.
     */
    MediaType getType();

    /**
     * Check if there is an entity available in the response.
     *
     * @return {@code true} if there is an entity present in the response.
     */
    boolean hasEntity();

    /**
     * Buffer the entity.
     * <p>
     * All the bytes of the original entity input stream will be read and stored
     * in memory. The original entity input stream will then be closed.
     *
     * @throws InvocationException if there is an error processing the response.
     */
    void bufferEntity() throws InvocationException;

    /**
     * Close the response and all resources associated with the response.
     * As part of the operation, if open, the entity input stream is closed.
     *
     * @throws InvocationException if there is an error closing the response.
     */
    void close() throws InvocationException;

    /**
     * Set the input stream of the response.
     *
     * @param entity the input stream of the response.
     */
    void setEntityInputStream(InputStream entity);

    /**
     * Set the response status using the {@link StatusType} instance.
     *
     * @param status the response status type to be set.
     */
    void setStatusType(StatusType status);

    /**
     * Set the response status code.
     *
     * @param status the response status code to be set.
     */
    void setStatusCode(int status);
    
    /**
     * Attach external annotations to the message entity.
     * 
     * @param annotations annotations to be externally attached to the entity.
     * @return updated response instance.
     */
    HttpResponse attach(Annotation... annotations);
    
    
    // TODO: add proper setEntity method
}
