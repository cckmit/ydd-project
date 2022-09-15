package com.ydw.oa.auth.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserOpera {
	
	String value();
	
	String type();
 
    /**
     * Corresponds to the 'notes' field of the operation.
     * <p>
     * A verbose description of the operation.
     */
    String notes() default "";

}
