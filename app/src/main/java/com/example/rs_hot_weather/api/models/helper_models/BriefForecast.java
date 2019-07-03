package com.example.rs_hot_weather.api.models.helper_models;

import com.example.rs_hot_weather.api.models.WeatherType;

import java.util.List;

public class BriefForecast {

    private long dt;
    private Measurements main;
    private List<GeneralInfo> weather;
    private Clouds clouds;
    private Wind wind;

    /**
     * The beginning of the forecast period
     * @return timestamp in milliseconds in UTC
     */
    public long getTimestamp() {
        return dt * 1000;
    }

    public String getWeatherShortDescription() {
        return weather.get(0).main;
    }

    public String getWeatherLongDescription() {
        return weather.get(0).description;
    }

    public String getImageUrl() {
        return "http://openweathermap.org/img/w/" + weather.get(0).icon + ".png";
    }

    /**
     * Gives the type of weather prevailing at the location
     * @return a constant defined in @WeatherType
     */
    public int getWeatherType() {
        if(weather.get(0).id == WeatherType.CLEAR) {
            return WeatherType.CLEAR;
        } else if(weather.get(0).id / 10 == WeatherType.EXTREME) {
            return WeatherType.EXTREME;
        } else {
            return weather.get(0).id / 100;
        }
    }

    public double getTemperature() {
        return main.temp;
    }

    public double getMaxTemperature() {
        return main.temp_max;
    }

    public double getMinTemperature() {
        return main.temp_min;
    }

    public double getPressure() {
        return main.pressure;
    }

    public double getHumidity() {
        return main.humidity;
    }

    public double getGroundLevel() {
        return main.grnd_level;
    }

    public double getSeaLevel() {
        return main.sea_level;
    }

    public double getTemperatureInKf() {
        return main.temp_kf;
    }

    public double getWindSpeed() {
        return wind.speed;
    }

    public double getWindDirectionInDegrees() {
        return wind.deg;
    }

    public double getCloudinessInPercentage() {
        return clouds.all;
    }
}