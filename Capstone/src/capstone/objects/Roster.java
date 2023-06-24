package capstone.objects;

import java.util.ArrayList;
import java.util.List;

import capstone.Main;

/**
 * Each roster correlates to a unique combination of a team and a season along with a list of skaters.
 * A roster for the Chicago Blackhawks from the 2021-2022 season contains the players who were on their team that season.
 * Rosters are from February 1st of the given year.
 */
public class Roster {
    
    private Team team;  private Season season;  private List<Skater> skaters;   private int avgEPV;

    // Constructors
    public Roster(){
        this.team = null;
        this.season = null;
        this.skaters = new ArrayList<>();
        this.avgEPV = -1;
    }

    public Roster(Team team, Season season){
        this.team = team;
        this.season = season;
        this.skaters = new ArrayList<>();
        this.avgEPV = -1;
    }

    public Roster(Team team, Season season, List<Skater> skaters){
        this.team = team;
        this.season = season;
        this.skaters = skaters;
        this.avgEPV = -1;
    }

    //Getters
    public Team getTeam(){return team;}
    public Season getSeason(){return season;}
    public List<Skater> getSkaters(){return skaters;}
    public int getAvgEPV(){return avgEPV;}

    //Setters
    public void setTeam(Team team){this.team = team;}
    public void setSeason(Season season){this.season = season;}
    public void setSkaters(List<Skater> skaters){this.skaters = skaters;}
    public void setAvgEPV(int avgEPV){this.avgEPV = avgEPV;}

    /**
     * Calculates the average estimated player value for the current roster
     * @return The average EPV of the current roster.
     */
    private int calculateAvgEPV(){
         
        int avgEPV = -1;

        // Get list of skater stats from the given season/team combination
        List<Stats> skaterStats = new ArrayList<>();
        for(Skater skater : this.skaters){
            for(Stats statLine : skater.getStats()){
                if(statLine.getSeason() == season && statLine.getEPV() > 0){skaterStats.add(statLine);}
            }
        }

        // Calculate average EPV
        int totalEPV = 0;
        for(Stats statLine : skaterStats){totalEPV += statLine.getEPV();}
        avgEPV = (int)(Math.round((double)totalEPV / (double)skaterStats.size()));

        return avgEPV;
    } 
    
    /**
     * Adds a skater to the current roster's list of skaters
     * @param skater The skater being added
     */
    public void addSkater(Skater skater){skaters.add(skater);}

    /**
     * Calls the calculateAvgEPV() function for every roster in the system and stores their values in the roster object.
     */
    public static void setAvgEPVs(){
        for(Team team : Main.teams){
            for(Roster roster : team.getRosters()){
                roster.setAvgEPV(roster.calculateAvgEPV());
            }
        }
    }
}
