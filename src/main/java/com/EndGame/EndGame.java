package com.EndGame;

import com.StartGame.ColorTheme;
import com.StartGame.GameModes;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;


/**
 * A class for game over screen.
 */
public class EndGame {
    private static EndGame singleInstance = null;
    private EndGame(){

    }
    public static EndGame getInstance(){
        if(singleInstance == null)
            singleInstance= new EndGame();
        return singleInstance;
    }

    public void endGameShow(Scene endGameScene, Group root, Stage primaryStage,long score){
        Text text = new Text("GAME OVER");
        text.setFont(Font.font(80));
        root.getChildren().add(text);

        Text scoreText = new Text(score+"");
        scoreText.setFill(Color.BLACK);
        scoreText.setFont(Font.font(80));
        root.getChildren().add(scoreText);

        // quit button
        Button quitButton = new Button("QUIT");
        quitButton.setPrefSize(85,35);
        quitButton.setTextFill(Color.BLACK);
        root.getChildren().add(quitButton);

        // main menu button
        Button mainMenuButton = new Button("BACK TO MAIN MENU");
        mainMenuButton.setPrefSize(150,35);
        mainMenuButton.setTextFill(Color.BLACK);
        root.getChildren().add(mainMenuButton);

        // retry button
        Button retryButton = new Button("RETRY");
        retryButton.setPrefSize(85,35);
        retryButton.setTextFill(Color.BLACK);
        root.getChildren().add(retryButton);

        // high score button
        Button highScoreButton = new Button("HIGH SCORE");
        highScoreButton.setPrefSize(150,35);
        highScoreButton.setTextFill(Color.BLACK);
        root.getChildren().add(highScoreButton);

        StackPane mainPane;

        VBox vBox = new VBox(5);
        VBox.setMargin(text, new Insets(-80, 0, 0, 0));
        VBox.setMargin(scoreText, new Insets(0, 0, 120, 0));
        vBox.setAlignment(Pos. CENTER);
        vBox.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.90));
        vBox.prefHeightProperty().bind(primaryStage.heightProperty());

        HBox hBox = new HBox();
        hBox.getChildren().addAll(mainMenuButton, retryButton, quitButton);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(60);

        vBox.getChildren().addAll(text, scoreText, hBox, highScoreButton);

        mainPane = new StackPane(vBox);
        mainPane.setPadding(new Insets(30));
        root.getChildren().add(mainPane);

        ColorTheme thisColor = new ColorTheme();

        //main menu button onClick
        mainMenuButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Parent root;
                try {
                    addScore(score);
                    primaryStage.close();
                    root = FXMLLoader.load(getClass().getResource("/com/Game/main_menu.fxml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                switch (ColorTheme.getMyColor()) {
                    case "black" -> root.setStyle("-fx-background-color: black");
                    case "white" -> root.setStyle("-fx-background-color: white");
                    case "green" -> root.setStyle("-fx-background-color: green");
                }

                // Create the Scene
                Scene mainMenu = new Scene(root);
                // Set the Scene to the Stage
                primaryStage.setScene(mainMenu);
                // Display the Stage
                primaryStage.setTitle("2048 Game");
                // center on screen
                primaryStage.centerOnScreen();
                primaryStage.show();
            }
        });

        //retry button onClick
        retryButton.setOnAction(actionEvent -> {
            GameModes a = new GameModes();
            try {
                addScore(score);
                primaryStage.close();
                a.start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        quitButton.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Quit Dialog");
            alert.setHeaderText("Quit from this page");
            alert.setContentText("Are you sure?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                addScore(score);
                root.getChildren().clear();
                primaryStage.close();
            }
        });

        highScoreButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Parent root;
                try {
                    addScore(score);
                    root = FXMLLoader.load(getClass().getResource("/com/Game/highScoreList.fxml"));
                    primaryStage.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                switch (ColorTheme.getMyColor()) {
                    case "black" -> root.setStyle("-fx-background-color: black");
                    case "white" -> root.setStyle("-fx-background-color: white");
                    case "green" -> root.setStyle("-fx-background-color: green");
                }

                // Create the Scene
                Scene highScore = new Scene(root);
                // Set the Scene to the Stage
                primaryStage.setScene(highScore);
                // Display the Stage
                primaryStage.setTitle("High Score List");
                // center on screen
                primaryStage.centerOnScreen();
                primaryStage.show();
            }
        });
    }

    // write to file
    public void addScore(long score){
        Long line = score;

        FileWriter file_writer;
        try {
            file_writer = new FileWriter("highScoreList.txt", true);
            BufferedWriter buffered_Writer = new BufferedWriter(file_writer);
            buffered_Writer.write(line+"\n");
            buffered_Writer.flush();
            buffered_Writer.close();
        } catch (IOException e) {
            System.out.println("Fail to save score" + e);
        }
    }
}
