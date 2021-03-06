package correctiontool;

import graphtools.Edge;
import graphtools.Graph;
import graphtools.Vertex;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EPackage;
import org.jdom2.Document;

public class PairwiseValidation extends Validation{
	private Graph XMLGraph;
	private Graph modelGraph;
	private Document xmldoc;
	private EPackage ecoremodel;
	private Graph validationErrors;

	public PairwiseValidation(Graph XMLGraph,Graph modelGraph,Document doc, EPackage model) {
		super(XMLGraph,modelGraph);
		this.XMLGraph = XMLGraph;
		this.modelGraph = modelGraph;
		this.xmldoc = doc;
		this.ecoremodel = model;		
		
	}

	@Override
	public Object getValidationResult() {
		Object result = null;
		validationErrors = matchGraphs(XMLGraph, modelGraph,xmldoc,ecoremodel);
		//TODO check graph to see if validation produces errors
		boolean pairsmatched = doPairsmatch(validationErrors);
		boolean equalNoOfnodes = noOfNodes(validationErrors, XMLGraph);
		
		if(pairsmatched && equalNoOfnodes){
			result = null;
			return result;
		}
		
		return validationErrors;
	}	
	
	
	private Graph matchGraphs(Graph DocGraph, Graph ModelGraph,Document doc, EPackage model){
		
		Graph pairedGraph = new Graph();
		ScoreCalculator myPairsCreater = new ScoreCalculator(doc, model,DocGraph,ModelGraph);
		ArrayList<Vertex> myPairsList = myPairsCreater.createDistanceTable();
		for(Vertex node: myPairsList ){
			System.out.println("paired_nodes: "+ node.getName()+" Score: " + node.getDistanceScore());
		}
		
		for(Vertex pairedNode1: myPairsList){
			for(Vertex pairedNode2: myPairsList){
				if(pairedNode1 != pairedNode2){
					Vertex ElemNode1 = new Vertex(pairedNode1.getVertexElement());
					Vertex ElemNode2 = new Vertex(pairedNode2.getVertexElement());
					Vertex EClassNode1 = new Vertex(pairedNode1.getVertexEclass());
					Vertex EClassNode2 = new Vertex(pairedNode2.getVertexEclass());
					
					//Check to see if there is a relation between the values in each pair of nodes to each other which will be used to match them in a graph
					boolean relationOfA = DocGraph.edgeExistBetween(ElemNode1, ElemNode2);
					boolean relationOfB = ModelGraph.edgeExistBetween(EClassNode1, EClassNode2);
					if(relationOfA && relationOfB){
						//check to see if there is already an edge between the two paired nodes in the new construction graph
						if(!pairedGraph.edgeExistBetween(pairedNode1, pairedNode2)){
							if(!pairedGraph.containsNode(pairedNode1)){
								if(!pairedGraph.containsNode(pairedNode2)){
									//if constructing graph doesn't contain pair 1 and pair 2
									Edge connectingEdge = new Edge(pairedNode1, pairedNode2);
									pairedNode1.addConnectedEdge(connectingEdge);
									pairedGraph.addNode(pairedNode1);
									pairedGraph.addNode(pairedNode2);
									pairedGraph.addEdge(connectingEdge);
								}else{
									//if constructing graph doesn't contain pair 1 but contains pair 2
									Edge connectingEdge = new Edge(pairedNode1, pairedGraph.getNode(pairedNode2));
									pairedNode1.addConnectedEdge(connectingEdge);
									pairedGraph.addNode(pairedNode1);
									pairedGraph.addEdge(connectingEdge);
								}
							}else{
								if(!pairedGraph.containsNode(pairedNode2)){
									//if constructing graph does contain pair 1 and but not pair 2
									Edge connectingEdge = new Edge(pairedGraph.getNode(pairedNode1), pairedNode2);
									pairedGraph.getNode(pairedNode1).addConnectedEdge(connectingEdge);
									pairedGraph.addNode(pairedNode2);
									pairedGraph.addEdge(connectingEdge);
								}else{
									//if constructing graph does contain pair 1 and pair 2
									Edge connectingEdge = new Edge(pairedGraph.getNode(pairedNode1), pairedGraph.getNode(pairedNode2));
									pairedGraph.getNode(pairedNode1).addConnectedEdge(connectingEdge);
									pairedGraph.addEdge(connectingEdge);
								}
							}
						}
					}else{
						if(!pairedGraph.containsNode(pairedNode1)){
							pairedGraph.addNode(pairedNode1);
						}
					}
				}
			}
		}
		
		for (Edge edge : pairedGraph.getEdges()) {
			System.out.println("Source: " + edge.getSource().getName() + "----------->" + "Target: "+ edge.getTarget().getName()+"--Edge Val: " + edge.getValue());
		}
		return pairedGraph;
		
		
	}
	
	private boolean doPairsmatch(Graph result){
		boolean answer = false;
		
		for (Vertex node : result.getNodes()) {
			String names[] = node.getName().split("_");
			if (names[0].equals(names[1])){
				answer = true;
			}else{
				answer = false;
				break;
			}
		}
		
		return answer;
	}
	
	private boolean noOfNodes(Graph result,Graph xml){
		
		if (result.getNodes().size() == xml.getNodes().size())
			return true;
		else
			return false;
	}
	
	private boolean relationshipPreservation(){
		//TODO write method
		return false;
	}
	

}
