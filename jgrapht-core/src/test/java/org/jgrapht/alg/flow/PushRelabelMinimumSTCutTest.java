/*
 * (C) Copyright 2016-2017, by Joris Kinable and Contributors.
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
package org.jgrapht.alg.flow;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.MinimumSTCutAlgorithm;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.Arrays;
import java.util.Set;

/**
 * @author Joris Kinable
 */
public class PushRelabelMinimumSTCutTest
    extends MinimumSourceSinkCutTest
{
    @Override
    MinimumSTCutAlgorithm<Integer, DefaultWeightedEdge> createSolver(
        Graph<Integer, DefaultWeightedEdge> network)
    {
        return new PushRelabelMFImpl<>(network);
    }

    public void testDisconnected1()
    {
        SimpleWeightedGraph<Integer, DefaultWeightedEdge> network =
            new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        Graphs.addAllVertices(network, Arrays.asList(0, 1, 2, 3, 4, 5));
        network.addEdge(2, 4);
        network.addEdge(3, 4);
        network.addEdge(1, 4);
        network.addEdge(0, 1);
        network.addEdge(2, 0);
        network.addEdge(1, 0);
        network.addEdge(4, 0);
        network.addEdge(4, 1);
        network.addEdge(1, 3);
        network.addEdge(4, 3);

        MinimumSTCutAlgorithm<Integer, DefaultWeightedEdge> prSolver = this.createSolver(network);
        double cutWeight = prSolver.calculateMinCut(0, 5);
        assertEquals(0d, cutWeight);
    }

    public void testDisconnected2()
    {
        SimpleWeightedGraph<Integer, DefaultWeightedEdge> network =
            new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        Graphs.addAllVertices(network, Arrays.asList(0, 1, 2));
        network.addEdge(0, 1);

        MinimumSTCutAlgorithm<Integer, DefaultWeightedEdge> prSolver = this.createSolver(network);
        double cutWeight = prSolver.calculateMinCut(0, 2);
        assertEquals(0d, cutWeight);
    }

    public void testRandomUndirectedGraphs()
    {
        for (int test = 0; test < NR_RANDOM_TESTS; test++) {
            Graph<Integer, DefaultWeightedEdge> network = generateUndirectedGraph();
            int source = 0;
            int sink = network.vertexSet().size() - 1;

            MinimumSTCutAlgorithm<Integer, DefaultWeightedEdge> prSolver =
                this.createSolver(network);
            MinimumSTCutAlgorithm<Integer, DefaultWeightedEdge> ekSolver =
                new EdmondsKarpMFImpl<>(network);

            double expectedCutWeight = ekSolver.calculateMinCut(source, sink);

            double cutWeight = prSolver.calculateMinCut(source, sink);
            Set<Integer> sourcePartition = prSolver.getSourcePartition();
            Set<Integer> sinkPartition = prSolver.getSinkPartition();
            Set<DefaultWeightedEdge> cutEdges = prSolver.getCutEdges();

            this.verifyUndirected(
                network, source, sink, expectedCutWeight, cutWeight, sourcePartition, sinkPartition,
                cutEdges);
        }
    }
}
