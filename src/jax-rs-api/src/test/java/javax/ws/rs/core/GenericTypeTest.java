/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Type literal construction unit tests.
 *
 * @author Marek Potociar (marek.potociar at oracle.com)
 */
public class GenericTypeTest {

    private static final Type arrayListOfStringsType = new ArrayList<String>() {

        private static final long serialVersionUID = 3109256773218160485L;
    }.getClass().getGenericSuperclass();

    public GenericTypeTest() {
    }

    @Test
    public void testStaticConstruction() {

        GenericType<ArrayList<String>> type = GenericType.<ArrayList<String>>of(ArrayList.class, new ParameterizedType() {

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
        final Type[] parameterTypes = type.getParameterTypes();

        assertEquals(1, parameterTypes.length);
        assertEquals(String.class, parameterTypes[0]);
    }

    @Test
    public void testAnnonymousConstruction() {
        GenericType<ArrayList<String>> tl = new GenericType<ArrayList<String>>() {
        };
        assertEquals(ArrayList.class, tl.getRawType());
        assertEquals(arrayListOfStringsType, tl.getType());
        final Type[] parameterTypes = tl.getParameterTypes();

        assertEquals(1, parameterTypes.length);
        assertEquals(String.class, parameterTypes[0]);
    }
}
