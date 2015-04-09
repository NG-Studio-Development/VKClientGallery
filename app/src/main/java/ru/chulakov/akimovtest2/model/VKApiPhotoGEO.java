package ru.chulakov.akimovtest2.model;

import android.os.Parcelable;

import com.vk.sdk.api.model.Identifiable;
import com.vk.sdk.api.model.VKApiPhoto;

import org.json.JSONObject;

public class VKApiPhotoGEO extends VKApiPhoto implements Parcelable, Identifiable {

    public double latitude;

    public double longitude;

    @Override
    public VKApiPhoto parse(JSONObject from) {

        if (from.has("lat") && from.has("long")) {
            latitude = from.optDouble("lat");
            longitude = from.optDouble("long");
        } else {
            latitude = Double.MIN_VALUE;
            longitude = Double.MIN_VALUE;
        }

        return super.parse(from);
    }

    public String getMaxImageURI() {

        if (!super.photo_2560.isEmpty())
            return super.photo_2560;
        else if(!super.photo_1280.isEmpty())
            return super.photo_1280;
        else if(!super.photo_807.isEmpty())
            return super.photo_807;
        else if(!super.photo_604.isEmpty())
            return super.photo_604;
        else if(!super.photo_130.isEmpty())
            return super.photo_130;
        else if(!super.photo_75.isEmpty())
            return super.photo_75;

        return new String();
    }

    public boolean hasLocation() {
        return latitude != Double.MIN_VALUE && longitude != Double.MAX_VALUE;
    }
}
