package com.crescentflare.jsoninflatorexample.viewlets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.SwitchCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.crescentflare.jsoninflator.JsonInflatable;
import com.crescentflare.jsoninflator.binder.InflatorBinder;
import com.crescentflare.jsoninflator.utility.InflatorMapUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Basic viewlet: button
 * Creation of a button through parsed JSON
 */
public class ButtonViewlet implements JsonInflatable
{
    @NotNull
    @Override
    public View create(@NotNull Context context)
    {
        return new Button(context);
    }

    @Override
    public boolean update(InflatorMapUtil mapUtil, @NotNull Object object, @NotNull Map<String, Object> attributes, @Nullable Object parent, @Nullable InflatorBinder binder)
    {
        if (object instanceof Button)
        {
            // Text
            Button button = (Button)object;
            button.setText(ViewViewlet.translatedText(button.getContext(), mapUtil.optionalString(attributes, "text", "")));

            // Text style
            String typeface = mapUtil.optionalString(attributes, "typeface", "");
            int defaultSize = (int)(button.getContext().getResources().getDisplayMetrics().scaledDensity * 17);
            button.setTextSize(TypedValue.COMPLEX_UNIT_PX, mapUtil.optionalDimension(attributes, "textSize", defaultSize));
            if (typeface.equals("bold"))
            {
                button.setTypeface(null, Typeface.BOLD);
            }
            else if (typeface.equals("italics"))
            {
                button.setTypeface(null, Typeface.ITALIC);
            }
            else
            {
                button.setTypeface(Typeface.DEFAULT);
            }
            button.setTextColor(mapUtil.optionalColor(attributes, "textColor", 0xFF101010));

            // Other properties
            String textAlignment = mapUtil.optionalString(attributes, "textAlignment", "");
            button.setMaxLines(mapUtil.optionalInteger(attributes, "maxLines", Integer.MAX_VALUE));
            if (textAlignment.equals("center"))
            {
                button.setGravity(Gravity.CENTER_HORIZONTAL);
            }
            else if (textAlignment.equals("right"))
            {
                button.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            }
            else
            {
                button.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            }

            // Standard view attributes
            ViewViewlet.applyDefaultAttributes(mapUtil, button, attributes);
            return true;
        }
        return false;
    }

    @Override
    public boolean canRecycle(InflatorMapUtil mapUtil, @NotNull Object object, @NotNull Map<String, Object> attributes)
    {
        return object instanceof Button && !(object instanceof SwitchCompat);
    }
}
