/*
 * (C) Copyright 2003-2017, by Joris Kinable and Contributors.
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
package org.jgrapht.graph;

import junit.framework.TestCase;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.util.WeightCombiner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Unit test for the {@link AsGraphUnion} class.
 *
 * @author Joris Kinable
 * @since Aug 24, 2015
 */
public class AsGraphUnionTest
    extends TestCase
{

    // ~ Instance fields --------------------------------------------------------

    private String v0 = "v0";
    private String v1 = "v1";
    private String v2 = "v2";
    private String v3 = "v3";
    private String v4 = "v4";

    private DefaultWeightedEdge e1 = new DefaultWeightedEdge(); // (v0,v1);
    private DefaultWeightedEdge e2 = new DefaultWeightedEdge(); // (v1,v4);
    private DefaultWeightedEdge e3 = new DefaultWeightedEdge(); // (v4,v0);
    private DefaultWeightedEdge e4 = new DefaultWeightedEdge(); // (v1,v2);
    private DefaultWeightedEdge e5 = new DefaultWeightedEdge(); // (v2,v3);
    private DefaultWeightedEdge e6 = new DefaultWeightedEdge(); // (v3,v4);
    private DefaultWeightedEdge e7 = new DefaultWeightedEdge(); // (v4,v1);
    private DefaultWeightedEdge e8 = new DefaultWeightedEdge(); // (v4,v4);

    Graph<String, DefaultWeightedEdge> undirectedGraph1;
    Graph<String, DefaultWeightedEdge> undirectedGraph2;
    // ~ Methods ----------------------------------------------------------------

    @Override
    public void setUp()
    {
        undirectedGraph1 = new WeightedPseudograph<>(DefaultWeightedEdge.class);
        undirectedGraph2 = new WeightedPseudograph<>(DefaultWeightedEdge.class);

        Graphs.addAllVertices(undirectedGraph1, Arrays.asList(v0, v1, v4));
        Graphs.addAllVertices(undirectedGraph2, Arrays.asList(v1, v2, v3, v4));

        undirectedGraph1.addEdge(v0, v1, e1);
        undirectedGraph1.addEdge(v1, v4, e2);
        undirectedGraph1.addEdge(v4, v0, e3);
        undirectedGraph1.addEdge(v4, v4, e8);

        undirectedGraph2.addEdge(v4, v1, e7);
        undirectedGraph2.addEdge(v1, v2, e4);
        undirectedGraph2.addEdge(v2, v3, e5);
        undirectedGraph2.addEdge(v3, v4, e6);
    }

    /**
     * Create and test the union of two Undirected Graphs
     */
    public void testUndirectedGraphUnion()
    {
        Graph<String, DefaultWeightedEdge> graphUnion =
            new AsGraphUnion<>(undirectedGraph1, undirectedGraph2);

        assertTrue(graphUnion.getType().isUndirected());
        assertTrue(graphUnion.getType().isWeighted());
        assertFalse(graphUnion.getType().isModifiable());

        assertEquals(new HashSet<>(Arrays.asList(v0, v1, v2, v3, v4)), graphUnion.vertexSet());
        assertEquals(
            new HashSet<>(Arrays.asList(e1, e2, e3, e4, e5, e6, e7, e8)), graphUnion.edgeSet());

        assertEquals(new HashSet<>(Arrays.asList(e1, e3)), graphUnion.edgesOf(v0));
        assertEquals(new HashSet<>(Arrays.asList(e1, e2, e4, e7)), graphUnion.edgesOf(v1));
        assertEquals(new HashSet<>(Arrays.asList(e4, e5)), graphUnion.edgesOf(v2));
        assertEquals(new HashSet<>(Arrays.asList(e5, e6)), graphUnion.edgesOf(v3));
        assertEquals(new HashSet<>(Arrays.asList(e2, e3, e6, e7, e8)), graphUnion.edgesOf(v4));

        assertEquals(2, graphUnion.degreeOf(v0));
        assertEquals(4, graphUnion.degreeOf(v1));
        assertEquals(2, graphUnion.degreeOf(v2));
        assertEquals(2, graphUnion.degreeOf(v3));
        assertEquals(6, graphUnion.degreeOf(v4));

        assertEquals(new HashSet<>(Arrays.asList(e1, e3)), graphUnion.incomingEdgesOf(v0));
        assertEquals(new HashSet<>(Arrays.asList(e1, e2, e4, e7)), graphUnion.incomingEdgesOf(v1));
        assertEquals(new HashSet<>(Arrays.asList(e4, e5)), graphUnion.incomingEdgesOf(v2));
        assertEquals(new HashSet<>(Arrays.asList(e5, e6)), graphUnion.incomingEdgesOf(v3));
        assertEquals(
            new HashSet<>(Arrays.asList(e2, e3, e6, e7, e8)), graphUnion.incomingEdgesOf(v4));

        assertEquals(2, graphUnion.inDegreeOf(v0));
        assertEquals(4, graphUnion.inDegreeOf(v1));
        assertEquals(2, graphUnion.inDegreeOf(v2));
        assertEquals(2, graphUnion.inDegreeOf(v3));
        assertEquals(6, graphUnion.inDegreeOf(v4));

        assertEquals(new HashSet<>(Arrays.asList(e1, e3)), graphUnion.outgoingEdgesOf(v0));
        assertEquals(new HashSet<>(Arrays.asList(e1, e2, e4, e7)), graphUnion.outgoingEdgesOf(v1));
        assertEquals(new HashSet<>(Arrays.asList(e4, e5)), graphUnion.outgoingEdgesOf(v2));
        assertEquals(new HashSet<>(Arrays.asList(e5, e6)), graphUnion.outgoingEdgesOf(v3));
        assertEquals(
            new HashSet<>(Arrays.asList(e2, e3, e6, e7, e8)), graphUnion.outgoingEdgesOf(v4));

        assertEquals(2, graphUnion.outDegreeOf(v0));
        assertEquals(4, graphUnion.outDegreeOf(v1));
        assertEquals(2, graphUnion.outDegreeOf(v2));
        assertEquals(2, graphUnion.outDegreeOf(v3));
        assertEquals(6, graphUnion.outDegreeOf(v4));

        assertTrue(graphUnion.getEdge(v1, v4) == e2);
        assertTrue(graphUnion.getEdge(v4, v1) == e2);
    }

    /**
     * Test the weight combiner for graphs having an edge in common.
     */
    public void testWeightCombiner()
    {
        // Create two graphs, both having the same vertices {0,1} and the same weighted edge (0,1)
        SimpleWeightedGraph<Integer, DefaultWeightedEdge> g1 =
            new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        Graphs.addAllVertices(g1, Arrays.asList(0, 1));
        DefaultWeightedEdge edge = g1.addEdge(0, 1);
        g1.setEdgeWeight(edge, 10);

        SimpleWeightedGraph<Integer, DefaultWeightedEdge> g2 =
            new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        Graphs.addAllVertices(g2, Arrays.asList(0, 1));
        g2.addEdge(0, 1, edge);
        // We need to create a mask of the second graph if we want to store the edge with a
        // different weight. Simply setting g2.setEdgeWeight(edge,20) would override the edge weight
        // for the same edge in g1 as well!
        Map<DefaultWeightedEdge, Double> weightMap = new HashMap<>();
        weightMap.put(edge, 20.0);
        Graph<Integer, DefaultWeightedEdge> g2Masked = new AsWeightedGraph<>(g2, weightMap);

        GraphUnion<Integer, DefaultWeightedEdge,
            Graph<Integer, DefaultWeightedEdge>> graphUnionSum =
                new GraphUnion<>(g1, g2Masked, WeightCombiner.SUM);
        assertEquals(30.0, graphUnionSum.getEdgeWeight(edge));
        GraphUnion<Integer, DefaultWeightedEdge,
            Graph<Integer, DefaultWeightedEdge>> graphUnionFirst =
                new GraphUnion<>(g1, g2Masked, WeightCombiner.FIRST);
        assertEquals(10.0, graphUnionFirst.getEdgeWeight(edge));
        GraphUnion<Integer, DefaultWeightedEdge,
            Graph<Integer, DefaultWeightedEdge>> graphUnionSecond =
                new GraphUnion<>(g1, g2Masked, WeightCombiner.SECOND);
        assertEquals(20.0, graphUnionSecond.getEdgeWeight(edge));
        GraphUnion<Integer, DefaultWeightedEdge,
            Graph<Integer, DefaultWeightedEdge>> graphUnionMax =
                new GraphUnion<>(g1, g2Masked, WeightCombiner.MAX);
        assertEquals(20.0, graphUnionMax.getEdgeWeight(edge));
        GraphUnion<Integer, DefaultWeightedEdge,
            Graph<Integer, DefaultWeightedEdge>> graphUnionMin =
                new GraphUnion<>(g1, g2Masked, WeightCombiner.MIN);
        assertEquals(10.0, graphUnionMin.getEdgeWeight(edge));
        GraphUnion<Integer, DefaultWeightedEdge,
            Graph<Integer, DefaultWeightedEdge>> graphUnionMult =
                new GraphUnion<>(g1, g2Masked, WeightCombiner.MULT);
        assertEquals(200.0, graphUnionMult.getEdgeWeight(edge));

        assertEquals(10.0, g1.getEdgeWeight(edge));
        assertEquals(10.0, g2.getEdgeWeight(edge));
        assertEquals(20.0, g2Masked.getEdgeWeight(edge));
    }

}
