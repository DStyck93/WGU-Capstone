package capstone.objects;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import capstone.Main;

/**
 * ALL STATS ARE PROVIDED BY HOCKEY-REFERENCE.COM
 * Each stat line consists of a unique combination of a skater and a season.
 * Raw numbers were downloaded from hockey-reference.com.
 * Each stat line also contains an EPV (Estimated Player Value) that is calculated using an algorithm designed by this program's developer.
 */
public class Stats {
    
    //region Private Variables
    private Season season;
    private Skater skater;
    private String skaterName;
    private String skaterPosition;
    private int gamesPlayed;     
    private int goals;          
    private int assists;   
    private int points;       
    private int plusMinus;           
    private int penaltyMins;           
    private int shots;         
    private int minsPlayed;         
    private int blocks;           
    private int hits;         
    private int faceoffWins; 
    private int faceoffLosses;
    private int faceoffsTaken;
    private int faceoffPercentage;
    private int epv; // Estimated Player Value
    //endregion

    public static final int MIN_RATING = 1;   // Min rating any stat value can have. 
    public static final int MAX_RATING = 100; // Max rating any stat value can have.

    // Constructors
    public Stats(Season season, Skater skater, String name, String position, int gamesPlayed, int goals, int assists, int plusMinus, int penaltyMins, int shots, int minsPlayed, 
    int blocks, int hits, int faceoffWins, int faceoffLosses){
        this.season=season;
        this.skater=skater;
        this.skaterName=name;
        this.skaterPosition=position;
        this.gamesPlayed=gamesPlayed;
        this.goals=goals;
        this.assists=assists;
        this.points=calculatePoints(goals, assists);
        this.plusMinus=plusMinus;
        this.penaltyMins=penaltyMins;
        this.shots=shots;
        this.minsPlayed=minsPlayed;
        this.blocks=blocks;
        this.hits=hits;
        this.faceoffWins=faceoffWins;
        this.faceoffLosses=faceoffLosses;
        this.faceoffsTaken = faceoffWins + faceoffLosses;
        this.faceoffPercentage=calculateFaceoffPercentage(faceoffWins, faceoffLosses);
        this.epv=-1;
    }
    public Stats(){
        this.season=null;
        this.skater=null;
        this.gamesPlayed=-1;
        this.goals=-1;
        this.assists=-1;
        this.plusMinus=-999;
        this.penaltyMins=-1;
        this.shots=-1;
        this.minsPlayed=-1;
        this.blocks=-1;
        this.hits=-1;
        this.faceoffWins=-1;
        this.faceoffLosses=-1;
        this.epv=-1;
    }

    //region Getters
    public Season getSeason(){return season;}
    public Skater getSkater(){return skater;}
    public String getSkaterName(){return skaterName;}
    public String getSkaterPosition(){return skaterPosition;}
    public int getGamesPlayed(){return gamesPlayed;}
    public int getGoals(){return goals;}
    public int getAssists(){return assists;}
    public int getPoints(){return points;}
    public int getPlusMinus(){return plusMinus;}
    public int getPenaltyMins(){return penaltyMins;}
    public int getShots(){return shots;}
    public int getMinsPlayed(){return minsPlayed;}
    public int getBlocks(){return blocks;}
    public int getHits(){return hits;}
    public int getFaceoffWins(){return faceoffWins;}
    public int getFaceoffLosses(){return faceoffLosses;}
    public int getFaceoffPercentage(){return faceoffPercentage;}
    public int getFaceoffsTaken(){return faceoffsTaken;}
    public int getEPV(){return epv;}
    //endregion

