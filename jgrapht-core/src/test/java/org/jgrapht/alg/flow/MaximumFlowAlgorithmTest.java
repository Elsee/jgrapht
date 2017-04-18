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
import org.jgrapht.alg.interfaces.MaximumFlowAlgorithm;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Map;

/**
 * @author Joris Kinable
 */
public abstract class MaximumFlowAlgorithmTest
    extends MaximumFlowMinimumCutAlgorithmTestBase
{

    abstract MaximumFlowAlgorithm<Integer, DefaultWeightedEdge> createSolver(
        Graph<Integer, DefaultWeightedEdge> network);

    private void runTestUndirected(
        Graph<Integer, DefaultWeightedEdge> graph, int source, int sink,
        int expectedResult)
    {
        MaximumFlowAlgorithm<Integer, DefaultWeightedEdge> solver = createSolver(graph);

        verifyUndirected(graph, source, sink, expectedResult, solver);
    }

    static void verifyUndirected(
        Graph<Integer, DefaultWeightedEdge> graph, int source, int sink,
        int expectedResult, MaximumFlowAlgorithm<Integer, DefaultWeightedEdge> solver)
    {
        MaximumFlowAlgorithm.MaximumFlow<DefaultWeightedEdge> maxFlow =
            solver.getMaximumFlow(source, sink);
        Double flowValue = maxFlow.getValue();
        Map<DefaultWeightedEdge, Double> flow = maxFlow.getFlow();

        assertEquals(expectedResult, flowValue.intValue());

        // Verify that every edge is contained in the flow map
        for (DefaultWeightedEdge e : graph.edgeSet())
            assertTrue(flow.containsKey(e));

        // Verify that the flow on every arc is between [-DEFAULT_EPSILON, edge_capacity]
        for (DefaultWeightedEdge e : flow.keySet()) {
            assertTrue(graph.containsEdge(e));
            assertTrue(flow.get(e) >= -EdmondsKarpMFImpl.DEFAULT_EPSILON);
            assertTrue(flow.get(e) <= (graph.getEdgeWeight(e) + EdmondsKarpMFImpl.DEFAULT_EPSILON));
        }

        // Verify flow preservation: amount of incoming flow must equal amount of outgoing flow
        // (exception for the source/sink vertices)
        for (Integer u : graph.vertexSet()) {
            double balance = 0.0;
            for (DefaultWeightedEdge e : graph.edgesOf(u)) {
                Integer v = solver.getFlowDirection(e);
                if (u == v) // incoming flow
                    balance += flow.get(e);
                else // outgoing flow
                    balance -= flow.get(e);
            }

            if (u.equals(source)) {
                assertEquals(-flowValue, balance, MaximumFlowAlgorithmBase.DEFAULT_EPSILON);
            } else if (u.equals(sink)) {
                assertEquals(flowValue, balance, MaximumFlowAlgorithmBase.DEFAULT_EPSILON);
            } else {
                assertEquals(0.0, balance, MaximumFlowAlgorithmBase.DEFAULT_EPSILON);
            }
        }

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
