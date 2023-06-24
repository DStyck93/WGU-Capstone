package capstone.scenes;

import capstone.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Designs the compare players scene and contains relevant functions.
 * Currently only displays a coming soon message. This class is for a future version.
 */
public class ComparePlayers {
    
    public static void loadScene(){
        if(Main.stage.isMaximized()){
            Main.stage.setMaximized(false);
            Main.stage.setScene(buildScene(Main.DEFAULT_SCENE_WIDTH, Main.DEFAULT_SCENE_HEIGHT));
            Main.stage.setMaximized(true);
        }
        else{
            Main.stage.setScene(buildScene(Main.stage.getScene().getWidth(), Main.stage.getScene().getHeight()));
        }
    }

    /**
     * Creates the compare players scene.
     * @return The compare players scene.
     */
    private static Scene buildScene(double sceneWidth, double sceneHeight){
        
        MenuBar menu = MyMenuBar.getMyMenuBar();

        Label label = new Label("Coming Soon...");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 30));

        Button button = new Button("Return to Dashboard");
        button.setOnAction(event->{Dashboard.loadScene();});

        VBox box = new VBox(label, button);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(200));
        box.setSpacing(50);

        VBox vbox = new VBox(menu, box);

        Scene scene = new Scene(vbox, sceneWidth, sceneHeight);

        return scene;
    }
}
