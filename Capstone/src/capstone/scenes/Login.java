package capstone.scenes;

import capstone.Main;
import capstone.objects.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.swing.JOptionPane;

/**
 * Designs the login scene and contains relevant functions.
 */
public class Login {

    private static TextField usernameTF;
    private static PasswordField pwField;

    /**
     * Displays the login scene.
     */
    public static void loadScene(){

        int minWidth = 300;
        int minHeight = 200;

        Main.stage.setScene(buildScene());
        Main.stage.setMinWidth(minWidth);
        Main.stage.setWidth(minWidth);
        Main.stage.setMinHeight(minHeight);
        Main.stage.setHeight(minHeight);
        Main.stage.centerOnScreen();
    }

    /**
     * Creates and returns the login scene.
     * @return The login scene.
     */
    private static Scene buildScene(){

        // Username
        Label usernameLabel = new Label("Username:");
        usernameLabel.setMinWidth(75);
        usernameTF = new TextField();
        usernameTF.setMinWidth(100);
        HBox usernameBox = new HBox(usernameLabel, usernameTF);
        usernameBox.setSpacing(10);
        usernameBox.setAlignment(Pos.CENTER);

        // Password
        Label passwordLabel = new Label("Password:");
        passwordLabel.setMinWidth(75);
        pwField = new PasswordField();
        pwField.setMinWidth(100);
        HBox passwordBox = new HBox(passwordLabel, pwField);
        passwordBox.setSpacing(10);
        passwordBox.setAlignment(Pos.CENTER);

        // Login Button
        Button loginButton = new Button("Login");
        loginButton.setMinWidth(50);
        loginButton.setOnAction(event->{loginHandler();});

        // New User Button
        Button newUserButton = new Button("New User");
        newUserButton.setMinWidth(70);
        newUserButton.setOnAction(event->{CreateUser.loadScene();});

        // Button Box
        HBox buttonBox = new HBox(loginButton, newUserButton);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);

        // Main Box
        VBox mainBox = new VBox(usernameBox, passwordBox, buttonBox);
        mainBox.setSpacing(10);
        mainBox.setPadding(new Insets(20));
        mainBox.setAlignment(Pos.CENTER);
        
        // Return sene
        return new Scene(mainBox);
    }

    /**
     * Logs the user in when the login button is clicked.
     */
    private static void loginHandler(){

        boolean isValid = false;

        // Get username/password from appropriate fields
        String username = usernameTF.getText().trim();
        String password = pwField.getText().trim();

        // Handle valid login
        for(User u: Main.users){
            if((u.getUsername().equals(username)) && (u.getHashedPassword().equals((User.hashPassword(password))))){ 
                
                isValid = true;

                // Set currentUser
                Main.currentUser = u;

                // Load Dashboard
                Main.stage.close();
                Dashboard.loadScene();
                Main.stage.setWidth(Main.DEFAULT_SCENE_WIDTH);
                Main.stage.setHeight(Main.DEFAULT_SCENE_HEIGHT);
                Main.stage.centerOnScreen();
                Main.stage.show();
                break;
            }
        }

        // Handle invalid login
        if(!isValid){
            JOptionPane.showMessageDialog(null, "Invalid username or password.");       
        }
    }
}