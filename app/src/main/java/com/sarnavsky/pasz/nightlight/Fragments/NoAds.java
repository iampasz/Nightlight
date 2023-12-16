package com.sarnavsky.pasz.nightlight.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.sarnavsky.pasz.nightlight.Interfaces.MyCallback;
import com.sarnavsky.pasz.nightlight.MainActivity;
import com.sarnavsky.pasz.nightlight.R;

public class NoAds extends Fragment {

    int noAdsCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.no_ads, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout noAdsLinear = view.findViewById(R.id.noAdsLinear);
        TextView no_ads_counter = view.findViewById(R.id.no_ads_counter);
        ConstraintLayout ads_constrain = view.findViewById(R.id.ads_constrain);

        noAdsCount = ((MainActivity)getActivity()).getNoAdsCount();
        no_ads_counter.setText(getResources().getString(R.string.ads_counter)+ noAdsCount);


        noAdsLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).showRewardedAd(new MyCallback() {
                    @Override
                    public void isShown(boolean shown) {
                        if (shown) {

                            //((MainActivity)getActivity()).adsView.setVisibility(View.GONE);
                            noAdsCount = ((MainActivity)getActivity()).getNoAdsCount();
                            no_ads_counter.setText(getResources().getString(R.string.ads_counter)+ noAdsCount);
                        }else{
                            no_ads_counter.setText(getResources().getString(R.string.dont_load));
                        }
                    }
                });

            }
        });


        ads_constrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().remove(NoAds.this).commit();
                ((MainActivity)getActivity()).clearAlpha();
            }
        });
    }
}
