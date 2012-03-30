/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
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
package javax.ws.rs.container;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.MessageProcessingException;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyWriter;

/**
 * Container request filter context.
 *
 * A mutable class that provides request-specific information for the filter,
 * such as request URI, message headers, message entity or request-scoped
 * properties. The exposed setters allow modification of the exposed request-specific
 * information.
 *
 * @author Marek Potociar (marek.potociar at oracle.com)
 * @since 2.0
 */
public interface ContainerRequestContext {

    /**
     * Get a mutable map of request-scoped properties that can be used for communication
     * between different request/response processing components.
     *
     * May be empty, but MUST never be {@code null}. In the scope of a single
     * request/response processing a same property map instance is shared by the
     * following methods:
     * <ul>
     *     <li>{@link javax.ws.rs.container.ContainerRequestContext#getProperties() }</li>
     *     <li>{@link javax.ws.rs.container.ContainerResponseContext#getProperties() }</li>
     *     <li>{@link javax.ws.rs.ext.InterceptorContext#getProperties() }</li>
     * </ul>
     *
     * A request-scoped property is an application-defined property that may be
     * added, removed or modified by any of the components (user, filter,
     * interceptor etc.) that participate in a given request/response processing
     * flow.
     * <p />
     * On the client side, this property map is initialized by calling
     * {@link javax.ws.rs.client.Configuration#setProperties(java.util.Map) } or
     * {@link javax.ws.rs.client.Configuration#setProperty(java.lang.String, java.lang.Object) }
     * on the configuration object associated with the corresponding
     * {@link javax.ws.rs.client.Invocation request invocation}.
     * <p />
     * On the server side, specifying the initial values is implementation-specific.
     * <p />
     * If there are no initial properties set, the request-scoped property map is
     * initialized to an empty map.
     *
     * @return a mutable request-scoped property map.
     */
    public Map<String, Object> getProperties();

    /**
     * Get request URI information.
     *
     * The returned object contains "live" view of the request URI information in
     * a sense that any changes made to the request URI using one of the
     * {@code setRequestUri(...)} methods will be reflected in the previously
     * returned {@link UriInfo} instance.
     *
     * @return request URI information.
     */
    public UriInfo getUriInfo();

    /**
     * Set a new request URI using the current base URI of the application to
     * resolve the application-specific request URI part.
     *
     * @param requestUri new URI of the request.
     *
     * @see #setRequestUri(java.net.URI, java.net.URI)
     */
    public void setRequestUri(URI requestUri);

    /**
     * Set a new request URI using a new base URI to resolve the application-specific
     * request URI part.
     *
     * @param baseUri base URI that will be used to resolve the application-specific
     *     part of the request URI.
     * @param requestUri new URI of the request.
     *
     * @see #setRequestUri(java.net.URI, java.net.URI)
     */
    public void setRequestUri(URI baseUri, URI requestUri);

    /**
     * Get the injectable request information.
     *
     * @return injectable request information.
     */
    public Request getRequest();

    /**
     * Get the request method.
     *
     * @return the request method.
     *
     * @see javax.ws.rs.HttpMethod
     */
    public String getMethod();

    /**
     * Set the request method.
     *
     * @param method new request method.
     *
     * @see javax.ws.rs.HttpMethod
     */
    public void setMethod(String method);

    /**
     * Get the mutable request headers multivalued map.
     *
     * @return mutable multivalued map of request headers.
     */
    public MultivaluedMap<String, String> getHeaders();

    /**
     * Get message date.
     *
     * @return the message date, otherwise {@code null} if not present.
     */
    public Date getDate();

    /**
     * Get the language of the entity.
     *
     * @return the language of the entity or {@code null} if not specified
     */
    public Locale getLanguage();

    /**
     * Get Content-Length value.
     *
     * @return Content-Length as integer if present and valid number. In other
     *     cases returns {@code -1}.
     */
    public int getLength();

    /**
     * Get the media type of the entity.
     *
     * @return the media type or {@code null} if not specified (e.g. there's no
     *     request entity).
     */
    public MediaType getMediaType();

    /**
     * Get a list of media types that are acceptable for the response.
     *
     * @return a read-only list of requested response media types sorted according
     *     to their q-value, with highest preference first.
     */
    public List<MediaType> getAcceptableMediaTypes();

