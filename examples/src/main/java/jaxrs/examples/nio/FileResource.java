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
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.Flow;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

/**
 * FileResource class.
 *
 * @author Santiago Pericas-Geertsen
 */
@Path("/file")
public class FileResource {

    private static final Logger LOGGER = Logger.getLogger(FileResource.class.getName());
    private static final int FOUR_KB = 4 * 1024;

    private static Map<String, byte[]> files = new ConcurrentHashMap<>();
    // or @Resource ManagedExecutorService on Java EE container
    // private static ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * Resource that reads input file from byte array and registers an NioWriterHandler
     * to write response non-blockingly. The NioWriterHandler is called if and only if
     * write is possible and last call returned {@code true}.
     *
     * @param path path to file.
     * @return response with AsyncWriter.
     */
    @GET
    @Path("download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@QueryParam("path") String path) {
        final ByteArrayInputStream in = new ByteArrayInputStream(files.get(path));
        final byte[] buffer = new byte[FOUR_KB];

        return Response.ok().entity(
                out -> {
                    // Are we sure this is running on another thread? if not, we need to have this passed to an executor
                    // service.
                    try {
                        int n;
                        while ((n = in.read(buffer)) > 0) {
                            out.onNext(ByteBuffer.wrap(buffer, 0, n));
                        }
                        // we're done
                        in.close();
                        out.onComplete();
                    } catch (IOException e) {
                        // do something with the exception.
                        // re-throwing it doesn't really help, since at this point, the code is executed on another
                        // thread.
                        LOGGER.log(Level.WARNING, "Exception thrown while writing a response.", e);
                    }
                }).build();
    }

    /**
     * Subscribing to a given publisher.
     * <p>
     * The consumer in entity can be the adaptation layer - for example between flow and org.ractivestreams ifaces.
     */
    @GET
    @Path("download2")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download2(@QueryParam("path") String path) {
        Flow.Source<ByteBuffer> entitySource = null; // ...

        // Subscriber is provided by the implementation and it is subscribed to "any" publisher of ByteBuffer.
        return Response.ok().entity(entitySource::subscribe).build();
    }

    /**
     * Resource that reads input file from byte array and registers an NioWriterHandler
     * to write response non-blockingly. The NioWriterHandler is called if and only
     * if write is possible and last call returned {@code true}. Any error occurred during
     * upload is returned back to the client, errors related to closing the stream might
     * be only logged.
     *
     * @param path     path to file.
     * @param request  injected request to read from.
     * @param response async response to inform completion.
     */
    @POST
    @Path("upload")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public void upload(@QueryParam("path") String path,
                       @Context Request request,
                       @Context AsyncResponse response) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] buffer = new byte[FOUR_KB];

        request.entity().subscribe(new Flow.Sink<ByteBuffer>() {
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
                try {
                    out.close();
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Exception thrown while closing an output file.", e);
                }
                response.resume(throwable);
            }

            @Override
            public void onComplete() {
                try {
                    files.put(path, out.toByteArray());
                    out.close();
                    response.resume("Upload complete.");
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Exception thrown while closing an output file.", e);
                    response.resume(e);
                }
            }
        });
    }
}
