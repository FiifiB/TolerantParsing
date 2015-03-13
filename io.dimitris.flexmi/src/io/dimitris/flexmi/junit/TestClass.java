package io.dimitris.flexmi.junit;

import java.io.File;

import org.eclipse.emf.emfatic.core.generator.ecore.EcoreGenerator;

public class TestClass {
	public static void main(String[] args) {
		EcoreGenerator gen = new EcoreGenerator();
		try {
			gen.generate(new File("ABC.emf"), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
