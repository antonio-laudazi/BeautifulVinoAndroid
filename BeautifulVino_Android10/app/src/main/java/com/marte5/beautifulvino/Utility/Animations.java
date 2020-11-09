package com.marte5.beautifulvino.Utility;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by Marte5, Maria Tourbanova on 02/03/18.
 */

public class Animations {

    public static void moveViewToInitPosition(View view, int finalPosition) {

        ObjectAnimator mover = ObjectAnimator.ofFloat(view, "translationY", 0, finalPosition);
        mover.setDuration(800);
        mover.start();
    }

    public static void moveViewToFinalPosition(View view, int finalPosition) {
        ObjectAnimator mover = ObjectAnimator.ofFloat(view, "translationY",-finalPosition, 0);
        mover.setDuration(800);
        mover.start();

    }

}
