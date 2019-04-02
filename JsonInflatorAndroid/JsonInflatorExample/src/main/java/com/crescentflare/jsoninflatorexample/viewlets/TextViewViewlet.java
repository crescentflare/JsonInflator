package com.crescentflare.jsoninflatorexample.viewlets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.crescentflare.jsoninflator.JsonInflatable;
import com.crescentflare.jsoninflator.binder.InflatorBinder;
import com.crescentflare.jsoninflator.utility.InflatorMapUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Basic viewlet: text view
 * Creation of a text view through parsed JSON
 */
public class TextViewViewlet implements JsonInflatable
{
    @NotNull
    @Override
    public Object create(@NotNull Context context)
    {
        return new TextView(context);
    }

    @Override
    public boolean update(InflatorMapUtil mapUtil, @NotNull Object object, @NotNull Map<String, Object> attributes, @Nullable Object parent, @Nullable InflatorBinder binder)
    {
        if (object instanceof TextView)
        {
            // Text
            TextView textView = (TextView)object;
            textView.setText(ViewViewlet.translatedText(textView.getContext(), mapUtil.optionalString(attributes, "text", "")));

            // Text style
            String typeface = mapUtil.optionalString(attributes, "typeface", "");
            int defaultSize = (int)(textView.getContext().getResources().getDisplayMetrics().scaledDensity * 17);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mapUtil.optionalDimension(attributes, "textSize", defaultSize));
            if (typeface.equals("bold"))
            {
                textView.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            }
            else if (typeface.equals("italics"))
            {
                textView.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
            }
            else
            {
                textView.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            }
            textView.setTextColor(mapUtil.optionalColor(attributes, "textColor", 0xFF101010));

            // Other properties
            String textAlignment = mapUtil.optionalString(attributes, "textAlignment", "");
            textView.setMaxLines(mapUtil.optionalInteger(attributes, "maxLines", Integer.MAX_VALUE));
            if (textAlignment.equals("center"))
            {
                textView.setGravity(Gravity.CENTER);
            }
            else if (textAlignment.equals("right"))
            {
                textView.setGravity(Gravity.RIGHT);
            }
            else
            {
                textView.setGravity(Gravity.LEFT);
            }

            // Standard view attributes
            ViewViewlet.applyDefaultAttributes(mapUtil, textView, attributes);
            return true;
        }
        return false;
    }

    @Override
    public boolean canRecycle(InflatorMapUtil mapUtil, @NotNull Object object, @NotNull Map<String, Object> attributes)
    {
        return object instanceof TextView && !(object instanceof Button) && !(object instanceof EditText);
    }
}