    //region Setters
    public void setSeason(Season season){this.season=season;}
    public void setSkater(Skater skater){this.skater=skater;}
    public void setSkaterName(String skaterName){this.skaterName=skaterName;}
    public void setSkaterPosition(String skaterPosition){this.skaterPosition=skaterPosition;}
    public void setGamesPlayed(int gamesPlayed){this.gamesPlayed=gamesPlayed;}
    public void setGoals(int goals){this.goals=goals;}
    public void setAssists(int assists){this.assists=assists;}
    public void setPoints(int points){this.points=points;}
    public void setPlusMinus(int plusMinus){this.plusMinus=plusMinus;}
    public void setPenaltyMins(int penaltyMins){this.penaltyMins=penaltyMins;}
    public void setShots(int shots){this.shots=shots;}
    public void setMinsPlayed(int minsPlayed){this.minsPlayed=minsPlayed;}
    public void setBlocks(int blocks){this.blocks=blocks;}
    public void setHits(int hits){this.hits=hits;}
    public void setFaceoffWins(int faceoffWins){this.faceoffWins=faceoffWins;}
    public void setFaceoffLosses(int faceoffLosses){this.faceoffLosses=faceoffLosses;}
    public void setFaceoffPercentage(int faceoffPercentage){this.faceoffPercentage=faceoffPercentage;}
    public void setFaceoffsTaken(int faceoffsTaken){this.faceoffsTaken=faceoffsTaken;}
    public void setEPV(int epv){this.epv=epv;}
    //endregion

    //region Private Functions
    /**
     * Calculates a faceoff percentage.
     * @param faceoffWins The number of faceoffs won.
     * @param faceoffLosses The number of faceoffs lost.
     * @return The percentage of faceoff wins.
     */
    private int calculateFaceoffPercentage(int faceoffWins, int faceoffLosses){

        double fow = (double)faceoffWins;
        double fol = (double)faceoffLosses;

        double faceoffsTaken = fow + fol;

        double faceoffPercentage = 100 * (fow / faceoffsTaken);

        return (int)Math.round(faceoffPercentage);
    }

    /**
     * Adds a skaters goal and assist numbers to get their point total.
     * @param goals The number of goals scored.
     * @param assists The number of assists the skater had.
     * @return The total points the skater had for the given season.
     */
    private int calculatePoints(int goals, int assists){
        return goals + assists;
    }

    /**
     * Calculates a skater's EPV for the given stats
     * @param statLine The stats being evaluated.
     * @return An EPV based on the stats given.
     */
    private static int calculateEPV(Stats statLine){

        if(statLine.gamesPlayed < Main.MIN_GAMES){
            return 0;
        }

        // Weights
        double goalWeight; double assistWeight; double plusMinusWeight; double penaltyMinWeight; double shotWeight; double blockWeight; double hitWeight;

        // Defense Weighting System
        if(statLine.getSkaterPosition().equals("D")){
            goalWeight = 2.0;
            assistWeight = 1.5;
            plusMinusWeight = 1.0;
            penaltyMinWeight = 0.25;
            shotWeight = 0.5;
            blockWeight = 1.5;
            hitWeight = 1.5;
        }

        // Forward Weighting System
        else{
            goalWeight = 2.0;
            assistWeight = 1.5;
            plusMinusWeight = 0.5;
            penaltyMinWeight = 0.25;
            shotWeight = 1.0;
            blockWeight = 0.5;
            hitWeight = 0.5;
        }

        double numWeights = goalWeight + assistWeight + plusMinusWeight + penaltyMinWeight + shotWeight + blockWeight + hitWeight;

        // Ratings
        double goalRating = (double)calculateStatRating(statLine, "Goals");
        double assistRating = (double)calculateStatRating(statLine, "Assists");
        double plusMinusRating = (double)calculateStatRating(statLine, "+/-");
        double penaltyMinRating = (double)calculateStatRating(statLine, "Penalty Minutes");
        double shotRating = (double)calculateStatRating(statLine, "Shots");
        double hitRating = (double)calculateStatRating(statLine, "Hits");
        double blockRating = (double)calculateStatRating(statLine, "Blocks");

        // Rating * Weight
        double adjGoals = goalRating * goalWeight;
        double adjAssists = assistRating * assistWeight;
        double adjPlusMinus = plusMinusRating * plusMinusWeight;
        double adjPenaltyMins = penaltyMinRating * penaltyMinWeight;
        double adjShots = shotRating * shotWeight;
        double adjBlocks = blockRating * blockWeight;
        double adjHits = hitRating * blockWeight;

        // EPV
        double epv = (adjGoals + adjAssists + adjPlusMinus + adjPenaltyMins + adjShots + adjBlocks + adjHits) / numWeights;
        if(epv > (double)MAX_RATING){epv = (double)MAX_RATING;}
        if(epv < (double)MIN_RATING){epv = (double)MIN_RATING;}
        return (int)Math.round(epv);
    }

