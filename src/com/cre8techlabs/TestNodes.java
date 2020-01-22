package com.cre8techlabs;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.cre8techlabs.Node.NodeType;

public class TestNodes {
	/*
AAA   BBB
     CC DDD
  EEEE FF GGG
  EEEE    GGG
 HHHHHHHHHH
	 */
	public static void main(String[] args) {
		Node a = createNode(0, 0, 3, 1, 'a');
		Node b = createNode(6, 0, 3, 1, 'b');
		Node c = createNode(5, 1, 2, 1, 'c');
		Node d = createNode(8, 1, 3, 1, 'd');
		Node e = createNode(2, 2, 4, 2, 'e');
		Node f = createNode(7, 2, 2, 1, 'f');
		Node g = createNode(10, 2, 3, 2, 'g');
		Node h = createNode(1, 4, 10, 1, 'h');
		
		linkNodes(a, b, c, d, e, f, g, h);

		assertNodeLinked(a, new Node[] {}, new Node[] {e}, new Node[] {}, new Node[] {b});
		assertNodeLinked(b, new Node[] {}, new Node[] {c, d}, new Node[] {a}, new Node[] {});
		
		assertNodeLinked(c, new Node[] {b}, new Node[] {e}, new Node[] {}, new Node[] {d});
//		assertNodeLinked(c, new Node[] {b}, new Node[] {}, new Node[] {}, new Node[] {d}); //original: missing down E

		assertNodeLinked(d, new Node[] {b}, new Node[] {f, g}, new Node[] {c}, new Node[] {});
//		assertNodeLinked(d, new Node[] {b}, new Node[] {f, g}, new Node[] {}, new Node[] {});//original: missing left C

		assertNodeLinked(e, new Node[] {c}, new Node[] {h}, new Node[] {}, new Node[] {f});
		assertNodeLinked(f, new Node[] {d}, new Node[] {h}, new Node[] {e}, new Node[] {g});
		
		assertNodeLinked(g, new Node[] {d}, new Node[] {h}, new Node[] {f}, new Node[] {});
//		assertNodeLinked(g, new Node[] {d}, new Node[] {}, new Node[] {f}, new Node[] {});//original: missing down H

		assertNodeLinked(h, new Node[] {e, g}, new Node[] {}, new Node[] {}, new Node[] {});
		
		displayRecursivelyAllNodesFromParentNode(a);
	}
	private static void linkNodes(Node...nodes) {
		Node.XYClass tmpKeyPairXY; //Review in the middle
		Set<INode> listNodesUp, listNodesDn, listNodesLf, listNodesRg;
		int[] listPts;
		
		for(int i =0; i < nodes.length; i++) {//looking all nodes
			//if (nodes[i].label == 'b') {//**** ONLY FOR TEST ****
				//init values for each node
				listNodesUp = new HashSet<INode>();
				listNodesDn = new HashSet<INode>();
				listNodesLf = new HashSet<INode>();
				listNodesRg = new HashSet<INode>();
				listPts = new int[] {Integer.MIN_VALUE, Integer.MAX_VALUE,  // UP,   DOWN, 
		                   Integer.MIN_VALUE, Integer.MAX_VALUE}; // LEFT, RIGHT				
				
				for(Node loopN : nodes) {//review all nodes
					if (loopN != nodes[i]) {//except the same node
						tmpKeyPairXY = nodes[i].DiffToNode(loopN);
						
						//UP NODES Y < 0
						if ((tmpKeyPairXY.y < 0) && (tmpKeyPairXY.x == 0)) {//x=0 below any point
							//review if less distance
							if (tmpKeyPairXY.y > listPts[NodeType.UP.val()]) {
								listPts[NodeType.UP.val()] = tmpKeyPairXY.y;
								listNodesUp.clear();
							}
							if (tmpKeyPairXY.y >= listPts[NodeType.UP.val()])//only less posit
								listNodesUp.add(loopN);
						}

						//DOWN NODES Y > 0
						if ((tmpKeyPairXY.y > 0) && (tmpKeyPairXY.x == 0)) {//x=0 below any point
							//review if less distance
							if (tmpKeyPairXY.y < listPts[NodeType.DOWN.val()]) {
								listPts[NodeType.DOWN.val()] = tmpKeyPairXY.y;
								listNodesDn.clear();
							}
							if (tmpKeyPairXY.y <= listPts[NodeType.DOWN.val()])//only less posit
								listNodesDn.add(loopN);
						}

						//RIGHT NODES X > 0
						if ((tmpKeyPairXY.x > 0) && (tmpKeyPairXY.y == 0)) {//y=0 below any point
							//review if less distance
							if (tmpKeyPairXY.x < listPts[NodeType.RIGHT.val()]) {
								listPts[NodeType.RIGHT.val()] = tmpKeyPairXY.x;
								listNodesRg.clear();
							}
							if (tmpKeyPairXY.x <= listPts[NodeType.RIGHT.val()])//only less posit
								listNodesRg.add(loopN);
						}

						//LEFT NODES X < 0
						if ((tmpKeyPairXY.x < 0) && (tmpKeyPairXY.y == 0)) {//y=0 below any point
							//review if less distance
							if (tmpKeyPairXY.x > listPts[NodeType.LEFT.val()]) {
								listPts[NodeType.LEFT.val()] = tmpKeyPairXY.x;
								listNodesLf.clear();
							}
							if (tmpKeyPairXY.x >= listPts[NodeType.LEFT.val()])//only less negat
								listNodesLf.add(loopN);
						}
					}//if the same one
				}//for thisN
				nodes[i].setUpNode(NodeType.UP, listNodesUp);
				nodes[i].setUpNode(NodeType.DOWN, listNodesDn);
				nodes[i].setUpNode(NodeType.RIGHT, listNodesRg);
				nodes[i].setUpNode(NodeType.LEFT, listNodesLf);
				//break;
			//}//if test node
		}//for main
		
	}

