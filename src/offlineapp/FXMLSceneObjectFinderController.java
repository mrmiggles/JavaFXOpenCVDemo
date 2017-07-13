/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package offlineapp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author 299490
 */
public class FXMLSceneObjectFinderController implements Initializable {

    private static final String newline = "\n";
    @FXML private TextArea textlog;
    @FXML private Button sceneSelector;
    @FXML private Button objectSelector;
    
    public FXMLSceneObjectFinderController(){
        textlog = new TextArea();
    }
    
    @FXML protected void selectImage2Handler(ActionEvent event){
        File selectedFile = selectFile(event);
       if (selectedFile != null) setImageIcon(selectedFile, event.getSource());       
    }
    
    @FXML protected void selectImage1Handler(ActionEvent event){
       File selectedFile = selectFile(event);
       if (selectedFile != null) setImageIcon(selectedFile, event.getSource());       
    }
    
    private File selectFile(ActionEvent event){
       FileChooser fileChooser = new FileChooser();
       fileChooser.setTitle("Open Resource File");
       fileChooser.getExtensionFilters().addAll(
            //new ExtensionFilter("Text Files", "*.txt"),
             //new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
             new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
             new ExtensionFilter("All Files", "*.*"));
       Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
       File selectedFile = fileChooser.showOpenDialog(app_stage);
       return selectedFile;
    }
    
    private void setImageIcon(File file, Object eventSource){
        
        try{
            BufferedImage image = ImageIO.read(new File(file.getAbsolutePath()));
            if (image == null) {
                textlog.appendText("The file " + file.getName() + " could not be opened , it is not an image"+ newline);
            } else {
                //This is where a real application would open the file.
               textlog.appendText("Opening: " + file.getName() + "." + newline); 
               if(eventSource.equals(sceneSelector)){
                   textlog.appendText("Scene Selector pressed" + newline);
               } else if(eventSource.equals(objectSelector)){
                   textlog.appendText("Object Selector pressed" + newline);
               }
               
            }            
        } catch (IOException ex) {
            textlog.appendText("The file " + file.getName() + " could not be opened , an error occurred."+newline);
        }
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
