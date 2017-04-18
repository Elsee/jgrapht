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
package org.jgrapht.alg.shortestpath;

import junit.framework.TestCase;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.VertexFactory;
import org.jgrapht.generate.GnmRandomGraphGenerator;
import org.jgrapht.generate.GraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.Pseudograph;
import org.jgrapht.graph.WeightedPseudograph;

import java.util.Random;

/**
 * @author Dimitrios Michail
 */
public class BidirectionalDijkstraShortestPathTest
    extends TestCase
{

    public void testGraphUndirected()
    {
        WeightedPseudograph<String, DefaultWeightedEdge> g =
            new WeightedPseudograph<>(DefaultWeightedEdge.class);

        g.addVertex("1");
        g.addVertex("2");
        g.addVertex("3");
        g.addVertex("4");
        g.addVertex("5");
        g.setEdgeWeight(g.addEdge("1", "2"), 3.0);
        g.setEdgeWeight(g.addEdge("3", "1"), 3.0);
        g.setEdgeWeight(g.addEdge("2", "4"), 3.0);
        g.setEdgeWeight(g.addEdge("3", "5"), 5.0);
        g.setEdgeWeight(g.addEdge("5", "4"), 5.0);

        GraphPath<String, DefaultWeightedEdge> p =
            new BidirectionalDijkstraShortestPath<>(g).getPath("3", "4");

        assertEquals("3", p.getStartVertex());
        assertEquals("4", p.getEndVertex());
        assertEquals(3, p.getLength());
        assertEquals(9.0, p.getWeight());
        assertEquals("3", p.getVertexList().get(0));
        assertEquals("1", p.getVertexList().get(1));
        assertEquals("2", p.getVertexList().get(2));
        assertEquals("4", p.getVertexList().get(3));
        assertEquals(g.getEdge("3", "1"), p.getEdgeList().get(0));
        assertEquals(g.getEdge("1", "2"), p.getEdgeList().get(1));
        assertEquals(g.getEdge("2", "4"), p.getEdgeList().get(2));
    }

    public void testSourceTargetEqualUndirected()
    {
        WeightedPseudograph<String, DefaultWeightedEdge> g =
            new WeightedPseudograph<>(DefaultWeightedEdge.class);

        g.addVertex("1");
        g.addVertex("2");
        g.addVertex("3");
        g.addVertex("4");
        g.addVertex("5");
        g.setEdgeWeight(g.addEdge("1", "2"), 3.0);
        g.setEdgeWeight(g.addEdge("3", "1"), 3.0);
        g.setEdgeWeight(g.addEdge("2", "4"), 3.0);
        g.setEdgeWeight(g.addEdge("3", "5"), 5.0);
        g.setEdgeWeight(g.addEdge("5", "4"), 5.0);

        GraphPath<String, DefaultWeightedEdge> p =
            new BidirectionalDijkstraShortestPath<>(g).getPath("3", "3");

        assertEquals("3", p.getStartVertex());
        assertEquals("3", p.getEndVertex());
        assertEquals(0, p.getLength());
        assertEquals(0.0, p.getWeight());
        assertEquals("3", p.getVertexList().get(0));
        assertTrue(p.getEdgeList().isEmpty());
    }

    public void testRandomGraphsWeightedUndirected()
    {
        GraphGenerator<String, DefaultWeightedEdge, String> gen =
            new GnmRandomGraphGenerator<>(20, 100, 1);
        WeightedPseudograph<String, DefaultWeightedEdge> g =
            new WeightedPseudograph<>(DefaultWeightedEdge.class);
        gen.generateGraph(g, new VertexFactory<String>()
        {
            private int id = 1;

            @Override
            public String createVertex()
            {
                return Integer.toString(id++);
            }
        }, null);

        Random weightedGenerator = new Random(7);
        for (DefaultWeightedEdge e : g.edgeSet()) {
            g.setEdgeWeight(e, weightedGenerator.nextDouble());
        }

        for (String v : g.vertexSet()) {
            for (String u : g.vertexSet()) {
                GraphPath<String, DefaultWeightedEdge> p1 =
                    new DijkstraShortestPath<>(g).getPath(v, u);

                GraphPath<String, DefaultWeightedEdge> p2 =
                    new BidirectionalDijkstraShortestPath<>(g).getPath(v, u);

                if (p1 == null) {
                    assertNull(p2);
                } else if (p2 == null) {
                    assertNull(p1);
                } else {
                    assertEquals(p1.getLength(), p2.getLength());
                    assertEquals(p1.getWeight(), p2.getWeight(), 0.0001);
                    assertEquals(p2.getWeight(), computePathWeight(g, p2), 0.0001);
                    assertEquals(p1.getStartVertex(), p2.getStartVertex());
                    assertEquals(p1.getEndVertex(), p2.getEndVertex());
                }
            }
        }

    }

    public void testWrongParameters()
    {
        Pseudograph<String, DefaultEdge> g = new Pseudograph<>(DefaultEdge.class);

        g.addVertex("1");
        g.addVertex("2");
        g.addEdge("1", "2");

        try {
            new BidirectionalDijkstraShortestPath<>(g, -2.0).getPath("1", "2");
            fail("No!");
        } catch (IllegalArgumentException e) {
        }

        try {
            new BidirectionalDijkstraShortestPath<>(g, 2.0).getPath("3", "2");
            fail("No!");
        } catch (IllegalArgumentException e) {
        }

        try {
            new BidirectionalDijkstraShortestPath<>(g, 2.0).getPath("2", "3");
            fail("No!");
        } catch (IllegalArgumentException e) {
        }

        try {
            new BidirectionalDijkstraShortestPath<>(null).getPath("1", "1");
            fail("No!");
        } catch (NullPointerException e) {
        }

        try {
            new BidirectionalDijkstraShortestPath<>(g).getPath(null, "1");
            fail("No!");
        } catch (IllegalArgumentException e) {
        }

        try {
            new BidirectionalDijkstraShortestPath<>(g).getPath("1", null);
            fail("No!");
        } catch (IllegalArgumentException e) {
        }
    }

    private <V, E> double computePathWeight(Graph<V, E> g, GraphPath<V, E> path)
    {
        if (path.getEdgeList().isEmpty()) {
            if (path.getStartVertex().equals(path.getEndVertex())) {
                return 0d;
            } else {
                return Double.POSITIVE_INFINITY;
            }
        }
        double total = 0d;
        for (E e : path.getEdgeList()) {
            total += g.getEdgeWeight(e);
        }
        return total;
    }

}

// End BidirectionalDijkstraShortestPathTest.java
