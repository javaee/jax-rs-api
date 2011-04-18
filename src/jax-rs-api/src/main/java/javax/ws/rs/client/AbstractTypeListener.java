/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
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

import javax.ws.rs.core.GenericType;

/**
 * This class provides a skeletal implementation of the {@link TypeListener}
 * interface. It provides implementation for the methods
 * {@link TypeListener#getType() } and {@link TypeListener#getGenericType() }.
 * <p>
 * Instances of this class may be passed to appropriate methods on
 * {@link AsyncWebResource} (or more specifically methods on
 * {@link AsyncHttpMethodInvoker}). For example,
 * <blockquote><pre>
 *     AsyncWebResource r = ...;
 *     Future&lt;String&gt; f = r.get(new AbstractTypeListener&lt;String&gt;(String.class) {
 *         public void onComplete(Future&lt;String&gt; f) throws InterruptedException {
 *             try {
 *                 String s = f.get();
 *             } catch (CancellationException ex) {
 *                 // Do processing of cancelled operation
 *             } catch (ExecutionException ex) {
 *                 // Do error processing
 *                 Exception cause = ex.getCause();
 *                 if (cause instanceof HttpMethodInvocationException) {
 *                     // Request/response error
 *                 } else
 *                     // Error making request e.g. timeout
 *                 }
 *             }
 *         }
 *     });
 * </pre></blockquote>
 *
 * @param <T> the type of the response entity.
 *
 * @author Paul Sandoz
 * @author Marek Potociar
 * @since 2.0
 */
public abstract class AbstractTypeListener<T> implements TypeListener<T> {

    private final Class<T> type;
    private final GenericType<T> genericType;

// TODO implement determining type or genericType from reflection
//    public AbstractTypeListener() {
//        // determine type or genericType from reflection
//    }
//
    /**
     * Construct a new listener defining the class of the response to receive.
     *
     * @param type the class of the response.
     */
    public AbstractTypeListener(Class<T> type) {
        this.type = type;
        this.genericType = null;
    }

    /**
     * Construct a new listener defining the generic type of the response to
     * receive.
     *
     * @param genericType the generic type of the response.
     */
    public AbstractTypeListener(GenericType<T> genericType) {
        this.type = genericType.getRawClass();
        this.genericType = genericType;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public GenericType<T> getGenericType() {
        return genericType;
    }
}
