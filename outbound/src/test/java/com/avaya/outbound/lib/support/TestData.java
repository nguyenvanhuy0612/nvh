package com.avaya.outbound.lib.support;

import com.avaya.outbound.lib.EnvSetup;
import com.google.common.base.Functions;
import com.google.common.collect.Maps;
import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class TestData<K, V> extends AbstractTestData<K, V> implements Map<K, V> {

    public TestData() {
        super();
    }

    public TestData(Map<? extends K, ? extends V> m) {
        this.putAll(m);
    }

    public TestData(String dataFile) {
        this.load(dataFile);
    }

    public TestData(String dataFile, String data) {
        this.load(dataFile, data);
    }

    public void load(String dataFile, String data) {
        try {
            dataFile = EnvSetup.TEST_DATA_PATH + "testcasedata/" + dataFile + ".json";
            JsonElement jsonElement = JsonParser.parseReader(new FileReader(dataFile));
            if (jsonElement.isJsonObject()) {
                jsonElement = jsonElement.getAsJsonObject().get(data);
                if (jsonElement.isJsonArray() || jsonElement.isJsonPrimitive()) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.add(data, jsonElement);
                    jsonElement = jsonObject;
                }
                this.clear();
                this.putAll((TestData) JsonConvert.toTestData(jsonElement.getAsJsonObject()));
                //this.putAll(gson.fromJson(jsonElement, type));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load(String dataFile) {
        try {
            dataFile = EnvSetup.TEST_DATA_PATH + "testcasedata/" + dataFile + ".json";
            JsonElement jsonElement = JsonParser.parseReader(new FileReader(dataFile));
            if (jsonElement.isJsonArray() || jsonElement.isJsonPrimitive()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.add("DATA", jsonElement);
                jsonElement = jsonObject;
            }
            this.clear();
            this.putAll((TestData) JsonConvert.toTestData(jsonElement.getAsJsonObject()));
            //this.putAll(gson.fromJson(jsonElement, type));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * TestData to Map<K, String>, with value will convert to {@link String} type
     *
     * @return {@link Map} with value is convert to {@link String}
     */
    public Map<K, String> toMap() {
        Map<K, String> r = new TestData<>();
        this.forEach((k, v) -> r.put(k, v == null ? null : v.toString()));
        return r;
    }

    /**
     * TestData as Map<K, R>
     * <p>This is unsafe cast</p>
     *
     * @return {@link Map} with values cast as R
     */
    public <R> Map<K, R> asMap() {
        return (Map) this;
    }

    /**
     * TestData as HashMap<K, R>
     * <p>This is unsafe cast</p>
     *
     * @return {@link HashMap} with values cast as R
     */
    public <R> HashMap<K, R> asHashMap() {
        return new HashMap<>((Map) this);
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that
     * {@code Objects.equals(key, k)},
     * then this method returns {@code v}; otherwise
     * it returns {@code null}.  (There can be at most one such mapping.)
     *
     * <p>If this map permits null values, then a return value of
     * {@code null} does not <i>necessarily</i> indicate that the map
     * contains no mapping for the key; it's also possible that the map
     * explicitly maps the key to {@code null}.  The {@link #containsKey
     * containsKey} operation may be used to distinguish these two cases.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     * {@code null} if this map contains no mapping for the key
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    public String get(String key) {
        V val = super.get(key);
        return val == null ? null : val.toString();
    }

    /**
     * Returns the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the value as String to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key
     * @throws ClassCastException   if the key is of an inappropriate type for
     *                              this map
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @implSpec The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     */
    public String getOrDefault(String key, V defaultValue) {
        V val = super.getOrDefault(key, defaultValue);
        return val == null ? null : val.toString();
    }

    /**
     * Returns the value to which the list of keys is mapped
     * <p>
     * This method will search each key in turn according to the corresponding layer.
     *
     * @param keys: Arrays of keys with String type
     * @return value cast (unsafe) as R type
     */
    public <R> R getData(String... keys) {
        Objects.requireNonNull(keys);
        Object m = this;
        for (String key : keys) {
            m = ((Map) m).get(key);
            if (m instanceof Map) {
                continue;
            }
            break;
        }
        return (R) m;
    }

    /**
     * Returns the value to which the list of keys is mapped
     * <p>
     * If you want to return the {@link String} type, the value will return as {@link Object}#toString()
     * <p>
     * This method will search each key in turn according to the corresponding layer.
     *
     * @param returnType: the type want to return, most case return type is:
     *                    String.class, List.class, TestData.class, Map.class, ArrayList.class
     * @param keys:       Arrays of keys with String type
     * @return value cast (unsafe) as R type
     */
    public <R> R getData(Class<R> returnType, String... keys) {
        Objects.requireNonNull(keys);
        Object m = this;
        for (String key : keys) {
            m = ((Map) m).get(key);
            if (m instanceof Map) {
                continue;
            }
            break;
        }
        if (m == null)
            return null;
        if (returnType.isAssignableFrom(m.getClass()))
            return returnType.cast(m);
        if (String.class.isAssignableFrom(returnType))
            return returnType.cast(String.valueOf(m));
        if (List.class.isAssignableFrom(returnType)) {
            List<String> r = new ArrayList<>();
            r.add(String.valueOf(m));
            return returnType.cast(r);
        }
        if (Map.class.isAssignableFrom(returnType)) {
            TestData<String, String> r = new TestData<>();
            r.put(Arrays.toString(keys), String.valueOf(m));
            return returnType.cast(r);
        }
        return (R) m;
    }

    /**
     * Returns the value to which the list of keys is mapped
     * <p></p>
     * This method will search each key in turn according to the corresponding layer.
     *
     * <p>
     * If a {@link String} is found at the end the search, that String will be returned.
     * <p>
     * If a {@link String} is found during the search, it will return {@code null}.
     * <p>
     * If a {@link List} is found during the search, it will return {@code null}.
     * <p>
     * If a {@link Map} is found during the search, it will return {@code null}.
     * <p>
     *
     * @param keys: Arrays of keys with String type
     * @return value as String
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    public String getStrict(String... keys) {
        Objects.requireNonNull(keys);
        Object m = this;
        for (int i = 0; i < keys.length; i++) {
            m = ((Map) m).get(keys[i]);
            if (m instanceof Map) {
                continue;
            }
            if (m instanceof String && i == keys.length - 1) {
                return m.toString();
            }
        }
        return null;
    }

    /**
     * Returns the value to which the list of keys is mapped
     * <p>
     * <p>
     * This method will search each key in turn according to the corresponding layer.
     * <p>
     * If a {@link String} is found during the search, that String will be returned.
     * <p>
     * If a {@link List} is found during the search, it will return a String with the content as {@link List} #toString().
     * <p>
     * If a {@link Map} is found during the search, it will return a String with the content as {@link Map} #toString().
     * <p>
     *
     * @param keys: Arrays of keys with String type
     * @return value as String
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    public String getString(String... keys) {
        Objects.requireNonNull(keys);
        Object m = this;
        for (String key : keys) {
            m = ((Map) m).get(key);
            if (m instanceof Map) {
                continue;
            }
            break;
        }
        return m == null ? null : m.toString();
    }

    /**
     * @param keys: Arrays of keys with String type
     * @return value as {@link Object}
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @deprecated Use {@link TestData#getData(String...)} instead
     * <p></p>
     * Returns the value as Object to which the list of keys is mapped
     */
    @Deprecated
    public Object getAsObject(String... keys) {
        Map<K, V> m = new HashMap<>(this);
        for (String key : keys) {
            V v = m.get(key);
            if (v instanceof Map) {
                m = (Map) v;
                continue;
            }
            if (v instanceof String || v instanceof List || v == null) {
                return v;
            }
        }
        return m;
    }

    /**
     * @param keys: Arrays of keys with String type
     * @return value as {@link List}
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @deprecated Use {@link TestData#getData(String...)} instead
     * <p></p>
     * Returns the value as List to which the list of keys is mapped
     */
    @Deprecated
    public List<V> getAsList(String... keys) {
        Map<K, V> m = new HashMap<>(this);
        for (String key : keys) {
            V v = m.get(key);
            if (v instanceof Map) {
                m = (Map) v;
                continue;
            }
            if (v instanceof String || v == null) {
                return null;
            }
            if (v instanceof List) {
                return (List) v;
            }
        }
        return null;
    }

    /**
     * @param keys: Arrays of keys with String type
     * @return value as List of String
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @deprecated Use {@link TestData#getData(String...)} instead
     * <p></p>
     * Returns the value as List of String to which the list of keys is mapped
     */
    @Deprecated
    public List<String> getAsListString(String... keys) {
        return getAsList(keys).stream().map(String::valueOf).collect(Collectors.toList());
    }

    /**
     * @param keys: Arrays of keys with String type
     * @return value as Map
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @deprecated Use {@link TestData#getData(String...)} instead
     * <p></p>
     * Returns the value as Map to which the list of keys is mapped
     */
    @Deprecated
    public Map<K, V> getAsMap(String... keys) {
        Map<K, V> m = new HashMap<>(this);
        for (String key : keys) {
            V v = m.get(key);
            if (v instanceof Map) {
                m = (Map) v;
                continue;
            }
            if (v instanceof String || v instanceof List || v == null) {
                return null;
            }
        }
        return m;
    }

    /**
     * @param keys: Arrays of keys with String type
     * @return value as Map with values is String
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @deprecated Use {@link TestData#getData(String...)} instead
     * <p></p>
     * Returns the value as Map to which the list of keys is mapped
     */
    @Deprecated
    public Map<K, String> getAsMapString(String... keys) {
        return Maps.transformValues(getAsMap(keys), Functions.toStringFunction());
    }

    public static class JsonConvert {

        public static TestData<String, Object> toTestData(JsonObject json) {
            TestData<String, Object> map = new TestData<>();
            for (String key : json.keySet()) {
                JsonElement value = json.get(key);
                if (value.isJsonArray()) {
                    map.put(key, toList(value.getAsJsonArray()));
                } else if (value.isJsonObject()) {
                    map.put(key, toTestData(value.getAsJsonObject()));
                } else if (value.isJsonPrimitive()) {
                    map.put(key, toString(value.getAsJsonPrimitive()));
                } else if (value.isJsonNull()) {
                    map.put(key, "null");
                }
            }
            return map;
        }

        public static List<Object> toList(JsonArray array) {
            List<Object> list = new ArrayList<>();
            for (JsonElement key : array) {
                if (key.isJsonObject()) {
                    list.add(toTestData(key.getAsJsonObject()));
                } else if (key.isJsonArray()) {
                    list.add(toList(key.getAsJsonArray()));
                } else if (key.isJsonPrimitive()) {
                    list.add(toString(key.getAsJsonPrimitive()));
                } else if (key.isJsonNull()) {
                    list.add("null");
                }
            }
            return list;
        }

        public static String toString(JsonPrimitive primitive) {
            return primitive.isString() ? primitive.getAsString() : String.valueOf(primitive);
        }
    }
}
