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

import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * {@link MediaType} unit test.
 *
 * @author Marek Potociar
 */
public class MediaTypeTest {

    /**
     * Test {@link MediaType#withCharset(String)} method.
     */
    @Test
    public void testWithCharset() {
        assertEquals("Unexpected produced media type content.",
                "UTF-8",
                MediaType.APPLICATION_XML_TYPE.withCharset("UTF-8")
                        .getParameters().get(MediaType.CHARSET_PARAMETER));
        assertEquals("Unexpected produced media type content.",
                "ISO-8859-13",
                MediaType.APPLICATION_XML_TYPE.withCharset("UTF-8").withCharset("ISO-8859-13")
                        .getParameters().get(MediaType.CHARSET_PARAMETER));
    }

    /**
     * Test that passing {@code null} values to {@link MediaType} constructor does
     * not throw a {@link NullPointerException} and produces expected result.
     */
    @Test
    public void testNullConstructorValues() {
        MediaType actual;

        actual = new MediaType(null, null, (Map<String, String>) null);
        assertEquals(MediaType.WILDCARD_TYPE, actual);

        actual = new MediaType(null, null, (String) null);
        assertEquals(MediaType.WILDCARD_TYPE, actual);
    }
}
