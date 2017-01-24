/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2015-2017 Oracle and/or its affiliates. All rights reserved.
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
package javax.ws.rs.sse;

/**
 * Base Server Sent Event definition.
 * <p>
 * This interface provides basic properties of the Server Sent Event, namely ID, Name, and Comment.
 * It also provides access to the Reconnect delay property.
 * <p>
 * {@code SseEvent} is extended by another two interfaces, {@link InboundSseEvent} and
 * {@link OutboundSseEvent}. The main difference is in how are instances created and how the stored
 * data can be accessed (or provided).
 *
 * @author Marek Potociar
 * @since 2.1
 */
public interface SseEvent {

    /**
     * A "reconnection not set" value for the SSE reconnect delay set via SSE event {@code retry} field.
     */
    long RECONNECT_NOT_SET = -1;

    /**
     * Get event identifier.
     * <p>
     * Contains value of SSE {@code "id"} field. This field is optional. Method may return {@code null}, if the event
     * identifier is not specified.
     *
     * @return event id.
     */
    String getId();

    /**
     * Get event name.
     * <p>
     * Contains value of SSE {@code "event"} field. This field is optional. Method may return {@code null}, if the event
     * name is not specified.
     *
     * @return event name, or {@code null} if not set.
     */
    String getName();

    /**
     * Get a comment string that accompanies the event.
     * <p>
     * Contains value of the comment associated with SSE event. This field is optional. Method may return {@code null},
     * if the event comment is not specified.
     *
     * @return comment associated with the event.
     */
    String getComment();

    /**
     * Get new connection retry time in milliseconds the event receiver should wait before attempting to
     * reconnect after a connection to the SSE event source is lost.
     * <p>
     * Contains value of SSE {@code "retry"} field. This field is optional. Method returns {@link #RECONNECT_NOT_SET}
     * if no value has been set.
     *
     * @return reconnection delay in milliseconds or {@link #RECONNECT_NOT_SET} if no value has been set.
     */
    long getReconnectDelay();

    /**
     * Check if the connection retry time has been set in the event.
     *
     * @return {@code true} if new reconnection delay has been set in the event, {@code false} otherwise.
     */
    boolean isReconnectDelaySet();

}
