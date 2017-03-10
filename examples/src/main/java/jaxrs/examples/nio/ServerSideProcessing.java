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

package jaxrs.examples.nio;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Flow;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.NioBodyContext;
import javax.ws.rs.ext.NioBodyReader;
import javax.ws.rs.ext.NioBodyWriter;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

/**
 * @author Pavel Bucek (pavel.bucek at oracle.com)
 */
public class ServerSideProcessing {

    // example processor.
    static final Flow.Processor<ByteBuffer, ByteBuffer> PROCESSOR = new Flow.Processor<ByteBuffer, ByteBuffer>() {

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            subscription.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(ByteBuffer item) {
            // subscribers.forEach(s -> s.onNext(item));
        }

        @Override
        public void onError(Throwable throwable) {
            // subscribers.forEach(s -> s.onError(throwable));
        }

        @Override
        public void onComplete() {
            // subscribers.forEach(Flow.Subscriber::onComplete);
        }

        @Override
        public void subscribe(Flow.Subscriber<? super ByteBuffer> subscriber) {
            // add subscriber
        }
    };


    public static class MyApp extends Application {

        private final Set<Class<?>> classes;

        public MyApp() {
            classes = new HashSet<>();

            classes.add(RequestFilter.class);
            classes.add(RequestInterceptor.class);
            classes.add(BodyReader.class);
            classes.add(BodyWriter.class);
            classes.add(ResponseInterceptor.class);
            classes.add(ResponseFilter.class);
        }

        @Override
        public Set<Class<?>> getClasses() {
            return classes;
        }
    }

    // pre-matching or post, doesn't really matter
    static class RequestFilter implements ContainerRequestFilter {
        @Override
        public void filter(ContainerRequestContext requestContext) throws IOException {

            // we might need an Executor/ExecutorService when creating a processor

            requestContext.addProcessor(PROCESSOR);
        }
    }

    // reader interceptor
    static class RequestInterceptor implements ReaderInterceptor {
        @Override
        public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {

            // we might need an Executor/ExecutorService when creating a processor

            context.addProcessor(PROCESSOR);
            return context.proceed();
        }
    }

    static class BodyReader implements NioBodyReader<String> {

        @Override
        public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
            return true;
        }

        @Override
        public void readFrom(NioBodyContext<ByteBuffer, String> nioBodyContext) {
            // we might need an Executor/ExecutorService when creating a publisher

            BufferToStringProcessor bufferToStringProcessor = new BufferToStringProcessor();
            nioBodyContext.getPublisher().subscribe(bufferToStringProcessor);
        }

        // must be "buffering" - must return the content once the subscriber subscribes
        // ? shouldn't we do it differently? Like...
            // BufferPublisher pub = BufferPublisher.from(s);
            // pub.subscribe(providedSubscriber);
            // no return, since the return type is void
        static class BufferToStringProcessor implements Flow.Publisher<String>, Flow.Subscriber<ByteBuffer> {
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(ByteBuffer item) {
                // cache/process
                // when we have enough to decode "string", do
                    // subscribers.forEach(s -> s.onNext(item));
                    // subscribers.forEach(Flow.Subscriber::onComplete);
            }

            @Override
            public void onError(Throwable throwable) {
                // subscribers.forEach(s -> s.onError(throwable));
            }

            @Override
            public void onComplete() {
                // depends when this will happen; it should trigger the "final" processing / conversion.
            }

            @Override
            public void subscribe(Flow.Subscriber<? super String> subscriber) {
                // add subscriber
            }
        }
    }

    // TODO: consider removing type param
    // not needed when dealing with bytes/byte buffers only
    static class BodyWriter implements NioBodyWriter<String> {

        @Override
        public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
            return true;
        }

        @Override
        public void writeTo(NioBodyContext<String, ByteBuffer> nioBodyContext) {

            // "convert" Publisher<String> to Publisher<ByteBuffer> and subscribe entity subscriber to it.
            // needs to support "root elements" and separators if necessary.
            // For JSON, that could be '{' at document start, ',' as a separator and '}' as document end.

            // we might need an Executor/ExecutorService when creating a publisher
            BufferPublisher.from(nioBodyContext.getPublisher()).subscribe(nioBodyContext.getSubscriber());
        }

        static class BufferPublisher implements Flow.Publisher<ByteBuffer> {

            static BufferPublisher from(Flow.Publisher<String> s) {
                return null; // ...
            }

            @Override
            public void subscribe(Flow.Subscriber<? super ByteBuffer> subscriber) {
                // add subscriber
            }
        }
    }

    // container response filter; named/global, doesn't really matter
    static class ResponseFilter implements ContainerResponseFilter {

        @Override
        public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {

            // we might need an Executor/ExecutorService when creating a processor

            responseContext.addProcessor(PROCESSOR);
        }
    }

    // writer interceptor
    static class ResponseInterceptor implements WriterInterceptor {
        @Override
        public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {

            // we might need an Executor/ExecutorService when creating a processor

            context.addProcessor(PROCESSOR);
        }
    }
}
