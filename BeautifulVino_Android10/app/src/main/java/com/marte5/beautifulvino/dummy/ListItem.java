package com.marte5.beautifulvino.dummy;

import android.os.Parcelable;

import java.lang.reflect.ParameterizedType;

/**
 * Created by Marte5, Maria Tourbanova on 20/03/18.
 */

public abstract class ListItem implements Parcelable{

    public static final int TYPE_AZ = 0;
    public static final int TYPE_VINO = 1;

    abstract public int getType();
}
