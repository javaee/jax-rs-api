/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxrs.examples.client.validator;

import javax.enterprise.util.AnnotationLiteral;
import javax.validation.Payload;

/**
 *
 * @author sp106478
 */
public class NotNull extends AnnotationLiteral<javax.validation.constraints.NotNull> 
    implements javax.validation.constraints.NotNull {

    @Override
    public String message() {
        return "{javax.validation.constraints.NotNull.message}";
    }

    @Override
    public Class<?>[] groups() {
        return new Class<?>[0];
    }

    @Override
    public Class<? extends Payload>[] payload() {
        return (Class<? extends Payload>[]) new Class<?>[0];
    }
    
}
