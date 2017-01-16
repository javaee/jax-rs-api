/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011-2017 Oracle and/or its affiliates. All rights reserved.
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

import java.nio.ByteBuffer;
import java.util.function.Consumer;

import javax.ws.rs.Flow;
import javax.ws.rs.core.Response;

/**
 * Uniform interface for NIO invocation of HTTP methods.
 *
 * @author Santiago Pericas-Geertsen
 * @since 2.1
 */
public interface NioInvoker {

    // POST
    Response post(Consumer<Flow.Subscriber<ByteBuffer>> writeHandler);

    // returning publisher might not work well; it could lose some data if the responseEntity
    // is not subscribed before there publisher is notified about the first chunk of response
    // entity
    void post(Flow.Publisher<ByteBuffer> requestEntity, Flow.Subscriber<ByteBuffer> responseEntity);

    // sending a stream of pojos
    Response post(Flow.Publisher<?> requestEntity);

    // consuming a stream of pojos -- lazy publishers (not sure whether this approach is correct in all possible scenarios)
    <T> Flow.Publisher<T> post(Class<T> entityType);

    // consuming a stream of pojos
    <T> void post(Class<T> entityType, Flow.Subscriber<T> entitySubscriber);
    <T> void post(Class<T> entityType, Flow.Subscriber<T> entitySubscriber, Flow.Subscriber<T>... subscribers);

    Response get();

    Response put(Consumer<Flow.Subscriber<ByteBuffer>> writeHandler);

    Response delete();

    Response head();

    Response options();

    Response trace();

    Response method(String name);

    Response method(String name, Consumer<Flow.Subscriber<ByteBuffer>> writeHandler);

}
