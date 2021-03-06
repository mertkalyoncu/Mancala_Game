package com.kalyoncu.mert.mancala.pit.playerpit.entity.impl;

public class PlayerPitImpl implements com.kalyoncu.mert.mancala.pit.playerpit.entity.PlayerPit {

	public PlayerPitImpl(int id, int pieceCount, String player) {
		super();
		this.id = id;
		this.pieceCount = pieceCount;
		this.player = player;
	}

	private int id = -1;
	private int pieceCount;
	private String player;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPieceCount() {
		return pieceCount;
	}

	public void setPieceCount(int pieceCount) {
		this.pieceCount = pieceCount;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	@Override
	public boolean isEmpty() {
		return getPieceCount() == 0;
	}

	@Override
	public boolean initialize() {
		setPieceCount(6);
		return true;
	}

}
