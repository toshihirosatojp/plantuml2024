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
package net.sourceforge.plantuml.klimt.shape;

import net.atmp.SpecialText;
import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.Shadowable;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;

public class BigFrame extends TextBlockMemoized {

	private final TextBlock title;
	private final TextBlock original;
	private final ClockwiseTopRightBottomLeft padding;
	private final Fashion symbolContext;

	public BigFrame(final TextBlock title, final TextBlock original, final ClockwiseTopRightBottomLeft padding,
			final Fashion symbolContext) {
		this.title = title;
		this.original = original;
		this.padding = padding;
		this.symbolContext = symbolContext;
	}

	private double getYpos(XDimension2D dimTitle) {
		if (dimTitle.getWidth() == 0)
			return 12;

		return dimTitle.getHeight() + 3;
	}

	private ClockwiseTopRightBottomLeft getEffectivePadding(StringBounder stringBounder) {
		final XDimension2D dimTitle = title.calculateDimension(stringBounder);
		return padding.incTop(dimTitle.getHeight() + 10);
	}

	private double computeWidth(StringBounder stringBounder) {
		final XDimension2D dimTitle = title.calculateDimension(stringBounder);
		final ClockwiseTopRightBottomLeft effectivePadding = getEffectivePadding(stringBounder);
		final MinMax originalMinMax = TextBlockUtils.getMinMax(original, stringBounder, false);
		final double ww = originalMinMax.getMinX() >= 0 ? originalMinMax.getMaxX() : originalMinMax.getWidth();
		return effectivePadding.getLeft() + Math.max(ww + 12, dimTitle.getWidth() + 10) + effectivePadding.getRight();
	}

	private double computeHeight(StringBounder stringBounder) {
		final XDimension2D dimTitle = title.calculateDimension(stringBounder);
		final ClockwiseTopRightBottomLeft effectivePadding = getEffectivePadding(stringBounder);
		final MinMax originalMinMax = TextBlockUtils.getMinMax(original, stringBounder, false);
		final double hh = originalMinMax.getMinY() >= 0 ? originalMinMax.getMaxY() : originalMinMax.getHeight();
		return effectivePadding.getTop() + dimTitle.getHeight() + hh + effectivePadding.getBottom();
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D dim = calculateDimension(stringBounder);
		ug = symbolContext.apply(ug);
		final XDimension2D dimTitle = title.calculateDimension(stringBounder);
		final double widthFull = dim.getWidth();
		final Shadowable rectangle = URectangle.build(widthFull, dim.getHeight())
				.rounded(symbolContext.getRoundCorner()).ignoreForCompressionOnX().ignoreForCompressionOnY();
		rectangle.setDeltaShadow(symbolContext.getDeltaShadow());

		ug.draw(rectangle);

		final double textWidth;
		final int cornersize;
		if (dimTitle.getWidth() == 0) {
			textWidth = widthFull / 3;
			cornersize = 7;
		} else {
			textWidth = dimTitle.getWidth() + 10;
			cornersize = 10;
		}
		final double textHeight = getYpos(dimTitle);

		final UPath line = UPath.none();
		line.setIgnoreForCompressionOnX();
		line.moveTo(textWidth, 0);

		line.lineTo(textWidth, textHeight - cornersize);
		line.lineTo(textWidth - cornersize, textHeight);

		line.lineTo(0, textHeight);
		ug.apply(HColors.none().bg()).draw(line);
		final double widthTitle = title.calculateDimension(stringBounder).getWidth();

		// Temporary hack...
		if (widthFull - widthTitle < 25)
			title.drawU(ug.apply(new UTranslate(3, 1)));
		else
			ug.apply(new UTranslate(3, 1)).draw(new SpecialText(title));

	}

	@Override
	public XDimension2D calculateDimensionSlow(StringBounder stringBounder) {
		return new XDimension2D(computeWidth(stringBounder), computeHeight(stringBounder));
	}

}