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
package javax.ws.rs.core;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Represents a generic type {@code T}.
 *
 * @param <T> the generic type parameter.
 *
 * @author Paul Sandoz
 * @since 2.0
 */
public class GenericType<T> {

    private final Type t;
    private final Class c;

    /**
     * Constructs a new generic type, deriving the generic type and class from
     * type parameter. Note that this constructor is protected, users should create
     * a (usually anonymous) subclass as shown above.
     */
    protected GenericType() {
        Type superclass = getClass().getGenericSuperclass();
        if (!(superclass instanceof ParameterizedType)) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;

        this.t = parameterized.getActualTypeArguments()[0];
        this.c = getClass(this.t);
    }

    /**
     * Constructs a new generic type, supplying the generic type
     * information and deriving the class.
     *
     * @param genericType the generic type.
     * @throws IllegalArgumentException if genericType
     * is null or is neither an instance of Class or ParameterizedType whose raw
     * type is not an instance of Class.
     */
    public GenericType(final Type genericType) throws IllegalArgumentException {
        if (genericType == null) {
            throw new IllegalArgumentException("Type must not be null");
        }

        this.t = genericType;
        this.c = getClass(this.t);
    }

    private static Class getClass(final Type type) {
        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if (parameterizedType.getRawType() instanceof Class) {
                return (Class) parameterizedType.getRawType();
            }
        } else if (type instanceof GenericArrayType) {
            GenericArrayType array = (GenericArrayType) type;
            return getArrayClass((Class) ((ParameterizedType) array.getGenericComponentType()).getRawType());
        }
        throw new IllegalArgumentException("Type parameter not a class or "
                + "parameterized type whose raw type is a class");
    }

    /**
     * Gets underlying {@code Type} instance derived from the
     * type.
     *
     * @return the type.
     */
    public final Type getType() {
        return t;
    }

    /**
     * Gets underlying raw class instance derived from the
     * type.
     *
     * @return the class.
     */
    @SuppressWarnings("unchecked")
    public final Class<T> getRawClass() {
        return c;
    }

    /**
     * Get Array class of component class.
     *
     * @param c the component class of the array
     * @return the array class.
     */
    private static Class getArrayClass(final Class c) {
        try {
            Object o = Array.newInstance(c, 0);
            return o.getClass();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
