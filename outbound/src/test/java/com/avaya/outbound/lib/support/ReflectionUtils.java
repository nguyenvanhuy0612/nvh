package com.avaya.outbound.lib.support;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ReflectionUtils {

    public static List<Field> getFieldList(final Object instance, Predicate<Field> predicate) {
        try {
            if (instance == null || predicate == null)
                return null;
            List<Field> fields = new ArrayList<>();
            Class<?> instanceClass = instance.getClass();
            while (instanceClass != Object.class) {
                fields.addAll(Arrays.stream(instanceClass.getDeclaredFields()).filter(predicate).collect(Collectors.toList()));
                instanceClass = instanceClass.getSuperclass();
            }
            return fields;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void set(final List<Field> fields, final Object instance, List<Object> values) {
        set(fields.toArray(new Field[]{}), instance, values.toArray());
    }

    public static void set(final Field[] fields, final Object instance, Object... values) {
        try {
            if (fields == null || instance == null || values == null || values.length == 0)
                return;
            for (Field field : fields) {
                if (field == null) {
                    continue;
                }
                for (Object value : values) {
                    if (value == null) {
                        continue;
                    }
                    if (field.getType().isAssignableFrom(value.getClass())) {
                        setFieldValue(field, instance, value);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void setObjectValue(final Object instance, Object... values) {
        try {
            if (instance == null || values == null || values.length == 0)
                return;
            Class<?> pageClass = instance.getClass();
            while (pageClass != Object.class) {
                set(pageClass.getDeclaredFields(), instance, values);
                pageClass = pageClass.getSuperclass();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Indicates whether or not a {@link Member} is both public and is contained in a public class.
     *
     * @param <T>    type of the object whose accessibility to test
     * @param member the Member to check for public accessibility (must not be {@code null}).
     * @return {@code true} if {@code member} is public and contained in a public class.
     * @throws NullPointerException if {@code member} is {@code null}.
     */
    public static <T extends AccessibleObject & Member> boolean isAccessible(final T member) {
        Objects.requireNonNull(member, "No member provided");
        return Modifier.isPublic(member.getModifiers()) && Modifier.isPublic(member.getDeclaringClass().getModifiers());
    }

    /**
     * Makes a {@link Member} {@link AccessibleObject#isAccessible() accessible} if the member is not public.
     *
     * @param <T>    type of the object to make accessible
     * @param member the Member to make accessible (must not be {@code null}).
     * @throws NullPointerException if {@code member} is {@code null}.
     */
    public static <T extends AccessibleObject & Member> void makeAccessible(final T member) {
        if (!isAccessible(member) && !member.isAccessible()) {
            member.setAccessible(true);
        }
    }

    /**
     * Makes a {@link Field} {@link AccessibleObject#isAccessible() accessible} if it is not public or if it is final.
     *
     * <p>Note that using this method to make a {@code final} field writable will most likely not work very well due to
     * compiler optimizations and the like.</p>
     *
     * @param field the Field to make accessible (must not be {@code null}).
     * @throws NullPointerException if {@code field} is {@code null}.
     */
    public static void makeAccessible(final Field field) {
        Objects.requireNonNull(field, "No field provided");
        if ((!isAccessible(field) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    /**
     * Gets the value of a {@link Field}, making it accessible if required.
     *
     * @param field    the Field to obtain a value from (must not be {@code null}).
     * @param instance the instance to obtain the field value from or {@code null} only if the field is static.
     * @return the value stored by the field.
     * @throws NullPointerException if {@code field} is {@code null}, or if {@code instance} is {@code null} but
     *                              {@code field} is not {@code static}.
     * @see Field#get(Object)
     */
    public static Object getFieldValue(final Field field, final Object instance) {
        try {
            makeAccessible(field);
            if (!Modifier.isStatic(field.getModifiers())) {
                Objects.requireNonNull(instance, "No instance given for non-static field");
            }
            return field.get(instance);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Gets the value of a static {@link Field}, making it accessible if required.
     *
     * @param field the Field to obtain a value from (must not be {@code null}).
     * @return the value stored by the static field.
     * @throws NullPointerException if {@code field} is {@code null}, or if {@code field} is not {@code static}.
     * @see Field#get(Object)
     */
    public static Object getStaticFieldValue(final Field field) {
        return getFieldValue(field, null);
    }

    /**
     * Sets the value of a {@link Field}, making it accessible if required.
     *
     * @param field    the Field to write a value to (must not be {@code null}).
     * @param instance the instance to write the value to or {@code null} only if the field is static.
     * @param value    the (possibly wrapped) value to write to the field.
     * @throws NullPointerException if {@code field} is {@code null}, or if {@code instance} is {@code null} but
     *                              {@code field} is not {@code static}.
     * @see Field#set(Object, Object)
     */
    public static void setFieldValue(final Field field, final Object instance, final Object value) {
        try {
            makeAccessible(field);
            if (!Modifier.isStatic(field.getModifiers())) {
                Objects.requireNonNull(instance, "No instance given for non-static field");
            }
            field.set(instance, value);
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Map field value object2 to object1
     *
     * @param field
     * @param object1
     * @param object2
     */
    public static void mapFieldValue(final Field field, final Object object1, final Object object2) {
        try {
            makeAccessible(field);
            if (!Modifier.isStatic(field.getModifiers())) {
                Objects.requireNonNull(object1, "No instance given for non-static field");
                Objects.requireNonNull(object2, "No instance given for non-static field");
            }
            Object value = field.get(object2);
            field.set(object1, value);
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Sets the value of a static {@link Field}, making it accessible if required.
     *
     * @param field the Field to write a value to (must not be {@code null}).
     * @param value the (possibly wrapped) value to write to the field.
     * @throws NullPointerException if {@code field} is {@code null}, or if {@code field} is not {@code static}.
     * @see Field#set(Object, Object)
     */
    public static void setStaticFieldValue(final Field field, final Object value) {
        setFieldValue(field, null, value);
    }

    /**
     * Gets the default (no-arg) constructor for a given class.
     *
     * @param clazz the class to find a constructor for
     * @param <T>   the type made by the constructor
     * @return the default constructor for the given class
     * @throws IllegalStateException if no default constructor can be found
     */
    public static <T> Constructor<T> getDefaultConstructor(final Class<T> clazz) {
        Objects.requireNonNull(clazz, "No class provided");
        try {
            final Constructor<T> constructor = clazz.getDeclaredConstructor();
            makeAccessible(constructor);
            return constructor;
        } catch (final NoSuchMethodException ignored) {
            try {
                final Constructor<T> constructor = clazz.getConstructor();
                makeAccessible(constructor);
                return constructor;
            } catch (final NoSuchMethodException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * Constructs a new {@code T} object using the default constructor of its class. Any exceptions thrown by the
     * constructor will be rethrown by this method, possibly wrapped in an
     * {@link java.lang.reflect.UndeclaredThrowableException}.
     *
     * @param clazz the class to use for instantiation.
     * @param <T>   the type of the object to construct.
     * @return a new instance of T made from its default constructor.
     * @throws IllegalArgumentException if the given class is abstract, an interface, an array class, a primitive type,
     *                                  or void
     * @throws IllegalStateException    if access is denied to the constructor, or there are no default constructors
     *                                  throws InternalException        wrapper of the underlying exception if checked
     */
    public static <T> T instantiate(final Class<T> clazz) {
        Objects.requireNonNull(clazz, "No class provided");
        final Constructor<T> constructor = getDefaultConstructor(clazz);
        try {
            return constructor.newInstance();
        } catch (final LinkageError | InstantiationException e) {
            // LOG4J2-1051
            // On platforms like Google App Engine and Android, some JRE classes are not supported: JMX, JNDI, etc.
            throw new IllegalArgumentException(e);
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException(e);
        } catch (final InvocationTargetException e) {
            throw new InternalError("Unreachable");
        }
    }
}

