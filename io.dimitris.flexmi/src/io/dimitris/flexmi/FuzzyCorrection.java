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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

public class FuzzyCorrection {
	private Document doc;
	private EPackage model;
	private Graph pairwiseGraph;
	private Graph CorrectorGraph;
	
	public FuzzyCorrection(Document XMLDoc, EPackage epackage){
		this.doc = XMLDoc;
		this.model = epackage;
	}
	
	public Document fixDocument(){
		Graph XMLGraph = convertDocToGraph();
		Graph modelGraph = convertModelToGraph(this.model);
		pairwiseGraph = matchGraphs(XMLGraph, modelGraph);
		if (pairwiseGraph.getNodes().isEmpty()){
			System.out.println("Could not make a paired graph");
			return doc;
		}
		CorrectorGraph = pairwiseGraph;
		Graph propagationGraph = propagateGraph(pairwiseGraph);
		LinkedHashMap<Vertex, Double> fixpointResult = runFixpointComputation(propagationGraph);
		System.out.println(fixpointResult.size());
		Document repairedDoc = new ErrorCorrector(doc,fixpointResult,modelGraph).start();
//		Document repairedDoc = new ErrorCorrector2(doc,fixpointResult,CorrectorGraph).start();
		XMLOutputter xmlout = new XMLOutputter();
		System.out.println(xmlout.outputString(repairedDoc));
		return repairedDoc;
	}
	
	public void findErrors(){
		Graph XMLGraph = convertDocToGraph();
		Graph modelGraph = convertModelToGraph(this.model);
		pairwiseGraph = matchGraphs(XMLGraph, modelGraph);
		Graph propagationGraph = propagateGraph(pairwiseGraph);
		LinkedHashMap<Vertex, Double> fixpointResult = runFixpointComputation(propagationGraph);
		new ErrorReporter(doc, fixpointResult).start();;
	}
	
	public Graph convertDocToGraph(){
		Graph DocGraph = new Graph();
		Vertex rootNode = new Vertex(doc.getRootElement());
		DocGraph.addNode(rootNode);
		if (!doc.getRootElement().getChildren().isEmpty()){
			for (Element child: doc.getRootElement().getChildren()){
				Vertex childNode = new Vertex(child);
				Edge parentofEdge = new Edge(rootNode, childNode);
				rootNode.addConnectedEdge(parentofEdge);
				DocGraph.addNode(childNode);
				DocGraph.addEdge(parentofEdge);
				if(!child.getChildren().isEmpty()){
					linkChild(child, childNode, DocGraph);
				}
			}
		}
		for (Vertex node: DocGraph.getNodes()){
			System.out.println(node.getName());
			
		}
		return DocGraph;
	}
	
	public static Graph convertDocToGraph(Document mydoc){
		Graph DocGraph = new Graph();
		Vertex rootNode = new Vertex(mydoc.getRootElement());
		DocGraph.addNode(rootNode);
		if (!mydoc.getRootElement().getChildren().isEmpty()){
			for (Element child: mydoc.getRootElement().getChildren()){
				Vertex childNode = new Vertex(child);
				Edge parentofEdge = new Edge(rootNode, childNode);
				rootNode.addConnectedEdge(parentofEdge);
				DocGraph.addNode(childNode);
				DocGraph.addEdge(parentofEdge);
				if(!child.getChildren().isEmpty()){
					linkChild(child, childNode, DocGraph);
				}
			}
		}
		for (Vertex node: DocGraph.getNodes()){
			System.out.println(node.getName());
			
		}
		return DocGraph;
	}
	
	private static void linkChild(Element childToParent, Vertex childToParentNode, Graph docGraph){
		for(Element child: childToParent.getChildren()){
			Vertex childNode = new Vertex(child);
			Edge parentofEdge = new Edge(childToParentNode, childNode);
			childToParentNode.addConnectedEdge(parentofEdge);
			docGraph.addNode(childNode);
			docGraph.addEdge(parentofEdge);
			if(!child.getChildren().isEmpty()){
				linkChild(child, childNode, docGraph);
			}
		}
	}
	
	public Graph convertModelToGraph(EPackage myModel){
		Graph modelGraph = new Graph();		
		for(EClassifier eclassifier: myModel.getEClassifiers()){
			EClass eclass = (EClass)eclassifier;
			checkAndCreateNodes(eclass, modelGraph, myModel);
		}
		return modelGraph;
	}
	
	private void checkAndCreateNodes(EClass eclass, Graph myGraph, EPackage myModel){
		if (eclass.isAbstract()){
			for(EClassifier eclassifier2: myModel.getEClassifiers()){
				EClass eclass2 = (EClass)eclassifier2;
				if (eclass2.isSuperTypeOf(eclass)){
					Vertex node = new Vertex(eclass2);
					if (myGraph.containsNode(node)){
						classLinkToChild(eclass, myGraph.getNode(node), myGraph);
					}else {
						myGraph.addNode(node);
						classLinkToChild(eclass, node, myGraph);				
					}						
				}
			}
		}else{
			Vertex classNode = new Vertex(eclass);
			if (myGraph.containsNode(classNode)){
				classLinkToChild(eclass, myGraph.getNode(classNode), myGraph);
			}else {
				myGraph.addNode(classNode);
				classLinkToChild(eclass, classNode, myGraph);				
			}
		}
	}
	
	private void classLinkToChild(EClass eclass, Vertex classNode, Graph myGraph ){
		for(EReference reference: eclass.getEReferences()){
			if(reference.isContainment()){
				EClass containmentOfClass = reference.getEReferenceType();
				Vertex Child = new Vertex(containmentOfClass);				
				if(myGraph.containsNode(Child)){
					Edge containmentEdge = new Edge(classNode, myGraph.getNode(Child));
					myGraph.addEdge(containmentEdge);
					classNode.addConnectedEdge(containmentEdge);
				}else{
					Edge containmentEdge = new Edge(classNode, Child);
					myGraph.addEdge(containmentEdge);
					classNode.addConnectedEdge(containmentEdge);
					myGraph.addNode(Child);
				}
			}
		}
	}
	
	public Graph matchGraphs(Graph DocGraph, Graph ModelGraph){
		
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
	
	public Graph propagateGraph(Graph pairedNodesGraph){
		for(Vertex parentNode: pairedNodesGraph.getNodes()){
			
			//check to see if it is a target node to some edge. if it is then it creates an edge back to its source
			ArrayList<Edge> edgesItBelongsTo = pairedNodesGraph.isATargetNode(parentNode);
			if (!edgesItBelongsTo.isEmpty()){
				for (Edge edge : edgesItBelongsTo){
					Edge newEdge = new Edge(parentNode, edge.getSource());
					parentNode.addConnectedEdge(newEdge);
					pairedNodesGraph.addEdge(newEdge);
				}
			}

		}
		for(Vertex parentNode: pairedNodesGraph.getNodes()){
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
		
		for(Edge edge: pairedNodesGraph.getEdges()){
			System.out.println("Source: " + edge.getSource().getName() + "----------->" + "Target: "+ edge.getTarget().getName()+"--Edge Val: " + edge.getValue());
		}
		return pairedNodesGraph;
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
	
//	private ArrayList<Graph> getCompleteGraphs(LinkedHashMap<Vertex, Double> fixpointResult){
//		ArrayList<Graph> graphResults = new ArrayList<Graph>();
//		
//		return graphResults;
//	}
	
	
 

}
