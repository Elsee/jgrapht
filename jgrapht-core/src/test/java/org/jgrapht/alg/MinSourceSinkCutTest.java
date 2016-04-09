/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2012, by Barak Naveh and Contributors.
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
/* -------------------------
 * MinSourceSinkCutTest.java
 * -------------------------
 * (C) Copyright 2012-2012, by Joris Kinable and Contributors.
 *
 * Original Author:  Joris Kinable
 * Contributor(s):
 *
 * Changes
 * -------
 * 26-Nov-2012 : Initial revision (JK);
 *
 */
package org.jgrapht.alg;

import junit.framework.TestCase;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;

/**
 * Test class for the MinSourceSinkCut class.
 * @author Joris Kinable
 *
 */
public class MinSourceSinkCutTest extends TestCase{

	/**
	 * Constructs a small realistic graph and computes the min s-t cut
	 */
	public void testRealGraph() {
		DirectedGraph<Integer, DefaultWeightedEdge> graph= new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Integer[] vertices={0,1,2,3,4,5,6};
		for(Integer i: vertices)
			graph.addVertex(i);
		
		DefaultWeightedEdge e03=Graphs.addEdge(graph, 0, 3, 3);
		DefaultWeightedEdge e14=Graphs.addEdge(graph, 1, 4, 1);
		DefaultWeightedEdge e24=Graphs.addEdge(graph, 2, 4, 2);
		DefaultWeightedEdge e26=Graphs.addEdge(graph, 2, 6, 1);

		
		MinSourceSinkCut<Integer, DefaultWeightedEdge> mc= new MinSourceSinkCut<>(graph);
		mc.computeMinCut(0, 6);
		
		//Test the current source
		assertEquals(0, mc.getCurrentSource(), 0);
		//Test the current sink
		assertEquals(6, mc.getCurrentSink(), 0);
		
		//Test the source and sink partitions
		List<Integer> l1 = Arrays.asList(0, 1, 2);
		List<Integer> l2 = Arrays.asList(3, 4, 5, 6);
	    Set<Integer> partition1 = new HashSet<>(l1);
	    Set<Integer> partition2 = new HashSet<>(l2);
		assertEquals(partition1, mc.getSourcePartition());
	    assertEquals(partition2, mc.getSinkPartition());
	
	    //Test the cut weight
	    assertEquals(7, mc.getCutWeight(),0);
	
	    //Test the cut edge set
	    List<DefaultWeightedEdge> l3=Arrays.asList(e03, e26, e24, e14);
	    Set<DefaultWeightedEdge> cutEdges= new HashSet<>(l3);
	    assertEquals(cutEdges, mc.getCutEdges());
	
	    cutEdges=mc.getCutEdges();
	    double weight=0;
	    for(DefaultWeightedEdge e: cutEdges)
	    	weight+=graph.getEdgeWeight(e);
	    assertEquals(7.0, weight, 0.0);
	}
	
