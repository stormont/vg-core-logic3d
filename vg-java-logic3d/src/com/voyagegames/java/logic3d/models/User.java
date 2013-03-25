package com.voyagegames.java.logic3d.models;

import java.util.ArrayList;
import java.util.List;

public class User {
	
	public final String name;
	public final byte[] encryptedPassword;
	
	private final List<String> mFriends;
	
	public User(final String name, final byte[] password) {
		this.name = name;
		this.encryptedPassword = password;
		this.mFriends = new ArrayList<String>();
	}
	
	public List<String> friends() {
		return mFriends;
	}
	
	public void addFriend(final String user) {
		mFriends.add(user);
	}

}
