package com.mhova.domain;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Board {
	public final Queue<Card> library1 = new LinkedList<>();
	public final Queue<Card> library2 = new LinkedList<>();

	public final LinkedHashMap<String, Card> graveyard1 = new LinkedHashMap<>();
	public final LinkedHashMap<String, Card> graveyard2 = new LinkedHashMap<>();

	public final LinkedHashMap<String, Card> battlefield1 = new LinkedHashMap<>();
	public final LinkedHashMap<String, Card> battlefield2 = new LinkedHashMap<>();

	public final LinkedHashMap<String, Card> exile1 = new LinkedHashMap<>();
	public final LinkedHashMap<String, Card> exile2 = new LinkedHashMap<>();
	
	public final LinkedHashMap<String, Card> hand1 = new LinkedHashMap<>();
	public final LinkedHashMap<String, Card> hand2 = new LinkedHashMap<>();
}
