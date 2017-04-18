/*
 * (C) Copyright 2015-2017, by Joris Kinable and Contributors.
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
package org.jgrapht.perf.graph;

import junit.framework.TestCase;
import org.jgrapht.alg.flow.EdmondsKarpMFImpl;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.generate.GnmRandomGraphGenerator;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;

/**
 * Benchmark class to compare different graph implementations. The benchmark creates a graph, runs
 * various algorithms on the graph and finally destroys (part of) the graph. This is an attempt to
 * simulate common usage of the graph.
 *
 * Note: Currently the tests are performed on a single graph. It would be better to run it on
 * multiple graphs. Not sure how to achieve that through the JMH framework.
 */
public class GraphPerformanceTest
    extends TestCase
{

    public static final int PERF_BENCHMARK_VERTICES_COUNT = 1000;
    public static final int PERF_BENCHMARK_EDGES_COUNT = 100000;
    public static final long SEED = 1446523573696201013l;
    public static final int NR_GRAPHS = 5; // Number of unique graphs on which the tests are
                                           // repeated

    @State(Scope.Benchmark)
    private static abstract class DirectedGraphBenchmarkBase
    {

        private Blackhole blackhole;
        protected GnmRandomGraphGenerator<Integer, DefaultWeightedEdge> rgg;
        private SimpleWeightedGraph<Integer, DefaultWeightedEdge> graph;

        /**
         * Creates a random graph using the Random Graph Generator
         * 
         * @return random graph
         */
        abstract SimpleWeightedGraph<Integer, DefaultWeightedEdge> constructGraph();

        @Setup
        public void setup()
        {
            blackhole = new Blackhole();
        }

        /**
         * Benchmark 1: graph construction
         */
        @Benchmark
        public void generateGraphBenchmark()
        {
            for (int i = 0; i < NR_GRAPHS; i++) {
                rgg = new GnmRandomGraphGenerator<>(
                    PERF_BENCHMARK_VERTICES_COUNT, PERF_BENCHMARK_EDGES_COUNT, SEED + i);
                // Create a graph
                graph = constructGraph();

            }
        }

        /**
         * Benchmark 2: Simulate graph usage: Create a graph, perform various algorithms, partially
         * destroy graph
         */
        @Benchmark
        public void graphPerformanceBenchmark()
        {
            for (int i = 0; i < NR_GRAPHS; i++) {
                rgg = new GnmRandomGraphGenerator<>(
                    PERF_BENCHMARK_VERTICES_COUNT, PERF_BENCHMARK_EDGES_COUNT, SEED + i);
                // Create a graph
                graph = constructGraph();

                Integer[] vertices =
                    graph.vertexSet().toArray(new Integer[graph.vertexSet().size()]);
                Integer source = vertices[0];
                Integer sink = vertices[vertices.length - 1];

                // Run various algorithms on the graph
                double length = this.calculateShorestPath(graph, source, sink);
                blackhole.consume(length);

                double maxFlow = this.calculateMaxFlow(graph, source, sink);
                blackhole.consume(maxFlow);

                // Destroy some random edges in the graph
                destroyRandomEdges(graph);
            }
        }

        private double calculateShorestPath(
            SimpleWeightedGraph<Integer, DefaultWeightedEdge> graph, Integer source,
            Integer sink)
        {
            DijkstraShortestPath<Integer, DefaultWeightedEdge> shortestPathAlg =
                new DijkstraShortestPath<>(graph);
            return shortestPathAlg.getPath(source, sink).getWeight();
        }

        private double calculateMaxFlow(
            SimpleWeightedGraph<Integer, DefaultWeightedEdge> graph, Integer source,
            Integer sink)
        {
            EdmondsKarpMFImpl<Integer, DefaultWeightedEdge> maximumFlowAlg =
                new EdmondsKarpMFImpl<>(graph);
            return maximumFlowAlg.getMaximumFlow(source, sink).getValue();
        }

        private void destroyRandomEdges(
            SimpleWeightedGraph<Integer, DefaultWeightedEdge> graph)
        {
            int nrVertices = graph.vertexSet().size();
            Random rand = new Random(SEED);
            for (int i = 0; i < PERF_BENCHMARK_EDGES_COUNT / 2; i++) {
                int u = rand.nextInt(nrVertices);
                int v = rand.nextInt(nrVertices);
                graph.removeEdge(u, v);
            }
        }

    }


}
