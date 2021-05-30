package query;

import article.Article;
import org.jetbrains.annotations.NotNull;
import com.google.gson.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Contains all the utility functions for making news based API calls
 */
public class NewsQuery implements ApiQuery {
    private String query;
    private NewsEndpoint endpoint;
    private Map<String, String> filterMap;
    private Map<String, String> defaultFilter;
    protected JsonObject jsonResult;

    public NewsQuery() {
        filterMap = new HashMap<>();

        // Default Endpoint will be top-headlines
        endpoint = NewsEndpoint.TOP_HEADLINES;

        // Default filter is language: english
        defaultFilter = new HashMap<>() {{
            put("language", "en");
        }};
        clearFilters();
    }

    /**
     * Assigns corresponding endpoint to the class' field
     * @param endpoint NewsEndpoint.EVERYTHING is passed to get advanced filtering
     *                 NewsEndpoint.TOP_HEADLINES is passed to get normal news filters (default)
     */
    public NewsQuery(NewsEndpoint endpoint) {
        this();
        this.endpoint = endpoint;
        if(endpoint.equals(NewsEndpoint.EVERYTHING)) {
            // For EVERYTHING endpoint, one of q, source or domain is mandatory
            defaultFilter = new HashMap<>() {{
                put("q", "a");
                put("sortBy", "publishedAt");
            }};
            clearFilters();
        } else if (endpoint.equals(NewsEndpoint.SOURCE)) {
            // For 'source' endpoint, the query can be made without any parameters
            defaultFilter = new HashMap<>();
            clearFilters();
        }
    }

    public Map<String, String> getFilters() {
        return filterMap;
    }

    @Override
    public void filterQuery(@NotNull String name, @NotNull String value) throws RuntimeException {
        // Validating name and value
        if(!endpoint.parameters.contains(name)) {
            throw new RuntimeException("Invalid parameter for " + endpoint.value + " endpoint.");
        }
        // validates the given value for the corresponding name
        if (!value.matches(REGEX_MAP.get(name))) {
            throw new RuntimeException("The given value for '" + name +"' is not valid!");
        }

        filterMap.put(name, URLEncoder.encode(value, StandardCharsets.UTF_8));
    }

    @Override
    public void removeFilter(@NotNull String key) {
        if (defaultFilter.containsKey(key)) {
            filterMap.replace(key, defaultFilter.get(key));
        } else {
            filterMap.remove(key);
        }
    }

    @Override
    public void clearFilters() {
        filterMap.clear();
        for (String key : defaultFilter.keySet()) {
            filterQuery(key, defaultFilter.get(key));
        }
    }

    @Override
    public void updateQuery() {
        StringBuilder queryBuilder = new StringBuilder(domainUrl);
        queryBuilder.append(endpoint.value).append("?");
        for(String key: filterMap.keySet()) {
            queryBuilder.append(key).append("=").append(filterMap.get(key)).append("&");
        }
        queryBuilder.append("apiKey="+ API_KEY);
        query = queryBuilder.toString();
    }

    @Override
    public void makeAPICall() {
        updateQuery();
        try {
            System.out.println("Making API request...");
            URL url = new URL(query);
//            System.out.println(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            url.openConnection().connect();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
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
        } catch (IOException e) {
            System.out.println("No internet connection!");
        }
    }

    @Override
    public JsonArray getResultJson() throws RuntimeException {
        makeAPICall();
        if (jsonResult == null)
            return null;
        if (jsonResult.get("status").getAsString().equals("error")) {
            throw new RuntimeException("Code: " + jsonResult.get("code") + "\n Message: "+ jsonResult.get("message"));
        }
        if (endpoint.equals(NewsEndpoint.SOURCE)) {
            return jsonResult.get("sources").getAsJsonArray();
        } else
            return jsonResult.get("articles").getAsJsonArray();
    }
}
