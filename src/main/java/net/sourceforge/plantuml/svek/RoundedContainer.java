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
package net.sourceforge.plantuml.svek;

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.URectangle;

public final class RoundedContainer {

	private final XDimension2D dim;
	private final double nameHeight;
	private final double descriptionHeight;
	private final HColor borderColor;
	private final HColor northBackcolor;
	private final HColor centerBackcolor;
	private final HColor southBackcolor;
	private final UStroke stroke;
	private final double rounded;
	private final double shadowing;

	public RoundedContainer(HColor borderColor, XDimension2D dim, double nameHeight, double descriptionHeight,
			HColor northBackcolor, HColor centerBackcolor, HColor southBackcolor, UStroke stroke, double rounded,
			double shadowing) {
		if (dim.getWidth() == 0)
			throw new IllegalArgumentException();

		this.rounded = rounded;
		this.dim = dim;
		this.nameHeight = nameHeight;
		this.borderColor = borderColor;
		this.northBackcolor = northBackcolor;
		this.centerBackcolor = centerBackcolor;
		this.southBackcolor = southBackcolor;
		this.descriptionHeight = descriptionHeight;
		this.stroke = stroke;
		this.shadowing = shadowing;
	}

	public void drawU(UGraphic ug) {
		ug = ug.apply(borderColor).apply(stroke);
		final URectangle rect = URectangle.build(dim.getWidth(), dim.getHeight()).rounded(rounded);

		if (shadowing > 0) {
			rect.setDeltaShadow(shadowing);
			ug.apply(HColors.transparent().bg()).draw(rect);
			rect.setDeltaShadow(0);

		}

		new RoundedNorth(dim.getWidth(), nameHeight, northBackcolor, rounded).drawU(ug);
		drawCenter(ug);
		new RoundedSouth(dim.getWidth(), dim.getHeight() - nameHeight - descriptionHeight, southBackcolor, rounded)
				.drawU(ug.apply(UTranslate.dy(nameHeight + descriptionHeight)));

		ug.apply(HColors.transparent().bg()).draw(rect);

		if (nameHeight > 0)
			ug.apply(UTranslate.dy(nameHeight)).draw(ULine.hline(dim.getWidth()));

		if (descriptionHeight > 0 && nameHeight + descriptionHeight > 0)
			ug.apply(UTranslate.dy(nameHeight + descriptionHeight)).draw(ULine.hline(dim.getWidth()));

	}

	private void drawCenter(UGraphic ug) {
		if (descriptionHeight == 0)
			return;
		final URectangle rect = URectangle.build(dim.getWidth(), descriptionHeight);
		ug = ug.apply(UStroke.simple()).apply(centerBackcolor).apply(centerBackcolor.bg());
		ug.apply(UTranslate.dy(nameHeight)).draw(rect);

	}
}
