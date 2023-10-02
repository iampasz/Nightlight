//package com.sarnavsky.pasz.nightlight2;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//
//import com.sarnavsky.pasz.nightlight.AdCard;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by pasz on 14.05.2017.
// */
//
//public class AdbActivity extends Activity {
//
//    private List<AdCard> adCards;
//    Context ctx;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.adb_layaut);
//
//        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
//
//        rv.setHasFixedSize(true);
//
//         ctx = this;
//
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        rv.setLayoutManager(llm);
//
//        initializeData();
//
//
//
//
//
//    RVAdapter adapter = new RVAdapter(adCards);
//        rv.setAdapter(adapter);
//
//
//
//    }
//
//    private void initializeData() {
//        adCards = new ArrayList<>();
//        adCards.add(new AdCard(R.string.bnl, R.string.bnld, R.string.bnll, R.drawable.frengsm));
//        adCards.add(new AdCard(R.string.abcru, R.string.abcrud, R.string.abcrul, R.drawable.abvrusm));
//        adCards.add(new AdCard(R.string.abcen, R.string.abcend, R.string.abcenl, R.drawable.abcsm));
//        adCards.add(new AdCard(R.string.abcua, R.string.abcuad, R.string.abcual, R.drawable.abvuasm));
//        adCards.add(new AdCard(R.string.abcenс, R.string.abcendс, R.string.abcenlс, R.drawable.engsm));
//        adCards.add(new AdCard(R.string.youtube, R.string.youtubed, R.string.youtubel, R.drawable.youtube));
//        adCards.add(new AdCard(R.string.instagram, R.string.instagramd, R.string.instagraml, R.drawable.instagram));
//    }
//}