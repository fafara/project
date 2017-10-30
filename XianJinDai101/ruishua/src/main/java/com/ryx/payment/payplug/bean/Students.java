package com.ryx.payment.payplug.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by XCC on 2017/1/17.
 */

public class Students implements Parcelable {
    private String name;
    private int age;
    private long number;

    public Students(String name, int age, long number) {
        this.name = name;
        this.age = age;
        this.number = number;
    }

    public Students() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.age);
        dest.writeLong(this.number);
    }

    protected Students(Parcel in) {
        this.name = in.readString();
        this.age = in.readInt();
        this.number = in.readLong();
    }

    public static final Parcelable.Creator<Students> CREATOR = new Parcelable.Creator<Students>() {
        @Override
        public Students createFromParcel(Parcel source) {
            return new Students(source);
        }

        @Override
        public Students[] newArray(int size) {
            return new Students[size];
        }
    };

    @Override
    public String toString() {
        return "Students{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", number=" + number +
                '}';
    }
}
