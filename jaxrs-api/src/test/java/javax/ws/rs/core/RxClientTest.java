/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2017 Oracle and/or its affiliates. All rights reserved.
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

package javax.ws.rs.core;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.CompletionStageRxInvoker;
import javax.ws.rs.client.RxInvokerProvider;
import javax.ws.rs.client.SyncInvoker;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Class RxClientTest.
 *
 * @author Santiago Pericas-Geertsen
 */
public class RxClientTest {

    private Client client = null;       // does not run

    /**
     * Shows how to use the default reactive invoker by calling method {@link
     * javax.ws.rs.client.Invocation.Builder#rx()} without any arguments.
     */
    @Test
    @Ignore
    public void testRxClient() {
        CompletionStage<List<String>> cs =
                client.target("remote/forecast/{destination}")
                      .resolveTemplate("destination", "mars")
                      .request()
                      .header("Rx-User", "Java8")
                      .rx()                               // gets CompletionStageRxInvoker
                      .get(new GenericType<List<String>>() {
                      });

        cs.thenAccept(System.out::println);
    }

    /**
     * Shows how other reactive invokers could be plugged in using the class
     * as an argument in {@link javax.ws.rs.client.Invocation.Builder#rx(Class)}.
     */
    @Test
    @Ignore
    public void testRxClient2() {
        Client rxClient = client.register(CompletionStageRxInvokerProvider.class, RxInvokerProvider.class);

        CompletionStage<List<String>> cs =
                rxClient.target("remote/forecast/{destination}")
                        .resolveTemplate("destination", "mars")
                        .request()
                        .header("Rx-User", "Java8")
                        .rx(CompletionStageRxInvoker.class)
                        .get(new GenericType<List<String>>() {
                        });

        cs.thenAccept(System.out::println);
    }

    /**
     * Shows how other reactive invokers could be plugged in using the class instance
     * as an argument in {@link javax.ws.rs.client.Invocation.Builder#rx(Class)}.
     */
    @Test
    @Ignore
    public void testRxClient3() {
        Client rxClient = client.register(CompletionStageRxInvokerProvider.class, RxInvokerProvider.class);

        CompletionStage<String> cs =
                rxClient.target("remote/forecast/{destination}")
                        .resolveTemplate("destination", "mars")
                        .request()
                        .header("Rx-User", "Java8")
                        .rx(CompletionStageRxInvoker.class)
                        .get(String.class);

        cs.thenAccept(System.out::println);
    }

    /**
     * RxInvokerProvider provided by the app/other framework.
     */
    public static class CompletionStageRxInvokerProvider implements RxInvokerProvider<CompletionStageRxInvoker> {

        @Override
        public boolean isProviderFor(Class<?> clazz) {
            return CompletionStageRxInvoker.class.equals(clazz);
        }

        @Override
        public CompletionStageRxInvoker getRxInvoker(SyncInvoker syncInvoker, ExecutorService executorService) {
            return null;
        }
    }
}
