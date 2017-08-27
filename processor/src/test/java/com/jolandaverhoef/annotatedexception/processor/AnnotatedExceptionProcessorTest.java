package com.jolandaverhoef.annotatedexception.processor;

import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import com.jolandaverhoef.annotatedexception.annotation.Exception;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class AnnotatedExceptionProcessorTest {

    @Test
    public void testDefaultCategory() {
        Truth.assertAbout(javaSource())
                .that(defaultCategoryClass1Original)
                .processedWith(new AnnotatedExceptionProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(defaultCategoryClass1Generated)
                .and()
                .generatesSources(defaultCategoryFactory);
    }

    @Test
    public void testCustomCategory() {
        Truth.assertAbout(javaSource())
                .that(customCategoryClass1Original)
                .processedWith(new AnnotatedExceptionProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(customCategoryClass1Generated)
                .and()
                .generatesSources(customCategoryFactory);
    }

    private final static JavaFileObject defaultCategoryClass1Original =
            JavaFileObjects.forSourceLines(
                    "test.TestClass",
                    "package test;",
                    "",
                    "import " + Exception.class.getCanonicalName() + ";",
                    "",
                    "@Exception",
                    "public class TestClass {}"
            );

    private final static JavaFileObject defaultCategoryClass1Generated =
            JavaFileObjects.forSourceLines(
                    "test.TestClassException",
                    "package test;",
                    "",
                    "import java.lang.Exception;",
                    "",
                    "class TestClassException extends Exception {}"
            );

    private final static JavaFileObject defaultCategoryFactory =
            JavaFileObjects.forSourceLines(
                    "test.ExceptionFactory",
                    "package test;",
                    "",
                    "import java.lang.Class;",
                    "import java.lang.Exception;",
                    "",
                    "class ExceptionFactory {",
                    "  public Exception create(Class origin) {",
                    "    if (origin == null) {",
                    "      throw new IllegalArgumentException(\"origin is null!\");",
                    "    }",
                    "    if (\"test.TestClass\".equals(origin.getCanonicalName())) {",
                    "      return new test.TestClassException();",
                    "    }",
                    "    throw new IllegalArgumentException(\"Unknown origin = \" + origin);",
                    "  }",
                    "}"
            );


    private final static JavaFileObject customCategoryClass1Original =
            JavaFileObjects.forSourceLines(
                    "test.TestClass",
                    "package test;",
                    "",
                    "import " + Exception.class.getCanonicalName() + ";",
                    "",
                    "@Exception(category=\"Custom\")",
                    "public class TestClass {}"
            );

    private final static JavaFileObject customCategoryClass1Generated =
            JavaFileObjects.forSourceLines(
                    "test.TestClassCustomException",
                    "package test;",
                    "",
                    "import java.lang.Exception;",
                    "",
                    "class TestClassCustomException extends Exception {}"
            );

    private final static JavaFileObject customCategoryFactory =
            JavaFileObjects.forSourceLines(
                    "test.CustomExceptionFactory",
                    "package test;",
                    "",
                    "import java.lang.Class;",
                    "import java.lang.Exception;",
                    "",
                    "class CustomExceptionFactory {",
                    "  public Exception create(Class origin) {",
                    "    if (origin == null) {",
                    "      throw new IllegalArgumentException(\"origin is null!\");",
                    "    }",
                    "    if (\"test.TestClass\".equals(origin.getCanonicalName())) {",
                    "      return new test.TestClassCustomException();",
                    "    }",
                    "    throw new IllegalArgumentException(\"Unknown origin = \" + origin);",
                    "  }",
                    "}"
            );
}
