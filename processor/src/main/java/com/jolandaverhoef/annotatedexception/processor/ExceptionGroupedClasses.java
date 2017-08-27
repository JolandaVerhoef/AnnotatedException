package com.jolandaverhoef.annotatedexception.processor;


import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * This class holds all {@link ExceptionAnnotatedClass}s that belongs to one factory. In other words,
 * this class holds a list with all @Exception annotated classes. This class also checks if the id of
 * each @Exception annotated class is unique.
 *
 * @author Hannes Dorfmann
 */
class ExceptionGroupedClasses {

    /**
     * Will be added to the name of the generated factory class
     */
    private static final String SUFFIX = "ExceptionFactory";

    /**
     * Will be added to the name of the generated factory class
     */
    private String category;

    /**
     * The class name of the last added class.
     * Used to define where the generated Factory will reside.
     */
    private String qualifiedItemClassName;

    private Map<String, ExceptionAnnotatedClass> itemsMap = new LinkedHashMap<>();

    ExceptionGroupedClasses(String category) {
        this.category = category;
    }

    /**
     * Adds an annotated class to this factory.
     */
    void add(ExceptionAnnotatedClass toInsert) throws ProcessingException {
        itemsMap.put(toInsert.getTypeElement().getQualifiedName().toString(), toInsert);
        qualifiedItemClassName = toInsert.getTypeElement().getQualifiedName().toString();
    }

    void generateCode(Elements elementUtils, Filer filer) throws IOException {
        TypeElement superClassName = elementUtils.getTypeElement(qualifiedItemClassName);
        String factoryClassName = "DEFAULT".equals(category) ? SUFFIX : category + SUFFIX;
        PackageElement pkg = elementUtils.getPackageOf(superClassName);
        String packageName = pkg.isUnnamed() ? null : pkg.getQualifiedName().toString();

        MethodSpec.Builder method = MethodSpec.methodBuilder("create")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Class.class, "origin")
                .returns(Exception.class);

        // check if id is null
        method.beginControlFlow("if (origin == null)")
                .addStatement("throw new IllegalArgumentException($S)", "origin is null!")
                .endControlFlow();

        // Generate items map
        for (ExceptionAnnotatedClass item : itemsMap.values()) {
            method.beginControlFlow("if ($S.equals(origin.getCanonicalName()))", item.getTypeElement().getQualifiedName())
                    .addStatement("return new $L()", item.getResultCanonicalClassName())
                    .endControlFlow();
        }

        method.addStatement("throw new IllegalArgumentException($S + origin)", "Unknown origin = ");

        TypeSpec typeSpec = TypeSpec.classBuilder(factoryClassName)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(method.build())
                .build();

        // Write file
        JavaFile.builder(packageName, typeSpec).build().writeTo(filer);
    }
}
