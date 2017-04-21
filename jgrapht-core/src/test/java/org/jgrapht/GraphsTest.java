/*
 * (C) Copyright 2003-2017, by Christoph Zauner and Contributors.
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
package org.jgrapht;

import org.jgrapht.graph.Pseudograph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.TestEdge;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christoph Zauner
 */
public class GraphsTest
{

    //@formatter:off
    /**
     * Graph before removing X:
     *
     *             +--> C
     *             |
     * A +--> B +--+
     *             |
     *             +--> D
     *
     * Expected graph after removing X:
     *
     *             +--> C
     *             |
     * A +--> B +--+
     *             |
     *             +--> D
     */
    //@formatter:on
    @Test
    public void removeVertex_vertexNotFound()
    {

        Graph<String, TestEdge> graph =
            new SimpleGraph<String, TestEdge>(TestEdge.class);

        String a = "A";
        String b = "B";
        String c = "C";
        String d = "D";
        String x = "X";

        graph.addVertex(a);
        graph.addVertex(b);
        graph.addVertex(c);
        graph.addVertex(d);

        graph.addEdge(a, b);
        graph.addEdge(b, c);
        graph.addEdge(b, d);

        Graph<String, TestEdge> expectedGraph =
            new SimpleGraph<String, TestEdge>(TestEdge.class);

        expectedGraph.addVertex(a);
        expectedGraph.addVertex(b);
        expectedGraph.addVertex(c);
        expectedGraph.addVertex(d);

        expectedGraph.addEdge(a, b);
        expectedGraph.addEdge(b, c);
        expectedGraph.addEdge(b, d);

        boolean vertexHasBeenRemoved = Graphs.removeVertexAndPreserveConnectivity(graph, x);

        Assert.assertEquals(expectedGraph, graph);
        Assert.assertFalse(vertexHasBeenRemoved);
    }

    //@formatter:off
    /**
     * Graph before removing B:
     *
     *             +-- C
     *             |
     * A +--> B +--+
     *             |
     *             +-- D
     *
     */
    //@formatter:on
    @Test
    public void removeVertex00()
    {

        Graph<String, TestEdge> graph =
            new Pseudograph<String, TestEdge>(TestEdge.class);

        String a = "A";
        String b = "B";
        String c = "C";
        String d = "D";

        graph.addVertex(a);
        graph.addVertex(b);
        graph.addVertex(c);
        graph.addVertex(d);

        graph.addEdge(a, b);
        graph.addEdge(b, c);
        graph.addEdge(b, d);

        List<String> predessors = Graphs.predecessorListOf(graph, b);

        Graph<String, TestEdge> expectedGraph =
            new Pseudograph<String, TestEdge>(TestEdge.class);

        expectedGraph.addVertex(a);
        expectedGraph.addVertex(c);
        expectedGraph.addVertex(d);

        expectedGraph.addEdge(a, a);
        expectedGraph.addEdge(a, c);
        expectedGraph.addEdge(a, d);
        expectedGraph.addEdge(c, a);
        expectedGraph.addEdge(c, c);
        expectedGraph.addEdge(c, d);
        expectedGraph.addEdge(d, a);
        expectedGraph.addEdge(d, c);
        expectedGraph.addEdge(d, d);

        boolean vertexHasBeenRemoved = Graphs.removeVertexAndPreserveConnectivity(graph, b);

        Assert.assertEquals(expectedGraph, graph);
        Assert.assertTrue(vertexHasBeenRemoved);
    }

    //@formatter:off
    /**
     * Graph before removing A:
     *
     * A +--> B
     *
     * Expected graph after removing A:
     *
     * B
     */
    //@formatter:on
    @Test
    public void removeVertex01()
    {

        Graph<String, TestEdge> graph =
            new Pseudograph<String, TestEdge>(TestEdge.class);

        String a = "A";
        String b = "B";

        graph.addVertex(a);
        graph.addVertex(b);

        graph.addEdge(a, b);

        Graph<String, TestEdge> expectedGraph =
            new Pseudograph<String, TestEdge>(TestEdge.class);

        expectedGraph.addVertex(b);
        expectedGraph.addEdge(b, b);

        boolean vertexHasBeenRemoved = Graphs.removeVertexAndPreserveConnectivity(graph, a);

        Assert.assertEquals(expectedGraph, graph);
        Assert.assertTrue(vertexHasBeenRemoved);
    }

