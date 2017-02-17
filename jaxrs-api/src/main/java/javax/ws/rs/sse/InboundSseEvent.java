/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2017 Oracle and/or its affiliates. All rights reserved.
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

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

/**
 * Inbound Server-sent event.
 * <p>
 * Used on the client side, when accepting incoming Server-sent events.
 *
 * @author Marek Potociar
 * @since 2.1
 */
public interface InboundSseEvent extends SseEvent {

    /**
     * Check if the event is empty (i.e. does not contain any data).
     *
     * @return {@code true} if current instance does not contain any data, {@code false} otherwise.
     */
    boolean isEmpty();

    /**
     * Get the original event data as {@link String}.
     *
     * @return event data de-serialized into a string.
     * @throws javax.ws.rs.ProcessingException when provided type can't be read. The thrown exception wraps the original cause.
     */
    String readData();

    /**
     * Read event data as a given Java type.
     *
     * @param type Java type to be used for event data de-serialization.
     * @return event data de-serialized as an instance of a given type.
     * @throws javax.ws.rs.ProcessingException when provided type can't be read. The thrown exception wraps the original cause.
     */
    <T> T readData(Class<T> type);

    /**
     * Read event data as a given generic type.
     *
     * @param type generic type to be used for event data de-serialization.
     * @return event data de-serialized as an instance of a given type.
     * @throws javax.ws.rs.ProcessingException when provided type can't be read. The thrown exception wraps the original cause.
     */
    <T> T readData(GenericType<T> type);

    /**
     * Read event data as a given Java type.
     *
     * @param messageType Java type to be used for event data de-serialization.
     * @param mediaType   {@link MediaType media type} to be used for event data de-serialization.
     * @return event data de-serialized as an instance of a given type.
     * @throws javax.ws.rs.ProcessingException when provided type can't be read. The thrown exception wraps the original cause.
     */
    <T> T readData(Class<T> messageType, MediaType mediaType);

    /**
     * Read event data as a given generic type.
     *
     * @param type      generic type to be used for event data de-serialization.
     * @param mediaType {@link MediaType media type} to be used for event data de-serialization.
     * @return event data de-serialized as an instance of a given type.
     * @throws javax.ws.rs.ProcessingException when provided type can't be read. The thrown exception wraps the original cause.
     */
    <T> T readData(GenericType<T> type, MediaType mediaType);

}
