package net.sourceforge.plantuml.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.EnumSet;

import org.junit.jupiter.api.Test;

class DiagramTypeTest {

	// --- Basic diagram type detection ---

	@Test
	void test_startuml_returns_all_legacy_types() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startuml");
		assertEquals(
				EnumSet.of(DiagramType.SEQUENCE, DiagramType.STATE, DiagramType.CLASS, DiagramType.OBJECT,
						DiagramType.ACTIVITY, DiagramType.DESCRIPTION, DiagramType.COMPOSITE, DiagramType.TIMING,
						DiagramType.HELP, DiagramType.SPRITES),
				result);
	}

	@Test
	void test_startgantt() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startgantt");
		assertEquals(EnumSet.of(DiagramType.GANTT), result);
	}

	@Test
	void test_startjson() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startjson");
		assertEquals(EnumSet.of(DiagramType.JSON), result);
	}

	@Test
	void test_startyaml() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startyaml");
		assertEquals(EnumSet.of(DiagramType.YAML), result);
	}

	@Test
	void test_startmindmap() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startmindmap");
		assertEquals(EnumSet.of(DiagramType.MINDMAP), result);
	}

	@Test
	void test_startwbs() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startwbs");
		assertEquals(EnumSet.of(DiagramType.WBS), result);
	}

	@Test
	void test_startditaa() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startditaa");
		assertEquals(EnumSet.of(DiagramType.DITAA), result);
	}

	@Test
	void test_startdot() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startdot");
		assertEquals(EnumSet.of(DiagramType.DOT), result);
	}

	@Test
	void test_startsalt() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startsalt");
		assertEquals(EnumSet.of(DiagramType.SALT), result);
	}

	@Test
	void test_startmath() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startmath");
		assertEquals(EnumSet.of(DiagramType.MATH), result);
	}

	@Test
	void test_startlatex() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startlatex");
		assertEquals(EnumSet.of(DiagramType.LATEX), result);
	}

	@Test
	void test_startdef() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startdef");
		assertEquals(EnumSet.of(DiagramType.DEFINITION), result);
	}

	@Test
	void test_startflow() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startflow");
		assertEquals(EnumSet.of(DiagramType.FLOW), result);
	}

	@Test
	void test_startchronology() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startchronology");
		assertEquals(EnumSet.of(DiagramType.CHRONOLOGY), result);
	}

	@Test
	void test_startnwdiag() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startnwdiag");
		assertEquals(EnumSet.of(DiagramType.NWDIAG), result);
	}

	@Test
	void test_startwire() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startwire");
		assertEquals(EnumSet.of(DiagramType.WIRE), result);
	}

	@Test
	void test_startboard() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startboard");
		assertEquals(EnumSet.of(DiagramType.BOARD), result);
	}

	@Test
	void test_startgit() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startgit");
		assertEquals(EnumSet.of(DiagramType.GIT), result);
	}

	@Test
	void test_starthcl() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@starthcl");
		assertEquals(EnumSet.of(DiagramType.HCL), result);
	}

	@Test
	void test_startebnf() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startebnf");
		assertEquals(EnumSet.of(DiagramType.EBNF), result);
	}

	@Test
	void test_startregex() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startregex");
		assertEquals(EnumSet.of(DiagramType.REGEX), result);
	}

	@Test
	void test_startfiles() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startfiles");
		assertEquals(EnumSet.of(DiagramType.FILES), result);
	}

	@Test
	void test_startchen() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startchen");
		assertEquals(EnumSet.of(DiagramType.CHEN_EER), result);
	}

	@Test
	void test_startchart() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startchart");
		assertEquals(EnumSet.of(DiagramType.CHART), result);
	}

	@Test
	void test_startpacketdiag() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startpacketdiag");
		assertEquals(EnumSet.of(DiagramType.PACKET), result);
	}

	@Test
	void test_startcreole() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startcreole");
		assertEquals(EnumSet.of(DiagramType.CREOLE), result);
	}

	@Test
	void test_startbpm() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startbpm");
		assertEquals(EnumSet.of(DiagramType.BPM), result);
	}

	@Test
	void test_startjcckit() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startjcckit");
		assertEquals(EnumSet.of(DiagramType.JCCKIT), result);
	}

	@Test
	void test_startsprites() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startsprites");
		assertEquals(EnumSet.of(DiagramType.SPRITES), result);
	}

	// --- Alias: @startproject maps to GANTT ---

	@Test
	void test_startproject_maps_to_gantt() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startproject");
		assertEquals(EnumSet.of(DiagramType.GANTT), result);
	}

	// --- Case insensitivity ---

	@Test
	void test_case_insensitive_startuml() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startUML");
		assertEquals(
				EnumSet.of(DiagramType.SEQUENCE, DiagramType.STATE, DiagramType.CLASS, DiagramType.OBJECT,
						DiagramType.ACTIVITY, DiagramType.DESCRIPTION, DiagramType.COMPOSITE, DiagramType.TIMING,
						DiagramType.HELP, DiagramType.SPRITES),
				result);
	}

	@Test
	void test_case_insensitive_startGantt() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startGantt");
		assertEquals(EnumSet.of(DiagramType.GANTT), result);
	}

	@Test
	void test_case_insensitive_STARTJSON() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@STARTJSON");
		assertEquals(EnumSet.of(DiagramType.JSON), result);
	}

	// --- Leading whitespace ---

	@Test
	void test_leading_spaces() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("   @startuml");
		assertEquals(
				EnumSet.of(DiagramType.SEQUENCE, DiagramType.STATE, DiagramType.CLASS, DiagramType.OBJECT,
						DiagramType.ACTIVITY, DiagramType.DESCRIPTION, DiagramType.COMPOSITE, DiagramType.TIMING,
						DiagramType.HELP, DiagramType.SPRITES),
				result);
	}

	@Test
	void test_leading_tab() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("\t@startgantt");
		assertEquals(EnumSet.of(DiagramType.GANTT), result);
	}

	// --- Backslash prefix instead of @ ---

	@Test
	void test_backslash_prefix() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("\\startuml");
		assertEquals(
				EnumSet.of(DiagramType.SEQUENCE, DiagramType.STATE, DiagramType.CLASS, DiagramType.OBJECT,
						DiagramType.ACTIVITY, DiagramType.DESCRIPTION, DiagramType.COMPOSITE, DiagramType.TIMING,
						DiagramType.HELP, DiagramType.SPRITES),
				result);
	}

	@Test
	void test_backslash_prefix_gantt() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("\\startgantt");
		assertEquals(EnumSet.of(DiagramType.GANTT), result);
	}

	// --- UNKNOWN cases ---

	@Test
	void test_empty_string_returns_empty() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("");
		assertTrue(result.isEmpty());
	}

	@Test
	void test_only_whitespace_returns_empty() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("   ");
		assertTrue(result.isEmpty());
	}

	@Test
	void test_no_arobase_returns_empty() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("startuml");
		assertTrue(result.isEmpty());
	}

	@Test
	void test_arobase_without_start_returns_empty() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@enduml");
		assertTrue(result.isEmpty());
	}

	@Test
	void test_arobase_start_only_returns_empty() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@start");
		assertTrue(result.isEmpty());
	}

	@Test
	void test_unrecognized_diagram_type_returns_unknown() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startxyz");
		assertEquals(EnumSet.of(DiagramType.UNKNOWN), result);
	}

	@Test
	void test_arobase_alone_returns_empty() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@");
		assertTrue(result.isEmpty());
	}

	@Test
	void test_random_text_returns_empty() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("hello world");
		assertTrue(result.isEmpty());
	}

	// --- Result cardinality ---

	@Test
	void test_result_is_not_empty() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startgantt");
		assertTrue(result.size() > 0);
	}

	@Test
	void test_startuml_returns_multiple_types() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startuml");
		assertTrue(result.size() > 1);
	}

	@Test
	void test_startgantt_returns_single_type() {
		final Collection<DiagramType> result = DiagramType.findStartTypes("@startgantt");
		assertEquals(1, result.size());
	}

}