    /**
     * Makes a copy of the given stats.
     * @param copy The stats object being copied to.
     * @param base The stats object being copied from.
     */
    private static void copyStats(Stats copy, Stats base){
        copy.season = base.season;
        copy.skater = base.skater;
        copy.skaterName = base.skaterName;
        copy.skaterPosition = base.skaterPosition;
        copy.gamesPlayed = base.gamesPlayed;
        copy.goals = base.goals;
        copy.assists = base.assists;
        copy.plusMinus = base.plusMinus;  
        copy.penaltyMins = base.penaltyMins;
        copy.shots = base.shots;
        copy.minsPlayed = base.minsPlayed;
        copy.blocks = base.blocks;
        copy.hits = base.hits;
        copy.faceoffWins = base.faceoffWins;
        copy.faceoffLosses = base.faceoffLosses;
        copy.faceoffPercentage = base.faceoffPercentage;
        copy.epv = base.epv;         
    }
      
    /**
     * Get's the worst plus minus for the given season.
     * The plus minus is adjusted based on how many minutes were played.
     * @param season The season 
     * @return
     */
    private static double getAdjMinPlusMinus(Season season){

        double adjMinPlusMinus = 0.0;   // Adjusted minimum +/-

        for(Stats stat : season.getStats()){

            if(stat.gamesPlayed >= Main.MIN_GAMES){
                double adjPlusMinus = (double)adjustStat(stat.plusMinus, stat.minsPlayed);

                if(adjPlusMinus < adjMinPlusMinus){
                    adjMinPlusMinus = adjPlusMinus; 
                }
            }
        }

        return adjMinPlusMinus;
    }

    /**
     * Adjust's a given stat based on the minutes played.
     * It is calculated as stat-value / 20 minutes * the number of games played in a full season.
     * @param stat The stat being adjusted.
     * @param minsPlayed The number of minutes it took the skater to obtain the stat number.
     * @return An adjusted stat based on time played.
     */
    private static int adjustStat(int stat, int minsPlayed){

        double maxMins = (double)Main.MAX_GAMES * (double)Main.TOP_MINS;

        double s = (double)stat;
        double min = (double)minsPlayed;

        double adjStat = (s / min) * maxMins;

        return (int)Math.round(adjStat);
    }
    //endregion

    //region Public Functions
    /**
     * Prints a stat line for the given player and season combination.
     * Used only for development testing.
     * @param playerName The name of the skater being evaluated.
     * @param seasonStartYear The year the season being evaluated started.
     */
    public static void printStatLine(String playerName, int seasonStartYear){
        
        Season seasonOfStats = new Season();
        for(Season season : Main.seasons){
            if(season.getStartYear() == seasonStartYear){
                seasonOfStats = season;
                break;
            }
        }

        Stats statsToPrint = new Stats();
        for(Stats statLine : seasonOfStats.getStats()){
            if(statLine.skaterName.equals(playerName)){
                statsToPrint = statLine;
                break;
            }
        }

        System.out.println("\n**********");
        System.out.println(statsToPrint.skaterName + " (" + seasonOfStats.getStartYear() + '-' + seasonOfStats.getEndYear() + ")");
        System.out.println("Goals: " + statsToPrint.goals + " (" + calculateStatRating(statsToPrint, "Goals") + ')');
        System.out.println("Assists: " + statsToPrint.assists + " (" + calculateStatRating(statsToPrint, "Assists") + ')');
        System.out.println("+/-: " + statsToPrint.plusMinus + " (" + calculateStatRating(statsToPrint, "+/-") + ')');
        System.out.println("Hits: " + statsToPrint.goals + " (" + calculateStatRating(statsToPrint, "Hits") + ')');
        System.out.println("Blocks: " + statsToPrint.blocks + " (" + calculateStatRating(statsToPrint, "Blocks") + ')');
        System.out.println("**********\n");
    }

