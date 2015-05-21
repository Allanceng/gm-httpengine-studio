package org.gemini.httpengine.annotation;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by geminiwen on 15/5/20.
 */
public class AnnotationProcessor extends AbstractProcessor {

    private Filer filer;
    private Elements elementUtils;
    private Types typeUtils;

    private static final List<Class<? extends Annotation>> SUPPORT_ANNOTATIONS = Arrays.asList(
        GET.class,
        POST.class,
        Path.class
    );

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);

        filer = env.getFiler();
        elementUtils = env.getElementUtils();
        typeUtils = env.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        findAndParseTargets(roundEnv);
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportTypes = new LinkedHashSet<String>();
        for (Class<? extends Annotation> annotation: SUPPORT_ANNOTATIONS) {
            supportTypes.add(annotation.getCanonicalName());
        }
        return supportTypes;
    }

    private void findAndParseTargets(RoundEnvironment env) {
        for (Element element: env.getElementsAnnotatedWith(Path.class)) {
            TypeMirror elementType = element.asType();
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }
}
