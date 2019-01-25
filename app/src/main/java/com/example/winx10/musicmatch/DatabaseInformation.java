package com.example.winx10.musicmatch;


import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class DatabaseInformation {
    public String name;
    public  String age;
    public String instrument;
    public String phnumber;
    public UserLocation location;

    public DatabaseInformation(){
    }

    public DatabaseInformation(String name, String phnumber, String age, UserLocation location,  String instrument) {
        this.name = name;
        this.age = age;
        this.instrument = instrument;
        this.phnumber = phnumber;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getPhnumber() {
        return phnumber;
    }

    public void setPhnumber(String phnumber) {
        this.phnumber = phnumber;
    }

    public UserLocation getLocation() {
        return location;
    }

    public void setLocation(UserLocation location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Name :" + name + "\nAge: " + age + "\nInstrument: " + instrument + "\nPh: " + phnumber + "\nLatitude: " + getLocation().getLatitude() + "\nLongitude: " + getLocation().getLongitude();
    }
}
