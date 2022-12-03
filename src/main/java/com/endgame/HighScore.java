package com.endgame;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.startgame.colortheme.ChangeColor.fxmlColor;

/**
 * Reads all users' scores from a file and shows the sorted high score list.
 *
 * @author  Lisa Ho Yen Xin
 * @version %I%, %G%
 * @since   2020-11-1
 */
public class HighScore {
    private static HighScore singleInstance = null;

    /**
     * Returns an active instance of the class if it exists.
     * Otherwise, creates a new instance of the {@link HighScore} class.
     *
     * @return instance of class
     */
    public static HighScore getInstance() {
        if (singleInstance == null)
            singleInstance = new HighScore();
        return singleInstance;
    }

    @FXML
    private TextArea usernameList;

    @FXML
    private TextArea scoreList;

    @FXML
    private Button backButton;

    /**
     * Sets the text for the high score list when the contents of the fxml file have been completely loaded.
     *
     * @throws IOException if fail to load file
     */
    public void initialize() throws IOException {
        usernameList.setText(getNameList(sortLines()));
        scoreList.setText(getScoreList(sortLines()));
        backButton.setOnAction(backToMenu);
    }

    /**
     * Reads all the content from a file,
     * <a href="file:C:\Users\lisah\IdeaProjects\COMP2042_CW_hfylh2\highScoreList.txt">
     *  * highScoreList.txt</a>, which includes usernames and scores.
     * Sorts the scores of all users in descending order and returns a sorted high score list.
     *
     * @return sorted high score list
     * @throws IOException if fail to load file
     */
    private String sortLines() throws IOException {
        String allContent = new String(Files.readAllBytes(Paths.get("highScoreList.txt")));
        ArrayList<String> str = new ArrayList<>(Arrays.asList(allContent.split("\n")));

        // sort in descending order
        str.sort((o1, o2) -> Integer.compare(
                Integer.parseInt(o2.substring(o2.indexOf(" ") + 1)),
                Integer.parseInt(o1.substring(o1.indexOf(" ") + 1))));

        return String.join("\n", str);
    }

    public String getNameList(String sorted) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new StringReader(sorted));

        String line = bufferedReader.readLine();
        String username;
        int counter = 0;

        ArrayList<String> nameStr = new ArrayList<>();

        while(line != null) {
            username = line.substring(0, line.indexOf(" "));
            nameStr.add(username);

            counter++;
            line = bufferedReader.readLine();

            if(counter==10){
                break;
            }
        }
        return String.join("\n", nameStr);
    }

    public String getScoreList(String sorted) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new StringReader(sorted));

        String line = bufferedReader.readLine();
        String score;
        int counter = 0;

        ArrayList<String> scoreStr = new ArrayList<>();

        while(line != null) {
            score = line.substring(line.indexOf(" ") + 1);
            scoreStr.add(score);

            counter++;
            line = bufferedReader.readLine();

            if(counter==10){
                break;
            }
        }
        return String.join("\n", scoreStr);
    }

    @FXML
    EventHandler<ActionEvent> backToMenu = new EventHandler<>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            Stage stage;
            Parent root;

            stage = (Stage) backButton.getScene().getWindow();
            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/game/main_menu.fxml")));
                fxmlColor(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("2048 Game");
            stage.centerOnScreen();
            stage.show();
        }
    };
}
