package com.avaya.outbound.lib.support;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.Field;

public final class TestDefaultElementLocatorFactory implements ElementLocatorFactory {
    private final SearchContext searchContext;

    public TestDefaultElementLocatorFactory(SearchContext searchContext) {
        this.searchContext = searchContext;
    }

    @Override
    public ElementLocator createLocator(Field field) {
        return new TestDefaultElementLocator(searchContext, field);
    }
}