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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * A default client configuration. This class may be extended for specific
 * configuration purposes.
 *
 * @author Marek Potociar
 * @see ClientConfiguration
 * @since 2.0
 */
public class DefaultClientConfiguration implements ClientConfiguration {

    private final Set<Class<?>> providerClasses;
    private final Set<Object> singletonProviders;
    private final Set<String> features;
    private final Set<String> immutableFeaturesView;
    private final Map<String, Object> properties;
    private final Map<String, Object> immutablePropertiesView;

    /**
     * TODO javadoc.
     */
    public DefaultClientConfiguration() {
        this.providerClasses = new LinkedHashSet<Class<?>>();
        this.singletonProviders = new LinkedHashSet<Object>();

        this.features = new HashSet<String>();
        this.immutableFeaturesView = Collections.unmodifiableSet(features);

        this.properties = new HashMap<String, Object>();
        this.immutablePropertiesView = Collections.unmodifiableMap(properties);
    }

    /**
     * TODO javadoc.
     *
     * @param providerClasses TODO.
     */
    public DefaultClientConfiguration(final Class<?>... providerClasses) {
        this();

        Collections.addAll(this.providerClasses, providerClasses);
    }

    /**
     * TODO javadoc.
     *
     * @param providerClasses TODO.
     */
    public DefaultClientConfiguration(final Set<Class<?>> providerClasses) {
        this();

        this.providerClasses.addAll(providerClasses);
    }

    public DefaultClientConfiguration(ClientConfiguration original) {
        this.providerClasses = new LinkedHashSet<Class<?>>(original.getProviderClasses());
        this.singletonProviders = new LinkedHashSet<Object>(original.getProviderSingletons());

        this.features = new HashSet<String>(original.getFeatures());
        this.immutableFeaturesView = Collections.unmodifiableSet(features);

        this.properties = new HashMap<String, Object>(original.getProperties());
        this.immutablePropertiesView = Collections.unmodifiableMap(properties);
    }    

    @Override
    public Set<Class<?>> getProviderClasses() {
        return providerClasses;
    }

    @Override
    public Set<Object> getProviderSingletons() {
        return singletonProviders;
    }

    @Override
    public Set<String> getFeatures() {
        return immutableFeaturesView;
    }

    @Override
    public boolean isEnabled(final String featureName) {
        return features.contains(featureName);
    }

    @Override
    public Map<String, Object> getProperties() {
        return immutablePropertiesView;
    }

    @Override
    public Object getProperty(final String propertyName) {
        return properties.get(propertyName);
    }

    @Override
    public DefaultClientConfiguration property(String name, Object value) {
        properties.put(name, value);
        return this;
    }

    @Override
    public DefaultClientConfiguration enable(String featureName) {
        features.add(featureName);
        return this;
    }

    @Override
    public DefaultClientConfiguration disable(String featureName) {
        features.remove(featureName);
        return this;
    }

    @Override
    public DefaultClientConfiguration properties(Map<String, Object> newProperties) {
        properties.clear();
        properties.putAll(newProperties);
        return this;
    }

    @Override
    public DefaultClientConfiguration register(Class<?> providerClass) {
        providerClasses.add(providerClass);
        return this;
    }

    @Override
    public DefaultClientConfiguration register(Object singletonProvider) {
        singletonProviders.add(singletonProvider);        
        return this;
    }
}
