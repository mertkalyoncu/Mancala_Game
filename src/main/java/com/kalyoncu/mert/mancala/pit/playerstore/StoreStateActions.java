package com.kalyoncu.mert.mancala.pit.playerstore;

import java.util.List;

import org.javatuples.Pair;
import org.springframework.statemachine.action.Action;

import com.kalyoncu.mert.mancala.pit.PitEvents;
import com.kalyoncu.mert.mancala.pit.PitStates;

public interface StoreStateActions {
	Action<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> initializeStore();

	Action<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> deposit();

	Action<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> depositMany();

	void createStoreStateMachines(List<Pair<?, ?>> pitStateMachinePairList);

	boolean sendBulkEvent(PitEvents.PLAYERSTOREEVENTS event);

	boolean endPitStoreActions();

	boolean initializePitStores();

}