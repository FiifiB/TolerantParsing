package correctiontool;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

public class ModelChecker {
	private EPackage model;
	private Document XMLdoc;
	private ArrayList<Element>correctedLeafNodes = new ArrayList<Element>();
	private ArrayList<Element> leafNodes = new ArrayList<Element>();
	private ArrayList<Integer> leafNodesDepths = new ArrayList<Integer>();
	
	public ModelChecker(EPackage model, Document XMLdoc) {
		this.model = model;
		this.XMLdoc = XMLdoc;
	}
	
	public Document StartChecking(){
//		correctAllLeafNodes(XMLdoc);
		return correctRootNode(XMLdoc);
	}
	
	private Document correctAllLeafNodes(Document possiblyFaultyDOC){
		ArrayList<Element>leafNodes = getLeafNodes(possiblyFaultyDOC);
		
		for(Element leafNode: leafNodes){
			//find best tagName for each leafNode
			LevenshteinDistance distanceMatching = new LevenshteinDistance(modelClassNametoStringArray());			
			String bestTagMatch = distanceMatching.bestMatch(leafNode.getName());
			//check to see if the bestTagMatch has matching attributes as the equivalent model class.
			//Before that we correct all attribute typos by comparing them to list of all possible attributes
			LevenshteinDistance distanceMatchingAtt = new LevenshteinDistance(allModelAttstoStringArray());
			for(Attribute faultyAtts: leafNode.getAttributes()){
				//~~~~hope that works
				correctAttributeName(leafNode, faultyAtts.getName(),distanceMatchingAtt.bestMatch(faultyAtts.getName()));
			}

			//check each attribute in the model class is in the xml node.
			boolean val = false;
			for(String att :getClassAtts((EClass)model.getEClassifier(bestTagMatch))){
				if (XMLNodeAtttoArray(leafNode).contains(att)){
					val = true;
				}else{
					val= false;
					break;
				}
			}
			
			if(val == true){
				correctTagName(leafNode, bestTagMatch);
				ArrayList<String> classmodleatt = new ArrayList<String>();
				
				//get list of all attribute names in the equivalent node class in the model
				for(EAttribute modelclassAtt: ((EClass)model.getEClassifier(leafNode.getName())).getEAttributes()){
					classmodleatt.add(modelclassAtt.getName());
				}
				
				// remove any attributes that are not in the model class.
				for(Attribute allAtt: leafNode.getAttributes()){
					if (!classmodleatt.contains(allAtt)){
						leafNode.removeAttribute(allAtt);						
					}
				}
			}
			
			correctedLeafNodes.add(leafNode);
			
		}
		
		// TODO add corrected nodes to the document and remove faulty nodes.
		return null;		
	}
	
	private Document correctRootNode(Document possiblyFaultyDOC){
		//find best tagName for rootNode
		LevenshteinDistance distanceMatching = new LevenshteinDistance(modelClassNametoStringArray());			
		String bestTagMatch = distanceMatching.bestMatch(possiblyFaultyDOC.getRootElement().getName());
		//check to see if the bestTagMatch has matching attributes as the equivalent model class.
		//Before that, we correct all attribute typos by comparing them to list of all possible attributes
		LevenshteinDistance distanceMatchingAtt = new LevenshteinDistance(allModelAttstoStringArray());
		for(Attribute faultyAtts: possiblyFaultyDOC.getRootElement().getAttributes()){
			//~~~~hope that works
			correctAttributeName(possiblyFaultyDOC.getRootElement(), faultyAtts.getName(),distanceMatchingAtt.bestMatch(faultyAtts.getName()));
		}

		//check each attribute in the model class is in the xml node.
		boolean val = false;
		for(String att :getClassAtts((EClass)model.getEClassifier(bestTagMatch))){
			if (XMLNodeAtttoArray(possiblyFaultyDOC.getRootElement()).contains(att)){
				val = true;
			}else{
				val= false;
				break;
			}
		}
		
		if(val == true){
			correctTagName(possiblyFaultyDOC.getRootElement(), bestTagMatch);
			ArrayList<String> classmodleatt = new ArrayList<String>();
			
			//get list of all attribute names in the equivalent node class in the model
			for(EAttribute modelclassAtt: ((EClass)model.getEClassifier(possiblyFaultyDOC.getRootElement().getName())).getEAttributes()){
				classmodleatt.add(modelclassAtt.getName());
			}
			
			// remove any attributes that are not in the model class.
			for(Attribute allAtt: possiblyFaultyDOC.getRootElement().getAttributes()){
				if (!classmodleatt.contains(allAtt.getName())){
					possiblyFaultyDOC.getRootElement().removeAttribute(allAtt);						
				}
			}
		}
		
		int counter = 1;
		findChildren(possiblyFaultyDOC.getRootElement(), counter);
		return possiblyFaultyDOC;
	}
	
