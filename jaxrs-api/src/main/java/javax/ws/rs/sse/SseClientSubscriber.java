/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
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

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Flow;

/**
 * Subscriber implementation consuming {@link InboundSseEvent InboundSseEvents}. *
 *
 * @author Pavel Bucek (pavel.bucek at oracle.com)
 */
public final class SseClientSubscriber implements Flow.Subscriber<InboundSseEvent> {

    private final Consumer<Flow.Subscription> onSubscribe;
    private final Consumer<InboundSseEvent> onEvent;
    private final Consumer<Throwable> onError;
    private final Runnable onComplete;

    private SseClientSubscriber(Consumer<Flow.Subscription> onSubscribe,
                                Consumer<InboundSseEvent> onEvent,
                                Consumer<Throwable> onError,
                                Runnable onComplete) {
        this.onSubscribe = onSubscribe;
        this.onEvent = onEvent;
        this.onError = onError;
        this.onComplete = onComplete;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        onSubscribe.accept(subscription);
    }

    @Override
    public void onNext(InboundSseEvent item) {
        onEvent.accept(item);
    }

    @Override
    public void onError(Throwable throwable) {
        onError.accept(throwable);
    }

    @Override
    public void onComplete() {
        onComplete.run();
    }

    /**
     * Create new {@link Builder} instance.
     *
     * @return new {@link Builder} instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Create new {@link SseClientSubscriber} from given {@link InboundSseEvent} consumer.
     * <p>
     * The consumer will be invoked each time a sse event is received.
     *
     * @param onNext {@code InboundSseEvent} consumer.
     * @return new instance of {@code SseClientSubscriber}, which calls provided sse event consumer and uses defaults
     * for other consumers.
     * @see SseClientSubscriber.Builder
     */
    public static SseClientSubscriber fromOnNext(Consumer<InboundSseEvent> onNext) {
        return builder().onNext(onNext).build();
    }

    /**
     * {@link SseClientSubscriber} builder.
     * <p>
     * When created, builder sets default handlers for all events:
     * <ul>
     * <li>onSubscribe: calls {@link Flow.Subscription#request(long)} with {@code Long.MAX_VALUE} as a parameter,</li>
     * <li>onNext: empty method,</li>
     * <li>onError: logs thrown exceptions,</li>
     * <li>onComplete: empty method.</li>
     * </ul>
     */
    public static class Builder {

        private final Logger LOGGER = Logger.getLogger(SseClientSubscriber.class.getName());

        private Consumer<Flow.Subscription> onSubscribe = subscription -> subscription.request(Long.MAX_VALUE);
        private Consumer<InboundSseEvent> onNext = inboundSseEvent -> {
        };
        private Consumer<Throwable> onError = throwable -> {
            LOGGER.log(Level.WARNING, "Exception caught: " + throwable.getMessage(), throwable);
        };
        private Runnable onComplete = () -> {
        };

        private Builder() {
        }

        /**
         * Set {@link Consumer} which will be invoked when the {@link SseClientSubscriber} is subscribed.
         * <p>
         * Each invocation replaces stored {@code Consumer}.
         *
         * @param onSubscribe {@code Consumer} which will be invoked when the built subscriber is subscribed. Cannot be
         *                    {code null}.
         * @return updated {@code Builder} instance.
         * @throws IllegalArgumentException when the provided {@link Consumer} is {@code null}.
         */
        public Builder onSubscribe(Consumer<Flow.Subscription> onSubscribe) {
            if (onSubscribe == null) {
                throw new IllegalArgumentException("Parameter 'onSubscribe' cannot be null.");
            }
            this.onSubscribe = onSubscribe;
            return this;
        }

        /**
         * Set {@link Consumer} which will be invoked when an incoming Sse event is received.
         * <p>
         * Each invocation replaces stored {@code Consumer}.
         *
         * @param onNext {@code Consumer} which will be invoked when an incoming Sse event is received.
         * @return updated {@code Builder} instance.
         * @throws IllegalArgumentException when the provided {@link Consumer} is {@code null}.
         */
        public Builder onNext(Consumer<InboundSseEvent> onNext) {
            if (onNext == null) {
                throw new IllegalArgumentException("Parameter 'onNext' cannot be null.");
            }

            this.onNext = onNext;
            return this;
        }

        /**
         * Set {@link Consumer} which will be invoked when an exception is thrown.
         * <p>
         * Each invocation replaces stored {@code Consumer}.
         *
         * @param onError {@code Consumer} which will be invoked when an exception is thrown.
         * @return updated {@code Builder} instance.
         * @throws IllegalArgumentException when the provided {@link Consumer} is {@code null}.
         */
        public Builder onError(Consumer<Throwable> onError) {
            if (onError == null) {
                throw new IllegalArgumentException("Parameter 'onError' cannot be null.");
            }

            this.onError = onError;
            return this;
        }

        /**
         * Set {@link Runnable} which will be invoked when all events are processed.
         * <p>
         * Each invocation replaces stored {@code Runnable}.
         *
         * @param onComplete {@code Runnable} which will be invoked when all events are processed.
         * @return updated {@code Builder} instance.
         * @throws IllegalArgumentException when the provided {@link Runnable} is {@code null}.
         */
        public Builder onComplete(Runnable onComplete) {
            if (onComplete == null) {
                throw new IllegalArgumentException("Parameter 'onComplete' cannot be null.");
            }

            this.onComplete = onComplete;
            return this;
        }

        public SseClientSubscriber build() {
            return new SseClientSubscriber(onSubscribe, onNext, onError, onComplete);
        }
    }
}
