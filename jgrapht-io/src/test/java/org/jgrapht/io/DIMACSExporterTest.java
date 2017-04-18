/*
 * (C) Copyright 2017-2017, by Dimitrios Michail and Contributors.
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

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

/**
 * Tests
 * 
 * @author Dimitrios Michail
 */
public class DIMACSExporterTest
{
    private static final String V1 = "v1";
    private static final String V2 = "v2";
    private static final String V3 = "v3";
    private static final String V4 = "v4";
    private static final String V5 = "v5";

    private static final String NL = System.getProperty("line.separator");

    // @formatter:off
    private static final String UNDIRECTED =
        "c" + NL +
        "c SOURCE: Generated using the JGraphT library" + NL +
        "c" + NL +
        "p sp 3 2" + NL +
        "a 1 2" + NL +
        "a 3 1" + NL;

    private static final String UNDIRECTED_WEIGHTED = 
        "c" + NL +
        "c SOURCE: Generated using the JGraphT library" + NL +
        "c" + NL +
        "p sp 3 2" + NL +
        "a 1 2 2.0" + NL +
        "a 3 1 5.0" + NL;

    private static final String UNDIRECTED_AS_UNWEIGHTED =
        "c" + NL +
        "c SOURCE: Generated using the JGraphT library" + NL +
        "c" + NL +
        "p sp 3 2" + NL +
        "a 1 2 1.0" + NL +
        "a 3 1 1.0" + NL;

    @Test
    public void testUndirected()
        throws UnsupportedEncodingException, ExportException
    {
        Graph<String, DefaultEdge> g = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
        g.addVertex(V1);
        g.addVertex(V2);
        g.addEdge(V1, V2);
        g.addVertex(V3);
        g.addEdge(V3, V1);

        DIMACSExporter<String, DefaultEdge> exporter = new DIMACSExporter<>();
        exporter.setFormat(DIMACSFormat.SHORTEST_PATH);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        exporter.exportGraph(g, os);
        String res = new String(os.toByteArray(), "UTF-8");
        assertEquals(UNDIRECTED, res);
    }

    @Test
    public void testUnweightedUndirected()
        throws UnsupportedEncodingException, ExportException
    {
        Graph<String, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
        g.addVertex(V1);
        g.addVertex(V2);
        g.addEdge(V1, V2);
        g.addVertex(V3);
        g.addEdge(V3, V1);

        DIMACSExporter<String, DefaultEdge> exporter = new DIMACSExporter<>();
        exporter.setFormat(DIMACSFormat.SHORTEST_PATH);
        exporter.setParameter(DIMACSExporter.Parameter.EXPORT_EDGE_WEIGHTS, true);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        exporter.exportGraph(g, os);
        String res = new String(os.toByteArray(), "UTF-8");
        assertEquals(UNDIRECTED_AS_UNWEIGHTED, res);
    }

    @Test
    public void testWeightedUndirected()
        throws UnsupportedEncodingException, ExportException
    {
        SimpleGraph<String, DefaultWeightedEdge> g =
            new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        g.addVertex(V1);
        g.addVertex(V2);
        g.addVertex(V3);
        DefaultWeightedEdge e1 = g.addEdge(V1, V2);
        g.setEdgeWeight(e1, 2.0);
        DefaultWeightedEdge e2 = g.addEdge(V3, V1);
        g.setEdgeWeight(e2, 5.0);

        DIMACSExporter<String, DefaultWeightedEdge> exporter = new DIMACSExporter<>();
        exporter.setFormat(DIMACSFormat.SHORTEST_PATH);
        exporter.setParameter(DIMACSExporter.Parameter.EXPORT_EDGE_WEIGHTS, true);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        exporter.exportGraph(g, os);
        String res = new String(os.toByteArray(), "UTF-8");
        assertEquals(UNDIRECTED_WEIGHTED, res);
    }

    @Test
    public void testParameters()
    {
        DIMACSExporter<String, DefaultWeightedEdge> exporter = new DIMACSExporter<>();
        assertFalse(exporter.isParameter(DIMACSExporter.Parameter.EXPORT_EDGE_WEIGHTS));
        exporter.setParameter(DIMACSExporter.Parameter.EXPORT_EDGE_WEIGHTS, true);
        assertTrue(exporter.isParameter(DIMACSExporter.Parameter.EXPORT_EDGE_WEIGHTS));
        exporter.setParameter(DIMACSExporter.Parameter.EXPORT_EDGE_WEIGHTS, false);
        assertFalse(exporter.isParameter(DIMACSExporter.Parameter.EXPORT_EDGE_WEIGHTS));
    }

    @Test
    public void testDefaultFormat()
    {
        DIMACSExporter<String, DefaultWeightedEdge> exporter = new DIMACSExporter<>();
        assertEquals(DIMACSFormat.MAX_CLIQUE, exporter.getFormat());
    }

}
