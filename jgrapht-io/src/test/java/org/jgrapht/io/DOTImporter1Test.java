/*
 * (C) Copyright 2015-2017, by Wil Selwood and Contributors.
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
package org.jgrapht.io;

import org.jgrapht.Graph;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Multigraph;
import org.junit.Test;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * 1st part of tests for DOTImporter. See also {@link DOTImporter2Test}.
 */
public class DOTImporter1Test
{

    @Test
    public void testUndirectedWithLabels()
        throws ImportException
    {
        String input = "graph G {\n" + "  1 [ \"label\"=\"abc123\" ];\n"
            + "  2 [ label=\"fred\" ];\n" + "  1 -- 2;\n" + "}";

        Multigraph<String, DefaultEdge> expected = new Multigraph<>(DefaultEdge.class);
        expected.addVertex("1");
        expected.addVertex("2");
        expected.addEdge("1", "2");

        GraphImporter<String, DefaultEdge> importer = buildImporter();

        Multigraph<String, DefaultEdge> result = new Multigraph<>(DefaultEdge.class);
        importer.importGraph(result, new StringReader(input));

        assertEquals(expected.toString(), result.toString());

        assertEquals(2, result.vertexSet().size());
        assertEquals(1, result.edgeSet().size());

    }

    @Test
    public void testMultiLinksUndirected()
        throws ImportException
    {
        String input = "graph G {\n" + "  1 [ label=\"bob\" ];\n" + "  2 [ label=\"fred\" ];\n"
        // the extra label will be ignored but not cause any problems.
            + "  1 -- 2 [ label=\"friend\"];\n" + "  1 -- 2;\n" + "}";

        Multigraph<String, DefaultEdge> expected = new Multigraph<>(DefaultEdge.class);
        expected.addVertex("1");
        expected.addVertex("2");
        expected.addEdge("1", "2", new DefaultEdge());
        expected.addEdge("1", "2", new DefaultEdge());

        GraphImporter<String, DefaultEdge> importer = buildImporter();

        Multigraph<String, DefaultEdge> result = new Multigraph<>(DefaultEdge.class);
        importer.importGraph(result, new StringReader(input));

        assertEquals(expected.toString(), result.toString());

        assertEquals(2, result.vertexSet().size());
        assertEquals(1, result.edgeSet().size());
    }

    @Test
    public void testDashLabelVertex()
        throws ImportException
    {
        String input =
            "graph G {\n" + "a [label=\"------this------contains-------dashes------\"]\n" + "}";

        Multigraph<String, DefaultEdge> result = new Multigraph<>(DefaultEdge.class);

        Map<String, Map<String, String>> attrs = new HashMap<>();

        VertexProvider<String> vp = (label, a) -> {
            attrs.put(label, a);
            return label;
        };
        EdgeProvider<String, DefaultEdge> ep = (f, t, l, a) -> new DefaultEdge();
        ComponentUpdater<String> cu = (v, a) -> {
        };
        DOTImporter<String, DefaultEdge> importer =
            new DOTImporter<String, DefaultEdge>(vp, ep, cu);

        importer.importGraph(result, new StringReader(input));

        assertEquals(1, result.vertexSet().size());
        String v = result.vertexSet().stream().findFirst().get();
        assertEquals("a", v);
        assertEquals("------this------contains-------dashes------", attrs.get("a").get("label"));
    }

    @Test
    public void testAttributesWithNoQuotes()
        throws ImportException
    {
        String input =
            "graph G {\n" + "  1 [ label = \"bob\" \"foo\"=bar ];\n" + "  2 [ label = \"fred\" ];\n"
            // the extra label will be ignored but not cause any problems.
                + "  1 -- 2 [ label = \"friend\" \"foo\" = wibble];\n" + "}";

        Multigraph<TestVertex, TestEdge> result =
            new Multigraph<TestVertex, TestEdge>(TestEdge.class);
        DOTImporter<TestVertex, TestEdge> importer = new DOTImporter<TestVertex, TestEdge>(
            (l, a) -> new TestVertex(l, a), (f, t, l, a) -> new TestEdge(l, a));

        importer.importGraph(result, new StringReader(input));
        assertEquals("wrong size of vertexSet", 2, result.vertexSet().size());
        assertEquals("wrong size of edgeSet", 1, result.edgeSet().size());

        for (TestVertex v : result.vertexSet()) {
            if ("1".equals(v.getId())) {
                assertEquals("wrong number of attributes", 2, v.getAttributes().size());
                assertEquals("Wrong attribute values", "bar", v.getAttributes().get("foo"));
                assertEquals("Wrong attribute values", "bob", v.getAttributes().get("label"));
            } else {
                assertEquals("wrong number of attributes", 1, v.getAttributes().size());
                assertEquals("Wrong attribute values", "fred", v.getAttributes().get("label"));
            }
        }

        for (TestEdge e : result.edgeSet()) {
            assertEquals("wrong id", "friend", e.getId());
            assertEquals("wrong number of attributes", 2, e.getAttributes().size());
            assertEquals("Wrong attribute value", "wibble", e.getAttributes().get("foo"));
            assertEquals("Wrong attribute value", "friend", e.getAttributes().get("label"));
        }

    }

