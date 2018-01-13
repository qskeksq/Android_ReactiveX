package com.example.administrator.rxandroidbasic08.domain;

/**
 * Created by Administrator on 2017-07-20.
 */

public class Data
{
    private RealtimeWeatherStation RealtimeWeatherStation;

    public RealtimeWeatherStation getRealtimeWeatherStation ()
    {
        return RealtimeWeatherStation;
    }

    public void setRealtimeWeatherStation (RealtimeWeatherStation RealtimeWeatherStation)
    {
        this.RealtimeWeatherStation = RealtimeWeatherStation;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [RealtimeWeatherStation = "+RealtimeWeatherStation+"]";
    }
}