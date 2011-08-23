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
package jaxrs.examples.client.spec;

import java.util.concurrent.Future;
import javax.ws.rs.client.AsyncInvoker;
import jaxrs.examples.client.custom.ThrottledClient;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientFactory;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.SyncInvoker;
import javax.ws.rs.client.Target;
import javax.ws.rs.core.HttpResponse;

import javax.xml.bind.annotation.XmlRootElement;

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
    
    public void fluentMethodChaining() {
        Client client = ClientFactory.newClient();
        HttpResponse res = client.target("http://example.org/hello")
                .accept("text/plain").get();
        
        HttpResponse res2 = client.target("http://example.org/hello")
                .queryParam("MyParam","...")
                .accept("text/plain")
                .header("MyHeader", "...")
                .get();
    }
    
    public void typeRelationships() {
        Client client = ClientFactory.newClient();
        Target uri = client.target("");
        Invocation.TargetedBuilder builder = uri.accept("text/plain");
        
        SyncInvoker syncInvoker = builder;
        AsyncInvoker asyncInvoker = builder.async();
        Invocation inv = builder.prepare().get();
        
        HttpResponse r1 = builder.get();
        HttpResponse r2 = syncInvoker.get();
        HttpResponse r3= inv.invoke();
        
        Future<HttpResponse> fr1 = asyncInvoker.get();
        Future<HttpResponse> fr2 = inv.submit();        
    }
    
    public void benefitsOfResourceUri() {
        Client client = ClientFactory.newClient();
        Target base = client.target("http://example.org/");
        Target hello = base.path("hello").path("{whom}");   
        final Target whomToGreet = hello.pathParam("whom", "world");
        HttpResponse res = whomToGreet.get();
    }
    
    public void gettingAndPostingCustomers() {
        Client client = ClientFactory.newClient();
        Customer c = client.target("http://examples.org/customers/123")
                .accept("application/xml").get(Customer.class);
        HttpResponse res = client.target("http://examples.org/premium-customers/")
                .type("application/xml").post(c);     
    }
    
    public void asyncSamples() throws Exception {
        Client client = ClientFactory.newClient();
        Future<Customer> fc = client.target("http://examples.org/customers/123")
                .accept("application/xml").async().get(Customer.class);
        Customer c = fc.get();   
    }
    
}