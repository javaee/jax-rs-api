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

package javax.ws.rs.ext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;

import javax.ws.rs.Flow;
import javax.ws.rs.core.MediaType;

/**
 * @author Pavel Bucek (pavel.bucek at oracle.com)
 */
public interface NioBodyWriter<T> {

    // TODO: what will be passed as a type param? item.getClass() from onNext(...) invocation?
    boolean isWriteable(Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType);

    // same issue as with client - returning publisher does require additional constraint:
    // publisher must be lazy; not publishing/discarding anything until there is a subscriber.
    // In this case, it's even harder, because it needs to be valid for the subscriber subscribed by the jax-rs
    // implementation; user can subscribe Logging subscriber or something like that and the implementation needs to see
    // all messages as well.
    //
    //    Flow.Publisher<ByteBuffer> writeTo(Flow.Publisher<T> entityPojoPublisher,
    //                                       Flow.Subscriber<ByteBuffer> entity,
    //                                       Class<?> type,
    //                                       Type genericType,
    //                                       Annotation[] annotations,
    //                                       MediaType mediaType,
    //                                       MultivaluedMap<String, Object> httpHeaders);

    Flow.Source<ByteBuffer> writeTo(NioBodyContext<T> nioBodyContext) throws java.io.IOException, javax.ws.rs.WebApplicationException;

        // Multiple items vs single one
        //
        // The #writeTo method can be written in a way, which can react to a stream or single event and producing
        // appropriate output. Something like:
        //
        //        entityPojoPublisher.subscribe(new Flow.Subscriber<NioResource.POJO>() {
        //
        //            volatile NioResource.POJO cache = null;
        //            volatile boolean moreThanOne = false;
        //
        //            @Override
        //            public void onSubscribe(Flow.Subscription subscription) {
        //                subscription.request(Long.MAX_VALUE);
        //            }
        //
        //            @Override
        //            public void onNext(NioResource.POJO item) {
        //                if (item == null) {
        //                    this.cache = item;
        //                } else {
        //                    if (moreThanOne) {
        //                        entity.onNext(/* separator if needed */ null);
        //                        entity.onNext(/* pojo2bytes(item) */ null);
        //                    } else {
        //                        moreThanOne = true;
        //
        //                        entity.onNext(/* root elem start */ null);
        //                        entity.onNext(/* pojo2bytes(cache) */ null);
        //                        entity.onNext(/* separator if needed */ null);
        //                        entity.onNext(/* pojo2bytes(item) */ null);
        //                    }
        //                }
        //            }
        //
        //            @Override
        //            public void onError(Throwable throwable) {
        //                // handle Error
        //            }
        //
        //            @Override
        //            public void onComplete() {
        //                if (!moreThanOne) {
        //                    entity.onNext(/* pojo2bytes(cache) */ null);
        //                } else {
        //                    entity.onNext(/* root elem end */ null);
        //                }
        //                entity.onComplete();
        //            }
        //        });

    // provide Executor / ExecutorService?
}
