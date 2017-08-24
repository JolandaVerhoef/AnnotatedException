package com.jolandaverhoef.annotatedexception.processor;

import com.google.auto.service.AutoService;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.jolandaverhoef.annotatedexception.annotation.Exception")
public class AnnotatedExceptionProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {
        return false;
    }
}
