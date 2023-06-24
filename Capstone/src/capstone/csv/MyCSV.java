package capstone.csv;

import capstone.Main;
import capstone.objects.Roster;
import capstone.objects.Season;
import capstone.objects.Skater;
import capstone.objects.Stats;
import capstone.objects.Team;
import capstone.objects.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MyCSV {
    
    /**
     * Combines all of the CSV loading functions into a more compact form.
     */
    public static void loadCSVs(){
        loadSeasons();
        loadTeams();
        loadUsers();
        loadRosters();
    }

    /**
     * Load the season CSV files into the system
     */
    private static void loadSeasons(){

        // Create a list of the files in skater_stats
        File folder = new File("src/capstone/csv/seasons");
        File[] listOfFiles = folder.listFiles();

        // Loop through the list of files, read the data from each one to create Skater objects, set the previous season,
        // and create a list of what seasons have been loaded into the program.
        for(int i = 0; i < listOfFiles.length; i++){ 

            // Verify File
            if(listOfFiles[i].isFile()){  

                String fileName = listOfFiles[i].getName();                        

                // Season
                String startYear = fileName.substring(0, 4);
                String endYear = fileName.substring(5, 9);
                Season season = new Season(startYear, endYear);
                Main.seasons.add(season);

                if(Main.previousSeason == null || Integer.parseInt(startYear) > Main.previousSeason.getStartYear()){
                    Main.previousSeason = season;
                }

                // Load the skater object from the file into the program.
                try{

                    // Open file
                    File file = new File("src/capstone/csv/seasons/" + fileName);
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    br.readLine(); // Skip 1st line
        
                    // Read each line in the file
                    String line = "";
                    String[] values;
                    while((line = br.readLine()) != null){
        
                        // Get values from line
                        values = line.split(",");
                        String name = values[0];
                        String position = values[1];
                        int gamesPlayed = Integer.parseInt(values[2]);
                        int goals = Integer.parseInt(values[3]);
                        int assists = Integer.parseInt(values[4]);
                        int plusMinus = Integer.parseInt(values[5]);
                        int penaltyMins = Integer.parseInt(values[6]);
                        int shots = Integer.parseInt(values[7]); 
                        int minsPlayed = Integer.parseInt(values[8]);
                        int blocks = Integer.parseInt(values[9]);
                        int hits = Integer.parseInt(values[10]);
                        int faceoffWins = Integer.parseInt(values[11]);
                        int faceoffLosses = Integer.parseInt(values[12]);
                        String id = values[13];

                        if(gamesPlayed > season.getNumGames()){
                            season.setNumGames(gamesPlayed);
                        }

                        Skater.validateName(name);

                        Skater skater = new Skater();

                        // Validate Center position
                        if(position.equals("C") && faceoffWins + faceoffLosses < 100){
                            position.equals("F");
                        }
                        else if(!position.contains("C") && faceoffWins + faceoffLosses > 100){
                            position += "/C";
                        }
        
                        boolean isUniqueSkater = true;
                        for(Skater s : Main.skaters){
                            if(s.getID().equals(id)){
                                isUniqueSkater = false;
                                skater = s;
                                break;
                            }
                        }

                        if(isUniqueSkater){
                            skater.setName(name);
                            skater.setPosition(position);
                            skater.setID(id);
                            Main.skaters.add(skater);
                        }

                        Stats stats = new Stats(season, skater, name, position, gamesPlayed, goals, assists, plusMinus, penaltyMins, shots, minsPlayed, blocks, hits, faceoffWins, faceoffLosses);
                        Skater.addStats(skater, stats);
                        season.addStats(stats);
                    }
                    br.close();
        
                }catch(Exception e){
                    System.out.println("Error loading " + fileName);
                }

                int currentYear = 0;
                int fileYear = Integer.parseInt(startYear);
                // Set the previous season value to update if this file is the most recent.
                if(fileYear > currentYear){
                    currentYear = fileYear;
                    Main.previousSeason = season;
                }
            }
        }
        // Sort seasons
        Main.seasons.sort(Comparator.comparing(Season::getStartYear).reversed());
    }

    /**
     * Loads the team.csv file into the program
     */
    private static void loadTeams(){

        Path filePath = Paths.get("src/capstone/csv/teams.csv");
        
        try{

            BufferedReader br = Files.newBufferedReader(filePath);
            String line = br.readLine();
            line = br.readLine(); // Skip 1st line

            // Read each line in users.csv
            while(line != null){

                // Get values from the current line
                String[] values = line.split(",");
                String city = values[0];
                String name = values[1];

                // Create the object and add it to the List
                Team team = new Team(city, name);
                Main.teams.add(team);

                // Read the next line
                line = br.readLine();
            }

        }catch(Exception e){
            System.out.println("Error reading teams.csv");
        }
    }

    /**
     * Reads the users.csv file, uses each line to create a user object, and then loads them into the program.
     */
    private static void loadUsers(){
        
        Path pathToFile = Paths.get("src/capstone/csv/users.csv");
        
        try{

            BufferedReader br = Files.newBufferedReader(pathToFile);
            String line = br.readLine();
            line = br.readLine(); // Skip 1st line

            // Read each line in users.csv
            while(line != null){
                String[] values = line.split(",");
                String username = values[0];
                String passwordHash = values[1];
                String teamName = values[2];

                // Get team based on the name
                Team team = Team.getTeamByName(teamName);

                // Create a User object with the given variables, and add it to the List
                User user = new User(username, passwordHash, team);
                Main.users.add(user);

                // Continue to next line
                line = br.readLine();
            }

        }catch(Exception e){
            System.out.println("Error reading users.csv");
        }
    }

    /**
     * Reads each file in the rosters folder, creates a roster object using the files, and then loads them into the program.
     */
    private static void loadRosters(){
        
        // Rosters Directory
        File rostersDirectory = new File("src/capstone/csv/rosters");
        
        // Roster Folders (one for each season)
        List<RosterFolder> rosterFolders = new ArrayList<>();
        for(File folder : rostersDirectory.listFiles()){
            RosterFolder rosterFolder = new RosterFolder(folder.getPath());
            rosterFolder.setSeason(Season.getSeason(Integer.parseInt(folder.getName().substring(0, 4))));
            rosterFolders.add(rosterFolder);
        }

        // Set List of files for each folder
        for(RosterFolder folder : rosterFolders){
            File directory = new File(folder.getDirectory());
            List<File> listOfFiles = new ArrayList<>();
            for(File file : directory.listFiles()){
                listOfFiles.add(file);
            }
            folder.setListOfFiles(listOfFiles);
        }

        // Add rosters to program
        for(RosterFolder folder : rosterFolders){
            for(File file : folder.getListOfFiles()){

                // Get team name
                String fileName = file.getName();
                int dotPosition = fileName.indexOf('.');
                String teamName = fileName.substring(0,dotPosition);
                if(teamName.contains("_")){
                    int underscorePosition = teamName.indexOf('_');
                    String firstString = teamName.substring(0,underscorePosition);
                    String secondString = teamName.substring(underscorePosition + 1);
                    teamName = firstString + ' ' + secondString;
                }

                // Initialize Roster
                Roster roster = new Roster();
                roster.setTeam(Team.getTeamByName(teamName));
                roster.setSeason(folder.getSeason());

                // Create a roster object using the information in the file
                try{

                    // Open file
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    br.readLine(); br.readLine(); // Skip first 2 lines
        
                    // Read each line in the file
                    String line = "";
                    String[] values;
                    while((line = br.readLine()) != null){
        
                        // Get values from line -- Leave in commented out sections for future updates
                        values = line.split(",");
                        //int number = Integer.parseInt(values[0]);
                        //String name = values[1];
                        //String position = values[2];
                        //String height = values[3];
                        //int weight = Integer.parseInt(values[4]);
                        //String shoots = values[5];
                        //String experience = values[6];
                        //String birthday = values[7];
                        String id = values[8];

                        for(Skater skater : Main.skaters){
                            if(skater.getID().equals(id)){
                                roster.addSkater(skater);
                            }
                        }
                    }
                    br.close();

                    Team.getTeamByName(teamName).addRoster(roster);
        
                }catch(Exception e){
                    System.out.println("Error loading " + fileName);
                }

            }
        }
    }
}
