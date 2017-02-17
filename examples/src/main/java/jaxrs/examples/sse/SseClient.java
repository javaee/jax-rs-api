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
package jaxrs.examples.sse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.InboundSseEvent;
import javax.ws.rs.sse.SseEventSource;
import javax.ws.rs.sse.SseSubscription;

/**
 * Examples of Client-side Server-sent events processing.
 *
 * @author Marek Potociar (marek.potociar at oracle.com)
 */
public class SseClient {

    public static final WebTarget target = ClientBuilder.newClient().target("server-sent-events");

    public static void main(String[] args) {
        consumeAllEvents();
    }

    private static void consumeAllEvents() {

        // EventSource#subscribe(Consumer<InboundSseEvent>)
        // consumes all events, writes then on standard out (System.out::println)
        try (final SseEventSource eventSource =
                     SseEventSource.target(target)
                                   .build()) {

            eventSource.subscribe(System.out::println);
            eventSource.open();

            for (int counter = 0; counter < 5; counter++) {
                target.request().post(Entity.text("message " + counter));
            }

            Thread.sleep(500); // make sure all the events have time to arrive
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // EventSource#subscribe(Consumer<InboundSseEvent>, Consumer<Throwable>)
        // consumes all events and all exceptions, writing both on standard out.
        try (final SseEventSource eventSource = SseEventSource.target(target).build()) {

            eventSource.subscribe(System.out::println, Throwable::printStackTrace);
            eventSource.open();

            for (int counter = 0; counter < 5; counter++) {
                target.request().post(Entity.text("message " + counter));
            }

            Thread.sleep(500); // make sure all the events have time to arrive
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // EventSource#subscribe(Consumer<InboundSseEvent>, Consumer<Throwable>, Runnable)
        // consumes all events and all exceptions, writing both on standard out.
        // registers onComplete callback, which will print out a message "There will be no further events."
        try (final SseEventSource eventSource = SseEventSource.target(target).build()) {

            eventSource.subscribe(
                    System.out::println,
                    Throwable::printStackTrace,
                    () -> System.out.println("There will be no further events."));
            eventSource.open();

            for (int counter = 0; counter < 5; counter++) {
                target.request().post(Entity.text("message " + counter));
            }

            Thread.sleep(500); // make sure all the events have time to arrive
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // EventSource#subscribe(Consumer<InboundSseEvent>, Consumer<Throwable>, Runnable)
        // consumes all events and all exceptions, writing both on standard out.
        // registers onComplete callback, which will print out a message "There will be no further events."
        // consumes SseSubscription, which can control backpressure. This particular case requests all evens which can
        //   possibly be received.
        try (final SseEventSource eventSource = SseEventSource.target(target).build()) {

            eventSource.subscribe(
                    sseSubscription -> {
                        sseSubscription.request(Long.MAX_VALUE);
                    },
                    System.out::println,
                    Throwable::printStackTrace,
                    () -> System.out.println("There will be no further events."));
            eventSource.open();

            for (int counter = 0; counter < 5; counter++) {
                target.request().post(Entity.text("message " + counter));
            }

            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // EventSource#subscribe(Consumer<InboundSseEvent>, Consumer<Throwable>, Runnable)
        // consumes all events and all exceptions, writing both on standard out.
        // registers onComplete callback, which will print out a message "There will be no further events."
        // consumes SseSubscription, which can control backpressure.
        // uses CustomSseSubscriber to group all callbacks and consumes events one by one; requesting next event when
        // the previous one is processed.
        try (final SseEventSource eventSource = SseEventSource.target(target).build()) {

            CustomSseSubscriber customSseSubscriber = new CustomSseSubscriber();

            eventSource.subscribe(
                    customSseSubscriber::onSubscribe,
                    customSseSubscriber::onEvent,
                    customSseSubscriber::onError,
                    customSseSubscriber::onComplete);

            eventSource.open();

            for (int counter = 0; counter < 5; counter++) {
                target.request().post(Entity.text("message " + counter));
            }

            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class CustomSseSubscriber {

        private final ExecutorService eventProcessorService  = Executors.newCachedThreadPool();

        private volatile SseSubscription subscription;

        void onSubscribe(SseSubscription subscription) {
            this.subscription = subscription;
            // request first event
            subscription.request(1);
        }

        void onEvent(InboundSseEvent event) {
            eventProcessorService.submit(() -> {
                // fire event processing in the new thread; assuming this is dedicated pool for long running
                // operations
                processEvent(event);

                // event processing is done, request another event
                subscription.request(1);

                // or do subscription.cancel(); if you wish to unsubscribe
            });
        }

        void onError(Throwable throwable) {
            throwable.printStackTrace();
        }

        void onComplete() {
            System.out.println("There will be no further events.");
        }

        private void processEvent(InboundSseEvent event) {
            // do something with the event.
        }
    }
}
