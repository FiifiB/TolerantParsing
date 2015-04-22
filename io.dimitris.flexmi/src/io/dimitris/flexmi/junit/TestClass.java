package io.dimitris.flexmi.junit;

import io.dimitris.flexmi.FuzzyCorrection;
import io.dimitris.flexmi.TestFailedException;

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
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;

public class TestClass {

	public TestClass(){
		
	}
//	public static void main(String[] args) {
//		EcoreGenerator gen = new EcoreGenerator();
//		try {
//			gen.generate(new File("ABC.emf"), true);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	
	@Test
	public void testCase1() {
		
		//Wrong tag names in two elements
		try {
			testDoc(new File("library.emf"), new File("ExpectedDocument1.xml"), new File("TestDocument1.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCase2() {
		
		//Wrong tag names and wrong attribute names in two elements
		try {
			testDoc(new File("library.emf"), new File("ExpectedDocument1.xml"), new File("TestDocument2.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCase3(){
		//Multiple elements with the same name
	}
	
	@Test
	public void testCase4(){}
	
	private void testDoc(File EmfModel,File ExpectedXML,File TestDocument) throws IOException{
		EcoreGenerator gen = new EcoreGenerator();
		try {
			gen.generate(EmfModel, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String modelname = FilenameUtils.removeExtension(EmfModel.getName());
		
		ResourceSet metamodelResourceSet = new ResourceSetImpl();
		metamodelResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());
		Resource metamodelResource = metamodelResourceSet.createResource(URI.createFileURI(modelname+".ecore"));
		metamodelResource.load(null);
		
		EPackage ePackage = (EPackage) metamodelResource.getContents().get(0);
		
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		Document expectedDoc = null;
		try {
			doc = (Document) builder.build(TestDocument);
			expectedDoc = (Document) builder.build(ExpectedXML);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XMLOutputter xmlout = new XMLOutputter(Format.getCompactFormat());
		String fixedDoc = xmlout.outputString(new FuzzyCorrection(doc, ePackage).fixDocument());
		String correctDoc = xmlout.outputString(expectedDoc);
		System.out.println(fixedDoc);
		
		try {
			if (fixedDoc.equals(correctDoc)){			
				System.out.println("The corrected XML matches the expected XML");		
			}else{
				throw new TestFailedException();
			}
		} catch (TestFailedException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}

}
