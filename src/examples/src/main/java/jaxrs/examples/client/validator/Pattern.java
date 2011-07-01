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
public class Pattern extends AnnotationLiteral<javax.validation.constraints.Pattern> 
    implements javax.validation.constraints.Pattern {

    private String regexp;
    
    public Pattern(String regexp) {
        this.regexp = regexp;
    }
    
    @Override
    public String message() {
        return "{javax.validation.constraints.Pattern.message}";
    }

    @Override
    public Class<?>[] groups() {
        return new Class<?>[0];
    }

    @Override
    public Class<? extends Payload>[] payload() {
        return (Class<? extends Payload>[]) new Class<?>[0];
    }

    @Override
    public String regexp() {
        return regexp;
    }

    @Override
    public Flag[] flags() {
        return new Flag[0];
    }
    
}
