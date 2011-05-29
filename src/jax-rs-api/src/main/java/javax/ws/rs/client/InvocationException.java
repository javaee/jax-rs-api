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
 * A runtime exception thrown when the status code of the returned HTTP response 
 * indicates a response that is not expected.
 * <p />
 * This exception is typically thrown by {@link HttpMethodInvoker} methods. It is 
 * also thrown by any of the {@code get(...)} methods of the typed response 
 * {@link java.util.concurrent.Future} returned from {@link AsyncHttpMethodInvoker} 
 * methods. Additionally, the exception is also thrown by 
 * {@link ClientResponse} {@code getEntity(...)} methods in case the returned response
 * is HTTP 204 (No Content).
 * 
 * @author Paul Sandoz
 * @author Marek Potociar
 * @since 2.0
 */
public class InvocationException extends RuntimeException {

    private static final long serialVersionUID = -8551966770517714263L;

    transient private final ClientResponse r;

    /**
     * Construct a uniform interface exception.
     * <p>
     * The client response entity will be buffered by calling
     * {@link ClientResponse#bufferEntity() }.
     *
     * @param r the client response. The message of the exception is set to
     *        r.toString();
     */
    public InvocationException(ClientResponse r) {
        this(r, true);
    }

    /**
     * Construct a uniform interface exception.
     *
     * @param r the client response. The message of the exception is set to
     *        r.toString();
     * @param bufferResponseEntity if true buffer the client response entity by calling
     *                             {@link ClientResponse#bufferEntity() }.
     */
    public InvocationException(ClientResponse r, boolean bufferResponseEntity) {
        super(r.toString());
        if (bufferResponseEntity) {
            r.bufferEntity();
        }
        this.r = r;
    }

    /**
     * Construct a uniform interface exception.
     * <p>
     * The client response entity will be buffered by calling
     * {@link ClientResponse#bufferEntity() }.
     *
     * @param message the message of the exception.
     * @param r the client response.
     *
     */
    public InvocationException(String message, ClientResponse r) {
        this(message, r, true);
    }

    /**
     * Construct a uniform interface exception.
     *
     * @param message the message of the exception.
     * @param r the client response.
     * @param bufferResponseEntity if true buffer the client response entity by calling
     *                             {@link ClientResponse#bufferEntity() }.
     *
     */
    public InvocationException(String message, ClientResponse r, boolean bufferResponseEntity) {
        super(message);
        if (bufferResponseEntity) {
            r.bufferEntity();
        }
        this.r = r;
    }

    /**
     * Get the client response associated with the exception.

     * @return the client response.
     */
    public ClientResponse getResponse() {
        return r;
    }
}
