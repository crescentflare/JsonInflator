package com.crescentflare.jsoninflatorexample.viewlets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.SwitchCompat;
import android.util.TypedValue;
import android.view.View;

import com.crescentflare.jsoninflator.JsonInflatable;
import com.crescentflare.jsoninflator.binder.InflatorBinder;
import com.crescentflare.jsoninflator.utility.InflatorMapUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Basic viewlet: switch
 * Creation of a switch through parsed JSON
 */
public class SwitchViewlet implements JsonInflatable
{
    @NotNull
    @Override
    public View create(@NotNull Context context)
    {
        return new SwitchCompat(context);
    }

    @Override
    public boolean update(InflatorMapUtil mapUtil, @NotNull Object object, @NotNull Map<String, Object> attributes, @Nullable Object parent, @Nullable InflatorBinder binder)
    {
        if (object instanceof SwitchCompat)
        {
            // Text
            SwitchCompat switchControl = (SwitchCompat)object;
            switchControl.setText(ViewViewlet.translatedText(switchControl.getContext(), mapUtil.optionalString(attributes, "text", "")));

            // State
            switchControl.setChecked(mapUtil.optionalBoolean(attributes, "on", false));

            // Text style
            String typeface = mapUtil.optionalString(attributes, "typeface", "");
            int defaultSize = (int)(switchControl.getContext().getResources().getDisplayMetrics().scaledDensity * 17);
            switchControl.setTextSize(TypedValue.COMPLEX_UNIT_PX, mapUtil.optionalDimension(attributes, "textSize", defaultSize));
            if (typeface.equals("bold"))
            {
                switchControl.setTypeface(null, Typeface.BOLD);
            }
            else if (typeface.equals("italics"))
            {
                switchControl.setTypeface(null, Typeface.ITALIC);
            }
            else
            {
                switchControl.setTypeface(Typeface.DEFAULT);
            }
            switchControl.setTextColor(mapUtil.optionalColor(attributes, "textColor", 0xFF101010));

            // Standard view attributes
            ViewViewlet.applyDefaultAttributes(mapUtil, switchControl, attributes);
            return true;
        }
        return false;
    }

    @Override
    public boolean canRecycle(InflatorMapUtil mapUtil, @NotNull Object object, @NotNull Map<String, Object> attributes)
    {
        return object instanceof SwitchCompat;
    }
}
