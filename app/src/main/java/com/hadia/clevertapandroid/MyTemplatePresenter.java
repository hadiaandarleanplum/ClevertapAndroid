package com.hadia.clevertapandroid;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.clevertap.android.sdk.inapp.customtemplates.CustomTemplateContext;
import com.clevertap.android.sdk.inapp.customtemplates.FunctionPresenter;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewException;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.review.model.ReviewErrorCode;

import java.lang.ref.WeakReference;

// This class handles custom in-app templates on CT
public class MyTemplatePresenter implements FunctionPresenter {
    private WeakReference<Activity> activityRef; // Use WeakReference to avoid memory leaks

    // Default constructor
    public MyTemplatePresenter() {
        // No Activity is set initially
    }

    // Setter to dynamically set the Activity
    public void setActivity(Activity activity) {
        this.activityRef = new WeakReference<>(activity);
    }

    @Override
    public void onPresent(@NonNull CustomTemplateContext.FunctionContext functionContext) {
        // Be sure to keep the context as long as the template UI is being displayed
        functionContext.setPresented();

        // Get the Activity from WeakReference
        Activity activity = activityRef != null ? activityRef.get() : null;
        if (activity == null) {
            System.out.println("Activity is null, cannot present.");
            return; // Exit if the activity reference is lost
        }

        // Initialize ReviewManager using the provided context
        ReviewManager reviewManager = ReviewManagerFactory.create(activity);

        // Request in-app review
        Task<ReviewInfo> request = reviewManager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();

                // Launch the review flow
                Task<Void> flow = reviewManager.launchReviewFlow(activity, reviewInfo);
                flow.addOnCompleteListener(flowTask -> {
                    System.out.println("REVIEW FLOW FINISHED");
                    // The flow has finished. Continue app flow
                });

            } else {
                // Handle error during review flow
                @ReviewErrorCode int reviewErrorCode = ((ReviewException) task.getException()).getErrorCode();
                System.out.println("Error during review flow: " + reviewErrorCode);
            }
        });
    }
}
