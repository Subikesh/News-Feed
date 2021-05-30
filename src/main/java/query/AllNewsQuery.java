package query;

import article.News;
import com.google.gson.*;
import utilities.DateUtilities;
import utilities.Globals;
import utilities.ShowsMenu;

import java.io.IOException;

/**
 * Class contains query forming functions for everything news endpoint
 */
public class AllNewsQuery extends NewsQuery implements ShowsMenu {
    // Managing the pages of API response object
    int pageNo = 1;
    final int PAGE_SIZE = 10;
    int maxPages = 0;

    // Contains the list of results fetched currently
    JsonArray resultArray;

    /**
     * Assigns the news endpoint as "everything"
     */
    public AllNewsQuery() {
        super(NewsEndpoint.EVERYTHING);
    }

    @Override
    public void showMenu() {
        String option;
        try {
            do {
                String mainMenu = "\n\n-------------------- Top Headlines --------------------\n" +
                        "News:\n";
                String newsString = getNewsString();
                mainMenu += showFilters();
                if(newsString.isEmpty())
                    mainMenu += "\n-- No news found for this filters. Generalize filters to view more news. --\n";
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
                        "9. SortBy\n" +
                        "10. Clear filters\n";
                if(pageNo < maxPages)
                    mainMenu += "11. Next Page\n";
                if(pageNo > 1)
                    mainMenu += "12. Previous Page\n";
                mainMenu += "\n0. Go back previous menu\nYour Option: ";
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
     * Calls the API and sets the page size and page number
     * Gets the headlines of all the news and returns as a string format
     * @return String which has numbers list of news headlines returned by API
     */
    private String getNewsString() {
        filterQuery("page", String.valueOf(pageNo));
        filterQuery("pageSize", String.valueOf(PAGE_SIZE));
        StringBuilder newsString = new StringBuilder();
        resultArray = getResultJson();
        if(resultArray == null)
            return "--No internet connection--";
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
            if (option != 11 && option != 12)
                pageNo = 1;
            switch (option) {
                case 1:
                    System.out.println("Possible language options: ar, de, en, es, fr, he, it, nl, no, pt, ru, se, " +
                            "ud, zh.\nEnter the language code: ");
                    System.out.println("Press 0 to reset filter: ");
                    input = Globals.input.readLine();
                    if(input.equals("0"))
                        removeFilter("language");
                    else
                        filterQuery("language", input);
                    break;
                case 2:
                    System.out.println("Enter the source' id:\n" +
                            "(Enter comma separated sources list for multiple source filters. Press 1 to view complete list of sources)");
                    System.out.println("Press 0 to reset filter: ");
                    input = Globals.input.readLine();
                    if(input.equals("0"))
                        removeFilter("sources");
                    else if(input.equals("1"))
                        new SourceQuery().showMenu();
                    else {
                        input = input.toLowerCase().replaceAll("\\s+", "");
                        filterQuery("sources", input);
                    }
                    break;
                case 3:
                    System.out.println("Enter the search query: \n" +
                            "(Note: The search query should not contain = or &. Enclose with \" for exact match)");
                    System.out.println("Press 0 to reset filter: ");
                    input = Globals.input.readLine();
                    if(input.equals("0"))
                        removeFilter("q");
                    else
                        filterQuery("q", input);
                    break;
                case 4:
                    System.out.println("Enter the search query: \n" +
                            "(Note: The search query should not contain = or &. Enclose with \" for exact match)");
                    System.out.println("Press 0 to reset filter: ");
                    input = Globals.input.readLine();
                    if(input.equals("0"))
                        removeFilter("qInTitle");
                    else
                        filterQuery("qInTitle", input);
                    break;
                case 5:
                    System.out.println("Enter the domains to filter:\n" +
                            "(A comma-separated string of domains (eg bbc.co.uk, techcrunch.com, engadget.com) " +
                            "to restrict the search to)\n");
                    System.out.println("Press 0 to reset filter: ");
                    input = Globals.input.readLine();
                    if(input.equals("0"))
                        removeFilter("domains");
                    else
                        filterQuery("domains", input);
                    break;
                case 6:
                    System.out.println("Enter the domains to exclude:\n" +
                            "(A comma-seperated string of domains (eg bbc.co.uk, techcrunch.com, engadget.com) " +
                            "to restrict the search to)\n");
                    System.out.println("Press 0 to reset filter: ");
                    input = Globals.input.readLine();
                    if(input.equals("0"))
                        removeFilter("excludeDomains");
                    else
                        filterQuery("excludeDomains", input);
                    break;
                case 7:
                    System.out.println("Enter the starting date:\n" +
                            "(Date should of 'yyyy-mm-dd' or 'yyyy-mm-ddTHH:mm:ss' format. Should be within 1 month within today)");
                    System.out.println("Press 0 to reset filter: ");
                    input = Globals.input.readLine();
                    if(input.equals("0"))
                        removeFilter("from");
                    else if (DateUtilities.isInLastMonth(input))
                        filterQuery("from", input);
                    else
                        System.out.println("Invalid input! Try again...");
                    break;
                case 8:
                    System.out.println("Enter the ending date:\n" +
                            "(Date should of 'yyyy-mm-dd' or 'yyyy-mm-ddTHH:mm:ss' format. Should be within 1 month within today)");
                    System.out.println("Press 0 to reset filter: ");
                    input = Globals.input.readLine();
                    if(input.equals("0"))
                        removeFilter("to");
                    else if (DateUtilities.isInLastMonth(input))
                        filterQuery("to", input);
                    else
                        System.out.println("Invalid input! Try again...");
                    break;
                case 9:
                    System.out.println("Enter the sorting attribute:\n" +
                            "(relevancy = articles more closely related to q come first.\n" +
                            "popularity = articles from popular sources and publishers come first.\n" +
                            "publishedAt = newest articles come first.)");
                    System.out.println("Press 0 to reset filter: ");
                    input = Globals.input.readLine();
                    if(input.equals("0"))
                        removeFilter("sortBy");
                    else
                        filterQuery("sortBy", input);
                    break;
                case 10:
                    clearFilters();
                    break;
                case 11:
                    if(pageNo < maxPages) {
                        pageNo++;
                        System.out.println("Page incremented");
                    }
                    break;
                case 12:
                    if(pageNo > 1) {
                        pageNo--;
                        System.out.println("Page decremented");
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid input! Try again...");
                    break;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
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
            News newsObj = new News(newsObject);
            newsObj.showMenu();
        } else {
            System.out.println("Invalid input! Try again...");
        }
    }
}
