package com.bsuir.stemming.controller;

import com.bsuir.stemming.entity.StemResult;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class StemResultsController {
    @FXML
    private TableView<StemResult> stemResultsTable;
    @FXML
    private TableColumn<StemResult, String> stemColumn;
    @FXML
    private TableColumn<StemResult, Integer> frequencyColumn;

    @FXML
    private Label stemmingTimeLabel;

    private static final String RESULTS_HELP_FXML_FILE_PATH = "view/results_help.fxml";
    private static final String STYLE_FILE_PATH = "style/main.css";


    public void setStemResults(List<StemResult> stemResults) {
        stemResultsTable.setItems(FXCollections.observableList(stemResults));
    }



    @FXML
    private void initialize() {
        stemColumn.setCellValueFactory(new PropertyValueFactory<>("wordValue"));
        frequencyColumn.setCellValueFactory(new PropertyValueFactory<>("frequency"));

    }

    @FXML
    private void saveResults(){
        Stage stage = new Stage();
        List<StemResult> stemResults = stemResultsTable.getItems();

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("txt","*.txt"));
        File file = fileChooser.showSaveDialog(stage);

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))){

            for(StemResult stemResult : stemResults) {
                bufferedWriter.write("Word: "+stemResult.getWordValue()+" frequency:"+stemResult.getFrequency()+"\n");
            }

           bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void setStemmingTime(int time){
        stemmingTimeLabel.setText("Stemming time: "+time+"msc");
    }

    @FXML
    private void controlShowingResultsHelp() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource(RESULTS_HELP_FXML_FILE_PATH));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getClassLoader().getResource(STYLE_FILE_PATH).toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Help");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
