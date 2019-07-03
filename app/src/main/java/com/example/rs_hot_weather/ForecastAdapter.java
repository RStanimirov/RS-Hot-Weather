package com.example.rs_hot_weather;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.rs_hot_weather.api.models.HourlyForecast;
import com.example.rs_hot_weather.databinding.DetailWeatherMaskBinding;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastHolder> {

    private HourlyForecast data;

    public ForecastAdapter(HourlyForecast data) {
        this.data = data;
    }

    @Override
    public ForecastHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DetailWeatherMaskBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.detail_weather_mask, parent, false);
        return new ForecastHolder(binding);
    }

    @Override
    public void onBindViewHolder(ForecastHolder holder, int position) {
        holder.bind(data.getForecasts().get(position));
    }

    @Override
    public int getItemCount() {
        return data.getForecasts().size();
    }
}
