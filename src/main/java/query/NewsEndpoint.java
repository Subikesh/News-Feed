package query;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This contains three constants for news filters
 * EVERYTHING: used to make news queries with more than one filter
 * TOP-HIGHLIGHTS: used to make simple filter with language, country etc.
 * SOURCE: used for getting the news-sources list
 */
public enum NewsEndpoint {
    EVERYTHING("everything", new HashSet<>(
            Arrays.asList("q", "qInTitle", "sources", "domains", "excludeDomains", "from", "to", "language",
                    "sortBy", "pageSize", "page")
    )),
    TOP_HEADLINES("top-headlines", new HashSet<>(
            Arrays.asList("q", "sources", "category", "language", "country", "pageSize", "page")
    )),
    SOURCE("sources", new HashSet<>(
            Arrays.asList("category", "language", "country")
    ));

    public final String value;
    public final Set<String> parameters;

    NewsEndpoint(String val, Set<String> parameters) {
        this.value = val;
        this.parameters = parameters;
    }
}
