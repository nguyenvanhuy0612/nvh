package com.avaya.outbound.lib.support;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class OutboundPageFactory {

    public static <T> T initElements(SearchContext searchContext, Class<T> pageClassToProxy) {
        T page = instantiatePage(searchContext, pageClassToProxy);
        initElements(searchContext, page);
        return page;
    }

    public static void initElements(SearchContext searchContext, Object page) {
        initElements(new TestDefaultElementLocatorFactory(searchContext), page);
    }

    public static void initElements(ElementLocatorFactory factory, Object page) {
        initElements(new TestDefaultFieldDecorator(factory), page);
    }

    public static void initElements(FieldDecorator decorator, Object page) {
        Class<?> proxyIn = page.getClass();
        while (proxyIn != Object.class) {
            proxyFields(decorator, page, proxyIn);
            proxyIn = proxyIn.getSuperclass();
        }
    }

    private static void proxyFields(FieldDecorator decorator, Object page, Class<?> proxyIn) {
        Field[] fields = proxyIn.getDeclaredFields();
        for (Field field : fields) {
            Object value = decorator.decorate(page.getClass().getClassLoader(), field);
            if (value != null) {
                try {
                    field.setAccessible(true);
                    field.set(page, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static <T> T instantiatePage(SearchContext searchContext, Class<T> pageClassToProxy) {
        try {
            try {
                Constructor<T> constructor = pageClassToProxy.getConstructor(WebDriver.class);
                return constructor.newInstance(searchContext);
            } catch (NoSuchMethodException e) {
                return pageClassToProxy.getDeclaredConstructor().newInstance();
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
