package com.example.rs_hot_weather.api;


import android.support.annotation.NonNull;
import android.util.Log;

import com.example.rs_hot_weather.api.models.CurrentWeather;
import com.example.rs_hot_weather.api.models.DailyForecast;
import com.example.rs_hot_weather.api.models.HourlyForecast;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherApi {

    private static final String UNITS_METRIC = "metric";

    private static WeatherApi instance;
    private final WeatherServiceInterface service;

    public static WeatherApi getInstance() {
        if(instance == null) {
            instance = new WeatherApi();
        }
        return instance;
    }

    private WeatherApi() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(WeatherServiceInterface.BASE_URL)
                .build();

        service = retrofit.create(WeatherServiceInterface.class);
    }

    public void getCurrentWeather(double latitude, double longitude,
                                  final DataListener<CurrentWeather> listener) {
        service.getCurrentWeather(String.valueOf(latitude), String.valueOf(longitude),
                WeatherServiceInterface.API_KEY,
                UNITS_METRIC).enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather>
                    response) {
                if(response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError();
                }
            }

            @Override
            public void onFailure(Call<CurrentWeather> call, Throwable t) {
                listener.onError();
                Log.e("TAG", "getCurrentWeather", t);
            }
        });
    }

    public void getDailyForecast(double latitude, double longitude,
                                 final DataListener<DailyForecast> listener) {
        Log.e("TAG", "Get forecast called");
        String lat = String.format("%.2f", latitude);
        String lon = String.format("%.2f", longitude);
        service.getDailyForecast(lat, lon, WeatherServiceInterface.API_KEY,
                1, UNITS_METRIC).enqueue(new Callback<DailyForecast>() {
            @Override
            public void onResponse(@NonNull Call<DailyForecast> call,
                                   @NonNull Response<DailyForecast> response) {
                if(response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError();
                }
            }

            @Override
            public void onFailure(Call<DailyForecast> call, Throwable t) {
                listener.onError();
                Log.e("TAG", "getDailyForecast", t);
            }
        });
    }

    public void getHourlyForecast(double latitude, double longitude,
                                  final DataListener<HourlyForecast> listener) {
        service.getHourlyForecast(String.valueOf(latitude), String.valueOf(longitude),
                WeatherServiceInterface.API_KEY, 10,
                UNITS_METRIC).enqueue(new Callback<HourlyForecast>() {
            @Override
            public void onResponse(Call<HourlyForecast> call, Response<HourlyForecast>
                    response) {
                if(response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError();
                }
            }

            @Override
            public void onFailure(Call<HourlyForecast> call, Throwable t)
            {
                listener.onError();
            }
        });
    }

    public interface DataListener<T> {
        void onSuccess(T data);
        void onError();
    }
}
