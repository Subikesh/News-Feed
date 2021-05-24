package utilities;

/**
 * Implemented by any class which displays main menu
 */
public interface ShowsMenu {
    /**
     * Displays the menu with all the actions to be done here
     */
    void showMenu();

    /**
     * Performs the action asked by the user in the Main menu.
     * @param option option specified by the user
     */
    void performAction(String option);
}
