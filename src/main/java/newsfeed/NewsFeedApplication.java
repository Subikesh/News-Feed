package newsfeed;

/**
 * Main class which invokes the application
 */
public class NewsFeedApplication {
    /**
     * The main function from which program execution starts
     * @param args command line arguments
     */
    public static void main(String[] args) {
        MainApplication app = new MainApplication();
        app.showMenu();
    }
}
