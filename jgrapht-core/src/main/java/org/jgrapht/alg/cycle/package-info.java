/**
 * Algorithms for enumeration of simple cycles in graphs.
 *
 * <p>
 * <b>Implementation Note:</b> All the implementations work correctly with loops but not with
 * multiple duplicate edges.
 * 
 * <p>
 * The worst case performance is achieved for graphs with special structure, so on practical
 * workloads an algorithm with higher worst case complexity may outperform an algorithm with lower
 * worst case complexity. Note also that "administrative costs" of algorithms with better worst case
 * performance are higher. Also higher is their memory cost (which is in all cases O(V+E)).
 *
 * The worst case time complexity of the Paton's algorithm for finding a cycle base in undirected
 * graphs is O(V^3).
 * 
 * <p>
 * <b>Literature:</b> <br>
 * <ol>
 * <li>P.Mateti and N.Deo, On algorithms for enumerating all circuits of a graph., SIAM J. Comput.,
 * 5 (1978), pp. 90-99.</li>
 * <li>L.G.Bezem and J.van Leeuwen, Enumeration in graphs., Technical report RUU-CS-87-7, University
 * of Utrecht, The Netherlands, 1987.</li>
 * <li>K. Paton, An algorithm for finding a fundamental set of cycles for an undirected linear
 * graph, Comm. ACM 12 (1969), pp. 514-518.</li>
 * </ol>
 * 
 */
package org.jgrapht.alg.cycle;
