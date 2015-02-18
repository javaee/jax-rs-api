/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2014-2015 Oracle and/or its affiliates. All rights reserved.
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
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * {@link javax.ws.rs.core.CacheControl} unit tests.
 *
 * @author Marek Potociar
 */
public class CacheControlTest {

    @Before
    public void setUp() throws Exception {
        RuntimeDelegate.setInstance(new RuntimeDelegateStub());
    }

    @After
    public void tearDown() throws Exception {
        RuntimeDelegate.setInstance(null);
    }

    /**
     * {@code CacheControl.equals()} contract test.
     *
     * This is a reproducer for JAX_RS_SPEC-471.
     */
    @Test
    public void testCacheControlsEqualsContract() {
        CacheControl first = new CacheControl();

        CacheControl second = new CacheControl();
        assertThat(first, equalTo(second));

        second.getCacheExtension(); // trigger lazy initialization
        assertThat(first, equalTo(second));
        assertThat(second, equalTo(first));

        CacheControl third = new CacheControl();
        third.getNoCacheFields(); // trigger lazy initialization
        assertThat(first, equalTo(third));
        assertThat(third, equalTo(first));
        assertThat(second, equalTo(third));
        assertThat(third, equalTo(second));

        CacheControl fourth = new CacheControl();
        fourth.getPrivateFields(); // trigger lazy initialization
        assertThat(first, equalTo(fourth));
        assertThat(fourth, equalTo(first));
        assertThat(second, equalTo(fourth));
        assertThat(fourth, equalTo(second));
        assertThat(third, equalTo(fourth));
        assertThat(fourth, equalTo(third));
    }

    /**
     * {@code CacheControl.hashCode()} contract test.
     *
     * This is a reproducer for JAX_RS_SPEC-471.
     */
    @Test
    public void testCacheControlsHashCodeContract() {
        CacheControl first = new CacheControl();

        CacheControl second = new CacheControl();
        assertThat(first.hashCode(), equalTo(second.hashCode()));

        second.getCacheExtension(); // trigger lazy initialization
        assertThat(first.hashCode(), equalTo(second.hashCode()));

        CacheControl third = new CacheControl();
        third.getNoCacheFields(); // trigger lazy initialization
        assertThat(first.hashCode(), equalTo(third.hashCode()));
        assertThat(second.hashCode(), equalTo(third.hashCode()));

        CacheControl fourth = new CacheControl();
        fourth.getPrivateFields(); // trigger lazy initialization
        assertThat(first.hashCode(), equalTo(fourth.hashCode()));
        assertThat(second.hashCode(), equalTo(fourth.hashCode()));
        assertThat(third.hashCode(), equalTo(fourth.hashCode()));
    }
}
