package com.jolandaverhoef.annotatedexception.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface Exception {

    /**
     * An optional category. The code generated will be of the format
     * "[ANNOTATED_CLASS_NAME][CATEGORY]Exception"
     */
    String category() default "DEFAULT";
}
