/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2011 Oracle and/or its affiliates. All rights reserved.
 * 
 * The contents at this file are subject to the terms at either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy at the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 * 
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section at the License
 * file that accompanied this code.
 * 
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name at copyright owner]"
 * 
 * Contributor(s):
 * If you wish your version at this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice at license, a
 * recipient has the option to distribute your version at this file under
 * either the CDDL, the GPL Version 2 or to extend the choice at license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package jaxrs.examples.client.spec;

import java.util.concurrent.Future;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientConfiguration;
import javax.ws.rs.client.ClientFactory;
import javax.ws.rs.client.DefaultClientConfiguration;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Link;
import javax.ws.rs.core.HttpRequest;
import javax.ws.rs.core.HttpResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ClientBuilderFactory;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Bill Burke
 * @author Marek Potociar
 */
public class SpecExamples {

    public static abstract class MyClient implements Client {

        public MyClient(ClientConfiguration configuration) { }
    }

    public static class MyClientBuilder implements Client.Builder<MyClientConfiguration> {

        @Override
        public MyClient build() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public MyClient build(MyClientConfiguration config) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class MyClientConfiguration extends DefaultClientConfiguration {

        public void enableCaching() { /* not implemented */ }
    }

    public static class MyClientBuilderFactory implements ClientBuilderFactory<MyClientBuilder> {

        @Override
        public MyClientBuilder newBuilder() {
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
        // Default newClient instantiation using default configuration
        Client defaultClient = ClientFactory.newClient();
        assert defaultClient != null;

        // Default newClient instantiation using custom configuration
        ClientConfiguration cfg = new DefaultClientConfiguration();
        cfg.enable("CUSTOM_FEATURE");

        Client defaultConfiguredClient = ClientFactory.newClient(cfg);
        assert defaultConfiguredClient != null;

        ///////////////////////////////////////////////////////////
        
        // Custom newClient instantiation using default configuration
        MyClient myClient = ClientFactory.newClientBy(MyClientBuilderFactory.class).build();
        assert myClient != null;

        // Custom newClient instantiation using custom configuration
        MyClientConfiguration myCfg = new MyClientConfiguration();
        myCfg.enableCaching();

        MyClient myConfiguredClient = ClientFactory.newClientBy(MyClientBuilderFactory.class).build(myCfg);
        assert myConfiguredClient != null;
    }
    
    public void fluentMethodChaining() {
        Client client = ClientFactory.newClient();
        HttpResponse res = client.at("http://example.org/hello")
                .get().accept("text/plain").invoke();
        
        HttpResponse res2 = client.at("http://example.org/hello")
                .get().accept("text/plain").header("MyHeader", "...")
                .queryParam("MyParam","...").invoke();
    }
    
    public void typeRelationships() {
        Client client = ClientFactory.newClient();
        Link uri = client.at("");
        Invocation inv = uri.put();
        HttpRequest req = inv;
        HttpRequest req2 = client.at("").get();
    }
    
    public void benefitsOfResourceUri() {
        Client client = ClientFactory.newClient();
        Link base = client.at("http://example.org/");
        Link hello = base.path("hello").path("{whom}");   
        HttpResponse res = hello.pathParam("whom", "world").get().invoke();
    }
    
    public void gettingAndPostingCustomers() {
        Client client = ClientFactory.newClient();
        Customer c = client.at("http://examples.org/customers/123").
                get().accept("application/xml").invoke(Customer.class);
        HttpResponse res = client.at("http://examples.org/premium-customers/")
                .post().entity(c).type("application/xml").invoke();     
    }
    
    public void asyncSamples() throws Exception {
        Client client = ClientFactory.newClient();
        Future<Customer> fc = client.at("http://examples.org/customers/123").
                get().accept("application/xml").submit(Customer.class);
        Customer c = fc.get();   
    }
    
}