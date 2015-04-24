package correctiontool;

import graphtools.Graph;
import graphtools.Vertex;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

public class ErrorCorrector {
	private Document doc;
	private LinkedHashMap<Vertex, Double> bestMatchings;
	private Graph model;
	
	public ErrorCorrector(Document XMLDocument, LinkedHashMap<Vertex, Double> bestMatchings, Graph modelGraph){
		this.doc = XMLDocument;
		this.bestMatchings = bestMatchings;
		this.model = modelGraph;
		
	}
	
	public Document start(){
		return fixRootDocument(doc.getRootElement());
	}
	
	private void repairChildren(Element parent, EClass parentClass, Element child){
		for(Entry<Vertex, Double> entry : bestMatchings.entrySet()){
			if(entry.getKey().pairContainsNode(child)){
				Vertex parentVertex =  new Vertex(parentClass);
				Vertex childVertex = new Vertex(entry.getKey().getVertexEclass());
				if(this.model.edgeExistBetween(parentVertex, childVertex)){
					Element newChild = new Element(entry.getKey().getVertexEclass().getName());
					//Set attributes of reconstructed element using its equivalent model class
					for(EAttribute atts: entry.getKey().getVertexEclass().getEAttributes()){
						newChild.setAttribute(atts.getName(), child.getAttributeValue(bestAttribute(child.getAttributes(), atts).getName()));
					}
					
					// Add element content to corrected element from error element.
					 newChild.setText(child.getText());
					
					parent.addContent(newChild);
					if(!child.getChildren().isEmpty()){
						for(Element children: child.getChildren()){
							repairChildren(newChild,entry.getKey().getVertexEclass(),children);
						}			
					}
					break;
				}
			}
		}
	}
	
	private Document fixRootDocument(Element root){
		Element newRoot = null;
		EClass newRootEclass = null;
		for(Entry<Vertex, Double> entry : bestMatchings.entrySet()){
			if(entry.getKey().pairContainsNode(root)){
				newRootEclass = entry.getKey().getVertexEclass();
				newRoot = new Element(entry.getKey().getVertexEclass().getName());
				LevenshteinDistance distanceMatchingAtt = new LevenshteinDistance(nodeAttsToStringArray(entry.getKey().getVertexEclass()));
				
				//Set attributes of reconstructed element using its equivalent model class
				for(EAttribute atts: entry.getKey().getVertexEclass().getEAttributes()){
					newRoot.setAttribute(atts.getName(), root.getAttributeValue(bestAttribute(root.getAttributes(), atts).getName()));
				}
				
				// Add element content to corrected element from error element.
				 newRoot.setText(root.getText());
				
				if(!root.getChildren().isEmpty()){
					for(Element children: root.getChildren()){
						repairChildren(newRoot,newRootEclass,children);
					}			
				}				
				break;
			}
		}
		
		
		
		Document newDoc  = new Document(newRoot);
		return newDoc;
	}
	
	private String[] nodeAttsToStringArray(EClass node){
		List<String> Attributes = new ArrayList<String>();
		for(EAttribute att: node.getEAttributes()){
			Attributes.add(att.getName());
		}
		String [] attNames = Attributes.toArray(new String [Attributes.size()]) ;
		return attNames;
	}
	
	private Attribute bestAttribute(List<Attribute> elementAtts, EAttribute eattribute){
		Attribute bestAttribute = null ;
		int tabscore = 5000000;
		for(Attribute atts: elementAtts){
			int score = LevenshteinDistance.distance(atts.getName(), eattribute.getName());
			if(score < tabscore){
				tabscore = score;
				bestAttribute = atts;
			}
		}
		return bestAttribute;
	}

}
