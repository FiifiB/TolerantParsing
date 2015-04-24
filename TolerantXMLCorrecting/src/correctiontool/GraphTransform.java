package correctiontool;

import graphtools.Edge;
import graphtools.Graph;
import graphtools.Vertex;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.jdom2.Document;
import org.jdom2.Element;

public class GraphTransform {

	public GraphTransform() {
		// TODO Auto-generated constructor stub
	}
	
	public static Graph convertDocToGraph(Document doc){
		Graph DocGraph = new Graph();
		Vertex rootNode = new Vertex(doc.getRootElement());
		DocGraph.addNode(rootNode);
		if (!doc.getRootElement().getChildren().isEmpty()){
			for (Element child: doc.getRootElement().getChildren()){
				Vertex childNode = new Vertex(child);
				Edge parentofEdge = new Edge(rootNode, childNode);
				rootNode.addConnectedEdge(parentofEdge);
				DocGraph.addNode(childNode);
				DocGraph.addEdge(parentofEdge);
				if(!child.getChildren().isEmpty()){
					linkChild(child, childNode, DocGraph);
				}
			}
		}
		for (Vertex node: DocGraph.getNodes()){
			System.out.println(node.getName());
			
		}
		return DocGraph;
	}
	
	private static void linkChild(Element childToParent, Vertex childToParentNode, Graph docGraph){
		for(Element child: childToParent.getChildren()){
			Vertex childNode = new Vertex(child);
			Edge parentofEdge = new Edge(childToParentNode, childNode);
			childToParentNode.addConnectedEdge(parentofEdge);
			docGraph.addNode(childNode);
			docGraph.addEdge(parentofEdge);
			if(!child.getChildren().isEmpty()){
				linkChild(child, childNode, docGraph);
			}
		}
	}
	
	public static Graph convertModelToGraph(EPackage myModel){
		Graph modelGraph = new Graph();		
		for(EClassifier eclassifier: myModel.getEClassifiers()){
			EClass eclass = (EClass)eclassifier;
			checkAndCreateNodes(eclass, modelGraph, myModel);
		}
		return modelGraph;
	}
	
	private static void checkAndCreateNodes(EClass eclass, Graph myGraph, EPackage myModel){
		if (eclass.isAbstract()){
			for(EClassifier eclassifier2: myModel.getEClassifiers()){
				EClass eclass2 = (EClass)eclassifier2;
				if (eclass2.isSuperTypeOf(eclass)){
					Vertex node = new Vertex(eclass2);
					if (myGraph.containsNode(node)){
						classLinkToChild(eclass, myGraph.getNode(node), myGraph);
					}else {
						myGraph.addNode(node);
						classLinkToChild(eclass, node, myGraph);				
					}						
				}
			}
		}else{
			Vertex classNode = new Vertex(eclass);
			if (myGraph.containsNode(classNode)){
				classLinkToChild(eclass, myGraph.getNode(classNode), myGraph);
			}else {
				myGraph.addNode(classNode);
				classLinkToChild(eclass, classNode, myGraph);				
			}
		}
	}
	
	private static void classLinkToChild(EClass eclass, Vertex classNode, Graph myGraph ){
		for(EReference reference: eclass.getEReferences()){
			if(reference.isContainment()){
				EClass containmentOfClass = reference.getEReferenceType();
				Vertex Child = new Vertex(containmentOfClass);				
				if(myGraph.containsNode(Child)){
					Edge containmentEdge = new Edge(classNode, myGraph.getNode(Child));
					myGraph.addEdge(containmentEdge);
					classNode.addConnectedEdge(containmentEdge);
				}else{
					Edge containmentEdge = new Edge(classNode, Child);
					myGraph.addEdge(containmentEdge);
					classNode.addConnectedEdge(containmentEdge);
					myGraph.addNode(Child);
				}
			}
		}
	}

}
