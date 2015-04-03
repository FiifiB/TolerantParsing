package io.dimitris.flexmi;

import graphtools.Graph;

public abstract class Validation {

	public Validation(Graph XMLGraph,Graph modelGraph) {
		
	}
	
	public abstract boolean getValidationResult();
	
	public abstract Object getValidationErrors();

}
