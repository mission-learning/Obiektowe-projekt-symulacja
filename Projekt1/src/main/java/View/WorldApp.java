package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class WorldApp extends Application {
    //Aplikacja powstala z wykorzystaniem SceneBuildera do tworzenia i zmieniania plikow fxml

    @Override
    public void start(Stage primaryStage) {
        try{
            FXMLLoader loader = new FXMLLoader();
            BorderPane root = loader.load(getClass().getResource("/View.fxml").openStream());
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }catch(Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
