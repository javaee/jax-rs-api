/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011-2015 Oracle and/or its affiliates. All rights reserved.
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

import java.net.URI;
import java.util.Map;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

/**
 * Example of WebTarget extension to support WebDAV.
 *
 * @author Marek Potociar
 */
public class WebDavTarget implements WebTarget {

    private final WebTarget target;

    public WebDavTarget(WebTarget target) {
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
    public WebDavTarget path(String path) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget resolveTemplate(String name, Object value) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget resolveTemplate(String name, Object value, boolean encodeSlashInPath) throws NullPointerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget resolveTemplateFromEncoded(String name, Object value) throws NullPointerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget resolveTemplates(Map<String, Object> templateValues) throws NullPointerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget resolveTemplatesFromEncoded(Map<String, Object> templateValues) throws NullPointerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget resolveTemplates(Map<String, Object> parameters, boolean encodeSlashInPath) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget matrixParam(String name, Object... values) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget queryParam(String name, Object... values) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTargetedBuilder request() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTargetedBuilder request(String... mediaTypes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTargetedBuilder request(MediaType... mediaTypes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget property(String name, Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget register(Class<?> componentClass) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget register(Class<?> componentClass, int priority) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget register(Class<?> componentClass, Class<?>... contracts) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget register(Class<?> providerClass, Map<Class<?>, Integer> contracts) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget register(Object component) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget register(Object component, int priority) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget register(Object component, Class<?>... contracts) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WebDavTarget register(Object provider, Map<Class<?>, Integer> contracts) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Configuration getConfiguration() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
