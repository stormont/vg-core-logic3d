package com.voyagegames.logic3d;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.Query;
import com.voyagegames.logic3d.models.GameConfig;
import com.voyagegames.logic3d.models.PieceIndex;

public class Games {

	public static List<GameModel> getGames(final String player) {
		final Objectify ofy = OfyService.ofy();
		final Query<GameModel> q1 = ofy.query(GameModel.class).filter("player1", player);
		final Query<GameModel> q2 = ofy.query(GameModel.class).filter("player2", player);
		final List<GameModel> results = new ArrayList<GameModel>();
		
		for (final GameModel gm : q1) {
			results.add(gm);
		}
		
		for (final GameModel gm : q2) {
			results.add(gm);
		}
		
		return results;
	}
	
	public static List<GameModel> getGamesInProgress(final String player) {
		final List<GameModel> games = getGames(player);
		final List<GameModel> results = new ArrayList<GameModel>();
		
		for (final GameModel gm : games) {
			if (!gm.completed()) {
				results.add(gm);
			}
		}
		
		return results;
	}
	
	public static List<GameModel> getGamesInProgress(final String player, final String opponent) {
		final List<GameModel> games = getGames(player);
		final List<GameModel> results = new ArrayList<GameModel>();
		
		for (final GameModel gm : games) {
			if (gm.completed()) {
				continue;
			}
			
			if (gm.player1().equalsIgnoreCase(opponent) || gm.player2().equalsIgnoreCase(opponent)) {
				results.add(gm);
			}
		}
		
		return results;
	}
	
	public static List<GameModel> getCompletedGames(final String player) {
		final List<GameModel> games = getGames(player);
		final List<GameModel> results = new ArrayList<GameModel>();
		
		for (final GameModel gm : games) {
			if (gm.completed()) {
				results.add(gm);
			}
		}
		
		return results;
	}
	
	public static GameModel startGame(
			final String player1,
			final String player2,
			final GameConfig config,
			final String playerTurn) throws IOException {
		if (player1.equals(player2)) {
			return null;
		}
		
		final List<GameModel> gamesInProgress = getGamesInProgress(player1, player2);
		
		if (gamesInProgress.size() > 0) {
			return null;
		}

		final Objectify ofy = OfyService.ofy();
		final Date now = new Date();
		final GameModel gm =
				new GameModel(
						now,
						now,
						player1,
						player2,
						playerTurn,
						false,
						config);
		
		ofy.put(gm);
		return gm;
	}
	
	public static boolean completeGame(final GameModel game) {
		final Objectify ofy = OfyService.ofy();
		
		if (game.completed()) {
			return true;
		}

		game.setCompleted();
		ofy.put(game);
		return true;
	}
	
	public static boolean addPiece(final GameModel game, final PieceIndex piece) throws IOException, ClassNotFoundException {
		final Objectify ofy = OfyService.ofy();
		
		game.addPiece(piece);
		game.setNextTurn();
		ofy.put(game);
		return true;
	}
	
	public static String buildJsonSummary(final GameModel game) throws IOException, ClassNotFoundException {
		final StringBuilder sb = new StringBuilder();
		
		sb.append("{");
		sb.append("\"id\":" + game.id());
		sb.append(",\"lastplay\":" + game.lastPlay().getTime());
		sb.append(",\"player1\":\"" + game.player1() + "\"");
		sb.append(",\"player2\":\"" + game.player2() + "\"");
		sb.append(",\"completed\":" + game.completed());
		sb.append(",\"size\":" + game.config().size);
		sb.append(",\"turn\":\"" + game.turn() + "\"");
		sb.append(",\"pieces\":[");
	
		final List<PieceIndex> pieces = game.pieces();
		
		for (int i = 0; i < pieces.size(); ++i) {
			final PieceIndex pi = pieces.get(i);
			final StringBuilder psb = new StringBuilder();
			
			psb.append("{\"player\":" + pi.player().ordinal());
			psb.append(",\"x\":" + pi.index().x);
			psb.append(",\"y\":" + pi.index().y);
			psb.append(",\"z\":" + pi.index().z);
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