	/**
	 * Test on a graph with fractional edge weights
	 */
	public void testRealGraph2() {
		DirectedGraph<Integer, DefaultWeightedEdge> graph= new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Integer[] vertices={0,1,2,3,4,5,6};
		for(Integer i: vertices)
			graph.addVertex(i);
		
		DefaultWeightedEdge e02=Graphs.addEdge(graph, 0, 2, 1.0/6);
		DefaultWeightedEdge e03=Graphs.addEdge(graph, 0, 3, 1.0/3);
		DefaultWeightedEdge e12=Graphs.addEdge(graph, 1, 2, 1.0/4);
		DefaultWeightedEdge e46=Graphs.addEdge(graph, 4, 6, 1.0/8);

		
		MinSourceSinkCut<Integer, DefaultWeightedEdge> mc= new MinSourceSinkCut<>(graph);
		mc.computeMinCut(0, 6);
		
		//Test the current source
		assertEquals(0, mc.getCurrentSource(), 0);
		//Test the current sink
		assertEquals(6, mc.getCurrentSink(), 0);
		
		//Test the source and sink partitions
		List<Integer> l1 = Arrays.asList(0, 1, 4);
		List<Integer> l2 = Arrays.asList(2, 3, 5, 6);
	    Set<Integer> partition1 = new HashSet<>(l1);
	    Set<Integer> partition2 = new HashSet<>(l2);
		assertEquals(partition1, mc.getSourcePartition());
	    assertEquals(partition2, mc.getSinkPartition());
	
	    //Test the cut weight
	    assertEquals(0.875, mc.getCutWeight(),0);
	
	    //Test the cut edge set
	    List<DefaultWeightedEdge> l3=Arrays.asList(e03, e02, e12, e46);
	    Set<DefaultWeightedEdge> cutEdges= new HashSet<>(l3);
	    assertEquals(cutEdges, mc.getCutEdges());
	
	    cutEdges=mc.getCutEdges();
	    double weight=0;
	    for(DefaultWeightedEdge e: cutEdges)
	    	weight+=graph.getEdgeWeight(e);
	    assertEquals(0.875, weight,0.000000001);
	}
	
	/**
	 * Computes the min s-t cut in a graph with 2 vertices and one directed edge between source and sink
	 */
	public void testGraphWithOneEdge(){
		DirectedGraph<Integer, DefaultWeightedEdge> graph= new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Integer[] vertices={0,1};
		for(Integer i: vertices)
			graph.addVertex(i);
		
		Graphs.addEdge(graph, 0, 1, 2);
		MinSourceSinkCut<Integer, DefaultWeightedEdge> mc= new MinSourceSinkCut<>(graph);
		mc.computeMinCut(0, 1);
		
		assertEquals(0, mc.getCurrentSource(), 0);
		assertEquals(1, mc.getCurrentSink(), 0);
		
		//Test the source and sink partitions
		List<Integer> l1 = Collections.singletonList(0);
		List<Integer> l2 = Collections.singletonList(1);
	    Set<Integer> partition1 = new HashSet<>(l1);
	    Set<Integer> partition2 = new HashSet<>(l2);
		assertEquals(partition1, mc.getSourcePartition());
	    assertEquals(partition2, mc.getSinkPartition());
	
	    //Test the cut weight
	    assertEquals(2, mc.getCutWeight(),0);
	
	    //Reverse the source and sink
	    mc.computeMinCut(1, 0);
	    assertEquals(1, mc.getCurrentSource(), 0);
		assertEquals(0, mc.getCurrentSink(), 0);
		assertEquals(0, mc.getCutWeight(),0);
	}

	/**
	 * Computes the min s-t cut in a disconnected graph
	 */
	public void testDisconnectedGraph(){
		DirectedGraph<Integer, DefaultWeightedEdge> graph= new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Integer[] vertices={0,1,2};
		for(Integer i: vertices)
			graph.addVertex(i);
		
		Graphs.addEdge(graph, 1, 2, 2);
		
		MinSourceSinkCut<Integer, DefaultWeightedEdge> mc= new MinSourceSinkCut<>(graph);
		mc.computeMinCut(0, 2);
		
		assertEquals(0, mc.getCurrentSource(), 0);
		//Test the current sink
		assertEquals(2, mc.getCurrentSink(), 0);
		
		//Test the source and sink partitions
		List<Integer> l1 = Collections.singletonList(0);
		List<Integer> l2 = Arrays.asList(1,2);
	    Set<Integer> partition1 = new HashSet<>(l1);
	    Set<Integer> partition2 = new HashSet<>(l2);
		assertEquals(partition1, mc.getSourcePartition());
	    assertEquals(partition2, mc.getSinkPartition());
	
	    //Test the cut weight
	    assertEquals(0, mc.getCutWeight(),0);
	    assertEquals(Collections.EMPTY_SET, mc.getCutEdges());
	}
	
	
}
