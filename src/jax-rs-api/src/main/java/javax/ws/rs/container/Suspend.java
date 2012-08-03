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
package javax.ws.rs.container;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Inject a suspended {@link AsynchronousResponse} into a parameter of an invoked
 * JAX-RS {@link javax.ws.rs.HttpMethod resource or sub-resource method}.
 *
 * The injected {@code AsynchronousResponse} instance is bound to the processing
 * of the active request and can be used to resume the request processing when
 * a response is available.
 * <p>
 * By default there is {@link AsynchronousResponse#NEVER no suspend timeout set} and
 * the asynchronous response is suspended indefinitely. The suspend timeout can be
 * specified using the annotation values. Declaratively specified timeout can be further
 * programmatically overridden using the {@link AsynchronousResponse#setSuspendTimeout(long, TimeUnit)}
 * method.
 * <p/>
 * <p>
 * This type of {@code AsynchronousResponse} instance provisioning is useful in environments
 * that provide additional support for thread management of asynchronously executed tasks,
 * such as in case of asynchronous EJB methods:
 * </p>
 * <pre>
 *  &#64;Stateless
 *  &#64;Path("/")
 *  public class MyEjbResource {
 *    &#64;GET
 *    &#64;Asynchronous
 *    public void longRunningOperation(&#64;Suspend AsynchronousResponse ar) {
 *      final String result = executeLongRunningOperation();
 *      ar.resume(result);
 *    }
 *
 *    private String executeLongRunningOperation() { &hellip; }
 *  }
 * </pre>
 * <p>
 * A resource or sub-resource method that injects an instance of an
 * {@code AsynchronousResponse} using the {@code &#64;Suspend} annotation is expected
 * be declared to return {@code void} type. Methods that inject asynchronous
 * response instance using the {@code &#64;Suspend} annotation and declare a
 * return type other than {@code void} MUST be detected by the JAX-RS runtime and
 * a warning message MUST be logged. Any response value returned from such resource
 * or sub-resource method MUST be ignored by the framework:
 * </p>
 * <pre>
 * &#64;Path("/messages/next")
 * public class MessagingResource {
 *     &hellip;
 *     &#64;GET
 *     public String readMessage(&#64;Suspend AsynchronousResponse ar) {
 *         suspended.put(ar);
 *         return "This response will be ignored.";
 *     }
 *     &hellip;
 * }
 * </pre>
 *
 * @author Marek Potociar
 * @since 2.0
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Suspend {
    /**
     * Suspend timeout value in the given {@link #timeUnit() time unit}. A default
     * value is {@link AsynchronousResponse#NEVER no timeout}. Similarly, any
     * explicitly set value lower then or equal to zero will be treated as a "no timeout"
     * value.
     */
    long timeOut() default AsynchronousResponse.NEVER;

    /**
     * The suspend timeout time unit. Defaults to {@link java.util.concurrent.TimeUnit#MILLISECONDS}.
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
