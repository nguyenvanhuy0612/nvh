package com.avaya.outbound.lib.support;


import org.openqa.selenium.By;
import org.openqa.selenium.support.AbstractFindByBuilder;
import org.openqa.selenium.support.PageFactoryFinder;
import org.openqa.selenium.support.pagefactory.Annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class TestAnnotations extends Annotations {

    public TestAnnotations(Field field) {
        super(field);
    }

    @Override
    public By buildBy() {
        Field field = super.getField();
        assertValidAnnotations();
        By ans = null;
        for (Annotation annotation : field.getDeclaredAnnotations()) {
            AbstractFindByBuilder builder = null;
            if (annotation.annotationType().isAnnotationPresent(PageFactoryFinder.class)) {
                try {
                    builder = annotation.annotationType()
                            .getAnnotation(PageFactoryFinder.class).value()
                            .getDeclaredConstructor().newInstance();
                } catch (ReflectiveOperationException e) {
                    // Fall through.
                }
            }
            if (builder != null) {
                ans = builder.buildIt(annotation, field);
                break;
            }
        }

        // custom by locator xpath by classname file and common file
        if (ans == null) {
            try {
                ans = Locator.getBy(field.getDeclaringClass().getSimpleName(), field.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // custom by locator data testid or id or name with locator same as name field
        if (ans == null) {
            ans = buildByFromDataTestIdOrIdOrName(field);
        }

        return ans;
    }

    protected By buildByFromDataTestIdOrIdOrName(Field field) {
        return new ByDataTestIdOrIdOrName(field.getName());
    }
}
