package io.dimitris.flexmi;

import graphtools.Edge;
import graphtools.Graph;
import graphtools.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.Document;
import org.jdom2.Element;

public class FloodingCorrection extends Correction {
	private Document doc;

	public FloodingCorrection(Document XmlDocument) {
		this.doc = XmlDocument;
	}
	
	@Override
	public Object getCorrectionResult(Object parameter) {
		LinkedHashMap<Vertex, Double> fixpointResult = null;
		Graph propagatedGraph = propagateGraph((Graph)parameter);
		return null;
	}
	
	public Graph propagateGraph(Graph pairedNodesGraph){
		Graph propagateGraph =  new Graph();
		
		for(Vertex parentNode: pairedNodesGraph.getNodes()){
			//check to see if it is a target node to some edge. if it is then it creates an edge back to its source
			ArrayList<Edge> edgesItBelongsTo = pairedNodesGraph.isATargetNode(parentNode);
			if(!edgesItBelongsTo.isEmpty()){
				for (Edge edge : edgesItBelongsTo){
					propagateGraph.addEdge(edge);
					Edge newEdge = new Edge(parentNode, edge.getSource());
					parentNode.addConnectedEdge(newEdge);
					propagateGraph.addEdge(newEdge);
					if(!propagateGraph.containsNode(edge.getSource())){
						propagateGraph.addNode(edge.getSource());
					}
				}
			}
			if(!propagateGraph.containsNode(parentNode)){
				propagateGraph.addNode(parentNode);
			}
		}
		
		//Assigns values to the edges
		for(Vertex parentNode: propagateGraph.getNodes()){
			if(parentNode.getNeighbours().size() != 0){
				int numberOfChildren = parentNode.getNeighbours().size();
				double myValue = 1.0 / ((double)numberOfChildren);
				for(Edge connectedEdge: parentNode.getConnectedEdges()){
					if (connectedEdge.getValue()== null){
						connectedEdge.setValue(myValue);
					}				
				}
			}			
			
		}		
		
		for(Edge edge: propagateGraph.getEdges()){
			System.out.println("Source: " + edge.getSource().getName() + "----------->" + "Target: "+ edge.getTarget().getName()+"--Edge Val: " + edge.getValue());
		}
		return propagateGraph;
	}
	
	public LinkedHashMap<Vertex, Double> runFixpointComputation(Graph propagatedGraph){
		LinkedHashMap<Vertex, Double> initialValueTable = new LinkedHashMap<Vertex, Double>();
		for (Vertex eachNode: propagatedGraph.getNodes()){
			initialValueTable.put(eachNode, 1.0);
		}
		LinkedHashMap<Vertex, Double> nthIterationTable = iterateComputation(propagatedGraph, initialValueTable);
		nthIterationTable = (LinkedHashMap<Vertex, Double>) sortByValue(nthIterationTable);
		
		LinkedHashMap<Vertex, Double> result = new LinkedHashMap<Vertex, Double>();
		ArrayList<Element> copyList = new ArrayList<Element>();
		ArrayList<Element> elementList = getAllElements();		
		//remove all nodepairs after pairing at least one of every element in the xml doc
		for (Entry<Vertex,Double> nodePair:nthIterationTable.entrySet()){
			if(result.size() != elementList.size()){
				if(!copyList.contains(nodePair.getKey().getVertexElement())){
					copyList.add(nodePair.getKey().getVertexElement());
					result.put(nodePair.getKey(), nodePair.getValue());
				}
			}else{
				break;
			}			
		}
		
		
		
//		System.out.println("Nodes Count: " + propagatedGraph.getNodes().size());
//		for(Entry<Vertex,Double> entry: result.entrySet()){
//			System.out.println("Node: " + entry.getKey().getName() + "-------->"+ "Value: "+entry.getValue());
//		}
		
		return result;
	}
	
	private LinkedHashMap<Vertex, Double> iterateComputation(Graph propagatedGraph,LinkedHashMap<Vertex, Double> fixpointValueTable ){
		LinkedHashMap<Vertex, Double> nthIterationTable = new LinkedHashMap<Vertex, Double>();
		double biggestVal = 0.0;
		for (Vertex eachNode: propagatedGraph.getNodes()){
			double neighoursValues = 0;
			for(Vertex neighbour:eachNode.getNeighbours()){
				double n = fixpointValueTable.get(neighbour);
				double m = neighbour.getConnectedEdgeValue();
				double r = n*m;
				neighoursValues = neighoursValues + r;
			}
			double previousSimValue = fixpointValueTable.get(eachNode);
			double newSimVal = previousSimValue + neighoursValues;
			if(newSimVal > biggestVal){
				//normailizing factor
				biggestVal = newSimVal;
			}
			nthIterationTable.put(eachNode, newSimVal);			
		}		
		//Normalize data
		for(Entry<Vertex, Double> entry: nthIterationTable.entrySet()){
			double normalizedVal = entry.getValue() / biggestVal;
			nthIterationTable.put(entry.getKey(), normalizedVal);
		}
		nthIterationTable = (LinkedHashMap<Vertex, Double>) sortByValue(nthIterationTable);
		double epsilon = 0.0001;
		int numberLessThanEpsilon = 0;
		for(Entry<Vertex,Double> entry: nthIterationTable.entrySet()){			
			if((fixpointValueTable.get(entry.getKey()) - entry.getValue()) < epsilon){
				numberLessThanEpsilon ++;
			}
		}
		
		for(Entry<Vertex,Double> entry: nthIterationTable.entrySet()){
			System.out.println("Node: " + entry.getKey().getName() + "-------->"+ "Value: "+entry.getValue());
			
		}
		if(numberLessThanEpsilon < (0.7 * nthIterationTable.size())){
			iterateComputation(propagatedGraph, nthIterationTable);			
		}
			
		return nthIterationTable;
	
	}
	
	private ArrayList<Element> getAllElements(){
		ArrayList<Element> result = new ArrayList<Element>();
		result.add(doc.getRootElement());
		getChildren(doc.getRootElement(),result);
		return result;
	}
	
	private void getChildren(Element element, ArrayList<Element> list) {
		if(element.getChildren().isEmpty()){
			return;
		}else {
			for(Element elem: element.getChildren()){
				list.add(elem);
				getChildren(elem, list);				
			}
		}		
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ){
		 List<Map.Entry<K, V>> list =  new LinkedList<Map.Entry<K, V>>(map.entrySet());
		 Collections.sort( list, new Comparator<Map.Entry<K, V>>(){
			 public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
	          {
	             return (o2.getValue()).compareTo( o1.getValue() );
	          }
		 } );
		 Map<K, V> result = new LinkedHashMap<K, V>();
		 for (Map.Entry<K, V> entry : list){
			 result.put( entry.getKey(), entry.getValue() );
		 }
		 return result;
	}

	
	


}