    //@formatter:off
    /**
     * Input:
     *
     * A (source, not part of graph)
     * B (target, already part of graph)
     * C (target, not part of graph)
     *
     * Expected output:
     *
     *      +--> B
     *      |
     * A +--+
     *      |
     *      +--> C
     */
    //@formatter:on
    @Test
    public void addOutgoingEdges()
    {

        SimpleGraph<String, TestEdge> graph =
            new SimpleGraph<String, TestEdge>(TestEdge.class);

        String a = "A";
        String b = "B";
        String c = "C";

        graph.addVertex(b);

        Graph<String, TestEdge> expectedGraph =
            new SimpleGraph<String, TestEdge>(TestEdge.class);

        expectedGraph.addVertex(a);
        expectedGraph.addVertex(b);
        expectedGraph.addVertex(c);

        expectedGraph.addEdge(a, b);
        expectedGraph.addEdge(a, c);

        List<String> targets = new ArrayList<String>();
        targets.add(b);
        targets.add(c);

        Graphs.addOutgoingEdges(graph, a, targets);

        Assert.assertEquals(expectedGraph, graph);
    }

    //@formatter:off
    /**
     * Input:
     *
     * A (target, not part of graph)
     * B (source, already part of graph)
     * C (source, not part of graph)
     *
     * Expected output:
     *
     *      +--+ B
     *      |
     * A <--+
     *      |
     *      +--+ C
     */
    //@formatter:on
    @Test
    public void addIncomingEdges()
    {

        SimpleGraph<String, TestEdge> graph =
            new SimpleGraph<String, TestEdge>(TestEdge.class);

        String a = "A";
        String b = "B";
        String c = "C";

        graph.addVertex(b);

        Graph<String, TestEdge> expectedGraph =
            new SimpleGraph<String, TestEdge>(TestEdge.class);

        expectedGraph.addVertex(a);
        expectedGraph.addVertex(b);
        expectedGraph.addVertex(c);

        expectedGraph.addEdge(b, a);
        expectedGraph.addEdge(c, a);

        List<String> targets = new ArrayList<String>();
        targets.add(b);
        targets.add(c);

        Graphs.addIncomingEdges(graph, a, targets);

        Assert.assertEquals(expectedGraph, graph);
    }

    //@formatter:off
    /**
     * Input:
     *
     *             +--> C
     *             |
     * A +--> B +--+
     *             |
     *             +--> D
     */
    //@formatter:on
    @Test
    public void vertexHasChildren_B()
    {

        SimpleGraph<String, TestEdge> graph =
            new SimpleGraph<String, TestEdge>(TestEdge.class);

        String a = "A";
        String b = "B";
        String c = "C";
        String d = "D";

        graph.addVertex(a);
        graph.addVertex(b);
        graph.addVertex(c);
        graph.addVertex(d);

        graph.addEdge(a, b);
        graph.addEdge(b, c);
        graph.addEdge(b, d);

        Assert.assertTrue(Graphs.vertexHasSuccessors(graph, b));
    }

    //@formatter:off
    /**
     * Input:
     *
     *             +--> C
     *             |
     * A +--> B +--+
     *             |
     *             +--> D
     */
    //@formatter:on
    @Test
    public void vertexHasChildren_C()
    {

        SimpleGraph<String, TestEdge> graph =
            new SimpleGraph<String, TestEdge>(TestEdge.class);

        String a = "A";
        String b = "B";
        String c = "C";
        String d = "D";

        graph.addVertex(a);
        graph.addVertex(b);
        graph.addVertex(c);
        graph.addVertex(d);

        graph.addEdge(a, b);
        graph.addEdge(b, c);
        graph.addEdge(b, d);

        Assert.assertTrue(Graphs.vertexHasSuccessors(graph, c));
    }

    //@formatter:off
    /**
     * Input:
     *
     *             +--> C
     *             |
     * A +--> B +--+
     *             |
     *             +--> D
     */
    //@formatter:on
    @Test
    public void vertexHasParents_B()
    {

        SimpleGraph<String, TestEdge> graph =
            new SimpleGraph<String, TestEdge>(TestEdge.class);

        String a = "A";
        String b = "B";
        String c = "C";
        String d = "D";

        graph.addVertex(a);
        graph.addVertex(b);
        graph.addVertex(c);
        graph.addVertex(d);

        graph.addEdge(a, b);
        graph.addEdge(b, c);
        graph.addEdge(b, d);

        Assert.assertTrue(Graphs.vertexHasPredecessors(graph, b));
    }
}

// End GraphsTest.java
