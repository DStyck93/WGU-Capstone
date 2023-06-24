package capstone.scenes;

import capstone.Main;
import capstone.objects.Team;
import capstone.objects.User;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 * Designs the edit user scene and its relevant functions.
 */
public class EditUser {

    private static Label name;
    private static Label team;

    /**
     * Loads the edit user scene.
     */
    public static void loadScene(){

        int minWidth = 450;
        int minHeight = 250;

        if(Main.stage.isMaximized()){
            Main.stage.setMaximized(false);
        }
        Main.stage.setScene(buildScene());
        Main.stage.setMinWidth(minWidth);
        Main.stage.setWidth(minWidth);
        Main.stage.setMinHeight(minHeight);
        Main.stage.setHeight(minHeight);
        Main.stage.centerOnScreen();
    }

    /**
     * Creates the edit user scene.
     * @return The edit user scene.
     */
    private static Scene buildScene(){

        Font font = Font.font("Arial", FontWeight.MEDIUM, 15);

        // Name
        Label nameLabel = new Label("Username:");
        name = new Label(Main.currentUser.getUsername());
        nameLabel.setFont(font);
        name.setFont(font);
        Button updateName = new Button("Update");
        updateName.setOnAction(e->{nameHandler();});

        // Password
        Label passwordLabel = new Label("Password:");
        Label password = new Label("********");
        passwordLabel.setFont(font);
        password.setFont(font);
        Button updatePassword = new Button("Update");
        updatePassword.setOnAction(e->{passwordHandler();});

        // Team
        Label teamLabel = new Label("Favorite Team:");
        team = new Label(Main.currentUser.getTeam().getName());
        teamLabel.setFont(font);
        team.setFont(font);
        Button updateTeam = new Button("Update");
        updateTeam.setOnAction(e->{teamHandler();});
        
        // Label Box
        VBox labelBox = new VBox(nameLabel, passwordLabel, teamLabel);
        labelBox.setSpacing(17);
        labelBox.setAlignment(Pos.CENTER_LEFT);
        labelBox.setMinWidth(120);

        // Value box
        VBox valueBox = new VBox(name, password, team);
        valueBox.setSpacing(17);
        valueBox.setAlignment(Pos.CENTER_LEFT);

        // Update Box
        VBox updateBox = new VBox(updateName, updatePassword, updateTeam);
        updateBox.setSpacing(10);
        updateBox.setAlignment(Pos.CENTER_RIGHT);
        updateBox.setMinWidth(100);

        // Info Box
        HBox infoBox = new HBox(labelBox, valueBox, updateBox);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setSpacing(40);

        // Delete
        Button deleteButton = new Button("Remove Account");
        deleteButton.setOnAction(event->{deleteButtonHandler();});

        // Cancel
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event->{cancelButtonHandler();});
        
        // Delete/Cancel HBox
        HBox botHBox = new HBox(deleteButton, cancelButton);
        botHBox.setAlignment(Pos.CENTER);
        botHBox.setSpacing(10);

        // Scene Box
        VBox sceneBox = new VBox(infoBox, botHBox);
        sceneBox.setSpacing(20);
        sceneBox.setAlignment(Pos.CENTER);