	private void findChildren(Element recursiveNode, int counter){
		if (recursiveNode.getChildren().isEmpty()){
			leafNodesDepths.add(counter);
			leafNodes.add(recursiveNode);
			counter = counter - 1;
		}else{
			for(Element node_counter: recursiveNode.getChildren()){
				//find best tagName for each NodeChild
				LevenshteinDistance distanceMatching = new LevenshteinDistance(modelClassNametoStringArray());			
				String bestTagMatch = distanceMatching.bestMatch(node_counter.getName());
				//check to see if the bestTagMatch has matching attributes as the equivalent model class.
				//Before that, we correct all attribute typos by comparing them to list of all possible attributes
				LevenshteinDistance distanceMatchingAtt = new LevenshteinDistance(allModelAttstoStringArray());
				for(Attribute faultyAtts: node_counter.getAttributes()){
					//~~~~hope that works
					correctAttributeName(node_counter, faultyAtts.getName(),distanceMatchingAtt.bestMatch(faultyAtts.getName()));
				}

				//check each attribute in the model class is in the xml node.
				boolean val = false;
				for(String att :getClassAtts((EClass)model.getEClassifier(bestTagMatch))){
					if (XMLNodeAtttoArray(node_counter).contains(att)){
						val = true;
					}else{
						val= false;
						break;
					}
				}
				
				if(val == true){
					correctTagName(node_counter, bestTagMatch);
					ArrayList<String> classmodleatt = new ArrayList<String>();
					
					//get list of all attribute names in the equivalent node class in the model
					for(EAttribute modelclassAtt: ((EClass)model.getEClassifier(node_counter.getName())).getEAttributes()){
						classmodleatt.add(modelclassAtt.getName());
					}
					
					// remove any attributes that are not in the model class.
					for(Attribute allAtt: node_counter.getAttributes()){
						if (!classmodleatt.contains(allAtt.getName())){
							node_counter.removeAttribute(allAtt);						
						}
					}
				}
				counter++;
				findChildren(node_counter, counter);
			}
		}		
	}	
	
	private ArrayList<Element> getLeafNodes (Document XMLdoc){
		int counter = 1;
		for(Element node: XMLdoc.getRootElement().getChildren()){
			findChildren(node, counter);
		}
		return leafNodes;
	}
	
	private Document replaceNodesInDoc(ArrayList<Element> replacementNodes,ArrayList<Integer> NodeDepths, Document FaultyDoc){
		return null;
	}
	
	private Element correctTagName(Element faultTag, String correctTag){
		return faultTag.setName(correctTag);
		
	}
	
	private Element correctAttributeName(Element faultNode, String faultAttName, String correctAttName){
//		String attValue = faultNode.getAttributeValue(faultAttName);
//		Attribute correctedAtt = 
		faultNode.getAttribute(faultAttName).setName(correctAttName);
//		faultNode.removeAttribute(faultAttName);
//		correctedAtt.setValue(attValue);
//		faultNode.setAttribute(correctedAtt);
		return faultNode;
	}
	
	private ArrayList<String> XMLNodeAtttoArray (Element element){
		ArrayList<String> Attributes = new ArrayList<String>();
		for (Attribute att :element.getAttributes()){
			Attributes.add(att.getName());
		}
		return Attributes;
	}
	
	private String[] allModelAttstoStringArray (){
		List<String> Attributes = new ArrayList<String>();
		for(EClassifier eclasses: model.getEClassifiers()){
			for (EAttribute att :((EClass)eclasses).getEAttributes()){
				Attributes.add(att.getName());
			}
		}
		
		String [] attNames = Attributes.toArray(new String [Attributes.size()]) ;
		return attNames;
	}
	
	private ArrayList<String> getClassAtts(EClass modelClass){
		ArrayList<String> Attributes = new ArrayList<String>();
		for(EAttribute att :modelClass.getEAttributes()){
			Attributes.add(att.getName());
		}
		return Attributes;
	}
	
	private String[] modelClassNametoStringArray (){
		List<String> classNames = new ArrayList<String>();
		for (EClassifier modelClass :model.getEClassifiers()){
			classNames.add(modelClass.getName());
		}
		String [] classStringNames = classNames.toArray(new String [classNames.size()]) ;
		return classStringNames;
	}

}
