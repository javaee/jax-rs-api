/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2015 Oracle and/or its affiliates. All rights reserved.
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

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NioInputStream;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FileResource class.
 *
 * @author Santiago Pericas-Geertsen
 */
@Path("/file")
public class FileResource {

    private static final int FOUR_KB = 4 * 1024;

    private static Map<String, byte[]> files = new ConcurrentHashMap<String, byte[]>();

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
                out -> {                    // writer handler
                    try {
                        final int n = in.read(buffer);
                        if (n >= 0) {
                            out.write(buffer, 0, n);
                            return true;    // more to write
                        }
                        in.close();
                        return false;       // we're done
                    } catch (IOException e) {
                        throw new WebApplicationException(e);
                    }
                }).build();
    }

    /**
     * Resource that reads input file from byte array and registers an NioWriterHandler
     * to write response non-blockingly. The NioWriterHandler is called if and only
     * if write is possible and last call returned {@code true}. An error handler
     * is also registered for logging purposes.
     *
     * @param path path to file.
     * @return response with AsyncWriter.
     */
    @GET
    @Path("downloadError")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadError(@QueryParam("path") String path) {
        final ByteArrayInputStream in = new ByteArrayInputStream(files.get(path));
        final byte[] buffer = new byte[FOUR_KB];

        return Response.ok().entity(
                out -> {                    // writer handler
                    try {
                        final int n = in.read(buffer);
                        if (n >= 0) {
                            out.write(buffer, 0, n);
                            return true;    // more to write
                        }
                        in.close();
                        return false;       // we're done
                    } catch (IOException e) {
                        throw new WebApplicationException(e);
                    }
                },
                throwable -> {              // error handler
                    System.out.println("Problem found: " + throwable.getMessage());
                    throw throwable;
                }).build();
    }

    /**
     * Resource that registers an NioReaderHandler to read the entity non-blockingly
     * and writes the result to a byte array. The handler check the end of stream
     * by calling {@code isFinished()}.
     *
     * @param path     path to file.
     * @param request  injected request to read from.
     * @param response async response to inform completion.
     */
    @POST
    @Path("upload")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public void upload(@QueryParam("path") String path, @Context Request request,
                       @Context AsyncResponse response) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] buffer = new byte[FOUR_KB];

        request.entity(
                in -> {                     // reader handler
                    try {
                        if (in.isFinished()) {
                            files.put(path, out.toByteArray());
                            out.close();
                            response.resume("Upload completed");
                        } else {
                            final int n = in.read(buffer);
                            out.write(buffer, 0, n);
                        }
                    } catch (IOException e) {
                        throw new WebApplicationException(e);
                    }
                });
    }

    /**
     * Resource that registers an NioReaderHandler to read the entity non-blockingly
     * and writes the result to a byte array. A completion handler is also registered
     * to finish the upload process.
     *
     * @param path     path to file.
     * @param request  injected request to read from.
     * @param response async response to inform completion.
     */
    @POST
    @Path("uploadCompletion")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public void uploadCompletion(@QueryParam("path") String path, @Context Request request,
                                 @Context AsyncResponse response) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] buffer = new byte[FOUR_KB];

        request.entity(
                in -> {                     // reader handler
                    try {
                        final int n = in.read(buffer);
                        out.write(buffer, 0, n);
                    } catch (IOException e) {
                        throw new WebApplicationException(e);
                    }
                },
                (NioInputStream in) -> {    // completion handler
                    try {
                        assert in.isFinished();
                        files.put(path, out.toByteArray());
                        out.close();
                        response.resume("Upload completed");
                    } catch (IOException e) {
                        throw new WebApplicationException(e);
                    }
                });
    }

    /**
     * Resource that registers an NioReaderHandler to read the entity non-blockingly
     * and writes the result to a byte array. A completion and an error handlers are
     * also registered.
     *
     * @param path     path to file.
     * @param request  injected request to read from.
     * @param response async response to inform completion.
     */
    @POST
    @Path("uploadCompletionError")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public void uploadCompletionError(@QueryParam("path") String path, @Context Request request,
                                      @Context AsyncResponse response) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] buffer = new byte[FOUR_KB];

        request.entity(
                in -> {                     // reader handler
                    try {
                        final int n = in.read(buffer);
                        out.write(buffer, 0, n);
                    } catch (IOException e) {
                        throw new WebApplicationException(e);
                    }
                },
                (NioInputStream in) -> {    // completion handler
                    try {
                        assert in.isFinished();
                        files.put(path, out.toByteArray());
                        out.close();
                        response.resume("Upload completed");
                    } catch (IOException e) {
                        throw new WebApplicationException(e);
                    }
                },
                throwable -> {              // error handler
                    System.out.println("Problem found: " + throwable.getMessage());
                    throw throwable;
                });
    }
}
