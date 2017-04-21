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
package org.jgrapht.alg.shortestpath;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.VertexFactory;
import org.jgrapht.alg.util.ToleranceDoubleComparator;
import org.jgrapht.graph.ClassBasedVertexFactory;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Johnson's all pairs shortest paths algorithm.
 * 
 * <p>
 * Finds the shortest paths between all pairs of vertices in a sparse graph. Edge weights can be
 * negative, but no negative-weight cycles may exist. It first executes the Bellman-Ford algorithm
 * to compute a transformation of the input graph that removes all negative weights, allowing
 * Dijkstra's algorithm to be used on the transformed graph.
 * 
 * <p>
 * Running time is O(|V||E| + |V|^2 log|V|).
 * 
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 *
 * @author Dimitrios Michail
 * @since February 2017
 */
public class JohnsonShortestPaths<V, E>
    extends BaseShortestPathAlgorithm<V, E>
{
    private Map<V, SingleSourcePaths<V, E>> paths;
    private VertexFactory<V> vertexFactory;
    private final Comparator<Double> comparator;

    /**
     * Construct a new instance.
     * 
     * @param graph the input graph
     * @param vertexClass the graph vertex class
     */
    public JohnsonShortestPaths(Graph<V, E> graph, Class<? extends V> vertexClass)
    {
        this(graph, new ClassBasedVertexFactory<>(vertexClass));
    }

    /**
     * Construct a new instance.
     * 
     * @param graph the input graph
     * @param vertexFactory the vertex factory of the graph
     */
    public JohnsonShortestPaths(Graph<V, E> graph, VertexFactory<V> vertexFactory)
    {
        this(graph, vertexFactory, ToleranceDoubleComparator.DEFAULT_EPSILON);
    }

    /**
     * Construct a new instance.
     * 
     * @param graph the input graph
     * @param vertexFactory the vertex factory of the graph
     * @param epsilon tolerance when comparing floating point values
     */
    public JohnsonShortestPaths(Graph<V, E> graph, VertexFactory<V> vertexFactory, double epsilon)
    {
        super(graph);
        this.vertexFactory = Objects.requireNonNull(vertexFactory, "Vertex factory cannot be null");
        this.comparator = new ToleranceDoubleComparator(epsilon);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GraphPath<V, E> getPath(V source, V sink)
    {
        if (!graph.containsVertex(source)) {
            throw new IllegalArgumentException(GRAPH_MUST_CONTAIN_THE_SOURCE_VERTEX);
        }
        if (!graph.containsVertex(sink)) {
            throw new IllegalArgumentException(GRAPH_MUST_CONTAIN_THE_SINK_VERTEX);
        }
        run();
        return paths.get(source).getPath(sink);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getPathWeight(V source, V sink)
    {
        if (!graph.containsVertex(source)) {
            throw new IllegalArgumentException(GRAPH_MUST_CONTAIN_THE_SOURCE_VERTEX);
        }
        if (!graph.containsVertex(sink)) {
            throw new IllegalArgumentException(GRAPH_MUST_CONTAIN_THE_SINK_VERTEX);
        }
        run();
        return paths.get(source).getWeight(sink);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SingleSourcePaths<V, E> getPaths(V source)
    {
        run();
        return paths.get(source);
    }

    /**
     * Executes the actual algorithm.
     */
    private void run()
    {
        if (paths != null) {
            return;
        }

        runUndirected(graph);
    }

    /**
     * Executes the algorithm for undirected graphs.
     * 
     * @param g the input graph
     */
    private void runUndirected(Graph<V, E> g)
    {
        /*
         * Check that no negative edge weight exists, otherwise there is a negative-weight cycle.
         */
        for (E e : g.edgeSet()) {
            double w = g.getEdgeWeight(e);
            if (comparator.compare(w, 0.0) < 0) {
                throw new RuntimeException(GRAPH_CONTAINS_A_NEGATIVE_WEIGHT_CYCLE);
            }
        }

        /*
         * Run Dijkstra for all vertices.
         */
        paths = new HashMap<>();
        DijkstraShortestPath<V, E> dijkstraAlg = new DijkstraShortestPath<>(g);
        for (V v : g.vertexSet()) {
            paths.put(v, dijkstraAlg.getPaths(v));
        }
    }

}
