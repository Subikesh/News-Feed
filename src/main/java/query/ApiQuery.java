package query;

import java.util.*;
import article.*;

/**
 * Interface contains the methods for creating, editing, and making API calls to newsAPI
 */
public interface ApiQuery {
    String domainUrl = "https://newsapi.org/v2/";
    String API_KEY = "27eee780f9e5438091906ffff0548b73";

    // Contains all the key value pairs for regex validation
    HashMap<String, String> REGEX_MAP = new HashMap<>()
    {{
        put("q", "^[a-z\\-\\.\\(\\)\\+\\\"]+$");
        put("qlnTitle", "^[a-z\\-\\.\\(\\)\\+\\\"]+$");
        put("sources", "^[a-z\\-]+$");
        put("domains", "^[a-z\\.]+$");
        put("excludeDomains", "^[a-z\\.]+$");
        put("from", "^\\d{4}\\-\\d{2}\\-\\d{2}(T\\d{2}(:\\d{2}){2})?$");
        put("to", "^\\d{4}\\-\\d{2}\\-\\d{2}(T\\d{2}(:\\d{2}){2})?$");
        put("language", "[a-z]{2}");
        put("sortBy", "^[a-z]+$");
        put("pageSize", "\\d+");
        put("page", "\\d+");
        put("lowerCase", "^[a-z]+$");
    }};

    /**
     * Validates the name and value and applies the filter to the API results
     * @param name attributes for filter like language, sources, etc
     * @param value value for the corresponding filter type - en, fr, it for languages
     */
    void filterQuery(String name, String value);

    /**
     * Makes an API call for the filters made till then and stores the JSON in result
     */
    void makeAPICall();

    /**
     * Returns result as objects
     * @param <T> Object like News or Source
     * @return a news or source object for the class in which it is implemented
     */
    <T extends Article> List<T> getResult();
}
