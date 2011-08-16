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
package jaxrs.examples.client.custom;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Configurable;
import javax.ws.rs.client.Feature;
import javax.ws.rs.client.Target;
import javax.ws.rs.core.HttpRequest;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.ClientBuilderFactory;

/**
 *
 * @author Marek Potociar (marek.potociar at oracle.com)
 */
public class ThrottledClient implements Client {

    public static class Builder implements Client.Builder<ThrottledClient> {

        private static final int DEFAULT_QUEUE_SIZE = 3;

        public static class Factory implements ClientBuilderFactory<ThrottledClient.Builder> {

            @Override
            public ThrottledClient.Builder newBuilder() {
                return new ThrottledClient.Builder();
            }
        }
        private int requestQueueCapacity = DEFAULT_QUEUE_SIZE;

        @Override
        public ThrottledClient build() {
            return new ThrottledClient(requestQueueCapacity);
        }

        @Override
        public ThrottledClient build(Configurable<?> configuration) {
            return new ThrottledClient(configuration, requestQueueCapacity);
        }

        public Builder requestQueueCapacity(int capacity) {
            this.requestQueueCapacity = capacity;

            return this;
        }
    }
    private final BlockingQueue<HttpRequest<?>> requestQueue;

    private ThrottledClient(int queueCapacity) {
        this.requestQueue = new ArrayBlockingQueue<HttpRequest<?>>(queueCapacity);
    }

    private ThrottledClient(Configurable<?> configuration, int queueCapacity) {
        this(queueCapacity);

        configure(configuration);
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Target target(String uri) throws IllegalArgumentException, NullPointerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Target target(URI uri) throws NullPointerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Target target(UriBuilder uriBuilder) throws NullPointerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, Object> getProperties() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getProperty(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<Class<? extends Feature>> getFeatures() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isEnabled(Class<? extends Feature> feature) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<Class<?>> getProviderClasses() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<Object> getProviderSingletons() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Client register(Class<?> providerClass) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Client register(Object... providers) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Client enable(Class<? extends Feature> feature) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Client disable(Class<? extends Feature> feature) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Client setProperties(Map<String, Object> properties) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Client setProperty(String name, Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public final Client configure(Configurable<?> configuration) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
