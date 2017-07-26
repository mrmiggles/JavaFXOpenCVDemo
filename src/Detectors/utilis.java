/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Detectors;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.Feature2D;
import org.opencv.core.MatOfPoint2f;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author r299490
 */
public class utilis {
    protected Feature2D featureDetector;
    protected Feature2D descriptorExtractor;
    private LinkedList<MatOfPoint2f> obj;
    private LinkedList<MatOfPoint2f> scene;
    private LinkedList<DMatch> good_matches = new LinkedList<>();
    
    
    public utilis(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Starting OpenCV...");
    }
    
    public void useSURFDetector(){
        featureDetector = org.opencv.xfeatures2d.SURF.create(100, 2,4,true, false);
        descriptorExtractor = org.opencv.xfeatures2d.FREAK.create();		
    }  
    
    public boolean matchDescriptors(MatOfKeyPoint objectDescriptors , MatOfKeyPoint sceneDescriptors){
     if(objectDescriptors.empty() || sceneDescriptors.empty()) {
         System.out.println("Descriptors are null");
         return false;
     }
     DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);	
     List<MatOfDMatch> matches = new LinkedList<>();
     

     System.out.println("Checking if object exists in the scene..");
     descriptorMatcher.knnMatch(objectDescriptors, sceneDescriptors, matches, 2);

     //System.out.println("Calculating good match list...");

     float nndrRatio = 0.7f;

     for(int i = 0; i < matches.size(); i++) {
         MatOfDMatch matofDMatch = matches.get(i);
         DMatch[] dmatcharray = matofDMatch.toArray();
         DMatch m1 = dmatcharray[0];
         DMatch m2 = dmatcharray[1];

         if (m1.distance <= m2.distance * nndrRatio) {
             //good_matches.addLast(m1);
             good_matches.add(m1);
         }
     }



     if(good_matches.size() >= 7) {
      return true;
     }		

     return false;
    }
    
    /**
     * Draw line between features on each image 
     * and write out result on each image
     * 
     * @param keypointsObj
     * @param keypointsScene
     * @param imgObj
     * @param imgScene 
     */
    public void drawMatchLines(MatOfKeyPoint keypointsObj, MatOfKeyPoint keypointsScene,
            Mat imgObj, Mat imgScene){
        
        Mat outputImg = new Mat();
        MatOfDMatch gmatches = new MatOfDMatch();
        gmatches.fromList(good_matches);
        Features2d.drawMatches(imgObj, keypointsObj, imgScene, keypointsScene, gmatches, outputImg);
        Imgcodecs.imwrite("matches.jpg", outputImg);
        
    }
    
}
