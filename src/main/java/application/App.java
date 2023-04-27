package application;

import java.io.FileNotFoundException;
import java.io.IOException;

//JavaFX imports
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class App extends Application 
{
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        
        LogPlan app = new LogPlan();
        for(int i = 0; i < app.getDeveloperList().size(); i++)
        {
            Developer dev = app.getDeveloperList().get(i);
            System.out.println(dev.getId());
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader logInn = new FXMLLoader(getClass().getResource("logIn.fxml"));
        StackPane root = new StackPane();
        root.getChildren().add(new Label("Hello World!"));
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }


}
