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
 *
 * 
 */
package net.sourceforge.plantuml.svek;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.BaseFile;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.baraye.EntityFactory;
import net.sourceforge.plantuml.baraye.EntityImp;
import net.sourceforge.plantuml.cucadiagram.ICucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Rankdir;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.cucadiagram.dot.DotSplines;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.posimo.Moveable;

public class DotStringFactory implements Moveable {

	private final Bibliotekon bibliotekon = new Bibliotekon();

	private final ColorSequence colorSequence;
	private final Cluster root;

	private Cluster current;
	private final UmlDiagramType umlDiagramType;
	private final ISkinParam skinParam;
	private final DotMode dotMode;
	private DotSplines dotSplines;

	private final StringBounder stringBounder;

	public DotStringFactory(StringBounder stringBounder, DotData dotData) {
		this.skinParam = dotData.getSkinParam();
		this.umlDiagramType = dotData.getUmlDiagramType();
		this.dotMode = dotData.getDotMode();

		this.colorSequence = new ColorSequence();
		this.stringBounder = stringBounder;
		this.root = new Cluster(dotData.getEntityFactory().getDiagram(), colorSequence, skinParam,
				dotData.getRootGroup());
		this.current = root;
	}

	public DotStringFactory(StringBounder stringBounder, ICucaDiagram diagram) {
		this.skinParam = diagram.getSkinParam();
		this.umlDiagramType = diagram.getUmlDiagramType();
		this.dotMode = DotMode.NORMAL;

		this.colorSequence = new ColorSequence();
		this.stringBounder = stringBounder;
		this.root = new Cluster(diagram, colorSequence, skinParam, diagram.getEntityFactory().getRootGroup());
		this.current = root;
	}

	public void addNode(SvekNode node) {
		current.addNode(node);
	}

	private double getHorizontalDzeta() {
		double max = 0;
		for (SvekLine l : bibliotekon.allLines()) {
			final double c = l.getHorizontalDzeta(stringBounder);
			if (c > max)
				max = c;

		}
		return max / 10;
	}

	private double getVerticalDzeta() {
		double max = 0;
		for (SvekLine l : bibliotekon.allLines()) {
			final double c = l.getVerticalDzeta(stringBounder);
			if (c > max)
				max = c;

		}
		if (root.diagram.getPragma().useKermor())
			return max / 100;
		return max / 10;
	}

	String createDotString(String... dotStrings) {
		final StringBuilder sb = new StringBuilder();

		double nodesep = getHorizontalDzeta();
		if (nodesep < getMinNodeSep())
			nodesep = getMinNodeSep();

		if (skinParam.getNodesep() != 0)
			nodesep = skinParam.getNodesep();

		final String nodesepInches = SvekUtils.pixelToInches(nodesep);
		// Log.println("nodesep=" + nodesepInches);
		double ranksep = getVerticalDzeta();
		if (ranksep < getMinRankSep())
			ranksep = getMinRankSep();

		if (skinParam.getRanksep() != 0)
			ranksep = skinParam.getRanksep();

		final String ranksepInches = SvekUtils.pixelToInches(ranksep);
		// Log.println("ranksep=" + ranksepInches);
		sb.append("digraph unix {");
		SvekUtils.println(sb);

		for (String s : dotStrings) {
			if (s.startsWith("ranksep"))
				sb.append("ranksep=" + ranksepInches + ";");
			else if (s.startsWith("nodesep"))
				sb.append("nodesep=" + nodesepInches + ";");
			else
				sb.append(s);

			SvekUtils.println(sb);
		}
		// sb.append("newrank=true;");
		// SvekUtils.println(sb);
		sb.append("remincross=true;");
		SvekUtils.println(sb);
		sb.append("searchsize=500;");
		SvekUtils.println(sb);
		// if (OptionFlags.USE_COMPOUND) {
		// sb.append("compound=true;");
		// SvekUtils.println(sb);
		// }

		dotSplines = skinParam.getDotSplines();
		if (dotSplines == DotSplines.POLYLINE) {
			sb.append("splines=polyline;");
			SvekUtils.println(sb);
		} else if (dotSplines == DotSplines.ORTHO) {
			sb.append("splines=ortho;");
			sb.append("forcelabels=true;");
			SvekUtils.println(sb);
		}

		if (skinParam.getRankdir() == Rankdir.LEFT_TO_RIGHT) {
			sb.append("rankdir=LR;");
			SvekUtils.println(sb);
		}

		manageMinMaxCluster(sb);

		if (root.diagram.getPragma().useKermor()) {
			for (SvekLine line : bibliotekon.lines0())
				line.appendLine(getGraphvizVersion(), sb, dotMode, dotSplines);
			for (SvekLine line : bibliotekon.lines1())
				line.appendLine(getGraphvizVersion(), sb, dotMode, dotSplines);

			root.printCluster3_forKermor(sb, bibliotekon.allLines(), stringBounder, dotMode, getGraphvizVersion(),
					umlDiagramType);

		} else {
			root.printCluster1(sb, bibliotekon.allLines(), stringBounder);

			for (SvekLine line : bibliotekon.lines0())
				line.appendLine(getGraphvizVersion(), sb, dotMode, dotSplines);

			root.printCluster2(sb, bibliotekon.allLines(), stringBounder, dotMode, getGraphvizVersion(),
					umlDiagramType);

			for (SvekLine line : bibliotekon.lines1())
				line.appendLine(getGraphvizVersion(), sb, dotMode, dotSplines);

		}

		SvekUtils.println(sb);
		sb.append("}");

		return sb.toString();
	}

