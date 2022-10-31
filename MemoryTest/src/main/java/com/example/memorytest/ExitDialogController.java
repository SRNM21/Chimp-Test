package com.example.memorytest;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class ExitDialogController {

    @FXML
    private Button dialogExitButton;
    @FXML
    private Button dialogCancelButton;

    TestController testController = new TestController();

    private boolean SFX_On = testController.SFX_On;
    public final String buttonsCLick_SFX = "file:src/main/resources/com/example/SFX/buttonsClick.wav";
    AudioClip buttonsClick = new AudioClip(buttonsCLick_SFX);

    public void confirmExit() {
        if (SFX_On){
            buttonsClick.play();
        }
        Stage stage = (Stage) dialogExitButton.getScene().getWindow();
        stage.close();
        Platform.exit();
    }

    public void cancelExit(){
        if (SFX_On){
            AudioClip buttonsClick = new AudioClip(buttonsCLick_SFX);
            buttonsClick.play();
        }
        Stage stage = (Stage) dialogCancelButton.getScene().getWindow();
        stage.close();
    }
}