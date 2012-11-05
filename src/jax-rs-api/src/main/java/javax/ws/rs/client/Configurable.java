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
package javax.ws.rs.client;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Represents a client-side JAX-RS components that may be configured,
 * namely {@link Client}, {@link WebTarget}, {@link Invocation.Builder Invocation Builder}
 * or {@link Invocation}.
 * <p>
 * A latest configuration state of a {@code Configurable} component is inherited from a parent
 * component to a child component whenever a new child component is created.
 * For example, when creating a new {@link WebTarget resource targets} using a configured
 * {@link Client} instance, the configuration of the {@code Client} instance is inherited by
 * any child web target instances being created.
 * Similarly, when creating a new {@code Invocation.Builder} or a derived
 * {@code WebTarget} instance using a parent {@code WebTarget} instance, the configuration
 * of the parent web target is inherited by any such child instances being created.
 * <p>
 * </p>
 * The configuration inherited by a configured child instance reflects the configuration state
 * of the configured parent at the time of the child instance creation. Once the child instance
 * is created its configuration state is detached from the parent configuration state. This means
 * that any subsequent changes in a parent configuration state do not affect the configuration
 * state of any previously created children.
 * <p>
 * </p>
 * Once a child instance is created, it's configuration can be further customized
 * using a set of configured instance mutator methods defined by this interface. As the configuration
 * state of a parent and child configured instances is detached, a change made in the configuration
 * state of a child instance does not affect the configuration state of its parent, for example:
 * <pre>
 * Client client = ClientFactory.newClient();
 * client.setProperty("FOO_PROPERTY", "FOO_VALUE");
 *
 * // inherits the configured "FOO_PROPERTY" from the client instance
 * WebTarget resourceTarget = client.target("http://examples.jaxrs.com/");
 *
 * // does not modify the client instance configuration
 * resourceTarget.register(new BarFeature());
 * </pre>
 * </p>
 * <p>
 * For a related discussion on registering providers or narrowing down the scope of the contracts registered
 * for each provider, see {@link javax.ws.rs.core.Configuration runtime configuration context documentation}.
 * The set of {@code register(...)} methods exposed by the {@code Configuration} interface is semantically
 * compatible with the set of methods exposed by the {@code Configurable} interface.
 * </p>
 *
 * @author Marek Potociar
 * @since 2.0
 */
public interface Configurable {
    /**
     * Get the immutable bag of configuration properties.
     *
     * @return the immutable view of configuration properties.
     */
    public Map<String, Object> getProperties();

    /**
     * Get the value for the property with a given name.
     *
     * @param name property name.
     * @return the property value for the specified property name or {@code null}
     *         if the property with such name is not configured.
     */
    public Object getProperty(String name);

    /**
     * Returns an immutable {@link java.util.Collection collection} containing the
     * property names available within the context of the current configurable instance.
     * <p>
     * Use the {@link #getProperty} method with a property name to get the value of
     * a property.
     * </p>
     *
     * @return an immutable {@link java.util.Collection collection} of property names.
     * @see #getProperty
     */
    public Collection<String> getPropertyNames();

    /**
     * Set the new configuration property, if already set, the existing value of
     * the property will be updated. Setting a {@code null} value into a property
     * effectively removes the property from the property bag.
     *
     * @param name  property name.
     * @param value (new) property value. {@code null} value removes the property
     *              with the given name.
     * @return the updated configurable instance.
     */
    public Configurable setProperty(String name, Object value);

    /**
     * Get the immutable set of registered provider and feature classes to be instantiated,
     * injected and utilized in the scope of the configurable instance.
     *
     * @return the immutable set of registered provider and feature classes. The returned
     *         value shall never be {@code null}.
     * @see #getProviderInstances()
     */
    public Set<Class<?>> getProviderClasses();

