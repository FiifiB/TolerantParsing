package graphtools;

public class Edge {
	private String Label;
	private Double Value = null;
	private String Type;
	private Vertex Source;
	private Vertex Target;
	
	public Edge(Vertex source, Vertex target){
		this.Source = source;
		this.Target = target;
	}

	public String getLabel() {
		return Label;
	}

	public void setLabel(String label) {
		Label = label;
	}

	public Double getValue() {
		return Value;
	}

	public void setValue(double value) {
		Value = value;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public Vertex getSource() {
		return Source;
	}

	public void setSource(Vertex source) {
		Source = source;
	}

	public Vertex getTarget() {
		return Target;
	}

	public void setTarget(Vertex target) {
		Target = target;
	}
	
	public boolean equals(Object o){
		return (((Edge)o).getSource().equals(this.Source)) && (((Edge)o).getTarget().equals(this.Target));
	}

}
