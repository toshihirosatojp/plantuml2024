package net.sourceforge.plantuml.preproc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Stdlib {

	private final String name;
	private final Map<String, String> info = new HashMap<String, String>();

	public static InputStream getResourceAsStream(String fullname) {
		fullname = fullname.replace(".puml", "");
		fullname = fullname.replace("awslib/", "awslib14/");

		return getInternalInputStream(fullname, ".puml");
	}

//	public static Stdlib retrieve(final String name) throws IOException {
//		Stdlib result = all.get(name);
//		if (result == null) {
//			final DataInputStream dataStream = getDataStream(name);
//			if (dataStream == null)
//				return null;
//
//			final String info = dataStream.readUTF();
//			dataStream.close();
//
//			final String link = getLinkFromInfo(info);
//			if (link == null)
//				result = new Stdlib(name, info);
//			else
//				result = retrieve(link);
//
//			all.put(name, result);
//		}
//		return result;
//	}

	private static String getLinkFromInfo(String infoString) {
		for (String s : infoString.split("\n"))
			if (s.contains("=")) {
				final String data[] = s.split("=");
				if (data[0].equals("LINK"))
					return data[1];
			}
		return null;
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

	private static Collection<String> getAll() throws IOException {
		final Set<String> result = new TreeSet<>();
		final InputStream home = getInternalInputStream("home", ".repx");
		final BufferedReader br = new BufferedReader(new InputStreamReader(home));
		String name;
		while ((name = br.readLine()) != null)
			result.add(name);

		return Collections.unmodifiableCollection(result);
	}

	public List<String> extractAllSprites() throws IOException {
		final List<String> result = new ArrayList<>();
		return result;
	}

	public static void addInfoVersion(List<String> strings, boolean details) {
//		try {
//			for (String name : getAll()) {
//				final Stdlib folder = Stdlib.retrieve(name);
//				if (details) {
//					strings.add("<b>" + name);
//					strings.add("Version " + folder.getVersion());
//					strings.add("Delivered by " + folder.getSource());
//					strings.add(" ");
//				} else {
//					strings.add("* " + name + " (Version " + folder.getVersion() + ")");
//				}
//			}
//		} catch (IOException e) {
//			Log.error("Error " + e);
//			return;
//		}
	}

	private String getVersion() {
		return info.get("VERSION");
	}

	private String getSource() {
		return info.get("SOURCE");
	}

	public static void printStdLib() {
		final List<String> print = new ArrayList<>();
		addInfoVersion(print, true);
		for (String s : print)
			System.out.println(s.replace("<b>", ""));

	}
}
