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
package javax.ws.rs.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.ws.rs.Consumes;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.RuntimeDelegate;
import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.QName;

/**
 * Class representing hypermedia links. A hypermedia link may include additional
 * parameters beyond its underlying URI. Parameters such as "rel" or "method"
 * provide additional meta-data and can be used to easily create instances of
 * {@link javax.ws.rs.client.Invocation} in order to follow links.
 *
 * @author Marek Potociar
 * @author Santiago Pericas-Geertsen (Santiago.PericasGeertsen at oracle.com)
 * @see javax.ws.rs.client.Client#invocation
 * @since 2.0
 */
public final class Link {

    public static final String CONSUMES = "consumes";
    public static final String METHOD = "method";
    public static final String PRODUCES = "produces";
    public static final String TITLE = "title";
    public static final String REL = "rel";
    public static final String TYPE = "type";
    /**
     * This link's underlying URI.
     */
    private URI uri;
    /**
     * The URI context for this link.
     */
    private URI context;
    /**
     * A map for all the link's parameters such as "rel", "type", "method", etc.
     */
    private MultivaluedMap<String, String> map = new MultivaluedHashMap<String, String>();
    /**
     * Underlying implementation delegate.
     */
    private static final HeaderDelegate<Link> delegate =
            RuntimeDelegate.getInstance().createHeaderDelegate(Link.class);

    /**
     * Returns the underlying URI associated with this link.
     *
     * @return underlying URI
     */
    public URI getUri() {
        return uri;
    }

    /**
     * Convenience method that returns a {@link javax.ws.rs.core.UriBuilder}
     * initialized with this link's underlying URI.
     *
     * @return UriBuilder initialized using underlying URI
     */
    public UriBuilder getBuilder() {
        return UriBuilder.fromUri(uri);
    }

    /**
     * Returns context URI for this link, if set.
     *
     * @return context URI or null if not set.
     */
    public URI getContextUri() {
        return context;
    }

    /**
     * Returns a list containing all the relations types defined
     * on this link via the "rel" parameter. If no relation types are
     * defined, this method returns an empty list.
     *
     * @return list of relation types
     */
    public List<String> getRel() {
        List<String> l = map.get(REL);
        return (l != null) ? new ArrayList<String>(l) : Collections.<String>emptyList();
    }

    /**
     * Returns the value associated with the link "title" param, or
     * null if this param is not specified.
     *
     * @return value of "title" parameter or null
     */
    public String getTitle() {
        return map.getFirst(TITLE);
    }

    /**
     * Returns the value associated with the link "type" param, or
     * null if this param is not specified.
     *
     * @return value of "type" parameter or null
     */
    public String getType() {
        return map.getFirst(TYPE);
    }

    /**
     * Returns the value associated with the link "method" param, or
     * null if this param is not specified.
     *
     * @return value of "method" parameter or null
     */
    public String getHttpMethod() {
        return map.getFirst(METHOD);
    }

    /**
     * Returns a list containing all the types defined on this link
     * via the "produces" parameter. If no produces types are
     * defined, this method returns an empty list.
     *
     * @return list of produces types
     */
    public List<String> getProduces() {
        List<String> l = map.get(PRODUCES);
        return (l != null) ? new ArrayList<String>(l) : Collections.<String>emptyList();
    }

    /**
     * Returns a list containing all the types defined on this link
     * via the "consumes" parameter. If no consumes types are
     * defined, this method returns an empty list.
     *
     * @return list of consumes types
     */
    public List<String> getConsumes() {
        List<String> l = map.get(CONSUMES);
        return (l != null) ? new ArrayList<String>(l) : Collections.<String>emptyList();
    }

    /**
     * Returns an immutable map that includes all the link parameters
     * defined on this link. If defined, this map will include entries
     * for "rel", "title" and "type".
     *
     * @return Immutable map of link parameters
     */
    public MultivaluedMap<String, String> getParams() {
        return new MultivaluedHashMap<String, String>(map);
    }

