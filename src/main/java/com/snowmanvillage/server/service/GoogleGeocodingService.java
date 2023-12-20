package com.snowmanvillage.server.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleGeocodingService {

    private final String apiKey;
    private final GeoApiContext geoApiContext;

    public GoogleGeocodingService(@Value("${google.maps.api-key}") String apiKey) {
        this.apiKey = apiKey;
        this.geoApiContext = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
    }

    public Map<String, LatLng> getLocationWithBounds(String address) {
        GeocodingResult[] geocodingResults =
            GeocodingApi.geocode(geoApiContext, address).awaitIgnoreError();
        if (geocodingResults != null && geocodingResults.length > 0) {
            LatLng center = geocodingResults[0].geometry.location;
            LatLng northEast = calculateCoordinate(center, 45);
            LatLng southWest = calculateCoordinate(center, 225);
            LatLng northWest = calculateCoordinate(center, 315);
            LatLng southEast = calculateCoordinate(center, 135);
            return Map.of("center", center, "northEast", northEast, "southWest", southWest,
                "northWest", northWest, "southEast", southEast);
        }
        return null;
    }

    private LatLng calculateCoordinate(LatLng center, double bearing) {
        double earthRadius = 6371.0; // 지구 반지름
        double angularDistance = 2 / earthRadius;

        double lat1 = Math.toRadians(center.lat);
        double lng1 = Math.toRadians(center.lng);
        double angularBearing = Math.toRadians(bearing);

        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(angularDistance) +
            Math.cos(lat1) * Math.sin(angularDistance) * Math.cos(angularBearing));
        double lng2 = lng1 + Math.atan2(
            Math.sin(angularBearing) * Math.sin(angularDistance) * Math.cos(lat1),
            Math.cos(angularDistance) - Math.sin(lat1) * Math.sin(lat2)
        );
        return new LatLng(Math.toDegrees(lat2), Math.toDegrees(lng2));
    }
}
