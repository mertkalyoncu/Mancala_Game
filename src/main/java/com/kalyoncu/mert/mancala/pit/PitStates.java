package com.kalyoncu.mert.mancala.pit;

public interface PitStates {
	enum PLAYERPITSTATES implements PitStates {
		INITIAL, EMPTY, ONE_ITEM, MANY_ITEMS
	}

	enum PLAYERSTORESTATES implements PitStates {
		INITIAL, EMPTY, ONE_ITEM, MANY_ITEMS
	}
}
