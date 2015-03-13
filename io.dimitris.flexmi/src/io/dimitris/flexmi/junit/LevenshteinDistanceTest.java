package io.dimitris.flexmi.junit;

import static org.junit.Assert.*;
import graphtools.Edge;
import graphtools.Graph;
import graphtools.Vertex;
import io.dimitris.flexmi.FuzzyCorrection;
import io.dimitris.flexmi.LevenshteinDistance;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.jdom2.Document;
import org.jdom2.Element;
import org.junit.Test;

public class LevenshteinDistanceTest {

	@Test
	public void testBestMatch() {
		String [] data = { "kitten", "sitting", "saturday", "sunday", "rosettacode", "raisethysword" };
		LevenshteinDistance testObj = new LevenshteinDistance(data);
		
		assertEquals("sunday", testObj.bestMatch("lunday"));
	}
	
	@Test
	public void testDistance() {
//		String [] data = { "kitten", "sitting", "saturday", "sunday", "rosettacode", "raisethysword" };
//		LevenshteinDistance testObj = new LevenshteinDistance(data);
		
		assertEquals(new Integer(1), LevenshteinDistance.distance("kiten", "kitten"));
	}
	
	

}
