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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * general-header =
 *      * Cache-Control            ; Section 14.9
 *        Connection               ; Section 14.10
 *        a? Date                  ; Section 14.18
 *        Pragma                   ; Section 14.32
 *        Trailer                  ; Section 14.40
 *        Transfer-Encoding        ; Section 14.41
 *        Upgrade                  ; Section 14.42
 *        Via                      ; Section 14.45
 *        Warning                  ; Section 14.46
 *
 * entity-header  =                                    Req     Res
 *      *  Allow                    ; Section 14.7      +       +
 *      *  Content-Encoding         ; Section 14.11     +       +
 *      *  Content-Language         ; Section 14.12     +       +
 *      a  Content-Length           ; Section 14.13     +       +
 *      -  Content-Location         ; Section 14.14     ?       +
 *         Content-MD5              ; Section 14.15     +       +
 *         Content-Range            ; Section 14.16     -       +
 *      *  Content-Type             ; Section 14.17     +       +
 *      -  Expires                  ; Section 14.21     -       +
 *      -  Last-Modified            ; Section 14.29     -       +
 *
 * response-header =
 *          Accept-Ranges           ; Section 14.5
 *          Age                     ; Section 14.6
 *       *  ETag                    ; Section 14.19
 *       *  Location                ; Section 14.30
 *          Proxy-Authenticate      ; Section 14.33
 *          Retry-After             ; Section 14.37
 *          Server                  ; Section 14.38
 *       *  Vary                    ; Section 14.44
 *          WWW-Authenticate        ; Section 14.47
 */
/**
 * An injectable interface that provides access to HTTP response header information.
 * This interface can only be injected as part of the response processing scope.
 * Alternatively, the interface can be retrieved from a {@link Response} instance
 * via its {@link Response#getHeaders() getHeaders()} method.
 *
 * @author Marek Potociar
 * @since 2.0
 */
public interface ResponseHeaders {

    /**
     * Response headers builder.
     *
     * @param <T> actual response headers builder type.
     * @author Marek Potociar
     * @since 2.0
     */
    public static interface Builder<T extends Builder> {

        // General headers
        /**
         * Set the list of allowed methods for the resource. Any duplicate method
         * names will be truncated to a single entry.
         *
         * @param methods the methods to be listed as allowed for the resource,
         *     if {@code null} any existing allowed method list will be removed.
         * @return the updated headers builder.
         */
        public T allow(String... methods);

        /**
         * Set the list of allowed methods for the resource.
         *
         * @param methods the methods to be listed as allowed for the resource,
         *     if {@code null} any existing allowed method list will be removed.
         * @return the updated headers builder.
         */
        public T allow(Set<String> methods);

        /**
         * Set the cache control data of the message.
         *
         * @param cacheControl the cache control directives, if {@code null}
         *     any existing cache control directives will be removed.
         * @return the updated headers builder.
         */
        public T cacheControl(CacheControl cacheControl);

        /**
         * Set the message entity content encoding.
         *
         * @param encoding the content encoding of the message entity,
         *     if {@code null} any existing value for content encoding will be
         *     removed.
         * @return the updated headers builder.
         */
        public T encoding(String encoding);

        /**
         * Add an arbitrary header.
         *
         * @param name the name of the header
         * @param value the value of the header, the header will be serialized
         *     using a {@link javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate} if
         *     one is available via {@link javax.ws.rs.ext.RuntimeDelegate#createHeaderDelegate(java.lang.Class)}
         *     for the class of {@code value} or using its {@code toString} method
         *     if a header delegate is not available. If {@code value} is {@code null}
         *     then all current headers of the same name will be removed.
         * @return the updated header builder.
         */
        public T header(String name, Object value);

        /**
         * Replaces all existing headers with the newly supplied headers.
         *
         * @param headers new headers to be set, if {@code null} all existing
         *     headers will be removed.
         * @return the updated headers builder.
         */
        public T replaceAll(ResponseHeaders headers);

        /**
         * Set the message entity language.
         *
         * @param language the language of the message entity, if {@code null} any
         *     existing value for language will be removed.
         * @return the updated headers builder.
         */
        public T language(String language);

