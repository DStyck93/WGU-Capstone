package capstone;   

import capstone.csv.MyCSV;
import capstone.objects.Roster;
import capstone.objects.Season;
import capstone.objects.Skater;
import capstone.objects.Stats;
import capstone.objects.Team;
import capstone.objects.User;
import capstone.scenes.Login;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


/**
 * The main class launches the program and contains public variables used throughout the project.
 */
public class Main extends Application{  

    public static Stage stage;
    public static User currentUser;
    public static Season previousSeason;
    public static Season nextSeason;
    public static List<User> users = new ArrayList<>();
    public static List<Team> teams = new ArrayList<>();
    public static List<Season> seasons = new ArrayList<>();
    public static List<Skater> skaters = new ArrayList<>();
    public static List<Stats> topStats = new ArrayList<>();

    public static final int MIN_GAMES = 20; // Minimum of games a skater must play in a season to be eligible for ratings.
    public static final int MAX_GAMES = 82; // Number of games played in a standard regular season.
    public static final int TOP_MINS = 20; // Number of minutes a team's top skaters get.

    public static final double DEFAULT_SCENE_WIDTH = 1850.0;
    public static final double DEFAULT_SCENE_HEIGHT = 900.0;

    public static void main (String[] args)  
    {  
        launch(args);  
    } 

    @Override  
    public void start(Stage primaryStage) throws Exception{   

        // Load Data, calculate EPVs, and get future predictions
        System.out.println("Loading...");
        MyCSV.loadCSVs();
        Stats.setFuturePredictions();
        Stats.setTopStats();
        Stats.setEPVs();
        Roster.setAvgEPVs();
        System.out.println("Done loading.");

        // Launch Program
        stage = new Stage();
        stage.setTitle("Capstone Project v1.1"); 
        Login.loadScene(); 
        stage.show();
    }  
}  