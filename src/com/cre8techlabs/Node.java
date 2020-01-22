package com.cre8techlabs;

import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

/**
 * Completed this class
 * @author lenderprice
 *
 */
public class Node implements INode {

	public char label = '\0';
	private Rectangle rect;
	private Set<INode> upNodes;
	private Set<INode> downNodes;
	private Set<INode> leftNodes;
	private Set<INode> rightNodes;
	private int mostX, mostY;

	public enum NodeType { 
		UP(0), DOWN(1), LEFT(2), RIGHT(3);
	    private int pos;
		NodeType(int pos){ this.pos = pos; }
		int val( ) {return this.pos; }
	}
	
	//only positive or zero (less than zero means is something in between -cross-)
	public static class XYClass { 
		public int x;
		public int y;
		XYClass(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
	
	Node(){
		rect = null;
		upNodes = downNodes = leftNodes = rightNodes = null;
	}
	
	public void setUpNode(NodeType nodeType, Set<INode> setNodes) {
		switch(nodeType) {
			case UP  : this.upNodes = setNodes; break; 
			case DOWN: this.downNodes = setNodes; break; 
			case LEFT: this.leftNodes = setNodes; break; 
			default  : this.rightNodes = setNodes; break; //the only missing is right
		}
	}

	/**
	 * Return the difference between 2 nodes
	 * @param a First Node
	 * @param b Second Node
	 * @return A 2 integer keypair (X, Y) means: X < 0 to left, X > 0 to right // Y < 0 to top, Y > 0 to bottom
	 */
	public XYClass DiffToNode(Node b) {
		int tmp, diffX, diffY;
		
		//Difference on X		
		if ((this.getRect().x >= b.getRect().x) && (this.getRect().x <= b.mostX)) //B contains THIS
			diffX = 0;
		else if ((b.getRect().x >= this.getRect().x) && (b.getRect().x <= this.mostX)) //THIS contains B
			diffX = 0;
		else {
			diffX = b.getRect().x - this.getRect().x;
			tmp = b.getRect().x - this.mostX;
			if (Math.abs(tmp) < Math.abs(diffX)) //less difference (right+, left-)
				diffX = tmp;
			tmp = b.mostX - this.getRect().x;
			if (Math.abs(tmp) < Math.abs(diffX)) //less difference
				diffX = tmp;
			tmp = b.mostX - this.mostX;
			if (Math.abs(tmp) < Math.abs(diffX)) //less difference
				diffX = tmp;
		}
		
		//Difference on Y		
		if ((this.getRect().y >= b.getRect().y) && (this.getRect().y <= b.mostY)) //B contains THIS
			diffY = 0;
		else if ((b.getRect().y >= this.getRect().y) && (b.getRect().y <= this.mostY)) //THIS contains B
			diffY = 0;
		else {
			diffY = b.getRect().y - this.getRect().y;
			tmp = b.getRect().y - this.mostY;
			if (Math.abs(tmp) < Math.abs(diffY)) //less difference (down+, up-)
				diffY = tmp;
			tmp = b.mostY - this.getRect().y;
			if (Math.abs(tmp) < Math.abs(diffY)) //less difference
				diffY = tmp;
			tmp = b.mostY - this.mostY;
			if (Math.abs(tmp) < Math.abs(diffY)) //less difference
				diffY = tmp;
		}
		
		return new XYClass(diffX, diffY);
	}
	
	@Override
	public Rectangle getRect() {
		if (rect == null)
			return new Rectangle();
		else
			return rect;
	}

	@Override
	public Set<INode> getUpNodes() {
		if (upNodes == null)
			return new HashSet<INode>();
		else
			return upNodes;
	}

	@Override
	public Set<INode> getDownNodes() {
		if (downNodes == null)
			return new HashSet<INode>();
		else
			return downNodes;
	}

	@Override
	public Set<INode> getLeftNodes() {
		if (leftNodes == null)
			return new HashSet<INode>();
		else
			return leftNodes;
	}

	@Override
	public Set<INode> getRightNodes() {
		if (rightNodes == null)
			return new HashSet<INode>();
		else
			return rightNodes;
	}

	@Override
	public void setRect(Rectangle rec) {
		if (rec == null) {
			this.rect = null;
			this.mostX = this.mostY = Integer.MAX_VALUE;
		}else {
			this.rect = rec;
			this.mostX = rec.x + rec.width - 1; //less 1 b/c count start on zero
			this.mostY = rec.y + rec.height - 1; //less 1 b/c count start on zero
		}
		
	}

}
