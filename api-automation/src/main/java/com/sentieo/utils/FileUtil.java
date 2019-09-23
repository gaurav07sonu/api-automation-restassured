package com.sentieo.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.sentieo.constants.*;

public class FileUtil {
	public File getFileFromResources(String fileName) throws MalformedURLException {
		// ClassLoader classLoader = getClass().getClassLoader();
		URL resource = new File(Constants.RESOURCE_PATH + File.separator + fileName).toURI().toURL();
		// URL resource = classLoader.getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("file is not found!");
		} else {
			return new File(resource.getFile());
		}

	}

	public StringBuffer getFileContent(File file) throws IOException {
		StringBuffer sb = new StringBuffer();
		if (file == null)
			return null;
		try (FileReader reader = new FileReader(file); BufferedReader br = new BufferedReader(reader)) {

			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		}
		return sb;
	}

	public static void main(String[] args) {
		try {
			String testNGFileName = args[0];
			replaceGradleValue(testNGFileName);

			String env = args[1];
			replaceEnvValue(env);

			String userName = args[2];
			replaceUserValue(userName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void replaceGradleValue(String testNGFileName) {
		try {
			Path path = Paths.get("../../../build.gradle");
			Stream<String> lines = Files.lines(path);
			List<String> replaced = lines.map(line -> line.replaceAll("testNG.xml", testNGFileName))
					.collect(Collectors.toList());
			Files.write(path, replaced);
			lines.close();
			System.out.println("Find and Replace done!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void replaceEnvValue(String env) {
		try {
			Path path = Paths.get("com/sentieo/constants/Constants.java");
			Stream<String> lines = Files.lines(path);
			List<String> replaced = lines.map(line -> line.replaceAll("app.sentieo.com", env + ".sentieo.com"))
					.collect(Collectors.toList());
			Files.write(path, replaced);
			lines.close();
			System.out.println("Find and Replace done!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void replaceUserValue(String uname) {
		try {
			Path path = Paths.get("com/sentieo/constants/Constants.java");
			Stream<String> lines = Files.lines(path);
			List<String> replaced = lines.map(line -> line.replaceAll("alphagani35@gmail.com", uname))
					.collect(Collectors.toList());
			Files.write(path, replaced);
			lines.close();
			System.out.println("Find and Replace done!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
