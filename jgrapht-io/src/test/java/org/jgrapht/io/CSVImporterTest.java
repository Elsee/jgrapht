/*
 * (C) Copyright 2016-2017, by Dimitrios Michail and Contributors.
 *
 * JGraphT : a free Java graph-theory library
 *
 * This program and the accompanying materials are dual-licensed under
 * either
 *
 * (a) the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation, or (at your option) any
 * later version.
 *
 * or (per the licensee's choosing)
 *
 * (b) the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation.
 */
package org.jgrapht.io;

import junit.framework.TestCase;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Pseudograph;
import org.jgrapht.graph.WeightedPseudograph;

import java.io.StringReader;

/**
 * 
 * @author Dimitrios Michail
 */
public class CSVImporterTest
    extends TestCase
{
    private static final String NL = System.getProperty("line.separator");

    public <E> CSVImporter<String, E> createImporter(
        Graph<String, E> g, CSVFormat format, Character delimiter)
    {
        return new CSVImporter<>(
            (l, a) -> l, (f, t, l, a) -> g.getEdgeFactory().createEdge(f, t), format, delimiter);
    }

    public <E> Graph<String, E> readGraph(
        String input, CSVFormat format, Character delimiter, Class<? extends E> edgeClass,
        boolean undirected, boolean weighted)
        throws ImportException
    {
        Graph<String, E> g;
        if (weighted) {
            g = new WeightedPseudograph<String, E>(edgeClass);
        } else {
            g = new Pseudograph<String, E>(edgeClass);
        }

        CSVImporter<String, E> importer = createImporter(g, format, delimiter);
        importer.importGraph(g, new StringReader(input));

        return g;
    }

    public void testDoubleOnUnweighted()
        throws ImportException
    {
        // @formatter:off
        String input =
              ";A;B;C;D;E" + NL
            + "C;1;0;0;1;0" + NL
            + "D;0;0;0.0;0;1" + NL
            + "B;0;0;0;0;0" + NL
            + "A;0;1;1;0;0" + NL
            + "E;1;1;0;1;1" + NL;
        // @formatter:on

        Graph<String, DefaultEdge> g = new Pseudograph<>(DefaultEdge.class);

        CSVImporter<String, DefaultEdge> importer = createImporter(g, CSVFormat.MATRIX, ';');
        importer.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_NODEID, true);
        importer.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_ZERO_WHEN_NO_EDGE, true);
        try {
            importer.importGraph(g, new StringReader(input));
            fail("No!");
        } catch (ImportException e) {
            // nothing
        }
    }

    public void testWrongHeaderNodeIds()
        throws ImportException
    {
        // @formatter:off
        String input =
              ";A;B;  ;D;E" + NL
            + "C;1;0;0;1;0" + NL
            + "D;0;0;0.0;0;1" + NL
            + "B;0;0;0;0;0" + NL
            + "A;0;1;1;0;0" + NL
            + "E;1;1;0;1;1" + NL;
        // @formatter:on

        Graph<String, DefaultEdge> g = new Pseudograph<>(DefaultEdge.class);

        CSVImporter<String, DefaultEdge> importer = createImporter(g, CSVFormat.MATRIX, ';');
        importer.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_NODEID, true);
        importer.setParameter(CSVFormat.Parameter.MATRIX_FORMAT_ZERO_WHEN_NO_EDGE, true);
        try {
            importer.importGraph(g, new StringReader(input));
            fail("No!");
        } catch (ImportException e) {
            // nothing
        }
    }

}
