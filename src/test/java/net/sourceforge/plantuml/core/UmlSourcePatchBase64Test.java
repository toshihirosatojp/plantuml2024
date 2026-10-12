package net.sourceforge.plantuml.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.klimt.creole.atom.AtomImg;
import net.sourceforge.plantuml.utils.BoyerMoore;
import net.sourceforge.plantuml.utils.SignatureUtils;

class UmlSourcePatchBase64Test {

	private static final BoyerMoore BM = new BoyerMoore(AtomImg.DATA_IMAGE_PNG_BASE64);

	private static String patch(String line) {
		final UmlSource source = UmlSource.create(new ArrayList<>(), false);
		return source.patchBase64Line(BM, line);
	}

	@Test
	void lineWithoutBase64IsReturnedUnchanged() {
		final String line = "rectangle \"hello\" as r1";
		assertSame(line, patch(line));
	}

	@Test
	void emptyLineIsReturnedUnchanged() {
		final String line = "";
		assertSame(line, patch(line));
	}

	@Test
	void simpleBase64WithScale() {
		final String md5 = SignatureUtils.getMD5Hex("ABCDEF");
		final String input = "rectangle \"<img data:image/png;base64,ABCDEF{scale=1}>\" as r1";
		final String expected = "rectangle \"<img data:image/png;md5," + md5 + "{scale=1}>\" as r1";
		assertEquals(expected, patch(input));
	}

	@Test
	void simpleBase64WithoutScale() {
		final String md5 = SignatureUtils.getMD5Hex("iVBORw0KGgo=");
		final String input = "before <img data:image/png;base64,iVBORw0KGgo=> after";
		final String expected = "before <img data:image/png;md5," + md5 + "> after";
		assertEquals(expected, patch(input));
	}

	@Test
	void multipleBase64InSameLine() {
		final String md5a = SignatureUtils.getMD5Hex("AAA");
		final String md5b = SignatureUtils.getMD5Hex("BBB");
		final String input = "<img data:image/png;base64,AAA{scale=1}> text <img data:image/png;base64,BBB{scale=2}>";
		final String expected = "<img data:image/png;md5," + md5a + "{scale=1}> text <img data:image/png;md5," + md5b
				+ "{scale=2}>";
		assertEquals(expected, patch(input));
	}

	@Test
	void base64WithTrailingEquals() {
		final String data = "iVBORw0KGgoAAAANSUhEUgAA==";
		final String md5 = SignatureUtils.getMD5Hex(data);
		final String input = "<img data:image/png;base64," + data + "{scale=0.5}>";
		final String expected = "<img data:image/png;md5," + md5 + "{scale=0.5}>";
		assertEquals(expected, patch(input));
	}

	@Test
	void realWorldAwsStyleLine() {
		final String base64Data = "iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAIGNIUk0AAHomAACAhAAA";
		final String md5 = SignatureUtils.getMD5Hex(base64Data);
		final String input = "rectangle \"==Action Error Rule\\n<img data:image/png;base64," + base64Data
				+ "{scale=1}>\\n//<size:12>[error]</size>//\" <<IoTRule>> as iotRule";
		final String expected = "rectangle \"==Action Error Rule\\n<img data:image/png;md5," + md5
				+ "{scale=1}>\\n//<size:12>[error]</size>//\" <<IoTRule>> as iotRule";
		assertEquals(expected, patch(input));
	}

	@Test
	void unclosedTagIsIgnored() {
		final String md5 = SignatureUtils.getMD5Hex("ABCDEF");
		final String input = "text <img data:image/png;base64,ABCDEF no closing bracket";
		final String expected = "text <img data:image/png;md5," + md5 + " no closing bracket";
		assertEquals(expected, patch(input));
	}

	@Test
	void tagStartAtBeginningOfLine() {
		final String md5 = SignatureUtils.getMD5Hex("XYZ");
		final String input = "data:image/png;base64,XYZ>";
		final String expected = "data:image/png;md5," + md5 + ">";
		assertEquals(expected, patch(input));
	}

	@Test
	void emptyBase64Data() {
		final String md5 = SignatureUtils.getMD5Hex("");
		final String input = "<img data:image/png;base64,{scale=1}>";
		final String expected = "<img data:image/png;md5," + md5 + "{scale=1}>";
		assertEquals(expected, patch(input));
	}

	@Test
	void emptyBase64DataNoScale() {
		final String md5 = SignatureUtils.getMD5Hex("");
		final String input = "<img data:image/png;base64,>";
		final String expected = "<img data:image/png;md5," + md5 + ">";
		assertEquals(expected, patch(input));
	}

}