    @Test
    public void testAttributesWithNoValues()
        throws ImportException
    {
        String input =
            "graph G {\n" + "  1 [ label = \"bob\" \"foo\" ];\n" + "  2 [ label = \"fred\" ];\n"
            // the extra label will be ignored but not cause any problems.
                + "  1 -- 2 [ label = friend foo];\n" + "}";

        Multigraph<TestVertex, TestEdge> graph = new Multigraph<>(TestEdge.class);

        VertexProvider<TestVertex> vp = (label, attrs) -> new TestVertex(label, attrs);
        EdgeProvider<TestVertex, TestEdge> ep = (f, t, l, attrs) -> new TestEdge(l, attrs);
        DOTImporter<TestVertex, TestEdge> importer = new DOTImporter<TestVertex, TestEdge>(vp, ep);

        try {
            importer.importGraph(graph, new StringReader(input));
            fail("Failed to import DOT graph: line 2:26 mismatched input ']' expecting '='");
        } catch (ImportException e) {
        }
    }

    @Test
    public void testUpdatingVertex()
        throws ImportException
    {
        String input = "graph G {\n" + "a -- b;\n" + "a [foo=\"bar\"];\n" + "}";
        Multigraph<TestVertex, DefaultEdge> result = new Multigraph<>(DefaultEdge.class);

        VertexProvider<TestVertex> vp = (label, attrs) -> new TestVertex(label, attrs);
        EdgeProvider<TestVertex, DefaultEdge> ep = (f, t, l, attrs) -> new DefaultEdge();
        ComponentUpdater<TestVertex> cu = (v, attrs) -> v.getAttributes().putAll(attrs);
        DOTImporter<TestVertex, DefaultEdge> importer = new DOTImporter<>(vp, ep, cu);

        importer.importGraph(result, new StringReader(input));

        assertEquals("wrong size of vertexSet", 2, result.vertexSet().size());
        assertEquals("wrong size of edgeSet", 1, result.edgeSet().size());
        for (TestVertex v : result.vertexSet()) {
            if ("a".equals(v.getId())) {
                assertEquals("wrong number of attributes", 1, v.getAttributes().size());
            } else {
                assertEquals("attributes are populated", 0, v.getAttributes().size());
            }
        }

    }

    @Test
    public void testParametersWithSemicolons()
        throws ImportException
    {
        String input = "graph G {\n  1 [ label=\"this label; contains a semi colon\" ];\n}\n";
        Multigraph<TestVertex, DefaultEdge> result =
            new Multigraph<TestVertex, DefaultEdge>(DefaultEdge.class);
        DOTImporter<TestVertex, DefaultEdge> importer = new DOTImporter<TestVertex, DefaultEdge>(
            (l, a) -> new TestVertex(l, a), (f, t, l, a) -> new DefaultEdge());

        importer.importGraph(result, new StringReader(input));
        assertEquals("wrong size of vertexSet", 1, result.vertexSet().size());
        assertEquals("wrong size of edgeSet", 0, result.edgeSet().size());
    }

