package net.sourceforge.plantuml.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class BoyerMooreTest {

	@Test
	void testBasicSearch() {
		final BoyerMoore bm = new BoyerMoore("abc");
		assertEquals(0, bm.searchIn("abcdef"));
		assertEquals(3, bm.searchIn("xxxabc"));
		assertEquals(2, bm.searchIn("__abc__"));
	}

	@Test
	void testSearchFromComment() {
		final BoyerMoore bm = new BoyerMoore("<img data:image/png;base64,");
		assertEquals(3, bm.searchIn("xxx<img data:image/png;base64,dddd"));
	}

	@Test
	void testSearchWithFrom() {
		final BoyerMoore bm = new BoyerMoore("abc");
		assertEquals(5, bm.searchIn("abcxxabc", 1));
		assertEquals(5, bm.searchIn("abcxxabc", 5));
	}

	@Test
	void testPatternNotFound() {
		final BoyerMoore bm = new BoyerMoore("xyz");
		assertEquals(-1, bm.searchIn("abcdef"));
		assertEquals(-1, bm.searchIn(""));
	}

	@Test
	void testNullText() {
		final BoyerMoore bm = new BoyerMoore("abc");
		assertEquals(-1, bm.searchIn(null));
	}

	@Test
	void testPatternAtEnd() {
		final BoyerMoore bm = new BoyerMoore("end");
		assertEquals(9, bm.searchIn("the very end"));
		assertEquals(-1, bm.searchIn("the very en"));
	}

	@Test
	void testSingleCharPattern() {
		final BoyerMoore bm = new BoyerMoore("x");
		assertEquals(0, bm.searchIn("xyz"));
		assertEquals(2, bm.searchIn("abx"));
		assertEquals(-1, bm.searchIn("abc"));
	}

	@Test
	void testPatternEqualsText() {
		final BoyerMoore bm = new BoyerMoore("hello");
		assertEquals(0, bm.searchIn("hello"));
	}

	@Test
	void testPatternLongerThanText() {
		final BoyerMoore bm = new BoyerMoore("abcdef");
		assertEquals(-1, bm.searchIn("abc"));
	}

	@Test
	void testFromBeyondText() {
		final BoyerMoore bm = new BoyerMoore("abc");
		assertEquals(-1, bm.searchIn("xyzabc", 100));
	}

	@Test
	void testNegativeFrom() {
		final BoyerMoore bm = new BoyerMoore("abc");
		assertEquals(0, bm.searchIn("abcdef", -5));
	}

	@Test
	void testRepeatedPattern() {
		final BoyerMoore bm = new BoyerMoore("aa");
		assertEquals(0, bm.searchIn("aaaa"));
		assertEquals(1, bm.searchIn("baa"));
	}

	@Test
	void testNullPatternThrows() {
		assertThrows(IllegalArgumentException.class, () -> new BoyerMoore(null));
	}

	@Test
	void testEmptyPatternThrows() {
		assertThrows(IllegalArgumentException.class, () -> new BoyerMoore(""));
	}

	@Test
	void testReuse() {
		final BoyerMoore bm = new BoyerMoore("test");
		assertEquals(0, bm.searchIn("testing"));
		assertEquals(4, bm.searchIn("one test two"));
		assertEquals(-1, bm.searchIn("no match"));
	}

	@Test
	void testAccentedTextWithAsciiPattern() {
		final BoyerMoore bm = new BoyerMoore("world");
		assertEquals(7, bm.searchIn("héllo, world!"));
	}

	@Test
	void testPatternSurroundedByAccents() {
		final BoyerMoore bm = new BoyerMoore("data");
		assertEquals(4, bm.searchIn("pré-data-résumé"));
	}

	@Test
	void testPatternNotFoundInAccentedText() {
		final BoyerMoore bm = new BoyerMoore("resume");
		assertEquals(-1, bm.searchIn("résumé"));
	}

	@Test
	void testAccentedTextMultipleOccurrences() {
		final BoyerMoore bm = new BoyerMoore("img");
		assertEquals(0, bm.searchIn("img à la une, img à la fin"));
		assertEquals(14, bm.searchIn("img à la une, img à la fin", 1));
	}

	@Test
	void testAccentedTextWithFromOffset() {
		final BoyerMoore bm = new BoyerMoore("tag");
		assertEquals(8, bm.searchIn("café et tag ici", 5));
	}

	@Test
	void testUnicodePatternThrows() {
		assertThrows(IllegalArgumentException.class, () -> new BoyerMoore("őabc"));
		assertThrows(IllegalArgumentException.class, () -> new BoyerMoore("abc世z"));
	}

	@Test
	void testSearchInUnicodeText() {
		final BoyerMoore bm = new BoyerMoore("find");
		assertEquals(4, bm.searchIn("世界你好find"));
		assertEquals(-1, bm.searchIn("世界你好"));
	}

	@Test
	void testSearchInTextWithMixedUnicode() {
		final BoyerMoore bm = new BoyerMoore("img");
		assertEquals(6, bm.searchIn("éèő世界Ximg"));
	}

}
