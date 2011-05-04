/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
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

import java.util.Locale;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

/**
 * An uniform interface for building and configuring HTTP request invocations.
 * <p />
 * The interface defines common set of methods for the JAX-RS client API
 * classes that provide configuration (and optionally subsequent invocation) 
 * of HTTP requests, e.g. {@link WebResource}.
 * 
 * @param <T> the type that implements {@link InvocationBuilder}.
 * @author Paul Sandoz
 * @author Marek Potociar
 * @see WebResource
 * @see AsyncWebResource
 * @see ClientRequest.Builder ClientRequest.Builder
 * 
 * @since 2.0
 */
public interface InvocationBuilder<T extends InvocationBuilder> {

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
     * Set the request entity it's media type.
     * <p>
     * Any Java type instance for a request entity, that is supported by the client
     * configuration of the client, can be passed. If generic information is
     * required then an instance of {@link javax.ws.rs.core.GenericEntity} may
     * be used.
     * 
     * @param entity the request entity
     * @param type the media type
     * @return the builder.
     */
    T entity(Object entity, MediaType type);

    /**
     * Set the request entity it's media type.
     * <p>
     * Any Java type instance for a request entity, that is supported by the client
     * configuration of the client, can be passed. If generic information is
     * required then an instance of {@link javax.ws.rs.core.GenericEntity} may
     * be used.
     * 
     * @param entity the request entity
     * @param type the media type
     * @return the builder.
     */
    T entity(Object entity, String type);
    
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
     * Add acceptable languages
     * 
     * @param locales an array of the acceptable languages
     * @return the builder.
     */
    T acceptLanguage(Locale... locales);
    
    /**
     * Add acceptable languages
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
     * Add an HTTP header and value.
     * 
     * @param name the HTTP header name.
     * @param value the HTTP header value.
     * @return the builder.
     */
    T header(String name, Object value);
}