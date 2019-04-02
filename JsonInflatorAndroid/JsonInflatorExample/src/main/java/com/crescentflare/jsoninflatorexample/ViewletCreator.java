package com.crescentflare.jsoninflatorexample;

import com.crescentflare.jsoninflator.JsonInflator;

import org.jetbrains.annotations.NotNull;

/**
 * A custom json inflator for views
 */
public class ViewletCreator extends JsonInflator
{
    public static ViewletCreator instance = new ViewletCreator("viewlet", "viewletStyle");

    private ViewletCreator(@NotNull String inflatableKey, @NotNull String attributeSetKey)
    {
        super(inflatableKey, attributeSetKey);
    }
}
