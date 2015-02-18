/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011-2015 Oracle and/or its affiliates. All rights reserved.
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

package jaxrs.examples.async;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.concurrent.TimeUnit.SECONDS;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

/**
 * Long-running asynchronous processing examples.
 *
 * @author Marek Potociar
 */
@Path("/async/longRunning")
@Produces("text/plain")
public class LongRunningAsyncOperationResource {

    @GET
    @Path("sync")
    public String basicSyncExample() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(LongRunningAsyncOperationResource.class.getName()).log(Level.SEVERE, "Response processing interrupted", ex);
        }
        return "Hello async world!";
    }

    @GET
    @Path("async")
    public void asyncExample(
            @Suspended final AsyncResponse ar) {
        ar.setTimeout(15, SECONDS);
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LongRunningAsyncOperationResource.class.getName()).log(Level.SEVERE, "Response processing interrupted", ex);
                }
                ar.resume("Hello async world!");
            }
        });
    }

    @GET
    @Path("asyncSelective")
    public void selectiveSuspend(@QueryParam("query") final String query, @Suspended final AsyncResponse ar) {
        if (!isComplex(query)) {
            // process simple queries synchronously
            ar.resume("Simple result for " + query);
        } else {
            Executors.newSingleThreadExecutor().submit(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(LongRunningAsyncOperationResource.class.getName())
                                .log(Level.SEVERE, "Response processing interrupted", ex);
                    }
                    ar.resume("Complex result for " + query);
                }
            });
        }
    }

    private boolean isComplex(String query) {
        return new Random(query.hashCode()).nextBoolean();
    }

    @GET
    @Path("asyncTimeoutOverride")
    public void overriddenTimeoutAsync(@QueryParam("timeOut") Long timeOut, @QueryParam("timeUnit") TimeUnit timeUnit,
                                       @Suspended final AsyncResponse ar) {
        if (timeOut != null && timeUnit != null) {
            ar.setTimeout(timeOut, timeUnit);
        } else {
            ar.setTimeout(15, SECONDS);
        }

        Executors.newSingleThreadExecutor().submit(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LongRunningAsyncOperationResource.class.getName())
                            .log(Level.SEVERE, "Response processing interrupted", ex);
                }
                ar.resume("Hello async world!");
            }
        });
    }

    @GET
    @Path("asyncHandleUsage")
    public void suspendHandleUsageExample(@Suspended final AsyncResponse ar) {
        ar.setTimeout(15, SECONDS);
        Executors.newSingleThreadExecutor().submit(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LongRunningAsyncOperationResource.class.getName())
                            .log(Level.SEVERE, "Response processing interrupted", ex);
                }
                ar.resume("Hello async world!");
            }
        });

        Executors.newSingleThreadExecutor().submit(new Runnable() {

            @Override
            public void run() {
                while (!ar.isDone()) {
                }
                Logger.getLogger(LongRunningAsyncOperationResource.class.getName())
                        .log(Level.INFO, "Context resumed with a response!");
            }
        });
    }
}
