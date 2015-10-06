/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2015 Oracle and/or its affiliates. All rights reserved.
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
import java.io.IOException;

/**
 * Outbound Server-Sent Events stream.
 *
 * When returned from resource method, the underlying client connection is kept open and the application code
 * is able to send events.
 * A server-side instance implementing the interface corresponds exactly to a single client HTTP connection.
 *
 * @author Marek Potociar (marek.potociar at oracle.com)
 * @since 2.1
 */
public interface SseEventOutput extends Closeable {

    /**
     * Write a new outbound SSE event.
     *
     * @param event an outbound SSE event instance to be written.
     * @throws IOException if this response is closed or when encountered any problem during serializing or writing a chunk.
     */
    void write(final OutboundSseEvent event) throws IOException;

    /**
     * Check if the stream has been closed already.
     *
     * Please note that the client connection represented by this {@code SseEventOutput} can be closed by the client side when
     * a client decides to close connection and disconnect from the server.
     *
     * @return {@code true} when closed, {@code false} otherwise.
     */
    boolean isClosed();
}
