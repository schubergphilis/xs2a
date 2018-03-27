package de.adorsys.aspsp.xs2a.web.annotation;


import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes("de.adorsys.aspsp.xs2a.web.annotation.ApiHeaders")
@SupportedSourceVersion( SourceVersion.RELEASE_8 )
public class ApiHeadersProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("=========process========");
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ApiHeaders.class);
        for (Element e : elements) {
            System.out.println("=========elements========"+ e);
        }
        return true;
    }

}
