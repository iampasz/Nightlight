package com.sarnavsky.pasz.nightlight;

import android.app.Application;
import android.content.Context;


//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;


///**
// * This is a subclass of {@link Application} used to provide shared objects for this app, such as
// * the {@link Tracker}.
// */
public class App extends Application {
  //  private static Tracker mTracker;
    static App app;
    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());
        context = getApplicationContext();
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
//    synchronized public static Tracker getDefaultTracker() {
//        if (mTracker == null) {
//            GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
//            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
//            mTracker = analytics.newTracker(R.xml.global_tracker);
//        }
//        return mTracker;
//    }
//    public static void sendEvent(String event){
//        getDefaultTracker().send(new HitBuilders.EventBuilder()
//                .setAction(event)
//                .setCategory("Config")
//                .build());
//    }
}