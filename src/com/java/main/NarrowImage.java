package com.java.main;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import org.w3c.dom.Element;

public class NarrowImage {

	
	public BufferedImage zoomImage(String src, float resizeTimes) {
		
		BufferedImage result = null;

		try {
			File srcfile = new File(src);
			if (!srcfile.exists()) {
				System.out.println("文件不存在");
				
			}
			BufferedImage im = ImageIO.read(srcfile);

			/* 原始图像的宽度和高度 */
			int width = im.getWidth();
			int height = im.getHeight();
			System.out.println(width);
			
			/* 调整后的图片的宽度和高度 */
			int toWidth = (int) (width * resizeTimes);
			int toHeight = (int) (height * resizeTimes);

			/* 新生成结果图片 */
			result = new BufferedImage(toWidth, toHeight,
					BufferedImage.TYPE_INT_RGB);

			result.getGraphics().drawImage(
					im.getScaledInstance(toWidth, toHeight,
							java.awt.Image.SCALE_SMOOTH), 0, 0, null);
			

		} catch (Exception e) {
			System.out.println("创建缩略图发生异常" + e.getMessage());
		}
		
		return result;

	}
	
	 public boolean writeHighQuality(BufferedImage im, String fileFullPath, Integer dpi) {
	        try {
	            /*输出到文件流*/
	            FileOutputStream newimage = new FileOutputStream(fileFullPath);
	            ImageWriter imageWriter = ImageIO.getImageWritersBySuffix("jpg").next();
	            ImageOutputStream ioStream = ImageIO.createImageOutputStream(newimage);
	            imageWriter.setOutput(ioStream);
	            
	            IIOMetadata imageMetaData = imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(im), null);
	            if (dpi != null && !dpi.equals("")) {
					Element tree = (Element) imageMetaData.getAsTree("javax_imageio_jpeg_image_1.0");
					Element jfif = (Element) tree.getElementsByTagName("app0JFI").item(0);
					jfif.setAttribute("Xdensity", Integer.toString(dpi));
					jfif.setAttribute("Ydensity", Integer.toString(dpi));
				}
	            
	            
	            JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter.getDefaultWriteParam();
	            jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
	            jpegParams.setCompressionQuality(0.9f);
	            imageWriter.write(imageMetaData, new IIOImage((RenderedImage) newimage, null, null), null);
	            ioStream.close();
	            imageWriter.dispose();
	            newimage.close();
	            return true;
	        } catch (Exception e) {
	            return false;
	        }
	    }
	 
	 
	 public static void main(String[] args) {
		 
		 String inputFoler = "C:\\Users\\Administrator\\Desktop\\1\\1.png" ; 
         /*这儿填写你存放要缩小图片的文件夹全地址*/
        String outputFolder = "C:\\Users\\Administrator\\Desktop\\1\\1.jpg";  
        /*这儿填写你转化后的图片存放的文件夹*/
      
		 
		 NarrowImage narrowImage = new NarrowImage();
		 narrowImage.writeHighQuality(narrowImage.zoomImage(inputFoler, 0.2f), outputFolder, 300);
		
	}

}
