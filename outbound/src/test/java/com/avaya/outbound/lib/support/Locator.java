package com.avaya.outbound.lib.support;

import com.avaya.outbound.lib.EnvSetup;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.By;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class Locator {
    public Properties common = EnvSetup.locatorMap.get("common_locator");
    public Properties classProp = new Properties(common);

    public Locator() {
    }

    public Locator(Class<?> clazz) {
        this(clazz.getSimpleName());
    }

    public Locator(Object object) {
        this(object.getClass().getSimpleName());
    }

    public Locator(String className) {
        this.classProp.putAll(getClassPropAndCommon(className));
    }

    public By get(String key) {
        return buildBy(this.getPropertyValue(key));
    }

    public String getLocator(String key) {
        return extractValue(this.getPropertyValue(key));
    }

    public String locatorFormat(String key, String... values) {
        return String.format(extractValue(this.getPropertyValue(key)), values);
    }

    public Type getType(String key) {
        return extractType(this.getPropertyValue(key));
    }

    public String getPropertyValue(String key) {
        return classProp.getProperty(key, "");
    }

    public synchronized static String getPropertyValue(String className, String key) {
        Properties prop = getClassPropAndCommon(className);
        return prop.getProperty(key, "");
    }

    public synchronized static By getBy(String className, String key) {
        return buildBy(getPropertyValue(className, key));
    }

    public synchronized static Properties getClassPropAndCommon(String className) {
        Properties classProp = EnvSetup.locatorMap.getOrDefault(className, null);
        if (classProp == null) {
            return EnvSetup.locatorMap.get("common_locator");
        }
        Properties prop = new Properties(EnvSetup.locatorMap.get("common_locator"));
        prop.putAll(classProp);
        return prop;
    }

    public static By buildBy(String input) {
        Pair<Type, String> locatorData = extractTypeAndValue(input);
        if (locatorData == null)
            return null;
        return switch (locatorData.getLeft()) {
            case id -> By.id(locatorData.getRight());
            case name -> By.name(locatorData.getRight());
            case className -> By.className(locatorData.getRight());
            case css -> By.cssSelector(locatorData.getRight());
            case tagName -> By.tagName(locatorData.getRight());
            case linkText -> By.linkText(locatorData.getRight());
            case partialLinkText -> By.partialLinkText(locatorData.getRight());
            case xpath -> By.xpath(locatorData.getRight());
            case data_testid -> By.xpath("//*[@data-testid='" + locatorData.getRight() + "']");
            default -> By.xpath(locatorData.getRight());
        };
    }

    public static Type extractType(String input) {
        return extractTypeAndValue(input).getLeft();
    }

    public static String extractValue(String input) {
        return extractTypeAndValue(input).getRight();
    }

    public static Pair<Type, String> extractTypeAndValue(String input) {
        if (input == null || input.isEmpty())
            return null;
        input = input.trim();
        int index = input.indexOf(":");
        if (index == -1) {
            return new ImmutablePair<>(Type.xpath, input);
        }
        String valueType = input.substring(0, index).toLowerCase().trim();
        String value = input.substring(index).replaceFirst(":", "").trim();
        return switch (valueType) {
            case "id" -> new ImmutablePair<>(Type.id, value);
            case "name" -> new ImmutablePair<>(Type.name, value);
            case "classname" -> new ImmutablePair<>(Type.className, value);
            case "css" -> new ImmutablePair<>(Type.css, value);
            case "linktext" -> new ImmutablePair<>(Type.linkText, value);
            case "partiallinktext" -> new ImmutablePair<>(Type.partialLinkText, value);
            case "xpath" -> new ImmutablePair<>(Type.xpath, value);
            case "data_testid", "datatestid", "data-testid" -> new ImmutablePair<>(Type.data_testid, value);
            default -> new ImmutablePair<>(Type.xpath, input);
        };
    }

    public enum Type {
        id,
        name,
        className,
        css,
        tagName,
        linkText,
        partialLinkText,
        xpath,
        data_testid,
        None
    }

    public synchronized static void setLocatorMap(String className) {
        setLocatorMap(className, EnvSetup.LOCATOR_FOLDER_PATH + className + ".properties");
    }

    public synchronized static void setLocatorMap(String className, String propFilePath) {
        try {
            Properties prop = readProperties(propFilePath);
            if (!prop.isEmpty())
                EnvSetup.locatorMap.put(className, prop);
        } catch (Exception e) {
            System.out.println("Cannot read data to Properties from file: " + propFilePath + ", Exception:" + e);
        }
    }

    public synchronized static Properties readProperties(String fileNamePath) {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(fileNamePath)) {
            prop.load(input);
        } catch (Exception e) {
            System.out.println("Not found file path: " + fileNamePath);
        }
        return prop;
    }

    public String getCommon(String key) {
        return common.getProperty(key);
    }

    public static Map<String, Properties> listPropFilesForFolder(final String folderPath) {
        try {
            final File folder = new File(folderPath);
            return Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                    .filter(file -> file.getName().toLowerCase().contains(".properties"))
                    .collect(Collectors.toMap(file -> FilenameUtils.removeExtension(file.getName()),
                            file -> readProperties(file.getAbsolutePath()),
                            (prev, next) -> next, HashMap::new));
        } catch (Exception e) {
            System.out.println("Cannot read list of property files: " + e);
        }
        return new HashMap<>();
    }

    @Override
    public String toString() {
        return "common properties: " + common + "\nclass properties: " + classProp;
    }
}
