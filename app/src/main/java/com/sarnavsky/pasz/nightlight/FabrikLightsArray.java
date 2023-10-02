package com.sarnavsky.pasz.nightlight;

import java.util.ArrayList;

/**
 * Created by pasz on 18.02.2018.
 */

public class FabrikLightsArray  {

    public ArrayList<Light> getLightsArray(){
        ArrayList mylight = new ArrayList();
        mylight.add(new Light(R.drawable.an_bear, R.string.bear));
        mylight.add(new Light(R.drawable.an_monkey, R.string.monkey));
        mylight.add(new Light(R.drawable.an_chiken, R.string.chiken));
        mylight.add(new Light(R.drawable.an_squirle, R.string.squirrel));
        mylight.add(new Light(R.drawable.an_elephant, R.string.elephant));
        mylight.add(new Light(R.drawable.an_snail, R.string.snail));
        mylight.add(new Light(R.drawable.an_banny, R.string.bunny));
        mylight.add(new Light(R.drawable.an_fox, R.string.fox));
        mylight.add(new Light(R.drawable.an_owl, R.string.owl));
        mylight.add(new Light(R.drawable.an_giraffe, R.string.giraffe));
        mylight.add(new Light(R.drawable.an_penguin, R.string.penguin));
        mylight.add(new Light(R.drawable.an_pig, R.string.pig));
        mylight.add(new Light(R.drawable.an_hipopotam, R.string.hipopotam));
        mylight.add(new Light(R.drawable.an_kaktus, R.string.cactus));
        mylight.add(new Light(R.drawable.an_rabbit, R.string.rebbit));
        mylight.add(new Light(R.drawable.an_kitty, R.string.kitty));
        mylight.add(new Light(R.drawable.an_sheep, R.string.sheep));
        mylight.add(new Light(R.drawable.an_moon, R.string.moon));
        mylight.add(new Light(R.drawable.an_butterfly, R.string.butterfly));
        mylight.add(new Light(R.drawable.an_panda, R.string.panda));
        mylight.add(new Light(R.drawable.an_lion, R.string.lion));
        mylight.add(new Light(R.drawable.an_frog, R.string.frog));
        mylight.add(new Light(R.drawable.an_mouse, R.string.mouse));
        return mylight;
    }

}
