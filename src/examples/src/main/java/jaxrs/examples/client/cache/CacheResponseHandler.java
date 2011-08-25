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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.ws.rs.core.HttpResponse;
import javax.ws.rs.ext.FilterContext;
import javax.ws.rs.ext.FilterContext.FilterAction;
import javax.ws.rs.ext.ResponseFilter;

/**
 * @author Bill Burke
 * @author Marek Potociar
 * @author Santiago Pericas-Geertsen
 */
public class CacheResponseHandler implements ResponseFilter {

    private Map<String, CacheEntry> cache;
    AtomicBoolean enabledFlag;

    public CacheResponseHandler(Map<String, CacheEntry> cache, AtomicBoolean enabled) {
        this.cache = cache;
        this.enabledFlag = enabled;
    }

    @Override
    public FilterAction postFilter(FilterContext ctx) throws IOException {
        if (enabledFlag.get() && ctx.getRequest().getMethod().equalsIgnoreCase("GET")) {
            URI uri = ctx.getRequest().getUri();
            HttpResponse response = ctx.getResponse();
            byte[] body = readFromStream(1024, response.getEntityInputStream());
            CacheEntry entry = new CacheEntry(response.getHeaderMap(), body);
            cache.put(uri.toString(), entry);
            response.setEntityInputStream(new ByteArrayInputStream(body));
        }
        return FilterAction.NEXT;
    }

    public static byte[] readFromStream(int bufferSize, InputStream entityStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[bufferSize];
        int wasRead = 0;
        do {
            try {
                wasRead = entityStream.read(buffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (wasRead > 0) {
                baos.write(buffer, 0, wasRead);
            }
        } while (wasRead > -1);
        return baos.toByteArray();
    }
}
