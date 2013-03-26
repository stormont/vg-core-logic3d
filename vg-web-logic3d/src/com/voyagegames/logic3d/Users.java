package com.voyagegames.logic3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class Users {
	
	public static final String TABLE_NAME = "Users";
	public static final String DATE_FIELD = "date";
	public static final String NAME_FIELD = "name";
	public static final String FRIENDS_FIELD = "friends";
	
	private static final String PASS_FIELD = "pass";
	
	public static Entity getUser(final String name) {
		final Key key = KeyFactory.createKey(TABLE_NAME, TABLE_NAME);
		final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		final Filter filter = new FilterPredicate(NAME_FIELD, FilterOperator.EQUAL, name);
		final Query query = new Query(TABLE_NAME, key).setFilter(filter);
	    
		final List<Entity> results = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(1));
		
		if (results.size() == 0) {
			return null;
		}
		
		return results.get(0);
	}
	
	public static void registerUser(final String name, final byte[] pass) {
		final Key key = KeyFactory.createKey(TABLE_NAME, TABLE_NAME);
		final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		final Entity entity = new Entity(TABLE_NAME, key);

		entity.setProperty(DATE_FIELD, new Date());
		entity.setProperty(NAME_FIELD, name);
		entity.setProperty(PASS_FIELD, new Blob(pass));
		entity.setProperty(FRIENDS_FIELD, new ArrayList<String>());
		datastore.put(entity);
	}
	
	public static boolean loginUser(final String name, final byte[] pass) {
		final Key key = KeyFactory.createKey(TABLE_NAME, TABLE_NAME);
		final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		final Filter filter = new FilterPredicate(NAME_FIELD, FilterOperator.EQUAL, name);
		final Query query = new Query(TABLE_NAME, key).setFilter(filter);
		final List<Entity> results = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(1));
		
		if (results.size() == 0) {
			return false;
		}
		
		final Blob stored = (Blob)results.get(0).getProperty(PASS_FIELD);
		return Arrays.equals(stored.getBytes(), pass);
	}

	@SuppressWarnings({ "unchecked" })
	public static boolean addFriend(final Entity user, final Entity friend) {
		final String friendName = (String)friend.getProperty(NAME_FIELD);
		
		List<String> friends = (List<String>)user.getProperty(FRIENDS_FIELD);
		
		if (friends == null) {
			friends = new ArrayList<String>();
		}
		
		for (final String f : friends) {
			if (f.equals(friendName)) {
				return false;
			}
		}

		final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		friends.add(friendName);
		user.setProperty(FRIENDS_FIELD, friends);
		datastore.put(user);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public static String buildJsonFriends(final Entity user) {
		final List<String> friends = (List<String>)user.getProperty(FRIENDS_FIELD);
		final int numFriends = friends.size();
		final StringBuilder sb = new StringBuilder();
		
		sb.append("{\"friends\":[");
		
		for (int i = 0; i < numFriends; ++i) {
			sb.append("\"" + friends.get(i) + "\"");
			
			if (i < (numFriends - 1)) {
				sb.append(",");
			}
		}
		
		sb.append("]}");
		return sb.toString();
	}

}
