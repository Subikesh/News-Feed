package query;

import article.Article;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.*;

public class NewsQuery implements ApiQuery {
    private String query;
    private NewsEndpoint endpoint;
    private Map<String, String> filterMap;
    private JsonObject jsonResult;

    public NewsQuery() {
        filterMap = new HashMap<>();

        // Default Endpoint will be top-headlines
        endpoint = NewsEndpoint.TOP_HEADLINES;

        // Default filter is language: english
        filterQuery("language", "en");
    }

    public Map<String, String> getFilters() {
        return filterMap;
    }

    public NewsEndpoint checkEndPoint() {
        if (NewsEndpoint.TOP_HEADLINES.parameters.containsAll(filterMap.keySet()))
            return NewsEndpoint.TOP_HEADLINES;
        else if(NewsEndpoint.EVERYTHING.parameters.containsAll(filterMap.keySet()))
            return NewsEndpoint.EVERYTHING;
        else
            return null;
    }

    public void updateEndPoint() throws RuntimeException{
        NewsEndpoint newEndPoint = checkEndPoint();
        if(newEndPoint == null) {
            throw new RuntimeException("The parameters are mixed up. Either give parameters of 'top-headlines' " +
                    "or the parameters of 'everything' endpoint");
        } else {
            endpoint = newEndPoint;
        }
    }

    @Override
    public void updateQuery() {
        StringBuilder queryBuilder = new StringBuilder(domainUrl);
        updateEndPoint();
        queryBuilder.append(endpoint.value).append("?");
        for(String key: filterMap.keySet()) {
            queryBuilder.append(key).append("=").append(filterMap.get(key)).append("&");
        }
        queryBuilder.append("apiKey="+ API_KEY);
        query = queryBuilder.toString();
    }

    @Override
    public void filterQuery(@NotNull String name, @NotNull String value) throws RuntimeException{
        // Validating name and value
        // check if string is lowercase letters
        if(!endpoint.parameters.contains(name)) {
            updateEndPoint();
        }
        // validates the given value for the corresponding name
        if (!value.matches(REGEX_MAP.get(name))) {
            throw new RuntimeException("Invalid query value!");
        }
        filterMap.put(name, value);
    }

    @Override
    public void makeAPICall() {
        updateQuery();
        try {
            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            System.out.println(query);
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            StringBuilder jsonString = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                jsonString.append(output);
            }
            conn.disconnect();
            output = jsonString.toString();

            // Gets the jsonObject from JSON string notation
            jsonResult = new Gson().fromJson(output, JsonObject.class);
            if (jsonResult.get("status").getAsString().equals("error")) {
                throw new RuntimeException("Code: " + jsonResult.get("code") + "\n Message: "+ jsonResult.get("message"));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public <T extends Article> List<T> getResult() {
        return null;
    }

    public static void main(String[] args) {
        ApiQuery newQuery = new NewsQuery();
        newQuery.makeAPICall();
    }
}
