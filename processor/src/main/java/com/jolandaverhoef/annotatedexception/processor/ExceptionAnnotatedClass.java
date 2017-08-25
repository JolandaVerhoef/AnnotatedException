package com.jolandaverhoef.annotatedexception.processor;

import com.jolandaverhoef.annotatedexception.annotation.Exception;

import javax.lang.model.element.TypeElement;

class ExceptionAnnotatedClass {
    private TypeElement annotatedClassElement;
    private String category;

    /**
     * @throws ProcessingException if id() from annotation is null
     */
    ExceptionAnnotatedClass(TypeElement classElement) throws ProcessingException {
        this.annotatedClassElement = classElement;
        Exception annotation = classElement.getAnnotation(Exception.class);
        annotation.category();
    }

    /**
     * The original element that was annotated with @Exception
     */
    TypeElement getTypeElement() {
        return annotatedClassElement;
    }

    /**
     * The category as specified in {@link Exception#category()}
     */
    String getCategory() {
        return category;
    }
}
