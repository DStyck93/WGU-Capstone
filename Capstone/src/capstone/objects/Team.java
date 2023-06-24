package capstone.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import capstone.Main;

/**
 * This class designs the team object and contains relevant functions.
 */
public class Team {

    private String city;
    private String name;
    private List<Roster> rosters;
    
    // Constructor
    public Team(String city, String name){
        this.city = city;
        this.name = name;
        this.rosters = new ArrayList<>();
    }

    // Getters
    public String getCity(){return city;}
    public String getName(){return name;}
    public List<Roster> getRosters(){return rosters;}

    // Setters
    public void setCity(String city){this.city = city;}
    public void setName(String name){this.name = name;}
    public void setRosters(List<Roster> rosters){this.rosters = rosters;}

    /**
     * Adds the given roster to the current team's list of rosters.
     * @param roster The roster being added.
     */
    public void addRoster(Roster roster){
        rosters.add(roster);
    }

    /**
     * Gets the team object based on the name given.
     * @param name The name of the team to return.
     * @return The appropriate team.
     */
    public static Team getTeamByName(String name){

        // Check Main.teams for a team that matches the given name.
        for(Team t : Main.teams){

            // If a match is found, return the team.
            if(t.name.equals(name)){
                return t;
            }
        }

        // If no match is found return an empty team and display an error message.
        JOptionPane.showMessageDialog(null, "No team found with the name: " + name);
        return null;
    }

    /**
     * Gets a list of team names.
     * @return A list of team names.
     */
    public static List<String> getTeamNames(){

        // List
        List<String> teamNames = new ArrayList<>();

        // For each team in Main.teams, add its name to the List
        for(Team t : Main.teams){
            teamNames.add(t.getName());
        }

        // Sort the list alphabetically
        Collections.sort(teamNames, String.CASE_INSENSITIVE_ORDER);

        // Return the List
        return teamNames;
    }
   
}