	private static void displayRecursivelyAllNodesFromParentNode(Node a) {
		Set<INode> readNodes = new HashSet<INode>();;
		int level = 0;
		displayRecursive(a, readNodes, level);
	}
	
	private static void displayRecursive(INode a, Set<INode> readNodes, int level) {
		if (!readNodes.contains(a)) {
			String replic = new String(new char[level*4]).replace("\0", " ");
			System.out.format("%s%s [%d, %d, %d, %d]%n", 
					replic, ((Node)a).label, a.getRect().x, a.getRect().y, a.getRect().width, a.getRect().height);
			readNodes.add(a);
			
			//Display DOWN Nodes
			for(INode childNode: a.getDownNodes())
				displayRecursive(childNode, readNodes, level + 1);
			
			//Display RIGHT Nodes
			for(INode childNode: a.getRightNodes()) 
				displayRecursive(childNode, readNodes, level + 1);

			//Display UP Nodes
//			for(INode childNode: a.getUpNodes())
//				displayRecursive(childNode, readNodes, level + 1);

			//Display LEFT Nodes
//			for(INode childNode: a.getLeftNodes())
//				displayRecursive(childNode, readNodes, level + 1);			
		}
	}
	private static void assertNodeLinked(Node node, Node[] up, Node[] down, Node[] left, Node[] right) {
		if (!containsAll(node.getUpNodes(), Arrays.asList(up))) {
			throw new AssertionError("The conditions are not meet");
		}
		if (!containsAll(node.getDownNodes(), Arrays.asList(down))) {
			throw new AssertionError("The conditions are not meet");
		}
		if (!containsAll(node.getLeftNodes(), Arrays.asList(left))) {
			throw new AssertionError("The conditions are not meet");
		}
		if (!containsAll(node.getRightNodes(), Arrays.asList(right))) {
			throw new AssertionError("The conditions are not meet");
		}
	}

	private static boolean containsAll(Collection<INode> nodes, Collection<INode> nodes2) {
		if (nodes.size() != nodes2.size())
			return false;
		if (nodes.isEmpty() && nodes2.isEmpty())
			return true;
		if (nodes.containsAll(nodes2) && nodes2.containsAll(nodes)) {
			return true;
		}
		return false;
	}

	private static Node createNode(int x, int y, int width, int height, char label) {
		Node result = new Node();
		result.setRect(new Rectangle(x, y, width, height));
		result.label = label;
		return result;
	}
}
