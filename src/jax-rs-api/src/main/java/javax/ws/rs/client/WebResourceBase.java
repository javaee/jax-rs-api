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
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;


/**
 * Base skeleton implementation of a filterable web resource and it's associated 
 * client request builder interface.
 * <p />
 * {@code WebResourceBase} class also defines basic client web resource contract
 * as well as some helper methods to be used by the extending classes when
 * implementing the defined contract.
 * <p />
 * Implementing classes are expected to implement at least one of the HTTP method
 * invoker client runtime contracts defined by the JAX-RS specification.
 *
 * @param <T> web resource type
 * @param <B> client request builder type associated with the web resource.
 * 
 * @author Marek Potociar
 * @see HttpMethodInvoker
 * @see AsyncHttpMethodInvoker
 * @since 2.0
 */
public abstract class WebResourceBase<T extends WebResourceBase, B extends InvocationBuilder> extends Filterable implements InvocationBuilder<B> {

    private final URI uri;

    /**
     * Construct using the root {@link ClientHandler} and web resource {@link URI}.
     * 
     * @param rootHandler the root client handler responsible for handling the request
     *     and returning a response.
     * @param uri {@link URI} identifying the new web resource.
     */
    protected WebResourceBase(ClientHandler rootHandler, URI uri) {
        super(rootHandler);
        this.uri = uri;
    }

    /**
     * Construct from an existing web resource base instance using the provided 
     * {@code URI} as an identifier of the newly constructed web resource.
     *
     * @param that the web resource base instance to copy.
     * @param uri {@link URI} identifying the new web resource.
     */
    protected WebResourceBase(T that, URI uri) {
        super(that);
        this.uri = uri;
    }

    /**
     * Create a new {@link UriBuilder} from this web resource using the provided
     * {@link URI} instance and following set of rules:
     * <p>
     * If the supplied URI contains a path component and the path starts
     * with a {@code '/'} then the path of this web resource URI is replaced. 
     * Otherwise the path represented by the supplied URI is appended.
     * <p>
     * If the supplied URI contains query parameters then those query parameters
     * will replace the query parameters (if any) of this web resource.
     *
     * @param uri the URI. May be {@code null}. In such case, the URI parameter
     *     will be ignored and the web resource URI will be used to construct the
     *     builder.
     * @return new URI builder instance.
     * @see #uri(java.net.URI)
     */
    protected final UriBuilder createUriBuilderFor(URI uri) {
        UriBuilder builder = getUriBuilder();
        String path = (uri == null) ? null : uri.getRawPath();
        if (path != null && path.length() > 0) {
            if (path.startsWith("/")) {
                builder.replacePath(path);
            } else {
                builder.path(path);
            }
        }
        String query = (uri == null) ? null : uri.getRawQuery();
        if (query != null && query.length() > 0) {
            builder.replaceQuery(query);
        }
        return builder;
    }

    /**
     * Create a new {@link UriBuilder} from this web resource with additional
     * query parameters added to the URI of this web resource.
     *
     * @param params the query parameters.
     * @return the new URI builder instance.
     * @throws IllegalArgumentException if the name or one of the values of
     *     any of the additional parameters is {@code null}.
     * @see #queryParams(MultivaluedMap)
     */
    protected final UriBuilder createUriBuilderFor(MultivaluedMap<String, String> params)
            throws IllegalArgumentException {

        UriBuilder builder = getUriBuilder();
        for (Map.Entry<String, List<String>> e : params.entrySet()) {
            for (String value : e.getValue()) {
                builder.queryParam(e.getKey(), value);
            }
        }
        return builder;
    }

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
     * Get the client request builder specific to the concrete web resource
     * implementation.
     *
     * @return the client request builder.
     */
    public abstract B getRequestBuilder();

    /**
     * Get the mutable {@link Map} containing the key-value pairs representing 
     * properties of the web resource.
     * <p />
     * Properties are inherited, thus creating a child web resource (for example via
     * {@code webResource.path("subpath")}) will set parents properties on it.
     * Also any modification of properties on a "parent" web resource instance
     * will be reflected in the properties of the child web resource. However 
     * modification of child resource properties won't reflect in the properties
     * of the parent resource.
     * <p/>
     * Methods {@link Map#entrySet()}, {@link Map#keySet()} and {@link Map#values()}
     * are returning read-only results 
     * (using {@link java.util.Collections#unmodifiableMap(java.util.Map)}).
     *
     * @return mutable map containing all properties of the web resource.
     */
    public abstract Map<String, Object> getProperties();

    /**
     * Create a new web resource instance from this web resource with an
     * additional path added to the URI of this web resource
     * (using {@link UriBuilder#path(java.lang.String)}).
     * <p>
     * Any filters on this web resource are inherited. Subsequent removal of
     * filters may cause undefined behaviour.
     *
     * @param path the additional path identifying the new web resource.
     * @return the new web resource.
     * @throws IllegalArgumentException if {@code path} parameter is {@code null}.
     */
    public abstract T path(String path) throws IllegalArgumentException;

    /**
     * Create a new web resource from this web resource with an additional single
     * query parameter added to the URI of this web resource.
     *
     * @param name the query parameter name
     * @param value the query parameter value
     * @return the new web resource.
     * @throws IllegalArgumentException if the name or the value of the query
     *     parameter is {@code null}.
     */
    public abstract T queryParam(String name, String value) throws IllegalArgumentException;

    /**
     * Create a new web resource from this web resource with additional multiple
     * query parameters added to the URI of this web resource.
     *
     * @param parameters the query parameters.
     * @return the new web resource.
     * @throws IllegalArgumentException if the name or one of the values of
     *     any of the additional parameters is {@code null}.
     */
    public abstract T queryParams(MultivaluedMap<String, String> parameters) throws IllegalArgumentException;

    /**
     * Create a new web resource from this web resource using the provided
     * {@link URI} instance and following set of rules:
     * <p>
     * If the supplied URI contains a path component and the path starts
     * with a {@code '/'} then the path of this web resource URI is replaced.
     * Otherwise the path represented by the supplied URI is appended.
     * <p>
     * If the supplied URI contains query parameters then those query parameters
     * will replace the query parameters (if any) of this web resource.
     *
     * @param uri the URI. May be {@code null}. In such case, the URI parameter
     *     will be ignored and the web resource URI will be used to construct the
     *     builder.
     * @return the new web resource.
     */
    public abstract T uri(URI uri);

    /**
     * Compares this web resource to the specified object.
     * <p>
     * The result is true if and only if the argument is not null, is a the instance
     * of the same class as this web resource and it's URI is equal to the URI of
     * this web resource.
     *
     * @param obj the object to compare this web resource against.
     * @return {@code true} if the web resources are equal; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof WebResourceBase) {
            final WebResourceBase that = (WebResourceBase) obj;

            boolean result = true;
            result &= that.uri.equals(this.uri);
            result &= that.getClass() == this.getClass();
            return result;
        }
        return false;
    }

    /**
     * Returns a hash code for this web resource.
     * <p>
     * The hash code is the combination of the hash codes of the implementation
     * class as well as the URI of this web resource.
     *
     * @return a hash code for this <code>WebResource</code>.
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + this.getClass().hashCode();
        hash = 29 * hash + uri.hashCode();
        
        return hash;
    }

    /**
     * String representation of the URI identifying the web resource.
     *
     * @return the String representation of the URI of the web resource.
     */
    @Override
    public String toString() {
        return uri.toString();
    }
}
