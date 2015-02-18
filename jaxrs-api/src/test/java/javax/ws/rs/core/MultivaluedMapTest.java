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

import javax.ws.rs.ext.RuntimeDelegate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class MultivaluedMapTest {

    @Before
    public void setUp() throws Exception {
        RuntimeDelegate.setInstance(new RuntimeDelegateStub());
    }

    @After
    public void tearDown() throws Exception {
        RuntimeDelegate.setInstance(null);
    }

    @Test
    public void testEqualsIgnoreOrder() {
        MultivaluedHashMap<String, String> mvm1 = new MultivaluedHashMap<String, String>();
        mvm1.addAll("foo1", "bar1", "bar2");
        mvm1.addAll("foo2", "baz1", "baz2");
        MultivaluedHashMap<String, String> mvm2 = new MultivaluedHashMap<String, String>();
        mvm2.addAll("foo1", "bar2", "bar1");
        mvm2.addAll("foo2", "baz2", "baz1");
        assertTrue(mvm1.equalsIgnoreValueOrder(mvm2));
        assertTrue(mvm2.equalsIgnoreValueOrder(mvm1));
    }

    @Test
    public void testEqualsIgnoreOrderInclusion() {
        MultivaluedHashMap<String, String> mvm1 = new MultivaluedHashMap<String, String>();
        mvm1.addAll("foo1", "bar1", "bar2");
        MultivaluedHashMap<String, String> mvm2 = new MultivaluedHashMap<String, String>();
        mvm2.addAll("foo1", "bar2", "bar1");
        mvm2.addAll("foo2", "baz2", "baz1");
        assertFalse(mvm1.equalsIgnoreValueOrder(mvm2));
        assertFalse(mvm2.equalsIgnoreValueOrder(mvm1));
    }

    @Test
    public void testEqualsIgnoreListSize() {
        MultivaluedHashMap<String, String> mvm1 = new MultivaluedHashMap<String, String>();
        mvm1.addAll("foo1", "bar1", "bar2");
        MultivaluedHashMap<String, String> mvm2 = new MultivaluedHashMap<String, String>();
        mvm2.addAll("foo1", "bar2", "bar1", "bar3");
        assertFalse(mvm1.equalsIgnoreValueOrder(mvm2));
        assertFalse(mvm2.equalsIgnoreValueOrder(mvm1));
    }

    @Test
    public void testEqualsEmpty() {
        MultivaluedHashMap<String, String> mvm1 = new MultivaluedHashMap<String, String>();
        MultivaluedHashMap<String, String> mvm2 = new MultivaluedHashMap<String, String>();
        assertTrue(mvm1.equals(mvm2));
        assertTrue(mvm2.equals(mvm1));
        assertTrue(mvm1.equalsIgnoreValueOrder(mvm2));
        assertTrue(mvm2.equalsIgnoreValueOrder(mvm1));
    }

    @Test
    public void testEqualsSame() {
        MultivaluedHashMap<String, String> mvm1 = new MultivaluedHashMap<String, String>();
        assertTrue(mvm1.equals(mvm1));
        assertTrue(mvm1.equalsIgnoreValueOrder(mvm1));
    }

    @Test
    public void testEqualsWithDuplicates() {
        MultivaluedHashMap<String, String> mvm1 = new MultivaluedHashMap<String, String>();
        mvm1.addAll("foo1", "bar1", "bar2", "bar1");
        MultivaluedHashMap<String, String> mvm2 = new MultivaluedHashMap<String, String>();
        mvm2.addAll("foo1", "bar2", "bar1");
        assertFalse(mvm1.equalsIgnoreValueOrder(mvm2));
        assertFalse(mvm2.equalsIgnoreValueOrder(mvm1));
    }
}