    /**
     * Sets the top stat values for each season in the program. 
     * Called when the program is booted.
     */
    public static void setTopStats(){

        for(Season season : Main.seasons){

            boolean isEligible;

            Stats topFwdStats = new Stats();
            topFwdStats.season = season;
            topFwdStats.skaterPosition = "F";

            Stats topDefStats = new Stats();
            topDefStats.season = season;
            topDefStats.skaterPosition = "D";
            
            for(Stats stats : season.getStats()){

                if(stats.gamesPlayed < Main.MIN_GAMES){isEligible = false;}
                else{isEligible = true;}

                // Adjust Stats
                Stats adjStats = adjustStatLine(stats);

                // Defense
                if(stats.getSkaterPosition().equals("D") && isEligible){
                    
                    if(adjStats.goals > topDefStats.goals){
                        topDefStats.goals = adjStats.goals;
                    }

                    if(adjStats.assists > topDefStats.assists){
                        topDefStats.assists = adjStats.assists;
                    }

                    if(adjStats.plusMinus > topDefStats.plusMinus){
                        topDefStats.plusMinus = adjStats.plusMinus;
                    }

                    if(adjStats.penaltyMins > topDefStats.penaltyMins){
                        topDefStats.penaltyMins = adjStats.penaltyMins;
                    }

                    if(adjStats.shots > topDefStats.shots){
                        topDefStats.shots = adjStats.shots;
                    }

                    if(adjStats.hits > topDefStats.hits){
                        topDefStats.hits = adjStats.hits;
                    }

                    if(adjStats.blocks > topDefStats.blocks){
                        topDefStats.blocks = adjStats.blocks;
                    }

                    if(adjStats.faceoffsTaken > 100 && adjStats.faceoffPercentage > topDefStats.faceoffPercentage){
                        topDefStats.faceoffPercentage = adjStats.faceoffPercentage;
                    }
                }

                // Forward
                else if(isEligible)
                {

                    if(adjStats.goals > topFwdStats.goals){
                        topFwdStats.goals = adjStats.goals;
                    }

                    if(adjStats.assists > topFwdStats.assists){
                        topFwdStats.assists = adjStats.assists;
                    }

                    if(adjStats.plusMinus > topFwdStats.plusMinus){
                        topFwdStats.plusMinus = adjStats.plusMinus;
                    }

                    if(adjStats.penaltyMins > topFwdStats.penaltyMins){
                        topFwdStats.penaltyMins = adjStats.penaltyMins;
                    }

                    if(adjStats.shots > topFwdStats.shots){
                        topFwdStats.shots = adjStats.shots;
                    }

                    if(adjStats.hits > topFwdStats.hits){
                        topFwdStats.hits = adjStats.hits;
                    }

                    if(adjStats.blocks > topFwdStats.blocks){
                        topFwdStats.blocks = adjStats.blocks;
                    }

                    if(adjStats.faceoffsTaken > 100 && adjStats.faceoffPercentage > topFwdStats.faceoffPercentage){
                        topFwdStats.faceoffPercentage = adjStats.faceoffPercentage;
                    }

                    }
            }

            Main.topStats.add(topFwdStats);
            Main.topStats.add(topDefStats);
        }
    }

    /**
     * Sets the EPV for each stat line in the program.
     * Called when the the program is booted.
     */
    public static void setEPVs(){  
        for(Season season : Main.seasons){
            for(Stats statLine : season.getStats()){
                statLine.epv = calculateEPV(statLine);
            }
        }
    }
      
