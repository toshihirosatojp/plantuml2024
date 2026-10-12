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
 *
 */
package net.sourceforge.plantuml.klimt.font;

public class UFontFactory {

	/**
	 * Builds a font using a face (weight + italic axis) and size.
	 *
	 * @param fullDefinition font family definition
	 * @param face           font face (style + weight), defaults to normal if null
	 * @param fontSize       font size
	 * @return configured font
	 */
	public static UFont build(String fullDefinition, UFontFace face, int fontSize) {
		final FontStack fontStack = FontStack.build(fullDefinition);
		final UFontFace safeFace = face == null ? UFontFace.normal() : face;
		return new UFont(fontStack, safeFace, fontSize);
	}

	public static UFont serif(int size) {
		return build(FontStack.SERIF, UFontFace.normal(), size);
	}

	public static UFont sansSerif(int size) {
		return build(FontStack.SANS_SERIF, UFontFace.normal(), size);
	}

	public static UFont monospace(int size) {
		return build(FontStack.MONOSPACE, UFontFace.normal(), size);
	}

	public static UFont byDefault(int size) {
		return sansSerif(12);
	}

}
