package io.dimitris.flexmi.junit;

import static org.junit.Assert.assertEquals;
import graphtools.Edge;
import graphtools.Graph;
import graphtools.Vertex;
import io.dimitris.flexmi.FuzzyCorrection;

import java.util.ArrayList;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.junit.Test;

public class FuzzyCorrectionTest {
	
	/**
	 * Test that the correct xml graph is created
	 */
	@Test
	public void testConvertDocToGraph() throws Exception {
		ResourceSet metamodelResourceSet = new ResourceSetImpl();
		metamodelResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());
		Resource metamodelResource = metamodelResourceSet.createResource(URI.createFileURI("ABC.ecore"));
		metamodelResource.load(null);
		
		EPackage ePackage = (EPackage) metamodelResource.getContents().get(0);
		
		Element root = new Element("A");
		Element child1 = new Element("B");
		Element child1_1 = new Element("D");
		child1.addContent(child1_1);
		root.addContent(child1);
		Document doc = new Document(root);
		
		FuzzyCorrection testObj =  new FuzzyCorrection(doc, ePackage);
		
		Vertex A = new Vertex("A", new ArrayList<Attribute>());
		Vertex B = new Vertex("B", new ArrayList<Attribute>());
		Vertex D = new Vertex("D", new ArrayList<Attribute>());
		Edge A_B = new Edge(A, B);
		Edge B_D = new Edge(B, D);
		A.addConnectedEdge(A_B);
		B.addConnectedEdge(B_D);
		Graph expectedGraph = new Graph();
		expectedGraph.addNode(A);
		expectedGraph.addNode(B);
		expectedGraph.addNode(D);
		expectedGraph.addEdge(A_B);
		expectedGraph.addEdge(B_D);
		
		assertEquals(expectedGraph.getNode(A), testObj.convertDocToGraph().getNode(A));
		assertEquals(expectedGraph.getNode(B), testObj.convertDocToGraph().getNode(B));
		assertEquals(expectedGraph.getNode(D), testObj.convertDocToGraph().getNode(D));
		assertEquals(expectedGraph.edgeExistBetween(A, B), testObj.convertDocToGraph().edgeExistBetween(A, B));
		assertEquals(expectedGraph.edgeExistBetween(B, D), testObj.convertDocToGraph().edgeExistBetween(B, D));
	}
	
	/**
	 * Test that the correct model graph is created
	 */
	@Test
	public void testConvertModelToGraph() throws Exception {
		ResourceSet metamodelResourceSet = new ResourceSetImpl();
		metamodelResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());
		Resource metamodelResource = metamodelResourceSet.createResource(URI.createFileURI("ABC.ecore"));
		metamodelResource.load(null);
		
		EPackage ePackage = (EPackage) metamodelResource.getContents().get(0);
		
		Element root = new Element("A");
		Element child1 = new Element("B");
		Element child1_1 = new Element("C");
		child1.addContent(child1_1);
		root.addContent(child1);
		Document doc = new Document(root);
		
		FuzzyCorrection testObj =  new FuzzyCorrection(doc, ePackage);
		
		Vertex A = new Vertex("A", new ArrayList<Attribute>());
		Vertex B = new Vertex("B", new ArrayList<Attribute>());
		Vertex C = new Vertex("C", new ArrayList<Attribute>());
		Vertex D = new Vertex("D", new ArrayList<Attribute>());
		Edge A_B = new Edge(A, B);
		Edge A_C = new Edge(A, C);
		Edge B_D = new Edge(B, D);
		A.addConnectedEdge(A_B);
		A.addConnectedEdge(A_C);
		B.addConnectedEdge(B_D);
		Graph expectedGraph = new Graph();
		expectedGraph.addNode(A);
		expectedGraph.addNode(B);
		expectedGraph.addNode(C);
		expectedGraph.addNode(D);
		expectedGraph.addEdge(A_B);
		expectedGraph.addEdge(A_C);
		expectedGraph.addEdge(B_D);
		
		assertEquals(expectedGraph.getNode(A), testObj.convertModelToGraph(ePackage).getNode(A));
		assertEquals(expectedGraph.getNode(B), testObj.convertModelToGraph(ePackage).getNode(B));
		assertEquals(expectedGraph.getNode(C), testObj.convertModelToGraph(ePackage).getNode(C));
		assertEquals(expectedGraph.getNode(D), testObj.convertModelToGraph(ePackage).getNode(D));
		assertEquals(expectedGraph.edgeExistBetween(A, B), testObj.convertModelToGraph(ePackage).edgeExistBetween(A, B));
		assertEquals(expectedGraph.edgeExistBetween(A, C), testObj.convertModelToGraph(ePackage).edgeExistBetween(A, C));
		assertEquals(expectedGraph.edgeExistBetween(B, D), testObj.convertModelToGraph(ePackage).edgeExistBetween(B, D));
		
	}
	
	/**
	 * Test that the correct paired graph is created
	 */
	@Test
	public void testMatchGraphs() throws Exception {
		ResourceSet metamodelResourceSet = new ResourceSetImpl();
		metamodelResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());
		Resource metamodelResource = metamodelResourceSet.createResource(URI.createFileURI("ABC.ecore"));
		metamodelResource.load(null);
		
		EPackage ePackage = (EPackage) metamodelResource.getContents().get(0);
		
		Element root = new Element("A");
		Element child1 = new Element("B");
		Element child1_1 = new Element("C");
		child1.addContent(child1_1);
		root.addContent(child1);
		Document doc = new Document(root);
		
		FuzzyCorrection testObj =  new FuzzyCorrection(doc, ePackage);
		
		Vertex A_A = new Vertex("A_A", new ArrayList<Attribute>());
		Vertex A_B = new Vertex("A_B", new ArrayList<Attribute>());
