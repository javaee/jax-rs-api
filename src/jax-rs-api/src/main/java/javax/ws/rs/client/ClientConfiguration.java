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

import java.util.Map;
import java.util.Set;

/**
 * JAX-RS client configuration contract.
 * <p />
 * A new default client configuration instance can be obtained by calling
 * the {@link Client#getDefaultConfiguration() } factory method. Once obtained,
 * the client configuration instance can be customized by specifying various 
 * configuration properties and their values. The resulting configuration instance
 * can be then passed to the {@link Client#create(javax.ws.rs.client.ClientConfiguration) }
 * factory method which will return a configured {@link Client} instance.
 *
 * @author Paul Sandoz
 * @author Marek Potociar
 * @since 2.0
 */
public interface ClientConfiguration {

    /**
     * Get the mutable set of provider classes to be instantiated in the scope
     * of the Client.
     * <p>
     * A provider class is a Java class with a {@link javax.ws.rs.ext.Provider}
     * annotation declared on the class that implements a specific service
     * interface.
     *
     * @return the mutable set of provider classes. After initialization of
     * the client modification of this value will have no effect.
     * The returned value shall never be {@code null}.
     */
    Set<Class<?>> getClasses();

    /**
     * Get the value of a feature represented by a boolean property in the
     * property bag.
     *
     * @param featureName the name of the feature.
     * @return true if the feature value is present and is an instance of
     *     {@link java.lang.Boolean} and that value is {@code true}, otherwise
     *     {@code false}.
     * @see #getFeatures()
     */
    boolean getPropertyAsFeature(String featureName);

    /**
     * Get the singleton provider instances to be utilized by the client.
     * <p>
     * When the client is initialized the set of provider instances
     * will be combined and take precedence over the instantiated provider
     * classes.
     *
     * @return the mutable set of provider instances. After initialization of
     *     the client modification of this value will have no effect.
     *     The returned value shall never be {@code null}.
     * @see #getClasses()
     */
    Set<Object> getSingletons();

    /**
     * Get the map of features associated with the client.
     * <p />
     * Features are properties defined by their {@code String} name and their
     * value that is always of {@code Boolean} type. A feature value of {@code true}
     * means that the feature is enabled, value of {@code false} means that
     * the feature is disabled. The meaning of a feature is implementation specific.
     *
     * @return the feature map. The returned value shall never be {@code null}.
     */
    Map<String, Boolean> getFeatures();

    /**
     * Get the value of a feature.
     *
     * @param featureName the feature name.
     * @return {@code true} if the feature is present and set to {@code true},
     *     otherwise {@code false} (if the feature is present and set to
     *     {@code false} or the feature is not present at all).
     * @see #getFeatures()
     */
    boolean getFeature(String featureName);

    /**
     * Get the map of properties associated with the client.
     *<p />
     * Properties are defined by their {@code String} name and their
     * value that can be of any Java type. The meaning of a property is
     * implementation specific.
     *
     * @return the properties. The returned value shall never be {@code null}.
     */
    Map<String, Object> getProperties();

    /**
     * Get the value of a property.
     *
     * @param propertyName the property name.
     * @return the property, or {@code null} if there is no property present for
     *     the given property name.
     * @see #getProperties()
     */
    Object getProperty(String propertyName);

    /**
     * Get the configured {@link Client.InstantiationDelegate}.
     *<p />
     * Client instantiation delegate is used by the {@code Client.create(...)}
     * methods to instantiate the concrete implementation of JAX-RS {@link Client}.
     * 
     * @return client instantiation delegate.
     * @see #setInstatiationDelegate(Client.InstantiationDelegate)
     * @see Client.InstantiationDelegate
     */
    Client.InstantiationDelegate getInstatiationDelegate();

    /**
     * Set the new {@link Client.InstantiationDelegate}.
     * <p />
     * This method may be used to override the default client instantiation delegate
     * provided by the client configuration implementation. Custom client
     * instantiation delegate can be used to plug in a custom {@link Client}
     * implementation.
     * 
     * @param delegate new client instantiation delegate instance.
     * @see #getInstatiationDelegate()
     * @see Client.InstantiationDelegate
     */
    void setInstatiationDelegate(Client.InstantiationDelegate delegate);

    /**
     * Get the configured root {@link ClientHandler}.
     * <p />
     * Root client handler is passed to every new web resource created by the
     * configured JAX-RS {@link Client}. It is the last client handler in the
     * request processing filter chain and is responsible for the ultimate
     * handling of the HTTP request and returning the HTTP response represented
     * as a {@link ClientResponse} instance.
     * 
     * @return root client handler.
     * @see #setRootHandler(ClientHandler)
     * @see Filterable
     */
    ClientHandler getRootHandler();

    /**
     * Set the new root {@link ClientHandler}.
     * <p />
     * This method may be used to override the default root client handler
     * provided by the client configuration implementation. Custom root client
     * handler can be used to plug in a custom implementation of HTTP request
     * processing.
     * 
     * @param handler the new root client handler instance.
     * @see #getRootHandler()
     * @see Filterable
     */
    void setRootHandler(ClientHandler handler);
}
