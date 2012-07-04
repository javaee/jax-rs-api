/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
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

import java.util.concurrent.TimeUnit;

/**
 * TODO: javadoc.
 *
 * @author Marek Potociar (marek.potociar at oracle.com)
 */
public interface AsynchronousResponse {
    /**
     * Resume processing of the request bound to the execution context using
     * response data provided.
     *
     * The provided response data can be of any Java type that can be
     * returned from a {@link javax.ws.rs.HttpMethod JAX-RS resource method}.
     * The processing of the data by JAX-RS framework follows the same path as
     * it would for the response data returned synchronously by a JAX-RS resource
     * method.
     *
     * @param response data to be sent back in response to the suspended request.
     * @throws IllegalStateException in case the request has not been
     *                               {@link #isSuspended() suspended}.
     * @see #resume(Throwable)
     */
    void resume(Object response) throws IllegalStateException;

    /**
     * Resume processing of the request bound to the execution context using
     * a throwable.
     *
     * For the provided throwable same rules apply as for an exception thrown
     * by a {@link javax.ws.rs.HttpMethod JAX-RS resource method}.
     * The processing of the throwable by JAX-RS framework follows the same path
     * as it would for any exception thrown by a JAX-RS resource method.
     *
     * @param response an exception to be raised in response to the suspended
     *                 request.
     * @throws IllegalStateException in case the request has not been
     *                               {@link #isSuspended() suspended}.
     * @see #resume(Object)
     */
    void resume(Throwable response) throws IllegalStateException;

    /**
     * Set the new suspend timeout.
     *
     * The new suspend timeout values override any timeout value specified either
     * programmatically via one of the {@code suspend(...)} methods or
     * {@link javax.ws.rs.Suspend declaratively}.
     * The execution context must be suspended for this method to succeed.
     *
     * @param time suspend timeout value in the give time {@code unit}. Value lower
     *             or equal to 0 causes the context to suspend indefinitely.
     * @param unit suspend timeout value time unit.
     * @throws IllegalStateException in case the context has not been suspended.
     */
    void setSuspendTimeout(long time, TimeUnit unit) throws IllegalStateException;

    /**
     * Cancel the request processing.
     * <p>
     * This method causes that the underlying network connection is closed without
     * any response being sent back to the client. Invoking this method multiple
     * times has the same effect as invoking it only once. Invoking this method
     * on a request that has already been resumed has no effect and the method
     * call is ignored.
     * </p>
     * <p>
     * Once the request is canceled, any attempts to suspend or resume the execution
     * context will result in an {@link IllegalStateException} being thrown.
     * </p>
     */
    void cancel();

    /**
     * Returns {@code true} if this execution context has been suspended and has
     * not {finished processing yet.
     *
     * @return {@code true} if this task was canceled before it completed.
     * @see #isCancelled()
     * @see #isDone()
     */
    boolean isSuspended();

    /**
     * Returns {@code true} if this execution context was canceled before it
     * completed normally.
     *
     * @return {@code true} if this task was canceled before it completed.
     * @see #isSuspended()
     * @see #isDone()
     */
    boolean isCancelled();

    /**
     * Returns {@code true} if this execution context has finished processing.
     *
     * Completion may be due to normal termination, a suspend timeout, or
     * cancellation -- in all of these cases, this method will return
     * {@code true}.
     *
     * @return {@code true} if this execution context has finished processing.
     * @see #isSuspended()
     * @see #isCancelled()
     */
    boolean isDone();

    /**
     * Set the default fall-back response to be used in case the suspended request
     * execution fails or times out.
     * <p/>
     * The provided response data can be of any Java type that can be
     * returned from a {@link javax.ws.rs.HttpMethod JAX-RS resource method}.
     * If used, the processing of the data by JAX-RS framework follows the same
     * path as it would for the response data returned synchronously by a JAX-RS
     * resource method.
     *
     * @param response data to be sent back to the client in case the suspended
     *                 request fails or times out.
     * @see #getFallbackResponse
     */
    void setFallbackResponse(Object response);

    /**
     * Returns default fall-back response to be send back to the client in case the
     * suspended request execution fails times out. The method may return {@code null}
     * if no default response was set in the execution context.
     *
     * @return default fall-back response to be sent back to the client in case the
     *         suspended request execution fails times out or {@code null} if no default
     *         response was set.
     * @see #setFallbackResponse
     */
    Response getFallbackResponse();
}
