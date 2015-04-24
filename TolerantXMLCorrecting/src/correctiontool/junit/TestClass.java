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
		
		//Wrong tag names and wrong attribute names in two elements
		testDoc(new File("library.emf"), new File("ExpectedDocument1.xml"), new File("TestDocument2.xml"));
	}
	
	
	
	@Test
	public void testCase3(){
		//Multiple elements with the same name
	}
	
	@Test
	public void testCase4(){}
	
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