    /**
     * Get the immutable set of registered provider and feature instances to be utilized by
     * the configurable instance.
     * <p>
     * When the configurable instance is initialized the set of provider and feature instances
     * will be combined with and take precedence over the instantiated registered provider
     * and feature classes.
     * </p>
     *
     * @return the immutable set of registered provider and feature instances. The returned
     *         value shall never be {@code null}.
     * @see #getProviderClasses()
     */
    public Set<Object> getProviderInstances();

    /**
     * Register a provider or a {@link javax.ws.rs.core.Feature feature} class to be instantiated
     * and used in the scope of the configurable instance.
     *
     * The registered class is registered as a provider of all the recognized JAX-RS or
     * implementation-specific extension contracts including {@code Feature} contract.
     * <p>
     * As opposed to the instances registered via {@link #register(Object)} method, classes
     * registered using this method are instantiated and properly injected
     * by the JAX-RS implementation provider. In case of a conflict between
     * a registered feature and/or provider instance and instantiated registered class,
     * the registered instance takes precedence and the registered class will not be
     * instantiated in such case.
     * </p>
     *
     * @param providerClass provider class to be instantiated and used in the scope
     *                      of the configurable instance.
     * @return the updated configurable instance.
     */
    public Configurable register(Class<?> providerClass);

    /**
     * Register a provider or a {@link javax.ws.rs.core.Feature feature} class to be instantiated and used
     * in the scope of the configurable instance.
     * <p>
     * This registration method provides same functionality as {@link #register(Class)}
     * except that any provider binding priority specified on the provider class using
     * {@link javax.ws.rs.BindingPriority &#64;BindingPriority} annotation is overridden
     * with the supplied {@code bindingPriority} value.
     * </p>
     * <p>
     * Note that in case the binding priority is not applicable to any particular provider
     * contract registered for the provider class, the supplied {@code bindingPriority} value
     * will be ignored for that contract.
     * </p>
     *
     * @param providerClass   provider class to be instantiated and used in the scope
     *                        of the configurable instance.
     * @param bindingPriority the overriding binding priority for the registered contract(s).
     * @return the updated configurable instance.
     */
    public Configurable register(Class<?> providerClass, int bindingPriority);

    /**
     * Register a provider or a {@link javax.ws.rs.core.Feature feature} class to be instantiated
     * and used in the scope of the configurable instance.
     * <p>
     * This registration method provides same functionality as {@link #register(Class)}
     * except the provider class is only registered as a provider of the listed
     * {@code contracts}. Note that in case the {@link javax.ws.rs.core.Feature} interface is not listed
     * explicitly, the provider class is not recognized as a JAX-RS feature.
     * </p>
     *
     * @param providerClass provider class to be instantiated and used in the scope
     *                      of the configurable instance.
     * @param contracts     the specific contracts implemented by the provider class
     *                      for which the provider should be registered. If omitted, the
     *                      provider class will be registered as a provider of all recognized
     *                      contracts implemented by the provider class.
     * @return the updated configurable instance.
     */
    public Configurable register(Class<?> providerClass, Class<?>... contracts);

    /**
     * Register a provider or a {@link javax.ws.rs.core.Feature feature} class to be instantiated
     * and used in the scope of the configurable instance.
     * <p>
     * This registration method provides same functionality as {@link #register(Class, Class[])}
     * except that any provider binding priority specified on the provider class using
     * {@link javax.ws.rs.BindingPriority &#64;BindingPriority} annotation is overridden
     * with the supplied {@code bindingPriority} value.
     * </p>
     * <p>
     * Note that in case the binding priority is not applicable to any particular provider
     * contract registered for the provider class, the supplied {@code bindingPriority} value
     * will be ignored for that contract.
     * </p>
     *
     * @param providerClass   provider class to be instantiated and used in the scope
     *                        of the configurable instance.
     * @param bindingPriority the overriding binding priority for the registered contract(s).
     * @param contracts       the specific contracts implemented by the provider class
     *                        for which the provider should be registered. If omitted, the
     *                        provider class will be registered as a provider of all recognized
     *                        contracts implemented by the provider class.
     * @return the updated configurable instance.
     */
    public Configurable register(Class<?> providerClass, int bindingPriority, Class<?>... contracts);

