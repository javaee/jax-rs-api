/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2015 Oracle and/or its affiliates. All rights reserved.
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

import java.io.IOException;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.SseContext;
import javax.ws.rs.sse.SseEventOutput;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Pavel Bucek (pavel.bucek at oracle.com)
 */
@Path("server-sent-events")
@Singleton
public class ServerSentEventsResource {

    private final Object outputLock = new Object();
    private SseEventOutput sseEventOutput;
    private final SseContext sseContext;

    @Inject
    public ServerSentEventsResource(SseContext sseContext) {
        this.sseContext = sseContext;
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public SseEventOutput getMessageQueue() {
        synchronized (outputLock) {
            if (sseEventOutput != null) {
                throw new IllegalStateException("Event output already served.");
            }

            sseEventOutput = sseContext.newOutput();
        }

        return sseEventOutput;
    }

    @POST
    public void addMessage(final String message) throws IOException {
        sseEventOutput.write(sseContext.newEvent().name("custom-message").data(String.class, message).build());
    }

    @DELETE
    public void close() throws IOException {
        synchronized (outputLock) {
            sseEventOutput.close();
            sseEventOutput = sseContext.newOutput();
        }
    }

    @POST
    @Path("domains/{id}")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public SseEventOutput startDomain(@PathParam("id") final String id) {
        final SseEventOutput output = sseContext.newOutput();

        new Thread() {
            public void run() {
                try {
                    output.write(sseContext.newEvent().name("domain-progress")
                            .data(String.class, "starting domain " + id + " ...").build());
                    Thread.sleep(200);
                    output.write(sseContext.newEvent().name("domain-progress").data("50%").build());
                    Thread.sleep(200);
                    output.write(sseContext.newEvent().name("domain-progress").data("60%").build());
                    Thread.sleep(200);
                    output.write(sseContext.newEvent().name("domain-progress").data("70%").build());
                    Thread.sleep(200);
                    output.write(sseContext.newEvent().name("domain-progress").data("99%").build());
                    Thread.sleep(200);
                    output.write(sseContext.newEvent().name("domain-progress").data("Done.").build());
                    output.close();

                } catch (final InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        return output;
    }
}
