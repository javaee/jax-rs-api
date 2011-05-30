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
import javax.ws.rs.client.ClientConfiguration;
import javax.ws.rs.client.ClientRequest;
import javax.ws.rs.client.ClientResponse;
import javax.ws.rs.client.AbstractInvocationCallback;
import javax.ws.rs.client.DefaultClientConfiguration;
import javax.ws.rs.client.HttpInvocation;
import javax.ws.rs.client.ResourceUri;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ClientFactory;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Bill Burke
 * @author Marek Potociar
 */
public class BasicExamples {

    public static abstract class MyClient extends Client {

        public MyClient(ClientConfiguration configuration) {
            super(configuration);
        }
    }

    public static class MyClientBuilder implements Client.Builder<MyClient, MyClientConfiguration> {

        @Override
        public MyClient create() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public MyClient create(MyClientConfiguration config) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class MyClientConfiguration extends DefaultClientConfiguration {

        public void enableCaching() { /* not implemented */ }
    }

    public static class MyClientBuilderFactory implements ClientFactory<MyClientBuilder> {

        @Override
        public MyClientBuilder createClientBuilder() {
            return new MyClientBuilder();
        }
    }

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
        Client defaultClient = Client.create();
        assert defaultClient != null;

        // Default client instantiation using custom configuration
        ClientConfiguration cfg = new DefaultClientConfiguration();
        cfg.getFeatures().put("CUSTOM_FEATURE", true);

        Client defaultConfiguredClient = Client.create(cfg);
        assert defaultConfiguredClient != null;

        ///////////////////////////////////////////////////////////
        
        // Custom client instantiation using default configuration
        MyClient myClient = Client.providedBy(MyClientBuilderFactory.class).create();
        assert myClient != null;

        // Custom client instantiation using custom configuration
        MyClientConfiguration myCfg = new MyClientConfiguration();
        myCfg.enableCaching();

        MyClient myConfiguredClient = Client.providedBy(MyClientBuilderFactory.class).create(myCfg);
        assert myConfiguredClient != null;
    }
    
    public void creatingResourceAndSubResourceUris() {
        // ResourceUri( http://jaxrs.examples.org/jaxrsApplication/customers/ )
        ResourceUri customersUri = Client.create().resourceUri("http://jaxrs.examples.org/jaxrsApplication/customers");        
        // ResourceUri( http://jaxrs.examples.org/jaxrsApplication/customers/{id}/ )
        ResourceUri anyCustomerUri = customersUri.path("{id}");
        // ResourceUri( http://jaxrs.examples.org/jaxrsApplication/customers/123/ )
        ResourceUri customer123 = anyCustomerUri.pathParam("id", 123);
        
        assert customer123 != null;
    }
    
    public void creatingClientRequestsAndInvocations() {
        final Client client = Client.create();
        
        // Create client request, customize it and invoke using client
        ClientRequest<?> request = client.request("http://jaxrs.examples.org/jaxrsApplication/customers").prepareGet();
        request.accept(MediaType.APPLICATION_XML).header("Foo", "Bar");
        ClientResponse responseA = client.invoke(request);        
        assert responseA.getStatusCode() == 200;
        
        // Direct invocation leveraging the HttpInvocation interface
        ClientResponse responseB = client.request("http://jaxrs.examples.org/jaxrsApplication/customers").prepareGet() // HttpInvocation (extends ClientRequest)
                .accept(MediaType.APPLICATION_XML).header("Foo", "Bar").invoke();
        assert responseB.getStatusCode() == 200;
    }

    public void creatingResourceUriRequestsAndInvocations() {
        final Client client = Client.create();
        final ResourceUri customersUri = client.resourceUri("http://jaxrs.examples.org/jaxrsApplication/customers");
        
        // Create resourceUri request, customize it and invoke using client
        ClientRequest<?> request = customersUri.prepareGet();
        request.accept(MediaType.APPLICATION_XML).header("Foo", "Bar");
        ClientResponse responseA = client.invoke(request);        
        assert responseA.getStatusCode() == 200;
        
        // Direct invocation leveraging the HttpInvocation interface
        ClientResponse responseB = customersUri.prepareGet() // HttpInvocation (extends ClientRequest)
                .accept(MediaType.APPLICATION_XML).header("Foo", "Bar").invoke();
        assert responseB.getStatusCode() == 200;
    }

