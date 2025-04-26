package org.asb.util;

import java.util.Comparator;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

public class Indexer {
	
	private NavigableMap<Object, Integer> map = null;
	private static Indexer indexer;
	private int index = 0;
	
	public static Indexer getIndexer() {
		if(indexer == null) indexer = new Indexer();
		return indexer;
	}
	
	
	private Indexer() {
		map = new TreeMap<Object, Integer>(new Comparator<Object>() {
			@Override
			public int compare(final Object arg0, final Object arg1) {
				return arg0 == arg1 ? 0 : arg0.hashCode() - arg1.hashCode();
			}
		});
		index = 1;
	}
	
	public synchronized Integer putAndGet(Object key) {
		if(map.containsKey(key)) return map.get(key);
		map.put(key, index++);
		return map.get(key);
	}
	
	public void dispose() {
		map = null;
		indexer = null;
		index = 0;
	}

}