package com.marte5.beautifulvino.OnBoarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marte5.beautifulvino.R;

public class OnboardingFragment2 extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(
                R.layout.onboarding_screen2,
                container,
                false
        );

        view.setTag(1);
        return view;
    }

}
