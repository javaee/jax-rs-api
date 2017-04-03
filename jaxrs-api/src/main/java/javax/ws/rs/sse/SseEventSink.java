/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2017 Oracle and/or its affiliates. All rights reserved.
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

import java.util.concurrent.CompletionStage;

/**
 * Outbound Server-Sent Events stream.
 * <p>
 * The instance of {@link SseEventSink} can be only acquired by injection of a resource method parameter:
 * <pre>
 * &#64;GET
 * &#64;Path("eventStream")
 * &#64;Produces(MediaType.SERVER_SENT_EVENTS)
 * public void eventStream(@Context SseEventSink eventSink) {
 *     // ...
 * }
 * </pre>
 * The injected instance is then considered as a return type, so the resource method doesn't return anything,
 * similarly as in server-side async processing.
 * <p>
 * The underlying client connection is kept open and the application code
 * is able to send events. A server-side instance implementing the interface
 * corresponds exactly to a single client HTTP connection.
 * <p>
 * The injected instance is thread safe.
 *
 * @author Marek Potociar (marek.potociar at oracle.com)
 * @since 2.1
 */
public interface SseEventSink extends AutoCloseable {

    /**
     * Check if the stream has been closed already.
     * <p>
     * Please note that the client connection represented by this {@code SseServerSink} can be closed by the
     * client side when a client decides to close connection and disconnect from the server.
     *
     * @return {@code true} when closed, {@code false} otherwise.
     */
    boolean isClosed();

    /**
     * Send an outbound Server-sent event to this sink.
     * <p>
     * Event will be serialized and sent to the client.
     *
     * @param event event to be written.
     * @return completion stage that completes when the event has been sent. If there is a problem during sending of
     * an event, completion stage will be completed exceptionally.
     */
    public CompletionStage<?> send(OutboundSseEvent event);

    /**
     * Close the {@link SseEventSink} instance and release all associated resources.
     * <p>
     * Subsequent calls have no effect and are ignored. Once the {@link SseEventSink} is closed,
     * invoking any method other than this one and {@link #isClosed()} would result in
     * an {@link IllegalStateException} being thrown.
     */
    @Override
    void close();
}
