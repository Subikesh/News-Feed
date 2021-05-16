package query;

import java.util.List;
import article.*;

public interface ApiQuery {
    String domainUrl = "https://newsapi.org/v2/";
    String API_KEY = "27eee780f9e5438091906ffff0548b73";

    void filterQuery(String name, String value);
    void makeAPICall();
    <T extends Article> List<T> getResult();
}
