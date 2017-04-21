package org.jgrapht.io;

import java.io.*;
import java.nio.charset.*;

import org.jgrapht.*;

/**
 * Created by Stanislav on 26.03.2017.
 */
public interface GraphImportExportFacade<V, E> {

    public void exportGraph(Graph<V, E> g) throws ExportException;

    public void importGraph(Graph<String, E> g, VertexProvider<String> vertexProvider, EdgeProvider<String, E> edgeProvider) throws ImportException;
}
