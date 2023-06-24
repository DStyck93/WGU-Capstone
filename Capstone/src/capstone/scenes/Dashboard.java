package capstone.scenes;

import capstone.Main;
import capstone.objects.Roster;
import capstone.objects.Season;
import capstone.objects.Skater;
import capstone.objects.Stats;
import capstone.objects.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 * Contains everything used to design the dashboard scene
 * The dashboard is the programs main interface and will be loaded after login.
 */
public class Dashboard {

    // Combo Boxes
    private static ComboBox<String> seasonComboBox;
    private static ComboBox<String> positionComboBox;

    // Table
    private static Label tableTitle;
    private static TableView<Stats> table;
    private static TableColumn<Stats, Integer> epvCol;

    // Bar Graph
    private static BarChart<String, Number> barChart;
    private static XYChart.Series<String, Number> epvSeries;
    private static XYChart.Series<String, Number> goalSeries;
    private static XYChart.Series<String, Number> assistSeries;
    private static XYChart.Series<String, Number> plusMinusSeries;
    private static XYChart.Series<String, Number> hitSeries; 
    private static XYChart.Series<String, Number> blockSeries;

    // Line Chart
    private static LineChart<String, Number> lineChart;
    private static ComboBox<String> teamComboBox;
    private static XYChart.Series<String, Number> teamSeries;
    
    /**
     * Loads the dashboard's scene.
     */
    public static void loadScene(){
        if(Main.stage.isMaximized()){
            Main.stage.setMaximized(false);
            Main.stage.setScene(buildScene(Main.DEFAULT_SCENE_WIDTH, Main.DEFAULT_SCENE_HEIGHT));
            Main.stage.setMaximized(true);
        }else{
            Main.stage.setScene(buildScene(Main.stage.getScene().getWidth(), Main.stage.getScene().getHeight()));
        }
    }

