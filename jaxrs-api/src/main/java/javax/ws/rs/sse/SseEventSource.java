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

import java.net.URL;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.ws.rs.client.WebTarget;

/**
 * Client for reading and processing {@link InboundSseEvent incoming Server-Sent Events}.
 * <p>
 * SSE event source instances of this class are thread safe. To build a new instance, you can use the
 * {@link #target(javax.ws.rs.client.WebTarget) SseEventSource.target(endpoint)} factory method to get
 * a new event source builder that can be further customised and eventually used to create a new SSE
 * event source.
 * <p>
 * Once a {@link SseEventSource} is created, it can be used to {@link #open open a connection}
 * to the associated {@link WebTarget web target}. After establishing the connection, the event source starts
 * processing any incoming inbound events. Whenever a new event is received, an
 * {@link Consumer#accept(Object) Consumer<InboundSseEvent>#accept(InboundSseEvent)} method is invoked on any registered event consumers.
 * <h3>Reconnect support</h3>
 * <p>
 * The {@code SseEventSource} supports automated recuperation from a connection loss, including
 * negotiation of delivery of any missed events based on the last received  SSE event {@code id} field value, provided
 * this field is set by the server and the negotiation facility is supported by the server. In case of a connection loss,
 * the last received SSE event {@code id} field value is send in the
 * <tt>{@value javax.ws.rs.core.HttpHeaders#LAST_EVENT_ID_HEADER}</tt> HTTP
 * request header as part of a new connection request sent to the SSE endpoint. Upon a receipt of such reconnect request, the SSE
 * endpoint that supports this negotiation facility is expected to replay all missed events. Note however, that this is a
 * best-effort mechanism which does not provide any guaranty that all events would be delivered without a loss. You should
 * therefore not rely on receiving every single event and design your client application code accordingly.
 * <p>
 * By default, when a connection the the SSE endpoint is lost, the event source will wait 500&nbsp;ms
 * before attempting to reconnect to the SSE endpoint. The SSE endpoint can however control the client-side retry delay
 * by including a special {@code retry} field value in the any send event. JAX-RS {@code SseEventSource} tracks any
 * received SSE event {@code retry} field values set by the endpoint and adjusts the reconnect delay accordingly,
 * using the last received {@code retry} field value as the reconnect delay.
 * <p>
 * In addition to handling the standard connection loss failures, JAX-RS {@code SseEventSource} automatically deals with any
 * {@code HTTP 503 Service Unavailable} responses from an SSE endpoint, that contain a
 * <tt>{@value javax.ws.rs.core.HttpHeaders#RETRY_AFTER}</tt> HTTP header with a valid value. The
 * <tt>HTTP 503 + {@value javax.ws.rs.core.HttpHeaders#RETRY_AFTER}</tt> technique is often used by HTTP endpoints
 * as a means of connection and traffic throttling.
 * In case a <tt>HTTP 503 + {@value javax.ws.rs.core.HttpHeaders#RETRY_AFTER}</tt> response is received in return to a connection
 * request, JAX-RS SSE event source will automatically schedule a new reconnect attempt and use the received
 * <tt>{@value javax.ws.rs.core.HttpHeaders#RETRY_AFTER}</tt> HTTP header value as a one-time override of the reconnect delay.
 *
 * @author Marek Potociar
 * @since 2.1
 */
public interface SseEventSource extends AutoCloseable {

    /**
     * JAX-RS {@link SseEventSource} builder class.
     * <p>
     * Event source builder provides methods that let you conveniently configure and subsequently build
     * a new {@code SseEventSource} instance. You can obtain a new event source builder instance using
     * a static {@link SseEventSource#target(javax.ws.rs.client.WebTarget) SseEventSource.target(endpoint)} factory method.
     * <p>
     * For example:
     * <pre>
     * SseEventSource es = SseEventSource.target(endpoint)
     *                             .reconnectingEvery(5, SECONDS)
     *                             .build();
     * es.register(System.out::println);
     * es.open();
     * </pre>
     */
    abstract class Builder {

        /**
         * Name of the property identifying the {@link SseEventSource.Builder} implementation
         * to be returned from {@link SseEventSource.Builder#newBuilder()}.
         */
        public static final String JAXRS_DEFAULT_SSE_BUILDER_PROPERTY =
                "javax.ws.rs.sse.SseEventSource.Builder";
        /**
         * Default SSE event source builder implementation class name.
         */
        private static final String JAXRS_DEFAULT_SSE_BUILDER =
                "org.glassfish.jersey.media.sse.internal.JerseySseEventSource$Builder";

        /**
         * Allows custom implementations to extend the SSE event source builder class.
         */
        protected Builder() {
        }