    /**
     * Equality test for links.
     * @param other Object to compare against
     * @return True if equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof Link) {
            final Link olink = (Link) other;
            return uri.equals(olink.uri) && map.equals(olink.map)
                    && (context == null || context.equals(olink.context));
        }
        return false;
    }

    /**
     * Hash code computation for links.
     * @return Hash code for this link
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.uri != null ? this.uri.hashCode() : 0);
        hash = 89 * hash + (this.context != null ? this.context.hashCode() : 0);
        hash = 89 * hash + (this.map != null ? this.map.hashCode() : 0);
        return hash;
    }

    /**
     * Returns a simplified string representation for this link's value.
     * All link params are serialized as link-param="value" where value
     * is a list of space-separated tokens. For example,
     *
     * <http://foo.bar/employee/john>; title="employee"; rel="manager friend"
     *
     * @return String representation for this link
     */
    @Override
    public String toString() {
        return delegate.toString(this);
    }

    /**
     * Simple parser to convert link string representations into links.
     *
     * link ::= '<' uri '>' (';' link-param)*
     * link-param ::= name '=' quoted-string
     *
     * The resulting language is similar to that defined in RFC 5988.
     *
     * @param value String representation
     * @return New link
     * @throws IllegalArgumentException
     */
    public static Link valueOf(String value) throws IllegalArgumentException {
        return delegate.fromString(value);
    }

    /**
     * Create a new instance initialized from an existing URI.
     * @param uri a URI that will be used to initialize the LinkBuilder.
     * @return a new LinkBuilder
     * @throws IllegalArgumentException if uri is null
     */
    public static LinkBuilder fromUri(URI uri) throws IllegalArgumentException {
        LinkBuilder b = new LinkBuilder();
        b.uri(uri);
        return b;
    }

    /**
     * Create a new instance initialized from an existing URI.
     * @param uri a URI that will be used to initialize the LinkBuilder.
     * @return a new LinkBuilder
     * @throws IllegalArgumentException if uri is null
     */
    public static LinkBuilder fromUri(String uri) throws IllegalArgumentException {
        LinkBuilder b = new LinkBuilder();
        b.uri(uri);
        return b;
    }

    /**
     * Generate a link by introspecting a resource method. Finds the first method
     * of a given name and generates a link that includes parameters "method",
     * "produces" (if available) and "consumes" (if available). The value of "rel"
     * defaults to that of the method name.
     *
     * @param resource resource class
     * @param method name of resource method
     * @return link builder to further configure link
     * @throws IllegalArgumentException if any argument is null or no method is found
     */
    public static LinkBuilder fromResourceMethod(Class<?> resource, String method)
            throws IllegalArgumentException {
        return fromResourceMethod(resource, method, method);
    }

    /**
     * Generate a link by introspecting a resource method. Finds the first method
     * of a given name and generates a link that includes parameters "method",
     * "produces" (if available) and "consumes" (if available). The value of "rel"
     * must be specified as an argument.
     *
     * @param resource resource class
     * @param method name of resource method
     * @param rel value of "rel" parameter
     * @return link builder to further configure link
     * @throws IllegalArgumentException if any argument is null or no method is found
     */
    public static LinkBuilder fromResourceMethod(Class<?> resource, String method, String rel)
            throws IllegalArgumentException {
        if (resource == null || method == null || rel == null) {
            throw new IllegalArgumentException("All parameters must be non-null");
        }

        LinkBuilder lb = Link.fromUri(UriBuilder.fromResource(resource).build());
        lb.rel(rel);
        Method[] methods = resource.getMethods();
        for (Method m : methods) {
            if (m.getName().equals(method)) {
                for (Annotation a : m.getAnnotations()) {
                    Class<? extends Annotation> at = a.annotationType();
                    HttpMethod hm = at.getAnnotation(HttpMethod.class);
                    if (hm != null) {
                        lb.httpMethod(at.getSimpleName());
                    }
                    if (at == Produces.class) {
                        for (String p : ((Produces) a).value()) {
                            lb.produces(p);
                        }
                    }
                    Consumes cs = m.getAnnotation(Consumes.class);
                    if (at == Consumes.class) {
                        for (String p : ((Produces) a).value()) {
                            lb.consumes(p);
                        }
                    }
                }
                return lb;
            }
        }
        throw new IllegalArgumentException("Method '" + method + "' not found in class '"
                + resource.getName() + "'");
    }

