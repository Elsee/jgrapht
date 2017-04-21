package org.jgrapht.io;

import junit.framework.TestCase;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Pseudograph;
import org.jgrapht.graph.SimpleGraph;

import java.io.File;

/**
 * Created by Stanislav on 27.03.2017.
 */
public class FileGraphImportExportFacadeTest  extends TestCase {

    private static final String NL = System.getProperty("line.separator");


    public void testCSV(){
        Graph<Integer, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
        Graph<String, DefaultEdge> g2 = new SimpleGraph<>(DefaultEdge.class);
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(4);
        g.addVertex(5);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(3, 4);
        g.addEdge(4, 5);

        File file = new File("./src/test/export/CSVDirectedAdjList.txt");
        FileGraphImportExportFacade<String, DefaultEdge> facade = new FileGraphImportExportFacade(file, ImportExportType.CSV);
        try {
            facade.exportGraph(g);
        } catch (ExportException e) {
            e.printStackTrace();
        }

        try {
            facade.importGraph(g2, (l, a) -> l, (f, t, l, a) -> g2.getEdgeFactory().createEdge(f.toString(), t.toString()));
        } catch (ImportException e) {
            e.printStackTrace();
        }

        assertEquals(g.toString(), g2.toString());
    }
public void testGml(){
        Graph<Integer, DefaultEdge> g = new Pseudograph<>(DefaultEdge.class);
        Graph<String, DefaultEdge> g2 = new Pseudograph<>(DefaultEdge.class);
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(4);
        g.addVertex(5);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(3, 1);
        g.addEdge(3, 4);
        g.addEdge(4, 5);
        g.addEdge(5, 1);
        g.addEdge(5, 2);
        g.addEdge(5, 3);
        g.addEdge(5, 4);
        g.addEdge(5, 5);
        g.addEdge(5, 5);

        File file = new File("./src/test/export/GmlDirectedAdjList.txt");
        FileGraphImportExportFacade<String, DefaultEdge> facade = new FileGraphImportExportFacade(file, ImportExportType.GML);
        try {
            facade.exportGraph(g);
        } catch (ExportException e) {
            e.printStackTrace();
        }

        try {
            facade.importGraph(g2, (l, a) -> l, (f, t, l, a) -> g2.getEdgeFactory().createEdge(f.toString(), t.toString()));
        } catch (ImportException e) {
            e.printStackTrace();
        }

        assertEquals(g.toString(), g2.toString());
    }

public void testDIMACS(){
        Graph<Integer, DefaultEdge> g = new Pseudograph<>(DefaultEdge.class);
        Graph<String, DefaultEdge> g2 = new Pseudograph<>(DefaultEdge.class);
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(4);
        g.addVertex(5);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(3, 1);
        g.addEdge(3, 4);
        g.addEdge(4, 5);
        g.addEdge(5, 1);
        g.addEdge(5, 2);
        g.addEdge(5, 3);
        g.addEdge(5, 4);
        g.addEdge(5, 5);
        g.addEdge(5, 5);

        File file = new File("./src/test/export/DIMACSDirectedAdjList.txt");
        FileGraphImportExportFacade<String, DefaultEdge> facade = new FileGraphImportExportFacade(file, ImportExportType.DIMACS);
        try {
            facade.exportGraph(g);
        } catch (ExportException e) {
            e.printStackTrace();
        }

        try {
            facade.importGraph(g2, (l, a) -> l, (f, t, l, a) -> g2.getEdgeFactory().createEdge(f.toString(), t.toString()));
        } catch (ImportException e) {
            e.printStackTrace();
        }

        assertEquals(g.toString(), g2.toString());
    }

public void testGraphML(){
        Graph<Integer, DefaultEdge> g = new Pseudograph<>(DefaultEdge.class);
        Graph<String, DefaultEdge> g2 = new Pseudograph<>(DefaultEdge.class);
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(4);
        g.addVertex(5);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(3, 1);
        g.addEdge(3, 4);
        g.addEdge(4, 5);
        g.addEdge(5, 1);
        g.addEdge(5, 2);
        g.addEdge(5, 3);
        g.addEdge(5, 4);
        g.addEdge(5, 5);
        g.addEdge(5, 5);

        File file = new File("./src/test/export/GraphMLDirectedAdjList.txt");
        FileGraphImportExportFacade<String, DefaultEdge> facade = new FileGraphImportExportFacade(file, ImportExportType.GRAPHML);
        try {
            facade.exportGraph(g);
        } catch (ExportException e) {
            e.printStackTrace();
        }

        try {
            facade.importGraph(g2, (l, a) -> l, (f, t, l, a) -> g2.getEdgeFactory().createEdge(f.toString(), t.toString()));
        } catch (ImportException e) {
            e.printStackTrace();
        }

        assertEquals(g.toString(), g2.toString());
    }


public void testDOT(){
        Graph<Integer, DefaultEdge> g = new Pseudograph<>(DefaultEdge.class);
        Graph<String, DefaultEdge> g2 = new Pseudograph<>(DefaultEdge.class);
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(4);
        g.addVertex(5);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(3, 1);
        g.addEdge(3, 4);
        g.addEdge(4, 5);
        g.addEdge(5, 1);
        g.addEdge(5, 2);
        g.addEdge(5, 3);
        g.addEdge(5, 4);
        g.addEdge(5, 5);
        g.addEdge(5, 5);

        File file = new File("./src/test/export/DOTDirectedAdjList.txt");
        FileGraphImportExportFacade<String, DefaultEdge> facade = new FileGraphImportExportFacade(file, ImportExportType.DOT);
        try {
            facade.exportGraph(g);
        } catch (ExportException e) {
            e.printStackTrace();
        }

        try {
            facade.importGraph(g2, (l, a) -> l, (f, t, l, a) -> g2.getEdgeFactory().createEdge(f.toString(), t.toString()));
        } catch (ImportException e) {
            e.printStackTrace();
        }

        assertEquals(g.toString(), g2.toString());
    }

}
