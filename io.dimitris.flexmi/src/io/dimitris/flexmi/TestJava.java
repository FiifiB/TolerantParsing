package io.dimitris.flexmi;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

public class TestJava {
	
	public static void main(String[] args) throws Exception {
		ResourceSet metamodelResourceSet = new ResourceSetImpl();
		metamodelResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());
		Resource metamodelResource = metamodelResourceSet.createResource(URI.createFileURI("ABC.ecore"));
		metamodelResource.load(null);
		
		EPackage ePackage = (EPackage) metamodelResource.getContents().get(0);
		
		Element root = new Element("A");
		Element child1 = new Element("G");
		Element child2 = new Element("C");
//		Element child1_1 = new Element("D");
//		child1.addContent(child1_1);
		root.addContent(child1);
		root.addContent(child2);
		Document doc = new Document(root);
		
		doc =  new FuzzyCorrection(doc, ePackage).fixDocument();
		
		XMLOutputter xmlout = new XMLOutputter();
		System.out.println(xmlout.outputString(doc));
//		System.out.println(doc.getRootElement().getChild("Book").getAttributes());
	
		
		
	}

}
