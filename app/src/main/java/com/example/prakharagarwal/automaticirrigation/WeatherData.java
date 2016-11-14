package com.example.prakharagarwal.automaticirrigation;

/**
 * Created by prekshasingla on 14/11/16.
 */
public class WeatherData {

    int weatherId ;
    //long date ;
    String city;
    String friendlyDateText ;
    //String dateText ;
    String icon;
    String description ;
    double high;
    double low;
    // Read humidity from cursor and update view
    float humidity ;
    // Read wind speed and direction from cursor and update view
    double windSpeedStr ;
    double windDirStr;
    // Read pressure from cursor and update view
    double pressure ;

    public WeatherData(int weatherId,String city,String friendlyDateText,String icon,
            String description ,double high, double low, float humidity ,double windSpeedStr,
            double windDirStr,double pressure){
        this.weatherId=weatherId;
        //this.date=date;
        this.city=city;
        this.friendlyDateText=friendlyDateText;
        this.icon=icon;
        //this.dateText=dateText;
        this.description=description;
        this.high=high;
        this.low=low;
        this.humidity=humidity;
        this.windSpeedStr=windSpeedStr;
        this.windDirStr=windDirStr;
        this.pressure=pressure;
    }
}
