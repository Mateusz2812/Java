package Weather;

import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class City {
    private String apiBase = "http://api.openweathermap.org/data/2.5/weather?q=";
    private String lang = "pl";
    private String apiKey = "b01556b5a5279db7b3f364454a5b7024";
    private Map<String, Object> weatherMap = null;
    private String city;
    private URL url;

    private static final Logger log = Logger.getRootLogger();

    public City(String city) {
        this.city = city;
        try
        {
            url = new URL(apiBase + city + "&appid=" + apiKey + "&lang=" + lang);
        }
        catch (MalformedURLException e)
        {
            log.error("City constructor: ", e);
        }
    }

    private Map JsonToMap(String str)
    {
        return new Gson().fromJson(str, Map.class);
    }

    public void Update() throws IOException {
            String out = new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next();
            weatherMap = JsonToMap(out);
            Date nowDate = new Date();
            weatherMap.put("update_time", nowDate);
            log.debug("City.Update: city data updated");
    }

    public Map BasicMap()
    {
        Map<String, Object> basicMap = new HashMap<>();
        basicMap.put("name", weatherMap.get("name"));
        basicMap.put("temp", JsonToMap(weatherMap.get("main").toString()).get("temp"));
        basicMap.put("pressure", JsonToMap(weatherMap.get("main").toString()).get("pressure"));
        basicMap.put("feels_like", JsonToMap(weatherMap.get("main").toString()).get("feels_like"));
        basicMap.put("humidity", JsonToMap(weatherMap.get("main").toString()).get("humidity"));
        basicMap.put("wind_speed", JsonToMap(weatherMap.get("wind").toString()).get("speed"));
        basicMap.put("clouds", JsonToMap(weatherMap.get("clouds").toString()).get("all"));
        log.debug("City.BasicMap: Basic map created");
        return basicMap;
    }
}
