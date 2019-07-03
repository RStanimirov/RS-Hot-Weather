package com.example.rs_hot_weather;

import android.support.v7.widget.RecyclerView;

import com.example.rs_hot_weather.api.models.helper_models.BriefForecast;
import com.example.rs_hot_weather.databinding.DetailWeatherMaskBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ForecastHolder extends RecyclerView.ViewHolder {

    private DetailWeatherMaskBinding binding;

    public ForecastHolder(DetailWeatherMaskBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(BriefForecast shortForecast) {
        binding.txtTodayDate.setText(new SimpleDateFormat("dd/MM HH:mm").format(new Date(shortForecast.getTimestamp())));
        binding.imgWeather.setImageResource(com.example.rs_hot_weather.WeatherConditions.getImageByWeatherType(shortForecast.getWeatherType()));
        binding.grpMask.setBackgroundColor(binding.getRoot().getContext().getResources().getColor
                (com.example.rs_hot_weather.WeatherConditions.getColorByTemperature(shortForecast.getTemperature())));
        binding.txtDescription.setText(shortForecast.getWeatherLongDescription());
        binding.txtShortWeather.setText(shortForecast.getWeatherShortDescription());
        binding.txtTemperature.setText(binding.getRoot().getContext().getString(R.string.temperature_holder,
                (int) shortForecast.getTemperature()));
    }
}