        // Return Scene
        return new Scene(sceneBox);
    }

    /**
     * Handles what happens when the update name button is clicked.
     */
    private static void nameHandler(){

        String newName = JOptionPane.showInputDialog(null, "Enter your new username:");

        if(newName != null){

            newName.trim();

            // Test for duplicate name
            boolean isValid = true;
            for(User u : Main.users){
                if(newName.equals(u.getUsername())){
                    JOptionPane.showMessageDialog(null, "Sorry, that username is already in use.");
                    isValid = false;
                }
            }

            if(isValid && validatePassword()){

                // Create updated user object
                User u = new User(Main.currentUser);
                u.setUsername(newName);

                // Update user
                User.updateCurrentUser(Main.currentUser.getUsername(), u);
                JOptionPane.showMessageDialog(null, "Your profile has been updated.");
                name.setText(newName);
            }
        }
        
    }

    /**
     * Handles what happens when the update password button is clicked.
     */
    private static void passwordHandler(){

        JPasswordField newPF = new JPasswordField();        // New Password Field
        JPasswordField confirmPF = new JPasswordField();    // Confirm Password Field

        int selection = JOptionPane.showConfirmDialog(null, newPF, "Enter your new password:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(selection == JOptionPane.OK_OPTION) {

            int selection2 = JOptionPane.showConfirmDialog(null, confirmPF, "Confirm your new password:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(selection2 == JOptionPane.OK_OPTION){

                String newPassword = new String(newPF.getPassword()).trim();
                String confirmPassword = new String(confirmPF.getPassword()).trim();
                if(newPassword.equals(confirmPassword) && newPassword.length() > 0){

                    String newHashedPassword = User.hashPassword(newPassword);
                    boolean isValid = true;
        
                    // Make sure password isn't the same as the old password.
                    if(newHashedPassword.equals(Main.currentUser.getHashedPassword())){
                        isValid = false;
                        JOptionPane.showMessageDialog(null, "Unable to update password. The password entered is the same as your old one.");
                    }
            
                    if(isValid && validatePassword()){
                        User updatedUser = new User(Main.currentUser);
                        String hash = User.hashPassword(newPassword);
                        updatedUser.setHashedPassword(hash);
                        User.updateCurrentUser(Main.currentUser.getUsername(), updatedUser);
                        JOptionPane.showMessageDialog(null, "Your password has been updated.");
                    }

                }
                else if(newPassword.length() == 0){
                    JOptionPane.showMessageDialog(null, "No password detected. Please try again.");
                }
                else{
                    JOptionPane.showMessageDialog(null, "Sorry, the passwords you entered don't match.");
                }
            }
        }
    }

    /**
     * Handles what happens when the update team button is clicked.
     */
    private static void teamHandler(){

        JComboBox<String> jcb = new JComboBox<>();
        for(String x : Team.getTeamNames()){
            jcb.addItem(x);
        }
        jcb.setSelectedItem(Main.currentUser.getTeam().getName());

        int selection = JOptionPane.showConfirmDialog(null, jcb, "Select your favorite team:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(selection == JOptionPane.OK_OPTION && validatePassword()){
            String newTeam = jcb.getSelectedItem().toString();
            team.setText(newTeam);
            User updatedUser = new User(Main.currentUser);
            updatedUser.setTeam(Team.getTeamByName(newTeam));
            User.updateCurrentUser(Main.currentUser.getUsername(), updatedUser);
            JOptionPane.showMessageDialog(null, "Your profile has been updated.");
        }
        
    }

    /**
     * Asks for the user to give their password.
     * Used to update profile information.
     * @return True if the correct password was given, else false.
     */
    private static boolean validatePassword(){

        // Get password from user
        String password = "";
        JPasswordField jpf = new JPasswordField();
        JLabel jl = new JLabel("Enter your password:");
        Box box = Box.createVerticalBox();
        box.add(jl);
        box.add(jpf);
        int x = JOptionPane.showConfirmDialog(null, box, "Password Confirmation", JOptionPane.OK_CANCEL_OPTION);
        if(x == JOptionPane.OK_OPTION){
            password = new String(jpf.getPassword());
        }

        String hash = User.hashPassword(password);
        if(hash.equals(Main.currentUser.getHashedPassword())){
            return true;
        }
        else{
            JOptionPane.showMessageDialog(null, "That password isn't recognized.");
            return false;
        }

    }

    /**
     * Deletes the user's profile when the appropriate button is clicked.
     */
    private static void deleteButtonHandler(){
        
        // Verify the user actually wants to delete their account.
        int confirmDelete = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete your account?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        
        // If the user confirms they want to delete their account
        if(confirmDelete == JOptionPane.YES_OPTION){

            // If correct password was given
            if(validatePassword()){

                // Delete the user
                User.deleteUser(Main.currentUser);

                // Confirmation message
                JOptionPane.showMessageDialog(null, "Deletion Successful");

                // Return to login screen
                Login.loadScene();
            }
        }
    }

    /**
     * Returns the user to the dashboard if the cancel button is clicked.
     */
    private static void cancelButtonHandler(){

        Dashboard.loadScene();
        Main.stage.setWidth(Main.DEFAULT_SCENE_WIDTH);
        Main.stage.setHeight(Main.DEFAULT_SCENE_HEIGHT);
        Main.stage.centerOnScreen();
    }
}
