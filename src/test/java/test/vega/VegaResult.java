package test.vega;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.json.JsonArray;
import net.sourceforge.plantuml.json.JsonObject;

public record VegaResult(Path path, VegaStatus status, long durationMs, Class<?> diagramClass, Throwable e, String tag,
		boolean allowFailure, String description) {

	public JsonObject toJsonObject() {
		final JsonObject entry = new JsonObject() //
				.add("file", path.toString().replace('\\', '/')) //
				.add("folder", path.getParent().toString().replace('\\', '/')) //
				.add("status", status.name().toLowerCase());
//				.add("duration_ms", durationMs);

		if (description != null)
			entry.add("description", description);

		if (tag != null)
			entry.add("tag", tag);

		if (allowFailure)
			entry.add("allow-failure", "true");

		if (diagramClass != null)
			entry.add("diagram_class", diagramClass.getSimpleName());

		if (e != null && e.getMessage() != null)
			entry.add("message", e.getMessage());

		if (e != null) {
			final StringWriter sw = new StringWriter();
			try (PrintWriter pw = new PrintWriter(sw)) {
				e.printStackTrace(pw);
			}

			final List<String> tmp = new ArrayList<>();
			int pos = 0;

			for (String s : sw.toString().split("\\R")) {
				if (s.startsWith("\tat "))
					s = s.substring(4);
				tmp.add(s);
				if (s.contains("test.vega."))
					pos = tmp.size();
			}

			final JsonArray stacktrace = new JsonArray();
			for (String line : tmp.subList(0, pos))
				stacktrace.add(line);

			entry.add("stacktrace", stacktrace);
		}
		return entry;
	}

}
