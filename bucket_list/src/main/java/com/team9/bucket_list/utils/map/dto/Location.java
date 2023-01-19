package com.team9.bucket_list.utils.map.dto;

// 위도 경도 DTO
public class Location {
    private double lat;     // 위도
    private double lng;     // 경도

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
