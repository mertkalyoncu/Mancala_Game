package com.kalyoncu.mert.mancala.pit.playerpit;

import org.springframework.statemachine.guard.Guard;

import com.kalyoncu.mert.mancala.pit.PitEvents;
import com.kalyoncu.mert.mancala.pit.PitStates;

public interface PitStateGuards {

	Guard<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> isEmpty();

	Guard<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> hasManyItems();

	Guard<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> hasOneItem();

}