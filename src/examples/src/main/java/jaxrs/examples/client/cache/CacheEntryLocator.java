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
package jaxrs.examples.client.cache;

import javax.ws.rs.client.ClientRequest;
import javax.ws.rs.client.ClientResponse;
import javax.ws.rs.ext.interceptor.RequestContext;
import javax.ws.rs.ext.interceptor.RequestHandler;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.Map;

/**
 * @author Bill Burke
 * @author Marek Potociar
 */
public class CacheEntryLocator implements RequestHandler<ClientRequest, ClientResponse> {

    private Map<String, CacheEntry> cache;

    public CacheEntryLocator(Map<String, CacheEntry> cache) {
        this.cache = cache;
    }

    public ClientResponse handle(RequestContext<ClientRequest, ClientResponse> context) {
        if (!context.getRequest().getMethod().equalsIgnoreCase("GET")) {
            return context.proceed();
        }
        URI uri = context.getRequest().getURI();
        CacheEntry entry = cache.get(uri.toString());

        if (entry == null) {
            return context.proceed();
        }

        ClientResponse response = context.createResponse();
        response.getHeaders().putAll(entry.getHeaders());
        response.setEntityInputStream(new ByteArrayInputStream(entry.getBody()));
        response.setStatusCode(200);
        return response;
    }
}