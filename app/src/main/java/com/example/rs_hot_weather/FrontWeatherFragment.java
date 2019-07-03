package com.example.rs_hot_weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.rs_hot_weather.api.WeatherApi;
import com.example.rs_hot_weather.api.models.CurrentWeather;
import com.example.rs_hot_weather.api.models.DailyForecast;
import com.example.rs_hot_weather.api.models.helper_models.Forecast;
import com.example.rs_hot_weather.databinding.FrontWeatherCardBinding;
import com.example.rs_hot_weather.databinding.FrontWeatherFragmentBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FrontWeatherFragment extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_GPS = 4;
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private FrontWeatherFragmentBinding binding;
    private FrontWeatherCardBinding grpCurrentWeather;
    private FrontWeatherCardBinding grpTomorrowWeather;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location location;

    private OnFragmentInteractionListener mListener;

    public FrontWeatherFragment() {
        // Required empty public constructor
    }

    public static FrontWeatherFragment newInstance() {
        FrontWeatherFragment fragment = new FrontWeatherFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.front_weather_fragment, container, false);
        setupViews();
        return binding.getRoot();
    }

    private void setupViews() {
        grpCurrentWeather = DataBindingUtil.bind(binding.grpCurrentWeather.getRoot());
        grpTomorrowWeather = DataBindingUtil.bind(binding.grpTomorrowWeather.getRoot());

        binding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshSelected();
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_GPS);
        } else {
            getLocationAndRefresh();
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocationAndRefresh() {
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            FrontWeatherFragment.this.location = location;
                            onRefreshSelected();
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GPS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocationAndRefresh();
                }
                return;
            }
        }
    }

    private void onRefreshSelected() {
        updateData();
    }

    private void updateData() {
        if(location != null) {
            WeatherApi.getInstance().getCurrentWeather(location.getLatitude(), location.getLongitude(),
                    new WeatherApi.DataListener<CurrentWeather>() {

                @Override
                public void onSuccess(CurrentWeather data) {
                    updateCurrentWeather(data);
                    binding.swiperefresh.setRefreshing(false);
                }

                @Override
                public void onError() {
                    binding.swiperefresh.setRefreshing(false);
                    Toast.makeText(getContext(), "Error while updating current weather",
                            Toast.LENGTH_SHORT).show();
                }
            });
            WeatherApi.getInstance().getDailyForecast(location.getLatitude(),
                    location.getLongitude(),
                    new WeatherApi.DataListener<DailyForecast>() {
                @Override
                public void onSuccess(DailyForecast data) {
                    updateDailyForecast(data);
                }

                @Override
                public void onError() {
                    Toast.makeText(getContext(), "Error while updating tomorrow's weather", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateDailyForecast(DailyForecast data) {
        Forecast tomorrowForecast = data.getForecastForDay(0);
        grpTomorrowWeather.grpCard.setBackgroundColor(getResources().getColor(WeatherConditions.getColorByTemperature(tomorrowForecast.getTemperatures().day)));
        grpTomorrowWeather.txtToday.setText(getString(R.string.tomorrow));
        grpTomorrowWeather.txtTodayDate.setText(new SimpleDateFormat(DATE_FORMAT).format(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)));
        grpTomorrowWeather.txtShortWeather.setText(tomorrowForecast.getWeatherShortDescription());
        grpTomorrowWeather.imgWeather.setImageResource(WeatherConditions.getImageByWeatherType(tomorrowForecast.getWeatherType()));
        grpTomorrowWeather.txtTemp.setText(getString(R.string.temperature_holder, (int) tomorrowForecast.getTemperatures().day));
        grpTomorrowWeather.txtTempAmplitude.setText(getString(R.string.temp_amplitude_holder, (int) tomorrowForecast.getTemperatures().min, (int) tomorrowForecast.getTemperatures().max));
        grpTomorrowWeather.txtDescription.setText(tomorrowForecast.getWeatherLongDescription());
        grpTomorrowWeather.txtClouds.setText(getString(R.string.percantage_placeholder, (int) tomorrowForecast.getCloudinessInPercentage()));
        grpTomorrowWeather.txtWind.setText(getString(R.string.m_per_s_placeholder, (int) tomorrowForecast.getWindSpeed()));
        grpTomorrowWeather.txtHumidity.setText(getString(R.string.percantage_placeholder, (int) tomorrowForecast.getHumidity()));
    }

    private void updateCurrentWeather(CurrentWeather data) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(data.getLocationName());
        grpCurrentWeather.grpCard.setBackgroundColor(getResources().getColor(WeatherConditions.getColorByTemperature(data.getTemperature())));
        grpCurrentWeather.txtTodayDate.setText(new SimpleDateFormat(DATE_FORMAT).format(new Date()));
        grpCurrentWeather.txtShortWeather.setText(data.getWeatherShortDescription());
        grpCurrentWeather.imgWeather.setImageResource(WeatherConditions.getImageByWeatherType(data.getWeatherType()));
        grpCurrentWeather.txtTemp.setText(getString(R.string.temperature_holder, (int) data.getTemperature()));
        grpCurrentWeather.txtTempAmplitude.setText(getString(R.string.temp_amplitude_holder, (int) data.getMinTemperature(), (int) data.getMaxTemperature()));
        grpCurrentWeather.txtDescription.setText(data.getWeatherLongDescription());
        grpCurrentWeather.txtClouds.setText(getString(R.string.percantage_placeholder, (int) data.getCloudinessInPercentage()));
        grpCurrentWeather.txtWind.setText(getString(R.string.m_per_s_placeholder, (int) data.getWindSpeed()));
        grpCurrentWeather.txtHumidity.setText(getString(R.string.percantage_placeholder, (int) data.getHumidity()));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
