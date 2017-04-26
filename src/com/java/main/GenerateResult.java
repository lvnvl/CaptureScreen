package com.java.main;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.java.ocr.OCRTest;

public class GenerateResult {

	public GenerateResult() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		CommandLineParser parser = new BasicParser();
		Options options = new Options();
		options.addOption("h", "help", false, "用法。 这个程序从截图的文件夹中识别最大值。\n"
				+ "\t-a 左上x,左上y,宽,高。指定识别的数据区域\n"
				+ "\t-n n 指标名称\n"
				+ "\r-d dir。截图所在的文件夹。需要指定到日期文件夹的绝对路径。\n"
				+ "\tsource code: https://github.com/v73alice/VmstatToChart.git");
		options.addOption("a", true, "截图区域,左上x,左上y,宽,高。指定识别的数据区域。");
		options.addOption("d", true, "dir。截图所在的文件夹。需要指定到日期文件夹的绝对路径。");
		options.addOption("n", true, "name,指标名称。");
		CommandLine commandLine = null;
		try {
			commandLine = parser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (commandLine.hasOption('h')) {
			HelpFormatter hfFormatter = new HelpFormatter();
			hfFormatter.printHelp("ocr", options, true);
			System.exit(0);
		}
		if (!commandLine.hasOption('n')) {
			System.err.println("error args!");
			HelpFormatter hfFormatter = new HelpFormatter();
			hfFormatter.printHelp("ocr", options, true);
			System.exit(0);
		}
		if (! commandLine.hasOption('a')) {
			System.err.println("error args!");
			HelpFormatter hfFormatter = new HelpFormatter();
			hfFormatter.printHelp("处理数据", options, true);
			System.exit(0);
		}
		String name = commandLine.getOptionValue("n");
		Main main = new Main();
		Rectangle rectangle = main.toRectangle(commandLine.getOptionValue("a"));
		if (rectangle == null) {
			System.out.println("截图参数错误，注意逗号用英文逗号。");
			System.exit(0);
		}
		
		String dirPath = commandLine.getOptionValue("d"); // d:/20170421234035
		File dir = new File(dirPath);
		if ((! dir.exists()) || dir.isFile()) {
			System.out.println("-d dir  截图所在的文件夹。需要指定到日期文件夹的绝对路径。");
			System.exit(0);
		}
		File[] caseDirs = dir.listFiles();
		File ocrResult = new File(dir, "ocr_" + name + ".txt");
		if (ocrResult.exists()) {
			ocrResult.delete();
		}
		ocrResult.createNewFile();
		FileWriter ocrWriter = new FileWriter(ocrResult);
		for (int i = 1; i <= caseDirs.length; i++) {
			File caseDir = new File(dir, String.valueOf(i)); // d:/20170421234035/1
			if (!caseDir.exists()) {
				continue;
			}
			File[] imgFiles = caseDir.listFiles();
			int size = imgFiles.length;         // TODO Null pointer
			float max = 0.0f;
			File caseResult = new File(dir, String.valueOf(i) + "_" + name + ".txt");
			if (caseResult.exists()) {
				caseResult.delete();
			}
			caseResult.createNewFile();
			FileWriter writer = new FileWriter(caseResult);
			for (int j = 0; j < size; j ++) {
				File img = new File(caseDir, j + ".jpg");
				ImageUtil.toBinary(img);
				File imgBinary = new File(caseDir, j + ".jpg_binary.jpg");
				float ocrValue = OCRTest.getFloatValue(imgBinary, rectangle);
				writer.write(String.valueOf(ocrValue) + System.getProperty("line.separator"));
				if (ocrValue > max) {
					max = ocrValue;
				}
				
				// generate binary img
//				ImageUtil.toBinary(img);
//				img.delete();
				// generate binary img
//				ImageUtil.toGray(img);
			}
			writer.close();
			ocrWriter.write(String.valueOf(max) + System.getProperty("line.separator"));
			System.out.println(i + ":  " + max);
		}
		ocrWriter.close();
		System.out.println("Done.");
	}

}
