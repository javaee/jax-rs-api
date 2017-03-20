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
package jaxrs.examples.nio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Flow;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

/**
 * FileResourceClient class.
 *
 * @author Santiago Pericas-Geertsen
 */
public class FileResourceClient {

    private static final Logger LOGGER = Logger.getLogger(FileResource.class.getName());
    private static final int FOUR_KB = 4 * 1024;

    private final Map<String, byte[]> files = new ConcurrentHashMap<>();

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public void uploadClient(String path) {
        final Client client = ClientBuilder.newClient();
        final ByteArrayInputStream in = new ByteArrayInputStream(files.get(path));
        final byte[] buffer = new byte[FOUR_KB];

        final InputStreamSource entitySource = new InputStreamSource(in);

        Flow.Source<ResponsePojo> responseEntity =
                client.target("/file")
                      .request(MediaType.APPLICATION_OCTET_STREAM)
                      .nio()
                      .post(
                              Entity.nio(ByteBuffer.class, entitySource, MediaType.APPLICATION_OCTET_STREAM_TYPE),
                              ResponsePojo.class
                      );

    }

    public static class ResponsePojo {

    }

    /**
     * Reads provided stream an publishes it to subscriber(s).
     */
    public static class InputStreamSource implements Flow.Source<ByteBuffer> {

        public InputStreamSource(InputStream inputStream) {
            // ..
        }

        @Override
        public void subscribe(Flow.Sink<? super ByteBuffer> sink) {

        }
    }

    public void downloadClient(String path) {

        final Client client = ClientBuilder.newClient();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] buffer = new byte[FOUR_KB];

        Flow.Source<ByteBuffer> responseEntity = client.target("/file")
                                                       .request()
                                                       .accept(MediaType.APPLICATION_OCTET_STREAM)
                                                       .nio()
                                                       .get(ByteBuffer.class);

        // TODO: don't promote GenericType - add "shortcut", something like <T> Publisher<T> nioReadEntity(Class<T>)
        responseEntity.subscribe(new Flow.Sink<ByteBuffer>() {
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(ByteBuffer item) {
                while (item.remaining() > 0) {
                    int remaining = item.remaining();

                    if (remaining >= buffer.length) {
                        ByteBuffer byteBuffer = item.get(buffer, 0, buffer.length);
                        out.write(buffer, 0, buffer.length);
                    } else {
                        ByteBuffer byteBuffer = item.get(buffer, 0, remaining);
                        out.write(buffer, 0, remaining);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                LOGGER.log(Level.WARNING, throwable.getMessage(), throwable);
            }

            @Override
            public void onComplete() {
                try {
                    out.close();
                    files.put(path, out.toByteArray());
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Exception thrown while closing an output file.", e);
                }
            }
        });
    }
}
