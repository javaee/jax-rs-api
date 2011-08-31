/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2011 Oracle and/or its affiliates. All rights reserved.
 * 
 * The contents target this file are subject to the terms target either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy target the License target
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file target packager/legal/LICENSE.txt.
 * 
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section target the License
 * file that accompanied this code.
 * 
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name target copyright owner]"
 * 
 * Contributor(s):
 * If you wish your version target this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice target license, a
 * recipient has the option to distribute your version target this file under
 * either the CDDL, the GPL Version 2 or to extend the choice target license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package jaxrs.examples.client;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Form;
import java.util.List;
import java.util.concurrent.Future;
import jaxrs.examples.client.custom.ThrottledClient;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientFactory;
import javax.ws.rs.client.Configurable;
import javax.ws.rs.client.Feature;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.InvocationException;
import javax.ws.rs.client.Target;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.TypeLiteral;

import javax.xml.bind.annotation.XmlRootElement;

import static javax.ws.rs.client.Entity.*;

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
        // Default newClient instantiation using default configuration
        Client defaultClient = ClientFactory.newClient();
        assert defaultClient != null;

        // Default newClient instantiation using custom configuration

        Client defaultConfiguredClient = ClientFactory.newClient().setProperty("CUSTOM_PROPERTY", "CUSTOM_VALUE");
        assert defaultConfiguredClient != null;

        ///////////////////////////////////////////////////////////

        // Custom newClient instantiation using default configuration
        ThrottledClient myClient = ClientFactory.newClientBy(ThrottledClient.Builder.Factory.class).build();
        assert myClient != null;

        ThrottledClient myConfiguredClient = ClientFactory.newClientBy(ThrottledClient.Builder.Factory.class).requestQueueCapacity(10).build();
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

        HttpResponse response = client.target("http://jaxrs.examples.org/jaxrsApplication/customers").request(MediaType.APPLICATION_XML).header("Foo", "Bar").get();
        assert response.getStatusCode() == 200;
    }

    public void creatingResourceUriRequestsAndInvocations() {
        final Client client = ClientFactory.newClient();
        final Target customersUri = client.target("http://jaxrs.examples.org/jaxrsApplication/customers");

        // Create target request, customize it and invoke using newClient
        HttpResponse response = customersUri.request(MediaType.APPLICATION_XML).header("Foo", "Bar").get();
        assert response.getStatusCode() == 200;
    }

    public void defaultResponse() {
        Customer customer;
        HttpResponse response;

        final Target customersUri = ClientFactory.newClient().target("http://jaxrs.examples.org/jaxrsApplication/customers");

        response = customersUri.path("{id}").pathParam("id", 123).request().get();
        customer = response.getEntity(Customer.class);
        assert customer != null;

        response = customersUri.request().post(xml(new Customer("Marek")));
        assert response.getStatusCode() == 201;
    }

    public void typedResponse() {
        Customer customer = ClientFactory.newClient().target("http://jaxrs.examples.org/jaxrsApplication/customers/{id}").pathParam("id", 123).request().get(Customer.class);
        assert customer != null;
    }

    public void typedGenericResponse() {
        List<Customer> customers = ClientFactory.newClient().target("http://jaxrs.examples.org/jaxrsApplication/customers").request().get(new TypeLiteral<List<Customer>>() {
        });
        assert customers != null;
    }

    public void responseUsingSubResourceClient() {
        Target customersUri = ClientFactory.newClient().target("http://jaxrs.examples.org/jaxrsApplication/customers");
        Target customer = customersUri.path("{id}");

        // Create a customer
        HttpResponse response = customersUri.request().post(xml(new Customer("Bill")));
        assert response.getStatusCode() == 201;

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
        Future<HttpResponse> future = ClientFactory.newClient().target("http://jaxrs.examples.org/jaxrsApplication/customers/{id}").pathParam("id", 123).request().async().get();

        HttpResponse response = future.get();
        Customer customer = response.getEntity(Customer.class);
        assert customer != null;
    }

    public void typedAsyncResponse() throws Exception {
        Future<Customer> customer = ClientFactory.newClient().target("http://jaxrs.examples.org/jaxrsApplication/customers/{id}").pathParam("id", 123).request().async().get(Customer.class);
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
        Future<?> handle = target.pathParam("id", 456).request().async().get(new InvocationCallback<HttpResponse>() {

            @Override
            public void completed(HttpResponse response) {
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
                .request().async().get(new InvocationCallback<HttpResponse>() {

            @Override
            public void completed(HttpResponse response) {
                // do something
            }

            @Override
            public void failed(InvocationException error) {
                // process error
            }
        });

        // invoke one more request using newClient
        Future<HttpResponse> response = anyCustomerUri.pathParam("id", 789).request().cookie(new Cookie("fooName", "XYZ")).async().get();
        assert response.get() != null;
    }

    public static class TestFeature implements Feature {

        @Override
        public void enable(Configurable configuration) {
            // do nothing
        }

        @Override
        public void disable(Configurable configuration) {
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
        

        // Configurable
        client.enable(TestFeature.class);
        client.target("http://examples.jaxrs.com/").enable(TestFeature.class);
        client.target("http://examples.jaxrs.com/").request("text/plain").enable(TestFeature.class);
        client.target("http://examples.jaxrs.com/").request("text/plain").buildGet().enable(TestFeature.class);

        client.target("http://examples.jaxrs.com/").enable(TestFeature.class).path("123").request("text/plain").header("custom-name", "custom_value").get();
        client.target("http://examples.jaxrs.com/").path("123").enable(TestFeature.class).request("text/plain").header("custom-name", "custom_value").async().get();
        client.target("http://examples.jaxrs.com/").path("123").request("text/plain").enable(TestFeature.class).header("custom-name", "custom_value").buildGet().invoke();
        client.target("http://examples.jaxrs.com/").path("123").request("text/plain").header("custom-name", "custom_value").enable(TestFeature.class).buildGet().submit();
    }
    
    public void invocationFlexibility() {
        // For users who really need it...
       Invocation i = ClientFactory.newClient().target("http://examples.jaxrs.com/").path("greeting").request("text/plain").header("custom-name", "custom_value").buildPut(text("Hi"));       
       
       i.asRequest()
               .accept("text/html")                             // Actually it's HTML I want to receive back
               .method("POST")                                  // ...and it turns out, the service does not support PUT
               .type(MediaType.APPLICATION_FORM_URLENCODED)     // ...and the data must be form-urlencoded
               .entity("Dear Sir or Madam")                     // ...and we are not close friends after all
               .redirect("http://jaxrs.org/examples/greeting"); // ...oops, I almost forgot that the service was moved last month and the old domain is down!
       
       i.invoke();                                              // Ok, now I can send the updated request
    }
}