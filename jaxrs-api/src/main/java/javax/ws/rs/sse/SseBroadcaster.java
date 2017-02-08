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

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.ws.rs.Flow;

/**
 * Server-Sent Events broadcasting facility.
 * <p>
 * Server broadcaster can be used to manage multiple {@link SseEventSink server sinks}. It enables
 * sending events to all registered event outputs and provides facility to effectively handle
 * exceptions and closures of individual registered event outputs.
 *
 * @author Marek Potociar
 * @since 2.1
 */
public interface SseBroadcaster extends AutoCloseable, Flow.Publisher<OutboundSseEvent> {

    /**
     * Register a listener, which will be called when an exception was thrown by a given SSE event output when trying
     * to write to it or close it.
     * <p>
     * This operation is potentially slow, especially if large number of listeners get registered in the broadcaster.
     * The {@code SseBroadcaster} implementation is optimized to efficiently handle small amounts of
     * concurrent listener registrations and removals and large amounts of registered listener notifications.
     *
     * @param onException bi-consumer, taking two parameters: {@link SseEventSink}, which is the source of the
     *                    exception and the actual {@link Exception}.
     */
    void onException(BiConsumer<SseEventSink, Exception> onException);

    /**
     * Register a listener, which will be called when the SSE event output has been closed (either by client closing
     * the connection or by calling {@link SseEventSink#close()} on the server side.
     * <p>
     * This operation is potentially slow, especially if large number of listeners get registered in the broadcaster.
     * The {@code SseBroadcaster} implementation is optimized to efficiently handle small amounts of
     * concurrent listener registrations and removals and large amounts of registered listener notifications.
     *
     * @param onClose consumer taking single parameter, a {@link SseEventSink}, which was closed.
     */
    void onClose(Consumer<SseEventSink> onClose);

    /**
     * Subscribe {@link OutboundSseEvent} subscriber (i.e. {@link SseEventSink})
     * to this {@code SseBroadcaster} instance.
     *
     * @param subscriber {@link Flow.Subscriber Subscriber&lt;OutboundSseEvent&gt;} to register.
     */
    @Override
    void subscribe(Flow.Subscriber<? super OutboundSseEvent> subscriber);

    /**
     * Publish an SSE event to all subscribed {@link SseEventSink} instances.
     *
     * @param event SSE event to be published.
     */
    void broadcast(final OutboundSseEvent event);

    /**
     * Close all subscribed {@link SseEventSink} instances.
     */
    @Override
    void close();
}
