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

import javax.ws.rs.Flow;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.NioBodyContext;
import javax.ws.rs.ext.NioBodyReader;
import javax.ws.rs.ext.NioBodyWriter;

/**
 * @author Pavel Bucek (pavel.bucek at oracle.com)
 */
@SuppressWarnings("ConstantConditions")
public class NioClient {

    private static final Client CLIENT = ClientBuilder.newClient();

    // EX1 -- basic handling - bytes
    private static void ex1() {
        // request entity
        Flow.Source<ByteBuffer> source = null;
        // response entity processor
        Flow.Sink<ByteBuffer> sink = null;

        CLIENT.target("http://nio/ex1")
              .request()
              .nio()
              .post(Entity.nio(ByteBuffer.class, source, MediaType.APPLICATION_OCTET_STREAM_TYPE), ByteBuffer.class)
              .subscribe(sink);

    }


    /**
     * sending stream of pojos.
     */
    private static class EX2 {
        private static void ex2() {

            CLIENT.register(Ex2NioBodyWriter.class);

            // request entity
            Flow.Source<NioResource.POJO> source = null;

            Response response =
                    CLIENT.target("http://nio/ex1")
                          .request()
                          .post(Entity.nio(NioResource.POJO.class, source, MediaType.APPLICATION_JSON_TYPE));

            assert Response.Status.Family.SUCCESSFUL.equals(response.getStatusInfo().getFamily());
        }

        public static class Ex2NioBodyWriter implements NioBodyWriter<NioResource.POJO> {
            @Override
            public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
                return false;
            }

            @Override
            public Flow.Source<ByteBuffer> writeTo(NioBodyContext<NioResource.POJO> nioBodyContext) {
                // map Publisher<POJO> to Publisher<ByteBuffer> and return it.
                return null;
            }
        }
    }


    /**
     * consuming stream of pojos.
     */
    private static class EX3 {

        private static void ex3() {

            CLIENT.register(Ex3NioBodyReader.class);

            Flow.Source<NioResource.POJO> pojoSource =
                    CLIENT.target("http://nio/ex1")
                          .request()
                          .nio()
                          .post(null, NioResource.POJO.class);

            pojoSource.subscribe(
                    // problem with this is that the publisher MUST BE lazy.
                    // once the code execution returns from the client implementation, underlying runtime doesn't have anything else
                    // than a publisher instance and it is legal to send events to that publisher. On the other hand, there must be
                    // simple way how to ensure that client can consume ALL events without a possibility that something will be
                    // missed because of "late" subscription.
                    null
            );

        }

        public static class Ex3NioBodyReader implements NioBodyReader<NioResource.POJO> {
            @Override
            public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
                return true;
            }

            @Override
            public Flow.Source<NioResource.POJO> readFrom(NioBodyContext<ByteBuffer> nioBodyContext) {
                NioResource.EX2.Ex2MappingProcessor mappingProcessor = new NioResource.EX2.Ex2MappingProcessor();
                nioBodyContext.getSource().subscribe(mappingProcessor);
                return mappingProcessor;
            }
        }
    }
}
