/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011-2012 Oracle and/or its affiliates. All rights reserved.
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
import javax.ws.rs.client.Configuration;
import javax.ws.rs.client.Feature;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.InvocationException;
import javax.ws.rs.client.Target;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.ext.ClientFactory;
import static javax.ws.rs.client.Entity.*;

import javax.xml.bind.annotation.XmlRootElement;
import jaxrs.examples.client.custom.ThrottledClient;

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

    public void clientBootstrapping() {
        // Default client instantiation using default configuration
        Client defaultClient = ClientFactory.newClient();
        defaultClient.configuration().setProperty("CUSTOM_PROPERTY", "CUSTOM_VALUE");
        assert defaultClient != null;

        // Default client instantiation using custom configuration

        Client defaultConfiguredClient = ClientFactory.newClient(defaultClient.configuration());
        assert defaultConfiguredClient != null;

        ///////////////////////////////////////////////////////////

        // Custom client instantiation examples
        ThrottledClient myClient = new ThrottledClient();
        assert myClient != null;

        ThrottledClient myConfiguredClient = new ThrottledClient(10);
        assert myConfiguredClient != null;
    }

    public void creatingResourceAndSubResourceUris() {
        // Target( http://jaxrs.examples.org/jaxrsApplication/customers/ )
        Target customersUri = ClientFactory.newClient().target("http://jaxrs.examples.org/jaxrsApplication/customers");
        // Target( http://jaxrs.examples.org/jaxrsApplication/customers/{id}/ )
        Target anyCustomerUri = customersUri.path("{id}");
        // Target( http://jaxrs.examples.org/jaxrsApplication/customers/123/ )
        Target customer123 = anyCustomerUri.pathParam("id", 123);

        assert customer123 != null;
    }

    public void creatingClientRequestsAndInvocations() {
        final Client client = ClientFactory.newClient();

        Response response = client.target("http://jaxrs.examples.org/jaxrsApplication/customers").request(MediaType.APPLICATION_XML).header("Foo", "Bar").get();
        assert response.getStatus() == 200;
    }

    public void creatingResourceUriRequestsAndInvocations() {
        final Client client = ClientFactory.newClient();
        final Target customersUri = client.target("http://jaxrs.examples.org/jaxrsApplication/customers");

        // Create target request, customize it and invoke using newClient
        Response response = customersUri.request(MediaType.APPLICATION_XML).header("Foo", "Bar").get();
        assert response.getStatus() == 200;
    }

    public void defaultResponse() {
        Customer customer;
        Response response;

        final Target customersUri = ClientFactory.newClient().target("http://jaxrs.examples.org/jaxrsApplication/customers");

        response = customersUri.path("{id}").pathParam("id", 123).request().get();
        customer = response.readEntity(Customer.class);
        assert customer != null;

        response = customersUri.request().post(xml(new Customer("Marek")));
        assert response.getStatus() == 201;
    }

    public void typedResponse() {
        Customer customer = ClientFactory.newClient()
                .target("http://jaxrs.examples.org/jaxrsApplication/customers/{id}")
                .pathParam("id", 123).request().get(Customer.class);
        assert customer != null;
    }

    public void typedGenericResponse() {
        List<Customer> customers = ClientFactory.newClient()
                .target("http://jaxrs.examples.org/jaxrsApplication/customers")
                .request().get(new GenericType<List<Customer>>() {
        });
        assert customers != null;
    }

    public void responseUsingSubResourceClient() {
        Target customersUri = ClientFactory.newClient().target("http://jaxrs.examples.org/jaxrsApplication/customers");
        Target customer = customersUri.path("{id}");

        // Create a customer
        Response response = customersUri.request().post(xml(new Customer("Bill")));
        assert response.getStatus() == 201;

        Customer favorite;
        // view a customer
        favorite = customer.pathParam("id", 123).request().get(Customer.class);
        assert favorite != null;

        // view a customer (alternative)
        favorite = customer.pathParam("id", 123) // Target ("http://jaxrs.examples.org/jaxrsApplication/customers/123/")
                .request().get(Customer.class);
        assert favorite != null;
    }

    public void asyncResponse() throws Exception {
        Future<Response> future = ClientFactory.newClient()
                .target("http://jaxrs.examples.org/jaxrsApplication/customers/{id}")
                .pathParam("id", 123).request().async().get();

        Response response = future.get();
        Customer customer = response.readEntity(Customer.class);
        assert customer != null;
    }

    public void typedAsyncResponse() throws Exception {
        Future<Customer> customer = ClientFactory.newClient()
                .target("http://jaxrs.examples.org/jaxrsApplication/customers/{id}")
                .pathParam("id", 123).request().async().get(Customer.class);
        assert customer.get() != null;
    }

    public void asyncCallback() {
        final Client client = ClientFactory.newClient();
        Target target = client.target("http://jaxrs.examples.org/jaxrsApplication/customers/{id}");
        target.pathParam("id", 123).request().async().get(new InvocationCallback<Customer>() {

            @Override
            public void completed(Customer customer) {
                // Do something
            }

            @Override
            public void failed(InvocationException error) {
                // process error
            }
        });

        // invoke another request in background
        Future<?> handle = target.pathParam("id", 456).request().async().get(new InvocationCallback<Response>() {

            @Override
            public void completed(Response response) {
                // do something
            }

            @Override
            public void failed(InvocationException error) {
                // process error
            }
        });
        handle.cancel(true);
    }

    public void asyncCallbackUsingSubResourceClient() throws Exception {
        final Client client = ClientFactory.newClient();
        Target anyCustomerUri = client.target("http://jaxrs.examples.org/jaxrsApplication/customers/{id}");

        // invoke a request in background
        Future<Customer> handle = anyCustomerUri.pathParam("id", 123) // Target
                .request().async().get(new InvocationCallback<Customer>() {

            @Override
            public void completed(Customer customer) {
                // Do something
            }

            @Override
            public void failed(InvocationException error) {
                // process error
            }
        });
        handle.cancel(true);

        // invoke another request in background
        anyCustomerUri.pathParam("id", 456) // Target
                .request().async().get(new InvocationCallback<Response>() {

            @Override
            public void completed(Response response) {
                // do something
            }

            @Override
            public void failed(InvocationException error) {
                // process error
            }
        });

        // invoke one more request using newClient
        Future<Response> response = anyCustomerUri.pathParam("id", 789)
                .request().cookie(new Cookie("fooName", "XYZ")).async().get();
        assert response.get() != null;
    }

    public static class TestFeature implements Feature {

        @Override
        public void onEnable(Configuration configuration) {
            // do nothing
        }

        @Override
        public void onDisable(Configuration configuration) {
            // do nothing
        }
    }

    public void commonFluentUseCases() {
        Client client = ClientFactory.newClient();

        // Invocation
        client.target("http://examples.jaxrs.com/");

        client.target("http://examples.jaxrs.com/").request("text/plain").get();
        client.target("http://examples.jaxrs.com/").request("text/plain").async().get();
        client.target("http://examples.jaxrs.com/").request().buildPut(text("Hi")).invoke();
        client.target("http://examples.jaxrs.com/").request("text/plain").buildGet().submit();


        client.target("http://examples.jaxrs.com/").path("123").request("text/plain").get();
        client.target("http://examples.jaxrs.com/").path("123").request("text/plain").async().get();
        client.target("http://examples.jaxrs.com/").path("123").request("text/plain").buildGet().invoke();
        client.target("http://examples.jaxrs.com/").path("123").request("text/plain").buildGet().submit();

        client.target("http://examples.jaxrs.com/").path("123").request("text/plain").get();
        client.target("http://examples.jaxrs.com/").path("123").request("text/plain").async().get();
        client.target("http://examples.jaxrs.com/").path("123").request("text/plain").buildGet().invoke();
        client.target("http://examples.jaxrs.com/").path("123").request("text/plain").buildGet().submit();

        client.target("http://examples.jaxrs.com/").path("123").request("text/plain").header("custom-name", "custom_value").get();
        client.target("http://examples.jaxrs.com/").path("123").request("text/plain").header("custom-name", "custom_value").async().get();
        client.target("http://examples.jaxrs.com/").path("123").request("text/plain").header("custom-name", "custom_value").buildGet().invoke();
        client.target("http://examples.jaxrs.com/").path("123").request("text/plain").header("custom-name", "custom_value").buildGet().submit();

        // POSTing Forms
        client.target("http://examples.jaxrs.com/").path("123").request(MediaType.APPLICATION_JSON).post(form(new Form("param1", "a").param("param2", "b")));

        MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
        formData.add("param1", "a");
        formData.add("param2", "b");
        client.target("http://examples.jaxrs.com/").path("123").request(MediaType.APPLICATION_JSON).post(form(formData));

        // Configuration
        TestFeature testFeature = new TestFeature();
        client.configuration().enable(testFeature);
        client.target("http://examples.jaxrs.com/").configuration().enable(testFeature);
        client.target("http://examples.jaxrs.com/").request("text/plain").configuration().enable(testFeature);
        client.target("http://examples.jaxrs.com/").request("text/plain").buildGet().configuration().enable(testFeature);
    }

    public void invocationFlexibility() {
        // For users who really need it...
        Invocation i = ClientFactory.newClient()
                .target("http://examples.jaxrs.com/greeting")
                .request("text/plain")
                .header("custom-name", "custom_value")
                .buildPut(text("Hi"));

        i.invoke();                                              // Ok, now I can send the updated request
    }
}
