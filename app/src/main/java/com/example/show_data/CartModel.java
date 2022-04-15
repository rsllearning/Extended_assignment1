package com.example.show_data;

public class CartModel {
    String kye,name,image;
    int qualtity;
    Float price;
    float totalprice;


    public CartModel() {

        this.kye = kye;
        this.name = name;
        this.image = image;
        this.price = price;
        this.qualtity = qualtity;
        this.totalprice = totalprice;
    }


    public String getKye() {
        return kye;
    }

    public void setKye(String kye) {
        this.kye = kye;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public int getQualtity() {
        return qualtity;
    }

    public void setQualtity(int qualtity) {
        this.qualtity = qualtity;
    }

    public float getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(float totalprice) {
        this.totalprice = totalprice;
    }
}
