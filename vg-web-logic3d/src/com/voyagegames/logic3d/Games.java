package com.voyagegames.logic3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.voyagegames.logic3d.models.GameConfig;
import com.voyagegames.logic3d.models.PieceIndex;

public class Games {

	public static final String TABLE_NAME = "Games";
	public static final String DATE_FIELD = "date";
	public static final String LAST_PLAY_FIELD = "lastplay";
	public static final String PLAYER_1_FIELD = "player1";
	public static final String PLAYER_2_FIELD = "player2";
	public static final String COMPLETED_FIELD = "completed";
	public static final String CONFIG_FIELD = "config";
	public static final String PIECES_FIELD = "pieces";
	public static final String TURN_FIELD = "turn";
	
	public static List<Entity> getGames(final String player) {
		final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		final Filter filter = new CompositeFilter(CompositeFilterOperator.OR, Arrays.<Filter>asList(
		         new FilterPredicate(PLAYER_1_FIELD, FilterOperator.EQUAL, player),
		         new FilterPredicate(PLAYER_2_FIELD, FilterOperator.EQUAL, player)));
		final Query query = new Query(TABLE_NAME).setFilter(filter);
	    
		return datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
	}
	
	public static List<Entity> getGamesInProgress(final String player) {
		final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		final Filter filter = new CompositeFilter(CompositeFilterOperator.AND, Arrays.<Filter>asList(
				new FilterPredicate(COMPLETED_FIELD, FilterOperator.EQUAL, false),
				new CompositeFilter(CompositeFilterOperator.OR, Arrays.<Filter>asList(
					new FilterPredicate(PLAYER_1_FIELD, FilterOperator.EQUAL, player),
					new FilterPredicate(PLAYER_2_FIELD, FilterOperator.EQUAL, player)))));
		final Query query = new Query(TABLE_NAME).setFilter(filter);
	    
		return datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
	}
	
	public static List<Entity> getGamesInProgress(final String player, final String opponent) {
		final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		final Filter filter = new CompositeFilter(CompositeFilterOperator.AND, Arrays.<Filter>asList(
				new FilterPredicate(COMPLETED_FIELD, FilterOperator.EQUAL, false),
				new CompositeFilter(CompositeFilterOperator.OR, Arrays.<Filter>asList(
					new CompositeFilter(CompositeFilterOperator.AND, Arrays.<Filter>asList(
							new FilterPredicate(PLAYER_1_FIELD, FilterOperator.EQUAL, player),
							new FilterPredicate(PLAYER_2_FIELD, FilterOperator.EQUAL, opponent))),
					new CompositeFilter(CompositeFilterOperator.AND, Arrays.<Filter>asList(
							new FilterPredicate(PLAYER_1_FIELD, FilterOperator.EQUAL, opponent),
							new FilterPredicate(PLAYER_2_FIELD, FilterOperator.EQUAL, player)))))));
		final Query query = new Query(TABLE_NAME).setFilter(filter);
	    
		return datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
	}
	
	public static List<Entity> getCompletedGames(final String player) {
		final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		final Filter filter = new CompositeFilter(CompositeFilterOperator.AND, Arrays.<Filter>asList(
				new CompositeFilter(CompositeFilterOperator.OR, Arrays.<Filter>asList(
					new FilterPredicate(PLAYER_1_FIELD, FilterOperator.EQUAL, player),
					new FilterPredicate(PLAYER_2_FIELD, FilterOperator.EQUAL, player))),
				new FilterPredicate(COMPLETED_FIELD, FilterOperator.EQUAL, true)));
		final Query query = new Query(TABLE_NAME).setFilter(filter);
	    
		return datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
	}
	
	public static Entity startGame(
			final String player1,
			final String player2,
			final GameConfig config,
			final String playerTurn) {
		if (player1.equals(player2)) {
			return null;
		}
		
		final List<Entity> gamesInProgress = getGamesInProgress(player1, player2);
		
		if (gamesInProgress.size() > 0) {
			return null;
		}

		final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		final Entity entity = new Entity(TABLE_NAME);
		final Date now = new Date();

		entity.setProperty(DATE_FIELD, now);
		entity.setProperty(LAST_PLAY_FIELD, now);
		entity.setProperty(PLAYER_1_FIELD, player1);
		entity.setProperty(PLAYER_2_FIELD, player2);
		entity.setProperty(COMPLETED_FIELD, false);
		entity.setProperty(CONFIG_FIELD, config);
		entity.setProperty(PIECES_FIELD, new ArrayList<PieceIndex>());
		entity.setProperty(TURN_FIELD, playerTurn);
		datastore.put(entity);
		return entity;
	}
	
	public static void completeGame(final Entity game) {
		final boolean completed = (Boolean)game.getProperty(COMPLETED_FIELD);
		
		if (completed) {
			return;
		}
		
		final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		game.setProperty(COMPLETED_FIELD, true);
		datastore.put(game);
	}
	
	public static void setPieces(final Entity game, final List<PieceIndex> pieces) {
		final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		game.setProperty(PIECES_FIELD, pieces);
		game.setProperty(LAST_PLAY_FIELD, new Date());
		datastore.put(game);
	}
	
	public static String getPlayer1(final Entity game) {
		return (String)game.getProperty(PLAYER_1_FIELD);
	}
	
	public static String getPlayer2(final Entity game) {
		return (String)game.getProperty(PLAYER_2_FIELD);
	}
	
	public static GameConfig getConfig(final Entity game) {
		return (GameConfig)game.getProperty(CONFIG_FIELD);
	}
	
	public static boolean getCompleted(final Entity game) {
		return (Boolean)game.getProperty(COMPLETED_FIELD);
	}
	
	@SuppressWarnings("unchecked")
	public static List<PieceIndex> getPieces(final Entity game) {
		return (List<PieceIndex>)game.getProperty(PIECES_FIELD);
	}
	
	public static String getTurn(final Entity game) {
		return (String)game.getProperty(TURN_FIELD);
	}
	
	@SuppressWarnings("unchecked")
	public static String buildJsonSummary(final Entity game) {
		final long id = game.getKey().getId();
		final Date lastPlay = (Date)game.getProperty(LAST_PLAY_FIELD);
		final String player1 = (String)game.getProperty(PLAYER_1_FIELD);
		final String player2 = (String)game.getProperty(PLAYER_2_FIELD);
		final boolean completed = (Boolean)game.getProperty(COMPLETED_FIELD);
		final String turn = (String)game.getProperty(TURN_FIELD);
		final List<PieceIndex> pieces = (List<PieceIndex>)game.getProperty(PIECES_FIELD);
		final StringBuilder sb = new StringBuilder();
		
		sb.append("{");
		sb.append("\"id\":" + id);
		sb.append(",\"lastplay\":" + lastPlay.getTime());
		sb.append(",\"player1\":\"" + player1 + "\"");
		sb.append(",\"player2\":\"" + player2 + "\"");
		sb.append(",\"completed\":" + completed);
		sb.append(",\"turn\":" + turn);
		sb.append(",\"pieces\":[");
		
		for (int i = 0; i < pieces.size(); ++i) {
			final PieceIndex pi = pieces.get(i);
			final StringBuilder psb = new StringBuilder();
			
			psb.append("{\"player\":" + pi.player.ordinal());
			psb.append("{\"x\":" + pi.index.x);
			psb.append("{\"y\":" + pi.index.y);
			psb.append("{\"z\":" + pi.index.z);
			psb.append("}");
			sb.append(psb.toString());
			
			if (i < pieces.size() - 1) {
				sb.append(",");
			}
		}
		
		sb.append("]}");
		return sb.toString();
	}
	
}
