package com.crescentflare.jsoninflator.utility;

import android.content.Context;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Json inflator utility: dimension lookup through resource ID
 * A dimension lookup implementation fetching dimensions through the app dimension resources defined in XML
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class InflatorResourceDimensionLookup implements InflatorDimensionLookup
{
    private Context resourceContext;

    public InflatorResourceDimensionLookup(@NotNull Context resourceContext)
    {
        this.resourceContext = resourceContext;
    }

    @Nullable
    @Override
    public Integer getDimension(@NotNull String refId)
    {
        int identifier = resourceContext.getResources().getIdentifier(refId, "dimen", resourceContext.getPackageName());
        if (identifier > 0)
        {
            return resourceContext.getResources().getDimensionPixelSize(identifier);
        }
        return null;
    }
}
