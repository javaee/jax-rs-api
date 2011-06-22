package jaxrs.examples.client.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientFactory;

public class ValidatorExample {

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = EmailValidator.class)
    public @interface Email {
        String message() default "{foo.bar.validation.constraints.email}";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    public static class EmailValidator implements ConstraintValidator<Email, String> {
        public void initialize(Email email) {
            // no-op
        }
        public boolean isValid(String value, ConstraintValidatorContext context) {
            // Ensure value is a valid e-mail address
            return true;
        }
    }

    public void example1() {
        Client c = ClientFactory.newClient();
        /*
        String response = c.resourceUri("http://example.com/foo/")
                .get()
                .invoke(String.class, EmailValidator.class);
         */
    }
    
    public void example2() {
        Client c = ClientFactory.newClient();
        /*
        String response = c.resourceUri("http://example.com/foo/")
                .put()
                .entity("foo@bar.com", EmailValidator.class)
                .invoke(String.class, EmailValidator.class);
         */
    }

}
