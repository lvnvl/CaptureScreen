package com.java.main;

import java.awt.Rectangle;
import java.util.Scanner;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.java.thread.CaptureThread;

public class Main {

	public Main() {
	}

	public Rectangle toRectangle(String area) {
		String[] params = area.split(",");
		if (params.length != 4) {
			return null;
		}
		return new Rectangle(Integer.valueOf(params[0]),
				Integer.valueOf(params[1]),
				Integer.valueOf(params[2]),
				Integer.valueOf(params[3]));
	}
	
	public static void main(String[] args) {
		Main main = new Main();
		CommandLineParser parser = new BasicParser();
		Options options = new Options();
		options.addOption("h", "help", false, "用法。 这个程序用来给 给定的区域 截图。\n"
				+ "\t-a 左上x,左上y,宽,高。\n"
				+ "\t-t 毫秒。截图的时间间隔。\n"
				+ "\t运行中控制说明：输入p,暂停；输入r,运行。\n "
				+ "\tsource code: https://github.com/v73alice/VmstatToChart.git");
		options.addOption("a", true, "截图区域,左上x,左上y,宽,高。");
		options.addOption("t", true, "截图间隔,毫秒");
		CommandLine commandLine = null;
		try {
			commandLine = parser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (commandLine.hasOption('h')) {
			HelpFormatter hfFormatter = new HelpFormatter();
			hfFormatter.printHelp("截屏", options, true);
			System.exit(0);
		}
		if (! commandLine.hasOption('a')) {
			System.err.println("error args!");
			HelpFormatter hfFormatter = new HelpFormatter();
			hfFormatter.printHelp("ToChart", options, true);
			System.exit(0);
		}
		int timeval = 0;
		timeval = Integer.valueOf(commandLine.getOptionValue("t", "300"));
		Rectangle area = main.toRectangle(commandLine.getOptionValue("a"));
		if (area == null) {
			System.out.println("截图参数错误，注意逗号用英文逗号。");
			System.exit(0);
		}
		System.out.println("初始化完成!");
		CaptureThread capture = new CaptureThread(area, timeval);
		capture.start();
		String input = "";
		Scanner scanner = new Scanner(System.in);
		int i = 1;
		while (! "s".equals(input)){
			if (capture.isRunning()) {
				System.out.println("截屏线程正在运行中。。。。。\n"
						+ "输入p暂停截屏，结束第 "+ (i - 1) +" 条用例：");
			} else {
				System.out.println("截屏线程已经暂停。。。。。\n"
						+ "输入r开始截屏，开始第 "+ i ++ +" 条用例：");
			}
			input = scanner.nextLine();
			if ("p".equals(input)) {
				capture.pause();
				System.out.println("截屏线程暂停成功。。。\n");
			}
			if ("r".equals(input)) {
				capture.play();
				System.out.println("截屏线程启动成功。。。。。\n");
			}
		}
		capture.stop();
		scanner.close();
	}

}
