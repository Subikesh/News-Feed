package query;

import article.News;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import utilities.Globals;
import utilities.ShowsMenu;

import java.io.IOException;

/**
 * Class contains query forming functions for top-headline news endpoint
 */
public class TopNewsQuery extends NewsQuery implements ShowsMenu {

    // Managing the pages of API response object
    int pageNo = 1;
    final int PAGE_SIZE = 10;
    int maxPages = 0;

    // Contains the list of results fetched currently
    JsonArray resultArray;

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
                mainMenu += "\nFilters: (command: filter)\n";
                if(getFilters().containsKey("sources")) {
                    mainMenu += "(You cannot apply country and category when sources filter is applied.)\n";
                    mainMenu += "\t1. Country\n" +
                            "\t2. Category\n";
                } else {
                    mainMenu += "1. Country\n" +
                            "2. Category\n";
                }
                if(getFilters().containsKey("country") || getFilters().containsKey("category")) {
                    mainMenu += "(You cannot combine sources filter when country or category filters are applied.)\n";
                    mainMenu += "\t3. Sources\n";
                } else {
                    mainMenu += "3. Sources\n";
                }
                mainMenu += "4. Search\n" +
                        "5. Language\n" +
                        "6. Clear filters\n";
                if(pageNo < maxPages)
                    mainMenu += "7. Next Page\n";
                if(pageNo > 1)
                    mainMenu += "8. Previous Page\n";
                mainMenu +=  "\n0. Go back previous menu\n" +
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
            if (option != 7 && option != 8)
                pageNo = 1;
            switch (option) {
                case 1:
                    if(getFilters().containsKey("sources")) {
                        System.out.println("Try applying this filter after removing sources filter\n");
                        break;
                    }
                    System.out.println("Possible country code options are: ae, ar, at, au, be, bg, br, ca, ch, cu,\n" +
                            "cz, cn, co, de, eg, fr, gb, gr, hk, hu, id, ie, il, in, it, jp, kr, lt, lv, ma, mx, my, ng, nl,\n" +
                            "no, nz, ph, pl, pt, ro, rs, ru, sa, se, sg, si, sk, th, tr, tw, ua, us, ve, za.\n" +
                            "\nEnter the country code: ");
                    System.out.println("Press 0 to reset filter");
                    input = Globals.input.readLine();
                    if(input.equals("0"))
                        removeFilter("country");
                    else
                        filterQuery("country", input);
                    break;
                case 2:
                    if(getFilters().containsKey("sources")) {
                        System.out.println("Try applying this filter after removing sources filter\n");
                        break;
                    }
                    System.out.println("Category options are: business, entertainment, general, health, science, \n" +
                            "sports, technology.\n" +
                            "Enter the category to filter: ");
                    System.out.println("Press 0 to reset filter");
                    input = Globals.input.readLine();
                    if(input.equals("0"))
                        removeFilter("category");
                    else
                        filterQuery("category", input);
                    break;
                case 3:
                    if(getFilters().containsKey("country") || getFilters().containsKey("category")) {
                        System.out.println("Try applying this filter after removing country or category filter.\n");
                        break;
                    }
                    System.out.println("Enter the source' id:\n" +
                            "(Comma separated sources list. Complete list of sources can be viewed from main page)");
                    System.out.println("Press 0 to reset filter");
                    input = Globals.input.readLine();
                    input = input.toLowerCase().replaceAll("\\s+", "");
                    if(input.equals("0"))
                        removeFilter("sources");
                    else
                        filterQuery("sources", input);
                    break;
                case 4:
                    System.out.println("Enter the search query: \n" +
                            "(Note: The search query should not contain = or &. Enclose with \" for exact match)\n");
                    System.out.println("Press 0 to reset filter");
                    input = Globals.input.readLine();
                    if(input.equals("0"))
                        removeFilter("q");
                    else
                        filterQuery("q", input);
                    break;
                case 5:
                    System.out.println("Possible language options: ar, de, en, es, fr, he, it, nl, no, pt, ru, se, " +
                            "ud, zh.\nEnter the language code: ");
                    System.out.println("Press 0 to reset filter: ");
                    input = Globals.input.readLine();
                    if(input.equals("0"))
                        removeFilter("language");
                    else
                        filterQuery("language", input);
                    break;
                case 6:
                    clearFilters();
                    break;
                case 7:
                    if(pageNo < maxPages) {
                        pageNo++;
                        System.out.println("Page incremented");
                    }
                    break;
                case 8:
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
            System.out.println("The value entered is not valid input.");
        }
    }

    /**
     * View complete details of that particular news headline
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
