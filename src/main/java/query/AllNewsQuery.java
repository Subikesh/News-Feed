package query;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import newsfeed.Globals;
import newsfeed.ShowsMenu;

import java.io.IOException;

public class AllNewsQuery extends NewsQuery implements ShowsMenu {
    // Managing the pages of API response object
    int pageNo = 1;
    final int PAGE_SIZE = 10;
    int maxPages = 0;

    // Contains the list of results fetched currently
    JsonArray resultArray;

    public AllNewsQuery() {
        super(NewsEndpoint.EVERYTHING);
    }

    @Override
    public void showMenu() {
        String option;
        try {
            do {
                String mainMenu = "\n\n-------------------- Top Headlines --------------------\n" +
                        "News: (command: news)\n";
                String newsString = getNewsString();
                mainMenu += "Filters applied: " + getFilters();
                if(newsString.isEmpty())
                    mainMenu += "-- No news found for this filters. Generalize filters to view more news. --\n";
                else
                    mainMenu += "\t\t\tPage no: " + pageNo +"/" + maxPages + "\n";
                mainMenu += newsString;
                mainMenu += "\nFilters: (command: filter)\n" +
                        "1. Language\n" +
                        "2. Sources\n" +
                        "3. Search\n" +
                        "4. Search title\n" +
                        "5. Domains\n" +
                        "6. excludeDomains\n" +
                        "7. Date from\n" +
                        "8. Date to\n" +
                        "9. SortBy\n";
                if(pageNo < maxPages)
                    mainMenu += "10. Next Page\n";
                if(pageNo > 1)
                    mainMenu += "11. Previous Page\n";
                mainMenu +=  "\n0. Go to main menu\n" +
                        "Your Option: ";
                System.out.println(mainMenu);
                option = Globals.input.readLine();
                performAction(option);
            } while(!(option.equals("0")) && ((option.split("\\s+").length == 1) || !(option.split("\\s+")[1].equals("0"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void performAction(String option) {
        String[] action = option.split("\\s+");
        try {
            if(action[0].equals("0"));
            else if(action[0].equalsIgnoreCase("filter")) {
                filterActions(Integer.parseInt(action[1]));
            } else if(action[0].equalsIgnoreCase("news")) {
                newsActions(Integer.parseInt(action[1]));
            } else {
                newsActions(Integer.parseInt(option));
            }
        } catch (NumberFormatException e) {
            System.out.println("Illegal input. Try again...");
        }
    }

    /**
     * Calls the API setting the page size and page number
     * Gets the headlines of all the news and returns as a string format
     * @return String which has numbers list of news headlines returned by API
     */
    private String getNewsString() {
        filterQuery("page", String.valueOf(pageNo));
        filterQuery("pageSize", String.valueOf(PAGE_SIZE));
        StringBuilder newsString = new StringBuilder();
        resultArray = getResultJson();
        maxPages = (int)Math.ceil(jsonResult.get("totalResults").getAsDouble()/PAGE_SIZE);
        int newsCount = 1;
        for (JsonElement news : resultArray) {
            newsString.append(newsCount++).append(". ");
            newsString.append(news.getAsJsonObject().get("title").getAsString()).append("\n");
        }
        return newsString.toString();
    }

    /**
     * Performs filters as per the option selected by user from menu
     * @param option option selected by user as integer
     */
    private void filterActions(int option) {
        String input;
        try {
            switch (option) {
                case 1:
                    System.out.println("Possible language options: ar, de, en, es, fr, he, it, nl, no, pt, ru, se, " +
                            "ud, zh.\nEnter the language code: ");
                    input = Globals.input.readLine();
                    filterQuery("language", input);
                    break;
                case 2:
                    System.out.println("Enter the source' id:\n" +
                            "(Complete list of sources can from view sources in main page)");
                    input = Globals.input.readLine();
                    filterQuery("sources", input);
                    break;
                case 3:
                    System.out.println("Enter the search query: \n" +
                            "(Note: The search query should be url-encoded string.)");
                    input = Globals.input.readLine();
                    filterQuery("q", input);
                    break;
                case 4:
                    System.out.println("Enter the search query: \n" +
                            "(Note: The search query should be url-encoded string.)");
                    input = Globals.input.readLine();
                    filterQuery("qInTitle", input);
                    break;
                case 5:
                    System.out.println("Enter the domains to filter:\n" +
                            "(A comma-seperated string of domains (eg bbc.co.uk, techcrunch.com, engadget.com) " +
                            "to restrict the search to)\n");
                    input = Globals.input.readLine();
                    filterQuery("domains", input);
                    break;
                case 6:
                    System.out.println("Enter the domains to exclude:\n" +
                            "(A comma-seperated string of domains (eg bbc.co.uk, techcrunch.com, engadget.com) " +
                            "to restrict the search to)\n");
                    input = Globals.input.readLine();
                    filterQuery("excludeDomains", input);
                    break;
                case 7:
                    System.out.println("Enter the starting date:\n" +
                            "(Date should of 'yyyy-mm-dd' format. Should be within 1 month before today)");
                    input = Globals.input.readLine();
                    filterQuery("from", input);
                    break;
                case 8:
                    System.out.println("Enter the ending date:\n" +
                            "(Date should of 'yyyy-mm-dd' format. Should be within 1 month before today)");
                    input = Globals.input.readLine();
                    filterQuery("to", input);
                    break;
                case 9:
                    System.out.println("Enter the sorting attribute:\n" +
                            "(relevancy = articles more closely related to q come first.\n" +
                            "popularity = articles from popular sources and publishers come first.\n" +
                            "publishedAt = newest articles come first.)");
                    input = Globals.input.readLine();
                    filterQuery("sortBy", input);
                    break;
                case 10:
                    if(pageNo < maxPages) {
                        pageNo++;
                        System.out.println("Page incremented");
                    }
                    break;
                case 11:
                    if(pageNo > 1) {
                        pageNo--;
                        System.out.println("Page decremented");
                    }
                    break;
                default:
                    System.out.println("Invalid input! Try again...");
                    break;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            System.out.println("The value entered is not valid input.");
        }
    }

    /**
     * View complete detail of that particular news headline
     * @param option option selected by user as integer
     */
    private void newsActions(int option) {
        if(option > 0 && option <= resultArray.size()) {
            JsonObject newsObject = resultArray.get(option - 1).getAsJsonObject();
            System.out.println("Title: " + newsObject.get("title") +
                    "\nAuthor: " + newsObject.get("author"));
        } else {
            System.out.println("Invalid input! Try again...");
        }
    }

//    public static void main(String[] args) {
//        AllNewsQuery newsQuery = new AllNewsQuery();
//        newsQuery.showMenu();
//    }
}
