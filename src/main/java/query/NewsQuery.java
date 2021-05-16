package query;

import article.Article;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class NewsQuery implements ApiQuery {
    private String query;
    public Map<String, String> filterMap;

    // Contains all the possible filter values for the API
    public final static Set<String> FILTER_KEYS = new HashSet<>(
            Arrays.asList("q", "qlnTitle", "sources", "domains", "excludeDomains", "from", "to", "language",
                    "sortBy", "pageSize", "page")
    );

    public NewsQuery() {
        filterMap = new HashMap<>();
        query = domainUrl + "everything?";

        // Default filter is language: english
        filterQuery("language", "en");
    }

    @Override
    public void filterQuery(@NotNull String name, @NotNull String value) throws RuntimeException{
        // Validating name and value
        // check if string is lowercase letters
        if(!FILTER_KEYS.contains(name)) {
            throw new RuntimeException("Invalid query parameter!");
        }
        // validates the given value for the corresponding name
        if (!value.matches(REGEX_MAP.get(name))) {
            throw new RuntimeException("Invalid query value!");
        }
        filterMap.put(name, value);
    }

    @Override
    public void makeAPICall() {

    }

    @Override
    public <T extends Article> List<T> getResult() {
        return null;
    }
}
