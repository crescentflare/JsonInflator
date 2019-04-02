package com.crescentflare.jsoninflatorexample.viewlets;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.crescentflare.jsoninflator.JsonInflatable;
import com.crescentflare.jsoninflator.binder.InflatorBinder;
import com.crescentflare.jsoninflator.utility.InflatorMapUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Basic viewlet: view
 * Creation of a simple view through parsed JSON
 */
public class ViewViewlet implements JsonInflatable
{
    // --
    // Implementation
    // --

    @NotNull
    @Override
    public Object create(@NotNull Context context)
    {
        return new View(context);
    }

    @Override
    public boolean update(InflatorMapUtil mapUtil, @NotNull Object object, @NotNull Map<String, Object> attributes, @Nullable Object parent, @Nullable InflatorBinder binder)
    {
        if (object instanceof View)
        {
            applyDefaultAttributes(mapUtil, (View)object, attributes);
        }
        return true;
    }

    @Override
    public boolean canRecycle(InflatorMapUtil mapUtil, @NotNull Object object, @NotNull Map<String, Object> attributes)
    {
        return false;
    }


    // --
    // Shared
    // --

    public static String translatedText(Context context, String value)
    {
        int textIdentifier = context.getResources().getIdentifier(value, "string", context.getPackageName());
        if (textIdentifier > 0)
        {
            return context.getResources().getString(textIdentifier);
        }
        return value;
    }

    public static void applyDefaultAttributes(InflatorMapUtil mapUtil, View view, Map<String, Object> attributes)
    {
        // Background color
        if (!(view instanceof EditText) && !(view instanceof Button))
        {
            view.setBackgroundColor(mapUtil.optionalColor(attributes, "backgroundColor", 0));
        }

        // Visibility
        String visibility = mapUtil.optionalString(attributes, "visibility", "");
        if (visibility.equals("gone"))
        {
            view.setVisibility(View.GONE);
        }
        else if (visibility.equals("invisible"))
        {
            view.setVisibility(View.INVISIBLE);
        }
        else
        {
            view.setVisibility(View.VISIBLE);
        }

        // Padding
        if (!(view instanceof EditText))
        {
            List<Integer> paddingList = mapUtil.optionalDimensionList(attributes, "padding");
            if (paddingList.size() == 4)
            {
                view.setPadding(paddingList.get(0), paddingList.get(1), paddingList.get(2), paddingList.get(3));
            }
            else
            {
                view.setPadding(
                        mapUtil.optionalDimension(attributes, "paddingLeft", 0),
                        mapUtil.optionalDimension(attributes, "paddingTop", 0),
                        mapUtil.optionalDimension(attributes, "paddingRight", 0),
                        mapUtil.optionalDimension(attributes, "paddingBottom", 0)
                );
            }
        }
    }

    public static void applyLayoutAttributes(InflatorMapUtil mapUtil, View view, Map<String, Object> attributes)
    {
        // Return early if layout parameters are not present
        if (view.getLayoutParams() == null)
        {
            return;
        }

        // Width
        String widthString = mapUtil.optionalString(attributes, "width", "");
        if (widthString.equals("matchParent"))
        {
            view.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        else if (widthString.equals("wrapContent"))
        {
            view.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        else
        {
            view.getLayoutParams().width = mapUtil.optionalDimension(attributes, "width", ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // Height
        String heightString = mapUtil.optionalString(attributes, "height", "");
        if (heightString.equals("matchParent"))
        {
            view.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        else if (heightString.equals("wrapContent"))
        {
            view.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        else
        {
            view.getLayoutParams().height = mapUtil.optionalDimension(attributes, "height", ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // Margin
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)
        {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
            List<Integer> marginList = mapUtil.optionalDimensionList(attributes, "margin");
            if (marginList.size() == 4)
            {
                marginLayoutParams.setMargins(marginList.get(0), marginList.get(1), marginList.get(2), marginList.get(3));
            }
            else
            {
                marginLayoutParams.setMargins(
                        mapUtil.optionalDimension(attributes, "marginLeft", 0),
                        mapUtil.optionalDimension(attributes, "marginTop", 0),
                        mapUtil.optionalDimension(attributes, "marginRight", 0),
                        mapUtil.optionalDimension(attributes, "marginBottom", 0)
                );
            }
        }
    }
}
