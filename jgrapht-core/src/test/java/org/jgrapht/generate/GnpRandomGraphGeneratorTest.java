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
package org.jgrapht.generate;

import org.jgrapht.*;
import org.jgrapht.graph.*;

import junit.framework.*;

/**
 * .
 *
 * @author Dimitrios Michail
 * @since September 2016
 */
public class GnpRandomGraphGeneratorTest
    extends TestCase
{

    private static final long SEED = 5;

    public void testZeroNodes()
    {
        GraphGenerator<Integer, DefaultEdge, Integer> gen = new GnpRandomGraphGenerator<>(0, 1d);
        Graph<Integer, DefaultEdge> g = new Pseudograph<>(DefaultEdge.class);
        gen.generateGraph(g, new IntegerVertexFactory(), null);
        assertEquals(0, g.edgeSet().size());
        assertEquals(0, g.vertexSet().size());
    }

    public void testBadParameters()
    {
        try {
            new GnpRandomGraphGenerator<>(-10, 0);
            fail("Bad parameter");
        } catch (IllegalArgumentException e) {
        }

        try {
            new GnpRandomGraphGenerator<>(10, -1.0);
            fail("Bad parameter");
        } catch (IllegalArgumentException e) {
        }

        try {
            new GnpRandomGraphGenerator<>(10, 2.0);
            fail("Bad parameter");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testUndirectedGraphGnp1()
    {
        GraphGenerator<Integer, DefaultEdge, Integer> gen =
            new GnpRandomGraphGenerator<>(6, 0.5, SEED);
        Graph<Integer, DefaultEdge> g = new Pseudograph<>(DefaultEdge.class);
        gen.generateGraph(g, new IntegerVertexFactory(), null);

        assertEquals(6, g.vertexSet().size());
        assertTrue(g.containsEdge(1, 3));
        assertTrue(g.containsEdge(1, 4));
        assertTrue(g.containsEdge(1, 5));
        assertTrue(g.containsEdge(1, 6));
        assertTrue(g.containsEdge(2, 4));
        assertTrue(g.containsEdge(2, 6));
        assertTrue(g.containsEdge(3, 6));
        assertTrue(g.containsEdge(4, 6));
        assertTrue(g.containsEdge(5, 6));

        assertEquals(9, g.edgeSet().size());
    }

    public void testUndirectedGraphGnp2()
    {
        GraphGenerator<Integer, DefaultEdge, Integer> gen =
            new GnpRandomGraphGenerator<>(6, 1.0, SEED);
        Graph<Integer, DefaultEdge> g = new Pseudograph<>(DefaultEdge.class);
        gen.generateGraph(g, new IntegerVertexFactory(), null);

        assertEquals(6, g.vertexSet().size());
        assertTrue(g.containsEdge(1, 2));
        assertTrue(g.containsEdge(1, 3));
        assertTrue(g.containsEdge(1, 4));
        assertTrue(g.containsEdge(1, 5));
        assertTrue(g.containsEdge(1, 6));
        assertTrue(g.containsEdge(2, 3));
        assertTrue(g.containsEdge(2, 4));
        assertTrue(g.containsEdge(2, 5));
        assertTrue(g.containsEdge(2, 6));
        assertTrue(g.containsEdge(3, 4));
        assertTrue(g.containsEdge(3, 5));
        assertTrue(g.containsEdge(3, 6));
        assertTrue(g.containsEdge(4, 5));
        assertTrue(g.containsEdge(4, 6));
        assertTrue(g.containsEdge(5, 6));

        assertEquals(15, g.edgeSet().size());
    }

    public void testUndirectedGraphGnp3()
    {
        GraphGenerator<Integer, DefaultEdge, Integer> gen =
            new GnpRandomGraphGenerator<>(6, 0.3, SEED);
        Graph<Integer, DefaultEdge> g = new Pseudograph<>(DefaultEdge.class);
        gen.generateGraph(g, new IntegerVertexFactory(), null);

        assertEquals(6, g.vertexSet().size());
        assertTrue(g.containsEdge(1, 3));
        assertTrue(g.containsEdge(2, 4));
        assertTrue(g.containsEdge(2, 6));
        assertTrue(g.containsEdge(3, 6));

        assertEquals(4, g.edgeSet().size());
    }

    public void testUndirectedGraphGnp4WithLoops()
    {
        final boolean allowLoops = true;
        GraphGenerator<Integer, DefaultEdge, Integer> gen =
            new GnpRandomGraphGenerator<>(6, 0.3, SEED, allowLoops);
        Graph<Integer, DefaultEdge> g = new Pseudograph<>(DefaultEdge.class);
        gen.generateGraph(g, new IntegerVertexFactory(), null);

        assertEquals(6, g.vertexSet().size());
        assertTrue(g.containsEdge(1, 2));
        assertTrue(g.containsEdge(2, 2));
        assertTrue(g.containsEdge(2, 4));
        assertTrue(g.containsEdge(3, 3));
        assertTrue(g.containsEdge(4, 6));
        assertTrue(g.containsEdge(5, 5));

        assertEquals(6, g.edgeSet().size());
    }

    private class IntegerVertexFactory
        implements VertexFactory<Integer>
    {
        private int id;

        @Override
        public Integer createVertex()
        {
            return ++id;
        }
    };

}

// End GnpGraphGraphGeneratorTest.java
