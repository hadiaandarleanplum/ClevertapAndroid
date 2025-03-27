package com.hadia.clevertapandroid;

import android.app.Application;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.inapp.customtemplates.CustomTemplate;
import com.clevertap.android.sdk.inapp.customtemplates.CustomTemplatesExtKt;

public class registerCustomInAppTemplates extends Application {
    private MyTemplatePresenter templatePresenter;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the presenter (without Activity for now)
        templatePresenter = new MyTemplatePresenter();

        // Register custom templates with CleverTap
        CleverTapAPI.registerCustomInAppTemplates(ctConfig ->
                CustomTemplatesExtKt.templatesSet(
                        new CustomTemplate.FunctionBuilder(true)
                                .name("function")
                                .presenter(templatePresenter) // Set the presenter
                                .intArgument("int", 0)
                                .build()
                )
        );
    }

    // Expose the presenter so the Activity can set itself later
    public MyTemplatePresenter getTemplatePresenter() {
        return templatePresenter;
    }
}
