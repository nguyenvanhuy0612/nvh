package com.avaya.outbound.lib.support;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ByIdOrName;

import java.util.ArrayList;
import java.util.List;

public class ByDataTestIdOrIdOrName extends ByIdOrName {

    private final By dataTestIdFinder;

    public ByDataTestIdOrIdOrName(String dataTestIdOrIdOrName) {
        super(dataTestIdOrIdOrName);
        this.dataTestIdFinder = By.xpath("//*[@data-testid='" + dataTestIdOrIdOrName + "']");
    }

    @Override
    public WebElement findElement(SearchContext context) {
        try {
            // First, try to locate by data-testid
            return this.dataTestIdFinder.findElement(context);
        } catch (NoSuchElementException e) {
            // Then by id or name
            return super.findElement(context);
        }
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        List<WebElement> elements = new ArrayList<>();

        // First: Find by data-testid ...
        elements.addAll(this.dataTestIdFinder.findElements(context));
        // Second: Find by id or name ...
        elements.addAll(super.findElements(context));

        return elements;
    }

    @Override
    public String toString() {
        return "by data-testid or " + super.toString();
    }
}
