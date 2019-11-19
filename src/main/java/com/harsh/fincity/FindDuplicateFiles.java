package com.harsh.fincity;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

public class FindDuplicateFiles {
	final static Logger logger = Logger.getLogger(FindDuplicateFiles.class);
	static Map<String, String> orignalMap = new HashMap<String, String>();
	static Map<String, List<String>> dulpicateMap = new HashMap<String, List<String>>();
	static Map<String, DuplicateFilePath> resultOFDuplicateFilePath = new HashMap<String, DuplicateFilePath>();

	public static void main(String[] args) {
	//	int count = 0;
		FindDuplicateFiles obj = new FindDuplicateFiles();
		//fetch all the Drives name of you machine
		File[] drives = File.listRoots();
		if (drives != null && drives.length > 0) {
			for (File drive : drives) {
			//	count++;
			//	if (count == 1) {

				//} else {
					obj.listFilesAndFolders(drive.getPath());
					logger.info("drive name : " + drive.getPath());
				//}
				System.out.println("drive name : " + drive.getPath());
			}
		}
		// obj.listFilesAndFolders(drives[3].getPath());
		logger.debug("orignalMap :" + orignalMap);
		logger.debug("duplicate Map :" + dulpicateMap);
		logger.info("orignalMap size:" + orignalMap.size());
		logger.info("duplicate Map size :" + dulpicateMap.size());
		logger.info("resultOFDuplicateFilePath Map size :"
				+ resultOFDuplicateFilePath.size());

		/* print all the duplicate list of files */
		printDuplicatefile();
	}

	/**
	 * 
	 */
	public static void printDuplicatefile() {
		Set<Entry<String, DuplicateFilePath>> s = resultOFDuplicateFilePath
				.entrySet();

		Iterator<Entry<String, DuplicateFilePath>> it = s.iterator();
		while (it.hasNext()) {
			logger.info("##########################################################################");
			Entry<String, DuplicateFilePath> entry = it.next();
			logger.info("fileName : " + entry.getKey());
			logger.info("OriginalPath Of File : "
					+ entry.getValue().getOrgFilePath());
			logger.info("Duplicates Found :"
					+ entry.getValue().getDuplicateFilePath().size());
			entry.getValue().getDuplicateFilePath().forEach(logger::info);
			logger.info("##########################################################################");
		}
	}

	public void listFilesAndFolders(String directoryName) {
		File directory = new File(directoryName);
		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			logger.info(directoryName + file.getName());
			if (file.isDirectory()) {
				listNestedFlies(file.getAbsolutePath());
			}
			// System.out.println(file.getName());
		}

	}

	public void listNestedFlies(String filePath) {
		Path start = Paths.get(filePath);
		try (Stream<Path> stream = Files.walk(start, Integer.MAX_VALUE)) {
			List<String> collect = stream.map(String::valueOf).sorted()
					.collect(Collectors.toList());
			// collect.forEach(System.out::println);
			for (String string : collect) {
				DuplicateFilePath duplicateFilePath = new DuplicateFilePath();
				StringBuilder sb = new StringBuilder(string);
				String fileName = sb.substring(sb.lastIndexOf("\\") + 1);
				if (orignalMap.get(fileName) == null) {
					orignalMap.put(fileName, string);
				} else {
					if (dulpicateMap.containsKey(fileName)) {
						List<String> existingList = new ArrayList<String>();
						existingList = dulpicateMap.get(fileName);
						existingList.add(string);
						dulpicateMap.put(fileName, existingList);
						duplicateFilePath.setOrgFilePath(orignalMap
								.get(fileName));
						duplicateFilePath.setDuplicateFilePath(existingList);
					} else {
						List<String> dupList = new ArrayList<String>();
						dupList.add(string);
						dulpicateMap.put(fileName, dupList);
						duplicateFilePath.setOrgFilePath(orignalMap
								.get(fileName));
						duplicateFilePath.setDuplicateFilePath(dupList);
					}
					resultOFDuplicateFilePath.put(fileName, duplicateFilePath);
				}
			}
			collect.forEach(logger::debug);
		} catch (UncheckedIOException e) {
			System.out.println(e.getMessage());
			logger.error(e.getMessage());
		} catch (AccessDeniedException e) {
			System.out.println(e.getMessage());
			logger.error(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}

	}

}