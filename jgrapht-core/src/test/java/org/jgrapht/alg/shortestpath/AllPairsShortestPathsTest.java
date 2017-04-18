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
package org.jgrapht.alg.shortestpath;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.generate.GnpRandomGraphGenerator;
import org.jgrapht.generate.GraphGenerator;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.IntegerVertexFactory;
import org.jgrapht.graph.WeightedPseudograph;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

/**
 * @author Dimitrios Michail
 */
public class AllPairsShortestPathsTest
{
    @Test
    public void testRandomFixedSeed()
    {
        final long seed = 47;
        Random rng = new Random(seed);
        testAllPairsShortestPaths(rng);
    }

    @Test
    public void testRandomFixedSeed8()
    {
        final long seed = 8;
        Random rng = new Random(seed);
        testAllPairsShortestPaths(rng);
    }

    @Test
    public void testRandomFixedSeed13()
    {
        final long seed = 13;
        Random rng = new Random(seed);
        testAllPairsShortestPaths(rng);
    }

    @Test
    public void testRandomFixedSeed17()
    {
        final long seed = 17;
        Random rng = new Random(seed);
        testAllPairsShortestPaths(rng);
    }

    private void testAllPairsShortestPaths(Random rng)
    {
        final int tests = 5;
        final int n = 20;
        final double p = 0.35;
        final int landmarksCount = 2;

        List<Function<Graph<Integer, DefaultWeightedEdge>,
            ShortestPathAlgorithm<Integer, DefaultWeightedEdge>>> algs = new ArrayList<>();
        algs.add((g) -> new DijkstraShortestPath<>(g));
        algs.add((g) -> new BidirectionalDijkstraShortestPath<>(g));
        algs.add((g) -> new AStarShortestPath<>(g, (u, t) -> 0d));
        algs.add((g) -> {
            Integer[] vertices = g.vertexSet().toArray(new Integer[0]);
            Set<Integer> landmarks = new HashSet<>();
            while (landmarks.size() < landmarksCount) {
                landmarks.add(vertices[rng.nextInt(g.vertexSet().size())]);
            }
            return new AStarShortestPath<>(g, new ALTAdmissibleHeuristic<>(g, landmarks));
        });

        GraphGenerator<Integer, DefaultWeightedEdge, Integer> gen =
            new GnpRandomGraphGenerator<>(n, p, rng, true);

        for (int i = 0; i < tests; i++) {
            Graph<Integer, DefaultWeightedEdge> g =
                new WeightedPseudograph<>(DefaultWeightedEdge.class);
            gen.generateGraph(g, new IntegerVertexFactory(), null);

            // assign random weights
            for (DefaultWeightedEdge e : g.edgeSet()) {
                g.setEdgeWeight(e, rng.nextDouble());
            }

            double[][] dist = new double[n][n];

            int j = 0;
            for (Function<Graph<Integer, DefaultWeightedEdge>,
                ShortestPathAlgorithm<Integer, DefaultWeightedEdge>> spProvider : algs)
            {
                ShortestPathAlgorithm<Integer, DefaultWeightedEdge> alg = spProvider.apply(g);
                for (Integer v : g.vertexSet()) {
                    for (Integer u : g.vertexSet()) {
                        GraphPath<Integer, DefaultWeightedEdge> path = alg.getPath(v, u);

                        double d;
                        if (path == null) {
                            d = Double.POSITIVE_INFINITY;
                        } else {
                            d = path.getWeight();
                        }

                        if (j == 0) {
                            dist[v][u] = d;
                        } else {
                            assertEquals(dist[v][u], d, 1e-9);
                        }
                    }
                }
                j++;
            }

        }

    }

}
