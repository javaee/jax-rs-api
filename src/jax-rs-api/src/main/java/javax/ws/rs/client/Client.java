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

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.Providers;

/**
 * TODO javadoc
 *
 * @author Marek Potociar
 * @since 2.0
 */
public abstract class Client {
    
    /**
     * TODO javadoc
     * @param <T>
     */
    public static interface Builder {
        public Builder using(ClientConfiguration config);
        
        public Client create();
    }

    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());
    //
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final URI uri = null;

    // Factory
    /**
     * Create client for specified resource URI using default configuration.
     * TODO fix javadoc
     *
     * @param uri resource URI
     * @return a default client.
     */
    public static Client.Builder of(String uri) {
        // todo implement
        return null;
    }

    public static Client.Builder of(URI uri) {
        // todo implement
        return null;
    }

    public static Client.Builder of(UriBuilder uri) {
        // todo implement
        return null;
    }

    public static Client create(String uri) {
        // todo implement
        return null;
    }

    public static Client create(URI uri) {
        // todo implement
        return null;
    }

    public static Client create(UriBuilder uri) {
        // todo implement
        return null;
    }

    /**
     * Protected constructor used by concrete implementations of the {@link Client}
     * class.
     * <p />
     * Use one of the static {@code Client.create(...)} factory methods to obtain
     * a new JAX-RS client instance.
     *
     * @param uri TODO
     * @param configuration holder for the client configuration
     */
    protected Client(UriBuilder uri, ClientConfiguration configuration) {
        // todo implement
    }

    // Clean-up
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
    public final void close() {
        if (closed.compareAndSet(false, true)) {
            onClose();
        }
    }

    /**
     * Called when the client gets destroyed. Any system resources associated with
     * the client instance MUST be cleaned up in this method. It is guaranteed that
     * the method will be called only once.
     *
     * @see #destroy() 
     */
    protected abstract void onClose();

    /**
     * Defers to {@link #destroy()} and calls {@link Object#finalize() finalizer}
     * method of the superclass.
     */
    @Override
    @SuppressWarnings("FinalizeDeclaration")
    protected void finalize() throws Throwable {
        try {
            if (closed.compareAndSet(false, true)) {
                LOGGER.log(Level.WARNING, "{0} instance leak detected.", this.getClass().getName());
                onClose();
            }
        } finally {
            super.finalize();
        }
    }

    // Getters
    /**
     * Get the URI identifying the resource.
     *
     * @return the URI.
     */
    public final URI getURI() {
        return uri;
    }

    /**
     * Get the URI builder initialized with the {@link URI} identifying the
     * resource.
     *
     * @return the URI builder.
     */
    public final UriBuilder getUriBuilder() {
        return UriBuilder.fromUri(uri);
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

    // Sub-resource client buidler methods

    public abstract Client path(String path) throws IllegalArgumentException;

    public abstract Client queryParam(String name, Object value) throws IllegalArgumentException;

    public abstract Client queryParams(MultivaluedMap<String, Object> parameters) throws IllegalArgumentException;    
    
    public abstract Client pathParam(String name, Object value) throws IllegalArgumentException;

    public abstract Client pathParams(MultivaluedMap<String, Object> parameters) throws IllegalArgumentException;    
    
    // Request builder methods
    public abstract ClientRequest get();

    public abstract ClientRequest put();

    public abstract ClientRequest post();

    public abstract ClientRequest delete();

    public abstract ClientRequest head();

    public abstract ClientRequest options();

    public abstract ClientRequest method(String name);
}
