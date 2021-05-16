package query;

import java.util.*;
import article.*;

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

    void filterQuery(String name, String value);
    void makeAPICall();
    <T extends Article> List<T> getResult();
}
