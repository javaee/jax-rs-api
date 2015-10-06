/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2015 Oracle and/or its affiliates. All rights reserved.
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
 * Server-Sent Events broadcasting facility.
 * <p>
 * TODO: more javadoc.
 *
 * @author Marek Potociar
 * @since 2.1
 */
public interface SseBroadcaster extends AutoCloseable {

    /**
     * Listener interface that can be implemented to listen to events fired by {@link SseBroadcaster} object.
     * <p>
     * To listen to events, implementation of this interface needs to register with a particular {@link SseBroadcaster} instance
     * using {@link SseBroadcaster#register(Listener)}.
     */
    interface Listener {

        /**
         * Called when exception was thrown by a given SSE event output when trying to write to it or close it.
         *
         * @param output    output instance that threw exception.
         * @param exception thrown exception.
         */
        void onException(SseEventOutput output, Exception exception);

        /**
         * Called when the SSE event output has been closed (either by client closing the connection or by calling
         * {@link SseEventOutput#close()} on the server side.
         *
         * @param output output instance that has been closed.
         */
        void onClose(SseEventOutput output);
    }

    /**
     * Register {@link SseBroadcaster.Listener} that will receive {@code SseBroadcaster} lifecycle events.
     * <p>
     * This operation is potentially slow, especially if large number of listeners get registered in the broadcaster.
     * The {@code Broadcaster} implementation is optimized to efficiently handle small amounts of
     * concurrent listener registrations and removals and large amounts of registered listener notifications.
     * </p>
     *
     * @param listener listener to be registered.
     * @return {@code true} if registered, {@code false} otherwise.
     */
    boolean register(Listener listener);

    /**
     * Register {@link SseEventOutput} to this {@code SseBroadcaster} instance.
     *
     * @param output {@link SseEventOutput} to register.
     * @return {@code true} if the instance was successfully registered, {@code false} otherwise.
     */
    boolean register(final SseEventOutput output);

    /**
     * Broadcast an SSE event to all registered {@link SseEventOutput} instances.
     *
     * @param event SSE event to be broadcast.
     */
    void broadcast(final OutboundSseEvent event);

    /**
     * Close all registered {@link SseEventOutput} instances.
     */
    @Override
    void close();
}
