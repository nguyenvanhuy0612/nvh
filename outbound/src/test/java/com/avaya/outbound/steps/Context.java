package com.avaya.outbound.steps;

import com.avaya.outbound.lib.UtilityFun;
import com.avaya.outbound.lib.support.OutboundPageFactory;
import com.avaya.outbound.lib.support.ReflectionUtils;
import com.avaya.outbound.lib.support.TestData;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class Context extends StepContext {

    public Context() {
        initContext();
    }

    public void initContext() {
        this.listShared = new ArrayList<>();
        this.listStringShared = new ArrayList<>();
        this.listDriver = new ArrayList<>();
        this.testData = new TestData<>();
        this.testDataObject = new TestData<>();
        this.utilityFun = new UtilityFun();
        this.context = this;
    }

    public void init(StepContext object) {
        try {
            for (Field field : StepContext.class.getDeclaredFields()) {
                if (field.getType().getName().contentEquals("org.apache.logging.log4j.Logger")) {
                    continue;
                }
                ReflectionUtils.mapFieldValue(field, object, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initContextPageObject() {
        try {
            Predicate<Field> pageFilter = field -> field.getType().getName().startsWith("com.avaya.outbound.frame.")
                    && !field.getType().getName().contentEquals("com.avaya.outbound.frame.CommonFunction")
                    && !field.getType().getName().contentEquals("com.avaya.outbound.frame.RestMethods");
            List<Field> fieldList = ReflectionUtils.getFieldList(this, pageFilter);
            Objects.requireNonNull(fieldList);
            for (Field field : fieldList) {
                initPageObject(field, this, this.driver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initPageObject(final Field field, final Object object, WebDriver driver) {
        try {
            field.setAccessible(true);
            field.set(object, OutboundPageFactory.initElements(driver, field.getType()));
            Object fieldValue = ReflectionUtils.getFieldValue(field, object);
            if (fieldValue != null)
                ReflectionUtils.setObjectValue(fieldValue, driver);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "";
    }
}
