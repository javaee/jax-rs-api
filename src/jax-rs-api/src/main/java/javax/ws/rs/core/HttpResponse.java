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
import java.util.Map;

import javax.ws.rs.core.Response.StatusType;

/**
 * Defines a runtime contract for HTTP response.
 *
 * TODO consider merging with core.Response
 *
 * @author Marek Potociar
 * @since 2.0
 */
public interface HttpResponse extends ResponseHeaders, ResponseHeaders.Builder<HttpResponse> {
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
        
    // TODO: add proper setEntity method

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
    <T> T getEntity(GenericType<T> entityType) throws MessageProcessingException;

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
     * @throws MessageProcessingException if there is an error processing the response.
     */
    void bufferEntity() throws MessageProcessingException;

    /**
     * Close the response and all resources associated with the response.
     * As part of the operation, if open, the entity input stream is closed.
     *
     * @throws MessageProcessingException if there is an error closing the response.
     */
    void close() throws MessageProcessingException;

    /**
     * Get the response input stream.
     *
     * @return the input stream of the response.
     */
    InputStream getEntityInputStream();
    
    /**
     * Set the input stream of the response.
     *
     * @param entity the input stream of the response.
     */
    void setEntityInputStream(InputStream entity);
}
