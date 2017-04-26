package com.java.ocr;
import java.awt.Rectangle;
import java.io.File;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

public class OCRTest {

	public static float getFloatValue(File imgFile, Rectangle rectangle) {
		ITesseract instance = new Tesseract();
		File tessDataFolder = new File(System.getenv("TESSDATA_PREFIX") + File.separator + "tessdata");
		instance.setDatapath(tessDataFolder.getAbsolutePath());
		instance.setLanguage("digtal");
		String result = "0.0";
		float val = 0.0f;
		try {
			result = instance.doOCR(imgFile, rectangle);
			result = result.trim();
			result = result.replace(" ", "");
//			System.out.println("ocr:-" + result + "-");
			val = Float.valueOf(result);
		} catch (TesseractException e) {
//			System.out.println("ocr result is: -" + result + "-" + result.length());
//			e.printStackTrace();
		} catch (Exception e) {
			val = 0.0f;
		}
		if ("".equals(result)) {
			return 0f;
		}
//		System.out.println("--" + result + "--");
		
		return val;
	}

	public static void main(String[] args) {
//		File imageFile = new File("0.png");
		ITesseract instance = new Tesseract();
		File tessDataFolder = new File(System.getenv("TESSDATA_PREFIX") + File.separator + "tessdata");
		instance.setDatapath(tessDataFolder.getAbsolutePath());
		System.out.println(tessDataFolder.getAbsolutePath());
		try {
//			String result = instance.doOCR(imageFile);
//			System.out.println(result);
//			System.out.println(instance.doOCR(imageFile, new Rectangle(0, 0, 70, 63)));
//			System.out.println(instance.doOCR(imageFile, new Rectangle(82, 0, 70, 63)));
//			System.out.println(instance.doOCR(imageFile, new Rectangle(155, 0, 60, 63)));
			File floatImg = new File("14.jpg");
			instance.setLanguage("digtal");
			String result = instance.doOCR(floatImg);
			
			System.out.println("ocr result is: -" + result + "-" + result.length());
		} catch (TesseractException e) {
			e.printStackTrace();
		}
	}

}
