package capstone.scenes;

import capstone.Main;
import capstone.objects.Team;
import capstone.objects.User;

import java.io.FileWriter;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.swing.JOptionPane;

/**
 * Constructs the create user scene and contains relevant functions.
 */
public class CreateUser {

    private static TextField usernameTF;
    private static PasswordField pf1;
    private static PasswordField pf2;
    private static ComboBox<String> teamCB;
    
    /**
     * Loads the create user scene
     */
    public static void loadScene(){

        int minWidth = 300;
        int minHeight = 250;

        Main.stage.setScene(buildScene());
        Main.stage.setMinWidth(minWidth);
        Main.stage.setWidth(minWidth);
        Main.stage.setMinHeight(minHeight);
        Main.stage.setHeight(minHeight);
        Main.stage.centerOnScreen();
    }

    /**
     * Constructs and returns the create user scene.
     * @return The create user scene.
     */
    private static Scene buildScene(){

        // Labels
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Label confirmPasswordLabel = new Label("Confirm Password:");
        Label teamLabel = new Label("Favorite Team:");

        // Text/Password fields
        usernameTF = new TextField();
        pf1 = new PasswordField();
        pf2 = new PasswordField();

        // Team ComboBox
        teamCB = new ComboBox<>(FXCollections.observableArrayList(Team.getTeamNames()));
        teamCB.getSelectionModel().selectFirst();

        // Buttons
        Button cancelButton = new Button("Cancel");
        Button createUserButton = new Button("Create User");

        // Button Handlers
        cancelButton.setOnAction(event->{Login.loadScene();});
        createUserButton.setOnAction(event->{createUserButtonHandler();});

        // Layouts
        VBox labelVBox = new VBox(usernameLabel, passwordLabel, confirmPasswordLabel, teamLabel);
        VBox inputVBox = new VBox(usernameTF, pf1, pf2, teamCB);
        HBox inputHBox = new HBox(labelVBox, inputVBox);
        HBox buttonHBox = new HBox(cancelButton, createUserButton);
        VBox mainVBox = new VBox(inputHBox, buttonHBox);

        // Alignment
        mainVBox.setAlignment(Pos.CENTER);
        inputHBox.setAlignment(Pos.CENTER);
        inputVBox.setAlignment(Pos.CENTER_LEFT);
        labelVBox.setAlignment(Pos.CENTER_RIGHT);
        buttonHBox.setAlignment(Pos.CENTER);

        // Spacing
        mainVBox.setSpacing(10);
        inputHBox.setSpacing(10);
        labelVBox.setSpacing(20);
        inputVBox.setSpacing(10);
        buttonHBox.setSpacing(10);

        // Return Scene
        return new Scene(mainVBox);
    }
   
    /**
     * Creates a new user profile when the create user button is clicked.
     */
    private static void createUserButtonHandler(){

        // Get Variables from user input
        String username = usernameTF.getText().trim();
        String password = pf1.getText().trim();
        String passwordConfirmation = pf2.getText().trim();
        String teamName = teamCB.getValue();
        boolean isValid = false;

        // Verify fields are not empty
        if(username.isEmpty() || password.isEmpty() || passwordConfirmation.isEmpty()){
            JOptionPane.showMessageDialog(null, "All fields must be filled out.");
        }else{
            isValid = true;
        }

        // Verify passwords patch
        if(!password.equals(passwordConfirmation) && isValid){
            JOptionPane.showMessageDialog(null, "Passwords do not match.");
            isValid = false;
        }
        
        // Make sure username is unique
        if(isValid){
            for(User u : Main.users){
                if(u.getUsername().equals(username)){
                    isValid = false;
                    break;
                }
            }
        }

        // If everything validates, create the user with the given information.
        if(isValid){

            String hash = User.hashPassword(password);

            // Create String for csv file
            String userStr = '\n' + username + ',' + hash + ',' + teamName;

            // Write to csv file
            try{
            FileWriter fw = new FileWriter("src/capstone/csv/users.csv", true);
            fw.write(userStr);
            fw.close();
            }catch(Exception e){
                System.out.println("Unable to write to users.csv.");
            }

            // Get Team object
            Team team = Team.getTeamByName(teamName);

            // Add user to List of users
            User user = new User(username, hash, team);
            Main.users.add(user);

            // Return to login screen
            Login.loadScene();
        }

        else{
            JOptionPane.showMessageDialog(null, "Sorry, that username is already in use.");
        }
        
    }
}
