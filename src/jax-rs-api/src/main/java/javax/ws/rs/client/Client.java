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

package javax.ws.rs.client;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.ext.Providers;
import javax.ws.rs.ext.RuntimeDelegate;

/**
 * The main class for creating web resource instances and configuring the properties
 * of connections and requests.
 * <p>
 * This class is {@link Filterable}, so {@link ClientFilter} instances may be added
 * to the client for filtering requests and responses, including those processed by
 * web resource instances created from the client.
 * <p>
 * A new client instance may be configured by passing a {@link ClientConfiguration} 
 * instance to the appropriate {@link Client#create(javax.ws.rs.client.ClientConfiguration)
 * factory method}.
 * <p>
 * Methods to create web resource instances are thread-safe. Methods that modify
 * configuration and or filters are not guaranteed to be thread-safe.
 * <p>
 * The creation of a {@code Client} instance is an expensive operation and
 * the instance may make use of and retain many resources. It is therefore
 * recommended that a single {@code Client} instance is reused for the creation
 * of all web resources that require the same configuration settings.
 *
 * @author Paul Sandoz
 * @author Marek Potociar
 * @since 2.0
 */
public abstract class Client extends Filterable {
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    /**
     * Defines runtime contract for a delegate that is responsible for instantiating
     * the concrete implementation of {@link Client}. 
     * <p />
     * Vendors implementing JAX-RS client API are responsible for providing an
     * implementation of a default client instantiation delegate as part of the
     * default client configuration.
     * <p />
     * Users of the JAX-RS client APi are free to plug in their own implementations
     * of the client instantiation delegate by replacing the default instantiation
     * delegate in a client configuration instance with their custom implementation.
     *
     * @see RuntimeDelegate#createClientConfiguration()
     * @see ClientConfiguration#getInstatiationDelegate()
     * @see ClientConfiguration#setInstatiationDelegate(Client.InstantiationDelegate)
     * @since 2.0
     */
    public interface InstantiationDelegate {
        /**
         * Create and configure a new {@link Client} instance based on the
         * supplied {@link ClientConfiguration}.
         * 
         * @param configuration client configuration used to configure instantiated
         *     client.
         * @return configured client.
         */
        Client instantiate(ClientConfiguration configuration);
    }

    /**
     * Create a default client using a default configuration.
     *
     * @return a default client.
     */
    public static Client create() {
        final ClientConfiguration cfg = defaultConfiguration();
        return cfg.getInstatiationDelegate().instantiate(cfg);
    }

    /**
     * Create a client based on the information stored in client configuration.
     *
     * @param configuration the client configuration.
     * @return a default client.
     * @see #defaultConfiguration() 
     */
    public static Client create(ClientConfiguration configuration) {
        return configuration.getInstatiationDelegate().instantiate(configuration);
    }

    /**
     * Return default client configuration instance.
     * <p />
     * Returned configuration instance can be further customized and used for
     * creating new {@link Client clients}.
     *
     * @return Instance representing a default client configuration.
     */
    public static ClientConfiguration defaultConfiguration() {
        return RuntimeDelegate.getInstance().createClientConfiguration();
    }

    /**
     * Protected constructor used by concrete implementations of the {@link Client}
     * class.
     * <p />
     * Use one of the static {@code Client.create(...)} factory methods to obtain
     * a new JAX-RS client instance.
     *
     * @param configuration holder for the client configuration
     */
    protected Client(ClientConfiguration configuration) {
        super(configuration.getRootHandler());
    }

    private final AtomicBoolean destroyed = new AtomicBoolean(false);

    /**
     * Destroy the client. Any system resources associated with the client
     * will be cleaned up.
     * <p>
     * This method must be called when there are not responses pending otherwise
     * undefined behavior will occur.
     * <p>
     * The client must not be reused after this method is called otherwise
     * undefined behavior will occur.
     */
    public final void destroy() {
        if (destroyed.compareAndSet(false, true)) {
            onDestroy();
        }
    }

    /**
     * Called when the client gets destroyed. Any system resources associated with
     * the client instance MUST be cleaned up in this method. It is guaranteed that
     * the method will be called only once.
     *
     * @see #destroy() 
     */
    protected abstract void onDestroy();

    /**
     * Defers to {@link #destroy()} and calls {@link Object#finalize() finalizer}
     * method of the superclass.
     */
    @Override
    @SuppressWarnings("FinalizeDeclaration")
    protected void finalize() throws Throwable {
        try {
            if (destroyed.compareAndSet(false, true)) {
                LOGGER.log(Level.WARNING, "{0} instance leak detected.",
                        this.getClass().getName());
                onDestroy();
            }
        } finally {
            super.finalize();
        }
    }

    /**
     * Get the mutable property bag.
     *
     * @return the property bag.
     */
    public abstract Map<String, Object> getProperties();

    /**
     * Get the {@link Providers} utilized by the client.
     *
     * @return the {@link Providers} utilized by the client.
     */
    public abstract Providers getProviders();

    /**
     * Create a Web resource from the client.
     *
     * @param uri the URI of the resource. Must not be {@code null}.
     * @return the Web resource.
     * @throws IllegalArgumentException If the given string violates RFC 2396.
     * @throws NullPointerException If the input argument is {@code null}.
     */
    public WebResource resource(String uri) throws IllegalArgumentException, NullPointerException {
        return resource(URI.create(uri));
    }

    /**
     * Create a Web resource from the client.
     *
     * @param uri the URI of the resource.
     * @return the Web resource.
     */
    public abstract WebResource resource(URI uri);

    /**
     * Create an asynchronous Web resource from the client.
     *
     * @param uri the URI of the resource.
     * @return the Web resource.
     * @throws IllegalArgumentException If the given string violates RFC 2396.
     * @throws NullPointerException If the input argument is {@code null}.
     */
    public AsyncWebResource asyncResource(String uri) throws IllegalArgumentException, NullPointerException {
        return asyncResource(URI.create(uri));
    }

    /**
     * Create an asynchronous Web resource from the client.
     *
     * @param uri the URI of the resource.
     * @return the Web resource.
     */
    public abstract AsyncWebResource asyncResource(URI uri);
}
