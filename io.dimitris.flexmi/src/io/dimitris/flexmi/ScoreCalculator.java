package io.dimitris.flexmi;

import graphtools.Graph;
import graphtools.Vertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

public class ScoreCalculator {
	private Document XML;
	private EPackage model;
	private Graph DocGraph;
	private Graph ModelGraph;
	
	public ScoreCalculator(Document doc, EPackage epackage, Graph DocGraph, Graph ModelGraph){
		this.XML = doc;
		this.model = epackage;
		this.DocGraph = DocGraph;
		this.ModelGraph = ModelGraph;
	}

	public ArrayList<Vertex> createDistanceTable() {
		ArrayList<Vertex> result = new ArrayList<Vertex>();
		for(Element element: getAllElements() ){
			for(EClass eclass: getAllEClass()){
				Vertex node = new Vertex(element, eclass);
				node.setDistanceScore(getDistanceScore(element, eclass));
				result.add(node);
			}
		}

		Collections.sort(result);

//		ArrayList<Vertex> newResult = new ArrayList<Vertex>();
//		ArrayList<EClass> copyList = new ArrayList<EClass>();
//		ArrayList<EClass> eclassList = getAllEClass();
//		ArrayList<Element> copyList2 = new ArrayList<Element>();
//		ArrayList<Element> elementList = getAllElements();	
//		//remove all nodepairs after pairing at least one of every eclass and one of every element in the model
//		for (Vertex nodePair:result){
//			if((copyList.size() != eclassList.size()) || (copyList2.size() != elementList.size())){
//				if(!copyList.contains(nodePair.getVertexEclass())){
//					copyList.add(nodePair.getVertexEclass());
//					if(!newResult.contains(nodePair)){
//						newResult.add(nodePair);
//					}
//					
//				}else if (!newResult.contains(nodePair)) {
//					newResult.add(nodePair);
//				}
//				if(!copyList2.contains(nodePair.getVertexElement())){
//					copyList2.add(nodePair.getVertexElement());
//					if(!newResult.contains(nodePair)){
//						newResult.add(nodePair);
//					}
//					
//				}else if (!newResult.contains(nodePair)) {
//					newResult.add(nodePair);
//				}
//			}		
//		}		
		return result;
	}

	private ArrayList<Element> getAllElements(){
		ArrayList<Element> result = new ArrayList<Element>();
		result.add(XML.getRootElement());
		getChildren(XML.getRootElement(),result);
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
	
	private ArrayList<EClass> getAllEClass(){
		ArrayList<EClass> result = new ArrayList<EClass>();
		for(EClassifier eclassifier: model.getEClassifiers()){
			EClass eclass = (EClass) eclassifier;
			result.add(eclass);
		}
		return result;
	}
	
	private double getDistanceScore(Element element, EClass eclass) {
		double sum = 0;		
		double nameDist = LevenshteinDistance.distance(element.getName(), eclass.getName());
		sum =  sum + nameDist;
		LevenshteinDistance attDistance = new LevenshteinDistance(classAttstoStringArray(eclass));
		double attSum = 0;
		int numberOfatts = eclass.getEAllAttributes().size();
		if(numberOfatts != 0 || element.getAttributes().size() != 0){
			//get all the distance of each attributes with the best match in the model and the divide the sum by how many attributes in the model class
			for(Attribute att: element.getAttributes()){
				attSum = attSum + LevenshteinDistance.distance(att.getName(), attDistance.bestMatch(att.getName()));
			}
			System.out.println("sum dist: " + attSum);
			attSum = attSum / numberOfatts;
			sum = sum + attSum;
		}
		
		int numberOfChildrenXML = DocGraph.getNode(element).getConnectedEdges().size();
		int numberOfChildrenModel = ModelGraph.getNode(eclass).getConnectedEdges().size();
		//get difference between number of children in graph and model
		int absdifference = Math.abs(numberOfChildrenXML - numberOfChildrenModel);
		sum = sum + absdifference;
		return sum;
	}
	
	private String[] classAttstoStringArray (EClass eclass){
		List<String> Attributes = new ArrayList<String>();
		for (EAttribute att :eclass.getEAttributes()){
			Attributes.add(att.getName());
		}
		
		String [] attNames = Attributes.toArray(new String [Attributes.size()]) ;
		return attNames;
	}

}
