package com.voyagegames.logic3d;

import java.util.Arrays;
import java.util.List;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;

public class Users {
	
	public static final String TABLE_NAME = "Users";
	
	private static final String NAME_FIELD = "name";
	private static final String PASS_FIELD = "pass";
	
	@SuppressWarnings("deprecation")
	public static boolean isUserRegistered(final String name) {
		final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		final Query query = new Query(TABLE_NAME)
			.addFilter(NAME_FIELD, Query.FilterOperator.EQUAL, name);
	    
		return (datastore.prepare(query).asList(FetchOptions.Builder.withLimit(1)).size() > 0);
	}
	
	public static void registerUser(final String name, final byte[] pass) {
		final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		final Entity entity = new Entity(TABLE_NAME);
		
		entity.setProperty(NAME_FIELD, name);
		entity.setProperty(PASS_FIELD, new Blob(pass));
		datastore.put(entity);
	}
	
	@SuppressWarnings("deprecation")
	public static boolean loginUser(final String name, final byte[] pass) {
		final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		final Query query = new Query(TABLE_NAME)
			.addFilter(NAME_FIELD, Query.FilterOperator.EQUAL, name);
		final List<Entity> results = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(1));
		
		if (results.size() == 0) {
			return false;
		}
		
		final Blob stored = (Blob)results.get(0).getProperty(PASS_FIELD);
		return Arrays.equals(stored.getBytes(), pass);
	}

}
