package com.crescentflare.jsoninflator.utility;

import android.content.Context;
import android.os.Build;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Json inflator utility: color lookup through resource ID
 * A color lookup implementation fetching colors through the app color resources defined in XML
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class InflatorResourceColorLookup implements InflatorColorLookup
{
    private Context resourceContext;

    public InflatorResourceColorLookup(@NotNull Context resourceContext)
    {
        this.resourceContext = resourceContext;
    }

    @Nullable
    @SuppressWarnings("deprecation")
    @Override
    public Integer getColor(@NotNull String refId)
    {
        int identifier = resourceContext.getResources().getIdentifier(refId, "color", resourceContext.getPackageName());
        if (identifier > 0)
        {
            return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ? resourceContext.getResources().getColor(identifier) : resourceContext.getResources().getColor(identifier, null);
        }
        return null;
    }
}
