package com.rayhahah.androidartstudy.module.reflect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rayhahah.androidartstudy.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflect);
    }

    public void reflectOne(View view) {
        ReflectBean reflectBean = new ReflectBean();
        Class<? extends ReflectBean> reflectBeanClass = reflectBean.getClass();
        String packageName = reflectBeanClass.getPackage().getName();
        String reflectBeanClassName = reflectBeanClass.getName();
    }

    public void reflectTwo(View view) {
        try {
            Class<?> reflectBeanClass = Class.forName("ReflectBean");
            ReflectBean reflectBean = (ReflectBean) reflectBeanClass.newInstance();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }

    public void reflectThree(View view) {
        Class<ReflectBean> reflectBeanClass = ReflectBean.class;

    }

    public void reflectContract(View view) {
        try {
            Class<?> reflectBeanClass = Class.forName("ReflectBean");
            Constructor<?>[] constructors = reflectBeanClass.getConstructors();
            ReflectBean reflectBean = (ReflectBean) constructors[0].newInstance();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public void reflectMethod(View view) {
        try {
            Class<?> reflectBeanClass = Class.forName("ReflectBean");
            ReflectBean reflectBean = (ReflectBean) reflectBeanClass.newInstance();
            Method[] methods = reflectBeanClass.getMethods();
            Method getLog = reflectBeanClass.getMethod("getLog", String.class);
            getLog.invoke("haha");
            Field age = reflectBeanClass.getField("age");
            //将属性赋值到对象当中
            age.set(reflectBean,10);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }
}
