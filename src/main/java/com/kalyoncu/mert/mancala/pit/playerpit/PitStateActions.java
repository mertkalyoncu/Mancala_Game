package com.kalyoncu.mert.mancala.pit.playerpit;

import java.util.List;

import org.javatuples.Pair;
import org.springframework.statemachine.action.Action;

import com.kalyoncu.mert.mancala.entity.MancalaGameResponse;
import com.kalyoncu.mert.mancala.pit.PitEvents;
import com.kalyoncu.mert.mancala.pit.PitStates;

public interface PitStateActions {

	Action<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> initializePit();

	Action<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> withdraw();

	Action<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> deposit();

	void createPitStateMachines();

	boolean sendBulkEvent(PitEvents.PLAYERPITEVENTS event);

	List<Pair<?, ?>> getPitStateMachinePairList();

	void setPitStateMachinePairList(List<Pair<?, ?>> pitStateMachinePairList);

	void completeUnfinishedWithdrawPairs();

	MancalaGameResponse processWithdraw(int id);

	boolean endPitStateActions();

	boolean initializePitStates();
}