package com.kalyoncu.mert.mancala.entity;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kalyoncu.mert.mancala.pit.Pit;

@Repository
public class MancalaGameResponse {

	public MancalaGameResponse() {
		super();
	}

	public MancalaGameResponse(List<Pit> pitList, String nextPlayer, boolean isGameOver) {
		super();
		this.pitList = pitList;
		this.nextPlayer = nextPlayer;
		this.gameOver = isGameOver;
	}

	private List<Pit> pitList;

	private String nextPlayer;

	private boolean gameOver;

	public List<Pit> getPitList() {
		return pitList;
	}

	public void setPitList(List<Pit> pitList) {
		this.pitList = pitList;
	}

	public String getNextPlayer() {
		return nextPlayer;
	}

	public void setNextPlayer(String nextPlayer) {
		this.nextPlayer = nextPlayer;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean isGameOver) {
		this.gameOver = isGameOver;
	}

}
