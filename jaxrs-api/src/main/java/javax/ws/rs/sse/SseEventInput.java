/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2013 Oracle and/or its affiliates. All rights reserved.
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
package javax.ws.rs.sse;

import java.io.Closeable;

/**
 * Inbound Server-Sent Events stream.
 * <p>
 * The input stream lets you serially read & consume SSE events as they arrive.
 *
 * @author Marek Potociar (marek.potociar at oracle.com)
 */
public interface SseEventInput extends Closeable {

    /**
     * Check if the chunked input has been closed.
     *
     * @return {@code true} if this chunked input has been closed, {@code false} otherwise.
     */
    boolean isClosed();

    /**
     * Read next SSE event from the response stream and convert it to a Java instance of {@link InboundSseEvent} type.
     * The method returns {@code null} if the underlying entity input stream has been closed (either implicitly or explicitly
     * by calling the {@link #close()} method).
     * <p>
     * Note: This operation is not thread-safe and has to be explicitly synchronized in case it is used from
     * multiple threads.
     * </p>
     *
     * @return next streamed event or {@code null} if the underlying entity input stream has been closed while reading
     * the next event data.
     * @throws IllegalStateException in case this chunked input has been closed.
     */
    InboundSseEvent read() throws IllegalStateException;


}
