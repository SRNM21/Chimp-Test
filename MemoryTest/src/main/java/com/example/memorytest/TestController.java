package com.example.memorytest;

import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class TestController {

    /*
        @VALUES
    */
    private int strikes = 0;
    private int  maxSlots = 5;
    private int enumeration = 1;
    public boolean SFX_On = true;
    public int[] slots = {
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
    };

    /*
        @FXML
     */
    @FXML
    private VBox vbox;
    @FXML
    private Circle strike1;
    @FXML
    private Circle strike2;
    @FXML
    private Circle strike3;
    @FXML
    private GridPane gridPane;
    @FXML
    private Button startButton;
    @FXML
    private Label numbersLabel;
    @FXML
    private Button SFXButton;
    @FXML
    private final Button[] buttonArray = new Button[36];

    /*
        @SFX
     */
    private final String strike_SFX = "file:src/main/resources/com/example/SFX/strike.wav";
    private final String gameOver_SFX = "file:src/main/resources/com/example/SFX/gameOver.wav";
    private final String gameStart_SFX = "file:src/main/resources/com/example/SFX/gameStart.wav";
    private final String buttonsCLick_SFX = "file:src/main/resources/com/example/SFX/buttonsClick.wav";
    private final String slotsClicked_SFX = "file:src/main/resources/com/example/SFX/slotsClicked.wav";
    private final String roundComplete_SFX = "file:src/main/resources/com/example/SFX/roundComplete.wav";

    private final AudioClip strike = new AudioClip(strike_SFX);
    private final AudioClip gameOver = new AudioClip(gameOver_SFX);
    private final AudioClip gameStart = new AudioClip(gameStart_SFX);
    private final AudioClip slotsCLicked = new AudioClip(slotsClicked_SFX);
    private final AudioClip buttonsClick = new AudioClip(buttonsCLick_SFX);
    private final AudioClip roundComplete = new AudioClip(roundComplete_SFX);

    public final String styleShow = "-fx-background-color: null; -fx-font-size: 35pt; -fx-padding: 0; -fx-text-alignment: center;" +
            " -fx-pref-height: 70; -fx-pref-width: 70; -fx-alignment: center; -fx-text-fill: white; ";

    public final String styleCovered = "-fx-background-color: white; -fx-font-size: 35pt; -fx-padding: 0; -fx-text-alignment: center;" +
            " -fx-pref-height: 70; -fx-pref-width: 70; -fx-alignment: center; -fx-text-fill: white; ";

    /*
        @FUNCTIONS
     */
    public void startGame(){

        if (SFX_On) gameStart.play();

        vbox.getChildren().clear();

        gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPrefSize(560, 560);
        gridPane.setAlignment(Pos.CENTER);
        vbox.getChildren().add(gridPane);

        createButtons();
        selectButtons();

        int timer;
        if (maxSlots > 20) timer = 4;
        else if (maxSlots > 10) timer = 3;
        else timer = 2;

        Timeline coverSlots = new Timeline(new KeyFrame(Duration.seconds(timer), e -> {

            for (int i = 0; i < slots.length; i++) {
                if (slots[i] != 0) buttonArray[i].setStyle(styleCovered);
            }
        }));

        coverSlots.play();
    }

    @FXML
    public void createButtons(){

        for (int ROW = 0; ROW < 6; ROW++) {
            for (int COLUMN = 0; COLUMN < 6; COLUMN++) {
                int number = 6 * ROW + COLUMN;

                Button button = new Button();

                button.setStyle(styleShow);
                button.setId(Integer.toString(number));
                button.setCursor(Cursor.HAND);
                button.setOnAction((ActionEvent e) -> {
                    int buttonID = Integer.parseInt(button.getId());

                    if (slots[buttonID] == enumeration){
                        enumeration++;
                        
                        if (enumeration != maxSlots + 1 && SFX_On) {
                            slotsCLicked.play();
                        }

                        buttonArray[buttonID].setDisable(true);
                        buttonArray[buttonID].setVisible(false);

                        if (enumeration == maxSlots + 1){
                            if (SFX_On) roundComplete.play();

                            ++maxSlots;
                            numbersLabel.setText(String.valueOf(maxSlots));

                            pausePhase();
                        }
                    } else {
                        ++strikes;

                        if (strikes != 3 && SFX_On) strike.play();
                        else if (strikes == 3 && SFX_On) gameOver.play();

                        lightStrike();
                        pausePhase();
                    }
                });
                
                gridPane.add(button, COLUMN, ROW);
                buttonArray[number] = button;
            }
        }
    }

    @FXML
    public void selectButtons(){

        Random rand = new Random();

        for (int i = 1; i <= maxSlots; i++){
            int random;
            do {
                random = rand.nextInt(36);
            } while (slots[random] != 0);

            slots[random] = i;
            buttonArray[random].setText(String.valueOf(i));
        }

        for (int i = 0; i < slots.length; i++){
            if (slots[i] == 0) buttonArray[i].setDisable(true);
        }
    }
    public void nextRound(){
        vbox.getChildren().clear();

        enumeration = 1;
        Arrays.fill(slots, 0);
        vbox.getChildren().remove(gridPane);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> startGame()));
        timeline.play();
    }

    public void restart(){
        vbox.getChildren().clear();

        strikes = 0;
        maxSlots = 5;
        enumeration = 1;
        Arrays.fill(slots, 0);
        numbersLabel.setText(String.valueOf(maxSlots));
        
        strike1.setFill(Color.BLACK);
        strike2.setFill(Color.BLACK);
        strike3.setFill(Color.BLACK);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> startGame()));
        timeline.play();
    }

    public void pausePhase(){
        vbox.getChildren().clear();

        String headerText;
        String leftBtnText;
        String rightBtnText;
        
        Label header = new Label();
        HBox hBox = new HBox(20);
        VBox p_vbox = new VBox(20);
        Button leftButton = new Button();
        Button rightButton = new Button();

        if (strikes != 3){
            rightBtnText = "Retry";
            leftBtnText = "Continue";
            headerText = "Numbers " + maxSlots;
            leftButton.setOnAction(event -> {
                if (SFX_On) buttonsClick.play();
                nextRound();
            });
            rightButton.setOnAction(event -> {
                if (SFX_On) buttonsClick.play();
                restart();
            });
        } else {
            rightBtnText = "Exit";
            leftBtnText = "Retry";
            headerText = "Game Over!";
            leftButton.setOnAction(event -> {
                if (SFX_On) buttonsClick.play();
                restart();
            });
            rightButton.setOnAction(event -> {
                if (SFX_On) buttonsClick.play();

                try {
                    exitButton();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        header.setText(headerText);
        header.setTextFill(Color.WHITE);
        header.setAlignment(Pos.CENTER);
        header.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));

        leftButton.setPrefHeight(40);
        leftButton.setPrefWidth(150);
        leftButton.setText(leftBtnText);
        leftButton.setCursor(Cursor.HAND);
        leftButton.setTextFill(Color.BLACK);
        leftButton.setStyle(startButton.getStyle());
        leftButton.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));

        rightButton.setPrefHeight(40);
        rightButton.setPrefWidth(150);
        rightButton.setText(rightBtnText);
        rightButton.setCursor(Cursor.HAND);
        rightButton.setTextFill(Color.WHITE);
        rightButton.setStyle(SFXButton.getStyle());
        rightButton.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));

        hBox.setAlignment(Pos.CENTER);
        p_vbox.setAlignment(Pos.CENTER);

        vbox.getChildren().add(p_vbox);
        p_vbox.getChildren().add(header);
        p_vbox.getChildren().add(hBox);
        hBox.getChildren().add(leftButton);
        hBox.getChildren().add(rightButton);
    }

    /*
        @BUTTONS
     */
    public void SFXButton() {
        Image SFX_ON_UI = new Image("file:src/main/resources/com/example/Images/SFX_ON.png");
        Image SFX_OFF_UI = new Image("file:src/main/resources/com/example/Images/SFX_OFF.png");
        ImageView ON = new ImageView(SFX_ON_UI);
        ImageView OFF = new ImageView(SFX_OFF_UI);

        ON.setFitHeight(20);
        ON.setFitWidth(20);

        OFF.setFitHeight(15);
        OFF.setFitWidth(17);

        if (SFX_On){
            buttonsClick.play();
            SFXButton.setGraphic(OFF);
            SFX_On = false;
        } else {
            SFXButton.setGraphic(ON);
            SFX_On = true;
        }
    }

    public void exitButton() throws IOException {
        if (SFX_On) buttonsClick.play();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("exitDialog.fxml"));
        DialogPane dialogPane = fxmlLoader.load();

        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> dialog.hide());

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:src/main/resources/com/example/Images/Icon.png"));

        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Chimp Test");
        dialog.showAndWait();
    }

    /*
        @STRIKE_INDICATOR
     */
    public void lightStrike(){
        Circle strikeIndicator = null;

        switch (strikes) {
            case 1 -> strikeIndicator = strike1;
            case 2 -> strikeIndicator = strike2;
            case 3 -> strikeIndicator = strike3;
        }

        FillTransition fillStrike = new FillTransition(Duration.millis(200), strikeIndicator);
        fillStrike.setFromValue(Color.BLACK);
        fillStrike.setToValue(Color.RED);
        fillStrike.play();
    }
}
