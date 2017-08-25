package com.jolandaverhoef.annotatedexception.processor;

import com.jolandaverhoef.annotatedexception.annotation.Exception;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

class ExceptionAnnotatedClass {
    private final static String SUFFIX = "Exception";
    private TypeElement annotatedClassElement;
    private String resultCanonicalClassName;
    private String category;

    /**
     * @throws ProcessingException if id() from annotation is null
     */
    ExceptionAnnotatedClass(TypeElement classElement) throws ProcessingException {
        this.annotatedClassElement = classElement;
        Exception annotation = classElement.getAnnotation(Exception.class);
        category = annotation.category();
        if("DEFAULT".equals(category)) category = "";
        resultCanonicalClassName = classElement.getQualifiedName() + category + SUFFIX;
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

    void generateCode(Elements elementUtils, Filer filer) throws IOException {
        String className = annotatedClassElement.getSimpleName() + category + SUFFIX;
        PackageElement pkg = elementUtils.getPackageOf(annotatedClassElement);
        String packageName = pkg.isUnnamed() ? null : pkg.getQualifiedName().toString();

        TypeSpec typeSpec = TypeSpec.classBuilder(className).superclass(java.lang.Exception.class).build();

        // Write file
        JavaFile.builder(packageName, typeSpec).build().writeTo(filer);
    }

    public String getResultCanonicalClassName() {
        return resultCanonicalClassName;
    }
}
