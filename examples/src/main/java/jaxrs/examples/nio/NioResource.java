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

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;

import javax.ws.rs.Consumes;
import javax.ws.rs.Flow;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.NioBodyContext;
import javax.ws.rs.ext.NioBodyReader;
import javax.ws.rs.ext.NioBodyWriter;
import javax.ws.rs.ext.Provider;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;

/**
 * Several examples of NIO processing on the server side.
 *
 * @author Pavel Bucek (pavel.bucek at oracle.com)
 */
@SuppressWarnings("ALL") // we don't care about any warnings for this class.

@Path("/nio")
public class NioResource {

    /**
     * basic handling - bytes only
     */
    public static class EX1 {

        @POST
        @Path("/ex1")
        // subscription to entity publisher must be done before returning from the response method
        public Flow.Source<ByteBuffer> ex1(Flow.Source<ByteBuffer> entity) {
            Ex1Processor processor = new Ex1Processor();

            entity.subscribe(processor);
            return processor;
        }

        // ex1 processor
        public static class Ex1Processor implements Flow.Processor<ByteBuffer, ByteBuffer> {
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                // notify the subscriber right when there is an entity chunk to be processed
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(ByteBuffer item) {
                // process request entity chunk
            }

            @Override
            public void onError(Throwable throwable) {
                // handle error
            }

            @Override
            public void onComplete() {
                // subscribers.forEach(Flow.Subscriber::onComplete);
                // request entity fully received
                // the response entity is now expected
                // - once this object is returned from the resource method, implementation will
                //   subscribe and wait until "onNext(ByteBuffer)" and/or "onComplete()" are invoked
            }

            @Override
            public void subscribe(Flow.Sink<? super ByteBuffer> sink) {
                // add subscriber
            }
        }
    }

    /**
     * consuming stream of pojos
     */
    public static class EX2 {

        @POST
        @Path("/ex2")
        @Consumes(MediaType.APPLICATION_JSON)
        // Subscription to entity publisher must be done before returning from the response method.
        // It doesn't make sense to return anything when the request (entity) is not processed.
        public void ex2(Flow.Source<POJO> entity,
                        @Suspended AsyncResponse response) {

            // TODO: introduce a helper or modify AsyncResponse to support this pattern directly?
            entity.subscribe(
                    // POJO subscriber - consumer
                    new Flow.Sink<POJO>() {
                        @Override
                        public void onSubscribe(Flow.Subscription subscription) {
                            // ...
                        }

                        @Override
                        public void onNext(POJO item) {
                            // ...
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            response.resume(throwable);
                        }

                        @Override
                        public void onComplete() {
                            response.resume(Response.ok().build());
                        }
                    }
            );
        }

        @Provider
        @Consumes(MediaType.APPLICATION_JSON)
        public static class Ex2NioBodyReader implements NioBodyReader<POJO> {
            @Override
            public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
                return true;
            }

            @Override
            public void readFrom(NioBodyContext<ByteBuffer, POJO> nioBodyContext) {
                Ex2MappingProcessor mappingProcessor = new Ex2MappingProcessor();
                mappingProcessor.subscribe(nioBodyContext.getSink());
                nioBodyContext.getSource().subscribe(mappingProcessor);
            }
        }

        // mapping Publisher<ByteBuffer> to Publisher<POJO>
        // ByteBuffers are expected to contain JSON (indicated by @Consumes on the resource method and NioBodyReader).
        public static class Ex2MappingProcessor implements Flow.Sink<ByteBuffer>, Flow.Source<POJO> {
            @Override
            public void subscribe(Flow.Sink<? super POJO> sink) {
            }

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(ByteBuffer item) {
                // process the buffer
                // if we have enough to publish new POJO instance, do it
                // if not, cache and wait
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onComplete() {
                // send onComplete to all subscribers
            }
        }
    }

    /**
     * produce stream of pojos
     */
    public static class EX3 {

        @GET
        @Path("/ex3")
        @Produces(MediaType.APPLICATION_JSON)
        // subscription to entity publisher must be done before returning from the response method
        public Flow.Source<POJO> ex3() {

            Flow.Source<POJO> pojoSource = null;

            // source of the POJO "stream" can be anything - database call, client call to
            // another service, ...
            //
            //    DB
            //        .getEmployees(department) // StreamPublisher<EmployeeDbModel> -- reactive stream
            //        .map((Function<EmployeeDbModel, EmployeeToReturn>) employeeDbModel -> {
            //            // ...
            //        });

            return pojoSource;
        }

        @Provider
        @Produces(MediaType.APPLICATION_JSON)
        public static class Ex3NioBodyWriter implements NioBodyWriter<POJO> {
            @Override
            public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
                return false;
            }

            @Override
            public void writeTo(NioBodyContext<POJO, ByteBuffer> nioBodyContext) {
                // map Publisher<POJO> to Publisher<ByteBuffer> and subscribe Flow.Subscriber<ByteBuffer> to it.
            }
        }
    }


    /**
     * integration with other publisher/subscriber iface (rxjava2, reactivestreams.org)
     */
    public static class EX4 {

        /**
         * Publisher adapter(s).
         */
        @GET
        @Path("download5")
        @Produces(MediaType.APPLICATION_JSON)
        public Flow.Source<Bar> download5(@QueryParam("path") String path) {

            Flow.Source<Foo> entitySource = null; // ...

            // javax.ws.rs.Flow.Publisher -> org.reactivestreams.Publisher (rxjava2 Flowable in this case)
            Flowable<Bar> barFlowable =
                    Flowable.fromPublisher((Publisher<Foo>) s -> entitySource.subscribe(new Flow.Sink<Foo>() {
                        @Override
                        public void onSubscribe(Flow.Subscription subscription) {
                            s.onSubscribe(new Subscription() {
                                @Override
                                public void request(long n) {
                                    subscription.request(n);
                                }

                                @Override
                                public void cancel() {
                                    subscription.cancel();
                                }
                            });
                        }

                        @Override
                        public void onNext(Foo item) {
                            s.onNext(item);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            s.onError(throwable);
                        }

                        @Override
                        public void onComplete() {
                            s.onComplete();
                        }
                    })).map(EX4::foot2Bar); // mapping, filtering, zipping, ...

            // org.reactivestreams.Publisher -> javax.ws.rs.Publisher
            return subscriber -> barFlowable.subscribe(new Subscriber<Bar>() {
                @Override
                public void onSubscribe(Subscription s) {
                    subscriber.onSubscribe(new Flow.Subscription() {
                        @Override
                        public void request(long n) {
                            s.request(n);
                        }

                        @Override
                        public void cancel() {
                            s.cancel();
                        }
                    });
                }

                @Override
                public void onNext(Bar bar) {
                    subscriber.onNext(bar);
                }

                @Override
                public void onError(Throwable t) {
                    subscriber.onError(t);
                }

                @Override
                public void onComplete() {
                    subscriber.onComplete();
                }
            });
        }

        private static Bar foot2Bar(Foo f) {
            return new Bar();
        }

        private static class Foo {
        }

        private static class Bar {
        }
    }


    static class POJO {
    }
}
