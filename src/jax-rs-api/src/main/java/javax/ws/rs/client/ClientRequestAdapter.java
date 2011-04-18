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

import java.io.IOException;
import java.io.OutputStream;

/**
 * Adapter for the client request to adapt the output stream to write the
 * entity. This mechanism can be used to enable logging or compression of
 * the request entity.
 * 
 * @author Paul Sandoz
 * @author Marek Potociar
 * @since 2.0
 */
public interface ClientRequestAdapter {

    /**
     * Adapt the output stream of the client request.
     * 
     * @param request the client request
     * @param out the output stream to write the request entity.
     * @return the adapted output stream to write the request entity.
     * @throws java.io.IOException in case of any I/O issues with adapting the
     *     existing client request output stream. This method declares the
     *     {@link IOException} so that the client request adapter implementations
     *     do not have to catch and wrap the I/O exceptions.
     */
    OutputStream adapt(ClientRequest request, OutputStream out) throws IOException;
}
