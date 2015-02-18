/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011-2015 Oracle and/or its affiliates. All rights reserved.
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

package jaxrs.examples.link.clusterservice;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;

/**
 * ClientTest class.
 *
 * @author Santiago.Pericas-Geertsen@oracle.com
 */
public class ClientTest {

    public void test() {
        Client client = ClientBuilder.newClient();

        // Get cluster representation -- entry point
        Response rc = client.target("/cluster").request("application/json").get();

        // Ensure cluster is online
        if (rc.hasLink("onliner")) {
            client.invocation(rc.getLink("onliner")).buildPost(null).invoke();
        }

        // Start all machines in cluster
        Cluster c = rc.readEntity(Cluster.class);
        for (Machine m : c.getMachines()) {
            // Machine name is need for URI template in link
            Link l = rc.getLinkBuilder("item").build(m.getName());

            // Create invocation from link and call invoke()
            Response rm = client.invocation(l).buildGet().invoke();

            // Start machine if not started already
            if (rm.hasLink("starter")) {
                client.invocation(rm.getLink("starter")).buildPost(null).invoke();
            }
        }
    }
}