    /**
     * Builder class for hypermedia links.
     *
     * @author Marek Potociar
     * @author Santiago Pericas-Geertsen (Santiago.PericasGeertsen at oracle.com)
     * @see Link
     * @since 2.0
     */
    public static class LinkBuilder {

        /**
         * Link being built by the builder.
         */
        private Link link = new Link();
        /**
         * Underlying builder for link's URI.
         */
        private UriBuilder uriBuilder;
        /**
         * Default "rel" value for this link.
         */
        private String defaultRel;

        /**
         * No default "rel" constructor.
         */
        protected LinkBuilder() {
            this.defaultRel = null;
        }

        /**
         * Constructor from default "rel" value.
         * @param defaultRel default value for "rel"
         */
        protected LinkBuilder(String defaultRel) {
            this.defaultRel = defaultRel;
        }

        /**
         * Set underlying URI for the link being constructed.
         *
         * @param uri underlying URI for link
         * @return the updated builder
         * @since 2.0
         */
        public LinkBuilder uri(URI uri) {
            uriBuilder = UriBuilder.fromUri(uri);
            return this;
        }

        /**
         * Set underlying string URI for the link being constructed.
         *
         * @param uri underlying URI for link
         * @return the updated builder
         * @throws IllegalArgumentException if string representation of URI is invalid
         * @since 2.0
         */
        public LinkBuilder uri(String uri) throws IllegalArgumentException {
            uriBuilder = UriBuilder.fromUri(uri);
            return this;
        }

        /**
         * Set underlying URI using a {@link javax.ws.rs.core.UriBuilder}.
         *
         * @param uriBuilder underlying {@link javax.ws.rs.core.UriBuilder} for link
         * @return the updated builder
         * @since 2.0
         */
        public LinkBuilder uriBuilder(UriBuilder uriBuilder) {
            this.uriBuilder = uriBuilder;
            return this;
        }

        /**
         * Set URI context for this link.
         *
         * @param context underlying context for link
         * @return the updated builder
         * @since 2.0
         */
        public LinkBuilder context(URI context) {
            link.context = context;
            return this;
        }

        /**
         * Set URI context for this link as a string.
         *
         * @param context underlying context for link
         * @return the updated builder
         * @throws IllegalArgumentException if string representation of URI is invalid
         * @since 2.0
         */
        public LinkBuilder context(String context) throws IllegalArgumentException {
            try {
                link.context = new URI(context);
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException(ex);
            }
            return this;
        }

        /**
         * Convenience method to set a link relation. More than one rel value can
         * be specified using this method.
         *
         * @param name relation name
         * @return the updated builder
         */
        public LinkBuilder rel(String name) {
            link.map.add(REL, name);
            return this;
        }

        /**
         * Convenience method to set a title on this link. If called more than once,
         * the previous value of title is overwritten.
         *
         * @param title title parameter of this link
         * @return the updated builder
         */
        public LinkBuilder title(String title) {
            link.map.putSingle(TITLE, title);
            return this;

        }

        /**
         * Convenience method to set a type on this link. More than one
         * type value can be specified using this method.
         *
         * @param type link type as string
         * @return the updated builder
         */
        public LinkBuilder type(String type) {
            link.map.add(TYPE, type);
            return this;
        }

        /**
         * Convenience method to set a type on this link. If called more than once,
         * the previous value of method is overwritten.
         *
         * @param method HTTP method name
         * @return the updated builder
         */
        public LinkBuilder httpMethod(String method) {
            link.map.putSingle(METHOD, method);
            return this;
        }

        /**
         * Convenience method to set a produces type on this link. More than one
         * type value can be specified using this method.
         *
         * @param type link type as string
         * @return the updated builder
         */
        public LinkBuilder produces(String type) {
            link.map.add(PRODUCES, type);
            return this;
        }

        /**
         * Convenience method to set a consumes type on this link. More than one
         * type value can be specified using this method.
         *
         * @param type link type as string
         * @return the updated builder
         */
        public LinkBuilder consumes(String type) {
            link.map.add(CONSUMES, type);
            return this;
        }

