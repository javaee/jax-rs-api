/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2015 Oracle and/or its affiliates. All rights reserved.
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
package jaxrs.examples.nio;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FileResourceClient class.
 *
 * @author Santiago Pericas-Geertsen
 */
public class FileResourceClient {

    private static final int FOUR_KB = 4 * 1024;

    private static Map<String, byte[]> files = new ConcurrentHashMap<String, byte[]>();

    public void uploadClient(String path) {
        final Client client = ClientBuilder.newClient();
        final ByteArrayInputStream in = new ByteArrayInputStream(files.get(path));
        final byte[] buffer = new byte[FOUR_KB];

        client.target("/file").request(MediaType.APPLICATION_OCTET_STREAM).nio().post(
                out -> {              // writer handler
                    try {
                        final int n = in.read(buffer);
                        if (n >= 0) {
                            out.write(buffer, 0, n);
                            return true;    // more to write
                        }
                        in.close();
                        return false;       // we're done
                    } catch (IOException e) {
                        throw new WebApplicationException(e);
                    }
                });
    }

    public void downloadClient(String path) {
        final Client client = ClientBuilder.newClient();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] buffer = new byte[FOUR_KB];

        client.target("/file").request().accept(MediaType.APPLICATION_OCTET_STREAM).nio().get(
                in -> {                     // reader handler
                    try {
                        if (in.isFinished()) {
                            files.put(path, out.toByteArray());
                            out.close();
                        } else {
                            final int n = in.read(buffer);
                            out.write(buffer, 0, n);
                        }
                    } catch (IOException e) {
                        throw new WebApplicationException(e);
                    }
                });

    }
}
