package query;

import article.Article;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsQuery implements ApiQuery {
    String query;
    Map<String, String> filterMap;

    @Override
    public void filterQuery(String name, String value) {

    }

    @Override
    public void makeAPICall() {

    }

    @Override
    public <T extends Article> List<T> getResult() {
        return null;
    }
}
