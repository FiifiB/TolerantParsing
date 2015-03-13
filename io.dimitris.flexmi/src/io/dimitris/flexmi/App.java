package io.dimitris.flexmi;

import java.util.ArrayList;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

public class App {
	
	// Simmetrics library
	public static void main(String[] args) throws Exception {
		
		ResourceSet metamodelResourceSet = new ResourceSetImpl();
		metamodelResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());
		Resource metamodelResource = metamodelResourceSet.createResource(URI.createFileURI("library.ecore"));
		metamodelResource.load(null);
		
		EPackage ePackage = (EPackage) metamodelResource.getContents().get(0);
		
		Element root = new Element("Library");
		root.setAttribute("name", "my Library");
		Element child1 = new Element("Book");
		child1.setAttribute("name", "Great Expectations");
		Element child1_1 = new Element("Chapter");
		child1_1.setAttribute("name", "Chapter 1");
		child1_1.setAttribute("Pages", "20");
		child1.addContent(child1_1);
		root.addContent(child1);
		Document doc = new Document(root);
		
		new App().loadObj(ePackage, doc);
		
//		new App().load(ePackage, "H");
	}
	
	public Resource load(EPackage ePackage, String rootElementTagName) {
		
		Resource resource = new ResourceImpl();
		ArrayList<String> tags = new ArrayList<String>();
		
		for (EClassifier eClassifier : ePackage.getEClassifiers()) {
			tags.add(eClassifier.getName());
		}
		
		String [] tags2 = tags.toArray(new String [tags.size()]) ;
		LevenshteinDistance distanceMatching = new LevenshteinDistance(tags2);
		
		String bestTagMatch = distanceMatching.bestMatch(rootElementTagName);
		System.out.println(bestTagMatch);
		
		if (bestTagMatch == null){
			System.out.println("There are no matches to this tagName");
		}else{
			EClass eClass = (EClass) ePackage.getEClassifier(bestTagMatch);
			EObject rootModelElement = ePackage.getEFactoryInstance().create(eClass);
			System.out.println(rootModelElement);
		}
		
		for (EClassifier eClassifier : ePackage.getEClassifiers()) {
			if (eClassifier.getName().equals(rootElementTagName)) {
				
				EClass eClass = (EClass) eClassifier;
				EObject rootModelElement = ePackage.getEFactoryInstance().create(eClass);
				System.out.println(rootModelElement);
			}
		}
		
		return resource;
		
	}
	
	public Resource loadObj(EPackage ePackage, Document xmldoc){
		ModelChecker checker = new ModelChecker(ePackage, xmldoc);
		Document finishDoc = checker.StartChecking();
		XMLOutputter xmlout = new XMLOutputter();
		System.out.println(xmlout.outputString(finishDoc));
		System.out.println(finishDoc.getRootElement().getChild("Book").getAttributes());
		return null;
	}
}
