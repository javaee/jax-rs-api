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
package jaxrs.examples.client.webdav;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.client.Configurable;
import javax.ws.rs.client.Feature;
import javax.ws.rs.client.InvocationException;
import javax.ws.rs.client.Target;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Headers;
import javax.ws.rs.core.HttpResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Variant;

/**
 *
 * @author Marek Potociar (marek.potociar at oracle.com)
 */
public class WebDavTarget implements Target, WebDavSyncInvoker {
    
    private final Target target;

    public WebDavTarget(Target target) {
        this.target = target;
    }
    
    @Override
    public URI getUri() {
        return target.getUri();
    }

    @Override
    public UriBuilder getUriBuilder() {
        return target.getUriBuilder();
    }

    @Override
    public HttpResponse search(Object entity) {
        return target.method("SEARCH", entity);
    }
    
    @Override
    public WebDavTarget path(String path) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget pathParam(String name, Object value) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget pathParam(String name, Object value, Annotation... annotations) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget pathParams(MultivaluedMap<String, Object> parameters) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget matrixParam(String name, Object... values) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget queryParam(String name, Object value) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget queryParam(String name, Object value, Annotation... annotations) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget queryParams(MultivaluedMap<String, Object> parameters) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavPreparedBuilder prepare() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavAsyncInvoker async() {
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
    public WebDavTarget configure(Configurable<?> configuration) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget register(Class<?> providerClass) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget register(Object... providers) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget enable(Class<? extends Feature> feature) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget disable(Class<? extends Feature> feature) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget setProperties(Map<String, Object> properties) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget setProperty(String name, Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTargetedBuilder accept(MediaType... types) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTargetedBuilder accept(String... types) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTargetedBuilder acceptLanguage(Locale... locales) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTargetedBuilder acceptLanguage(String... locales) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTargetedBuilder cookie(Cookie cookie) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTargetedBuilder allow(String... methods) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTargetedBuilder allow(Set<String> methods) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTargetedBuilder cacheControl(CacheControl cacheControl) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTargetedBuilder encoding(String encoding) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTargetedBuilder header(String name, Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTargetedBuilder headers(Headers headers) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTargetedBuilder language(String language) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTargetedBuilder language(Locale language) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTargetedBuilder type(MediaType type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTargetedBuilder type(String type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTargetedBuilder variant(Variant variant) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HttpResponse get() throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T get(Class<T> responseType) throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T get(GenericType<T> responseType) throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HttpResponse put(Object entity) throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T put(Object entity, Class<T> responseType) throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T put(Object entity, GenericType<T> responseType) throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HttpResponse post(Object entity) throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T post(Object entity, Class<T> responseType) throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T post(Object entity, GenericType<T> responseType) throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HttpResponse delete() throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T delete(Class<T> responseType) throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T delete(GenericType<T> responseType) throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HttpResponse head() throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T head(Class<T> responseType) throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T head(GenericType<T> responseType) throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HttpResponse options() throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T options(Class<T> responseType) throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T options(GenericType<T> responseType) throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HttpResponse trace(Object entity) throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T trace(Object entity, Class<T> responseType) throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T trace(Object entity, GenericType<T> responseType) throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HttpResponse method(String name) throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T method(String name, Class<T> responseType) throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T method(String name, GenericType<T> responseType) throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HttpResponse method(String name, Object entity) throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T method(String name, Object entity, Class<T> responseType) throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T method(String name, Object entity, GenericType<T> responseType) throws InvocationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
  
}
