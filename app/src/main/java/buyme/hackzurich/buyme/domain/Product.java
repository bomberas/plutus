package buyme.hackzurich.buyme.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by aminendah on 9/16/2017.
 */

public class Product implements  Serializable{

    private String category;
    private String sku;
    private String title;
    private String price;
    private String brand_name;
    private String shop_name;
    private String product_url;
    private String image_id;
    private String img_url;
    private String msrp;
    private String x;
    private String y;
    private String width;
    private String height;

    public Product(){

    }

    public Product(String category, String sku, String title, String price, String brand_name, String shop_name, String product_url, String image_id, String img_url, String msrp, String x, String y, String width, String height) {
        this.category = category;
        this.sku = sku;
        this.title = title;
        this.price = price;
        this.brand_name = brand_name;
        this.shop_name = shop_name;
        this.product_url = product_url;
        this.image_id = image_id;
        this.img_url = img_url;
        this.msrp = msrp;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getProduct_url() {
        return product_url;
    }

    public void setProduct_url(String product_url) {
        this.product_url = product_url;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getMsrp() {
        return msrp;
    }

    public void setMsrp(String msrp) {
        this.msrp = msrp;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