        /**
         * Set the message entity language.
         *
         * @param language the language of the message entity, if {@code null} any
         *     existing value for type will be removed.
         * @return the updated headers builder.
         */
        public T language(Locale language);

        /**
         * Set the message entity media type.
         *
         * @param type the media type of the message entity. If {@code null}, any
         *     existing value for type will be removed
         * @return the updated header builder.
         */
        public T type(MediaType type);

        /**
         * Set the message entity media type.
         *
         * @param type the media type of the message entity. If {@code null}, any
         *     existing value for type will be removed
         * @return the updated header builder.
         */
        public T type(String type);

        /**
         * Set message entity representation metadata.
         * <p/>
         * Equivalent to setting the values of content type, content language,
         * and content encoding separately using the values of the variant properties.
         *
         * @param variant metadata of the message entity, a {@code null} value is
         *     equivalent to a variant with all {@code null} properties.
         * @return the updated header builder.
         *
         * @see #encoding(java.lang.String)
         * @see #language(java.util.Locale)
         * @see #type(javax.ws.rs.core.MediaType)
         */
        public T variant(Variant variant);

        // Response-specific headers
        /**
         * Set the content location.
         *
         * @param location the content location. Relative or absolute URIs
         *     may be used for the value of content location. If {@code null} any
         *     existing value for content location will be removed.
         * @return the updated response headers builder.
         */
        public T contentLocation(URI location);

        /**
         * Add cookies to the response message.
         *
         * @param cookies new cookies that will accompany the response. A {@code null}
         *     value will remove all cookies, including those added via the
         *     {@link #header(java.lang.String, java.lang.Object)} method.
         * @return the updated response headers builder.
         */
        public T cookie(NewCookie... cookies);

        /**
         * Set the response expiration date.
         *
         * @param expires the expiration date, if {@code null} removes any existing
         *     expires value.
         * @return the updated response headers builder.
         */
        public T expires(Date expires);

        /**
         * Set the response entity last modification date.
         *
         * @param lastModified the last modified date, if {@code null} any existing
         *     last modified value will be removed.
         * @return the updated response headers builder.
         */
        public T lastModified(Date lastModified);

        /**
         * Set the location.
         *
         * @param location the location. If a relative URI is supplied it will be
         *     converted into an absolute URI by resolving it relative to the
         *     base URI of the application (see {@link UriInfo#getBaseUri}).
         *     If {@code null} any existing value for location will be removed.
         * @return the updated response header builder.
         */
        public T location(URI location);

        /**
         * Set a response entity tag.
         *
         * @param tag the entity tag, if {@code null} any existing entity tag
         *     value will be removed.
         * @return the updated response headers builder.
         */
        public T tag(EntityTag tag);

        /**
         * Set a strong response entity tag.
         * <p/>
         * This is a shortcut for <code>tag(new EntityTag(<i>value</i>))</code>.
         *
         * @param tag the string content of a strong entity tag. The JAX-RS
         *     runtime will quote the supplied value when creating the header.
         *     If {@code null} any existing entity tag value will be removed.
         * @return the updated response headers builder.
         */
        public T tag(String tag);

        /**
         * Add a Vary header that lists the available variants.
         *
         * @param variants a list of available representation variants, a {@code null}
         *     value will remove an existing value for Vary header.
         * @return the updated response header builder.
         */
        public T variants(Variant... variants);

        /**
         * Add a Vary header that lists the available variants.
         *
         * @param variants a list of available representation variants, a {@code null}
         *     value will remove an existing value for Vary header.
         * @return the updated response header builder.
         */
        public T variants(List<Variant> variants);

        /**
         * Add one or more link headers.
         *
         * @param links links to be added to the message as headers, a {@code null}
         *     value will remove any existing Link headers.
         * @return the updated response header builder.
         * @since 2.0
         */
        public T links(Link... links);

        /**
         * Add a link header.
         *
         * @param uri TODO.
         * @param rel TODO.
         * @return the updated response header builder.
         * @since 2.0
         */
        public T link(URI uri, String rel);

