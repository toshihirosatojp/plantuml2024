/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.activitydiagram3.gtile.GConnection;
import net.sourceforge.plantuml.activitydiagram3.gtile.GPoint;
import net.sourceforge.plantuml.activitydiagram3.gtile.Gtile;
import net.sourceforge.plantuml.klimt.UChange;
import net.sourceforge.plantuml.klimt.UGroup;
import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.url.Url;

/**
 * A UGraphic interceptor that dispatches drawing operations to multiple
 * swimlane-specific UGraphics in a single traversal of the Ftile tree.
 * <p>
 * This replaces the pattern of N separate traversals with N
 * UGraphicInterceptorOneSwimlane instances, turning O(N * tree_size) into
 * O(tree_size).
 * <p>
 * The {@code activeSwimlanes} set tracks which swimlanes are relevant at the
 * current depth in the Ftile tree, so primitive shapes are dispatched only to
 * the correct LimitFinders.
 */
public class UGraphicInterceptorAllSwimlanes implements UGraphic {

	private final Map<Swimlane, UGraphic> swimlaneToUg;
	private final List<Swimlane> orderedList;
	private final Set<Swimlane> activeSwimlanes;
	private final StringBounder stringBounder;

	public UGraphicInterceptorAllSwimlanes(Map<Swimlane, UGraphic> swimlaneToUg, List<Swimlane> orderedList,
			StringBounder stringBounder) {
		this(swimlaneToUg, orderedList, swimlaneToUg.keySet(), stringBounder);
	}

	private UGraphicInterceptorAllSwimlanes(Map<Swimlane, UGraphic> swimlaneToUg, List<Swimlane> orderedList,
			Set<Swimlane> activeSwimlanes, StringBounder stringBounder) {
		this.swimlaneToUg = swimlaneToUg;
		this.orderedList = orderedList;
		this.activeSwimlanes = activeSwimlanes;
		this.stringBounder = stringBounder;
	}

	public void draw(UShape shape) {
		if (shape instanceof Ftile) {
			final Ftile tile = (Ftile) shape;
			final Set<Swimlane> tileSwimlanes = tile.getSwimlanes();
			// Check if any active swimlane is in this tile
			boolean hasMatch = false;
			for (Swimlane swimlane : activeSwimlanes)
				if (tileSwimlanes.contains(swimlane)) {
					hasMatch = true;
					break;
				}
			if (hasMatch) {
				// Narrow the active swimlanes to the intersection with this tile's swimlanes
				final UGraphicInterceptorAllSwimlanes narrowed = withActiveSwimlanes(tileSwimlanes);
				tile.drawU(narrowed);
			}

		} else if (shape instanceof Gtile) {
			final Gtile tile = (Gtile) shape;
			final Set<Swimlane> tileSwimlanes = tile.getSwimlanes();
			boolean hasMatch = false;
			for (Swimlane swimlane : activeSwimlanes)
				if (tileSwimlanes.contains(swimlane)) {
					hasMatch = true;
					break;
				}
			if (hasMatch) {
				final UGraphicInterceptorAllSwimlanes narrowed = withActiveSwimlanes(tileSwimlanes);
				tile.drawU(narrowed);
			}

		} else if (shape instanceof GConnection) {
			final GConnection connection = (GConnection) shape;
			final List<GPoint> hooks = connection.getHooks();
			final GPoint point0 = hooks.get(0);
			final GPoint point1 = hooks.get(1);
			for (Swimlane swimlane : activeSwimlanes)
				if (point0.match(swimlane) && point1.match(swimlane)) {
					connection.drawU(swimlaneToUg.get(swimlane));
				}

		} else if (shape instanceof Connection) {
			final Connection connection = (Connection) shape;
			final Ftile tile1 = connection.getFtile1();
			final Ftile tile2 = connection.getFtile2();
			for (Swimlane swimlane : activeSwimlanes) {
				final boolean contained1 = tile1 == null || tile1.getSwimlaneOut() == null
						|| tile1.getSwimlaneOut() == swimlane;
				final boolean contained2 = tile2 == null || tile2.getSwimlaneIn() == null
						|| tile2.getSwimlaneIn() == swimlane;
				if (contained1 && contained2) {
					final UGraphic ug = swimlaneToUg.get(swimlane);
					if (ug != null)
						connection.drawU(ug);
				}
			}

		} else {
			// Primitive shape: dispatch only to the currently active swimlanes
			for (Swimlane swimlane : activeSwimlanes) {
				final UGraphic ug = swimlaneToUg.get(swimlane);
				if (ug != null)
					ug.draw(shape);
			}
		}
	}

	/**
	 * Returns a new instance with the active swimlanes narrowed to the
	 * intersection of the current active set and the given tile swimlanes.
	 * If the sets are identical, returns {@code this} to avoid allocation.
	 */
	private UGraphicInterceptorAllSwimlanes withActiveSwimlanes(Set<Swimlane> tileSwimlanes) {
		if (tileSwimlanes.containsAll(activeSwimlanes))
			return this;

		final Set<Swimlane> narrowed = new java.util.LinkedHashSet<>();
		for (Swimlane s : activeSwimlanes)
			if (tileSwimlanes.contains(s))
				narrowed.add(s);

		return new UGraphicInterceptorAllSwimlanes(swimlaneToUg, orderedList, narrowed, stringBounder);
	}

	public UGraphic apply(UChange change) {
		final Map<Swimlane, UGraphic> newMap = new LinkedHashMap<>();
		for (Map.Entry<Swimlane, UGraphic> entry : swimlaneToUg.entrySet())
			newMap.put(entry.getKey(), entry.getValue().apply(change));

		return new UGraphicInterceptorAllSwimlanes(newMap, orderedList, activeSwimlanes, stringBounder);
	}

	public StringBounder getStringBounder() {
		return stringBounder;
	}

	public UParam getParam() {
		return swimlaneToUg.values().iterator().next().getParam();
	}

	public ColorMapper getColorMapper() {
		return swimlaneToUg.values().iterator().next().getColorMapper();
	}

	public void startUrl(Url url) {
	}

	public void closeUrl() {
	}

	public void startGroup(UGroup group) {
	}

	public void closeGroup() {
	}

	public void flushUg() {
		for (UGraphic ug : swimlaneToUg.values())
			ug.flushUg();
	}

	public boolean matchesProperty(String propertyName) {
		return false;
	}

	public HColor getDefaultBackground() {
		return swimlaneToUg.values().iterator().next().getDefaultBackground();
	}

	public void writeToStream(OutputStream os, String metadata, int dpi) throws IOException {
		throw new UnsupportedOperationException();
	}

}