    /**
     * Get a list of languages that are acceptable for the response.
     *
     * @return a read-only list of acceptable languages sorted according
     *     to their q-value, with highest preference first.
     */
    public List<Locale> getAcceptableLanguages();

    /**
     * Get any cookies that accompanied the request.
     *
     * @return a read-only map of cookie name (String) to {@link Cookie}.
     */
    public Map<String, Cookie> getCookies();

    /**
     * Check if there is a non-empty entity available in the request message.
     *
     * The method returns {@code true} if the entity is present, returns
     * {@code false} otherwise.
     * <p/>
     * Note that in case the message contained an entity, but it was already
     * consumed via one of the {@code writeEntity(...)} methods without being
     * {@link #bufferEntity() buffered} or replaced using one of the
     * {@code writeEntity(...)} methods, the {@code hasEntity()} returns {@code false}.
     *
     * @return {@code true} if there is an entity present in the message,
     *     {@code false} otherwise.
     */
    public boolean hasEntity();

    /**
     * Write a new message entity. Effectively replaces the existing entity with
     * a new one.
     *
     * The method finds and invokes the proper {@link MessageBodyWriter} to
     * serialize the entity into an input stream from which it can be read again.
     *
     * @param <T> entity type.
     * @param entity the instance to write.
     * @param type the class of object that is to be written.
     * @param mediaType the entity media type.
     * @param annotations an array of the annotations attached to the entity.
     * @throws MessageProcessingException if there was en error writing
     *     the entity.
     */
    public <T> void writeEntity(
            final T entity,
            final Class<T> type,
            final MediaType mediaType,
            final Annotation annotations[]) throws MessageProcessingException;

    /**
     * Write a new message entity. Effectively replaces the existing entity with
     * a new one.
     *
     * The method finds and invokes the proper {@link MessageBodyWriter} to
     * serialize the entity into an input stream from which it can be read again.
     *
     * @param <T> entity type.
     * @param entity the instance to write.
     * @param genericType the generic type information of object that is to be
     *     written.
     * @param mediaType the entity media type.
     * @param annotations an array of the annotations attached to the entity.
     * @throws MessageProcessingException if there was en error writing
     *     the entity.
     */
    public <T> void writeEntity(
            final GenericType<T> genericType,
            final Annotation annotations[],
            final MediaType mediaType,
            final T entity);

    /**
     * Read the message entity as an instance of specified Java type using
     * a {@link javax.ws.rs.ext.MessageBodyReader} that supports mapping the
     * message entity stream onto the requested type.
     *
     * Returns {@code null} if the message does not {@link #hasEntity() contain}
     * an entity body. Unless the supplied entity type is an
     * {@link java.io.InputStream input stream}, this method automatically
     * {@link InputStream#close() closes} the consumed entity stream.
     * In case the entity was {@link #bufferEntity() bufferd} previously,
     * the buffered input stream is reset so that the entity is readable again.
     *
     * @param <T> entity instance Java type.
     * @param type the requested type of the entity.
     * @return the message entity or {@code null} if message does not contain an
     *     entity body.
     * @throws MessageProcessingException if the content of the message cannot be
     *     mapped to an entity of the requested type.
     * @see javax.ws.rs.ext.MessageBodyWriter
     * @see javax.ws.rs.ext.MessageBodyReader
     */
    public <T> T readEntity(final Class<T> type) throws MessageProcessingException;

    /**
     * Read the message entity as an instance of specified Java type using
     * a {@link javax.ws.rs.ext.MessageBodyReader} that supports mapping the
     * message entity stream onto the requested type.
     *
     * Returns {@code null} if the message does not {@link #hasEntity() contain}
     * an entity body. Unless the supplied entity type is an
     * {@link java.io.InputStream input stream}, this method automatically
     * {@link InputStream#close() closes} the consumed entity stream.
     * In case the entity was {@link #bufferEntity() bufferd} previously,
     * the buffered input stream is reset so that the entity is readable again.
     *
     * @param <T> entity instance Java type.
     * @param type the requested generic type of the entity.
     * @return the message entity or {@code null} if message does not contain an
     *     entity body.
     * @throws MessageProcessingException if the content of the message cannot be
     *     mapped to an entity of the requested type.
     * @see javax.ws.rs.ext.MessageBodyWriter
     * @see javax.ws.rs.ext.MessageBodyReader
     */
    public <T> T readEntity(final GenericType<T> type) throws MessageProcessingException;

