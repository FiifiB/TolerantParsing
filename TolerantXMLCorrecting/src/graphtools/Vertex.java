package graphtools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.jdom2.Attribute;
import org.jdom2.Element;

public class Vertex implements Comparable<Vertex> {
	private int ID;
	private String Name;
	private ArrayList<Attribute> Attributes;
	private String Content;
	private Element vertexElement;
	private EClass vertexEClass;
	private Double DistanceScore;
	private ArrayList<Edge> connectedEdges; // these are all outgoing edges
	
	public Vertex(String name, ArrayList<Attribute> Attributes, String Content){
		this.Name = name;
		this.Attributes = Attributes;
		this.Content = Content;
		connectedEdges = new ArrayList<Edge>();
		this.vertexElement = null;
		this.vertexEClass = null;
	}
	
	public Vertex(Element element){
		this.ID = new Random().nextInt(10000000);
		this.Name = element.getName();
		this.Attributes = convertToArrayList(element.getAttributes());
		connectedEdges = new ArrayList<Edge>();
		this.vertexElement = element;
		this.Content = element.getText();
	}
	
	public Vertex(EClass eclass){
		this.Name = eclass.getName();
		this.Attributes = convertToAttributes(eclass);
		connectedEdges = new ArrayList<Edge>();
		this.vertexEClass = eclass;
	}
	
	public Vertex(Element element,EClass eclass){
		this.vertexElement = element;
		this.vertexEClass = eclass;
		this.Name = element.getName() + "_" + eclass.getName();
		connectedEdges = new ArrayList<Edge>();
		this.Attributes = null;
	}
	
	private ArrayList<Attribute> convertToAttributes(EClass eclassAtts){
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		for(EAttribute eatts: eclassAtts.getEAttributes() ){
			Attribute att = new Attribute(eatts.getName(), "");
			attributes.add(att);
		}
		return attributes;
	}
	
	public boolean equals(Object o){
		if(((Vertex)o).getAttributes() != null && this.Attributes != null){
			return (((Vertex)o).getName().equals(this.Name) &&
					((Vertex)o).getAttributes().size() == this.Attributes.size());
		}
		return (((Vertex)o).getName().equals(this.Name));
	}
	
	public int hashCode(){
		return this.Name.length();
	}
	
	@Override
	public int compareTo(Vertex comparableDistance) {
		int compareQuantity = this.DistanceScore.compareTo(comparableDistance.DistanceScore);
		return compareQuantity;
	}

	public Double getDistanceScore() {
		return DistanceScore;
	}

	public void setDistanceScore(double distanceScore) {
		DistanceScore = distanceScore;
	}

	public String getName() {
		return Name;
	}

	public ArrayList<Attribute> getAttributes() {
		return Attributes;
	}
	
	public String getContent(){
		return Content;
	}

	public Element getVertexElement() {
		return vertexElement;
	}

	public EClass getVertexEclass() {
		return vertexEClass;
	}
	
	public ArrayList<Vertex> getNeighbours(){
		ArrayList<Vertex> Neighbours = new ArrayList<Vertex>();
		for(Edge edges: connectedEdges ){
			Neighbours.add(edges.getTarget());
		}
		
		return Neighbours;
	}
	
	public void addConnectedEdge(Edge edge){
		connectedEdges.add(edge);
	}
	
	public ArrayList<Edge> getConnectedEdges(){
		return connectedEdges;
	}
	
	public Boolean hasConnectedEdges(){
		return (!connectedEdges.isEmpty());
	}
	
	public Double getConnectedEdgeValue(){
		return connectedEdges.get(0).getValue();
	}
	
	public boolean pairContainsNode(Element node){
		if (this.vertexElement.getName() == node.getName()){
			return true;
		}else{
			return false;
		}
	}
	
	private ArrayList<Attribute> convertToArrayList (List<Attribute>attributes){
		ArrayList<Attribute> result = new ArrayList<Attribute>();
		for(Attribute atts : attributes){
			result.add(atts);
		}
		return result;
	}
}
