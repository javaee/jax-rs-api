/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011 Oracle and/or its affiliates. All rights reserved.
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
package javax.ws.rs.ext;

import javax.ws.rs.core.HttpRequest;
import javax.ws.rs.core.HttpResponse;

/**
 * Context class used by instances of {@link javax.ws.rs.ext.RequestFilter}
 * and {@link javax.ws.rs.ext.ResponseFilter}. 
 * 
 * @author Santiago Pericas-Geertsen
 * @author Bill Burke
 * @since 2.0
 */
public interface FilterContext extends BaseContext {

    /**
     * Enumeration to control how the filter chain is processed.
     * A filter can return {@link #NEXT} to continue the filter chain 
     * or {@link #STOP} to abort it.
     */
    public enum FilterAction { STOP, NEXT };

    /**
     * Get instance of HTTP request object. 
     * 
     * @return Request object being filtered
     */
    HttpRequest getRequest();

    /**
     * Get instance of HTTP response object. May return null in a
     * {@link javax.ws.rs.ext.RequestFilter} unless set via
     * {@link #setResponse(javax.ws.rs.core.HttpResponse)}.
     * 
     * @return Response object being filtered
     */
    HttpResponse getResponse();

    /**
     * Set an HTTP response object in the context. A caching filter
     * could set a response by calling this method and returning
     * {@link javax.ws.rs.ext.FilterContext.FilterAction#STOP} to
     * stop the filter chain.
     * 
     * @param res Response object to be set
     */
    void setResponse(HttpResponse res);

    /**
     * Create a fresh HTTP response object. A caching filter
     * could call this method to get an HTTP response object and
     * initialize it from a cache. This method does not update
     * the state of the context object.
     * 
     * @return Newly created HTTP response object
     * @see #setResponse(javax.ws.rs.core.HttpResponse) 
     */
    HttpResponse createResponse();
}
