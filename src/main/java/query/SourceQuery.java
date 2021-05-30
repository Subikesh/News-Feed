package query;

import article.Source;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import utilities.Globals;
import utilities.ShowsMenu;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SourceQuery extends NewsQuery implements ShowsMenu {

    // Managing the pages of API response object
    int pageNo = 1;
    final int PAGE_SIZE = 15;
    int maxPages = 0;

    // Contains the list of results fetched currently
    JsonArray resultArray;

    /**
     * Assigns the NewsQuery endpoint as SOURCE
     */
    public SourceQuery() {
        super(NewsEndpoint.SOURCE);
    }

    @Override
    public void showMenu() {
        String option = null;
        String sourceTitles = "";
        try {
            do {
                String mainMenu = "\n\n-------------------- Sources --------------------\n" +
                        "Source ids:\n";
                if(option != null) {
                    String[] prevOption = option.split("\\s+");
                    if (prevOption.length != 2 || !(prevOption[1].equals("5") || prevOption[1].equals("6"))) {
                        pageNo = 1;
                        sourceTitles = getSourceTitles();
                    }
                } else
                    sourceTitles = getSourceTitles();
                mainMenu += "Filters applied: " + getFilters();
                if(sourceTitles.isEmpty())
                    mainMenu += "\n-- No sources found for this filters. Generalize filters to view more sources. --\n";
                else
                    mainMenu += "\t\t\tPage no: " + pageNo +"/" + maxPages + "\n";
                mainMenu += sourceTitles;
                mainMenu += "\nFilters: (command: filter)\n" +
                        "1. Category\n" +
                        "2. Language\n" +
                        "3. Country\n" +
                        "4. Clear filters\n";
                if(pageNo < maxPages)
                    mainMenu += "5. Next Page\n";
                if(pageNo > 1)
                    mainMenu += "6. Previous Page\n";
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
            } else if(action[0].equalsIgnoreCase("source")) {
                sourceActions(Integer.parseInt(action[1]));
            } else {
                sourceActions(Integer.parseInt(option));
            }
        } catch (NumberFormatException e) {
            System.out.println("Illegal input. Try again...");
        }
    }

    /**
     * Calls the API setting the page size and page number
     * Gets the title of all the sources and returns as a string format
     * @return String which has numbered list of source titles returned by API
     */
    private String getSourceTitles() {
        StringBuilder newsString = new StringBuilder();
        resultArray = getResultJson();
        if(resultArray == null)
            return "--No internet connection--";
        maxPages = (int) Math.ceil((double) resultArray.size() / PAGE_SIZE);
        // Menu count
        int sourceCount = 1;
        for (int i = (pageNo-1) * PAGE_SIZE; i < resultArray.size() && i < (pageNo * PAGE_SIZE); i++) {
            JsonObject source = resultArray.get(i).getAsJsonObject();
            newsString.append(sourceCount++).append(". ");
            newsString.append(source.get("id").getAsString()).append("\n");
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
            if (option != 5 && option != 6) {
                removeFilter("page");
            }
            switch (option) {
                case 1:
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
                case 2:
                    System.out.println("Possible language options: ar, de, en, es, fr, he, it, nl, no, pt, ru, se, " +
                            "ud, zh.\nEnter the language code: ");
                    System.out.println("Press 0 to reset filter: ");
                    input = Globals.input.readLine();
                    if(input.equals("0"))
                        removeFilter("language");
                    else
                        filterQuery("language", input);
                    break;
                case 3:
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
                case 4:
                    clearFilters();
                    break;
                case 5:
                    if(pageNo < maxPages) {
                        pageNo++;
                        System.out.println("Page incremented");
                    }
                    break;
                case 6:
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
     * View complete detail of that particular source
     * @param option option selected by user as integer
     */
    private void sourceActions(int option) {
        if(option > 0 && option <= PAGE_SIZE) {
            JsonObject sourceObject = resultArray.get((option - 1) + (PAGE_SIZE * (pageNo-1))).getAsJsonObject();
            Source sourceObj = new Source(sourceObject);
            sourceObj.showMenu();
        } else {
            System.out.println("Invalid input! Try again...");
        }
    }
}