    @Test
    public void testLabelsWithEscapedSemicolons()
        throws ImportException
    {
        String escapedLabel = "this \\\"label; \\\"contains an escaped semi colon";
        String input = "graph G {\n node [ label=\"" + escapedLabel + "\" ];\n node0 }\n";
        Multigraph<TestVertex, DefaultEdge> result =
            new Multigraph<TestVertex, DefaultEdge>(DefaultEdge.class);
        DOTImporter<TestVertex, DefaultEdge> importer = new DOTImporter<TestVertex, DefaultEdge>(
            (label, attrs) -> new TestVertex(label, attrs), (f, t, l, a) -> new DefaultEdge());

        importer.importGraph(result, new StringReader(input));
        assertEquals("wrong size of vertexSet", 1, result.vertexSet().size());
        assertEquals("wrong size of edgeSet", 0, result.edgeSet().size());
        assertEquals(
            "wrong parsing", "node0", ((TestVertex) result.vertexSet().toArray()[0]).getId());
        assertEquals(
            "wrong parsing", "this \"label; \"contains an escaped semi colon",
            ((TestVertex) result.vertexSet().toArray()[0]).getAttributes().get("label"));
    }

    @Test
    public void testNoLineEndBetweenNodes()
        throws ImportException
    {
        String input =
            "graph G {\n  1 [ label=\"this label; contains a semi colon\" ];  2 [ label=\"wibble\" ] \n}\n";
        Multigraph<TestVertex, DefaultEdge> result =
            new Multigraph<TestVertex, DefaultEdge>(DefaultEdge.class);
        DOTImporter<TestVertex, DefaultEdge> importer = new DOTImporter<TestVertex, DefaultEdge>(
            (l, a) -> new TestVertex(l, a), (f, t, l, a) -> new DefaultEdge());

        importer.importGraph(result, new StringReader(input));
        assertEquals("wrong size of vertexSet", 2, result.vertexSet().size());
        assertEquals("wrong size of edgeSet", 0, result.edgeSet().size());
    }

    @Test
    public void testWithReader()
        throws ImportException
    {
        String input = "graph G {\n" + "  1 [ \"label\"=\"abc123\" ];\n"
            + "  2 [ label=\"fred\" ];\n" + "  1 -- 2;\n" + "}";

        Multigraph<String, DefaultEdge> expected = new Multigraph<>(DefaultEdge.class);
        expected.addVertex("1");
        expected.addVertex("2");
        expected.addEdge("1", "2");

        GraphImporter<String, DefaultEdge> importer = buildImporter();

        Graph<String, DefaultEdge> result = new Multigraph<>(DefaultEdge.class);
        importer.importGraph(result, new StringReader(input));

        assertEquals(expected.toString(), result.toString());

        assertEquals(2, result.vertexSet().size());
        assertEquals(1, result.edgeSet().size());

    }

    private void testGarbageGraph(
        String input, String expected, AbstractBaseGraph<String, DefaultEdge> graph)
    {
        GraphImporter<String, DefaultEdge> importer = buildImporter();
        try {
            importer.importGraph(graph, new StringReader(input));
            fail("Should not get here");
        } catch (ImportException e) {
            assertEquals(expected, e.getMessage());
        }
    }

    private GraphImporter<String, DefaultEdge> buildImporter()
    {
        return new DOTImporter<String, DefaultEdge>(new VertexProvider<String>()
        {
            @Override
            public String buildVertex(String label, Map<String, String> attributes)
            {
                return label;
            }
        }, new EdgeProvider<String, DefaultEdge>()
        {
            @Override
            public DefaultEdge buildEdge(
                String from, String to, String label, Map<String, String> attributes)
            {
                return new DefaultEdge();
            }
        });
    }

    private class TestVertex
    {
        String id;
        Map<String, String> attributes;

        public TestVertex(String id, Map<String, String> attributes)
        {
            this.id = id;
            this.attributes = attributes;
        }

        public String getId()
        {
            return id;
        }

        public Map<String, String> getAttributes()
        {
            return attributes;
        }

        @Override
        public String toString()
        {
            return id + ", " + attributes;
        }

    }

    private class TestEdge
        extends DefaultEdge
    {
        private static final long serialVersionUID = 1L;

        String id;
        Map<String, String> attributes;

        public TestEdge(String id, Map<String, String> attributes)
        {
            super();
            this.id = id;
            this.attributes = attributes;
        }

        public String getId()
        {
            return id;
        }

        public Map<String, String> getAttributes()
        {
            return attributes;
        }

        @Override
        public String toString()
        {
            return id + ", " + attributes;
        }
    }
}
