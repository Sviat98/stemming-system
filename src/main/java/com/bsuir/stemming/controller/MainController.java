package com.bsuir.stemming.controller;

import com.bsuir.stemming.api.data.DocumentParser;
import com.bsuir.stemming.api.data.TextReader;
import com.bsuir.stemming.api.service.StopwordService;
import com.bsuir.stemming.data.parser.AdaptiveDocumentParser;
import com.bsuir.stemming.data.reader.InformationTextReader;

import com.bsuir.stemming.data.reader.InvalidPathException;
import com.bsuir.stemming.entity.StemResult;
import com.bsuir.stemming.service.StopwordServiceImpl;
import com.bsuir.stemming.data.reader.ReadingException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.tartarus.snowball.ext.germanStemmer;
import org.tartarus.snowball.ext.russianStemmer;
import org.tartarus.snowball.ext.spanishStemmer;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class MainController {
    private static final String MAIN_STEM_RESULTS_FILE_PATH = "view/stem_results.fxml";
    private static final String STYLE_FILE_PATH = "style/main.css";
    private static final String MAIN_HELP_FXML_FILE_PATH = "view/main_help.fxml";

    private static final int MILLIS_FROM_NANO = 1000000;

    private static DocumentParser documentParser = new AdaptiveDocumentParser();
    private static StopwordService stopwordService = new StopwordServiceImpl();
    private static TextReader documentReader = new InformationTextReader();


    @FXML
    private TextField queryField;

    @FXML
    private TextField minRankField;

    @FXML
    private Label filePathLabel;

    @FXML
    private RadioButton germanButton;

    @FXML
    private RadioButton spanishButton;


    @FXML
    private void loadFile() {
        Stage stage = new Stage();

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML", "*.html"));
        File file = new FileChooser().showOpenDialog(stage);
        System.out.println(file.getParent());

        filePathLabel.setText(file.getAbsolutePath());
    }


    @FXML
    private void controlStemming() {
        try{
        int timeBeforeStemming = LocalDateTime.now().getNano()/MILLIS_FROM_NANO;

            List<StemResult> stemResults = doStemming();

            int timeAfterStemming = LocalDateTime.now().getNano()/MILLIS_FROM_NANO;
        for (StemResult stemResult:stemResults) {
            System.out.println("Word: " + stemResult.getWordValue() + " frequency:" + stemResult.getFrequency());
        }

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource(MAIN_STEM_RESULTS_FILE_PATH));
            Parent root = loader.load();

            StemResultsController stemResultsController = loader.getController();
            stemResultsController.setStemResults(stemResults);
            stemResultsController.setStemmingTime(timeAfterStemming-timeBeforeStemming);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getClassLoader().getResource(STYLE_FILE_PATH).toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Stemming results");
            stage.setScene(scene);
            stage.show();
        }catch (IOException | InvalidPathException e){
            e.printStackTrace();
        }


    }

    private List<StemResult> doStemming() throws InvalidPathException{
        String filePath = filePathLabel.getText();

        if (filePath.equals("файл не выбран")){
            throw new InvalidPathException("You didn't choose the file");
        }


        try{
            String documentText = documentReader.read(filePath);

            List<String> wordValues = documentParser.parse(documentText);

            wordValues.removeIf(word -> stopwordService.containsIgnoreCase(word));

            for (String word : wordValues) {

                if(germanButton.isSelected()){
                    germanStemmer germanStemmer = new germanStemmer();
                    germanStemmer.setCurrent(word);
                    if (germanStemmer.stem()){
                        wordValues.set(wordValues.indexOf(word),germanStemmer.getCurrent());
                    }
                }

                else if(spanishButton.isSelected()){
                    spanishStemmer spanishStemmer = new spanishStemmer();
                    spanishStemmer.setCurrent(word);
                    if (spanishStemmer.stem()){
                        wordValues.set(wordValues.indexOf(word),spanishStemmer.getCurrent());
                    }
                }

            }

            Set<String> uniqueWords = new HashSet<>(wordValues);

            List<StemResult> stemResults = new ArrayList<>();

            for(String word:uniqueWords){
                stemResults.add(new StemResult(word,Collections.frequency(wordValues,word)));
            }
            return stemResults;
        }
        catch (ReadingException e){
            e.printStackTrace();
        }

return Collections.emptyList();
    }


    @FXML
    private void controlShowingHelp() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource(MAIN_HELP_FXML_FILE_PATH));
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
