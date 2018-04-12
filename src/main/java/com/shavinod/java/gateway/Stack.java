package com.shavinod.java.gateway;

import java.util.LinkedList;
import java.util.List;

public class Stack {

	private List<String> internalList = new LinkedList<String>();

	public void push(String element) {
		// TODO Auto-generated method stub

		internalList.add(0, element);

	}

	public void pop() {
		// TODO Auto-generated method stub
		internalList.remove(0);
	}

	public List<String> getInternalList() {
		return internalList;
	}

	private void pushAll(List<String> elements) {
		// TODO Auto-generated method stub
		for (String element : elements) {
			this.push(element);
		}

	}

}
