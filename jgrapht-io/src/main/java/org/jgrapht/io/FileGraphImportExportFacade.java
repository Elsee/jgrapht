package org.jgrapht.io;

import org.jgrapht.Graph;

import java.io.File;

import static org.jgrapht.io.ImportExportType.*;

/**
 * Created by Stanislav on 26.03.2017.
 */
public class FileGraphImportExportFacade<V,E>
        implements GraphImportExportFacade {

    private File file;
    private ImportExportType currentType;
    private GraphExporter<V, E> ge;
    private GraphImporter<String, E> gi;
    //private Character defaultDelimiter = ',';

    public FileGraphImportExportFacade(File file, ImportExportType type) {
        this.file = file;
        this.currentType = type;
        _setGraphExporter();
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setCurrentType(ImportExportType type) {
        if(this.currentType != type) {
            this.currentType = type;
            _setGraphExporter();
        }
    }


    @Override
    public void exportGraph(Graph g) throws ExportException {
        ge.exportGraph(g, file);
    }

    @Override
    public void importGraph(Graph g, VertexProvider vertexProvider, EdgeProvider edgeProvider) throws ImportException {
        _setGraphImporter(vertexProvider, edgeProvider);
        gi.importGraph(g, file);
    }


    private void _setGraphImporter(VertexProvider<String> vertexProvider, EdgeProvider<String, E> edgeProvider) {
        Graph<V,E> g;
        switch(currentType){
            case CSV:
                gi = new CSVImporter<String, E>(vertexProvider, edgeProvider);
                break;
            case GML:
                gi = new GmlImporter<String, E>(vertexProvider, edgeProvider);
                break;
            case GRAPHML:
                gi = new GraphMLImporter<String, E>(vertexProvider, edgeProvider);
                break;
            case DOT:
                gi = new DOTImporter<String, E>(vertexProvider, edgeProvider);
                break;
            case DIMACS:
                gi = new DIMACSImporter<String, E>(vertexProvider, edgeProvider);
                break;
        }
    }

    private void _setGraphExporter() {
        Graph<V,E> g;
        switch(currentType){
            case CSV:
                ge = new CSVExporter<V, E>();
                break;
            case GML:
                ge = new GmlExporter<V, E>();
                break;
            case GRAPHML:
                ge = new GraphMLExporter<V, E>();
                break;
            case DOT:
                ge = new DOTExporter<V, E>();
                break;
            case DIMACS:
                ge = new DIMACSExporter<V, E>();
                break;
        }

    }

}
