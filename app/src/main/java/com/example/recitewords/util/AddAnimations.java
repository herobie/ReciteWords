package com.example.recitewords.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

import androidx.fragment.app.Fragment;

public class AddAnimations {

    public static void crossFade(Fragment fragment) {
        int mShortAnimationDuration = 100;
        fragment.getView().animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (fragment.getView().getVisibility() == View.GONE){
                            fragment.getView().setVisibility(View.VISIBLE);
                        }else {
                            fragment.getView().setVisibility(View.GONE);
                        }

                    }
                });
    }
}
