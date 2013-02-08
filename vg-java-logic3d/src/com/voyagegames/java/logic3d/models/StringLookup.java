package com.voyagegames.java.logic3d.models;

import java.util.Map;

public class StringLookup {

	public final String language;
	
	private final Map<String, String> map;
	
	public StringLookup(final String language, final Map<String, String> map) {
		this.language = language;
		this.map = map;
		
		final ActionResult[] values = ActionResult.values();
		
		for (final ActionResult sd : values) {
			if (!map.containsKey(sd.toString())) {
				throw new IllegalArgumentException("String map does not contain key " + sd.toString());
			}
		}
	}
	
	public String lookup(final ActionResult definition) {
		return map.get(definition.toString());
	}

}