    /**
     * Creates the dashboard scene.
     * @return The dashboard scene
     */
    private static Scene buildScene(double sceneWidth, double sceneHeight){

        //region Top
        MenuBar menu = MyMenuBar.getMyMenuBar();

        // Header
        Label header = new Label("Welcome " + Main.currentUser.getUsername());
        header.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        header.setPadding(new Insets(20));

        // VBox
        VBox topVBox = new VBox(menu, header);
        topVBox.setAlignment(Pos.CENTER);
        topVBox.setSpacing(10);    
        //endregion
        
        //region Left
        // Button Formatting
        Font buttonFont = Font.font("Arial", FontWeight.BOLD, 20);
        int buttonWidth = 200;
        int buttonHeight = 100;

        // Scene Buttons
        Button skaterPredictionsButton = new Button("Skater Predictions");
        Button comparePlayersButton = new Button("Compare Players");
        Button teamViewButton = new Button("Team View");

        // Button Handlers
        skaterPredictionsButton.setOnAction(event->{SkaterPredictions.loadScene();});
        comparePlayersButton.setOnAction(event->{ComparePlayers.loadScene();;});
        teamViewButton.setOnAction(event->{TeamView.loadScene();});

        Button sceneButtons[] = {skaterPredictionsButton, comparePlayersButton, teamViewButton};
        for(Button b : sceneButtons){
            b.setPrefSize(buttonWidth, buttonHeight);
            b.setFont(buttonFont);
            b.setWrapText(true);
            b.setTextAlignment(TextAlignment.CENTER);
        }
        
        // Button Box
        VBox leftBox = new VBox(skaterPredictionsButton, comparePlayersButton, teamViewButton);
        leftBox.setAlignment(Pos.TOP_CENTER);
        leftBox.setSpacing(20);
        leftBox.setPadding(new Insets(20));
        //endregion
        
        //region Center

        // Season Filter
        List<String> seasons = new ArrayList<>();
        for(Season season : Main.seasons){
            if(season != Main.nextSeason){
                seasons.add(season.getString());
            }
        }
        Label seasonDropdownLabel = new Label("Filter by Season: ");
        seasonComboBox = new ComboBox<>(FXCollections.observableArrayList(seasons));
        seasonComboBox.setValue(Main.previousSeason.getString());
        HBox seasonDropdownBox = new HBox(seasonDropdownLabel, seasonComboBox);
        seasonDropdownBox.setSpacing(10);

        // Position Filter
        Label positionDropdownLabel = new Label("Filter by position: ");
        String[] positions = {"All", "F", "C", "W", "LW", "RW", "D"};
        positionComboBox = new ComboBox<>(FXCollections.observableArrayList(positions));
        positionComboBox.setValue("All");
        HBox positionHBox = new HBox(positionDropdownLabel, positionComboBox);
        positionHBox.setSpacing(10);

        // Update Button
        Button updateButton = new Button("Update");
        updateButton.setOnAction(event->{updateButtonHandler();});

        // Filter VBox
        VBox filterBox = new VBox(seasonDropdownBox, positionHBox, updateButton);
        filterBox.setSpacing(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        filterBox.setMinWidth(220);

        // Table Title
        tableTitle = new Label("Skater Stats for the " + Main.previousSeason.getString() + " Season");
        tableTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        HBox tableTitleBox = new HBox(tableTitle);
        tableTitleBox.setAlignment(Pos.CENTER);

        // Create Table
        table = createTable();

        // Source Label
        Label sourceLabel = new Label("Stats are provided by hockey-reference.com");
        Label minGameLabel = new Label("A minium of " + Main.MIN_GAMES + " games played is required to be eligible for an EPV rating.");

        // Table V-Box
        VBox tableBox = new VBox(tableTitleBox, table, sourceLabel, minGameLabel);
        tableBox.setSpacing(10);
        tableBox.setAlignment(Pos.CENTER);
        
        // Center HBox
        HBox centerBox = new HBox(filterBox, tableBox);        
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(20);
        centerBox.setPadding(new Insets(10));
        //endregion
        
        //region Right
        //region BarChart
        buildBarChart();

        // Initialize Check Boxes
        CheckBox goalsCheck = new CheckBox("Goals");
        CheckBox assistsCheck = new CheckBox("Assists");
        CheckBox plusMinusCheck = new CheckBox("+/-");
        CheckBox hitsCheck = new CheckBox("Hits");
        CheckBox blocksCheck = new CheckBox("Blocks");

        // Select Check Boxes
        goalsCheck.setSelected(true);
        assistsCheck.setSelected(true);
        plusMinusCheck.setSelected(true);
        hitsCheck.setSelected(true);
        blocksCheck.setSelected(true);

        // Check Box Handlers
        goalsCheck.setOnAction(e->{goalsCheckHandler();});
        assistsCheck.setOnAction(e->{assistsCheckHandler();});
        plusMinusCheck.setOnAction(e->{plusMinusCheckHandler();});
        hitsCheck.setOnAction(e->{hitsCheckHandler();});
        blocksCheck.setOnAction(e->{blocksCheckHandler();});

        // Horizontal Box for Check Boxes
        HBox checkBox = new HBox(goalsCheck, assistsCheck, plusMinusCheck, hitsCheck, blocksCheck);
        checkBox.setSpacing(20);
        checkBox.setAlignment(Pos.CENTER);

        // Bar Chart Box
        VBox barChartBox = new VBox(barChart, checkBox);
        barChartBox.setAlignment(Pos.CENTER);
        //endregion

        // Line Chart
        buildLineChart();

        // Team Names
        List<String> teamNames = new ArrayList<>();
        for(Team team : Main.teams){
            teamNames.add(team.getName());
        }
        Collections.sort(teamNames);

        // Team Combo Box
        teamComboBox = new ComboBox<>(FXCollections.observableArrayList(teamNames));
        teamComboBox.setValue(Main.currentUser.getTeam().getName());
        teamComboBox.setOnAction(e->{teamComboBoxHandler();});

        // Line Chart Box
        VBox lineChartBox = new VBox(lineChart, teamComboBox);
        lineChartBox.setAlignment(Pos.CENTER);

        // Vertical Box for right side of border pane
        VBox rightBox = new VBox(barChartBox, lineChartBox);
        rightBox.setPadding(new Insets(20));
        rightBox.setSpacing(20);
        rightBox.setAlignment(Pos.CENTER);
        //endregion

        // Border Pane
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topVBox);
        borderPane.setLeft(leftBox);
        borderPane.setCenter(centerBox);
        borderPane.setRight(rightBox);

        // Scene
        return new Scene(borderPane, sceneWidth, sceneHeight);
    }
 
