
public interface Api {
    /**
     * Confirms the API is available and we pass authentication.
     *
     * @return false on not 200
     */
    public boolean connect();

    /**
     * Get a three-day forecast
     *
     * @param zip The zip to get weather on.
     * @return
     */
    public String getForecast(String zip);
}
