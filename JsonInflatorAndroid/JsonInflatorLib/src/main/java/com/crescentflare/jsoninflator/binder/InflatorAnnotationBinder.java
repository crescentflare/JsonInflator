package com.crescentflare.jsoninflator.binder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Json inflator binder: object annotations
 * An inflator binder implementation which assigns objects to annotated fields in the given class
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class InflatorAnnotationBinder implements InflatorBinder
{
    private Object assignToObject;
    private Map<String, Field> annotatedFields = new HashMap<>();

    public InflatorAnnotationBinder(@NotNull Object assignToObject)
    {
        this.assignToObject = assignToObject;
        for (Field field : assignToObject.getClass().getDeclaredFields())
        {
            for (Annotation annotation : field.getDeclaredAnnotations())
            {
                if (annotation instanceof InflatableRef)
                {
                    annotatedFields.put(((InflatableRef)annotation).value(), field);
                }
            }
        }
    }

    @Override
    public void onBind(@NotNull String refId, @Nullable Object object)
    {
        if (object != null)
        {
            Field assignField = annotatedFields.get(refId);
            if (assignField != null)
            {
                try
                {
                    assignField.setAccessible(true);
                    assignField.set(assignToObject, object);
                }
                catch (IllegalAccessException ignored)
                {
                }
            }
        }
    }
}
