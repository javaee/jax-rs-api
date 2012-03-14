/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011-2012 Oracle and/or its affiliates. All rights reserved.
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
import static java.util.concurrent.TimeUnit.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Suspend;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.ExecutionContext;

/**
 *
 * @author Marek Potociar (marek.potociar at oracle.com)
 */
@Path("/async/longRunning")
@Produces("text/plain")
public class LongRunningAsyncOperationResource {

    @Context
    private ExecutionContext ctx;

    @GET
    @Path("basicSyncExample")
    public String basicSyncExample() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(LongRunningAsyncOperationResource.class.getName()).log(Level.SEVERE, "Response processing interrupted", ex);
        }
        return "Hello async world!";
    }

    @GET
    @Suspend(timeOut = 15, timeUnit = SECONDS)
    @Path("suspendViaAnnotationFieldInjectedCtx")
    public void suspendViaAnnotationExample() {
        Executors.newSingleThreadExecutor().submit(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LongRunningAsyncOperationResource.class.getName()).log(Level.SEVERE, "Response processing interrupted", ex);
                }
                ctx.resume("Hello async world!");
            }
        });

        // default suspend;
    }

    @GET
    @Suspend(timeOut = 15, timeUnit = SECONDS)
    @Path("suspendViaAnnotationMethodInjectedCtx")
    public void suspendViaAnnotationExample2(@Context final ExecutionContext ctx2) {
        Executors.newSingleThreadExecutor().submit(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LongRunningAsyncOperationResource.class.getName()).log(Level.SEVERE, "Response processing interrupted", ex);
                }
                ctx2.resume("Hello async world!");
            }
        });

        // default suspend;
    }

    @GET
    @Path("suspendViaContext")
    public String suspendViaContextExample(@QueryParam("query") final String query) {
        if (!isComplex(query)) {
            return "Simple result for " + query; // process simple queries synchronously
        }

        ctx.suspend(); // programmatic suspend
        Executors.newSingleThreadExecutor().submit(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LongRunningAsyncOperationResource.class.getName()).log(Level.SEVERE, "Response processing interrupted", ex);
                }
                ctx.resume("Complex result for " + query);
            }
        });

        return null; // return value ignored for suspended requests
    }

    private boolean isComplex(String query) {
        return new Random().nextBoolean();
    }

    @GET
    @Path("timeoutOverriden")
    @Suspend(timeOut = 15000) // default time unit is milliseconds
    public void timeoutValueConflict_OverridingExample(@QueryParam("timeOut") Long timeOut, @QueryParam("timeUnit") TimeUnit timeUnit) {
        Executors.newSingleThreadExecutor().submit(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LongRunningAsyncOperationResource.class.getName()).log(Level.SEVERE, "Response processing interrupted", ex);
                }
                ctx.resume("Hello async world!");
            }
        });
        if (timeOut != null && timeUnit != null) {
            ctx.setSuspendTimeout(timeOut, timeUnit); // time-out values specified in the @Suspend annotation are overriden
        } else {
            // suspend using annotation values
        }
    }

    @GET
    @Path("suspendHandleUsage")
    public void suspendHandleUsageExample() {
        Executors.newSingleThreadExecutor().submit(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LongRunningAsyncOperationResource.class.getName()).log(Level.SEVERE, "Response processing interrupted", ex);
                }
                ctx.resume("Hello async world!");
            }
        });

        ctx.suspend();

        Executors.newSingleThreadExecutor().submit(new Runnable() {

            @Override
            public void run() {
                while (!ctx.isDone()) {
                }
                Logger.getLogger(LongRunningAsyncOperationResource.class.getName()).log(Level.INFO, "Context resumed with a response!");
            }
        });
    }
}
