package net.sourceforge.plantuml.preproc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stdlib {

	private final String name;
	private final Map<String, String> info = new HashMap<String, String>();

	public static InputStream getResourceAsStream(String fullname) {
		fullname = fullname.replace(".puml", "");
		fullname = fullname.replace("awslib/", "awslib14/");

		final String fullpath = "/app/stdlib/" + fullname + ".puml";
		System.err.println("Trying to read " + fullpath);
		// See https://docs.leaningtech.com/cheerpj/File-System-support
		try {
			return new FileInputStream(fullpath);
		} catch (FileNotFoundException e) {
			System.err.println("Cannot load " + fullpath);
			return null;
		}
		// return getInternalInputStream(fullname, ".puml");
	}

	private Stdlib(String name, String info) throws IOException {
		this.name = name;
		fillMap(info);
	}

	private void fillMap(String infoString) {
		for (String s : infoString.split("\n"))
			if (s.contains("=")) {
				final String data[] = s.split("=");
				this.info.put(data[0], data[1]);
			}
	}

	private static InputStream getInternalInputStream(String fullname, String extension) {
		final String res = "/stdlib/" + fullname + extension;
		return Stdlib.class.getResourceAsStream(res);
	}

	public static void extractStdLib() throws IOException {
	}

	public List<String> extractAllSprites() throws IOException {
		final List<String> result = new ArrayList<>();
		return result;
	}

	public static void addInfoVersion(List<String> strings, boolean details) {
	}

	public static void printStdLib() {

	}
}
