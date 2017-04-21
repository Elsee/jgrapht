/*
 * (C) Copyright 2006-2017, by John V Sichi and Contributors.
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
package org.jgrapht.alg.shortestpath;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.SingleSourcePaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedPseudograph;

import java.util.Arrays;
import java.util.List;

/**
 * .
 *
 * @author John V. Sichi
 */
public class BellmanFordShortestPathTest
    extends ShortestPathTestCase
{
    // ~ Methods ----------------------------------------------------------------

    public void testUndirected()
    {
        SingleSourcePaths<String, DefaultWeightedEdge> tree;
        Graph<String, DefaultWeightedEdge> g = create();

        tree = new BellmanFordShortestPath<>(g).getPaths(V3);

        // find best path
        assertEquals(
            Arrays.asList(new DefaultWeightedEdge[] { e13, e12, e24, e45 }),
            tree.getPath(V5).getEdgeList());
        assertEquals(3.0, tree.getPath(V1).getWeight(), 1e-9);
        assertEquals(5.0, tree.getPath(V2).getWeight(), 1e-9);
        assertEquals(0.0, tree.getPath(V3).getWeight(), 1e-9);
        assertEquals(10.0, tree.getPath(V4).getWeight(), 1e-9);
        assertEquals(15.0, tree.getPath(V5).getWeight(), 1e-9);
    }

    @Override
    protected List<DefaultWeightedEdge> findPathBetween(
        Graph<String, DefaultWeightedEdge> g, String src, String dest)
    {
        return new BellmanFordShortestPath<>(g).getPaths(src).getPath(dest).getEdgeList();
    }

    public void testNegativeEdgeUndirectedGraph()
    {
        WeightedPseudograph<String, DefaultWeightedEdge> g =
            new WeightedPseudograph<>(DefaultWeightedEdge.class);
        g.addVertex("w");
        g.addVertex("y");
        g.addVertex("x");
        g.setEdgeWeight(g.addEdge("w", "y"), 1);
        g.setEdgeWeight(g.addEdge("y", "x"), 1);
        g.setEdgeWeight(g.addEdge("y", "x"), -1);
        try {
            new BellmanFordShortestPath<>(g).getPaths("w");
            fail("Negative-weight cycle not detected");
        } catch (RuntimeException e) {
            assertEquals("Graph contains a negative-weight cycle", e.getMessage());
        }
    }

}

// End BellmanFordShortestPathTest.java
