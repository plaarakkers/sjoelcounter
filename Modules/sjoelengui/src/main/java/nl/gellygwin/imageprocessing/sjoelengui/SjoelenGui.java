package nl.gellygwin.imageprocessing.sjoelengui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.gellygwin.imageprocessing.opencvprocessor.Processor;

/**
 *
 * SjoelenGui
 */
public class SjoelenGui extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Processor.initialize();
        
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/SjoelenGui.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("SjoelenGui");
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

}
