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

package jaxrs.examples.client.spec;

import java.util.concurrent.Future;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.SyncInvoker;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import static javax.ws.rs.client.Entity.entity;

import javax.xml.bind.annotation.XmlRootElement;

import jaxrs.examples.client.custom.ThrottledClient;

/**
 * @author Bill Burke
 * @author Marek Potociar
 */
public class SpecExamples {

    @XmlRootElement
    public static class Customer {

        private final String name;

        public Customer(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public void clientBootstrapping() {
        // Default client instantiation using default configuration
        Client defaultClient = ClientBuilder.newClient();
        defaultClient.property("CUSTOM_PROPERTY", "CUSTOM_VALUE");
        assert defaultClient != null;

        // Default client instantiation using custom configuration

        Client defaultConfiguredClient = ClientBuilder.newClient(defaultClient.getConfiguration());
        assert defaultConfiguredClient != null;

        ///////////////////////////////////////////////////////////

        // Custom client instantiation examples
        ThrottledClient myClient = new ThrottledClient();
        assert myClient != null;

        ThrottledClient myConfiguredClient = new ThrottledClient(10);
        assert myConfiguredClient != null;
    }

    public void fluentMethodChaining() {
        Client client = ClientBuilder.newClient();
        Response res = client.target("http://example.org/hello")
                .request("text/plain").get();

        Response res2 = client.target("http://example.org/hello")
                .queryParam("MyParam", "...")
                .request("text/plain")
                .header("MyHeader", "...")
                .get();
    }

    public void typeRelationships() {
        Client client = ClientBuilder.newClient();
        WebTarget uri = client.target("");
        Invocation.Builder builder = uri.request("text/plain");

        SyncInvoker syncInvoker = builder;
        AsyncInvoker asyncInvoker = builder.async();
        Invocation inv = builder.buildGet();

        Response r1 = builder.get();
        Response r2 = syncInvoker.get();
        Response r3 = inv.invoke();

        Future<Response> fr1 = asyncInvoker.get();
        Future<Response> fr2 = inv.submit();
    }

    public void benefitsOfResourceUri() {
        Client client = ClientBuilder.newClient();
        WebTarget base = client.target("http://example.org/");
        WebTarget hello = base.path("hello").path("{whom}");
        final WebTarget whomToGreet = hello.resolveTemplate("whom", "world");
        Response res = whomToGreet.request().get();
    }

    public void gettingAndPostingCustomers() {
        Client client = ClientBuilder.newClient();
        Customer c = client.target("http://examples.org/customers/123")
                .request("application/xml").get(Customer.class);
        Response res = client.target("http://examples.org/premium-customers/")
                .request().post(entity(c, "application/xml"));
    }

    public void asyncSamples() throws Exception {
        Client client = ClientBuilder.newClient();
        Future<Customer> fc = client.target("http://examples.org/customers/123")
                .request("application/xml").async().get(Customer.class);
        Customer c = fc.get();
    }
}