    /**
     * Creates a list of predicted skater stats for the upcoming season and loads them into the program.
     */
    public static void setFuturePredictions(){

        Main.nextSeason = new Season(Main.previousSeason.getEndYear(), Main.previousSeason.getEndYear() + 1);
        Main.seasons.add(Main.nextSeason);

        // Loop through all stat lines from the previous season
        for(Stats statLine : Main.previousSeason.getStats()){

            if(statLine.gamesPlayed >= Main.MIN_GAMES){

                // Get the skater
                Skater skater = statLine.getSkater();
            
                // Get historic stats for the skater
                Stats lastSeasonStats = duplicateStats(statLine);
                List<Stats> previousStats = new ArrayList<>();
                for(Stats stats : skater.getStats()){
                    previousStats.add(duplicateStats(stats));
                }

                // Adjust stats
                if(lastSeasonStats.faceoffsTaken < 100){
                    lastSeasonStats.faceoffPercentage = 0;
                }
                lastSeasonStats = Stats.adjustStatLine(lastSeasonStats);
                for(Stats s : previousStats){
                    if(s.faceoffsTaken < 100){
                        s.faceoffPercentage = 0;
                    }
                    s = Stats.adjustStatLine(s);
                }

                // Get career average
                Stats careerAverage = Stats.duplicateStats(statLine);
                int totalGoals = 0; int totalAssists = 0; int totalPlusMinus = 0; int totalPenaltyMins = 0; int totalShots = 0; int totalHits = 0; int totalBlocks = 0;
                int totalFaceoffPercentage = 0;
                for(Stats s : previousStats){
                    totalGoals += s.getGoals();
                    totalAssists += s.getAssists();
                    totalPlusMinus += s.getPlusMinus();
                    totalPenaltyMins += s.getPenaltyMins();
                    totalShots += s.getShots();
                    totalHits += s.getHits();
                    totalBlocks += s.getBlocks();
                    totalFaceoffPercentage += s.getFaceoffPercentage();
                }
                careerAverage.setGoals((int)Math.round((double)totalGoals / (double)previousStats.size()));
                careerAverage.setAssists((int)Math.round((double)totalAssists / (double)previousStats.size()));
                careerAverage.setPlusMinus((int)Math.round((double)totalPlusMinus / (double)previousStats.size()));
                careerAverage.setPenaltyMins((int)Math.round((double)totalPenaltyMins / (double)previousStats.size()));
                careerAverage.setShots((int)Math.round((double)totalShots / (double)previousStats.size()));
                careerAverage.setHits((int)Math.round((double)totalHits / (double)previousStats.size()));
                careerAverage.setBlocks((int)Math.round((double)totalBlocks / (double)previousStats.size()));
                careerAverage.setFaceoffPercentage((int)Math.round((double)totalFaceoffPercentage / (double)previousStats.size()));

                // Initialize Stat object for the predictions
                Stats prediction = Stats.duplicateStats(statLine);
                prediction.setMinsPlayed(Main.MAX_GAMES * Main.TOP_MINS);
                prediction.setSeason(Main.nextSeason);

                // Make predictions based off historic stats -- ((Last Season + Career Avg) / 2
                prediction.setGoals(((lastSeasonStats.getGoals()) + careerAverage.getGoals()) / 2);
                prediction.setAssists(((lastSeasonStats.getAssists()) + careerAverage.getAssists()) / 2);
                prediction.setPoints(prediction.calculatePoints(prediction.goals, prediction.assists));
                prediction.setPlusMinus(((lastSeasonStats.getPlusMinus()) + careerAverage.getPlusMinus()) / 2);
                prediction.setPenaltyMins(((lastSeasonStats.getPenaltyMins()) + careerAverage.getPenaltyMins()) / 2);
                prediction.setShots(((lastSeasonStats.getShots()) + careerAverage.getShots()) / 2);
                prediction.setHits(((lastSeasonStats.getHits()) + careerAverage.getHits()) / 2);
                prediction.setBlocks(((lastSeasonStats.getBlocks()) + careerAverage.getBlocks()) / 2);
                prediction.setFaceoffPercentage(((lastSeasonStats.getFaceoffPercentage()) + careerAverage.getFaceoffPercentage()) / 2);

                Main.nextSeason.addStats(prediction);
            }   
        }
    }
   
