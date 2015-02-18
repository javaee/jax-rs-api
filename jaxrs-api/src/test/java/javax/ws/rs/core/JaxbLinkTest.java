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

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Unit test for JAX-RS Link marshalling and unmarshalling via JAXB.
 *
 * @author Marek Potociar (marek.potociar at oracle.com)
 */
public class JaxbLinkTest {

    @Test
    public void testSerializationOfJaxbLink() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Link.JaxbLink.class);
        final Marshaller marshaller = jaxbContext.createMarshaller();
        final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        Map<QName, Object> expectedParams = new HashMap<QName, Object>();
        final QName qName = new QName("http://example.ns", "foo");
        expectedParams.put(qName, "test");
        final URI expectedUri = URI.create("/foo/bar");
        Link.JaxbLink expected = new Link.JaxbLink(expectedUri, expectedParams);

        final StringWriter writer = new StringWriter();

        JAXBElement<Link.JaxbLink> jaxbLinkJAXBElement =
                new JAXBElement<Link.JaxbLink>(new QName("", "link"), Link.JaxbLink.class, expected);
        marshaller.marshal(jaxbLinkJAXBElement, writer);

        final Link.JaxbLink actual = unmarshaller.unmarshal(new StreamSource(
                new StringReader(writer.toString())), Link.JaxbLink.class).getValue();

        assertEquals("Unmarshalled JaxbLink instance not equal to the marshalled one.", expected, actual);
        assertEquals("Unmarshalled JaxbLink instance URI not equal to original.", expectedUri, actual.getUri());
        assertEquals("Unmarshalled JaxbLink instance params not equal to original.", expectedParams, actual.getParams());
    }

    @Test
    public void testEqualsHashCode() throws Exception {
        Link.JaxbLink first = new Link.JaxbLink();
        Link.JaxbLink second = new Link.JaxbLink();

        // trigger lazy initialization on first
        first.getParams();

        assertThat(first, equalTo(second));
        assertThat(second, equalTo(first));
        assertThat(first.hashCode(), equalTo(second.hashCode()));
    }
}
