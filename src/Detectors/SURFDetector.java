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

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.DMatch;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.Feature2D;
import org.opencv.imgcodecs.Imgcodecs;

import utils.StreamReader;

public class SURFDetector {
	
	private Feature2D featureDetector;
	private Feature2D descriptorExtractor;

	
	public SURFDetector(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Starting...");
        descriptorExtractor = org.opencv.xfeatures2d.SURF.create(100, 2,4,true, false);
        featureDetector = org.opencv.xfeatures2d.SURF.create();
	}
	
	public MatOfKeyPoint getKeyPointsofImage(String imgPath){
		Mat objectImage = Imgcodecs.imread(imgPath, Imgcodecs.CV_LOAD_IMAGE_COLOR);
		MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
        System.out.println("Detecting key points...");
        featureDetector.detect(objectImage, objectKeyPoints);
        
        return objectKeyPoints;
	}
	public MatOfKeyPoint getKeyPointsofImage(InputStream inputStream) throws IOException{
		 // Read into byte-array
	    byte[] temporaryImageInMemory = StreamReader.readStream(inputStream);
		Mat objectImage = Imgcodecs.imdecode(new MatOfByte(temporaryImageInMemory), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
        MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
        
        System.out.println("Detecting key points...");
        featureDetector.detect(objectImage, objectKeyPoints);
        
        return objectKeyPoints;
	}
	
	public MatOfKeyPoint getDescriptorsofImage(String imgPath) throws IOException{
		Mat objectImage = Imgcodecs.imread(imgPath, Imgcodecs.CV_LOAD_IMAGE_COLOR);
		
		MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
		MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
		
        System.out.println("Detecting key points...");
        featureDetector.detect(objectImage, objectKeyPoints);
		
		descriptorExtractor.compute(objectImage, objectKeyPoints, objectDescriptors);
		return objectDescriptors;
		
	}
	
	/**
	 * Takes InputStream and returns
	 * objectDescriptors about that image
	 * @param is
	 * @return
	 * @throws IOException
	 */
	
	public MatOfKeyPoint getDescriptorsofImage(InputStream is) throws IOException{
		 // Read into byte-array
	    byte[] temporaryImageInMemory = StreamReader.readStream(is);
		Mat objectImage = Imgcodecs.imdecode(new MatOfByte(temporaryImageInMemory), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
		
        MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();		
		MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
		
        System.out.println("Detecting key points...");
        featureDetector.detect(objectImage, objectKeyPoints);		
		
		descriptorExtractor.compute(objectImage, objectKeyPoints, objectDescriptors);
		return objectDescriptors;
		
	}
	
	/**
	 * Function takes Descriptors and determines if any object in
	 * objectDescriptors exists in sceneDescriptors
	 * if a reasonable number of matches are found return true
	 * 
	 * @param objectDescriptors
	 * @param sceneDescriptors
	 * @return
	 */
	
	public boolean compareImageDescriptors(MatOfKeyPoint objectDescriptors , MatOfKeyPoint sceneDescriptors){
		
		List<MatOfDMatch> matches = new LinkedList<MatOfDMatch>();
        DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
        
        System.out.println("Matching object and scene images...");
        descriptorMatcher.knnMatch(objectDescriptors, sceneDescriptors, matches, 2);

        System.out.println("Calculating good match list...");
        LinkedList<DMatch> goodMatchesList = new LinkedList<DMatch>();

        float nndrRatio = 0.7f;

        for (int i = 0; i < matches.size(); i++) {
            MatOfDMatch matofDMatch = matches.get(i);
            DMatch[] dmatcharray = matofDMatch.toArray();
            DMatch m1 = dmatcharray[0];
            DMatch m2 = dmatcharray[1];

            if (m1.distance <= m2.distance * nndrRatio) {
                goodMatchesList.addLast(m1);

            }
        }
        
        if (goodMatchesList.size() >= 7) {
            return true;
        }
        
        return false;
	}
	
	private void printKeypoints(MatOfKeyPoint objectKeyPoints ){
        KeyPoint[] keypoints = objectKeyPoints.toArray();
        
        for(int i=0; i<keypoints.length; i++){
        	System.out.println(keypoints[i]);
        }		
	}
		
}