        /**
         * Create a new SSE event source instance using the default implementation class provided by the JAX-RS
         * implementation provider.
         *
         * @return new SSE event source builder instance.
         */
        static Builder newBuilder() {
            try {
                Object delegate = FactoryFinder.find(JAXRS_DEFAULT_SSE_BUILDER_PROPERTY,
                        JAXRS_DEFAULT_SSE_BUILDER, SseEventSource.Builder.class);
                if (!(delegate instanceof Builder)) {
                    Class pClass = Builder.class;
                    String classnameAsResource = pClass.getName().replace('.', '/') + ".class";
                    ClassLoader loader = pClass.getClassLoader();
                    if (loader == null) {
                        loader = ClassLoader.getSystemClassLoader();
                    }
                    URL targetTypeURL = loader.getResource(classnameAsResource);
                    throw new LinkageError("ClassCastException: attempting to cast"
                                           + delegate.getClass().getClassLoader().getResource(classnameAsResource)
                                           + " to " + targetTypeURL);
                }
                return (Builder) delegate;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        protected abstract Builder target(WebTarget endpoint);

        /**
         * Set the initial reconnect delay to be used by the event source.
         * <p>
         * Note that this value may be later overridden by the SSE endpoint using either a {@code retry} SSE event field
         * or <tt>HTTP 503 + {@value javax.ws.rs.core.HttpHeaders#RETRY_AFTER}</tt> mechanism as described
         * in the {@link SseEventSource} javadoc.
         *
         * @param delay the default time to wait before attempting to recover from a connection loss.
         * @param unit  time unit of the reconnect delay parameter.
         * @return updated event source builder instance.
         */
        public abstract Builder reconnectingEvery(long delay, TimeUnit unit);

        /**
         * Build new SSE event source pointing at a SSE streaming {@link WebTarget web target}.
         * <p>
         * The returned event source is ready, but not {@link SseEventSource#open() connected} to the SSE endpoint.
         * It is expected that you will manually invoke its {@link #open()} method once you are ready to start
         * receiving SSE events. In case you want to build an event source instance that is already connected
         * to the SSE endpoint, use the event source builder {@link #open()} method instead.
         * <p>
         * Once the event source is open, the incoming events are processed by the event source in an
         * asynchronous task that runs in an internal single-threaded {@link ScheduledExecutorService
         * scheduled executor service}.
         *
         * @return new event source instance, ready to be connected to the SSE endpoint.
         * @see #open()
         */
        public abstract SseEventSource build();
    }

    /**
     * Register a {@link InboundSseEvent} consumer.
     * <p>
     * Given consumer is invoked once per each received event.
     *
     * @param onEvent event consumer.
     * @throws IllegalArgumentException when the provided parameter is {@code null}.
     */
    void register(Consumer<InboundSseEvent> onEvent);

    /**
     * Register {@link InboundSseEvent} and {@link Throwable} consumers.
     * <p>
     * Event consumer is invoked once per each received event, {@code Throwable} consumer is invoked invoked upon a
     * unrecoverable error encountered by a {@link SseEventSource}.
     *
     * @param onEvent event consumer.
     * @param onError error consumer.
     * @throws IllegalArgumentException when the any of the provided parameters is {@code null}.
     */
    void register(Consumer<InboundSseEvent> onEvent,
                  Consumer<Throwable> onError);

    /**
     * Register {@link InboundSseEvent} and {@link Throwable} consumers and onComplete callback.
     * <p>
     * Event consumer is invoked once per each received event, {@code Throwable} consumer is invoked invoked upon a
     * unrecoverable error encountered by a {@link SseEventSource}, onComplete callback is invoked when there are no
     * further events to be received.
     *
     * @param onEvent    event consumer.
     * @param onError    error consumer.
     * @param onComplete onComplete handler.
     * @throws IllegalArgumentException when the any of the provided parameters is {@code null}.
     */
    void register(Consumer<InboundSseEvent> onEvent,
                  Consumer<Throwable> onError,
                  Runnable onComplete);

    /**
     * Create a new {@link SseEventSource.Builder event source builder} that provides convenient way how to
     * configure and fine-tune various aspects of a newly prepared event source instance.
     *
     * @param endpoint SSE streaming endpoint. Must not be {@code null}.
     * @return a builder of a new event source instance pointing at the specified SSE streaming endpoint.
     * @throws NullPointerException in case the supplied web target is {@code null}.
     */
    static Builder target(WebTarget endpoint) {
        return Builder.newBuilder().target(endpoint);
    }

    /**
     * Open the connection to the supplied SSE underlying {@link WebTarget web target} and start processing incoming
     * {@link InboundSseEvent events}.
     *
     * @throws IllegalStateException in case the event source has already been opened earlier.
     */
    void open();

    /**
     * Check if this event source instance has already been {@link #open() opened}.
     *
     * @return {@code true} if this event source is open, {@code false} otherwise.
     */
    boolean isOpen();

    /**
     * Close this event source.
     * <p>
     * The method will wait up to 5 seconds for the internal event processing tasks to complete.
     */
    @Override
    default void close() {
        close(5, TimeUnit.SECONDS);
    }

    /**
     * Close this event source and wait for the internal event processing task to complete
     * for up to the specified amount of wait time.
     * <p>
     * The method blocks until the event processing task has completed execution after a shutdown
     * request, or until the timeout occurs, or the current thread is interrupted, whichever happens
     * first.
     * <p>
     * In case the waiting for the event processing task has been interrupted, this method restores
     * the {@link Thread#interrupted() interrupt} flag on the thread before returning {@code false}.
     *
     * @param timeout the maximum time to wait.
     * @param unit    the time unit of the timeout argument.
     * @return {@code true} if this executor terminated and {@code false} if the timeout elapsed
     * before termination or the termination was interrupted.
     */
    boolean close(final long timeout, final TimeUnit unit);
}
