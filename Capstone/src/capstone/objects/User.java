package capstone.objects;

import capstone.Main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Designs the user object and contains relevant functions.
 */
public class User {

    private String username;
    private String hashedPassword;
    private Team team;
    
    // Constructors
    public User(String username, String hashedPassword, Team team){
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.team = team;
    }
    public User(User user){
        this.username = user.getUsername();
        this.hashedPassword = user.getHashedPassword();
        this.team = user.getTeam();
    }
    
    // Getters
    public String getUsername(){return username;}
    public String getHashedPassword(){return hashedPassword;}
    public Team getTeam(){return team;}
    
    // Setters
    public void setUsername(String username){this.username = username;}
    public void setHashedPassword(String hashedPassword){this.hashedPassword = hashedPassword;}    
    public void setTeam(Team team){this.team = team;}

    /**
     * Convert's the current user into a String.
     * Used to write data to the users.csv file.
     * @return A string with all of the user's data.
     */
    private String getString(){
        return username + ',' + hashedPassword + ',' + team.getName();
    }

    /**
     * Updates a user's profile.
     * @param oldUsername The user being updated.
     * @param updatedUser The updated user object.
     */
    public static void updateCurrentUser(String oldUsername, User updatedUser){


        // Update users.csv
        String filePath = "src/capstone/csv/users.csv";     // Path to update
        String tempPath = "src/capstone/csv/oldUsers.csv";  // Temp path to copy data to

        try{
            
            // Rename old file
            File oldFile = new File(filePath);
            File renamedFile = new File(tempPath);
            oldFile.renameTo(renamedFile);

            // Set up reader
            FileReader fr = new FileReader(renamedFile);
            BufferedReader br = new BufferedReader(fr);

            // Set up writer
            File newFile = new File(filePath);
            FileWriter fw = new FileWriter(newFile);
            fw.write("username,password,team\n"); // Write header

            // Read each line in the original file
            String line = br.readLine();
            line = br.readLine(); // Skip 1st line
            while(line != null){

                // Get username from line
                String[] values = line.split(",");
                String username = values[0];

                // Write data that isn't being updated to the new file
                if(!username.equals(oldUsername)){
                    fw.write(line + "\n");
                }

                // Continue to next line
                line = br.readLine();
            }

            // Write updated user to file
            fw.write(updatedUser.getString());

            // Close reader/writer
            br.close();
            fw.flush();
            fw.close();

            // Delete old file
            Files.deleteIfExists(Paths.get(tempPath));

        }catch(Exception e){
            System.out.println("Error updating users.csv");
        }

        // Update user objects
        for(User u : Main.users){
            if(u == Main.currentUser){
                u.setUsername(updatedUser.getUsername());
                u.setHashedPassword(updatedUser.getHashedPassword());
                u.setTeam(updatedUser.getTeam());
                Main.currentUser = updatedUser;
            }
        }        
    }

    public static void deleteUser(User user){

        // Delete from program
        Main.users.remove(user);

        // Update CSV
        String filePath = "src/capstone/csv/users.csv";
        String tempPath = "src/capstone/csv/temp.csv";

        try{
            
            File userFile = new File(filePath);
            File tempFile = new File(tempPath);

            // Set up reader
            BufferedReader reader = new BufferedReader(new FileReader (userFile));

            // Set up writer
            FileWriter writer = new FileWriter(tempFile);
            writer.write("username,password(hashed),team"); // Write header

            // Get list of users to put on new file
            List<User> updatedUsers = new ArrayList<>();
            reader.readLine(); // skip 1st line;
            String line = reader.readLine();
            while(line != null){

                // Get user info from line
                String[] values = line.split(",");
                String username = values[0];
                String hashedPassword = values[1];
                Team team = Team.getTeamByName(values[2]);

                // Add to updated users List
                if(!username.equals(user.username)){
                    updatedUsers.add(new User(username, hashedPassword, team));
                }
                
                // Continue to next line
                line = reader.readLine();
            }

            for(User u : updatedUsers){
                writer.write('\n' + u.username + ',' + u.hashedPassword + ',' + u.getTeam().getName());
            }

            // Close Reader/Writer
            writer.flush();
            writer.close();
            reader.close();

            // Delete old file
            Files.deleteIfExists(Paths.get(filePath));

            // Rename Temp File
            tempFile.renameTo(userFile);

        }catch(Exception e){
            System.out.println("Error removing user.");
        }
    }

    /**
     * Hashes a password using the SHA-256 algorithm.
     * @param password The password being hashed.
     * @return The hashed password.
     */
    public static String hashPassword(String password){
        String hexString = null;
        try {hexString = toHexString(getSHA(password));}
        catch(NoSuchAlgorithmException e) {System.out.println("Error in User.hashPassword() - NoSuchAlgorithmException thrown");}
        if(hexString == null) {System.out.println("Error in User.hashPassword() - hexString = null");}
        return hexString;
    }

    //region THIS PORTION OF CODE WAS TAKEN FROM GEEKSFORGEEKS.ORG/SHA-256-HASH-IN-JAVA AND HAS BEEN SLIGHTLY MODIFIED.
    /**
     * Takes a byte array an converts it into a string.
     * THIS FUNCTION WAS COPIED FROM GEEKSFORGEEKS.ORG AND HAS BEEN SLIGHTLY MODIFIED.
     * @param hash The byte array being converted.
     * @return The converted byte array.
     */
    private static String toHexString(byte[] hash){

        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeroes
        while(hexString.length() < 64) {hexString.insert(0, '0');}

        return hexString.toString();
    }

    /**
     * Takes a given string, runs it through the SHA-256 algorithm, and converts it to an array of bytes.
     * THIS FUNCTION WAS COPIED FROM GEEKSFORGEEKS.ORG AND HAS BEEN SLIGHTLY MODIFIED.
     * @param input The string being converted.
     * @return A byte array.
     * @throws NoSuchAlgorithmException Needed for the SHA-256 algorithm.
     */
    private static byte[] getSHA(String input) throws NoSuchAlgorithmException{

        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called to calculate message digest of an input and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }
    //endregion
}
