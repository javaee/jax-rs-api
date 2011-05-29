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
package jaxrs.examples.client;

import java.util.List;
import java.util.concurrent.Future;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientRequest;
import javax.ws.rs.client.ClientResponse;
import javax.ws.rs.client.AbstractInvocationCallback;
import javax.ws.rs.core.GenericType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Bill Burke
 * @author Marek Potociar
 */
public class BasicExamples {

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

    public void basicResponse() {
        Customer customer;
        ClientResponse response;

        final Client client = Client.create("http://localhost:9095/customers");

        response = client.path("{id}").get() // ClientRequest
                .pathParam("id", 123).invoke();
        customer = response.getEntity(Customer.class);
        assert customer != null;

        response = client.post() // ClientRequest
                .entity(new Customer("Marek")).type("application/xml").invoke();
        assert response.getStatusCode() == 201;
    }

    public void typedResponse() {
        Customer customer;

        customer = Client.create("http://localhost:9095/customers/{id}").get() // ClientRequest
                .pathParam("id", 123).invoke(Customer.class);
        assert customer != null;
    }

    public void typedGenericResponse() {
        List<Customer> customers = Client.create("http://localhost:9095/customers").get() // ClientRequest
                .invoke(new GenericType<List<Customer>>() {
        });
        assert customers != null;
    }

    public void responseUsingSubResourceClient() {
        Client customers = Client.create("http://localhost:9095/customers"); // Client ("http://localhost:9095/customers/")
        Client customer = customers.path("{id}"); // Client ("http://localhost:9095/customers/{id}/")

        // Create a customer
        ClientResponse response = customers.post() // ClientRequest
                .entity(new Customer("Bill")).type("application/xml").invoke();
        assert response.getStatusCode() == 201;

        Customer favorite;
        // view a customer
        favorite = customer.get() // ClientRequest
                .pathParam("id", 123).invoke(Customer.class);
        assert favorite != null;

        // view a customer (alternative)
        favorite = customer.pathParam("id", 123) // Client ("http://localhost:9095/customers/123/")
                .get() // ClientRequest
                .invoke(Customer.class);
        assert favorite != null;
    }

    public void asyncResponse() throws Exception {
        Future<ClientResponse> future = Client.create("http://localhost:9095/customers/{id}").get() // ClientRequest
                .pathParam("id", 123).start();

        ClientResponse response = future.get();
        Customer customer = response.getEntity(Customer.class);
        assert customer != null;
    }

    public void typedAsyncResponse() throws Exception {
        Future<Customer> customer = Client.create("http://localhost:9095/customers/{id}").get() // ClientRequest
                .pathParam("id", 123).start(Customer.class);
        assert customer.get() != null;
    }

    public void asyncCallback() {
        ClientRequest request = Client.create("http://localhost:9095/customers/{id}").get(); // ClientRequest

        // invoke a request in background
        request.pathParam("id", 123).start(new AbstractInvocationCallback<Customer>() {

            @Override
            public void onComplete(Future<Customer> future) {
                // Do something
            }
        });

        // invoke another request in background
        Future<?> handle = request.pathParam("id", 456).start(new AbstractInvocationCallback<ClientResponse>() {

            @Override
            public void onComplete(Future<ClientResponse> future) {
                // do something
            }
        });
        handle.cancel(true);
    }

    public void asyncCallbackUsingSubResourceClient() {
        Client anyCustomer = Client.create("http://localhost:9095/customers/{id}"); // Client

        // invoke a request in background
        Future<Customer> handle = anyCustomer.pathParam("id", 123) // Client
                .get() // ClientRequest
                .start(new AbstractInvocationCallback<Customer>() {

            @Override
            public void onComplete(Future<Customer> future) {
                // Do something
            }
        });
        handle.cancel(true);

        // invoke another request in background
        anyCustomer.pathParam("id", 456) // Client
                .get() // ClientRequest
                .start(new AbstractInvocationCallback<ClientResponse>() {

            @Override
            public void onComplete(Future<ClientResponse> future) {
                // do something
            }
        });
    }
}