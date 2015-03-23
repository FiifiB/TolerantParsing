package io.dimitris.flexmi;

import java.util.LinkedHashMap;

import graphtools.Graph;
import graphtools.Vertex;

public abstract class Validation {
	private Graph XMLGraph;
	private Graph modelGraph;

	public Validation(Graph XMLGraph,Graph modelGraph) {
		this.XMLGraph = XMLGraph;
		this.modelGraph = modelGraph;
	}
	
	public abstract LinkedHashMap<Vertex, Double> validate();

}
