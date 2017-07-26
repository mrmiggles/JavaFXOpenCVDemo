/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Detectors;

/**
 *
 * @author r299490
 */
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.imgcodecs.Imgcodecs;

import utils.StreamReader;

public class FREAK extends utilis {


    private MatOfKeyPoint keyPoints;
    private MatOfKeyPoint objectDescriptors;
    private Mat objImage;

    public FREAK(){		

    }

    public void setDescriptorsAndKeyPoints(String imgPath) throws IOException{
        objImage = Imgcodecs.imread(imgPath, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
        
        keyPoints = new MatOfKeyPoint();
        objectDescriptors = new MatOfKeyPoint();
        System.out.println("Detecting key points...");

        featureDetector.detect(objImage, keyPoints);		
        descriptorExtractor.compute(objImage, keyPoints, objectDescriptors);		
        //return objectDescriptors;
    }

    /**
     * 
     * @param image -- javafx.scene.image
     * @return
     * @throws IOException 
     */
    public void setDescriptorsAndKeyPoints(Image image) throws IOException {

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", byteOutput);
        setDescriptorsAndKeyPoints(byteOutput.toByteArray());

        //return objectDescriptors;
    }
    /**
     * Takes InputStream and returns
     * objectDescriptors about that image
     * @param is
     * @return
     * @throws IOException
     */

    public void setDescriptorsAndKeyPoints(InputStream is) throws IOException{
        // Read into byte-array
        byte[] temporaryImageInMemory = StreamReader.readStream(is);
        setDescriptorsAndKeyPoints(temporaryImageInMemory);
        //return objectDescriptors;
    }
    
    private void setDescriptorsAndKeyPoints(byte[] image){
        objImage = Imgcodecs.imdecode(new MatOfByte(image), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
	
        objectDescriptors = new MatOfKeyPoint();
        keyPoints = new MatOfKeyPoint();
        
        System.out.println("Detecting key points...");
        featureDetector.detect(objImage, keyPoints);		
        descriptorExtractor.compute(objImage, keyPoints, objectDescriptors);        
    }
    
    public MatOfKeyPoint getDescriptors(){
        return this.objectDescriptors;
    }
    
    public MatOfKeyPoint getKeyPoints(){
        return this.keyPoints;
    }
    
    public Mat getObjImage(){
        return this.objImage;
    }
}