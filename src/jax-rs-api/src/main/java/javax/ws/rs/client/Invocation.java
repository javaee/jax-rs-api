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

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Future;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Headers;
import javax.ws.rs.core.HttpRequest;
import javax.ws.rs.core.TypeLiteral;
import javax.ws.rs.core.HttpResponse;

/**
 *
 * @author Marek Potociar
 */
public interface Invocation {

    public static interface Builder extends SyncInvoker {

        // Invocation builder methods
        // TODO: document that the request instance needs to be cloned so that the
        // data used in the invocation processing chain are decoupled from the original
        // request data that were used to initiate the invocation to prevent accidental
        // issues caused by mutable nature of the request
        public Invocation build(String method);

        public Invocation build(String method, Entity<?> entity);

        public Invocation buildGet();

        public Invocation buildDelete();

        public Invocation buildPost(Entity<?> entity);

        public Invocation buildPut(Entity<?> entity);

        public AsyncInvoker async();

        // Headers builder methods
        public Builder acceptLanguage(Locale... locales);

        public Builder acceptLanguage(String... locales);

        public Builder cookie(Cookie cookie);

        public Builder allow(String... methods);

        public Builder allow(Set<String> methods);

        public Builder cacheControl(CacheControl cacheControl);

        public Builder header(String name, Object value);

        public Builder headers(Headers headers);

        // Configuration
        public Configuration configuration();
    }

    public HttpResponse invoke() throws InvocationException;

    public <T> T invoke(Class<T> responseType) throws InvocationException;

    public <T> T invoke(TypeLiteral<T> responseType) throws InvocationException;

    public Future<HttpResponse> submit();

    public <T> Future<T> submit(Class<T> responseType);

    public <T> Future<T> submit(TypeLiteral<T> responseType);

    public <T> Future<T> submit(InvocationCallback<T> callback);

    // Request data accessor
    public HttpRequest asRequest();

    // Configuration
    public Configuration configuration();
}
