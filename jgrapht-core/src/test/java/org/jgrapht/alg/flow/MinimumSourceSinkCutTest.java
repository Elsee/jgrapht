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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Joris Kinable
 */
public abstract class MinimumSourceSinkCutTest
    extends MaximumFlowMinimumCutAlgorithmTestBase
{

    public static final int NR_RANDOM_TESTS = 20;

    abstract MinimumSTCutAlgorithm<Integer, DefaultWeightedEdge> createSolver(
        Graph<Integer, DefaultWeightedEdge> network);

    private void runTestUndirected(
        Graph<Integer, DefaultWeightedEdge> network, int source, int sink,
        double expectedCutWeight)
    {
        MinimumSTCutAlgorithm<Integer, DefaultWeightedEdge> mc = createSolver(network);
        double cutWeight = mc.calculateMinCut(source, sink);
        Set<Integer> sourcePartition = mc.getSourcePartition();
        Set<Integer> sinkPartition = mc.getSinkPartition();
        Set<DefaultWeightedEdge> cutEdges = mc.getCutEdges();

        this.verifyUndirected(
            network, source, sink, expectedCutWeight, cutWeight, sourcePartition, sinkPartition,
            cutEdges);
    }

    void verifyUndirected(
        Graph<Integer, DefaultWeightedEdge> network, int source, int sink,
        double expectedCutWeight, double cutWeight, Set<Integer> sourcePartition,
        Set<Integer> sinkPartition, Set<DefaultWeightedEdge> cutEdges)
    {

        assertEquals(expectedCutWeight, cutWeight);
        assertTrue(sourcePartition.contains(source));
        assertTrue(sinkPartition.contains(sink));
        assertTrue(Collections.disjoint(sourcePartition, sinkPartition));
        Set<Integer> unionSet = new HashSet<>(sourcePartition);
        unionSet.addAll(sinkPartition);
        unionSet.removeAll(network.vertexSet());
        assertTrue(unionSet.isEmpty());

        assertEquals(
            network
                .edgeSet().stream()
                .filter(
                    e -> sourcePartition.contains(network.getEdgeSource(e))
                        ^ sourcePartition.contains(network.getEdgeTarget(e)))
                .collect(Collectors.toSet()),
            cutEdges);
        assertEquals(cutWeight, cutEdges.stream().mapToDouble(network::getEdgeWeight).sum());

    }

    public void testProblematicCase()
    {
        Graph<Integer, DefaultWeightedEdge> network =
            new SimpleWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        Graphs.addEdgeWithVertices(network, 1, 2, 0);
        Graphs.addEdgeWithVertices(network, 1, 4, 1);
        Graphs.addEdgeWithVertices(network, 1, 5, 1);
        Graphs.addEdgeWithVertices(network, 4, 5, 1);
        Graphs.addEdgeWithVertices(network, 2, 3, 1);
        Graphs.addEdgeWithVertices(network, 2, 6, 1);
        Graphs.addEdgeWithVertices(network, 3, 6, 1);
        Graphs.addEdgeWithVertices(network, 3, 4, 0);
        runTestUndirected(network, 1, 6, 0);
    }

    /*************** TEST CASES FOR UNDIRECTED GRAPHS ***************/

    public void testUndirectedN1()
    {
        runTestUndirected(getUndirectedN1(), 0, 8, 28);
    }

    public void testUndirectedN2()
    {
        runTestUndirected(getUndirectedN2(), 1, 4, 93);
    }

    public void testUndirectedN3()
    {
        runTestUndirected(getUndirectedN3(), 1, 49, 104);
    }

    public void testUndirectedN4()
    {
        runTestUndirected(getUndirectedN4(), 1, 99, 634);
    }

    public void testUndirectedN5()
    {
        runTestUndirected(getUndirectedN5(), 1, 49, 112);
    }

    public void testUndirectedN6()
    {
        runTestUndirected(getUndirectedN6(), 1, 69, 194);
    }

    public void testUndirectedN7()
    {
        runTestUndirected(getUndirectedN7(), 1, 69, 33);
    }

    public void testUndirectedN8()
    {
        runTestUndirected(getUndirectedN8(), 1, 99, 501);
    }

    public void testUndirectedN9()
    {
        runTestUndirected(getUndirectedN9(), 1, 2, 0);
    }
}
