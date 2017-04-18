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

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;

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

        // EventSource#register(Consumer<InboundSseEvent>)
        // consumes all events, writes then on standard out (System.out::println)
        try (final SseEventSource eventSource =
                     SseEventSource.target(target)
                                   .build()) {

            eventSource.register(System.out::println);
            eventSource.open();

            for (int counter = 0; counter < 5; counter++) {
                target.request().post(Entity.text("message " + counter));
            }

            Thread.sleep(500); // make sure all the events have time to arrive
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // EventSource#register(Consumer<InboundSseEvent>, Consumer<Throwable>)
        // consumes all events and all exceptions, writing both on standard out.
        try (final SseEventSource eventSource = SseEventSource.target(target).build()) {

            eventSource.register(System.out::println, Throwable::printStackTrace);
            eventSource.open();

            for (int counter = 0; counter < 5; counter++) {
                target.request().post(Entity.text("message " + counter));
            }

            Thread.sleep(500); // make sure all the events have time to arrive
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // EventSource#register(Consumer<InboundSseEvent>, Consumer<Throwable>, Runnable)
        // consumes all events and all exceptions, writing both on standard out.
        // registers onComplete callback, which will print out a message "There will be no further events."
        try (final SseEventSource eventSource = SseEventSource.target(target).build()) {

            eventSource.register(
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
    }
}
