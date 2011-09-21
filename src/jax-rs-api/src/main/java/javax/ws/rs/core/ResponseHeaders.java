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
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.Response.Status;

/**
 * TODO javadoc.
 *
 * @author Marek Potociar
 * @since 2.0
 */
public interface ResponseHeaders extends Headers {

    /**
     * entity-header =                                      Req     Res
     *       *  Content-Location         ; Section 14.14     ?       +
     *          Content-Range            ; Section 14.16     -       +
     *       *  Expires                  ; Section 14.21     -       +
     *       *  Last-Modified            ; Section 14.29     -       +
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
     * TODO javadoc.
     *
     * @param <T> actual response headers builder type.
     * @author Marek Potociar
     * @since 2.0
     */
    public static interface Builder<T extends Builder> extends Headers.Builder<T> {

        /**
         * Set the content location.
         *
         * @param location the content location. Relative or absolute URIs
         *     may be used for the value of content location. If {@code null} any
         *     existing value for content location will be removed.
         * @return the updated response headers builder.
         */
        T contentLocation(URI location);

        /**
         * Add cookies to the response message.
         *
         * @param cookies new cookies that will accompany the response. A {@code null}
         *     value will remove all cookies, including those added via the
         *     {@link #header(java.lang.String, java.lang.Object)} method.
         * @return the updated response headers builder.
         */
        T cookie(NewCookie... cookies);

        /**
         * Set the response expiration date.
         *
         * @param expires the expiration date, if {@code null} removes any existing
         *     expires value.
         * @return the updated response headers builder.
         */
        T expires(Date expires);

        /**
         * Set the response entity last modification date.
         *
         * @param lastModified the last modified date, if {@code null} any existing
         *     last modified value will be removed.
         * @return the updated response headers builder.
         */
        T lastModified(Date lastModified);

        /**
         * Set the location.
         *
         * @param location the location. If a relative URI is supplied it will be
         *     converted into an absolute URI by resolving it relative to the
         *     base URI of the application (see {@link UriInfo#getBaseUri}).
         *     If {@code null} any existing value for location will be removed.
         * @return the updated response header builder.
         */
        T location(URI location);

        /**
         * Set a response entity tag.
         *
         * @param tag the entity tag, if {@code null} any existing entity tag 
         *     value will be removed.
         * @return the updated response headers builder.
         */
        T tag(EntityTag tag);

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
        T tag(String tag);

        /**
         * Add a Vary header that lists the available variants.
         *
         * @param variants a list of available representation variants, a {@code null}
         *     value will remove an existing value for Vary header.
         * @return the updated response header builder.
         */
        T variants(Variant... variants);

        /**
         * Add a Vary header that lists the available variants.
         *
         * @param variants a list of available representation variants, a {@code null}
         *     value will remove an existing value for Vary header.
         * @return the updated response header builder.
         */
        T variants(List<Variant> variants);

        /**
         * Add one or more link headers.
         * 
         * @param links links to be added to the message as headers, a {@code null}
         *     value will remove any existing Link headers.
         * @return the updated response header builder.
         * @since 2.0
         */
        T links(Link... links);
        
        /**
         * Add a link header.
         *
         * @param uri TODO.
         * @param rel TODO.
         * @return the updated response header builder.
         * @since 2.0
         */
        T link(URI uri, String rel);

        /**
         * Add a link header.
         *
         * @param uri TODO.
         * @param rel TODO.
         * @return the updated response header builder.
         * @since 2.0
         */
        T link(String uri, String rel);
        
    }

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
    EntityTag getEntityTag();

    // TODO add support for link headers
    /**
     * Get the last modified date.
     *
     * @return the last modified date, otherwise {@code null} if not present.
     */
    Date getLastModified();

    /**
     * Get the location.
     *
     * @return the location URI, otherwise {@code null} if not present.
     */
    URI getLocation();

    Set<Link> getLinks();

    Link getLink(String relation);
}
