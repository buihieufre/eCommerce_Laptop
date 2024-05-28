package com.example.appbanlaptop.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class LaptopProduct implements Parcelable {
    private String name;
    private String imageUrl;
    private double oldPrice;
    private int discount;
    private int quantity;

    // Các phương thức getter và setter

    // Triển khai Parcelable
    protected LaptopProduct(Parcel in) {
        name = in.readString();
        imageUrl = in.readString();
        oldPrice = in.readDouble();
        discount = in.readInt();
        quantity = in.readInt();
    }

    public static final Creator<LaptopProduct> CREATOR = new Creator<LaptopProduct>() {
        @Override
        public LaptopProduct createFromParcel(Parcel in) {
            return new LaptopProduct(in);
        }

        @Override
        public LaptopProduct[] newArray(int size) {
            return new LaptopProduct[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeDouble(oldPrice);
        dest.writeInt(discount);
        dest.writeInt(quantity);
    }
}
