package capstone.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * This class designs a skater object and contains related functions.
 */
public class Skater {
    
    private String name;        
    private String position; // Position: F, C, W, LW, RW, D
    private String id;    
    private List<Stats> stats;

    // Constructors
    public Skater(){
        this.name="NA";
        this.position="NA";
        this.id="NA";
        this.stats = new ArrayList<>();
    }
    public Skater(Skater s){
        this.name = s.getName();
        this.position = s.getPosition();
        this.id = s.getID();
        this.stats = s.getStats();
    }
    
    // Getters 
    public String getName(){return name;}
    public String getPosition(){return position;}
    public String getID(){return id;}
    public List<Stats> getStats(){return stats;}

    //Setters
    public void setName(String n){this.name=n;}
    public void setPosition(String p){this.position=p;}
    public void setID(String id){this.id=id;}
    public void setStats(List<Stats> stats){this.stats=stats;}

    /**
     * Adds the given stat line to the given skater.
     * @param skater The skater the stats are being added to.
     * @param stats The stats being added.
     */
    public static void addStats(Skater skater, Stats stats){skater.stats.add(stats);}
  
    /**
     * Validates that the given name only contains useable characters.
     * Used to test names for skaters. Only displays an error message to command line.
     * User will need to update the names csv files manually if an error is found.
     * @param name The name being tested.
     */
    public static void validateName(String name){
        
        char[] charArray = name.toCharArray();

        for(char c : charArray){

            if(!Character.isDigit(c) && !Character.isLetter(c) && c != (' ') && c != ('.') && c != ('-') && c != ('\'')){
                
                System.out.println(name + " contains an invalid character.");
            }
        }
    }

    /**
     * Creates a copy of the given skater.
     * @param base The skater being copied from.
     * @return A copy of the base skater.
     */
    public static Skater duplicateSkater(Skater base){
        Skater copy = new Skater();
        copy.name = base.name;
        copy.position = base.position;
        copy.id = base.id;
        copy.stats = base.stats;
        return copy;
    }

    /**
     * Shortens a players name for display purposes.
     * @param fullName The full name of the player.
     * @return A combination with the initial of the player's first name and their complete last name.
     */
    public static String shortenName(String fullName){  
        String[] name = fullName.split(" ", 2);
        String adjName = fullName.charAt(0) + ". " + name[1];
        return adjName;
    }

    /**
     * Returns the stat line for the given skater + season combination.
     * @param skater The skater being evaluated.
     * @param season The season the skater is being evaluated for.
     * @return The stats the skater had for the given season.
     */
    public static Stats getSeasonStats(Skater skater, Season season){

        for(Stats stats : skater.stats){
            if(stats.getSeason() == season){
                return stats;
            }
        }

        System.out.println("Error in Skater.getSeasonStats");
        return null;
    }

    /**
     * Expands position abbreviations.
     * i.e. C = Center
     * @param position The abbreviated position string.
     * @return The expanded position string
     */
    public static String expandPositionName(String position){
        switch(position){
            case("F"):
                return "Forward";
            case("C"):
                return "Center";
            case("W"):
                return "Wing";
            case("LW"):
                return "Left Wing";
            case("RW"):
                return "Right Wing";
            case("D"):
                return "Defense";
            default:
                System.out.println("Error in Skater.expandPositionName -- Invalid position: " + position);
                return null;
        }
    }
}