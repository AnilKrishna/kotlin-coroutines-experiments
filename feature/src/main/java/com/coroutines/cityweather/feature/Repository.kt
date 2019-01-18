package com.coroutines.cityweather.feature


object Repository {

    /**
     * API key generated from https://openweathermap.org/
     * f08f5bce8d0fe0c0903f145a88d773f8 - working - 16days weather
     * 7d5f1eb65a69dc70eb88897e111c8d5f - free api test - 5 days
     * 04520cbaa7b7548673e29e5f8225e843
     */
    internal val appid = "f08f5bce8d0fe0c0903f145a88d773f8"

    /**
     * @throws IllegalStateException
     */
    suspend fun getCityCharts(city: String): List<Chart> {
        val forecasts = getDailyForecastsByCity(city, 10, "metric").list?.toList() ?: return emptyList()
        forecasts.size > 1 || return emptyList()
        return listOf(
                forecasts.tempChart,
                forecasts.humidityAndCloudinessChart,
                forecasts.windSpeedChart,
                forecasts.pressureChart,
                forecasts.rainAndSnowChart
        )
        // TODO: more charts? use all private functions from below?
    }

    /**
     * @param city City name + optional country code after comma
     * @param units Default (null) means in Kelvin, "metric" means in Celsius, "imperial" means in Fahrenheit
     * @throws IllegalStateException
     */
    private suspend fun getForecastByCity(city: String, units: String? = null)
            : OpenWeatherMapApi.Forecast
            = OpenWeatherMapApi.service.getForecastByCity(appid, city, units).await()

    /**
     * Up to five days forecasts with data every 3 hours for given city name
     * @param city City name + optional country code after comma
     * @param cnt Optional maximum number of forecasts in returned "list"
     * @param units Default (null) means in Kelvin, "metric" means in Celsius, "imperial" means in Fahrenheit
     * @throws IllegalStateException
     */
    private suspend fun getForecastsByCity(city: String, cnt: Long? = null, units: String? = null)
            : OpenWeatherMapApi.Forecasts
            = OpenWeatherMapApi.service.getForecastsByCity(appid, city, cnt, units).await()

    /**
     * Up to 16 days daily forecasts for given city
     * @param city City name + optional country code after comma
     * @param cnt Optional maximum number of daily forecasts in returned "list"
     * @param units Default (null) means in Kelvin, "metric" means in Celsius, "imperial" means in Fahrenheit
     * @throws IllegalStateException
     */
    private suspend fun getDailyForecastsByCity(city: String, cnt: Long? = null, units: String? = null)
            : OpenWeatherMapApi.DailyForecasts
            = OpenWeatherMapApi.service.getDailyForecastsByCity(appid, city, cnt, units).await()
}