package com.example.rs_hot_weather.api.models;


import com.example.rs_hot_weather.api.models.helper_models.BriefForecast;
import com.example.rs_hot_weather.api.models.helper_models.LocationInfo;

import java.util.List;

public class HourlyForecast {

    private LocationInfo city;
    private List<BriefForecast> list;

    public String getLocationName() {
        return city.name;
    }

    public List<BriefForecast> getForecasts() {
        return list;
    }
}
