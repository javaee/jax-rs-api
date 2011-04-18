/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
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
package javax.ws.rs.client;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * A future listener that can be registered to receive an event when producing 
 * a {@code Future} result has reached the completed termination state due to
 * the normal termination, an exception or a cancellation.
 * 
 * @param <T> the result type held by the future.
 * 
 * @author Paul Sandoz
 * @author Marek Potociar
 * @see AsyncClientHandler
 * @since 2.0
 */
public interface FutureListener<T> {

    /**
     * Invoked when a {@link Future} has reached the completed termination state.
     * <p>
     * The catching of an {@link ExecutionException} when {@code Future.get(...)}
     * is invoked may be utilized to determine if the future terminated with an
     * exception. The original exception can be obtained by invoking
     * {@link ExecutionException#getCause()}.
     * <p>
     * The catching of a {@link CancellationException} when {@code Future.get}
     * is invoked may be utilized to determine if the result computation was
     * canceled.
     *
     * @param future the completed Future. Invocation of {@link Future#isDone() }
     *    will return true. Since the future is complete, invocation of 
     *    {@link Future#get() } or {@link Future#get(long, java.util.concurrent.TimeUnit) }
     *    will not result in the throwing of an {@link InterruptedException}.
     * @throws InterruptedException this exception is declared so that the developer
     *     does not need to catch it when invoking one of the {@code Future.get}
     *     methods.
     */
    void onComplete(Future<T> future) throws InterruptedException;
}
