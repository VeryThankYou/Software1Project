package application;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Parent;
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
        for(int i = 0; i < app.getProjectList().size(); i++)
        {
            Project proj = app.getProjectList().get(i);
            System.out.println(proj.getName());
            System.out.println(proj.getId());
            for(int i2 = 0; i2 < proj.getActivities().size(); i2 ++)
            {
                Activity act = proj.getActivities().get(i2);
                System.out.println(act.getName());
            }
        }
        
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("logIn.fxml"));
        System.out.println("Loader created");
        Parent root = loader.load();
        System.out.println("Root created");

        //Parent root = FXMLLoader.load(getClass().getResource("logIn.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
