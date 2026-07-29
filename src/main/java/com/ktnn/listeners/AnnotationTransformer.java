package com.ktnn.listeners;

import com.ktnn.annotations.FrameAnnotation;
import com.ktnn.consts.FrameConst;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class AnnotationTransformer implements IAnnotationTransformer {
    private static ThreadLocal<String[]> currentTestGroups = new ThreadLocal<>();

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        if (Objects.isNull(testMethod)) return;
        currentTestGroups.set(getGroupsFromTestClass(testMethod));
        annotation.setGroups(currentTestGroups.get());
    }

    @Override
    public void transform(IConfigurationAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        if (annotation.getBeforeTestClass() || annotation.getBeforeTestMethod() || annotation.getBeforeSuite()) {
            annotation.setGroups(currentTestGroups.get());
        }

        annotation.setTimeOut(7 * 60 * 1000); //7 mins
    }

    /**
     * Get all groups for each test method
     *
     * @param testMethod : Test Case
     * @return : List of group values
     */
    private String[] getGroupsFromTestClass(Method testMethod) {
        List<String> result = new ArrayList<>();

        Test testAnnotation;

        for (Method method : testMethod.getDeclaringClass().getMethods()) {
            // Filter only testing methods
            if (!method.getName().equals(testMethod.getName())) continue;
            testAnnotation = method.getAnnotation(Test.class);
            if (testAnnotation != null && testAnnotation.groups().length > 0) {
                result.add(Arrays.toString(testAnnotation.groups()));
            }
            // Filter custom annotation
            var fwAnnotations = testMethod.getAnnotation(FrameAnnotation.class);
            if (Objects.nonNull(fwAnnotations)) {
                FrameConst.CategoryType[] tcCate = fwAnnotations.category();
                Arrays.stream(tcCate).forEach(v -> result.add(v.toString().trim()));
            }
        }
        return result.toArray(new String[0]);
    }
}



