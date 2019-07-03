package com.example.rs_hot_weather.api.models.helper_models;

import com.example.rs_hot_weather.api.models.WeatherType;

import java.util.List;

public class Forecast {
    private Temperatures temp;
    private double pressure;
    private double humidity;
    private List<GeneralInfo> weather;
    private double speed;
    private double deg;
    private double clouds;

    public String getWeatherShortDescription() {
        return weather.get(0).main;
    }

    public String getWeatherLongDescription() {
        return weather.get(0).description;
    }

    public String getImageUrl() {
        return "https://openweathermap.org/img/w/" + weather.get(0).icon + ".png";
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

    public Temperatures getTemperatures() {
        return temp;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return speed;
    }

    public double getWindDirectionInDegrees() {
        return deg;
    }

    public double getCloudinessInPercentage() {
        return clouds;
    }
}
