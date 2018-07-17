package com.shavinod.java.prototype;

public interface SearchResult {

	SearchResult clone();

	int totalEntries();

	String getPage(int page);
}