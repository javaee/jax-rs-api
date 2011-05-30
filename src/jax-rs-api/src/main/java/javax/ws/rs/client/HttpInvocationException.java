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

/**
 * A runtime exception thrown during the HTTP request invocation processing,
 * that signals a failure to process the HTTP request or response. The exception
 * message or nested {@link Throwable} cause SHOULD contain additional information
 * about the reason of the processing failure. The exception is also thrown when
 * the status code of the returned HTTP response indicates a response that is not
 * expected.
 * <p />
 * This exception is typically thrown by the {@link ClientRequest} HTTP invocation
 * methods. Additionally, the exception is also thrown by {@link ClientResponse}
 * {@code getEntity(...)} methods in case the returned response is
 * HTTP 204 (No Content).
 *
 * @author Marek Potociar
 * @since 2.0
 */
public class HttpInvocationException extends ClientException {

    private static final long serialVersionUID = -8551966770517714263L;
    private transient final ClientResponse response;

    /**
     * Construct a uniform interface exception.
     * <p>
     * The client response entity will be buffered by calling
     * {@link ClientResponse#bufferEntity() }.
     *
     * @param response the client response. The message of the exception is set to
     *        r.toString();
     */
    public HttpInvocationException(final ClientResponse response) {
        this(response, true);
    }

    /**
     * Construct a uniform interface exception.
     *
     * @param response the client response. The message of the exception is set to
     *        r.toString();
     * @param bufferResponseEntity if true buffer the client response entity by calling
     *                             {@link ClientResponse#bufferEntity() }.
     */
    public HttpInvocationException(final ClientResponse response, final boolean bufferResponseEntity) {
        super(response.toString());
        if (bufferResponseEntity) {
            response.bufferEntity();
        }
        this.response = response;
    }

    /**
     * Construct a uniform interface exception.
     * <p>
     * The client response entity will be buffered by calling
     * {@link ClientResponse#bufferEntity() }.
     *
     * @param message the message of the exception.
     * @param response the client response.
     *
     */
    public HttpInvocationException(final String message, final ClientResponse response) {
        this(message, response, true);
    }

    /**
     * Construct a uniform interface exception.
     *
     * @param message the message of the exception.
     * @param response the client response.
     * @param bufferResponseEntity if true buffer the client response entity by calling
     *                             {@link ClientResponse#bufferEntity() }.
     *
     */
    public HttpInvocationException(final String message,
            final ClientResponse response,
            final boolean bufferResponseEntity) {
        super(message);
        if (bufferResponseEntity) {
            response.bufferEntity();
        }
        this.response = response;
    }

    /**
     * Get the client response associated with the exception.
    
     * @return the client response.
     */
    public ClientResponse getResponse() {
        return response;
    }
}
