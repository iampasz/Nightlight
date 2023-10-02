package com.sarnavsky.pasz.nightlight;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.InterstitialAd;


/**
 * Created by pasz on 23.10.2016.
 */


public class ImageFragment extends Fragment {
    int light;
    public static Bundle args;
    ImageView imageView;

//    private AdView mAdView;
//    InterstitialAd mInterstitialAd;

    private static final String ARG_PAGE_NUMBER = "pageNumber";
    private String mParam;

    private ILightProtocol lightProtocol;

    static Fragment init(Light val) {
        ImageFragment truitonFrag = new ImageFragment();
        truitonFrag.getTag();
        // Supply val input as an argument.
        args = new Bundle();
        args.putSerializable("val", val);
        truitonFrag.setArguments(args);

        return truitonFrag;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        lightProtocol = (ILightProtocol) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
       imageView = (ImageView) view.findViewById(R.id.imageView);
        Light light = (Light)  getArguments().getSerializable("val");
        imageView.setImageResource(light.mypic);
        super.onViewCreated(view, savedInstanceState);



        view.findViewById(R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                lightProtocol.changeLight();

                return false;
            }



        });
    }

}