        /**
         * Add a link header.
         *
         * @param uri TODO.
         * @param rel TODO.
         * @return the updated response header builder.
         * @since 2.0
         */
        public T link(String uri, String rel);
    }

    // General header getters
    /**
     * Get the allowed HTTP methods from the Allow HTTP header.
     *
     * @return the allowed HTTP methods, all methods will returned as upper case
     *     strings.
     */
    public Set<String> getAllowedMethods();

    /**
     * Get message date.
     *
     * @return the message date, otherwise {@code null} if not present.
     */
    public Date getDate();

    /**
     * Get a HTTP header as a single string value.
     * <p/>
     * Each single header value is converted to String using a
     * {@link javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate} if one is available
     * via {@link javax.ws.rs.ext.RuntimeDelegate#createHeaderDelegate(java.lang.Class)}
     * for the header value class or using its {@code toString} method  if a header
     * delegate is not available.
     *
     * @param name the HTTP header.
     * @return the HTTP header value. If the HTTP header is not present then
     *     {@code null} is returned. If the HTTP header is present but has no
     *     value then the empty string is returned. If the HTTP header is present
     *     more than once then the values of joined together and separated by a ','
     *     character.
     * @see #asMap()
     * @see #getHeaderValues(java.lang.String)
     */
    public String getHeader(String name);

    /**
     * Get the map of HTTP message header names to their respective values.
     * The returned map is case-insensitive wrt. keys and is read-only.
     * <p/>
     * Each single header value is converted to String using a
     * {@link javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate} if one is available
     * via {@link javax.ws.rs.ext.RuntimeDelegate#createHeaderDelegate(java.lang.Class)}
     * for the header value class or using its {@code toString} method  if a header
     * delegate is not available.
     *
     * @return a read-only map of header names and values.
     * @throws java.lang.IllegalStateException if called outside of the message
     *     processing scope.
     * @see #getHeader(java.lang.String)
     * @see #getHeaderValues(java.lang.String)
     */
    public MultivaluedMap<String, String> asMap();

    /**
     * Get the values of a single HTTP message header. The returned List is read-only.
     * This is a convenience shortcut for {@code asMap().get(name)}.
     * <p/>
     * Each single header value is converted to String using a
     * {@link javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate} if one is available
     * via {@link javax.ws.rs.ext.RuntimeDelegate#createHeaderDelegate(java.lang.Class)}
     * for the header value class or using its {@code toString} method  if a header
     * delegate is not available.
     *
     * @param name the header name, case insensitive.
     * @return a read-only list of header values.
     * @throws java.lang.IllegalStateException if called outside of the message
     *     processing scope.
     * @see #asMap()
     * @see #getHeader(java.lang.String)
     */
    public List<String> getHeaderValues(String name);

    /**
     * Get the language of the entity
     * @return the language of the entity or null if not specified
     * @throws java.lang.IllegalStateException if called outside the scope of a request
     */
    public Locale getLanguage();

    /**
     * Get Content-Length value.
     *
     * @return Content-Length as integer if present and valid number. In other
     * cases returns -1.
     */
    public int getLength();

    /**
     * Get the media type of the entity
     * @return the media type or null if there is no request entity.
     * @throws java.lang.IllegalStateException if called outside the scope of a request
     */
    public MediaType getMediaType();

    // Response-specific header getters
    /**
     * Get any new cookies set on the response message.
     *
     * @return a read-only map of cookie name (String) to Cookie.
     */
    public Map<String, NewCookie> getCookies();

    /**
     * Get the entity tag.
     *
     * @return the entity tag, otherwise {@code null} if not present.
     */
    public EntityTag getEntityTag();

    /**
     * Get the last modified date.
     *
     * @return the last modified date, otherwise {@code null} if not present.
     */
    public Date getLastModified();

    /**
     * Get the location.
     *
     * @return the location URI, otherwise {@code null} if not present.
     */
    public URI getLocation();

    public Set<Link> getLinks();

    public Link getLink(String relation);
}