//		Vertex A_C = new Vertex("A_C", new ArrayList<Attribute>());
//		Vertex A_D = new Vertex("A_D", new ArrayList<Attribute>());
		
		
//		Vertex B_A = new Vertex("B_A", new ArrayList<Attribute>());
		Vertex B_B = new Vertex("B_B", new ArrayList<Attribute>());
//		Vertex B_C = new Vertex("B_C", new ArrayList<Attribute>());
//		Vertex B_D = new Vertex("B_D", new ArrayList<Attribute>());
		
		
//		Vertex C_A = new Vertex("C_A", new ArrayList<Attribute>());
//		Vertex C_B = new Vertex("C_B", new ArrayList<Attribute>());
		Vertex C_C = new Vertex("C_C", new ArrayList<Attribute>());
		Vertex C_D = new Vertex("C_D", new ArrayList<Attribute>());
		
//		Vertex D_A = new Vertex("D_A", new ArrayList<Attribute>());
//		Vertex D_B = new Vertex("D_B", new ArrayList<Attribute>());
//		Vertex D_C = new Vertex("D_C", new ArrayList<Attribute>());
//		Vertex D_D = new Vertex("D_D", new ArrayList<Attribute>());
		
		Edge A_AtoB_B = new Edge(A_A, B_B);
		Edge B_BtoC_D = new Edge(B_B, C_D);
		A_A.addConnectedEdge(A_AtoB_B);
		B_B.addConnectedEdge(B_BtoC_D);
		
		Graph expectedGraph = new Graph();
		expectedGraph.addNode(A_A);
		expectedGraph.addNode(A_B);
		expectedGraph.addNode(C_C);
		expectedGraph.addNode(B_B);
		expectedGraph.addNode(C_D);
		expectedGraph.addEdge(A_AtoB_B);
		expectedGraph.addEdge(B_BtoC_D);
		
		assertEquals(expectedGraph.getNode(A_A), testObj.matchGraphs(testObj.convertDocToGraph(), testObj.convertModelToGraph(ePackage)).getNode(A_A));
		assertEquals(expectedGraph.getNode(A_B), testObj.matchGraphs(testObj.convertDocToGraph(), testObj.convertModelToGraph(ePackage)).getNode(A_B));
		assertEquals(expectedGraph.getNode(B_B), testObj.matchGraphs(testObj.convertDocToGraph(), testObj.convertModelToGraph(ePackage)).getNode(B_B));
		assertEquals(expectedGraph.getNode(C_C), testObj.matchGraphs(testObj.convertDocToGraph(), testObj.convertModelToGraph(ePackage)).getNode(C_C));
		assertEquals(expectedGraph.getNode(C_D), testObj.matchGraphs(testObj.convertDocToGraph(), testObj.convertModelToGraph(ePackage)).getNode(C_D));

		assertEquals(expectedGraph.edgeExistBetween(A_A, B_B), testObj.matchGraphs(testObj.convertDocToGraph(), testObj.convertModelToGraph(ePackage)).edgeExistBetween(A_A, B_B));
		assertEquals(expectedGraph.edgeExistBetween(B_B, C_D), testObj.matchGraphs(testObj.convertDocToGraph(), testObj.convertModelToGraph(ePackage)).edgeExistBetween(B_B, C_D));
		
	}

}
