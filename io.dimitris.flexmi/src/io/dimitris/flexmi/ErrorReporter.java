package io.dimitris.flexmi;

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

public class ErrorReporter {
	private Document doc;
	private LinkedHashMap<Vertex, Double> bestMatchings;
	
	public ErrorReporter(Document XMLDocument, LinkedHashMap<Vertex, Double> bestMatchings){
		this.doc = XMLDocument;
		this.bestMatchings = bestMatchings;
	}
	
	public ArrayList<String> start(){
		ArrayList<String> errorReport = new ArrayList<String>();
		getErrorReport(doc.getRootElement(),errorReport);
		if(errorReport.isEmpty()){
			System.out.println("Your XML document has been checked and there havent been any errors");
		}else {
			for(String errors: errorReport){
				System.out.println(errors);
			}
		}
		return errorReport;
	}
	
	private void getErrorReport(Element element, ArrayList<String> errorReport){
		
		for(Entry<Vertex, Double> entry : bestMatchings.entrySet()){
			if(entry.getKey().pairContainsNode(element)){
				if(!element.getName().equals(entry.getKey().getVertexEclass().getName())){
					String error =  "There is an error with the Element Name: " + element.getName() +"\n" 
							+ "A suggestion would be to change it to: " + entry.getKey().getVertexEclass().getName() ;
					errorReport.add(error);
				}
				LevenshteinDistance distanceMatchingAtt = new LevenshteinDistance(nodeAttsToStringArray(entry.getKey().getVertexEclass()));
				//check the number of attributes in the Equivalent EClass to the number of element attributes
				//if element atts is more than EClass element then we chose the ones with the highest score for each attribute
				//and then delete the lowest scoring
				if(element.getAttributes().size() != entry.getKey().getVertexEclass().getEAttributes().size()){
					if(element.getAttributes().size() > entry.getKey().getVertexEclass().getEAttributes().size() ){
						String error =  "There are too many attributes with the Element: " + element.getName() +"\n" 
								+ "\t The element should only contain one of each of these attributes: " + entry.getKey().getVertexEclass().getName() + "\n";
						String errorPt2 = " Attributes: "; 
						for(EAttribute atts: entry.getKey().getVertexEclass().getEAttributes()){
							errorPt2 = errorPt2 + atts.getName() + ", "; 
						}
						error = error + errorPt2;
						errorReport.add(error);
					}else{
						String error =  "There aren't enough attributes with the Element: " + element.getName() +"\n" 
								+ "\t The element should contain one of each of these attributes: " + entry.getKey().getVertexEclass().getName() + "\n";
						String errorPt2 = " Attributes: "; 
						for(EAttribute atts: entry.getKey().getVertexEclass().getEAttributes()){
							errorPt2 = errorPt2 + atts.getName() + ", "; 
						}
						error = error + errorPt2;
						errorReport.add(error);
					}
				}
				for(Attribute atts: element.getAttributes()){
					ArrayList<String>eattNames = new ArrayList<String>();
					for(EAttribute eatts: entry.getKey().getVertexEclass().getEAttributes()){
						eattNames.add(eatts.getName());
					}
					if (!eattNames.contains(atts.getName())){
						String error = "\t The Attribute: " + " \"" + atts.getName() + " \"" + " in the Element Name: " + element.getName() + 
								" has an error \n" + "\t A suggestion would be to change it to: " + distanceMatchingAtt.bestMatch(atts.getName()) + "\n";
						errorReport.add(error);
					}
				}
				break;
			}
		}
		if(!element.getChildren().isEmpty()){
			for(Element child: element.getChildren()){
				getErrorReport(child,errorReport);
			}	
		}
	}
	
	private String[] nodeAttsToStringArray(EClass node){
		List<String> Attributes = new ArrayList<String>();
		for(EAttribute att: node.getEAttributes()){
			Attributes.add(att.getName());
		}
		String [] attNames = Attributes.toArray(new String [Attributes.size()]) ;
		return attNames;
	}
	

}
