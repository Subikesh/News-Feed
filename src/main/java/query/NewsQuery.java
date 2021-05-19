package query;

import article.Article;
import org.jetbrains.annotations.NotNull;
import com.google.gson.*;

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
    protected JsonObject jsonResult;

    public NewsQuery() {
        filterMap = new HashMap<>();

        // Default Endpoint will be top-headlines
        endpoint = NewsEndpoint.TOP_HEADLINES;

        // Default filter is language: english
        filterQuery("language", "en");
    }

    /**
     * Assignes corresponding endpoint to the class' field
     * @param endpoint NewsEndpoint.EVERYTHING is passed to get advanced filtering
     *                 NewsEndpoint.TOP_HEADLINES is passed to get normal news filters (default)
     */
    public NewsQuery(NewsEndpoint endpoint) {
        this();
        this.endpoint = endpoint;
        if(endpoint.equals(NewsEndpoint.EVERYTHING)) {
            filterQuery("q", "a");
            filterQuery("sortBy", "publishedAt");
        }
    }

    public Map<String, String> getFilters() {
        return filterMap;
    }

    @Override
    public void filterQuery(@NotNull String name, @NotNull String value) throws RuntimeException{
        // Validating name and value
        if(!endpoint.parameters.contains(name)) {
            throw new RuntimeException("Invalid parameter for " + endpoint.value + " endpoint.");
        }
        // validates the given value for the corresponding name
        if (!value.matches(REGEX_MAP.get(name))) {
            throw new RuntimeException("The given value for '" + name +"' is not valid!");
        }
        filterMap.put(name, value);
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public <T extends Article> List<T> getResult() {
        makeAPICall();
        return null;
    }

    @Override
    public JsonArray getResultJson() throws RuntimeException {
        makeAPICall();
        if (jsonResult.get("status").getAsString().equals("error")) {
            throw new RuntimeException("Code: " + jsonResult.get("code") + "\n Message: "+ jsonResult.get("message"));
        }
        return jsonResult.get("articles").getAsJsonArray();
    }
}
