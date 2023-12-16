package com.sarnavsky.pasz.nightlight;

import static android.R.anim.linear_interpolator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;
import com.sarnavsky.pasz.nightlight.Fragments.Brights;
import com.sarnavsky.pasz.nightlight.Fragments.TimerFragment;
import com.sarnavsky.pasz.nightlight.Interfaces.MyCallback;
import com.sarnavsky.pasz.nightlight.databinding.MainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator3;


public class MainActivity extends AppCompatActivity implements Brights.MyInterface {


    MainBinding binding;

    int current_item = 2;
    boolean timerStatus = false;
    int fonStatus = 0;
    int lullabyStatus = 0;
    int bgStatus = 0;
    MyAdapter mAdapter;
    ArrayList<Light> myLight;
    private Timer autoChange;
    private Animation mFadeInAnimation, mFadeOutAnimation;
    boolean checkAutomate = true;
    boolean checkMenu = true;
    boolean show = true;
    AnimationSet set, set2;
    MediaPlayer mediaPlayer;
    Flow flow;
    CircleIndicator3 indicator;
    CountDownTimer offMessage;
    CountDownTimer globalTimer;
    CountDownTimer cdt;


    int addCounter;
    int currentNlBright;
    AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    private RewardedAd rewardedAd;
    private ConsentInformation consentInformation;

    ConsentForm consentForm;

    public MainActivity() {
    }

    @SuppressLint({"ClickableViewAccessibility", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        binding = MainBinding.inflate(getLayoutInflater());

        startGlobalTimer();
        saveNoAdsCount(-1);
        addCounter = getNoAdsCount();
        adRequest = new AdRequest.Builder().build();

        if (addCounter > 0) {
            binding.adsView.setVisibility(View.GONE);

        } else {
            binding.adsView.loadAd(adRequest);
        }

        loadRewardedAd();
        loadInterstitial();

        getBrightsPreference();

        MobileAds.initialize(this, initializationStatus -> {

            Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
            for (String adapterClass : statusMap.keySet()) {
                AdapterStatus status = statusMap.get(adapterClass);
                assert status != null;
                Log.d("MyApp", String.format(
                        "Adapter name: %s, Description: %s, Latency: %d",
                        adapterClass, status.getDescription(), status.getLatency()));
            }

            Log.i("MyApp", "WE ARE HERE ");

        });

        binding.adsView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                //Log.i("MyApp", mAdView.getResponseInfo().getMediationAdapterClassName());
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                //Log.i("MyApp", mAdView.getResponseInfo().getMediationAdapterClassName());
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.i("MyApp", Objects.requireNonNull(Objects.requireNonNull(binding.adsView.getResponseInfo()).getMediationAdapterClassName()));
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                // Log.i("MyApp", mAdView.getResponseInfo().getMediationAdapterClassName());
            }

            @Override
            public void onAdSwipeGestureClicked() {
                super.onAdSwipeGestureClicked();
                // Log.i("MyApp", mAdView.getResponseInfo().getMediationAdapterClassName());
            }
        });

        binding.lockScreen.setOnTouchListener((v, event) -> {
            //clearAlpha();
            showButtons();
            startGlobalTimer();
            return false;
        });

        binding.lockButton.setOnClickListener(v -> {

            clearAlpha();
            //lockButton.setAlpha(1f);

            lockButton();
        });


        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.sound);


        mFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fadeout);

        RotateAnimation rotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f); //2
        ScaleAnimation scale = new ScaleAnimation(2.0f, 2.0f, 2.0f, 2.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(100000); //3
        rotate.setRepeatMode(Animation.INFINITE); //4
        rotate.setRepeatCount(-1); //5

        rotate.setInterpolator(MainActivity.this, linear_interpolator);

        scale.setDuration(100000); //3
        scale.setRepeatMode(Animation.REVERSE); //4
        scale.setRepeatCount(-1); //5

        set = new AnimationSet(false); //10
        set.addAnimation(rotate); //11

        set.addAnimation(scale);

        RotateAnimation rotate2 = new RotateAnimation(360, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f); //2
        rotate2.setDuration(100000);
        rotate2.setInterpolator(MainActivity.this, linear_interpolator);
        rotate2.setRepeatMode(Animation.INFINITE);
        rotate2.setRepeatCount(-1);
        set2 = new AnimationSet(false); //10
        set2.addAnimation(scale);
        set2.addAnimation(rotate2);


        FabrikLightsArray factory = new FabrikLightsArray();
        myLight = factory.getLightsArray();


        mAdapter = new MyAdapter(myLight);

        binding.pager.setAdapter(mAdapter);


        binding.pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.bottomText.setText(myLight.get(binding.pager.getCurrentItem()).mytext);
            }
        });


        indicator = findViewById(R.id.indicator);
        indicator.setViewPager(binding.pager);

