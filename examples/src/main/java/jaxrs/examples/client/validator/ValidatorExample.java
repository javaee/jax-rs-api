/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011-2015 Oracle and/or its affiliates. All rights reserved.
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

package jaxrs.examples.client.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.enterprise.util.AnnotationLiteral;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

public class ValidatorExample {

    @java.lang.annotation.Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = EmailValidator.class)
    public @interface Email {

        String message() default "{foo.bar.validation.constraints.email}";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    public static class EmailValidator implements ConstraintValidator<Email, String> {

        @Override
        public void initialize(Email email) {
            // no-op
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            // Ensure value is a valid e-mail address
            return true;
        }
    }

    public class EmailImpl extends AnnotationLiteral<Email> implements Email {

        private static final long serialVersionUID = -3177939101972190621L;

        @Override
        public String message() {
            return "{javax.validation.constraints.NotNull.message}";
        }

        @Override
        public Class<?>[] groups() {
            return new Class<?>[0];
        }

        @Override
        @SuppressWarnings("unchecked")
        public Class<? extends Payload>[] payload() {
            return (Class<? extends Payload>[]) new Class<?>[0];
        }
    }
//    public void annotationBasedRequestResponseValidation() {
//        Client c = ClientBuilder.newClient();
//
//        HttpResponse response = c.target("http://example.com/foo/").post().entity("marek.potociar@oracle.com", new EmailImpl()).invoke();
//
//        String userId = response.annotateEntity(new NotNull(), new Pattern("[0-9]+")).getEntity(String.class);
//        System.out.println("User id = " + userId);
//    }
//    public void annotationBasedRequestParameterValidation() {
//        Client c = ClientBuilder.newClient();
//
//        final Target rootResource = c.target("http://example.com/foo");
//        String userId = rootResource.get().queryParam("email", "marek.potociar@oracle.com", new EmailImpl()).invoke(String.class);
//
//        // Path param validation using resource target:
//        HttpResponse r1 = rootResource.path("{userId}").pathParam("userId", userId, new Pattern("[0-9]+")).get().invoke();
//        assert r1.getStatus() == Response.Status.OK;
//
//        // Path param validation using invocation:
//        HttpResponse r2 = rootResource.path("{userId}").get().pathParam("userId", userId, new Pattern("[0-9]+")).invoke();
//        assert r2.getStatus() == Response.Status.OK;
//    }
//    public void example2() {
//        Client c = ClientBuilder.newClient();
//        /*
//        String response = c.resourceUri("http://example.com/foo/")
//        .put()
//        .entity("foo@bar.com", EmailValidator.class)
//        .invoke(String.class, EmailValidator.class);
//         */
//    }
}
