package com.example.forum.Service;


import com.example.forum.Entity.Vo.response.WeatherVO;

public interface WeatherService {
    WeatherVO fetchWeather(double longitude, double latitude);
}