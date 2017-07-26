/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package offlineapp;

import Detectors.FREAK;
import FileUtils.ImageSelect;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;

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
    @FXML private Button detectObjButton;
    @FXML private ImageView sceneImageView;
    @FXML private ImageView objImageView;
    @FXML private ImageView resultsImage;
    
    @FXML protected void detectObjectHandler(ActionEvent event){
        if(sceneImageView.getImage() == null || objImageView.getImage() == null){
            textlog.appendText("You must select both a scene and an object");
        } else {
            
            try {
                FREAK freak = new FREAK();
                freak.useSURFDetector();
                
                //set obj keypoints and descriptors
                freak.setDescriptorsAndKeyPoints(objImageView.getImage());
                MatOfKeyPoint objDescriptors = freak.getDescriptors();
                MatOfKeyPoint objKeyPoints = freak.getKeyPoints();
                Mat objImage = freak.getObjImage();
                
                //set scene keypoints and descriptors
                freak.setDescriptorsAndKeyPoints(sceneImageView.getImage());
                MatOfKeyPoint sceneDescriptors = freak.getDescriptors();
                MatOfKeyPoint sceneKeyPoints = freak.getKeyPoints();
                Mat sceneImage = freak.getObjImage();
                
                
                boolean found = freak.matchDescriptors(objDescriptors, sceneDescriptors);
                if(found){
                    textlog.appendText("Match found" + newline);
                    textlog.appendText("Saving image as 'matches.jpg'" + newline);
                    freak.drawMatchLines(objKeyPoints, sceneKeyPoints, objImage, sceneImage);
                    File resultImageFile = new File("matches.jpg");
                    if(resultImageFile.exists()) resultsImage.setImage(new Image(resultImageFile.toURI().toString()));
                } else {
                    textlog.appendText("Match NOT found"+ newline);
                }
            } catch (IOException ex) {
                //Logger.getLogger(ComparisonGUI.class.getName()).log(Level.SEVERE, null, ex);
                textlog.appendText("Could not compare images");
            }             
        }                  
    }
    
    @FXML protected void selectImage2Handler(ActionEvent event){
        File selectedFile = ImageSelect.GUI(event, "Select Image");
       if (selectedFile != null) setImageIcon(selectedFile, event.getSource());       
    }
    
    @FXML protected void selectImage1Handler(ActionEvent event){
       File selectedFile = ImageSelect.GUI(event, "Select Image");
       if (selectedFile != null) setImageIcon(selectedFile, event.getSource());       
    }
        
    private void setImageIcon(File file, Object eventSource){
        
        try{
            BufferedImage image = ImageIO.read(file);
            if (image == null) {
                textlog.appendText("The file " + file.getName() + " could not be opened , it is not an image"+ newline);
            } else {
                //This is where a real application would open the file.
               textlog.appendText("Opening: " + file.getAbsolutePath() + "." + newline); 
               if(eventSource.equals(sceneSelector)){
                  sceneImageView.setImage(new Image(file.toURI().toString()));
                  textlog.appendText("Scene set as: " + file.getName() + newline);
               } else if(eventSource.equals(objectSelector)){
                   objImageView.setImage(new Image(file.toURI().toString()));
                  textlog.appendText("Object set as: " + file.getName() + newline);
               }
               image.flush();               
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
