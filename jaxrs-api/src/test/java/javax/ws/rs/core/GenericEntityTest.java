/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2015 Oracle and/or its affiliates. All rights reserved.
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

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import static org.junit.Assert.*;

public class GenericEntityTest {

    @Test
    public void testListOfString() {
        System.out.println("testListOfString");
        List<String> list = new ArrayList<String>();
        GenericEntity<List<String>> listOfString = new GenericEntity<List<String>>(list) {
        };
        Class<?> rawType = listOfString.getRawType();
        assertEquals(rawType, ArrayList.class);
        Type type = listOfString.getType();
        assertTrue(type instanceof ParameterizedType);
        ParameterizedType pType = (ParameterizedType) type;
        Type typeArgs[] = pType.getActualTypeArguments();
        assertEquals(1, typeArgs.length);
        assertTrue(typeArgs[0] instanceof Class<?>);
        Class<?> typeArgType = (Class<?>) typeArgs[0];
        assertEquals(typeArgType, String.class);
    }

    @Test
    public void testMapOfStringInteger() {
        System.out.println("testMapOfStringInteger");
        Map<String, Integer> map = new HashMap<String, Integer>();
        GenericEntity<Map<String, Integer>> mapOfString = new GenericEntity<Map<String, Integer>>(map) {
        };
        Class<?> rawType = mapOfString.getRawType();
        assertEquals(rawType, HashMap.class);
        Type type = mapOfString.getType();
        assertTrue(type instanceof ParameterizedType);
        ParameterizedType pType = (ParameterizedType) type;
        Type typeArgs[] = pType.getActualTypeArguments();
        assertEquals(2, typeArgs.length);
        assertTrue(typeArgs[0] instanceof Class<?>);
        Class<?> typeArgType = (Class<?>) typeArgs[0];
        assertEquals(typeArgType, String.class);
        assertTrue(typeArgs[1] instanceof Class<?>);
        typeArgType = (Class<?>) typeArgs[1];
        assertEquals(typeArgType, Integer.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testArrayOfListOfString() {
        System.out.println("testArrayOfListOfString");
        List<String> array[] = new List[1];
        GenericEntity<List<String>[]> arrayOfListOfString = new GenericEntity<List<String>[]>(array) {
        };
        Class<?> rawType = arrayOfListOfString.getRawType();
        assertTrue(rawType.isArray());
        Type type = arrayOfListOfString.getType();
        assertTrue(type instanceof GenericArrayType);
        type = ((GenericArrayType) type).getGenericComponentType();
        assertTrue(type instanceof ParameterizedType);
        ParameterizedType pType = (ParameterizedType) type;
        Type typeArgs[] = pType.getActualTypeArguments();
        assertEquals(1, typeArgs.length);
        assertTrue(typeArgs[0] instanceof Class<?>);
        Class<?> typeArgType = (Class<?>) typeArgs[0];
        assertEquals(typeArgType, String.class);
    }

    @Test
    public void testNumber() {
        System.out.println("testNumber");
        Number n = new Integer(0);
        GenericEntity<Number> number = new GenericEntity<Number>(n) {
        };
        Class<?> rawType = number.getRawType();
        assertEquals(rawType, Integer.class);
        Type type = number.getType();
        assertTrue(type instanceof Class);
        Class<?> genericType = (Class<?>) type;
        assertEquals(genericType, Number.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCtor() {
        System.out.println("testCtor");
        try {
            // check GenericEntity(Integer, Number) works
            Method getNumber = this.getClass().getDeclaredMethod("getNumber");
            Type rt = getNumber.getGenericReturnType();
            GenericEntity ge = new GenericEntity(1, rt);
            // check GenericEntity(String, Number) fails
            try {
                ge = new GenericEntity("foo", rt);
                fail("Expected IllegalArgumentException");
            } catch (IllegalArgumentException e) {
            }
            // check GenericEntity(Integer[], Number[]) works
            Method getNumbers = this.getClass().getDeclaredMethod("getNumbers");
            rt = getNumbers.getGenericReturnType();
            Integer ints[] = {1, 2};
            ge = new GenericEntity(ints, rt);
            // check GenericEntity(String[], Number[]) fails
            try {
                String strings[] = {"foo", "bar"};
                ge = new GenericEntity(strings, rt);
                fail("Expected IllegalArgumentException");
            } catch (IllegalArgumentException e) {
            }
            // check GenericEntity(ArrayList<String>, List<String>) works
            Method getList = this.getClass().getDeclaredMethod("getList");
            rt = getList.getGenericReturnType();
            ArrayList<String> als = new ArrayList<String>();
            ge = new GenericEntity(als, rt);
            // check GenericEntity(ArrayList<Integer>, List<String>) works
            // note that erasure loses the generic type of the ArrayList
            ArrayList<Integer> ali = new ArrayList<Integer>();
            ge = new GenericEntity(ali, rt);
            // check GenericEntity(Set<String>, List<String>) fails
            try {
                Set<String> ss = new HashSet<String>();
                ge = new GenericEntity(ss, rt);
                fail("Expected IllegalArgumentException");
            } catch (IllegalArgumentException e) {
            }
            // check GenericEntity(ArrayList<String>[], List<String>[]) works
            Method getLists = this.getClass().getDeclaredMethod("getLists");
            rt = getLists.getGenericReturnType();
            ArrayList<String>[] lists = new ArrayList[1];
            ge = new GenericEntity(lists, rt);
            // check GenericEntity(ArrayList<Integer>[], List<String>[]) works
            // note that erasure loses the generic type of the ArrayList
            ArrayList<Integer>[] ilists = new ArrayList[1];
            ge = new GenericEntity(ilists, rt);
            // check GenericEntity(Set<String>[], List<String>[]) fails
            try {
                Set<String>[] ss = new Set[1];
                ge = new GenericEntity(ss, rt);
                fail("Expected IllegalArgumentException");
            } catch (IllegalArgumentException e) {
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unhandled Exception");
        }
    }

    private Number getNumber() {
        return null;
    }

    private Number[] getNumbers() {
        return null;
    }

    private List<String> getList() {
        return null;
    }

    private List<String>[] getLists() {
        return null;
    }
}