    /**
     * Creates of copy of the given stats.
     * @param base The stats being copied.
     * @return A copy of the given stats.
     */
    public static Stats duplicateStats(Stats base){

        Stats copy = new Stats();

        copy.season = base.season;
        copy.skater = base.skater;
        copy.skaterName = base.skaterName;
        copy.skaterPosition = base.skaterPosition;
        copy.gamesPlayed = base.gamesPlayed;
        copy.goals = base.goals;
        copy.assists = base.assists;
        copy.points = base.points;
        copy.plusMinus = base.plusMinus;
        copy.penaltyMins = base.penaltyMins;
        copy.shots = base.shots;
        copy.minsPlayed = base.minsPlayed;
        copy.blocks = base.blocks;
        copy.hits = base.hits;       
        copy.faceoffWins = base.faceoffWins;
        copy.faceoffLosses = base.faceoffLosses;
        copy.faceoffsTaken = base.faceoffsTaken;
        copy.faceoffPercentage = base.faceoffPercentage;
        copy.epv = base.epv;
        
        return copy;
    }

    /**
     * Gets the top EPVs for a given season/position combination.
     * @param season The season being evaluated.
     * @param position The position being evaluated.
     * @param numSkaters The size of the list being returned.
     * @return A list of the top skaters from the given season/position combination.
     */
    public static List<Stats> getTopEPVs(Season season, String position, int numSkaters){

        List<Stats> topEPVs = new ArrayList<>();

        for(Stats statLine : season.getStats()){

            // Test if position matches
            boolean isValid = false;
            switch(position){
                case("All"):
                    isValid = true;
                    break;
                case("F"):
                    if(statLine.skaterPosition.contains("F") || statLine.skaterPosition.contains("W") || statLine.skaterPosition.contains("C"))
                        isValid = true;
                    break;
                case("W"):
                    if(statLine.skaterPosition.contains("F") || statLine.skaterPosition.contains("W"))
                        isValid = true;
                    break;
                case("LW"):
                    if((statLine.skaterPosition.contains("LW") || statLine.skaterPosition.contains("W")) && !statLine.skaterPosition.contains("RW"))
                        isValid = true;
                case("RW"):
                    if((statLine.skaterPosition.contains("RW") || statLine.skaterPosition.contains("W")) && !statLine.skaterPosition.contains("LW"))
                        isValid = true;
                default:
                    if(statLine.skaterPosition.contains(position))
                        isValid = true;
            }

            if(isValid){

                if(topEPVs.size() < numSkaters){
                    topEPVs.add(statLine);
                }
                else{
                    if(statLine.epv > topEPVs.get(numSkaters - 1).epv){
                        topEPVs.remove(numSkaters - 1);
                        topEPVs.add(statLine);
                        topEPVs.sort(Comparator.comparing(Stats::getEPV).reversed());
                    }
                }
            }
        }

        // Handle Errors
        if(topEPVs.size() > numSkaters){
            System.out.println("Error in Stats.getTopEPVs -- Invalid list size: " + topEPVs.size());
        }

        return topEPVs;
    }
     
