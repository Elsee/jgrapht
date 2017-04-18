/*
 * (C) Copyright 2016-2017, by Andrew Gainer-Dewar and Contributors.
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

import org.jgrapht.EnhancedTestCase;
import org.jgrapht.Graph;

import java.util.Iterator;

/**
 * Unit tests for MaskEdgeSet.
 *
 * @author Andrew Gainer-Dewar
 */
public class MaskEdgeSetTest
    extends EnhancedTestCase
{
    private String v1 = "v1";
    private String v2 = "v2";
    private String v3 = "v3";
    private String v4 = "v4";
    private DefaultEdge e1, e2, e3;

    private MaskEdgeSet<String, DefaultEdge> testMaskedEdgeSet;

    @Override
    protected void setUp()
    {
        Graph<String, DefaultEdge> undirected = new SimpleGraph<>(DefaultEdge.class);

        undirected.addVertex(v1);
        undirected.addVertex(v2);
        undirected.addVertex(v3);
        undirected.addVertex(v4);

        e1 = undirected.addEdge(v1, v2);
        e2 = undirected.addEdge(v2, v3);
        e3 = undirected.addEdge(v2, v4);

        testMaskedEdgeSet =
            new MaskEdgeSet<>(undirected, undirected.edgeSet(), v -> v == v1, e -> e == e2);
    }

    public void testContains()
    {
        assertFalse(testMaskedEdgeSet.contains(e1));
        assertFalse(testMaskedEdgeSet.contains(e2));
        assertTrue(testMaskedEdgeSet.contains(e3));
        assertFalse(testMaskedEdgeSet.contains(v1));
    }

    public void testSize()
    {
        assertEquals(1, testMaskedEdgeSet.size());
    }

    public void testIterator()
    {
        Iterator<DefaultEdge> it = testMaskedEdgeSet.iterator();
        assertTrue(it.hasNext());
        assertEquals(e3, it.next());
        assertFalse(it.hasNext());
    }
}
