package org.gemini.httpengine.inject;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by geminiwen on 15/5/21.
 */
public class APIClassInjector {

    private final String classPackage;
    private final String className;
    private final String targetClass;
    private final Set<APIMethodInjector> methods;

    public APIClassInjector(String classPackage, String className, String targetClass) {
        this.classPackage = classPackage;
        this.className = className;
        this.targetClass = targetClass;
        this.methods = new LinkedHashSet<>();
    }

    public void addMethod(APIMethodInjector e) {
        methods.add(e);
    }

    public String getFqcn() {
        return classPackage + "." + className;
    }

    public String brewJava() throws Exception{
        StringBuilder builder = new StringBuilder("package " + this.classPackage + ";\n");
        builder.append("import org.gemini.httpengine.library.*;\n");

        builder.append("public class " + this.className + " implements " + this.targetClass + " {\n");
        for (APIMethodInjector methodInjector : methods) {
            builder.append(methodInjector.brewJava());
        }
        builder.append("}\n");
        return builder.toString();
    }
}
