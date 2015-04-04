package io.dimitris.flexmi;

import graphtools.Graph;
import graphtools.Vertex;

import java.util.LinkedHashMap;

import org.jdom2.Document;

public class ErrorCorrector2 {
	private Document doc;
	private LinkedHashMap<Vertex, Double> bestMatchings;
	private Graph model;
	private Graph first;
	private Graph second;
	private Graph third;
	

	public ErrorCorrector2(Document XMLDocument, LinkedHashMap<Vertex, Double> bestMatchings, Graph modelGraph) {
		this.doc = XMLDocument;
		this.bestMatchings = bestMatchings;
		this.model = modelGraph;
	}
	
	public Document start(){
		return null;
	}

}
