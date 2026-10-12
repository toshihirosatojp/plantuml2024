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
 * Original Author:  Arnaud Roques
 *
 */
package com.plantuml.ubrex;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

public class Repetition {

	private final Set<Integer> values = new TreeSet<>();
	private final AtomicInteger minInclusive = new AtomicInteger(Integer.MAX_VALUE);

	public static Repetition parse(TextNavigator input) {
		final Repetition result = new Repetition();
		final StringBuilder token = new StringBuilder();
		while (true) {
			final char ch = input.charAt(0);
			if (ch == '}') {
				result.addToken(token.toString());
				input.jump(1);
				return result;
			} else if (ch == ';') {
				result.addToken(token.toString());
				token.setLength(0);
			} else {
				token.append(ch);
			}
			input.jump(1);
		}
	}

	private void addToken(String token) {
		if (token.isEmpty())
			throw new UnsupportedOperationException("empty repetition token");

		if (token.endsWith("+")) {
			final int min = Integer.parseInt(token.substring(0, token.length() - 1));
			this.minInclusive.set(min);
		} else if (token.contains("-")) {
			final int dash = token.indexOf('-');
			final int min = Integer.parseInt(token.substring(0, dash));
			final int max = Integer.parseInt(token.substring(dash + 1));
			for (int i = min; i <= max; i++)
				add(i);
		} else {
			final int exact = Integer.parseInt(token);
			add(exact);
		}
	}

	private void add(int nb) {
		this.values.add(nb);
	}

	@Override
	public String toString() {
		return values.toString();
	}

	public boolean match(int nb) {
		if (nb >= minInclusive.get())
			return true;
		return values.contains(nb);
	}

}
