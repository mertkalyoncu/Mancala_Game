package com.kalyoncu.mert.mancala.pit.playerstore;

import org.springframework.statemachine.guard.Guard;

import com.kalyoncu.mert.mancala.pit.PitEvents;
import com.kalyoncu.mert.mancala.pit.PitStates;

public interface StoreStateGuards {

	Guard<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> isEmpty();

	Guard<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> hasOneItem();

	Guard<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> hasManyItems();

}