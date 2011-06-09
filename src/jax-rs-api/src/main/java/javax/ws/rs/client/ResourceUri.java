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

import javax.ws.rs.core.HttpRequest;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.Providers;

/**
 * TODO javadoc.
 *
 * @author Marek Potociar
 * @since 2.0
 */
public interface ResourceUri extends HttpRequest.Builder<Invocation> {

    // Getters
    /**
     * Get the URI identifying the resource.
     *
     * @return the resource URI.
     */
    URI getURI();

    /**
     * Get the URI builder initialized with the {@link URI} identifying the
     * resource.
     *
     * @return the initialized URI builder.
     */
    UriBuilder getUriBuilder();

    /**
     * Get the {@link Providers} utilized by the {@code ResourceUri} instance.
     *
     * @return the {@link Providers} utilized by the {@code ResourceUri} instance.
     */
    Providers getProviders();

    /**
     * Get the immutable resource property bag.
     * <p />
     * When creating new {@link ResourceUri} instances or {@link Invocation}s using
     * a {@link Client} instance, the properties and features set on the {@code Client}
     * instance are inherited by the child instances being created. Similarly,
     * when creating new {@code Invocations} or derived {@code ResourceUri}s using
     * a {@code ResourceUri} instance, the parent {@code ResourceUri} instance
     * properties and features are inherited by the child instances being created.
     * The set of inherited features and properties on the child instance reflects the
     * state of the parent set of features and properties at the time of the child
     * instance creation. Once the child instance is created its properties and features
     * are detached from the parent configuration. This means that any subsequent
     * changes in the parent configuration MUST NOT affect the configuration of
     * previously created child instances.
     * <p />
     * Once the child instance is created, it's configuration can be further customized
     * using the provided set of instance configuration mutator methods. A change
     * made in the configuration of a child instance MUST NOT affect the configuration
     * of its parent.
     *
     * @return the property bag.
     */
    Map<String, Object> getProperties();

    /**
     * Determine if a feature is enabled for the particular resource.
     *
     * @param featureName the name of the feature.
     * @return {@code true} if the feature value is present in the property bag
     *     and is an instance of {@link java.lang.Boolean} and that value is
     *     {@code true}, otherwise {@code false}.
     * @see #getProperties()
     */
    boolean isEnabled(final String featureName);

    /**
     * Set the configuration property for the particular resource.
     *
     * @param name property name.
     * @param value property value.
     * @return the updated {@code ResourceUri} instance.
     * @see #getProperties()
     */
    ResourceUri property(String name, Object value);

    /**
     * Enable a feature for the particular resource.
     *
     * @param featureName feature name.
     * @return the updated {@code ResourceUri} instance.
     * @see #getProperties()
     */
    ResourceUri enable(String featureName);

    /**
     * Disable a feature for the particular resource.
     *
     * @param featureName feature name.
     * @return the updated {@code ResourceUri} instance.
     * @see #getProperties()
     */
    ResourceUri disable(String featureName);

    /**
     * Set new properties for the particular resource (replaces everything
     * previously set).
     *
     * @param properties set of properties for the resource. The content of
     *     the map will replace any existing properties set on the resource.
     * @return the updated {@code ResourceUri} instance.
     * @see #getProperties()
     */
    ResourceUri properties(Map<String, Object> properties);

    // Sub-resource URI buidler methods
    ResourceUri path(String path) throws IllegalArgumentException;

    ResourceUri pathParam(String name, Object value) throws IllegalArgumentException;

    ResourceUri pathParams(MultivaluedMap<String, Object> parameters) throws IllegalArgumentException;

    ResourceUri matrixParam(String name, Object... values) throws IllegalArgumentException;

    ResourceUri queryParam(String name, Object value) throws IllegalArgumentException;

    ResourceUri queryParams(MultivaluedMap<String, Object> parameters)
            throws IllegalArgumentException;
}