    /**
     * Builds the table used on the dashboard's scene.
     * Contains skater stats for a given year & position combination.
     * @return A table of skater stats.
     */
    private static TableView<Stats> createTable(){

        double tipDelay = 0.5; // Seconds

        // Initialize Table
        TableView<Stats> table = new TableView<>();

        // Formatting
        table.setEditable(false);
        table.setMinWidth(680);
        table.setMaxWidth(680);
        table.setPrefHeight(800);

        // Columns
        TableColumn<Stats, String> nameCol = new TableColumn<>();
        TableColumn<Stats, String> posCol = new TableColumn<>();
        TableColumn<Stats, Integer> gpCol = new TableColumn<>();
        TableColumn<Stats, Integer> gCol = new TableColumn<>();
        TableColumn<Stats, Integer> aCol = new TableColumn<>();
        TableColumn<Stats, Integer> ptsCol = new TableColumn<>();
        TableColumn<Stats, Integer> pmCol = new TableColumn<>();
        TableColumn<Stats, Integer> pimCol = new TableColumn<>();
        TableColumn<Stats, Integer> sCol = new TableColumn<>();
        TableColumn<Stats, Integer> toiCol = new TableColumn<>();
        TableColumn<Stats, Integer> blkCol = new TableColumn<>();
        TableColumn<Stats, Integer> hitCol = new TableColumn<>();
        TableColumn<Stats, Integer> fowCol = new TableColumn<>();
        TableColumn<Stats, Integer> folCol = new TableColumn<>();
        TableColumn<Stats, Integer> fopCol = new TableColumn<>();
        epvCol = new TableColumn<>();

        // Cell Values
        nameCol.setCellValueFactory(new PropertyValueFactory<Stats, String>("skaterName"));
        posCol.setCellValueFactory(new PropertyValueFactory<Stats, String>("skaterPosition"));
        gpCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("gamesPlayed"));
        gCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("goals"));
        aCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("assists"));
        ptsCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("points"));
        pmCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("plusMinus"));
        pimCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("penaltyMins"));
        sCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("shots"));
        toiCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("minsPlayed"));
        blkCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("blocks"));
        hitCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("hits"));
        fowCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("faceoffWins"));
        folCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("faceoffLosses"));
        fopCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("faceoffPercentage"));
        epvCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("ePV"));

        // Tooltips
        Tooltip nameTip = new Tooltip("First Name + Last Name");
        Tooltip posTip = new Tooltip("Position");
        Tooltip gpTip = new Tooltip("Games Played");
        Tooltip gTip = new Tooltip("Goals");
        Tooltip aTip = new Tooltip("Assists");
        Tooltip ptsTip = new Tooltip("Points");
        Tooltip pmTip = new Tooltip("+/-");
        Tooltip pimTip = new Tooltip("Penalty Minutes");
        Tooltip sTip = new Tooltip("Shots");
        Tooltip toiTip = new Tooltip("Time on Ice (Minutes)");
        Tooltip blkTip = new Tooltip("Blocks");
        Tooltip hitTip = new Tooltip("Hits");
        Tooltip fowTip = new Tooltip("Faceoff Wins");
        Tooltip folTip = new Tooltip("Faceoff Losses");
        Tooltip fopTip = new Tooltip("Faceoff Percentage");
        Tooltip epvTip = new Tooltip("Estimated Player Value");

        // Tooltip Delays
        Tooltip toolTips[] = {nameTip, posTip, gpTip, gTip, aTip, ptsTip, pmTip, pimTip, sTip, toiTip, blkTip, hitTip, fowTip, folTip, fopTip, epvTip};
        for(Tooltip tip : toolTips){
            tip.setShowDelay(Duration.seconds(tipDelay));
        }

        // Column Labels
        Label nameLabel = new Label("Name");
        Label posLabel = new Label("Pos");
        Label gpLabel = new Label("GP");
        Label gLabel = new Label("G");
        Label aLabel = new Label("A");
        Label ptsLabel = new Label("Pts");
        Label pmLabel = new Label("+/-");
        Label pimLabel = new Label("PIM");
        Label sLabel = new Label("S");
        Label toiLabel = new Label("TOI");
        Label blkLabel = new Label("Blk");
        Label hitLabel = new Label("Hit");
        Label fowLabel = new Label("FOW");
        Label folLabel = new Label("FOL");
        Label fopLabel = new Label("FO%");
        Label epvLabel = new Label("EPV");

        nameLabel.setTooltip(nameTip);
        posLabel.setTooltip(posTip);
        gpLabel.setTooltip(gpTip);
        gLabel.setTooltip(gTip);
        aLabel.setTooltip(aTip);
        ptsLabel.setTooltip(ptsTip);
        pmLabel.setTooltip(pmTip);
        pimLabel.setTooltip(pimTip);
        sLabel.setTooltip(sTip);
        toiLabel.setTooltip(toiTip);
        blkLabel.setTooltip(blkTip);
        hitLabel.setTooltip(hitTip);
        fowLabel.setTooltip(fowTip);
        folLabel.setTooltip(folTip);
        fopLabel.setTooltip(fopTip);
        epvLabel.setTooltip(epvTip);

        // Column Graphics
        nameCol.setGraphic(nameLabel);
        posCol.setGraphic(posLabel);
        gpCol.setGraphic(gpLabel);
        gCol.setGraphic(gLabel);
        aCol.setGraphic(aLabel);
        ptsCol.setGraphic(ptsLabel);
        pmCol.setGraphic(pmLabel);
        pimCol.setGraphic(pimLabel);
        sCol.setGraphic(sLabel);
        toiCol.setGraphic(toiLabel);
        blkCol.setGraphic(blkLabel);
        hitCol.setGraphic(hitLabel);
        fowCol.setGraphic(fowLabel);
        folCol.setGraphic(folLabel);
        fopCol.setGraphic(fopLabel);
        epvCol.setGraphic(epvLabel);

        // Add columns to table
        table.getColumns().add(nameCol);
        table.getColumns().add(posCol);
        table.getColumns().add(gpCol);
        table.getColumns().add(gCol);
        table.getColumns().add(aCol);
        table.getColumns().add(ptsCol);
        table.getColumns().add(pmCol);
        table.getColumns().add(pimCol);
        table.getColumns().add(sCol);
        table.getColumns().add(toiCol);
        table.getColumns().add(blkCol);
        table.getColumns().add(hitCol);
        table.getColumns().add(fowCol);
        table.getColumns().add(folCol);
        table.getColumns().add(fopCol);
        table.getColumns().add(epvCol);

        // Column widths
        ptsCol.setPrefWidth(35);
        fowCol.setPrefWidth(40);
        folCol.setPrefWidth(40);
        fopCol.setPrefWidth(40);
        epvCol.setPrefWidth(45);

        // Add items
        for(Stats s : Main.previousSeason.getStats()){
            table.getItems().add(s);
        }

        // Order by EPV
        epvCol.setSortType(TableColumn.SortType.DESCENDING);
        table.getSortOrder().add(epvCol);

        // Return table view
        return table;
    }

    /**
     * Updates the dashboard's table using the given filters.
     */
    private static void updateTable(){

        // Get values from combo boxes
        String seasonString = seasonComboBox.getSelectionModel().getSelectedItem();
        String position = positionComboBox.getSelectionModel().getSelectedItem();

        // Get Season object
        Season season = new Season();
        for(Season s : Main.seasons){
            if(Integer.parseInt(seasonString.substring(0,4)) == s.getStartYear()){
                season = Season.duplicateSeason(s);
            }
        }

        // Get Data for table
        List<Stats> stats = new ArrayList<>();
        for(Stats statLine : season.getStats()){
            if(Stats.isFromThisPosition(statLine, position)){
                stats.add(statLine);
            }
        }

        // Update table
        table.getItems().clear();
        for(Stats s : stats){
            table.getItems().add(s);
        }

        // Order by EPV
        epvCol.setSortType(TableColumn.SortType.DESCENDING);
        table.getSortOrder().add(epvCol);

        // Update table title
        String expandedPosition = "NA"; 
        if(position.equals("All")){
            expandedPosition = "Skater";
        }else{
            expandedPosition = Skater.expandPositionName(position);
        }

        tableTitle.setText(expandedPosition + " Stats for the " + season.getStartYear() + '-' + season.getEndYear() + " Season");
    }

    /**
     * Creates the bar chart used on the dashboard.
     * Contains the skaters with the top 5 EPVs for the given position & season combination.
     * Also display each skater's ratings for different stats.
     */
    private static void buildBarChart(){

        // X-Axis
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Player");

        // Y-Axis
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Rating");
        yAxis.setAutoRanging(false);
        yAxis.setUpperBound(Stats.MAX_RATING);

        // Chart
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Top 5 Skaters (" + Main.previousSeason.getString() + ')');
        barChart.setAnimated(false);

        // Get top 5 skaters for previous season.
        List<Stats> topFiveEPVs = Stats.getTopEPVs(Main.previousSeason, "All", 5);

        // Initialize Series
        epvSeries = new XYChart.Series<>();
        goalSeries = new XYChart.Series<>();
        assistSeries = new XYChart.Series<>();
        plusMinusSeries = new XYChart.Series<>();
        hitSeries = new XYChart.Series<>(); 
        blockSeries = new XYChart.Series<>();

        // Series Names
        epvSeries.setName("Estimated Value");
        goalSeries.setName("G Rating");
        assistSeries.setName("A Rating");
        plusMinusSeries.setName("+/- Rating");
        hitSeries.setName("HIT Rating");
        blockSeries.setName("BLK Rating");

        // Add data to series
        int rating;
        for(Stats statLine : topFiveEPVs){

            String name = Skater.shortenName(statLine.getSkaterName());

            // EPV Series
            epvSeries.getData().add(new XYChart.Data<String, Number>(name, statLine.getEPV()));

            // Goal Series
            rating = Stats.calculateStatRating(statLine, "Goals");
            goalSeries.getData().add(new XYChart.Data<String, Number>(name, rating));

            // Assist Series
            rating = Stats.calculateStatRating(statLine, "Assists");
            assistSeries.getData().add(new XYChart.Data<String, Number>(name, rating));

            // +/- Series
            rating = Stats.calculateStatRating(statLine, "+/-");
            plusMinusSeries.getData().add(new XYChart.Data<String, Number>(name, rating));

            // Hit Series
            rating = Stats.calculateStatRating(statLine, "Hits");
            hitSeries.getData().add(new XYChart.Data<String, Number>(name, rating));

            // Block Series
            rating = Stats.calculateStatRating(statLine, "Blocks");
            blockSeries.getData().add(new XYChart.Data<String, Number>(name, rating));
        }
        
        barChart.getData().add(epvSeries);
        barChart.getData().add(goalSeries);
        barChart.getData().add(assistSeries);
        barChart.getData().add(plusMinusSeries);
        barChart.getData().add(hitSeries);
        barChart.getData().add(blockSeries);
    }

    /**
     * Updates the dashboard's bar chart with the given filters.
     */
    private static void updateBarChart(){

        int numSkaters = 5; // Number of skaters being evaluated for the bar chart.

        // Get values from combo boxes
        String seasonString = seasonComboBox.getSelectionModel().getSelectedItem();
        String position = positionComboBox.getSelectionModel().getSelectedItem();
        
        Season season = new Season();
        for(Season s : Main.seasons){
            if(s.getStartYear() == Integer.parseInt(seasonString.substring(0,4))){
                season = Season.duplicateSeason(s);
            }
        }

        List<Stats> topFiveEPVs = Stats.getTopEPVs(season, position, numSkaters);

        // Clear Data
        epvSeries.getData().clear();
        goalSeries.getData().clear();
        assistSeries.getData().clear();
        plusMinusSeries.getData().clear();
        hitSeries.getData().clear();
        blockSeries.getData().clear();

        // Update series
        int rating;
        for(Stats statLine : topFiveEPVs){

            String name = Skater.shortenName(statLine.getSkaterName());

            // Series 1 - EPVs
            epvSeries.getData().add(new XYChart.Data<String, Number>(name, statLine.getEPV()));

            // Series 2 - Goal Ratings
            rating = Stats.calculateStatRating(statLine, "Goals");
            goalSeries.getData().add(new XYChart.Data<String, Number>(name, rating));

            // Series 3 - Assist Ratings
            rating = Stats.calculateStatRating(statLine, "Assists");
            assistSeries.getData().add(new XYChart.Data<String, Number>(name, rating));

            // +/- Series
            rating = Stats.calculateStatRating(statLine, "+/-");
            plusMinusSeries.getData().add(new XYChart.Data<String, Number>(name, rating));

            // Series 4 - Hit Ratings
            rating = Stats.calculateStatRating(statLine, "Hits");
            hitSeries.getData().add(new XYChart.Data<String, Number>(name, rating));

            // Series 5 - Block Ratings
            rating = Stats.calculateStatRating(statLine, "Blocks");
            blockSeries.getData().add(new XYChart.Data<String, Number>(name, rating));
        }
   
        // Update chart title
        String expandedPosition;
        if(position.equals("All")){
            expandedPosition = "Skaters";
        }
        else if(position.equals("D")){
            expandedPosition = "Defensemen";
        }
        else{
            expandedPosition = Skater.expandPositionName(position) + 's';
        }
        barChart.setTitle("Top 5 " + expandedPosition + " (" + season.getString() + ')');
    }

    /**
     * Creates the dashboard's line chart.
     * Displays a team's average EPV compared to the league's min/max ratings over time.
     */
    private static void buildLineChart(){

        // X-Axis
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Season");

        // Y-Axis
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Avg EPV");

        // Line Chart
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(Main.currentUser.getTeam().getName() + " Average EPV");
        lineChart.setAnimated(false);
        
        // Team Series
        teamSeries = new XYChart.Series<>();
        teamSeries.setName("Team Average");
        for(Roster roster : Main.currentUser.getTeam().getRosters()){
            teamSeries.getData().add(new XYChart.Data<String, Number>(roster.getSeason().getString(), roster.getAvgEPV()));
        }

        // Min Series
        XYChart.Series<String, Number> minSeries = new XYChart.Series<>();
        minSeries.setName("Minimum Average");
        List<Roster> minRosters = getMinRosters();
        for(Roster roster : minRosters){
            minSeries.getData().add(new XYChart.Data<String, Number>(roster.getSeason().getString(), roster.getAvgEPV()));
        }

        // Max Series
        XYChart.Series<String, Number> maxSeries = new XYChart.Series<>();
        maxSeries.setName("Maximum Average");
        List<Roster> maxRosters = getMaxRosters();
        for(Roster roster : maxRosters){
            maxSeries.getData().add(new XYChart.Data<String, Number>(roster.getSeason().getString(), roster.getAvgEPV()));
        }

        lineChart.getData().add(teamSeries);
        lineChart.getData().add(minSeries);
        lineChart.getData().add(maxSeries);
    }

    /**
     * Updates the table and bar chart when the update button is clicked.
     */
    private static void updateButtonHandler(){
        updateTable();
        updateBarChart();
    }

    /**
     * Gets the roster with the worst average EPV for each season.
     * @return A list of the worst roster from each season.
     */
    private static List<Roster> getMinRosters(){

        List<Roster> minRosters = new ArrayList<>();

        for(Season season : Main.seasons){

            if(season != Main.nextSeason){

                Roster minRoster = new Roster();
                minRoster.setSeason(season);
                minRoster.setAvgEPV(Stats.MAX_RATING);

                for(Team team : Main.teams){
                    for(Roster roster : team.getRosters()){
                        if(roster.getSeason() == season && roster.getAvgEPV() < minRoster.getAvgEPV()){
                            minRoster.setAvgEPV(roster.getAvgEPV());
                            minRoster.setTeam(team);
                        }
                    }
                }

                minRosters.add(minRoster);

            }
        }

        return minRosters;
    }

    /**
     * Gets the roster with the best average EPV for each season.
     * @return A list of the best roster from each season.
     */
    private static List<Roster> getMaxRosters(){

        List<Roster> maxRosters = new ArrayList<>();

        for(Season season : Main.seasons){

            if(season != Main.nextSeason){

                Roster maxRoster = new Roster();
                maxRoster.setSeason(season);
                maxRoster.setAvgEPV(Stats.MIN_RATING);

                for(Team team : Main.teams){
                    for(Roster roster : team.getRosters()){
                        if(roster.getSeason() == season && roster.getAvgEPV() > maxRoster.getAvgEPV()){
                            maxRoster.setAvgEPV(roster.getAvgEPV());
                            maxRoster.setTeam(team);
                        }
                    }
                }

                maxRosters.add(maxRoster);
            }
        }

        return maxRosters;
    }

    /**
     * Updates the line chart when a new team is selected.
     */
    private static void teamComboBoxHandler(){

        // Get value from combo box
        Team team = Team.getTeamByName(teamComboBox.getSelectionModel().getSelectedItem());

        // Clear team series
        teamSeries.getData().clear();

        // Update team series
        for(Roster roster : team.getRosters()){
            if(roster.getSeason() != Main.nextSeason){
                teamSeries.getData().add(new XYChart.Data<String, Number>(roster.getSeason().getString(), roster.getAvgEPV()));
            }
        }

        // Update title
        lineChart.setTitle(team.getName() + " Average EPV");
    }

    //region Check Box Handlers
    /**
     * The statCheckHandler functions update the bar chart when clicked.
     */
    private static void goalsCheckHandler(){
        if(barChart.getData().contains(goalSeries)) {barChart.getData().remove(goalSeries);}
        else{barChart.getData().add(goalSeries);}
    }

    /**
     * The statCheckHandler functions update the bar chart when clicked.
     */
    private static void assistsCheckHandler(){
        if(barChart.getData().contains(assistSeries)) {barChart.getData().remove(assistSeries);}
        else{barChart.getData().add(assistSeries);}
    }

    /**
     * The statCheckHandler functions update the bar chart when clicked.
     */
    private static void plusMinusCheckHandler(){
        if(barChart.getData().contains(plusMinusSeries)) {barChart.getData().remove(plusMinusSeries);}
        else{barChart.getData().add(plusMinusSeries);}
    }

    /**
     * The statCheckHandler functions update the bar chart when clicked.
     */
    private static void hitsCheckHandler(){
        if(barChart.getData().contains(hitSeries)) {barChart.getData().remove(hitSeries);}
        else{barChart.getData().add(hitSeries);}
    }

    /**
     * The statCheckHandler functions update the bar chart when clicked.
     */
    private static void blocksCheckHandler(){
        if(barChart.getData().contains(blockSeries)) {barChart.getData().remove(blockSeries);}
        else{barChart.getData().add(blockSeries);}
    }
    //endregion
}
