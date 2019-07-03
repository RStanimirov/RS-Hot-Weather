package com.example.rs_hot_weather.api;

import com.example.rs_hot_weather.api.models.CurrentWeather;
import com.example.rs_hot_weather.api.models.DailyForecast;
import com.example.rs_hot_weather.api.models.HourlyForecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherServiceInterface {

    public static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    public static final String API_KEY = "cb82cff148792ac240ecf68a83b88024";
    //RS: key obtained after registration at openweathermap.org,
    // but working only for current weather forecast:

    @GET("weather")
    Call<CurrentWeather> getCurrentWeather(@Query("lat") String lat,
                                           @Query("lon") String lon,
                                           @Query("APPID") String apiKey,
                                           @Query("units") String units);

    @GET("forecast/daily")
    Call<DailyForecast> getDailyForecast(@Query("lat") String lat,
                                         @Query("lon") String lon,
                                         @Query("APPID") String apiKey,
                                         @Query("cnt") int daysCount,
                                         @Query("units") String units);

    @GET("forecast")
    Call<HourlyForecast> getHourlyForecast(@Query("lat") String lat,
                                           @Query("lon") String lon,
                                           @Query("APPID") String apiKey,
                                           @Query("cnt") int hoursCount,
                                           @Query("units") String units);
}
