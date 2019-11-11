package com.bsuir.stemming;



import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Runner extends Application {
    private static final String MAIN_FXML_FILE_PATH = "view/main.fxml";
    private static final String STYLE_FILE_PATH = "style/main.css";

    public static void main(String[] args) {


        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(MAIN_FXML_FILE_PATH));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getClassLoader().getResource(STYLE_FILE_PATH).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Stemming system");
        primaryStage.setOnCloseRequest(event -> {

            Platform.exit();
        });
        primaryStage.show();
    }
}