    /**
     * Read the message entity as an instance of specified Java type using
     * a {@link javax.ws.rs.ext.MessageBodyReader} that supports mapping the
     * message entity stream onto the requested type.
     *
     * Returns {@code null} if the message does not {@link #hasEntity() contain}
     * an entity body. Unless the supplied entity type is an
     * {@link java.io.InputStream input stream}, this method automatically
     * {@link InputStream#close() closes} the consumed entity stream.
     * In case the entity was {@link #bufferEntity() bufferd} previously,
     * the buffered input stream is reset so that the entity is readable again.
     *
     * @param <T> entity instance Java type.
     * @param type the requested type of the entity.
     * @param annotations annotations attached to the entity.
     * @return the message entity or {@code null} if message does not contain an
     *     entity body.
     * @throws MessageProcessingException if the content of the message cannot be
     *     mapped to an entity of the requested type.
     * @see javax.ws.rs.ext.MessageBodyWriter
     * @see javax.ws.rs.ext.MessageBodyReader
     */
    public <T> T readEntity(final Class<T> type, final Annotation[] annotations) throws MessageProcessingException;

    /**
     * Read the message entity as an instance of specified Java type using
     * a {@link javax.ws.rs.ext.MessageBodyReader} that supports mapping the
     * message entity stream onto the requested type.
     *
     * Returns {@code null} if the message does not {@link #hasEntity() contain}
     * an entity body. Unless the supplied entity type is an
     * {@link java.io.InputStream input stream}, this method automatically
     * {@link InputStream#close() closes} the consumed entity stream.
     * In case the entity was {@link #bufferEntity() bufferd} previously,
     * the buffered input stream is reset so that the entity is readable again.
     *
     * @param <T> entity instance Java type.
     * @param type the requested generic type of the entity.
     * @param annotations annotations attached to the entity.
     * @return the message entity or {@code null} if message does not contain an
     *     entity body.
     * @throws MessageProcessingException if the content of the message cannot be
     *     mapped to an entity of the requested type.
     * @see javax.ws.rs.ext.MessageBodyWriter
     * @see javax.ws.rs.ext.MessageBodyReader
     */
    public <T> T readEntity(final GenericType<T> type, final Annotation[] annotations) throws MessageProcessingException;

    /**
     * Buffer the original message entity stream.
     *
     * In case the original message entity input stream is open, all the bytes of
     * the stream are read and stored in memory. The original entity input stream
     * is automatically {@link InputStream#close() closed} afterwards.
     * <p />
     * This operation is idempotent, i.e. it can be invoked multiple times with
     * the same effect which also means that calling the {@code bufferEntity()}
     * method on an already buffered (and thus closed) message instance is legal
     * and has no further effect.
     * <p />
     * Note that if the message entity has been replaced using one of the
     * {@code writeEntity(...)} methods, such entity input stream is buffered
     * already and a subsequent call to {@code bufferEntity()} has no effect.
     *
     * @throws MessageProcessingException if there is an error buffering the
     *     message entity.
     * @since 2.0
     */
    public void bufferEntity() throws MessageProcessingException;

    /**
     * Get the entity input stream.
     *
     * @return entity input stream.
     */
    public InputStream getEntityStream();

    /**
     * Set a new entity input stream.
     *
     * @param input new entity input stream.
     */
    public void setEntityStream(InputStream input);

    /**
     * Get the injectable request security context.
     *
     * @return request security context.
     */
    public SecurityContext getSecurityContext();

    /**
     * Set a new request security context.
     *
     * @param context new request security context.
     */
    public void setSecurityContext(SecurityContext context);

    /**
     * Abort the filter chain with a response.
     *
     * This method breaks the filter chain processing and returns the provided
     * response back to the client. The provided response goes through the
     * chain of applicable response filters.
     *
     * @param response response to be sent back to the client.
     */
    public void abortWith(Response response);
}
