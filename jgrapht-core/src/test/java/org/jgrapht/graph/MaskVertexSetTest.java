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
 * Unit tests for MaskVertexSet.
 *
 * @author Andrew Gainer-Dewar
 */
public class MaskVertexSetTest
    extends EnhancedTestCase
{
    private Graph<String, DefaultEdge> undirected;
    private String v1 = "v1";
    private String v2 = "v2";
    private String v3 = "v3";
    private String v4 = "v4";
    private DefaultEdge e1;

    private MaskVertexSet<String> testMaskVertexSet;

    @Override
    protected void setUp()
    {
        undirected = new SimpleGraph<>(DefaultEdge.class);

        undirected.addVertex(v1);
        undirected.addVertex(v2);
        undirected.addVertex(v3);
        undirected.addVertex(v4);

        e1 = undirected.addEdge(v1, v2);
        undirected.addEdge(v2, v3);

        testMaskVertexSet = new MaskVertexSet<>(undirected.vertexSet(), v -> v == v1);
    }

    public void testContains()
    {
        assertFalse(testMaskVertexSet.contains(v1));
        assertTrue(testMaskVertexSet.contains(v2));

        assertFalse(testMaskVertexSet.contains(e1));
    }

    public void testSize()
    {
        assertEquals(3, testMaskVertexSet.size());
    }

    public void testIterator()
    {
        Iterator<String> it = testMaskVertexSet.iterator();
        assertTrue(it.hasNext());
        assertEquals(v2, it.next());
        assertTrue(it.hasNext());
        assertEquals(v3, it.next());
        assertTrue(it.hasNext());
        assertEquals(v4, it.next());
        assertFalse(it.hasNext());
    }
}