	private void manageMinMaxCluster(final StringBuilder sb) {
		final List<String> minPointCluster = new ArrayList<>();
		final List<String> maxPointCluster = new ArrayList<>();
		for (Cluster cluster : bibliotekon.allCluster()) {
			final String minPoint = cluster.getMinPoint(umlDiagramType);
			if (minPoint != null)
				minPointCluster.add(minPoint);

			final String maxPoint = cluster.getMaxPoint(umlDiagramType);
			if (maxPoint != null)
				maxPointCluster.add(maxPoint);

		}
		if (minPointCluster.size() > 0) {
			sb.append("{rank=min;");
			for (String s : minPointCluster) {
				sb.append(s);
				sb.append(" [shape=point,width=.01,label=\"\"]");
				sb.append(";");
			}
			sb.append("}");
			SvekUtils.println(sb);
		}
		if (maxPointCluster.size() > 0) {
			sb.append("{rank=max;");
			for (String s : maxPointCluster) {
				sb.append(s);
				sb.append(" [shape=point,width=.01,label=\"\"]");
				sb.append(";");
			}
			sb.append("}");
			SvekUtils.println(sb);
		}
	}

	private int getMinRankSep() {
		if (umlDiagramType == UmlDiagramType.ACTIVITY) {
			// return 29;
			return 40;
		}
		if (root.diagram.getPragma().useKermor())
			return 40;
		return 60;
	}

	private int getMinNodeSep() {
		if (umlDiagramType == UmlDiagramType.ACTIVITY) {
			// return 15;
			return 20;
		}
		return 35;
	}

	private Object graphvizVersion;

	public Object getGraphvizVersion() {
		throw new UnsupportedOperationException();
	}

	private Object getGraphvizVersionInternal() {
		throw new UnsupportedOperationException();
	}

	public String getSvg(BaseFile basefile, String[] dotOptions) throws IOException {
		throw new UnsupportedOperationException();
	}

	public boolean illegalDotExe() {
		throw new UnsupportedOperationException();
	}

	public File getDotExe() {
		throw new UnsupportedOperationException();
	}

	public void solve(EntityFactory entityFactory, final String svg) throws IOException, InterruptedException {
		throw new UnsupportedOperationException();
	}

	private int getClusterIndex(final String svg, int colorInt) {
		final String colorString = StringUtils.goLowerCase(StringUtils.sharp000000(colorInt));
		final String keyTitle1 = "=\"" + colorString + "\"";
		int idx = svg.indexOf(keyTitle1);
		if (idx == -1) {
			final String keyTitle2 = "stroke:" + colorString + ";";
			idx = svg.indexOf(keyTitle2);
		}
		if (idx == -1)
			throw new IllegalStateException("Cannot find color " + colorString);

		return idx;
	}

	public void openCluster(EntityImp g, ClusterHeader clusterHeader) {
		this.current = current.createChild(clusterHeader, colorSequence, skinParam, g);
		bibliotekon.addCluster(this.current);
	}

	public void closeCluster() {
		if (current.getParentCluster() == null)
			throw new IllegalStateException();

		this.current = current.getParentCluster();
	}

	public void moveSvek(double deltaX, double deltaY) {
		for (SvekNode sh : bibliotekon.allNodes())
			sh.moveSvek(deltaX, deltaY);

		for (SvekLine line : bibliotekon.allLines())
			line.moveSvek(deltaX, deltaY);

		for (Cluster cl : bibliotekon.allCluster())
			cl.moveSvek(deltaX, deltaY);

	}

	public final Bibliotekon getBibliotekon() {
		return bibliotekon;
	}

	public ColorSequence getColorSequence() {
		return colorSequence;
	}

}
