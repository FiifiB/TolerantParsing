package correctiontool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

public class RatingsChecker {
	private EPackage model;
	private Document doc;
	public RatingsChecker(EPackage epackage, Document XMLDoc){
		model = epackage;
		doc = XMLDoc;
	}
	
	public void startCalculating(){
		MultiValueMap<Integer, EClass> elementScoreTable = new MultiValueMap<Integer, EClass>();
		for(EClassifier eclassifier:model.getEClassifiers()){
			EClass modelClass = (EClass)eclassifier;
			elementScoreTable.put(distanceMatch(doc.getRootElement(), modelClass), modelClass);
		}
		EClass bestClassFit = bestClass(elementScoreTable);
		correctTagName(doc.getRootElement(), bestClassFit.getName());
		// TODO correct all attribute names
		ArrayList<String> classmodleatt = new ArrayList<String>();
		
		//get list of all attribute names in the equivalent node class in the model
		for(EAttribute modelclassAtt: bestClassFit.getEAttributes()){
			classmodleatt.add(modelclassAtt.getName());
		}
		
		// remove any attributes that are not in the model class.
		for(Attribute allAtt: doc.getRootElement().getAttributes()){
			if (!classmodleatt.contains(allAtt.getName())){
				doc.getRootElement().removeAttribute(allAtt);						
			}
		}
	}
	
	private Integer distanceMatch(Element element, EClass eclass){
		int sum = 0;
		int tagdistance = LevenshteinDistance.distance(element.getName(),eclass.getName());
		int attdistance=0;
		ArrayList<attrScorePair> bestAttMatches = new ArrayList<RatingsChecker.attrScorePair>();
		
		for(Attribute element_att: element.getAttributes()){
			MultiValueMap<Integer, String> bestAttTable = new MultiValueMap<Integer, String>();
			int sizeOfTable = 0;
			for(EAttribute class_att: eclass.getEAttributes()){
				bestAttTable.put(LevenshteinDistance.distance(element_att.getName(), class_att.getName()), class_att.getName());
				sizeOfTable ++;
				if(sizeOfTable == eclass.getEAttributes().size()){
					attrScorePair bestattpair = bestAttribute(bestAttTable, element_att); 
					bestAttMatches.add(bestattpair);
					attdistance = attdistance + bestattpair.getScore();
				}
			}
		}
		sum = tagdistance + attdistance;
		return sum;	
		}
	
	private attrScorePair bestAttribute(MultiValueMap<Integer, String> table, Attribute errorAtt){
		ArrayList<Integer> s = new ArrayList<Integer>();
		for(Integer key : table.keySet()){
			s.add(key);
		}
		Collections.sort(s);
		
		ArrayList<String> topCHoice = (ArrayList<String>) table.getCollection(s.get(0));
		if(topCHoice.size()==1){
			Attribute correctAtt = errorAtt;
			correctAtt.setName(topCHoice.get(0));
			attrScorePair result = new attrScorePair();
			result.setAttribute(correctAtt);
			result.setScore(s.get(0));
			return result;
		}else{
			//TODO find out if the first value has more than one value. Then pick one at random with equal probability
			return null;
		}		
	}
	
	private class attrScorePair{
		Attribute attribute;
		Integer score;
		
		public Attribute getAttribute() {
			return attribute;
		}
		public void setAttribute(Attribute attribute) {
			this.attribute = attribute;
		}
		public Integer getScore() {
			return score;
		}
		public void setScore(Integer score) {
			this.score = score;
		}
	}
	
	private EClass bestClass(MultiValueMap<Integer, EClass> table){
		ArrayList<Integer> s = new ArrayList<Integer>();
		for(Integer key : table.keySet()){
			s.add(key);
		}
		Collections.sort(s);
		
		ArrayList<EClass> topCHoice = (ArrayList<EClass>) table.getCollection(s.get(0));
		if(topCHoice.size()==1){
			EClass result = topCHoice.get(0);
			return result;
		}else{
			//TODO find out if the first value has more than one value. Then pick one at random with equal probability
			return null;
		}		
	}
	
	private Element correctTagName(Element faultTag, String correctTag){
		return faultTag.setName(correctTag);
		
	}

}


