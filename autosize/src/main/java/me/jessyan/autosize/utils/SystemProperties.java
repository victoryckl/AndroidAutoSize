package me.jessyan.autosize.utils;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SystemProperties {
    private static final String TAG = SystemProperties.class.getSimpleName();

    // String SystemProperties.get(String key){}
    public static String get(String key) {
        init();

        String value = null;

        try {
            value = (String) mGetMethod.invoke(mClassType, key);
        } catch (InvocationTargetException e) {
            Throwable t = e.getTargetException();
            t.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    //int SystemProperties.get(String key, int def){}
    public static int getInt(String key, int def) {
        init();

        int value = def;
        try {
            Integer v = (Integer) mGetIntMethod.invoke(mClassType, key, def);
            value = v.intValue();
        } catch (InvocationTargetException e) {
            Throwable t = e.getTargetException();
            t.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static int getSdkVersion() {
        return getInt("ro.build.version.sdk", -1);
    }

    public static boolean getBoolean(String key, boolean def) {
        init();

        boolean value = def;
        try {
            Boolean v = (Boolean) mGetBooleanMethod.invoke(mClassType, key, def);
            value = v.booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static void setBoolean(String key, boolean value) {
        init();

        try {
            mSetMethod.invoke(mClassType, key, "" + value);
        } catch (InvocationTargetException e) {
            Throwable t = e.getTargetException();
            t.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void set(String key, String val) {
        init();
        try {
            mSetMethod.invoke(mClassType, key, val);
        } catch (InvocationTargetException e) {
            Throwable t = e.getTargetException();
            t.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addChangeCallback(Runnable callback) {
        init();
        try {
            maddChangeCallback.invoke(mClassType, callback);
        } catch (InvocationTargetException e) {
            Throwable t = e.getTargetException();
            t.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //-------------------------------------------------------------------
    private static Class<?> mClassType = null;
    private static Method mSetMethod = null;
    private static Method mGetMethod = null;
    private static Method mGetIntMethod = null;
    private static Method mGetBooleanMethod = null;
    private static Method maddChangeCallback = null;

    private static void init() {
        try {
            if (mClassType == null) {
                mClassType = Class.forName("android.os.SystemProperties");

                mSetMethod = mClassType.getDeclaredMethod("set", String.class, String.class);
                mGetMethod = mClassType.getDeclaredMethod("get", String.class);
                mGetIntMethod = mClassType.getDeclaredMethod("getInt", String.class, int.class);
                mGetBooleanMethod = mClassType.getDeclaredMethod("getBoolean", String.class, boolean.class);
                maddChangeCallback = mClassType.getDeclaredMethod("addChangeCallback", Runnable.class);
            }
        } catch (Exception e) {
            Log.e(TAG, "[init] " + e.getMessage());
            e.printStackTrace();
        }
    }
}