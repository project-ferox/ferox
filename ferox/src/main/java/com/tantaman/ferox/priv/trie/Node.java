package com.tantaman.ferox.priv.trie;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.tantaman.ferox.api.router.IRouteSegment;
import com.tantaman.ferox.util.ArrayIterator;

/**
 * @author tantaman
 *
 */
public class Node {
	private Node wildNode;
	private final Map<IRouteSegment, Node> nodes;
	private final Map<IRouteSegment, Node> regs;
	
	private IRouteSegment segment;
	private IRouteSegment catchall;
	
	public Node() {
		nodes = new HashMap<>();
		regs = new LinkedHashMap<>();
	}
	
	private String getNext(ArrayIterator<String> iter) {
		if (iter.hasNext()) return iter.next();
		return null;
	}
	
	public IRouteSegment match(String piece, ArrayIterator<String> pieces) {
		if (piece == null) {
			if (segment != null)
				return segment;
			return catchall;
		}
		
		String next = getNext(pieces);
		
		Node n = nodes.get(new StringToStandardRouteSegmentLookup(piece));
		if (n != null) {
			IRouteSegment match = n.match(next, pieces.clone());
			if (match != null)
				return match;
		}
		
		// Not using our indexing atm
		// TODO: keep a hit count in the regex's so we can move hot
		// regs to the front.  or actually implement the indexing.
		// TODO: technically someone could be adding equivalent regexes
		// so the same regex could transition to multiple nodes...
		// and regexes can span multiple pieces...  which we don't handle at the moment either.
		for (Map.Entry<IRouteSegment, Node> re : regs.entrySet()) {
			if (re.getKey().matches(piece)) {
				IRouteSegment match = re.getValue().match(next, pieces.clone());
				if (match != null)
					return match;
			}
		}
		
		// Wild matches any single piece, so transition to it if
		// nothing else matched
		if (wildNode != null) {
			IRouteSegment match = wildNode.match(next, pieces.clone());
			if (match != null)
				return match;
		}
		
		// Catchall really matches everything
		// so return that if nothing else ever matched.
		return catchall;
	}
	
	public boolean isLeaf() {
		return wildNode == null && nodes.isEmpty() && regs.isEmpty();
	}
	
	public Node addSegment(IRouteSegment segment) {
		switch (segment.type()) {
		case ROOT:
			return this;
		case WILD:
			if (wildNode == null) {
				wildNode = new Node();
			}
			
			attachIfLeaf(wildNode, segment);
			
			return wildNode;
		case STANDARD:
			Node node = nodes.get(segment);
			if (node == null) {
				node = new Node();
				nodes.put(segment, node);
			}
			
			attachIfLeaf(node, segment);
			
			return node;
		case REGEX:
			Node regNode = regs.get(segment);
			if (regNode == null) {
				regNode = new Node();
				regs.put(segment, regNode);
			}
			
			attachIfLeaf(regNode, segment);
			
			return regNode;
		case CATCHALL:
			if (!segment.isLeaf())
				throw new IllegalStateException("Catchall can only be the last element in a route");
			
			if (catchall != null)
				throw new IllegalStateException("Ambiguous path");
			catchall = segment;
			return this;
		}
		
		throw new IllegalStateException("Added an unknown segment type");
	}
	
	private void attachIfLeaf(Node node, IRouteSegment segment) {
		if (segment.isLeaf()) {
			if (node.segment == null)
				node.segment = segment;
			else
				throw new IllegalStateException("An ambiguous path has been added");
		}
	}
}
