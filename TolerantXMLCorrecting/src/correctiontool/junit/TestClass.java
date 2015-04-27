package correctiontool.junit;

import graphtools.Graph;
import graphtools.Vertex;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

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

import correctiontool.ErrorCorrector;
import correctiontool.FloodingCorrection;
import correctiontool.FuzzyCorrection;
import correctiontool.GraphTransform;
import correctiontool.LoadData;
import correctiontool.PairwiseValidation;
import correctiontool.TestFailedException;

public class TestClass {

	public TestClass(){
		
	}
	
	@Test
	public void testCase1() throws Exception {
		
		//Wrong tag names in two elements
		testDoc(new File("library.emf"), new File("ExpectedDocument1.xml"), new File("TestDocument1.xml"));
	}
	
	@Test
	public void testCase2() throws Exception {
		
		//Wrong tag names and wrong attribute names in multiple elements
		testDoc(new File("library.emf"), new File("ExpectedDocument3.xml"), new File("TestDocument2.xml"));
	}
	
	
	
	@Test
	public void testCase3() throws Exception{
		//Multiple elements with the same name
		testDoc(new File("library.emf"), new File("ExpectedDocument2.xml"), new File("TestDocument3.xml"));
	}
	
	@Test
	public void testCase4() throws Exception{
		//Wrong tag name in one element
		testDoc(new File("library.emf"), new File("ExpectedDocument1.xml"), new File("TestDocument4.xml"));
	}
	
	@Test
	public void testCase5() throws Exception{
		//Wrong attribute name in one element
		testDoc(new File("library.emf"), new File("ExpectedDocument1.xml"), new File("TestDocument5.xml"));
	}
	
	@Test
	public void testCase6() throws Exception{
		//Testing with elements without attributes and single letter tag names
		//where one element has a wrong tag name
		testDoc(new File("ABC.emf"), new File("ExpectedDocument4.xml"), new File("TestDocument6.xml"));
	}
	
	@Test
	public void testCase7() throws Exception{
		//Testing with elements without attributes and single letter tag names
		//where two element has a wrong tag name
		testDoc(new File("ABC.emf"), new File("ExpectedDocument4.xml"), new File("TestDocument7.xml"));
	}
	
	@Test
	public void testCase8() throws Exception{
		//Wrong tag name in multiple elements with different meta-model more complex 
		testDoc(new File("Petrinet.emf"), new File("ExpectedDocument5.xml"), new File("TestDocument8.xml"));
	}
	
	@Test
	public void testCase9() throws Exception{
		//Wrong tag name and attributes in multiple elements with different meta-model
		testDoc(new File("Petrinet.emf"), new File("ExpectedDocument5.xml"), new File("TestDocument9.xml"));
	}
	
	@Test
	public void testCase10() throws Exception{
		//Testing with elements without attributes and single letter tag names
		//where multiple elements have wrong tag names and the error is extensive
		testDoc(new File("ABC.emf"), new File("ExpectedDocument6.xml"), new File("TestDocument10.xml"));
	}
	
	@Test
	public void testCase11() throws Exception{
		//Multiple elements with tag name errors with an XML document with depth greater than 3
		testDoc(new File("library.emf"), new File("ExpectedDocument7.xml"), new File("TestDocument11.xml"));
	}
	
	@Test
	public void testCase12() throws Exception{
		//Wrong tag name in one element with different meta-model more complex 
		testDoc(new File("Petrinet.emf"), new File("ExpectedDocument5.xml"), new File("TestDocument12.xml"));
	}
	
	private void testDoc(File EmfModel,File ExpectedXML,File TestDocument) throws Exception{
		
		EPackage myModel = LoadData.getEpackage(EmfModel);
		Document doc = LoadData.getDocument(TestDocument);
		Document expectedDoc = LoadData.getDocument(ExpectedXML);
		
		Graph modelGraph = GraphTransform.convertModelToGraph(myModel);
		Graph docGraph = GraphTransform.convertDocToGraph(doc);
		
		PairwiseValidation validation =  new PairwiseValidation(docGraph, modelGraph, doc, myModel);
		Graph pairwiseGraph  = (Graph)validation.getValidationResult();
		
		FloodingCorrection correction = new FloodingCorrection(doc);
		LinkedHashMap<Vertex, Double>bestMatchings = (LinkedHashMap<Vertex, Double>)correction.getCorrectionResult(pairwiseGraph);
		
		ErrorCorrector output = new ErrorCorrector(doc, bestMatchings, modelGraph);

		XMLOutputter xmlout = new XMLOutputter(Format.getCompactFormat());
		String fixedDoc = xmlout.outputString(output.start());
		String correctDoc = xmlout.outputString(expectedDoc);
		System.out.println(fixedDoc);
		
		
		if (fixedDoc.equals(correctDoc)){			
			System.out.println("The corrected XML matches the expected XML");		
		}else{
			throw new TestFailedException();
		}
	}

}
