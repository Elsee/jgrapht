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
package org.jgrapht;

/**
 * A graph type.
 * 
 * <p>
 * The graph type describes various properties of a graph such as whether it is undirected, whether
 * it contain self-loops (edges with the same source and target vertices), whether
 * it contain parallel-edges (multiple edges with the same source and target) and whether it is
 * weighted or not.
 * 
 * @author Dimitrios Michail
 */
public interface GraphType
{
    /**
     * Returns true if all edges of the graph are undirected, false otherwise.
     * 
     * @return true if all edges of the graph are undirected, false otherwise
     */
    boolean isUndirected();

    /**
     * Returns <code>true</code> if and only if multiple edges are allowed in this graph. The
     * meaning of multiple edges is that there can be many edges going from vertex v1 to vertex v2.
     *
     * @return <code>true</code> if and only if multiple edges are allowed.
     */
    boolean isAllowingMultipleEdges();

    /**
     * Returns <code>true</code> if and only if self-loops are allowed in this graph. A self loop is
     * an edge that its source and target vertices are the same.
     *
     * @return <code>true</code> if and only if graph self-loops are allowed.
     */
    boolean isAllowingSelfLoops();

    /**
     * Returns <code>true</code> if and only if cycles are allowed in this graph.
     *
     * @return <code>true</code> if and only if graph cycles are allowed.
     */
    boolean isAllowingCycles();

    /**
     * Returns <code>true</code> if and only if the graph supports edge weights.
     *
     * @return <code>true</code> if the graph supports edge weights, <code>false</code> otherwise.
     */
    boolean isWeighted();

    /**
     * Returns <code>true</code> if the graph is simple, <code>false</code> otherwise.
     * 
     * @return <code>true</code> if the graph is simple, <code>false</code> otherwise
     */
    boolean isSimple();

    /**
     * Returns <code>true</code> if the graph is a pseudograph, <code>false</code> otherwise.
     * 
     * @return <code>true</code> if the graph is a pseudograph, <code>false</code> otherwise
     */
    boolean isPseudograph();

    /**
     * Returns <code>true</code> if the graph is a multigraph, <code>false</code> otherwise.
     * 
     * @return <code>true</code> if the graph is a multigraph, <code>false</code> otherwise
     */
    boolean isMultigraph();

    /**
     * Returns <code>true</code> if the graph is modifiable, <code>false</code> otherwise.
     * 
     * @return <code>true</code> if the graph is modifiable, <code>false</code> otherwise
     */
    boolean isModifiable();

    /**
     * Create an undirected variant of the current graph type.
     * 
     * @return an undirected variant of the current graph type
     */
    GraphType asUndirected();

    /**
     * Create an unweighted variant of the current graph type.
     * 
     * @return an unweighted variant of the current graph type
     */
    GraphType asUnweighted();

    /**
     * Create a weighted variant of the current graph type.
     * 
     * @return a weighted variant of the current graph type
     */
    GraphType asWeighted();

    /**
     * Create a modifiable variant of the current graph type.
     * 
     * @return a modifiable variant of the current graph type
     */
    GraphType asModifiable();

    /**
     * Create an unmodifiable variant of the current graph type.
     * 
     * @return a unmodifiable variant of the current graph type
     */
    GraphType asUnmodifiable();
}
