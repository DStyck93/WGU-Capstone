package capstone.objects;

import java.util.ArrayList;
import java.util.List;

import capstone.Main;

/**
 * A season contains a start year, end year, number of games played, and a list of skater stats.
 */
public class Season {
    
    private int startYear;      // YYYY
    private int endYear;        // YYYY
    private int numGames;       // Number of games played in the season
    private List<Stats> stats;  // Skater stats

    // Constructors
    public Season(int startYear, int endYear){
        this.startYear = startYear;
        this.endYear = endYear;
        this.stats = new ArrayList<>();
    }
    public Season(String startYear, String endYear){
        this.startYear = Integer.parseInt(startYear);
        this.endYear = Integer.parseInt(endYear);
        this.stats = new ArrayList<>();
    }
    public Season(){
        this.startYear = -1;
        this.endYear = -1;
        this.stats = new ArrayList<>();
    }

    // Getters
    public int getStartYear(){return startYear;}
    public int getEndYear(){return endYear;}
    public int getNumGames(){return numGames;}
    public List<Stats> getStats(){return stats;}
    
    // Setters
    public void setStartYear(int year){this.startYear = year;}
    public void setEndYear(int year){this.endYear = year;}
    public void setNumGames(int numGames){this.numGames=numGames;}
    public void setStats(List<Stats> stats){this.stats = stats;}

    /**
     * Adds the given stat line to the current season's list of stats.
     * @param stat The stat line being added.
     */
    public void addStats(Stats stat){stats.add(stat);}

    /**
     * Creates a copy of the given season.
     * @param base The season being copied.
     * @return A copy of the base season.
     */
    public static Season duplicateSeason(Season base){

        Season copy = new Season();

        copy.startYear = base.startYear;
        copy.endYear = base.endYear;
        copy.stats = base.stats;

        return copy;
    }

    /**
     * Converts the current season into a string.
     * @return YYYY - YYYY
     */
    public String getString(){
        String startY = String.valueOf(startYear);
        String endY = String.valueOf(endYear);
        return startY + '-' + endY;
    }

    /**
     * Returns the season which started in the given year.
     * @param startYear The year being analyzed.
     * @return The season which started in the given year.
     */
    public static Season getSeason(int startYear){
        for(Season season : Main.seasons){
            if(season.startYear == startYear){
                return season;
            }
        }
        System.out.println("Error in Season.getSeason(startYear). Start year: " + startYear);
        return null;
    }
}
