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
package javax.ws.rs.core;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

/**
 * Represents the the HTML form data request entity encoded using the
 * {@code "application/x-www-form-urlencoded"} content type.
 *
 * @author Marek Potociar
 * @since 2.0
 */
public class Form extends MultivaluedHashMap<String, String> {

    private static final long serialVersionUID = 9081959911712065219L;
    //
    private final MultivaluedHashMap<String, Annotation> parameterAnnotations = new MultivaluedHashMap<String, Annotation>();

    /**
     * Add a new form parameter with custom annotations attached.
     *
     * @param parameterName form parameter name.
     * @param value form parameter value.
     * @param annotations array of annotations to be attached to the form parameter.
     * @throws NullPointerException if the supplied parameter annotation array 
     *     is {@code null}.
     */
    public void add(String parameterName, String value, Annotation... annotations) {
        add(parameterName, value);
        parameterAnnotations.addAll(parameterName, annotations);
    }
    
    /**
     * Return a non-null list of annotations attached to a given form parameter.
     * If there are no annotations attached to the given parameter or if the
     * parameter does not exist, the returned list will be empty.
     *
     * @param parameterName the form parameter name.
     * @return annotations attached to the form parameter. If the parameter 
     *     does not exist or if there are no annotations attached to the parameter,
     *     an empty annotation list is returned. The method is guaranteed to never
     *     return {@code null}.
     */
    public List<Annotation> getParameterAnnotations(String parameterName) {
        List<Annotation> annotations = parameterAnnotations.get(parameterName);
        return (annotations != null) ? annotations : Collections.<Annotation>emptyList();
    }
}
