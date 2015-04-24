package correctiontool;

import graphtools.Graph;

public abstract class Validation {

	public Validation(Graph XMLGraph,Graph modelGraph) {
		
	}
	
	public abstract Object getValidationResult();

}