    public void defaultResponse() {
        Customer customer;
        ClientResponse response;

        final ResourceUri customersUri = Client.create().resourceUri("http://jaxrs.examples.org/jaxrsApplication/customers");

        response = customersUri.path("{id}").prepareGet().pathParam("id", 123).invoke();
        customer = response.getEntity(Customer.class);
        assert customer != null;

        response = customersUri.preparePost().entity(new Customer("Marek")).type("application/xml").invoke();
        assert response.getStatusCode() == 201;
    }

    public void typedResponse() {
        Customer customer = Client.create()
                .request("http://jaxrs.examples.org/jaxrsApplication/customers/{id}")
                .prepareGet()
                .pathParam("id", 123)
                .invoke(Customer.class);
        assert customer != null;
    }

    public void typedGenericResponse() {
        List<Customer> customers = Client.create()
                .request("http://jaxrs.examples.org/jaxrsApplication/customers")
                .prepareGet()
                .invoke(new GenericType<List<Customer>>() { });
        assert customers != null;
    }

    public void responseUsingSubResourceClient() {
        ResourceUri customersUri = Client.create().resourceUri("http://jaxrs.examples.org/jaxrsApplication/customers");
        ResourceUri customer = customersUri.path("{id}");

        // Create a customer
        ClientResponse response = customersUri.preparePost()
                .entity(new Customer("Bill")).type("application/xml").invoke();
        assert response.getStatusCode() == 201;

        Customer favorite;
        // view a customer
        favorite = customer.prepareGet() // HttpInvocation (extends ClientRequest)
                .pathParam("id", 123).invoke(Customer.class);
        assert favorite != null;

        // view a customer (alternative)
        favorite = customer.pathParam("id", 123) // ResourceUri ("http://jaxrs.examples.org/jaxrsApplication/customers/123/")
                .prepareGet() // HttpInvocation (extends ClientRequest)
                .invoke(Customer.class);
        assert favorite != null;
    }

    public void asyncResponse() throws Exception {
        Future<ClientResponse> future = Client.create()
                .request("http://jaxrs.examples.org/jaxrsApplication/customers/{id}")
                .prepareGet()
                .pathParam("id", 123)
                .start();

        ClientResponse response = future.get();
        Customer customer = response.getEntity(Customer.class);
        assert customer != null;
    }

    public void typedAsyncResponse() throws Exception {
        Future<Customer> customer = Client.create()
                .request("http://jaxrs.examples.org/jaxrsApplication/customers/{id}")
                .prepareGet()
                .pathParam("id", 123)
                .start(Customer.class);
        assert customer.get() != null;
    }

    public void asyncCallback() {
        final Client client = Client.create();
        HttpInvocation request = client.request("http://jaxrs.examples.org/jaxrsApplication/customers/{id}").prepareGet();
        request.pathParam("id", 123);

        // invoke a request in background        
        client.start(request, new AbstractInvocationCallback<Customer>() {

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

    public void asyncCallbackUsingSubResourceClient() throws Exception {
        final Client client = Client.create();
        ResourceUri anyCustomerUri = client.resourceUri("http://jaxrs.examples.org/jaxrsApplication/customers/{id}");

        // invoke a request in background
        Future<Customer> handle = anyCustomerUri.pathParam("id", 123) // ResourceUri
                .prepareGet() // HttpInvocation (extends ClientRequest)
                .start(new AbstractInvocationCallback<Customer>() {

            @Override
            public void onComplete(Future<Customer> future) {
                // Do something
            }
        });
        handle.cancel(true);

        // invoke another request in background
        anyCustomerUri.pathParam("id", 456) // ResourceUri
                .prepareGet() // HttpInvocation (extends ClientRequest)
                .start(new AbstractInvocationCallback<ClientResponse>() {

            @Override
            public void onComplete(Future<ClientResponse> future) {
                // do something
            }
        });
        
        // invoke one more request using client
        ClientRequest<?> request = anyCustomerUri.pathParam("id", 789).prepareGet();
        request.cookie(new Cookie("fooName", "XYZ"));
        Future<ClientResponse> response = client.start(request);
        assert response.get() != null;
    }
}