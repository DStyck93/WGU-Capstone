package capstone.scenes;

import java.util.ArrayList;
import java.util.List;

import capstone.Main;
import capstone.objects.Skater;
import capstone.objects.Stats;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * This class handles the design of the skater predictions scene and its relevant functions.
 */
public class SkaterPredictions {

    private static ComboBox<String> positionComboBox;

    // Table
    private static Label tableHeader;
    private static TableView<Stats> table;
    private static TableColumn<Stats, Integer> epvCol;

    // Top 5 Chart
    private static BarChart<String, Number> chart;
    private static XYChart.Series<String, Number> epvSeries;
    private static XYChart.Series<String, Number> goalSeries;
    private static XYChart.Series<String, Number> assistSeries;
    private static XYChart.Series<String, Number> plusMinusSeries;
    private static XYChart.Series<String, Number> hitSeries; 
    private static XYChart.Series<String, Number> blockSeries;

    /**
     * Displays the skater predictions scene.
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
     * Creates and returns the skater predictions scene.
     * @return The skater predictions scene.
     */
    private static Scene buildScene(double sceneWidth, double sceneHeight){

        //region Top
        // Menu Bar
        MenuBar menu = MyMenuBar.getMyMenuBar();
        //endregion

        //region Left
        // Spacing from top
        Button spacer = new Button("Blank");
        spacer.setPrefSize(200, 50);
        spacer.setVisible(false);

        // Dashboard Button
        Button dashboardButton = new Button("Return to Dashboard");
        Font buttonFont = Font.font("Arial", FontWeight.BOLD, 20);
        dashboardButton.setFont(buttonFont);
        dashboardButton.setTextAlignment(TextAlignment.CENTER);
        dashboardButton.setPrefSize(200, 100);
        dashboardButton.setWrapText(true);
        dashboardButton.setOnAction(e->{Dashboard.loadScene();});

        // Left Box
        VBox leftBox = new VBox(spacer, dashboardButton);
        leftBox.setPadding(new Insets(20));
        leftBox.setAlignment(Pos.TOP_LEFT);
        //endregion

        //region Center
        // Position Filter
        Label positionDropdownLabel = new Label("Filter by position: ");
        String[] positions = {"All", "F", "C", "W", "LW", "RW", "D"};
        positionComboBox = new ComboBox<>(FXCollections.observableArrayList(positions));
        positionComboBox.setValue("All");
        HBox positionBox = new HBox(positionDropdownLabel, positionComboBox);
        positionBox.setSpacing(10);

        // Update Button
        Button update = new Button("Update");
        update.setOnAction(e->{updateButtonHandler();});
        
        // Filter Box
        VBox filterBox = new VBox(positionBox, update);
        filterBox.setSpacing(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        filterBox.setMinWidth(170);

        // Table Header
        tableHeader = new Label(Main.nextSeason.getString() + " Projections");
        tableHeader.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
        tableHeader.setAlignment(Pos.CENTER);

        // Table
        buildTable();

        // Table VBox
        VBox tableBox = new VBox(tableHeader, table);
        tableBox.setPadding(new Insets(50));
        tableBox.setAlignment(Pos.CENTER);
        tableBox.setSpacing(40);

        // Center Box
        HBox centerBox = new HBox(filterBox, tableBox);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(20);
        //endregion
        
        //region Right
        
        // Chart
        buildChart();

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

        VBox rightBox = new VBox(chart, checkBox);
        rightBox.setPadding(new Insets(20));
        rightBox.setAlignment(Pos.CENTER);
        //endregion

        // Border Pane
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menu);
        borderPane.setLeft(leftBox);
        borderPane.setCenter(centerBox);
        borderPane.setRight(rightBox);

        // Scene
        Scene scene = new Scene(borderPane, sceneWidth, sceneHeight);

        return scene;
    }

