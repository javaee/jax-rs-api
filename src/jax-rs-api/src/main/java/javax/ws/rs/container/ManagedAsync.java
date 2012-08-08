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

/**
 * Marks a {@link javax.ws.rs.HttpMethod resource or sub-resource method}
 * for an asynchronous execution managed by the JAX-RS runtime.
 *
 * Resource methods annotated with {@code &#64;ManagedAsync} annotation are
 * asynchronously executed by JAX-RS runtime using a dedicated
 * {@link java.util.concurrent.Executor resource method executor} in a separate
 * thread.
 *
 * <p>
 * In a basic non-managed scenario, JAX-RS users are expected to take care of thread
 * management used for the asynchronous execution on their own. For example:
 * <pre>
 * &#64;GET
 * public void suspendViaAnnotationExample(&#64;Suspended final AsyncResponse ar) {
 *   Executors.newSingleThreadExecutor().submit(new Runnable() {
 *     &#64;Override
 *     public void run() {
 *       // perform long-running result computation
 *       ar.resume(result);
 *     }
 *   });
 * }
 * </pre>
 * Such basic un-managed asynchronous processing is sometimes required or desirable to perform
 * more complex asynchronous execution management or delegate the asynchronous execution to
 * another process, container or runtime.
 * </p>
 * <p>
 * In the managed asynchronous execution scenario, JAX-RS users delegate the asynchronous
 * execution thread management to the JAX-RS runtime. In this case JAX-RS runtime executes
 * the managed method in a separate thread using a dedicated method executor:
 * <pre>
 * &#64;GET
 * &#64;ManagedAsync
 * public void suspendViaAnnotationExample(&#64;Suspended final AsyncResponse ar) {
 *   // perform long-running result computation
 *   ar.resume(result);
 * }
 * </pre>
 * As demonstrated by the example above, managed asynchronous execution makes the asynchronous
 * execution code less typically verbose and easier to write and is suitable for most use cases
 * dealing with long-running asynchronous tasks that produce the response once finished.
 * </p>
 *
 * @author Marek Potociar (marek.potociar at oracle.com)
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ManagedAsync {
}
