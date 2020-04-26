package net.jabba.compiler;

import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import java.util.Arrays;
import java.util.List;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;
import static java.util.Collections.singletonList;

class JabbaProcessorTest {

    @Test
    void process() {
        List<String> sourceLines = Arrays.asList("package test;",
                "class My {",
                "",
                "  @net.jabba.Recursive",
                "  static String foo() {",
                "    return \"\";",
                "  }",
                "}",
                "");
        JavaFileObject javaFile = JavaFileObjects.forSourceLines("test.My", sourceLines);
        assertAbout(javaSources()).that(singletonList(javaFile))
                .processedWith(new JabbaProcessor())
                .compilesWithoutError();
    }
}