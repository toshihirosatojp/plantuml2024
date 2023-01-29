/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 *
 * If you like this project or if you find it useful, you can support us at:
 *
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
 * Contribution :  Hisashi Miyashita
 *
 *
 */
package net.sourceforge.plantuml.svek;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.AlignmentParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.cucadiagram.EntityPosition;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.USymbols;

public class ClusterDotString {

	private final Cluster cluster;
	private final ISkinParam skinParam;
	private static final String ID_EE = "ee";

	public ClusterDotString(Cluster cluster, ISkinParam skinParam) {
		this.cluster = cluster;
		this.skinParam = skinParam;
	}

	void printInternal(StringBuilder sb, Collection<SvekLine> lines, StringBounder stringBounder, DotMode dotMode,
			Object graphvizVersion, UmlDiagramType type) {
		throw new UnsupportedOperationException();
	}

	private String getSourceInPoint(UmlDiagramType type) {
		if (skinParam.useSwimlanes(type))
			return "sourceIn" + cluster.getColor();

		return null;
	}

	private String getSinkInPoint(UmlDiagramType type) {
		if (skinParam.useSwimlanes(type))
			return "sinkIn" + cluster.getColor();

		return null;
	}

	private String empty() {
		// return "empty" + color;
		// We use the same node with one for thereALinkFromOrToGroup2 as an empty
		// because we cannot put a new node in the nested inside of the cluster
		// if thereALinkFromOrToGroup2 is enabled.
		return Cluster.getSpecialPointId(cluster.getGroup());
	}

	private boolean hasPort() {
		for (EntityPosition pos : entityPositionsExceptNormal())
			if (pos.isPort())
				return true;

		return false;
	}

	private Set<EntityPosition> entityPositionsExceptNormal() {
		final Set<EntityPosition> result = EnumSet.<EntityPosition>noneOf(EntityPosition.class);
		for (SvekNode sh : cluster.getNodes())
			if (sh.getEntityPosition() != EntityPosition.NORMAL)
				result.add(sh.getEntityPosition());

		return Collections.unmodifiableSet(result);
	}

	private void subgraphClusterNoLabel(StringBuilder sb, String id) {
		subgraphClusterWithLabel(sb, id, "\"\"");
	}

	private void subgraphClusterWithLabel(StringBuilder sb, String id, String label) {
		sb.append("subgraph " + cluster.getClusterId() + id + " {");
		sb.append("label=" + label + ";");
	}

	private void printRanks(String rank, List<? extends SvekNode> entries, StringBuilder sb,
			StringBounder stringBounder) {
		if (entries.size() > 0) {
			sb.append("{rank=" + rank + ";");
			for (SvekNode sh1 : entries)
				sb.append(sh1.getUid() + ";");

			sb.append("}");
			SvekUtils.println(sb);
			for (SvekNode sh2 : entries)
				sh2.appendShape(sb, stringBounder);

			SvekUtils.println(sb);
			if (hasPort()) {
				boolean arrow = false;
				String node = null;
				for (SvekNode sh : entries) {
					if (arrow)
						sb.append("->");

					arrow = true;
					node = sh.getUid();
					sb.append(node);
				}
				if (arrow)
					sb.append(" [arrowhead=none]");

				sb.append(';');
				SvekUtils.println(sb);
				sb.append(node + "->" + empty() + ";");
				SvekUtils.println(sb);
			}
		}
	}

	private List<SvekNode> withPosition(Set<EntityPosition> positions) {
		final List<SvekNode> result = new ArrayList<>();
		for (final Iterator<SvekNode> it = cluster.getNodes().iterator(); it.hasNext();) {
			final SvekNode sh = it.next();
			if (positions.contains(sh.getEntityPosition()))
				result.add(sh);

		}
		return result;
	}

	private boolean protection0(UmlDiagramType type) {
		if (skinParam.useSwimlanes(type))
			return false;

		return true;
	}

	private boolean protection1(UmlDiagramType type) {
		if (cluster.getGroup().getUSymbol() == USymbols.NODE)
			return true;

		if (skinParam.useSwimlanes(type))
			return false;

		return true;
	}

	private boolean isThereALinkFromOrToGroup(Collection<SvekLine> lines) {
		for (SvekLine line : lines)
			if (line.isLinkFromOrTo(cluster.getGroup()))
				return true;

		return false;
	}

}
