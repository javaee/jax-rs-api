/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2015 Oracle and/or its affiliates. All rights reserved.
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

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Type literal construction unit tests.
 *
 * @author Marek Potociar
 * @author Martin Matula
 */
public class GenericTypeTest {

    private static final Type arrayListOfStringsType = new ArrayList<String>() {

        private static final long serialVersionUID = 3109256773218160485L;
    }.getClass().getGenericSuperclass();

    public GenericTypeTest() {
    }

    private static class ParameterizedSubclass1 extends GenericType<ArrayList<String>> {
    }

    private static class ParameterizedSubclass2<T, V> extends GenericType<V> {
    }

    @Test
    public void testParameterizedSubclass1() {
        ParameterizedSubclass1 ps = new ParameterizedSubclass1() {
        };

        assertEquals(arrayListOfStringsType, ps.getType());
        assertEquals(ArrayList.class, ps.getRawType());
    }

    @Test
    public void testParameterizedSubclass2() {
        ParameterizedSubclass2<String, ArrayList<String>> ps =
                new ParameterizedSubclass2<String, ArrayList<String>>() {
                };

        assertEquals(arrayListOfStringsType, ps.getType());
        assertEquals(ArrayList.class, ps.getRawType());
    }

    @Test
    public void testConstructor() {

        GenericType type = new GenericType(new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{String.class};
            }

            @Override
            public Type getRawType() {
                return ArrayList.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        });

        assertEquals(ArrayList.class, type.getRawType());
        assertEquals(arrayListOfStringsType, type.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGenericType() throws NoSuchMethodException {
        ArrayList<String> al = new ArrayList<String>();
        Method addMethod = al.getClass().getMethod("add", Object.class);
        final Type type = addMethod.getGenericParameterTypes()[0];
        new GenericType(type);
    }

    @Test
    public void testConstructor2() {
        GenericType gt = new GenericType(arrayListOfStringsType);
        assertEquals(ArrayList.class, gt.getRawType());
        assertEquals(arrayListOfStringsType, gt.getType());
    }

    @Test
    public void testAnonymousConstruction() {
        GenericType<ArrayList<String>> tl = new GenericType<ArrayList<String>>() {
        };
        assertEquals(ArrayList.class, tl.getRawType());
        assertEquals(arrayListOfStringsType, tl.getType());
    }

    @Test
    public void testGenericTypeOfArray() {
        assertEquals(List[].class, new GenericType<List<String>[]>() {
        }.getRawType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullGenericType() {
        new GenericType(null);
    }

    // Regression test for JAX_RS_SPEC-274
    @Test
    public void testGenericTypeOfNonGenericArray() {
        assertEquals(String[].class, new GenericType<String[]>(){}.getRawType());
    }
}
