package io.dimitris.flexmi;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.emfatic.core.generator.ecore.EcoreGenerator;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class LoadData {

	public LoadData() {
		// TODO Auto-generated constructor stub
	}
	
	public static EPackage getEpackage(File EmfModel) throws Exception {
		EPackage modelpackage = null;
		EcoreGenerator gen = new EcoreGenerator();
		gen.generate(EmfModel, true);		
		
		String modelname = FilenameUtils.removeExtension(EmfModel.getName());
		
		ResourceSet metamodelResourceSet = new ResourceSetImpl();
		metamodelResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
		.put("ecore", new XMIResourceFactoryImpl());
		Resource metamodelResource = metamodelResourceSet
				.createResource(URI.createFileURI(modelname+".ecore"));
		
		metamodelResource.load(null);		
		modelpackage = (EPackage) metamodelResource.getContents().get(0);
		
		return modelpackage;
	}
	
	public static Document getDocument(File XMLFile) throws JDOMException, IOException{
		Document xmldocument = null;
		
		SAXBuilder builder = new SAXBuilder();
		
		xmldocument = (Document) builder.build(XMLFile);
		
		return xmldocument;		
	}

}
