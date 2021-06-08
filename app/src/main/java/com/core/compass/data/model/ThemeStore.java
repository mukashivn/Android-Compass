package com.core.compass.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Package: com.core.ssvapp.data.entity
 * Created by: CuongCK
 * Date: 3/13/18
 */

public class ThemeStore {
    @SerializedName("data")
    private ArrayList<ThemeModel> themes;

    public ArrayList<ThemeModel> getThemes() {
        return themes;
    }

}
