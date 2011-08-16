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
 * Configurable instance contract.
 * <p />
 * When creating new {@link Target} instances or {@link Invocation Invocations} using
 * a {@link Client} instance, the setProperties and features set on the {@code Client}
 * instance are inherited by the child instances being created. Similarly,
 * when creating new {@code Invocations} or derived {@code Targets} using
 * a {@code Target} instance, the parent {@code Target} instance
 * setProperties and features are inherited by the child instances being created.
 * The set of inherited features and setProperties on the child instance reflects the
 * state of the parent set of features and setProperties at the time of the child
 * instance creation. Once the child instance is created its setProperties and features
 * are detached from the parent configuration. This means that any subsequent
 * changes in the parent configuration MUST NOT affect the configuration of
 * previously created child instances.
 * <p />
 * Once the child instance is created, it's configuration can be further customized
 * using the provided set of instance configuration mutator methods. A change
 * made in the configuration of a child instance MUST NOT affect the configuration
 * of its parent.
 * 
 *
 * @param <T> 
 * @author Marek Potociar
 * @since 2.0
 */
public interface Configurable<T extends Configurable> {

    // Getters      
    /**
     * Get the immutable setProperty bag for the configurable instance.
     * <p />
     *
     * @return the setProperty bag of the configurable instance.
     * @see Configurable
     */
    Map<String, Object> getProperties();

    /**
     * Get the setProperty value for the specified setProperty name.
     *
     * @param name setProperty name.
     * @return the setProperty value for the specified setProperty name or {@code null}
     *     if the setProperty with such name is not configured.
     * @see Configurable
     */
    Object getProperty(String name);

    /**
     * Get the immutable set of feature classes of enabled features on the
     * configurable instance.
     * <p />
     *
     * @return the enabled feature set. The returned value shall never be {@code null}.
     */
    Set<Class<? extends Feature>> getFeatures();

    /**
     * Determine if a feature is enabled for the configurable instance.
     *
     * @param feature the feature class.
     * @return {@code true} if the feature is enabled, otherwise {@code false}.
     * @see Configurable
     */
    boolean isEnabled(Class<? extends Feature> feature);
    
    /**
     * Get the immutable set of provider classes to be instantiated in the scope
     * of the configured instance.
     * <p>
     * A provider class is a Java class with a {@link javax.ws.rs.ext.Provider}
     * annotation declared on the class that implements a specific service
     * interface.
     *
     * @return the immutable set of provider classes. The returned value shall
     * never be {@code null}.
     * @see #getProviderSingletons()
     */
    Set<Class<?>> getProviderClasses();

    /**
     * Get the immutable set of singleton provider instances to be utilized by
     * the configured instance.
     * <p>
     * When the configured instance is initialized the set of provider instances
     * will be combined and take precedence over the instantiated provider
     * classes.
     *
     * @return the immutable set of provider instances. The returned value shall
     * never be {@code null}.
     * @see #getProviderClasses()
     */
    Set<Object> getProviderSingletons();

    // Mutators
    /**
     * Configure the instance using external configuration. Current configuration
     * of the instance is replaced.
     *
     * @param configuration configuration to be used to configure the instance.
     *
     * @return the updated configurable instance.
     */
    T configure(Configurable<?> configuration);
    
    /**
     * Register a provider class to be instantiated and used in the scope of the
     * configured instance.
     * <p/>
     * As opposed to the providers registered by the 
     * {@link #register(java.lang.Object) provider instances}, providers
     * registered using this method are instantiated per interaction.
     *
     * @param providerClass provider class to be instantiated and used in the scope
     *     of the configured instance.
     * @return the updated configurable instance.
     * @see #getProviderClasses()
     */
    T register(Class<?> providerClass);

    /**
     * Register one or more provider ("singleton") instances to be used in the
     * scope of the configured instance.
     * <p/>
     * As opposed to the providers registered by the 
     * {@link #register(java.lang.Class) provider classes}, provider instances
     * registered using this method behave in the scope of the configured instance
     * as singletons &ndash; a single provider instance is used to serve all the
     * interactions.
     *
     * @param providers one or more provider instances to be used in the scope of
     *     the configured instance.
     * @return the updated configurable instance.
     * @see #getProviderSingletons()
     */
    T register(Object... providers);    

    /**
     * Enable a feature for the configurable instance using a feature class.
     *
     * @param feature class of the feature to be enabled.
     * @return the updated configurable instance.
     * @see Configurable
     */
    T enable(Class<? extends Feature> feature);

    /**
     * Disable a feature for the configurable instance using a feature class.
     *
     * @param feature class of the feature to be disabled.
     * @return the updated configurable instance.
     * @see Configurable
     */
    T disable(Class<? extends Feature> feature);

    /**
     * Set new setProperties for the configurable instance (replaces everything
     * previously set).
     *
     * @param properties set of properties for the configurable instance.
     *     The content of the map will replace any existing setProperties set
     *     on the configurable instance.
     * @return the updated configurable instance.
     * @see Configurable
     */
    T setProperties(Map<String, Object> properties);

    /**
     * Set the configuration setProperty for the configurable instance.
     *
     * @param name setProperty name.
     * @param value setProperty value.
     * @return the updated configurable instance.
     * @see Configurable
     */
    T setProperty(String name, Object value);   
}
