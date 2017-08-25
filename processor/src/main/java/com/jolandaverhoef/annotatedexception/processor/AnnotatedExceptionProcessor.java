package com.jolandaverhoef.annotatedexception.processor;

import com.google.auto.service.AutoService;
import com.jolandaverhoef.annotatedexception.annotation.Exception;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes("com.jolandaverhoef.annotatedexception.annotation.Exception")
public class AnnotatedExceptionProcessor extends AbstractProcessor {

    private final static String DEFAULT_CATEGORY_NAME = "DEFAULT";

    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    private Map<String, ExceptionGroupedClasses> exceptionClasses = new LinkedHashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {
        try {
            for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(Exception.class)) {

                // Check if a class has been annotated with @Exception
                if (annotatedElement.getKind() != ElementKind.CLASS) {
                    throw new ProcessingException(annotatedElement,
                            "Only classes can be annotated with @%s",
                            Exception.class.getSimpleName());
                }

                // We can cast it, because we know that it of ElementKind.CLASS
                TypeElement typeElement = (TypeElement) annotatedElement;

                ExceptionAnnotatedClass annotatedClass = new ExceptionAnnotatedClass(typeElement);

                checkValidClass(annotatedClass);

                // Everything is fine, so try to add
                String category = annotatedClass.getCategory();
                if(category == null) category = DEFAULT_CATEGORY_NAME;
                ExceptionGroupedClasses exceptionClass =
                        exceptionClasses.get(category);
                if (exceptionClass == null) {
                    exceptionClass = new ExceptionGroupedClasses(category);
                    exceptionClasses.put(category, exceptionClass);
                }

                exceptionClass.add(annotatedClass);
            }

            // Generate code
            for (ExceptionGroupedClasses exceptionClass : exceptionClasses.values()) {
                exceptionClass.generateCode(elementUtils, filer);
            }
            exceptionClasses.clear();
        } catch (ProcessingException e) {
            error(e.getElement(), e.getMessage());
        } catch (IOException e) {
            error(null, e.getMessage());
        }

        return true;
    }

    /**
     * Checks if the annotated element observes our rules
     */
    private void checkValidClass(ExceptionAnnotatedClass item) throws ProcessingException {

        // Cast to TypeElement, has more type specific methods
        TypeElement classElement = item.getTypeElement();

        // TODO: Check if the class needs to be public
        if (!classElement.getModifiers().contains(Modifier.PUBLIC)) {
            throw new ProcessingException(classElement, "The class %s is not public.",
                    classElement.getQualifiedName().toString());
        }

        // TODO: Check if the class needs to be non-abstract
        // Check if it's an abstract class
        if (classElement.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new ProcessingException(classElement,
                    "The class %s is abstract. You can't annotate abstract classes with @%",
                    classElement.getQualifiedName().toString(), Exception.class.getSimpleName());
        }
    }

    /**
     * Prints an error message
     *
     * @param e The element which has caused the error. Can be null
     * @param msg The error message
     */
    private void error(Element e, String msg) {
        messager.printMessage(Diagnostic.Kind.ERROR, msg, e);
    }

}
