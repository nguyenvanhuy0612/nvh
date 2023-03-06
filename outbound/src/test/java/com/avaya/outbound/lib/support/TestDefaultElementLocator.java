package com.avaya.outbound.lib.support;


import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;

import java.lang.reflect.Field;


public class TestDefaultElementLocator extends DefaultElementLocator {

    public TestDefaultElementLocator(SearchContext searchContext, Field field) {
        super(searchContext, new TestAnnotations(field));
    }
}