// optional
        mAdapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());


        binding.adsView.setOnClickListener(view -> showAlertDialog());


        binding.starsButton.setOnClickListener(v -> {
            //startConsoleTimer();
            clearAlpha();
            binding.starsButton.setAlpha(1f);
            closeOpenFragment();
            setBgStatus();
        });
        binding.lullabyButton.setOnClickListener(view -> {
            //startConsoleTimer();
            clearAlpha();
            binding.lullabyButton.setAlpha(1f);

            closeOpenFragment();
            setLullabyStatus();
        });
        binding.bgcolor.setOnClickListener(view -> {
            //startConsoleTimer();

            clearAlpha();
            binding.bgcolor.setAlpha(1f);

            closeOpenFragment();
            setFonStatus();
        });
        binding.timerButton.setOnClickListener(view -> {

            clearAlpha();
            binding.timerButton.setAlpha(1f);

            closeOpenFragment();


            TimerFragment timer_fragment = (TimerFragment) getSupportFragmentManager().findFragmentByTag("TIMER_FRAGMENT");

            if (timer_fragment != null && timer_fragment.isVisible()) {
                mAdapter.notifyDataSetChanged();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new TimerFragment(), "TIMER_FRAGMENT").commit();
            }
        });

        binding.sunButton.setOnClickListener(v -> {

            clearAlpha();


            binding.sunButton.setAlpha(1f);

            closeOpenFragment();

            Brights brightsFragment = (Brights) getSupportFragmentManager().findFragmentByTag("BRIGHTS_FRAGMENT");

            if (brightsFragment != null && brightsFragment.isVisible()) {
                mAdapter.notifyDataSetChanged();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new Brights(), "BRIGHTS_FRAGMENT").commit();
            }

        });

        binding.autoButton.setOnClickListener(view -> {

            closeOpenFragment();
            setAutomate();
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getSettings();

        //showGDPR


        showGDPR();

    }

    public void startTimer(int hours, int minutes) {
        binding.bottomText.setVisibility(View.VISIBLE);
        binding.bottomText.setText("");
        int mySeconds = (((hours * 60 * 60) + (60 * minutes)) * 1000);
        closeApp(mySeconds);

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onTrigger() {
        mAdapter.notifyDataSetChanged();
        Log.i("BRIGHTS", "notifyDataSetChanged");
    }

    class MyAutoTimer extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(() -> {
                mFadeInAnimation.setAnimationListener(animationFadeOutListener);
                binding.automate.startAnimation(mFadeInAnimation);
            });
        }
    }

    int ic = 1;
    Animation.AnimationListener animationFadeOutListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationEnd(Animation animation) {

            binding.automate.setImageResource(myLight.get(ic).mypic);


            currentNlBright = getBrightsPreference();

            binding.automate.setColorFilter(brightIt(currentNlBright));

            binding.automate.startAnimation(mFadeOutAnimation);
            ic++;

            if (ic == myLight.size()) {
                ic = 0;
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub
            binding.automate.setVisibility(View.VISIBLE);
        }
    };

    public void showToast(String string) {

        binding.bottomText.setVisibility(View.VISIBLE);

        if (offMessage == null) {
            offMessage = new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    if (!timerStatus) {
                        binding.bottomText.setVisibility(View.INVISIBLE);
                        binding.bottomText.setText("");
                    }

                }
            }.start();
        } else {
            offMessage.cancel();
            offMessage.start();
        }


        binding.bottomText.setText(string);
    }

    private void setBgStatus() {

        switch (bgStatus) {
            case 0:
                binding.fon2.startAnimation(set);
                binding.fon3.startAnimation(set2);
                binding.fon2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                binding.fon3.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                binding.fon1.setVisibility(View.VISIBLE);
                binding.fon2.setVisibility(View.VISIBLE);
                binding.fon3.setVisibility(View.VISIBLE);
                bgStatus++;
                showToast(getString(R.string.star_on));
                break;

            case 1:

                binding.fon2.clearAnimation();
                binding.fon3.clearAnimation();
                binding.fon2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                binding.fon3.setScaleType(ImageView.ScaleType.CENTER_CROP);
                bgStatus++;
                showToast(getString(R.string.star_anim_off));
                break;

            case 2:

                binding.fon1.setVisibility(View.GONE);
                binding.fon2.setVisibility(View.GONE);
                binding.fon3.setVisibility(View.GONE);
                showToast(getString(R.string.star_off));
                bgStatus = 0;
                break;
        }

    }

    private void setLullabyStatus() {
        switch (lullabyStatus) {
            case 0:
                binding.lullabyButton.setImageResource(R.drawable.bt_repeat);
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.sound);
                mediaPlayer.start();
                lullabyStatus = 1;
                showToast(getString(R.string.melody));
                break;
            case 1:
                mediaPlayer.reset();

                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.sound);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();

                lullabyStatus = 2;
                showToast(getString(R.string.melody_loop));
                break;
            case 2:
                binding.lullabyButton.setImageResource(R.drawable.bt_nota);
                mediaPlayer.reset();
                lullabyStatus = 0;
                showToast(getString(R.string.melody_off));
                break;
        }
    }

    private void setAutomate() {

        clearAlpha();


        getBrightsPreference();

        if (checkAutomate) {
            binding.autoButton.setAlpha(1f);
            binding.automate.setVisibility(View.VISIBLE);
            binding.pager.setVisibility(View.GONE);
            showToast(getString(R.string.auto_change));
            autoChange = new Timer();
            MyAutoTimer mAutoTimer = new MyAutoTimer();
            autoChange.schedule(mAutoTimer, 3000, 10000);
            checkAutomate = false;
        } else {
            binding.automate.clearAnimation();
            binding.automate.setVisibility(View.GONE);
            showToast(getString(R.string.auto_change_off));
            if (autoChange != null) {
                autoChange.cancel();
            }
            binding.pager.setVisibility(View.VISIBLE);
            checkAutomate = true;
        }
    }

    private void setFonStatus() {

        switch (fonStatus) {
            case 0:

                binding.fon1.setImageResource(R.drawable.bg_green);
                binding.fon2.setImageResource(R.drawable.stars_green);
                binding.fon3.setImageResource(R.drawable.lg_green);
                binding.backgrounds.setBackgroundResource(R.color.green);
                fonStatus++;
                showToast(getString(R.string.bgcolor_green));
                break;

            case 1:
                binding.fon1.setImageResource(R.drawable.bg_purple);
                binding.fon2.setImageResource(R.drawable.stars_purple);
                binding.fon3.setImageResource(R.drawable.lg_purple);
                binding.backgrounds.setBackgroundResource(R.color.purpl);
                fonStatus++;
                showToast(getString(R.string.bgcolor_purple));
                break;

            case 2:
                binding.fon1.setImageResource(R.drawable.bg_red);
                binding.fon2.setImageResource(R.drawable.stars_red);
                binding.fon3.setImageResource(R.drawable.lg_red);
                binding.backgrounds.setBackgroundResource(R.color.orange);
                fonStatus++;
                showToast(getString(R.string.bgcolor_orange));
                break;

            case 3:
                binding.fon1.setImageResource(R.drawable.bg_blue);
                binding.fon2.setImageResource(R.drawable.stars_blue);
                binding.fon3.setImageResource(R.drawable.lg_blue);
                binding.backgrounds.setBackgroundResource(R.color.blue);
                showToast(getString(R.string.bgcolor_c));
                fonStatus++;
                break;

            case 4:
                binding.fon1.setImageResource(R.drawable.bg_gray);
                binding.fon2.setImageResource(R.drawable.stars_gray);
                binding.fon3.setImageResource(R.drawable.lg_gray);
                binding.backgrounds.setBackgroundResource(R.color.black);
                showToast(getString(R.string.bgcolor_durk));
                fonStatus++;
                break;

            case 5:
                binding.fon1.setImageResource(R.drawable.bg_dark_blue);
                binding.fon2.setImageResource(R.drawable.stars_dark_blue);
                binding.fon3.setImageResource(R.drawable.lg_dark_blue);
                binding.backgrounds.setBackgroundResource(R.color.darkblue);
                showToast(getString(R.string.bgcolor_blue));
                fonStatus = 0;
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding.lullabyButton.setAlpha(0.25f);
        mediaPlayer.reset();
        saveSettings();
        lullabyStatus = 0;


    }

    @Override
    protected void onPause() {
        //    mAdView.pause();

        super.onPause();
        binding.lullabyButton.setAlpha(0.25f);

    }

    @Override
    protected void onStop() {
        super.onStop();
        lullabyStatus = 0;

        saveSettings();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void saveSettings() {
        SharedPreferences sharedPref = this.getSharedPreferences("MYPREFS", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("CURRENT_ITEM", binding.pager.getCurrentItem());
        Log.i("SETTINGS", "saveSettings" + binding.pager.getCurrentItem());
        editor.apply();
    }

    public void saveNoAdsCount(int adCounter) {

        int currentCount = getNoAdsCount();
        if (currentCount > 0 && adCounter != -1) {
            SharedPreferences sharedPref = this.getSharedPreferences("MYPREFS", 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("NO_ADS_COUNTER", currentCount + adCounter);
            editor.apply();
        }

        Log.i("ADDCOUNTER", currentCount + " Реклама отключена");
    }

    public int getNoAdsCount() {
        SharedPreferences sharedPref = this.getSharedPreferences("MYPREFS", 0);
        return sharedPref.getInt("NO_ADS_COUNTER", 0);
    }

    public void getSettings() {
        SharedPreferences sharedPref = this.getSharedPreferences("MYPREFS", 0);
        current_item = sharedPref.getInt("CURRENT_ITEM", 0);
        binding.pager.setCurrentItem(current_item);
    }

    public void closeApp(int mySeconds) {

        if (cdt != null) {
            timerStatus = false;
            cdt.cancel();
            cdt = null;
        }
        if (mySeconds > 0) {
            timerStatus = true;
            cdt = new CountDownTimer(mySeconds, 1000) {
                @SuppressLint("DefaultLocale")
                @Override
                public void onTick(long l) {
                    binding.bottomText.setText(String.format("%02d:%02d:%02d", (l / 1000) / 3600, ((l / 1000) % 3600) / 60, (l / 1000) % 60));
                }

                @Override
                public void onFinish() {
                    Log.i("FINISH", "App is OFF");
                    finish();
                }
            };
            cdt.start();
        } else {
            binding.bottomText.setVisibility(View.INVISIBLE);
        }
    }

    public void lockButton() {

        closeOpenFragment();

        if (checkMenu) {
            binding.lockScreen.setClickable(true);

            binding.bottomText.setVisibility(View.INVISIBLE);
            flow.setVisibility(View.INVISIBLE);
            binding.adsView.setVisibility(View.INVISIBLE);
            binding.adsView.setVisibility(View.GONE);
            indicator.setVisibility(View.INVISIBLE);

            binding.lockButton.setImageResource(R.drawable.bt_closed);


            binding.lockScreen.setClickable(true);
            checkMenu = false;
            show = false;
        } else {

            binding.lockScreen.setClickable(false);
            binding.bottomText.setVisibility(View.VISIBLE);
            flow.setVisibility(View.VISIBLE);
            binding.adsView.setVisibility(View.VISIBLE);
            indicator.setVisibility(View.VISIBLE);

            binding.lockButton.setImageResource(R.drawable.bt_unlock);
            binding.lockScreen.setClickable(false);
            checkMenu = true;
            show = true;

            if (getNoAdsCount() < 1) {
                binding.adsView.setVisibility(View.VISIBLE);
                showInterstitial();
            }


        }

    }

    private void startGlobalTimer() {
        if (globalTimer != null) {
            globalTimer.start();
        } else {
            globalTimer = new CountDownTimer(5000, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {

                    // clearAlpha();

                    if (!checkMenu) {
                        binding.lockButton.setVisibility(View.INVISIBLE);
                        binding.bottomText.setVisibility(View.INVISIBLE);
                    }



                }
            }.start();
        }
    }

    private void showButtons() {
        if (checkMenu) {
            binding.lockButton.setVisibility(View.VISIBLE);
            binding.bottomText.setVisibility(View.VISIBLE);
            flow.setVisibility(View.VISIBLE);
            binding.adsView.setVisibility(View.VISIBLE);
        } else {
            binding.lockButton.setVisibility(View.VISIBLE);
            binding.bottomText.setVisibility(View.VISIBLE);
        }
    }

    public void loadInterstitial() {

        if (addCounter <1 ) {
            InterstitialAd.load(this, "ca-app-pub-1237459888817948/5738678237", adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInterstitialAd = interstitialAd;
                            Log.i("ADS", "onAdLoaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            Log.d("ADS", loadAdError.toString());
                            mInterstitialAd = null;
                        }
                    });
        }

    }

    public void showInterstitial() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
            loadInterstitial();
        } else {
            Log.d("ADS", "The interstitial ad wasn't ready yet.");
        }
    }

    public static ColorMatrixColorFilter brightIt(int fb) {
        ColorMatrix cmB = new ColorMatrix();
        cmB.set(new float[]{
                1, 0, 0, 0, fb,
                0, 1, 0, 0, fb,
                0, 0, 1, 0, fb,
                0, 0, 0, 1, 0});

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(cmB);

        return new ColorMatrixColorFilter(colorMatrix);
    }

    public int getBrightsPreference() {
        SharedPreferences sharedPref = this.getSharedPreferences("MYPREFS", 0);
        return sharedPref.getInt("BRIGHT", 0);
    }

    private void closeOpenFragment() {
        List<Fragment> arrayFragments = getSupportFragmentManager().getFragments();
        if (arrayFragments.size() > 0) {
            getSupportFragmentManager().beginTransaction().remove(arrayFragments.get(0)).commit();
        }
    }

    public void clearAlpha() {
        binding.starsButton.setAlpha(0.25f);
        binding.timerButton.setAlpha(0.25f);
        binding.lullabyButton.setAlpha(0.25f);
        binding.backgrounds.setAlpha(0.25f);
        binding.sunButton.setAlpha(0.25f);
        binding.autoButton.setAlpha(0.25f);
        binding.adsView.setAlpha(0.25f);
        binding.lockButton.setAlpha(0.25f);
    }

    public void loadRewardedAd() {
        RewardedAd.load(this, "ca-app-pub-1237459888817948/5203491613",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d("TAG", loadAdError.toString());
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d("TAG", "Ad was loaded.");
                    }
                });
    }

    public void showRewardedAd(MyCallback myCallback) {
        if (rewardedAd != null) {
            rewardedAd.show(this, rewardItem -> {
                // Handle the reward.
                Log.d("TAG", "The user earned the reward.");
                saveNoAdsCount(2);
                myCallback.isShown(true);
                loadRewardedAd();
            });

            rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    // Called when a click is recorded for an ad.
                    Log.d("TAG", "Ad was clicked.");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    Log.d("TAG", "Ad dismissed fullscreen content.");
                    rewardedAd = null;
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    // Called when ad fails to show.
                    Log.e("TAG", "Ad failed to show fullscreen content.");
                    rewardedAd = null;
                }

                @Override
                public void onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d("TAG", "Ad recorded an impression.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d("TAG", "Ad showed fullscreen content.");
                }
            });

        } else {
            Log.d("TAG", "The rewarded ad wasn't ready yet.");
            myCallback.isShown(false);
        }


    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.custom_dialog_layout, null);
        builder.setView(dialogLayout);

        Button positive_button = dialogLayout.findViewById(R.id.positive_button);
        Button negative_button = dialogLayout.findViewById(R.id.negative_button);

        AlertDialog dialog = builder.create();

        positive_button.setOnClickListener(v -> {


            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.appsforkids.pasz.nightlightpromax"));
            startActivity(browserIntent);

            dialog.dismiss();

        });

        negative_button.setOnClickListener(v -> dialog.dismiss());

        //dialog.getWindow().setBackgroundDrawableResource(R.drawable.shape_menu4c);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void showGDPR() {

        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                //.setConsentDebugSettings(debugSettings)
                .setTagForUnderAgeOfConsent(false)
                .build();
        ConsentInformation.OnConsentInfoUpdateSuccessListener inform = this::loadForm;
        ConsentInformation.OnConsentInfoUpdateFailureListener infoFailed = formError -> {
            //  Log.i("TESTID", formError.getMessage() + "onConsentInfoUpdateFailure");
        };

        consentInformation = UserMessagingPlatform.getConsentInformation(this);
        consentInformation.requestConsentInfoUpdate(MainActivity.this, params, inform, infoFailed);

        // consentInformation.reset();
    }

    public void loadForm() {
        // Loads a consent form. Must be called on the main thread.

        UserMessagingPlatform.loadConsentForm(
                this,
                consentForm -> {
                    MainActivity.this.consentForm = consentForm;
                    if (consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.REQUIRED) {
                        consentForm.show(
                                MainActivity.this,
                                formError -> {
                                    consentInformation.getConsentStatus();// App can start requesting ads.

                                    // Handle dismissal by reloading form.
                                    loadForm();
                                });
                    }
                },
                formError -> {

                }
        );
    }
}


