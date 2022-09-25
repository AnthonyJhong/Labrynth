package labrynth.CS146;

import java.util.LinkedList;
import java.awt.Color;
import java.util.Iterator;

public class Vertex {
	private int index;
	private String value;
	private LinkedList<Vertex> edges;
	private Color vColor;
	private int distance;
	private Vertex parent;
	private int viewNumber;
	
	public Vertex(int index) {
		this.index = index;
		value = " ";
		edges = new LinkedList<>();
		vColor = Color.WHITE;
		distance = 0;
		parent = null;
		viewNumber = -99;
	}
	
	public void addEdge(Vertex vertex) {
		edges.add(vertex);
	}
	
	public Iterator<Vertex> getEdgeIterator() {
		return edges.iterator();
	}
	
	public int getIndex() {
		return index;
	}
	
	public Color getColor() {
		return vColor;
	}
	
	public void setColor(Color c) {
		vColor = c;
	}
	
	public void setDistance(int d) {
		distance = d;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public Vertex getParent() {
		return parent;
	}
	
	public void setParent(Vertex v) {
		parent = v;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String s) {
		value = s;
	}
	
	public void setViewNumber(int num) {
		viewNumber = num;
	}
	
	public int getViewNumber() {
		return viewNumber;
	}
	
	public void reset() {
		value = " ";
		vColor = Color.WHITE;
		distance = 0;
		parent = null;
		viewNumber = -99;
	}
}
