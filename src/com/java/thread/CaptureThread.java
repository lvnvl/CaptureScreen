package com.java.thread;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;

import org.apache.xmlgraphics.image.codec.tiff.TIFFEncodeParam;

import com.github.jaiimageio.impl.common.ImageUtil;
import com.github.jaiimageio.plugins.tiff.TIFFField;
import com.github.jaiimageio.plugins.tiff.TIFFTag;

public class CaptureThread extends Thread{
	private Rectangle field;
	private boolean isRunning;
	private int currentCase;
	private String startTime;
	private int millseconds;

	public CaptureThread(Rectangle field, int timeval) {
		this.field = field;
		this.isRunning = false;
		this.currentCase = 0;
		this.millseconds = timeval;
		this.startTime = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
	}

	@Override
	public void run() {
		int i = 0;
		int j = 0;
		while (true) {
			i = 0;
			while (isRunning) {
				try {
					screenShotAsFile(field.x, field.y, field.width, field.height, 
							System.getProperty("user.dir") + File.separator 
							+ startTime + File.separator + currentCase, 
							String.valueOf(i ++), 
							"jpg");
					Thread.sleep(millseconds);
				} catch (InterruptedException e) {
					System.out.println(i);
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(1000);
//				System.out.println(j ++);
			} catch (InterruptedException e) {
				System.out.println(j ++);
				e.printStackTrace();
			}
		}
	}
	
	public void pause() {
		if (isRunning) {
			setRunning(false);
		}
	}
	
	public void play() {
		if (!isRunning) {
			currentCase ++;
			setRunning(true);
		}
	}

    /**
     * 指定屏幕区域截图，保存到指定目录
     * @param x
     * @param y
     * @param width
     * @param height
     * @param savePath - 文件保存路径
     * @param fileName - 文件保存名称
     * @param format - 文件格式
     */
    public void screenShotAsFile(int x, int y, int width, int height, String savePath, String fileName, String format) {
        try {
            Robot robot = new Robot();
            BufferedImage bfImage = robot.createScreenCapture(new Rectangle(x, y, width, height));
            File path = new File(savePath);
            File file = new File(path, fileName+ "." + format);
            if (!path.exists()) {
            	path.mkdirs();
            }
            if (!file.exists()) {
				file.createNewFile();
			}
            com.java.main.ImageUtil.resize(bfImage, 3, 1.0f, file);
//            ImageIO.write(bfImage, format, file);
        } catch (AWTException e) {
            e.printStackTrace();    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public Rectangle getField() {
		return field;
	}

	public void setField(Rectangle field) {
		this.field = field;
	}

}
