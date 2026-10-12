/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 *
 * If you like this project or if you find it useful, you can support us at:
 *
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 *
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Shunli Han
 *
 *
 */
package net.sourceforge.plantuml.utils;

public class BoyerMoore {

	private final String pattern;
	private final int[] shift;
	private final int defaultShift;

	public BoyerMoore(String pattern) {
		if (pattern == null || pattern.isEmpty())
			throw new IllegalArgumentException("Pattern must not be null or empty");

		this.pattern = pattern;
		this.shift = buildShiftTable(pattern);
		this.defaultShift = pattern.length();
	}

	private static int[] buildShiftTable(String pattern) {
		final int ALPHABET_SIZE = 256;
		final int[] table = new int[ALPHABET_SIZE];
		final int last = pattern.length() - 1;

		for (int i = 0; i < ALPHABET_SIZE; i++)
			table[i] = pattern.length();

		for (int i = 0; i < last; i++) {
			final char c = pattern.charAt(i);
			if (c >= 256)
				throw new IllegalArgumentException("pattern=" + pattern);
			table[c] = last - i;
		}

		return table;
	}

	public int searchIn(String text) {
		return searchIn(text, 0);
	}

	public int searchIn(String text, int from) {
		if (text == null)
			return -1;

		final int textLen = text.length();
		final int patLen = pattern.length();

		if (from < 0)
			from = 0;

		if (patLen > textLen - from)
			return -1;

		int i = from;

		while (i <= textLen - patLen) {
			int j = patLen - 1;
			while (j >= 0 && pattern.charAt(j) == text.charAt(i + j))
				j--;

			if (j < 0)
				return i;

			final char c = text.charAt(i + patLen - 1);
			i += (c < 256) ? shift[c] : defaultShift;
		}

		return -1;
	}

}