    /**
     * Register a provider or a {@link javax.ws.rs.core.Feature feature} ("singleton") instance to be used
     * in the scope of the configurable instance.
     *
     * The registered instance is registered as a provider of all the recognized JAX-RS or
     * implementation-specific extension contracts including {@code Feature} contract.
     * <p>
     * As opposed to the instances registered via {@link #register(Class)} method, classes
     * registered using this method used "as is", i.e. are not managed or
     * injected by the JAX-RS implementation provider. In case of a conflict between
     * a registered feature and/or provider instance and instantiated registered class,
     * the registered instance takes precedence and the registered class will not be
     * instantiated in such case.
     *
     * @param provider a provider instance to be registered in the scope of the configurable
     *                 instance.
     * @return the updated configurable instance.
     */
    public Configurable register(Object provider);

    /**
     * Register a provider or a {@link javax.ws.rs.core.Feature feature} ("singleton") instance to be used
     * in the scope of the configurable instance.
     * <p>
     * This registration method provides same functionality as {@link #register(Object)}
     * except that any provider binding priority specified on the provider using
     * {@link javax.ws.rs.BindingPriority &#64;BindingPriority} annotation is overridden
     * with the supplied {@code bindingPriority} value.
     * </p>
     * <p>
     * Note that in case the binding priority is not applicable to any particular provider
     * contract registered for the provider, the supplied {@code bindingPriority} value
     * will be ignored for that contract.
     * </p>
     *
     * @param provider        provider class to be instantiated and used in the scope
     *                        of the configurable instance.
     * @param bindingPriority the overriding binding priority for the registered contract(s).
     * @return the updated configurable instance.
     */
    public Configurable register(Object provider, int bindingPriority);

    /**
     * Register a provider or a {@link javax.ws.rs.core.Feature feature} ("singleton") instance to be used
     * in the scope of the configurable instance.
     * <p>
     * This registration method provides same functionality as {@link #register(Object)}
     * except the provider is only registered as a provider of the listed
     * {@code contracts}. Note that in case the {@link javax.ws.rs.core.Feature} interface is not listed
     * explicitly, the provider is not recognized as a JAX-RS feature.
     * </p>
     *
     * @param provider  a provider instance to be registered in the scope of the configurable
     *                  instance.
     * @param contracts the specific contracts implemented by the provider class
     *                  for which the provider should be registered. If omitted, the
     *                  provider class will be registered as a provider of all recognized
     *                  contracts implemented by the provider class.
     * @return the updated configurable instance.
     */
    public Configurable register(Object provider, Class<?>... contracts);

    /**
     * Register a provider or a {@link javax.ws.rs.core.Feature feature} ("singleton") instance to be used
     * in the scope of the configurable instance.
     * <p>
     * This registration method provides same functionality as {@link #register(Object, Class[])}
     * except that any provider binding priority specified on the provider using
     * {@link javax.ws.rs.BindingPriority &#64;BindingPriority} annotation is overridden
     * with the supplied {@code bindingPriority} value.
     * </p>
     * <p>
     * Note that in case the binding priority is not applicable to any particular provider
     * contract registered for the provider, the supplied {@code bindingPriority} value
     * will be ignored for that contract.
     * </p>
     *
     * @param provider        a provider instance to be registered in the scope of the configurable
     *                        instance.
     * @param bindingPriority the overriding binding priority for the registered contract(s).
     * @param contracts       the specific contracts implemented by the provider class
     *                        for which the provider should be registered. If omitted, the
     *                        provider class will be registered as a provider of all recognized
     *                        contracts implemented by the provider class.
     * @return the updated configurable instance.
     */
    public Configurable register(Object provider, int bindingPriority, Class<?>... contracts);

    /**
     * Replace the existing configuration state with a configuration state of an externally provided configurable
     * instance.
     *
     * @param configurable configuration state represented by a configurable instance to be used to update this
     *                     configurable instance.
     * @return the updated configurable instance.
     */
    public Configurable updateFrom(Configurable configurable);
}
