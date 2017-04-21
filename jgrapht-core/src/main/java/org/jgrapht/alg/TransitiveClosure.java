/*
 * (C) Copyright 2007-2017, by Vinayak R Borkar and Contributors.
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
package org.jgrapht.alg;

/**
 * Constructs the transitive closure of the input graph.
 *
 * @author Vinayak R. Borkar
 * @since May 5, 2007
 */
public class TransitiveClosure
{
    /**
     * Singleton instance.
     */
    public static final TransitiveClosure INSTANCE = new TransitiveClosure();

    /**
     * Private Constructor.
     */
    private TransitiveClosure()
    {
    }

    /**
     * Computes floor(log_2(n)) + 1
     */
    private int computeBinaryLog(int n)
    {
        assert n >= 0;

        int result = 0;
        while (n > 0) {
            n >>= 1;
            ++result;
        }

        return result;
    }

}

// End TransitiveClosure.java
