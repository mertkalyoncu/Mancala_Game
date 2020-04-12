package com.kalyoncu.mert.mancala.pit;

public interface PitEvents {

	enum PLAYERPITEVENTS implements PitEvents {
		INITIAL, START_GAME, WITHDRAW, DEPOSIT, REPLAY
	}

	enum PLAYERSTOREEVENTS implements PitEvents {
		INITIAL, START_GAME, DEPOSIT, DEPOSIT_MANY, REPLAY
	}

}
