package com.sauceDemo.testngUtils;

//This class will automatically apply RetryAnalyzer to all your @Test methods during runtime.

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class AnnotationTransformer implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        // Set the retryAnalyzer for all @Test methods
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}