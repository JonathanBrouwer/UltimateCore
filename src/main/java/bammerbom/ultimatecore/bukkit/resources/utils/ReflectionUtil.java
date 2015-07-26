/*
 * This file is part of UltimateCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) Bammerbom
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package bammerbom.ultimatecore.bukkit.resources.utils;

import bammerbom.ultimatecore.bukkit.resources.classes.ErrorLogger;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Helper class that can "execute" strings and helps general NSM and OBC calls.
 *
 * @author molenzwiebel
 */
public class ReflectionUtil {

    /**
     * The NMS Path of the current Bukkit version. EG: net.minecraft.server.1_6_2R
     */
    public static String NMS_PATH = "net.minecraft.server." + (Bukkit.getServer() != null ? Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] : "UNKNOWN");
    public static String OBC_PATH = "org.bukkit.craftbukkit." + (Bukkit.getServer() != null ? Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] : "UNKNOWN");

    /**
     * Execute a reflection command
     *
     * @param command  The command to execute
     * @param toCallOn The object to call the command on
     * @param args     The arguments
     * @return The Object derived from the execution, or null if nothing is returned
     * @throws Exception When something goes wrong during execution
     */
    public static ReflectionObject execute(String command, Object toCallOn, Object... args) throws Exception {
        if (toCallOn instanceof ReflectionObject) {
            toCallOn = ((ReflectionObject) toCallOn).fetch();
        }
        //Split the command into parts
        String[] parts = command.split("\\.");

        //The current object
        Object obj = toCallOn;

        //Loop through the part
        for (String part : parts) {
            //If the part contains a bracket. (Function)
            if (part.indexOf('(') != -1) {
                //If the closing bracket is directly after the opening bracket (Function without args)
                if (part.charAt(part.indexOf('(') + 1) == ')') {
                    //Calls getObjByFunction. getObjByFunction(object, "toLowerCase"); for example
                    obj = getObjByFunction(obj, part.substring(0, part.length() - 2));
                } else {
                    //Function with 1 or more arguments
                    //Split the arguments with a comma
                    String[] arguments = part.substring(part.indexOf('(') + 1, part.indexOf(')')).split(", ");

                    //The paramaters found
                    Object[] params = new Object[arguments.length];

                    //Match the arguments to the arguments provided
                    int i = 0;
                    for (String arg : arguments) {
                        params[i++] = args[Integer.parseInt(arg.replace("{", "").replace("}", "")) - 1];
                    }

                    //Call getObjByFunction with the correct parameters
                    obj = getObjByFunction(obj, part.substring(0, part.indexOf('(')), params);
                }
            } else {
                //Get a variable of the current object.
                obj = getObj(obj, part);
            }
        }
        return new ReflectionObject(obj);
    }

    public static ReflectionObject executeStatic(String command, Class<?> toCallOn, Object... args) throws Exception {
        //Split the command into parts
        String[] parts = command.split("\\.");

        //The current object
        Object obj = toCallOn;

        //Loop through the part
        for (String part : parts) {
            //If the part contains a bracket. (Function)
            if (part.indexOf('(') != -1) {
                //If the closing bracket is directly after the opening bracket (Function without args)
                if (part.charAt(part.indexOf('(') + 1) == ')') {
                    //Calls getObjByFunction. getObjByFunction(object, "toLowerCase"); for example
                    obj = getStaticObjByFunction(toCallOn, part.substring(0, part.length() - 2));
                } else {
                    //Function with 1 or more arguments
                    //Split the arguments with a comma
                    String[] arguments = part.substring(part.indexOf('(') + 1, part.indexOf(')')).split(", ");

                    //The paramaters found
                    Object[] params = new Object[arguments.length];

                    //Match the arguments to the arguments provided
                    int i = 0;
                    for (String arg : arguments) {
                        params[i++] = args[Integer.parseInt(arg.replace("{", "").replace("}", "")) - 1];
                    }

                    //Call getObjByFunction with the correct parameters
                    obj = getStaticObjByFunction(toCallOn, part.substring(0, part.indexOf('(')), params);
                }
            } else {
                //Get a variable of the current object.
                obj = getStaticObj(toCallOn, part);
            }
        }
        return new ReflectionObject(obj);
    }

    /**
     * Tries to get a specified field value from the specified object
     *
     * @param obj   The object to find the field from
     * @param field The field name
     * @return The value of the field
     * @throws Exception If something went wrong during executing
     */
    protected static Object getObj(Object obj, String field) throws Exception {
        try {
            Field f = obj.getClass().getDeclaredField(field);
            f.setAccessible(true);
            return f.get(obj);
        } catch (NoSuchFieldException ex) {
            Class<?> cur = obj.getClass().getSuperclass();
            while (true) {
                try {
                    Field f = cur.getDeclaredField(field);
                    f.setAccessible(true);
                    return f.get(obj);
                } catch (NoSuchFieldException ex2) {
                    if (cur.getSuperclass() != null) {
                        cur = cur.getSuperclass();
                        continue;
                    }
                    return null;
                }
            }
        }
    }

    protected static Object getStaticObj(Class<?> obj, String field) throws Exception {
        Field f = obj.getDeclaredField(field);
        f.setAccessible(true);
        return f.get(null);
    }

    /**
     * Tries to invoke a method on the provided object with zero arguments
     *
     * @param obj        The object to call on
     * @param methodName The method name
     * @return The result of the function
     * @throws Exception If something went wrong during executing
     */
    protected static Object getObjByFunction(Object obj, String methodName) throws Exception {
        Method m = null;
        Class<?> c;
        //Traverse loop to make sure we get every method, also the inherited ones
        for (c = obj.getClass();
             c != null;
             c = c.getSuperclass()) {
            for (Method method : c.getDeclaredMethods()) {
                if (method.getName().equals(methodName)) {
                    m = method;
                }
            }
        }
        m.setAccessible(true);
        return m.invoke(obj);
    }

    protected static Object getStaticObjByFunction(Class<?> obj, String methodName) throws Exception {
        Method m = null;
        Class<?> c = obj;
        //Traverse loop to make sure we get every method, also the inherited ones
        for (c = obj;
             c != null;
             c = c.getSuperclass()) {
            for (Method method : c.getDeclaredMethods()) {
                if (method.getName().equals(methodName)) {
                    m = method;
                }
            }
        }
        m.setAccessible(true);
        return m.invoke(obj);
    }

    /**
     * Tries to invoke a method on the provided object with the provided arguments
     *
     * @param obj  The object to call on
     * @param name The name of the method
     * @param args The arguments used while executing
     * @return The result of the function
     * @throws Exception If something went wrong during executing
     */
    protected static Object getObjByFunction(Object obj, String name, Object[] args) throws Exception {
        Method m = null;
        Class<?> c;
        //Traverse loop to make sure we get every method, also the inherited ones
        for (c = obj.getClass();
             c != null;
             c = c.getSuperclass()) {
            for (Method method : c.getDeclaredMethods()) {
                if (method.getName().equals(name) && checkForMatch(method.getParameterTypes(), args)) {
                    m = method;
                }
            }
        }
        if (m == null) {
            throw new IllegalArgumentException("Could not find function " + name + " with arguments " + Arrays.toString(args) + " on object " + obj.getClass().getName());
        }
        m.setAccessible(true);
        return m.invoke(obj, args);
    }

    protected static Object getStaticObjByFunction(Class<?> obj, String name, Object[] args) throws Exception {
        Method m = null;
        Class<?> c;
        //Traverse loop to make sure we get every method, also the inherited ones
        for (c = obj;
             c != null;
             c = c.getSuperclass()) {
            for (Method method : c.getDeclaredMethods()) {
                if (method.getName().equals(name) && checkForMatch(method.getParameterTypes(), args)) {
                    m = method;
                }
            }
        }
        if (m == null) {
            throw new IllegalArgumentException("Could not find function " + name + " with arguments " + Arrays.toString(args) + " on object " + obj.getClass().getName());
        }
        m.setAccessible(true);
        return m.invoke(obj, args);
    }

    /**
     * Compares a Class[] array and a Object[] array, checking if they have equal classes
     *
     * @param classes The Class array
     * @param args    The Object array
     * @return Whether the array classes are equals
     */
    private static boolean checkForMatch(Class<?>[] classes, Object[] args) {
        if (classes.length != args.length) {
            return false;
        }
        int i = 0;
        for (Class<?> cls : classes) {
            Object obj = args[i++];
            if (!cls.isAssignableFrom(obj.getClass()) && !isPrimitiveWrapper(cls, obj.getClass())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compares two classes to see if they wrap each other.
     *
     * @param class1 The first class
     * @param class2 The second class
     * @return If one of the classes wraps the other one
     */
    private static boolean isPrimitiveWrapper(Class<?> class1, Class<?> class2) {
        if ((class1 == Integer.class && class2 == int.class) || (class1 == int.class && class2 == Integer.class)) {
            return true;
        }
        if ((class1 == Boolean.class && class2 == boolean.class) || (class1 == boolean.class && class2 == Boolean.class)) {
            return true;
        }
        if ((class1 == Character.class && class2 == char.class) || (class1 == char.class && class2 == Character.class)) {
            return true;
        }
        if ((class1 == Float.class && class2 == float.class) || (class1 == float.class && class2 == Float.class)) {
            return true;
        }
        if ((class1 == Double.class && class2 == double.class) || (class1 == double.class && class2 == Double.class)) {
            return true;
        }
        if ((class1 == Long.class && class2 == long.class) || (class1 == long.class && class2 == Long.class)) {
            return true;
        }
        if ((class1 == Short.class && class2 == short.class) || (class1 == short.class && class2 == Short.class)) {
            return true;
        }
        if ((class1 == Byte.class && class2 == byte.class) || (class1 == byte.class && class2 == Byte.class)) {
            return true;
        }
        return false;
    }

    /**
     * Tries to find a matching constructor for the provided class and arguments
     *
     * @param cls  The class to find a constructor for
     * @param args The arguments
     * @return The constructor, or null if not found
     */
    private static Constructor<?> getMatchingConstructor(Class<?> cls, Object... args) {
        for (Constructor<?> constr : cls.getConstructors()) {
            if (checkForMatch(constr.getParameterTypes(), args)) {
                return constr;
            }
        }
        throw new IllegalArgumentException("Could not create a ReflectionObject for class " + cls.getName() + ": No " +
                "matching constructor found!");
    }

    public static class ReflectionObject {

        /**
         * The internal object operations are performed on
         */
        private final Object object;

        /**
         * Default constructor
         *
         * @param obj The object the ReflectionObject wraps around
         */
        public ReflectionObject(Object obj) {
            this.object = obj;
        }

        /**
         * Creates a new instance of the provided class, passing in the provided parameters. Then
         * returns the wrapped ReflectionObject
         *
         * @param className The name of the class
         * @param args      The constructor arguments
         * @return The wrapped ReflectionObject. The real object can be received with
         * ReflectionObject.fromNMS(class, args).fetch();
         */
        public static ReflectionObject fromNMS(String className, Object... args) {
            try {
                Class<?> clazz = Class.forName(NMS_PATH + "." + className);
                return new ReflectionObject(getMatchingConstructor(clazz, args).newInstance(args));
            } catch (Exception e) {
                try {
                    Class<?> clazz = Class.forName(className);
                    return new ReflectionObject(getMatchingConstructor(clazz, args).newInstance(args));
                } catch (Exception ex) {
                    ErrorLogger.log(ex, "(REFLECTION) Failed to find NMS class." + className);
                    return null;
                }
            }
        }

        public static ReflectionObject fromOBC(String className, Object... args) {
            try {
                Class<?> clazz = Class.forName(OBC_PATH + "." + className);
                return new ReflectionObject(getMatchingConstructor(clazz, args).newInstance(args));
            } catch (Exception e) {
                try {
                    Class<?> clazz = Class.forName(className);
                    return new ReflectionObject(getMatchingConstructor(clazz, args).newInstance(args));
                } catch (Exception ex) {
                    ErrorLogger.log(ex, "(REFLECTION) Failed to find OBC class. " + className);
                    return null;
                }
            }
        }

        /**
         * Returns the wrapped object
         *
         * @return The wrapped object
         */
        public Object fetch() {
            return object;
        }

        /**
         * Invokes a specified method, returning a new ReflectionObject if the method returns an
         * object
         *
         * @param methodName The method name
         * @param args       The arguments
         * @return A {@link ReflectionObject} if the method did not return null
         */
        public ReflectionObject invoke(String methodName, Object... args) {
            try {
                Object obj;
                obj = getObjByFunction(object, methodName, args);
                if (obj != null) {
                    return new ReflectionObject(obj);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Could not invoke " + methodName + " on object of class " + this.object.getClass().getName(), e);
            }
            return null;
        }

        /**
         * Tries to get a specified field on the object
         *
         * @param field The field name
         * @return The value of the field
         */
        public Object get(String field) {
            try {
                if (object instanceof Class) {
                    return getStaticObj((Class<?>) object, field);
                } else {
                    return getObj(object, field);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Could not get " + field + " on object of class " + this.object.getClass().getName(), e);
            }
        }

        /**
         * Does the same as {@link ReflectionObject#get(String)} but wraps the returned object in a {@link
         * ReflectionObject
         * }
         *
         * @param field The field name
         * @return The return of {@link ReflectionObject#get(String)}, wrapped in a {@link ReflectionObject
         * }
         */
        public ReflectionObject getAsRO(String field) {
            return new ReflectionObject(get(field));
        }

        /**
         * Gets the wrapped object, but casts it to the specified class if it is an instance, or
         * tries to find a matching constructor if possible.<br>
         * For example: get("operators", ReflectionObject.class); will get the operators object. As
         * the operators object is not a instance of ReflectionObject, it tries to invoke <i>new
         * ReflectionObject(operators);</i>
         *
         * @param as The class to cast the returning value as.
         * @return The gotten object, casted as the provided class
         */
        public <T> T fetchAs(Class<T> as) {
            try {
                return castOrCreate(object, as);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }

        /**
         * Gets the specified object, but casts it to the specified class if it is an instance, or
         * tries to find a matching constructor if possible.<br>
         * For example: get("operators", ReflectionObject.class); will get the operators object. As
         * the operators object is not a instance of ReflectionObject, it tries to invoke <i>new
         * ReflectionObject(operators);</i>
         *
         * @param field The field to get
         * @param as    The class to cast the returning value as.
         * @return The gotten object, casted as the provided class
         */
        public <T> T get(String field, Class<T> as) {
            try {
                Object obj = get(field);
                return castOrCreate(obj, as);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }

        /**
         * Helper method for {@link ReflectionObject#get(String, Class)} and
         * {@link ReflectionObject#fetchAs(Class)}
         *
         * @param obj
         * @param as
         * @return
         * @throws Exception
         */
        @SuppressWarnings("unchecked")
        private <T> T castOrCreate(Object obj, Class<T> as) throws Exception {
            try {
                if (as.isAssignableFrom(obj.getClass())) {
                    return (T) obj;
                }

                Constructor<?> constr = getMatchingConstructor(as, obj);
                if (constr != null) {
                    return (T) constr.newInstance(obj);
                }

                throw new IllegalArgumentException("Could not convert object of class " + obj.getClass().getName() +
                        " to " + as.getName());
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }

        /**
         * Sets the provided field to the provided value
         *
         * @param field The field to be set
         * @param arg   The value of the field
         */
        public void set(String field, Object arg) {
            try {
                Field f = object.getClass().getDeclaredField(field);
                f.setAccessible(true);
                f.set(object, arg);
            } catch (Exception e) {
                throw new IllegalArgumentException("Could not set " + field + " on object of class " + this.object.getClass().getName(), e);
            }
        }

    }

    public static class ReflectionStatic {

        public static Class<?> fromNMS(String className) {
            try {
                Class<?> clazz = Class.forName(NMS_PATH + "." + className);
                return clazz;
            } catch (Exception e) {
                try {
                    Class<?> clazz = Class.forName(className);
                    return clazz;
                } catch (Exception ex) {
                    ErrorLogger.log(ex, "(REFLECTION) Failed to find NMS class." + className);
                    return null;
                }
            }
        }

        public static Class<?> fromOBC(String className) {
            try {
                Class<?> clazz = Class.forName(OBC_PATH + "." + className);
                return clazz;
            } catch (Exception e) {
                try {
                    Class<?> clazz = Class.forName(className);
                    return clazz;
                } catch (Exception ex) {
                    ErrorLogger.log(ex, "(REFLECTION) Failed to find OBC class. " + className);
                    return null;
                }
            }
        }
    }
}
