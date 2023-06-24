package capstone.scenes;

import capstone.Main;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * Designs a menu bar that will be used among multiple scenes.
 */
public class MyMenuBar {

    /**
     * Creates a menu bar that is used in multiple scenes.
     * @return A menu bar.
     */
    public static MenuBar getMyMenuBar(){

        // Main Menu Items
        MenuItem editProfileMenuItem = new MenuItem("Edit Profile");
        MenuItem logOutMenuItem = new MenuItem("Log out");

        // Scene Menu Items
        MenuItem DashboardMenuItem = new MenuItem("Dashboard");
        MenuItem skaterPredictionsMenuItem = new MenuItem("Skater Predictions");
        MenuItem comparePlayersMenuItem = new MenuItem("Compare Players");
        MenuItem teamViewMenuItem = new MenuItem("Team View");

        // Event Handlers
        editProfileMenuItem.setOnAction(e->{EditUser.loadScene();});
        logOutMenuItem.setOnAction(e->{logOutHandler();});
        DashboardMenuItem.setOnAction(e->{Dashboard.loadScene();});
        comparePlayersMenuItem.setOnAction(e->{ComparePlayers.loadScene();});
        skaterPredictionsMenuItem.setOnAction(e->{SkaterPredictions.loadScene();});
        teamViewMenuItem.setOnAction(e->{TeamView.loadScene();});

        // Main Menu
        Menu mainMenu = new Menu("Menu");
        mainMenu.getItems().addAll(editProfileMenuItem, logOutMenuItem);

        // Scenes Menu
        Menu sceneMenu = new Menu("Scenes");
        sceneMenu.getItems().addAll(DashboardMenuItem, skaterPredictionsMenuItem, comparePlayersMenuItem, teamViewMenuItem);

        // Menu Bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(mainMenu, sceneMenu);

        return menuBar;
    }

    /**
     * Logs the user out and returns them to the login screen when the appropriate menu item is clicked.
     */
    private static void logOutHandler(){
        Login.loadScene();
        Main.currentUser = null;
    }
}
