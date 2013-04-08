package com.voyagegames.logic3d;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;

import com.voyagegames.logic3d.core.Common;
import com.voyagegames.logic3d.models.GameConfig;
import com.voyagegames.logic3d.models.PieceIndex;

public class GameModel {

    @Id
    private Long id;
    
    private Date created;
    private Date lastPlay;
    private String player1;
    private String player2;
    private String turn;
    private boolean completed;
    private byte[] config;
    private byte[] pieces;
    
    @SuppressWarnings("unused")
	private GameModel() {
    	// no-op; required for objectify-appengine
    }
    
    public GameModel(
    		final Date created,
    		final Date lastPlay,
    		final String player1,
    		final String player2,
    		final String turn,
    		final boolean completed,
    		final GameConfig config) throws IOException {
    	this.created = created;
    	this.lastPlay = lastPlay;
    	this.player1 = player1;
    	this.player2 = player2;
    	this.turn = turn;
    	this.completed = completed;
    	this.config = Common.serialize(config);
    	this.pieces = new byte[0];
    }
    
    public long id() {
    	return this.id;
    }
    
    public Date created() {
    	return this.created;
    }
    
    public Date lastPlay() {
    	return this.lastPlay;
    }
    
    public String player1() {
    	return this.player1;
    }
    
    public String player2() {
    	return this.player2;
    }
    
    public String turn() {
    	return this.turn;
    }
    
    public boolean completed() {
    	return this.completed;
    }
    
    public GameConfig config() throws IOException, ClassNotFoundException {
    	return (GameConfig)Common.deserialize(this.config);
    }
    
    @SuppressWarnings("unchecked")
	public List<PieceIndex> pieces() throws IOException, ClassNotFoundException {
    	final List<PieceIndex> result = new ArrayList<PieceIndex>();
    	
    	if (this.pieces != null && this.pieces.length > 0) {
    		final List<PieceIndex> deserialized = (List<PieceIndex>)Common.deserialize(this.pieces);
    		
	    	for (final PieceIndex pi : deserialized) {
	    		result.add(pi);
	    	}
    	}
    	
    	return result;
    }
    
    public void setCompleted() {
    	this.completed = true;
    }
    
    public void setNextTurn() {
    	if (this.turn.equals(this.player1)) {
    		this.turn = this.player2;
    	} else {
    		this.turn = this.player1;
    	}
    }

    public void addPiece(final PieceIndex piece) throws IOException, ClassNotFoundException {
    	final List<PieceIndex> result = pieces();
    	result.add(piece);
    	this.pieces = Common.serialize(result);
    }

}
