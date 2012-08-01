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

import javax.ws.rs.ext.RuntimeDelegate;

/**
 * A JAX-RS asynchronous response that provides access to asynchronous server side
 * response manipulation.
 * <p>
 * A new instance of {@code AsynchronousResponse} is created by a call to a static
 * {@code AsynchronousResponse.suspend(...)} method. The returned asynchronous response instance
 * is bound to the running request and can be used to asynchronously provide the request processing
 * result or otherwise manipulate the suspended client connection. The available operations include:
 * <ul>
 * <li>setting a default fall-back response (e.g. in case of a time-out event etc.)</li>
 * <li>resuming the suspended request processing</li>
 * <li>cancel the suspended request processing</li>
 * </ul>
 * </p>
 * <p>
 * Following example demonstrates the use of the {@code AsynchronousResponse} for asynchronous
 * HTTP request processing:
 * </p>
 * <pre>
 * &#64;Path("/messages/next")
 * public class SimpleAsyncEventResource {
 *     private static final BlockingQueue&lt;AsynchronousResponse&gt; suspended =
 *             new ArrayBlockingQueue&lt;AsynchronousResponse&gt;(5);
 *
 *     &#64;GET
 *     public AsynchronousResponse pickUpMessage() throws InterruptedException {
 *         final AsynchronousResponse ar = AsynchronousResponse.suspend();
 *         suspended.put(ar);
 *         return ar;
 *     }
 *
 *     &#64;POST
 *     public String postMessage(final String message) throws InterruptedException {
 *         final AsynchronousResponse ar = suspended.take();
 *         ar.resume(message); // resumes the processing of one GET request
 *         return "Message sent";
 *     }
 * }
 * </pre>
 *
 * @author Marek Potociar (marek.potociar at oracle.com)
 * @since 2.0
 */
public abstract class AsynchronousResponse {
    /**
     * Constant specifying no suspend timeout value.
     */
    public static final long NEVER = 0;

    /**
     * Suspend the running request processing and create a new asynchronous response
     * for the suspended request.
     * <p>
     * While the asynchronous response returned from this method  is still suspended,
     * the suspend timeout value may be updated using the {@link #setSuspendTimeout(long, TimeUnit)}
     * method.
     * </p>
     * <p>
     * The method may only be invoked from within the context of a running
     * {@link javax.ws.rs.HttpMethod JAX-RS resource method} that has not been
     * previously suspended.
     * </p>
     *
     * @return suspended asynchronous response for the running request.
     * @throws IllegalStateException in case the method is not invoked from within
     *                               a context of a running request that was not yet
     *                               suspended.
     * @see #suspend(long)
     * @see #suspend(long, java.util.concurrent.TimeUnit)
     */
    public static AsynchronousResponse suspend() throws IllegalStateException {
        return RuntimeDelegate.getInstance().createAsynchronousResponse(NEVER, TimeUnit.MILLISECONDS);
    }

    /**
     * Suspend the running request processing and create a new asynchronous response
     * for the suspended request.
     * <p>
     * While the asynchronous response returned from this method  is still suspended,
     * the suspend timeout value may be updated using the {@link #setSuspendTimeout(long, TimeUnit)}
     * method.
     * </p>
     * <p>
     * The method may only be invoked from within the context of a running
     * {@link javax.ws.rs.HttpMethod JAX-RS resource method} that has not been
     * previously suspended.
     * </p>
     *
     * @param millis suspend timeout value in milliseconds. Value lower
     *               or equal to 0 causes the response to suspend indefinitely.
     * @return suspended asynchronous response for the running request.
     * @throws IllegalStateException in case the method is not invoked from within
     *                               a context of a running request that was not yet
     *                               suspended.
     * @see #suspend()
     * @see #suspend(long, java.util.concurrent.TimeUnit)
     */
    public static AsynchronousResponse suspend(long millis) throws IllegalStateException {
        return RuntimeDelegate.getInstance().createAsynchronousResponse(millis, TimeUnit.MILLISECONDS);
    }

    /**
     * Suspend the running request processing and create a new asynchronous response
     * for the suspended request.
     * <p>
     * While the asynchronous response returned from this method  is still suspended,
     * the suspend timeout value may be updated using the {@link #setSuspendTimeout(long, TimeUnit)}
     * method.
     * </p>
     * <p>
     * The method may only be invoked from within the context of a running
     * {@link javax.ws.rs.HttpMethod JAX-RS resource method} that has not been
     * previously suspended.
     * </p>
     *
     * @param time suspend timeout value in the give time {@code unit}. Value lower
     *             or equal to 0 causes the context to suspend indefinitely.
     * @param unit suspend timeout value time unit
     * @return suspended asynchronous response for the running request.
     * @throws IllegalStateException in case the method is not invoked from within
     *                               a context of a running request that was not yet
     *                               suspended.
     * @see #suspend()
     * @see #suspend(long)
     */
    public static AsynchronousResponse suspend(long time, TimeUnit unit) throws IllegalStateException {
        return RuntimeDelegate.getInstance().createAsynchronousResponse(time, unit);
    }

