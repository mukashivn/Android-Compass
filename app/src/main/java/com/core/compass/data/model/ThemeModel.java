package com.core.compass.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Package: com.core.ssvapp.data.model
 * Created by: CuongCK
 * Date: 3/13/18
 */

public class ThemeModel {
    @SerializedName("id")
    private int id;
    @SerializedName("compass_image")
    private String compassImage;
    @SerializedName("window_bg_color")
    private String windowBgColor;
    @SerializedName("text_color")
    private String textColor;
    @SerializedName("degree_image")
    private String degreeImage;
    @SerializedName("arrow_image")
    private String arrowImage;
    @SerializedName("default")
    private boolean _default;
    @SerializedName("payment")
    private boolean payment;
    @SerializedName("icon_theme")
    private String iconTheme;
    @SerializedName("icon_setting")
    private String iconSetting;
    @SerializedName("icon_share")
    private String iconShare;
    @SerializedName("icon_flash")
    private String iconFlash;
    @SerializedName("icon_rate")
    private String iconRate;
    @SerializedName("theme_name")
    private String themeName;
    @SerializedName("is_local")
    private boolean is_local;
    @SerializedName("item_bg")
    private String item_bg;
    @SerializedName("choose_bg")
    private String choose_bg;
    @SerializedName("image_theme")
    private String image_theme;
    @SerializedName("icon_location")
    private String icon_location;
    @SerializedName("balance_group")
    private String balance_group;
    @SerializedName("balance_group_focus")
    private String balance_group_focus;

    public ThemeModel(int id, String compassImage, String windowBgColor, String textColor, String degreeImage, String arrowImage, boolean _default, boolean payment, String iconTheme, String iconSetting, String iconShare, String iconFlash, String iconRate, String themeName, boolean local, String item_bg, String choose_bg, String image_theme, String icon_location, String balance_group, String balance_focus) {
        this.id = id;
        this.compassImage = compassImage;
        this.windowBgColor = windowBgColor;
        this.textColor = textColor;
        this.degreeImage = degreeImage;
        this.arrowImage = arrowImage;
        this._default = _default;
        this.payment = payment;
        this.iconTheme = iconTheme;
        this.iconSetting = iconSetting;
        this.iconShare = iconShare;
        this.iconFlash = iconFlash;
        this.iconRate = iconRate;
        this.themeName = themeName;
        this.is_local = local;
        this.item_bg = item_bg;
        this.choose_bg = choose_bg;
        this.image_theme = image_theme;
        this.icon_location = icon_location;
        this.balance_group= balance_group;
        this.balance_group_focus = balance_focus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompassImage() {
        return compassImage;
    }

    public void setCompassImage(String compassImage) {
        this.compassImage = compassImage;
    }

    public String getWindowBgColor() {
        return windowBgColor;
    }

    public void setWindowBgColor(String windowBgColor) {
        this.windowBgColor = windowBgColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getDegreeImage() {
        return degreeImage;
    }

    public void setDegreeImage(String degreeImage) {
        this.degreeImage = degreeImage;
    }

    public String getArrowImage() {
        return arrowImage;
    }

    public void setArrowImage(String arrowImage) {
        this.arrowImage = arrowImage;
    }

    public boolean getDefault() {
        return _default;
    }

    public void setDefault(boolean _default) {
        this._default = _default;
    }

    public boolean getPayment() {
        return payment;
    }

    public void setPayment(boolean payment) {
        this.payment = payment;
    }

    public String getIconTheme() {
        return iconTheme;
    }

    public void setIconTheme(String iconTheme) {
        this.iconTheme = iconTheme;
    }

    public String getIconSetting() {
        return iconSetting;
    }

    public void setIconSetting(String iconSetting) {
        this.iconSetting = iconSetting;
    }

    public String getIconShare() {
        return iconShare;
    }

    public void setIconShare(String iconShare) {
        this.iconShare = iconShare;
    }

    public String getIconFlash() {
        return iconFlash;
    }

    public void setIconFlash(String iconFlash) {
        this.iconFlash = iconFlash;
    }

    public String getIconRate() {
        return iconRate;
    }

    public void setIconRate(String iconRate) {
        this.iconRate = iconRate;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public boolean is_default() {
        return _default;
    }

    public void set_default(boolean _default) {
        this._default = _default;
    }

    public boolean isPayment() {
        return payment;
    }

    public boolean isIs_local() {
        return is_local;
    }

    public void setIs_local(boolean is_local) {
        this.is_local = is_local;
    }

    public String getItem_bg() {
        return item_bg;
    }

    public void setItem_bg(String item_bg) {
        this.item_bg = item_bg;
    }

    public String getChoose_bg() {
        return choose_bg;
    }

    public void setChoose_bg(String choose_bg) {
        this.choose_bg = choose_bg;
    }

    public String getImage_theme() {
        return image_theme;
    }

    public void setImage_theme(String image_theme) {
        this.image_theme = image_theme;
    }

    public String getIcon_location() {
        return icon_location;
    }

    public void setIcon_location(String icon_location) {
        this.icon_location = icon_location;
    }

    public String getBalance_group() {
        return balance_group;
    }

    public void setBalance_group(String balance_group) {
        this.balance_group = balance_group;
    }

    public String getBalance_group_focus() {
        return balance_group_focus;
    }

    public void setBalance_group_focus(String balance_group_focus) {
        this.balance_group_focus = balance_group_focus;
    }
}
