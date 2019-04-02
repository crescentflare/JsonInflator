package com.crescentflare.jsoninflatorexample;

import android.app.Application;
import android.content.Context;

import com.crescentflare.jsoninflatorexample.viewlets.ButtonViewlet;
import com.crescentflare.jsoninflatorexample.viewlets.EditTextViewlet;
import com.crescentflare.jsoninflatorexample.viewlets.LinearLayoutViewlet;
import com.crescentflare.jsoninflatorexample.viewlets.SwitchViewlet;
import com.crescentflare.jsoninflatorexample.viewlets.TextViewViewlet;
import com.crescentflare.jsoninflatorexample.viewlets.ViewViewlet;

/**
 * The singleton application context (containing the other singletons in the app)
 */
public class ExampleApplication extends Application
{
    // --
    // Global context member
    // --

    public static Context context = null;


    // --
    // Initialization
    // --

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = this;
        registerViewlets();
    }

    public void registerViewlets()
    {
        ViewletCreator.instance.register("linearLayout", new LinearLayoutViewlet());
        ViewletCreator.instance.register("textView", new TextViewViewlet());
        ViewletCreator.instance.register("editText", new EditTextViewlet());
        ViewletCreator.instance.register("switch", new SwitchViewlet());
        ViewletCreator.instance.register("button", new ButtonViewlet());
        ViewletCreator.instance.register("view", new ViewViewlet());
    }
}