        /**
         * Set an arbitrary parameter on this link. This method supports adding
         * more than one parameter value for each parameter. It is recommended
         * to use the more specific methods {@link #httpMethod} or {@link #title}
         * when setting these single-valued parameters.
         *
         * @param name the name of the parameter
         * @param value the value set for the parameter
         * @return the updated builder
         * @throws IllegalArgumentException if either the name or value are null
         */
        public LinkBuilder param(String name, String value) throws IllegalArgumentException {
            if (name == null || value == null) {
                throw new IllegalArgumentException("Link parameter name or value is null");
            }
            link.map.add(name, value);
            return this;
        }

        /**
         * Finish building this link and return the instance.
         *
         * @return newly built link.
         */
        public Link build() {
            if (defaultRel != null && !link.map.containsKey(REL)) {
                link.map.add(REL, defaultRel);
            }
            link.uri = uriBuilder.build();
            return link;
        }

        /**
         * Finish building this link using the supplied values as URI parameters.
         *
         * @param values parameters used to build underlying URI
         * @return the updated builder
         * @throws UriBuilderException
         */
        public Link build(Object... values) throws UriBuilderException {
            if (defaultRel != null && !link.map.containsKey(REL)) {
                link.map.add(REL, defaultRel);
            }
            link.uri = uriBuilder.build(values);
            return link;
        }
    }

    /**
     * Value type for {@link javax.ws.rs.core.Link} that can be marshalled and
     * unmarshalled by JAXB.
     *
     * @author Santiago Pericas-Geertsen (Santiago.PericasGeertsen at oracle.com)
     * @see javax.ws.rs.core.Link.LinkAdapter
     * @since 2.0
     */
    public static class JaxbLink {

        private URI uri;
        private Map<QName, Object> params;

        public JaxbLink() {
        }

        public JaxbLink(URI uri) {
            this.uri = uri;
        }

        public JaxbLink(URI uri, Map<QName, Object> params) {
            this.uri = uri;
            this.params = params;
        }

        @XmlAttribute(name = "href")
        public URI getUri() {
            return uri;
        }

        @XmlAnyAttribute
        public Map<QName, Object> getParams() {
            if (params == null) {
                params = new HashMap<QName, Object>();
            }
            return params;
        }
    }

    /**
     * <p>An implementation of JAXB {@link javax.xml.bind.annotation.adapters.XmlAdapter}
     * that maps the JAX-RS {@link javax.ws.rs.core.Link} type to a value that can be
     * marshalled and unmarshalled by JAXB.</p>
     *
     * <p>All link parameters are treated as multi valued except for "title" and "method".</p>
     *
     * @author Santiago Pericas-Geertsen (Santiago.PericasGeertsen at oracle.com)
     * @see javax.ws.rs.core.Link.JaxbLink
     * @since 2.0
     */
    public static class LinkAdapter extends XmlAdapter<JaxbLink, Link> {

        @Override
        public Link unmarshal(JaxbLink v) throws Exception {
            Link.LinkBuilder lb = Link.fromUri(v.getUri());
            for (Entry<QName, Object> e : v.getParams().entrySet()) {
                final String name = e.getKey().getLocalPart();
                if (TITLE.equals(name) || METHOD.equals(name)) {
                    lb.param(name, e.getValue().toString());
                } else {
                    String[] values = e.getValue().toString().split(" ");
                    for (String value : values) {
                        lb.param(name, value);
                    }
                }
            }
            return lb.build();
        }

        @Override
        public JaxbLink marshal(Link v) throws Exception {
            JaxbLink jl = new JaxbLink(v.getUri());
            for (Entry<String, List<String>> e : v.getParams().entrySet()) {
                final String name = e.getKey();
                if (TITLE.equals(name) || METHOD.equals(name)) {
                    jl.getParams().put(new QName("", name), e.getValue().get(0));
                } else {
                    boolean first = true;
                    StringBuilder sb = new StringBuilder();
                    for (String value : e.getValue()) {
                        if (first) {
                            first = false;
                        } else {
                            sb.append(" ");
                        }
                        sb.append(value);
                    }
                    jl.getParams().put(new QName("", e.getKey()), sb.toString());
                }
            }
            return jl;
        }
    }
}