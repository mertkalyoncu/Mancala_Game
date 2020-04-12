package com.kalyoncu.mert.mancala.pit;

public interface Pit {

	public boolean isEmpty();

	public boolean initialize();

	public int getPieceCount();

	void setPieceCount(int pieceCount);

	public String getPlayer();

	public int getId();
}
