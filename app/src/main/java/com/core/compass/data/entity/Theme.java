package com.core.compass.data.entity;

import com.core.compass.data.model.ThemeModel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Package: com.core.ssvapp.data.entity
 * Created by: CuongCK
 * Date: 3/13/18
 */

public class Theme extends RealmObject {
    @PrimaryKey
    private int id;
    private String compassImage;
    private String windowBgColor;
    private String textColor;
    private String degreeImage;
    private String arrowImage;
    private boolean _default;
    private boolean payment;
    private String iconTheme;
    private String iconSetting;
    private String iconShare;
    private String iconFlash;
    private String iconRate;
    private String themeName;
    private boolean is_local;
    private String item_bg;
    private String choose_bg;
    private String image_theme;
    private String icon_location;
    private String balance_group;
    private String balance_focus;

    public Theme() {
    }

    public Theme(ThemeModel model) {
        this.id = model.getId();
        this.compassImage = model.getCompassImage();
        this.windowBgColor = model.getWindowBgColor();
        this.textColor = model.getTextColor();
        this.degreeImage = model.getDegreeImage();
        this.arrowImage = model.getArrowImage();
        this._default = model.getDefault();
        this.payment = model.getPayment();
        this.iconTheme = model.getIconTheme();
        this.iconSetting = model.getIconSetting();
        this.iconShare = model.getIconShare();
        this.iconFlash = model.getIconFlash();
        this.iconRate = model.getIconRate();
        this.themeName = model.getThemeName();
        this.is_local = model.isIs_local();
        this.item_bg = model.getItem_bg();
        this.choose_bg = model.getChoose_bg();
        this.image_theme = model.getImage_theme();
        this.icon_location = model.getIcon_location();
        this.balance_group = model.getBalance_group();
        this.balance_focus = model.getBalance_group_focus();
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

    public String getBalance_focus() {
        return balance_focus;
    }

    public void setBalance_focus(String balance_focus) {
        this.balance_focus = balance_focus;
    }

    public ThemeModel map2Model() {
        ThemeModel model = new ThemeModel(id, compassImage, windowBgColor, textColor, degreeImage, arrowImage, _default, payment, iconTheme, iconSetting, iconShare, iconFlash, iconRate, themeName, is_local, item_bg, choose_bg, image_theme, icon_location, balance_group, balance_focus);
        return model;
    }
}
