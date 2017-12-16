package com.shujrah.spaceshooter;

import com.badlogic.gdx.Gdx;

/**
 * Created by waqar on 12/11/2017.
 */

public class MyLib {
    public static boolean logging;

    public static float timeState=0f;


    public MyLib(){

    }

    public static void llog(float time, String str) {

        if (!logging) {
            return;
        }

        timeState += Gdx.graphics.getDeltaTime();

        if (timeState >= time) {
            // 1 second just passed
            timeState = 0f; // reset our timer
            Gdx.app.log("Log", str);
        }
    }

    public static boolean flip(Boolean bit){
        return !bit; // ? true : false;

    }

}