    /**
     * Resume the suspended request processing using the provided response data.
     * <p>
     * The provided response data can be of any Java type that can be
     * returned from a {@link javax.ws.rs.HttpMethod JAX-RS resource method}.
     * The processing of the data by JAX-RS framework follows the same path as
     * it would for the response data returned synchronously by a JAX-RS resource
     * method.
     * The asynchronous response must be still in a {@link #isSuspended() suspended} state
     * for this method to succeed.
     * </p>
     *
     * @param response data to be sent back in response to the suspended request.
     * @throws IllegalStateException in case the response is not {@link #isSuspended() suspended}.
     * @see #resume(Throwable)
     */
    public abstract void resume(Object response) throws IllegalStateException;

    /**
     * Resume the suspended request processing using the provided throwable.
     *
     * <p>
     * For the provided throwable same rules apply as for an exception thrown
     * by a {@link javax.ws.rs.HttpMethod JAX-RS resource method}.
     * The processing of the throwable by JAX-RS framework follows the same path
     * as it would for any exception thrown by a JAX-RS resource method.
     * The asynchronous response must be still in a {@link #isSuspended() suspended} state
     * for this method to succeed.
     * </p>
     *
     * @param response an exception to be raised in response to the suspended
     *                 request.
     * @throws IllegalStateException in case the response is not {@link #isSuspended() suspended}.
     * @see #resume(Object)
     */
    public abstract void resume(Throwable response) throws IllegalStateException;

    /**
     * Set/update the suspend timeout.
     * <p>
     * The new suspend timeout values override any timeout value previously specified.
     * The asynchronous response must be still in a {@link #isSuspended() suspended} state
     * for this method to succeed.
     * </p>
     *
     * @param time suspend timeout value in the give time {@code unit}. Value lower
     *             or equal to 0 causes the context to suspend indefinitely.
     * @param unit suspend timeout value time unit.
     * @throws IllegalStateException in case the response is not {@link #isSuspended() suspended}.
     */
    public abstract void setSuspendTimeout(long time, TimeUnit unit) throws IllegalStateException;

    /**
     * Cancel the suspended request processing.
     *
     * This method causes that the underlying suspended client connection is closed eventually.
     * <p>
     * If a {@link #setFallbackResponse(Object) fallback response} was set, the fallback response
     * is returned to the client. In case no fallback response has been set,  JAX-RS implementations
     * should indicate that the request processing has been cancelled by sending back a
     * {@link Response.Status#INTERNAL_SERVER_ERROR HTTP 500} error response.
     * </p>
     * <p>
     * Invoking this method multiple times has the same effect as invoking it only once.
     * Invoking this method on an asynchronous response instance that has already been
     * resumed has no effect and the method call is ignored. Once the request is canceled,
     * any attempts to suspend or resume the execution context will result in an
     * {@link IllegalStateException} being thrown.
     * </p>
     */
    public abstract void cancel();

    /**
     * Check if the asynchronous response instance is in a suspended state.
     *
     * Method returns {@code true} if this asynchronous response is still suspended and has
     * not finished processing yet (either by resuming or canceling the response).
     *
     * @return {@code true} if this asynchronous response is in a suspend state, {@code false}
     *         otherwise.
     * @see #isCancelled()
     * @see #isDone()
     */
    public abstract boolean isSuspended();

    /**
     * Check if the asynchronous response instance has been cancelled.
     *
     * Method returns {@code true} if this asynchronous response has been canceled before
     * completion.
     *
     * @return {@code true} if this task was canceled before completion.
     * @see #isSuspended()
     * @see #isDone()
     */
    public abstract boolean isCancelled();

    /**
     * Check if the processing of a request this asynchronous response instance belongs to
     * has finished.
     *
     * Method returns {@code true} if the processing of a request this asynchronous response
     * is bound to is finished.
     * <p>
     * The request processing may be finished due to a normal termination, a suspend timeout, or
     * cancellation -- in all of these cases, this method will return {@code true}.
     * </p>
     *
     * @return {@code true} if this execution context has finished processing.
     * @see #isSuspended()
     * @see #isCancelled()
     */
    public abstract boolean isDone();

    /**
     * Set a default fall-back response to be used in case the suspended request
     * execution does not terminate normally via a call to {@code resume(...)} method
     * (e.g. is cancelled or times out).
     * <p>
     * The provided response data can be of any Java type that can be
     * returned from a {@link javax.ws.rs.HttpMethod JAX-RS resource method}.
     * If used, the processing of the data by JAX-RS framework follows the same
     * path as it would for the response data returned synchronously by a JAX-RS
     * resource method.
     * </p>
     *
     * @param response data to be sent back to the client in case the suspended
     *                 request is cancelled or times out.
     * @see #getFallbackResponse
     */
    public abstract void setFallbackResponse(Object response);

    /**
     * Get a default fall-back response to be send back to the client in case the
     * suspended request execution is cancelled or times out. The method may return {@code null}
     * if no default response was set in the execution context.
     *
     * @return default fall-back response to be sent back to the client in case the
     *         suspended request execution is cancelled or times out. Returns {@code null} if no default
     *         response was set.
     * @see #setFallbackResponse
     */
    public abstract Response getFallbackResponse();
}
