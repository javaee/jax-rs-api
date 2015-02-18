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

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import jaxrs.examples.link.clusterservice.Machine.Status;

/**
 * MachineResource class.
 *
 * @author Santiago.Pericas-Geertsen@oracle.com
 */
@Path("/cluster/machine/{name}")
public class MachineResource {

    @Context
    private UriInfo uriInfo;

    private Machine machine;

    @GET
    @Produces({"application/json"})
    public Response self(@PathParam("name") String name) {
        machine = getMachine(name);
        return Response.ok(machine).links(getTransitionalLinks()).build();
    }

    @POST
    @Path("starter")
    @Produces({"application/json"})
    public Response starter(@PathParam("name") String name) {
        machine = getMachine(name);
        machine.setStatus(Status.STARTED);
        return Response.ok(machine).links(getTransitionalLinks()).build();
    }

    @POST
    @Path("stopper")
    @Produces({"application/json"})
    public Response stopper(@PathParam("name") String name) {
        machine = getMachine(name);
        machine.setStatus(Status.STOPPED);
        return Response.ok(machine).links(getTransitionalLinks()).build();
    }

    @POST
    @Path("suspender")
    @Produces({"application/json"})
    public Response suspender(@PathParam("name") String name) {
        machine = getMachine(name);
        machine.setStatus(Status.SUSPENDED);
        return Response.ok(machine).links(getTransitionalLinks()).build();
    }

    private Machine getMachine(String name) {
        Machine m = Model.getMachine(name);
        if (m == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return m;
    }

    private Link[] getTransitionalLinks() {
        String name = machine.getName();
        URI uri = uriInfo.getRequestUri();
        URI baseUri = uriInfo.getBaseUri();

        Link self = Link.fromMethod(getClass(), "self").baseUri(baseUri)
                .rel("self").buildRelativized(uri, name);
        Link starter = Link.fromMethod(getClass(), "starter").baseUri(baseUri)
                .rel("starter").buildRelativized(uri, name);
        Link stopper = Link.fromMethod(getClass(), "stopper").baseUri(baseUri)
                .rel("stopper").buildRelativized(uri, name);
        Link suspender = Link.fromMethod(getClass(), "suspender").baseUri(baseUri)
                .rel("suspender").buildRelativized(uri, name);

        switch (machine.getStatus()) {
            case STOPPED:
                return new Link[]{self, starter};
            case STARTED:
                return new Link[]{self, stopper, suspender};
            case SUSPENDED:
                return new Link[]{self, starter};
            default:
                throw new IllegalStateException();
        }
    }
}