    /**
     * Calculates the rating of the given stat.
     * Ratings are a scale of 1-100
     * @param statLine The stat line being evaluated.
     * @param statStr The type of stat being evaluated.
     * @return The stat's rating on a scale of 1-100.
     */
    public static int calculateStatRating(Stats statLine, String statStr){

        double maxRating = 100.0;
        double minRating = 1.0;

        // Convert C, LW, RW, etc to F
        String position = statLine.skaterPosition;
        if(!position.equals("D")){
            position = "F";
        }

        // Top stats for the given position/season combination
        Stats topStats = new Stats();
        for(Stats stats : Main.topStats){
            if(stats.season == statLine.season && stats.skaterPosition.equals(position)){
                copyStats(topStats, stats);
            }
        }

        double statRating = -1;
        double adjStat = -1;
        switch(statStr){
            case("Goals"):
                adjStat = adjustStat(statLine.goals, statLine.minsPlayed);
                statRating = maxRating * (adjStat / topStats.goals);
                break;
            case("Assists"):
                adjStat = adjustStat(statLine.assists, statLine.minsPlayed);
                statRating = maxRating * (adjStat / topStats.assists);
                break;
            case("+/-"):
                adjStat = adjustStat(statLine.plusMinus, statLine.minsPlayed);
                double adjMinPlusMinus = getAdjMinPlusMinus(statLine.season);
                double adjTopPlusMinus = topStats.plusMinus - adjMinPlusMinus;
                double adjPlusMinus = adjStat - adjMinPlusMinus;
                statRating = maxRating * (adjPlusMinus / adjTopPlusMinus);

                break;
            case("Penalty Minutes"):
                adjStat = adjustStat(statLine.penaltyMins, statLine.minsPlayed);
                statRating = maxRating - (maxRating * adjStat / topStats.penaltyMins);
                break;
            case("Shots"):
                adjStat = adjustStat(statLine.shots, statLine.minsPlayed);
                statRating = maxRating * (adjStat / topStats.shots);
                break;
            case("Blocks"):
                adjStat = adjustStat(statLine.blocks, statLine.minsPlayed);
                statRating = maxRating * (adjStat / topStats.blocks);
                break;
            case("Hits"):
                adjStat = adjustStat(statLine.hits, statLine.minsPlayed);
                statRating = maxRating * (adjStat / topStats.hits);
                break;
            default:
                System.out.println("Error in Stats.calculateStatRating -- Invalid stat string: " + statStr);
                return -1;
        }

        // Adjust stat ratings outside bounds.
        if(statRating > maxRating){statRating = maxRating;} // Possible due to stat adjustments
        if(statRating < minRating){statRating = minRating;} // Possible for PIM due to stat adjustments

        return (int)Math.round(statRating);
    }
    
    /**
     * Returns stats the skater would've had if they played 20 minutes / night for a full season.
     * @param statLine The stats to adjust.
     * @return The adjusted stats.
     */
    public static Stats adjustStatLine(Stats statLine){

        Stats adjStatLine = duplicateStats(statLine);

        adjStatLine.goals = adjustStat(statLine.goals, statLine.minsPlayed);
        adjStatLine.assists = adjustStat(statLine.assists, statLine.minsPlayed);
        adjStatLine.plusMinus = adjustStat(statLine.plusMinus, statLine.minsPlayed);
        adjStatLine.penaltyMins = adjustStat(statLine.penaltyMins, statLine.minsPlayed);
        adjStatLine.shots = adjustStat(statLine.shots, statLine.minsPlayed);
        adjStatLine.hits = adjustStat(statLine.hits, statLine.minsPlayed);
        adjStatLine.blocks = adjustStat(statLine.blocks, statLine.minsPlayed);

        return adjStatLine;
    }

    /**
     * Determines if the given stat line is of the given position.
     * @param statLine The stat line being evaluated.
     * @param testPosition The position being tested for.
     * @return True if the stat line is from the given position.
     */
    public static boolean isFromThisPosition(Stats statLine, String testPosition){

        switch(testPosition){
            case("All"):
                return true;
            case("F"):
                if(statLine.skaterPosition.contains("F") || statLine.skaterPosition.contains("C") || statLine.skaterPosition.contains("W"))
                    return true;
                return false;
            case("C"):
                if(statLine.skaterPosition.contains("C"))
                    return true;
                return false;
            case("W"):
                if(statLine.skaterPosition.contains("W") || statLine.skaterPosition.contains("F"))
                    return true;
                return false;
            case("LW"):
                if(statLine.skaterPosition.contains("LW"))
                    return true;
                return false;
            case("RW"):
                if(statLine.skaterPosition.contains("RW"))
                    return true;   
                return false;
            case("D"):
                if(statLine.skaterPosition.contains("D"))
                    return true;
                return false;
            default:
                System.out.println("Error in Stats.isFromThisPosition -- Failed position test");
                return false;

        }
    }
    //endregion

}