    /**
     * Creates the table used for the skater predictions scene.
     */
    private static void buildTable(){

        table = new TableView<>();

        // Format
        table.setEditable(false);
        table.setMinWidth(555);
        table.setMaxWidth(555);
        table.setPrefHeight(800);

        // Columns
        TableColumn<Stats, String> nameCol = new TableColumn<>("Name");
        TableColumn<Stats, String> positionCol = new TableColumn<>("Pos");
        TableColumn<Stats, Integer> gCol = new TableColumn<>("G");
        TableColumn<Stats, Integer> aCol = new TableColumn<>("A");
        TableColumn<Stats, Integer> ptsCol = new TableColumn<>("P");
        TableColumn<Stats, Integer> pmCol = new TableColumn<>("+/-");
        TableColumn<Stats, Integer> pimCol = new TableColumn<>("PIM");
        TableColumn<Stats, Integer> sCol = new TableColumn<>("S");
        TableColumn<Stats, Integer> blkCol = new TableColumn<>("BLK");
        TableColumn<Stats, Integer> hitCol = new TableColumn<>("HIT");
        TableColumn<Stats, Integer> fopCol = new TableColumn<>("FO%");
        epvCol = new TableColumn<>("EPV");

        // Set Column value types
        nameCol.setCellValueFactory(new PropertyValueFactory<Stats, String>("skaterName"));
        positionCol.setCellValueFactory(new PropertyValueFactory<Stats, String>("skaterPosition"));
        gCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("goals"));
        aCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("assists"));
        ptsCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("points"));
        pmCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("plusMinus"));
        pimCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("penaltyMins"));
        sCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("shots"));
        blkCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("blocks"));
        hitCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("hits"));
        fopCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("faceoffPercentage"));
        epvCol.setCellValueFactory(new PropertyValueFactory<Stats, Integer>("ePV"));

        // Add columns to table
        table.getColumns().add(nameCol);
        table.getColumns().add(positionCol);
        table.getColumns().add(gCol);
        table.getColumns().add(aCol);
        table.getColumns().add(ptsCol);
        table.getColumns().add(pmCol);
        table.getColumns().add(pimCol);
        table.getColumns().add(sCol);
        table.getColumns().add(blkCol);
        table.getColumns().add(hitCol);
        table.getColumns().add(fopCol);
        table.getColumns().add(epvCol);

        // Add items to table
        for(Stats statLine : Main.nextSeason.getStats()){
            table.getItems().add(statLine);
        }

        // Order by EPV
        epvCol.setSortType(TableColumn.SortType.DESCENDING);
        table.getSortOrder().add(epvCol);
    }

    /**
     * Creates the bar chart used in the skater prediction's scene.
     */
    private static void buildChart(){

        // X-Axis
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Player");

        // Y-Axis
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Rating");
        yAxis.setAutoRanging(false);
        yAxis.setUpperBound(Stats.MAX_RATING);

        // Chart
        chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Top 5 Projected Skaters");
        chart.setAnimated(false);

        // Get top 5 skaters for previous season.
        List<Stats> topFiveEPVs = Stats.getTopEPVs(Main.nextSeason, "All", 5);

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

            // EPVs
            epvSeries.getData().add(new XYChart.Data<String, Number>(name, statLine.getEPV()));

            // Goal Ratings
            rating = Stats.calculateStatRating(statLine, "Goals");
            goalSeries.getData().add(new XYChart.Data<String, Number>(name, rating));

            // Assist Ratings
            rating = Stats.calculateStatRating(statLine, "Assists");
            assistSeries.getData().add(new XYChart.Data<String, Number>(name, rating));

            // Plus Minus Ratings
            rating = Stats.calculateStatRating(statLine, "+/-");
            plusMinusSeries.getData().add(new XYChart.Data<String, Number>(name, rating));

            // Hit Ratings
            rating = Stats.calculateStatRating(statLine, "Hits");
            hitSeries.getData().add(new XYChart.Data<String, Number>(name, rating));

            // Block Ratings
            rating = Stats.calculateStatRating(statLine, "Blocks");
            blockSeries.getData().add(new XYChart.Data<String, Number>(name, rating));
        }
        
        chart.getData().add(epvSeries);
        chart.getData().add(goalSeries);
        chart.getData().add(assistSeries);
        chart.getData().add(plusMinusSeries);
        chart.getData().add(hitSeries);
        chart.getData().add(blockSeries);
    }

    /**
     * This function combines the updateTable() and updateChart() functions.
     */
    private static void updateButtonHandler(){
        updateTable();
        updateChart();
    }

    /**
     * Updates the table with the selected filters.
     */
    private static void updateTable(){

        // Get position from combo box
        String position = positionComboBox.getSelectionModel().getSelectedItem();

        // Get List of stats from given position
        List<Stats> stats = new ArrayList<>();
        for(Stats statLine : Main.nextSeason.getStats()){
            if(Stats.isFromThisPosition(statLine, position)){
                stats.add(statLine);
            }
        }

        // Update Table
        table.getItems().clear();
        for(Stats statLine : stats){
            table.getItems().add(statLine);
        }

        // Order by EPV
        epvCol.setSortType(TableColumn.SortType.DESCENDING);
        table.getSortOrder().add(epvCol);

        // Update Title
        String header = "2022-2023 ";
        if(position.equals("All")){
            header += "Projections";
        }
        else{
            header += Skater.expandPositionName(position) + " Projections";
        }
        tableHeader.setText(header);
    }

    /**
     * Updates the bar chart with the given filters.
     */
    private static void updateChart(){

        int numSkaters = 5; // Number of skaters being evaluated for the bar chart.

        // Get Position value from combo box
        String position = positionComboBox.getSelectionModel().getSelectedItem();
    
        // Get top five projected skaters from the given position
        List<Stats> topFiveEPVs = Stats.getTopEPVs(Main.nextSeason, position, numSkaters);

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

            // EPVs
            epvSeries.getData().add(new XYChart.Data<String, Number>(name, statLine.getEPV()));

            // Goal Ratings
            rating = Stats.calculateStatRating(statLine, "Goals");
            goalSeries.getData().add(new XYChart.Data<String, Number>(name, rating));

            // Assist Ratings
            rating = Stats.calculateStatRating(statLine, "Assists");
            assistSeries.getData().add(new XYChart.Data<String, Number>(name, rating));

            // +/- Rating
            rating = Stats.calculateStatRating(statLine, "+/-");
            plusMinusSeries.getData().add(new XYChart.Data<String, Number>(name, rating));

            // Hit Rating
            rating = Stats.calculateStatRating(statLine, "Hits");
            hitSeries.getData().add(new XYChart.Data<String, Number>(name, rating));

            // Block Rating
            rating = Stats.calculateStatRating(statLine, "Blocks");
            blockSeries.getData().add(new XYChart.Data<String, Number>(name, rating));
        }
   
        // Update chart title
        String title = "Top 5 Projected ";
        if(position.equals("All")){
            title += "Skaters";
        }
        else if(position.equals("D")){
            title += "Defensemen";
        }
        else{
            title += Skater.expandPositionName(position) + 's';
        }
        chart.setTitle(title);        
    }

    //region Check Box Handlers
    /**
     * Check box handler for the bar chart.
     */
    private static void goalsCheckHandler(){
        if(chart.getData().contains(goalSeries))
            chart.getData().remove(goalSeries);
        else
            chart.getData().add(goalSeries);
    }

    /**
     * Check box handler for the bar chart.
     */
    private static void assistsCheckHandler(){
        if(chart.getData().contains(assistSeries))
            chart.getData().remove(assistSeries);
        else
            chart.getData().add(assistSeries);
    }

    /**
     * Check box handler for the bar chart.
     */
    private static void plusMinusCheckHandler(){
        if(chart.getData().contains(plusMinusSeries))
            chart.getData().remove(plusMinusSeries);
        else
            chart.getData().add(plusMinusSeries);
    }

    /**
     * Check box handler for the bar chart.
     */
    private static void hitsCheckHandler(){
        if(chart.getData().contains(hitSeries))
            chart.getData().remove(hitSeries);
        else
            chart.getData().add(hitSeries);
    }

    /**
     * Check box handler for the bar chart.
     */
    private static void blocksCheckHandler(){
        if(chart.getData().contains(blockSeries))
            chart.getData().remove(blockSeries);
        else
            chart.getData().add(blockSeries);
    }
    //endregion
}
