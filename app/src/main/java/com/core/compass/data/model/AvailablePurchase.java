package com.core.compass.data.model;

/**
 * Package: com.core.ssvapp.data.model
 * Created by: CuongCK
 * Date: 3/14/18
 */

public class AvailablePurchase {
    private String sku;
    private String price;
    public AvailablePurchase(String sku, String price) {
        this.sku = sku;
        this.price = price;
    }
    public String getSku() {
        return sku;
    }
    public String getPrice() {
        return price;
    }
}
