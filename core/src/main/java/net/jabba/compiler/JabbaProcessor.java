package net.jabba.compiler;

import com.sun.source.tree.BlockTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.TreeVisitor;
import com.sun.source.util.SimpleTreeVisitor;
import com.sun.source.util.Trees;
import net.jabba.Recursive;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public final class JabbaProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Stream.of(Recursive.class)
                .map(Class::getCanonicalName)
                .collect(toSet());
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
        Set<ExecutableElement> methods = ElementFilter.methodsIn(env.getElementsAnnotatedWith(Recursive.class));
        for (ExecutableElement method : methods) {
            process(method);
        }
        return false;
    }

    private void process(ExecutableElement method) {
        Trees trees = Trees.instance(processingEnv);
        MethodTree tree = trees.getTree(method);
        BlockTree body = tree.getBody();
        List<? extends StatementTree> statements = body.getStatements();
        StatementTree statement = statements.get(0);
        TreeVisitor<Object, Object> treeVisitor = new SimpleTreeVisitor<Object, Object>() {
            @Override
            public Object visitReturn(ReturnTree node, Object o) {
                return super.visitReturn(node, o);
            }
        };
        Object accept = statement.accept(treeVisitor, "");
    }
}
