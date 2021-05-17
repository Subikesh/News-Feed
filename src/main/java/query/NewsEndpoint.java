package query;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This contains two constants for news filters
 * EVERYTHING: used to make news queries with more than one filter
 * TOP-HIGHLIGHTS: used to make simple filter with language, country etc.
 */
public enum NewsEndpoint {
    EVERYTHING("everything", new HashSet<>(
            Arrays.asList("q", "qlnTitle", "sources", "domains", "excludeDomains", "from", "to", "language",
                    "sortBy", "pageSize", "page")
    )),
    TOP_HEADLINES("top-headlines", new HashSet<>(
            Arrays.asList("q", "sources", "category", "language", "country", "pageSize", "page")
    ));

    public final String value;
    public final Set<String> parameters;

    NewsEndpoint(String val, Set<String> parameters) {
        this.value = val;
        this.parameters = parameters;
    }

    public NewsEndpoint next() {
        if(ordinal() == values().length -1)
            return values()[0];
        return values()[ordinal() + 1];
    }
}
