package com.ktnn.annotations;
import com.ktnn.consts.AuthorType;
import com.ktnn.consts.FrameConst.CategoryType;

import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class cho annotation của test script
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FrameAnnotation {
    CategoryType[] category();

    AuthorType[] author();

    @Nullable
    AuthorType[] reviewer();
}
