package graphtools;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EClass;
import org.jdom2.Element;

public class Graph {
	private ArrayList<Vertex> Nodes;
	private ArrayList<Edge> Edges;
	
	public Graph(){
		Nodes = new ArrayList<Vertex>();
		Edges = new ArrayList<Edge>();
	}
	
	public ArrayList<Vertex> getNodes() {
		return Nodes;
	}
	
	public Vertex getNode(Vertex node) {
		Vertex result = null;
		
		for(Vertex vertex: Nodes){
			if (vertex.equals(node)){
				result = vertex;
				break;
			}
		}
		return result;	
	}
	
	public Vertex getNode(Element node) {
		Vertex result = null;
		
		for(Vertex vertex: Nodes){
			if (vertex.equals(new Vertex(node))){
				result = vertex;
				break;
			}
		}
		return result;	
	}
	
	public Vertex getNode(EClass node) {
		Vertex result = null;
		
		for(Vertex vertex: Nodes){
			if (vertex.equals(new Vertex(node))){
				result = vertex;
				break;
			}
		}
		return result;	
	}
	
	public void addNode(Vertex node) {
		this.Nodes.add(node);
	}
	public ArrayList<Edge> getEdges() {
		return Edges;
	}
	public void addEdge(Edge edge) {
		this.Edges.add(edge);
	}
	
	public ArrayList<Edge> isATargetNode(Vertex node){
		//is the specified node a target node in the graph? if so returns edges where this is true
		ArrayList<Edge> result = new ArrayList<Edge>();
		for (Edge edge: Edges){
			if(edge.getTarget()== node){
				result.add(edge);
			}
		}
		
		return result;
	}
	
	public boolean edgeExistBetween(Vertex source, Vertex target){
		boolean result = false;
		for(Edge edge: Edges){
			if(edge.getSource().getName().equals(source.getName()) && edge.getTarget().getName().equals(target.getName()) ){
				result = true;
				break;
			}
		}
		return result;
	}
	
	public boolean containsNode(Vertex node){
		return Nodes.contains(node);
	}
	
	public boolean equals(Object o){
		boolean sameedges = true;
		boolean samenodes = true;
		
		for (Edge edge : ((Graph)o).getEdges()) {
			sameedges = this.Edges.contains(edge);
			if(!this.Edges.contains(edge)){
				break;
			}
		}
		
		for (Vertex node: ((Graph)o).getNodes()) {
			samenodes = this.Nodes.contains(node);
			if(!this.Nodes.contains(node)){
				break;
			}
		}
		
		return sameedges && samenodes;
	}

}
