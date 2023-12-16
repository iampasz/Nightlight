package com.sarnavsky.pasz.nightlight.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.sarnavsky.pasz.nightlight.MainActivity;
import com.sarnavsky.pasz.nightlight.R;

public class Brights extends Fragment {

   TextView text_prcent_s;
   TextView text_prcent_b;
   TextView bottom_text;
   ConstraintLayout br_constrain;
   ImageView lighter;
   ImageView automate;
   ImageView bear_image;
   ImageView sun_image;



   int BRIGHTS = 0;

   MyInterface mCallback;

   public interface MyInterface {
       void onTrigger();
   }

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

      return inflater.inflate(R.layout.brights_fragment, container, false);
   }

   @SuppressLint("SetTextI18n")
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);



      lighter = getActivity().findViewById(R.id.imageView);
      automate = getActivity().findViewById(R.id.automate);
      bottom_text = (TextView) getActivity().findViewById(R.id.bottom_text);

      br_constrain = view.findViewById(R.id.br_constrain);
      bear_image = view.findViewById(R.id.bear_image);
      sun_image = view.findViewById(R.id.sun_image);

      text_prcent_s = view.findViewById(R.id.text_prcent_s);
      SeekBar seekBar_s = view.findViewById(R.id.seekBar_s);
      seekBar_s.setMax(100);
      seekBar_s.setProgress(100);
      changeBright(100);
      seekBar_s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
         @Override
         public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            ((MainActivity)getActivity()).showToast(getResources().getString(R.string.screan_bright_is)+seekBar_s.getProgress()+" %");
         }

         @Override
         public void onStartTrackingTouch(SeekBar seekBar) {

         }

         @SuppressLint("SetTextI18n")
         @Override
         public void onStopTrackingTouch(SeekBar seekBar) {

            changeBright(seekBar.getProgress());

         }
      });

      br_constrain.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            getParentFragmentManager().beginTransaction().remove(Brights.this).commit();
            ((MainActivity)getActivity()).clearAlpha();
         }
      });


      SeekBar seekBar_b = view.findViewById(R.id.seekBar_b);
      text_prcent_b = view.findViewById(R.id.text_prcent_b);

      seekBar_b.setMax(100);




      seekBar_b.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
         @Override
         public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            ((MainActivity)getActivity()).showToast(getResources().getString(R.string.bright_is)+seekBar_b.getProgress()+" %");
         }

         @Override
         public void onStartTrackingTouch(SeekBar seekBar) {
         }

         @Override
         public void onStopTrackingTouch(SeekBar seekBar) {



           int myPos =  getRange(seekBar.getProgress());

            BRIGHTS = myPos;

            if(lighter!=null){
               lighter.setColorFilter(brightIt(myPos));
            }

            if(automate!=null){
               automate.setColorFilter(brightIt(myPos));
            }



            text_prcent_b.setText(seekBar.getProgress()+"%");
         }
      });

      float val = getProcent(getBrightsPreference());
      seekBar_b.setProgress((int) val);

      int result = (int)Math.ceil(val);
      text_prcent_b.setText(result+"%");


      bear_image.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            seekBar_b.setProgress(50);
            lighter.setColorFilter(0);
            text_prcent_b.setText(50+"%");
            BRIGHTS=0;
         }
      });

      sun_image.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            seekBar_s.setProgress(50);
            changeBright(50);
            text_prcent_s.setText(50+"%");
         }
      });
   }

   private void changeBright(int bright){

      float progress = (float) bright/100;

      WindowManager.LayoutParams layout = getActivity().getWindow().getAttributes();
      layout.screenBrightness = progress;
      getActivity().getWindow().setAttributes(layout);

      text_prcent_s.setText(bright+"%");
   }

   public static ColorMatrixColorFilter brightIt(int fb) {
      ColorMatrix cmB = new ColorMatrix();
      cmB.set(new float[] {
              1, 0, 0, 0, fb,
              0, 1, 0, 0, fb,
              0, 0, 1, 0, fb,
              0, 0, 0, 1, 0   });

      ColorMatrix colorMatrix = new ColorMatrix();
      colorMatrix.set(cmB);
      ColorMatrixColorFilter f = new ColorMatrixColorFilter(colorMatrix);

      return f;
   }

   private int getRange(int procent){

      return (240*procent/100)-120;

   }

   private float getProcent(float bright){
      return (float) 100*((bright +120)/240);
   }

   public void saveBrights(int brights) {
      SharedPreferences sharedPref = getContext().getSharedPreferences("MYPREFS", 0);
      SharedPreferences.Editor editor = sharedPref.edit();
      editor.putInt("BRIGHT", brights);
      editor.apply();
   }

   public int getBrightsPreference() {
      SharedPreferences sharedPref = getContext().getSharedPreferences("MYPREFS", 0);
      BRIGHTS = sharedPref.getInt("BRIGHT", 0);
      return BRIGHTS;
   }

   @Override
   public void onStop() {
      super.onStop();
      saveBrights(BRIGHTS);
      mCallback.onTrigger();


   }

   @Override
   public void onAttach(Activity activity) {
      super.onAttach(activity);

      // This makes sure that the container activity has implemented
      // the callback interface. If not, it throws an exception
      try {
         mCallback = (MyInterface ) activity;
      } catch (ClassCastException e) {
         throw new ClassCastException(activity.toString()
                 + " must implement MyInterface ");
      }
   }
}
