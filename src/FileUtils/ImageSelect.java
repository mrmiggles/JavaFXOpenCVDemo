/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileUtils;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author r299490
 */
public class ImageSelect {
    
    public static File GUI(ActionEvent event, String windowTitle) {
        FileChooser fileChooser = new FileChooser();
       fileChooser.setTitle(windowTitle);
       fileChooser.getExtensionFilters().addAll(
            //new ExtensionFilter("Text Files", "*.txt"),
             //new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
             new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
             new FileChooser.ExtensionFilter("All Files", "*.*"));
       Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
       File selectedFile = fileChooser.showOpenDialog(app_stage);
       return selectedFile;       
        
    }
 }
