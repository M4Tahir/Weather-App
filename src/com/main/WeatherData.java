package com.main;


import org.json.JSONObject;

public class WeatherData {
    public String description, detailDescription, cityName;
    double maxTemp, minTemp, currentTemp, realFeel, windSpeed;
    int maxTemp1, minTemp1, currentTemp1, realFeel1;
    int humidity, pressure, windDeg;
    private String jsonFormatFile;


    WeatherData(String jsonFormatFile) {
        this.jsonFormatFile = jsonFormatFile;
        // to convert the string to a json object using json library

        JSONObject object = new JSONObject(jsonFormatFile);
        System.out.println(object.getJSONArray("weather").getJSONObject(0).getString("main"));
        currentTemp = object.getJSONObject("main").getDouble("temp");
        maxTemp = object.getJSONObject("main").getDouble("temp_max");
        minTemp = object.optJSONObject("main").getDouble("temp_min");
        realFeel = object.getJSONObject("main").getDouble("feels_like");
        windSpeed = object.getJSONObject("wind").getDouble("speed");

        windDeg = object.getJSONObject("wind").getInt("deg");
        humidity = object.getJSONObject("main").getInt("humidity");
        pressure = object.getJSONObject("main").getInt("pressure");
        description = object.getJSONArray("weather").getJSONObject(0).getString("main"); // clean
        detailDescription = object.getJSONArray("weather").getJSONObject(0).getString("description"); // clear sky
        cityName = object.getString("name");
        // ----------------------------------- converting kelvin to centigrade-------------------------------------------------
        currentTemp1 = tempConverter(currentTemp);
        maxTemp1 = tempConverter(maxTemp);
        minTemp1 = tempConverter(minTemp);
        realFeel1 = tempConverter(realFeel);


    }

    private int tempConverter(double input) {

        return (int) ((int) input - 273.15);

    }

    public String getDescription() {
        return description;
    }

    public String getDetailDescription() {
        return detailDescription;
    }

    public String getCityName() {
        return cityName;
    }

    public double getWindSpeed() {
        return windSpeed;
    }


    public int getHumidity() {
        return humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public int getWindDeg() {
        return windDeg;
    }

    public int getMaxTemp1() {
        return maxTemp1;
    }

    public int getMinTemp1() {
        return minTemp1;
    }

    public int getCurrentTemp1() {
        return currentTemp1;
    }

    public int getRealFeel1() {
        return realFeel1;
    }


//    ----------------------------------- test purpose----------------------------------------------------------------------------------

//    public static void main(String[] args) {
//        WeatherData wd = new WeatherData(j);
//        System.out.println(wd.getCityName());
//        System.out.println(wd.getDescription());
//        System.out.println(wd.getDetailDescription());
//        System.out.println(wd.getMaxTemp());
//        System.out.println(wd.getMinTemp());
//        System.out.println(wd.getCurrentTemp());
//        System.out.println(wd.getWindSpeed());
//        System.out.println(wd.getWindDeg());
//        System.out.println(wd.getRealFeel());
//        System.out.println(wd.getHumidity());
//        System.out.println(wd.getPressure());
//
//    }

//    ----------------------------------- test purpose----------------------------------------------------------------------------------
}
