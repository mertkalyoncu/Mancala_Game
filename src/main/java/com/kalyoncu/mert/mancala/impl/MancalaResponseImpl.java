package com.kalyoncu.mert.mancala.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;

import com.kalyoncu.mert.mancala.entity.MancalaGameResponse;
import com.kalyoncu.mert.mancala.pit.Pit;
import com.kalyoncu.mert.mancala.pit.PitEvents;
import com.kalyoncu.mert.mancala.pit.PitStates;
import com.kalyoncu.mert.mancala.pit.playerpit.PitStateActions;

@Configuration
public class MancalaResponseImpl {

	private static final Logger logger = Logger.getLogger(MancalaResponseImpl.class.getName());

	@Autowired
	PitStateActions pitStateActionsImpl;

	public MancalaGameResponse createMancalaGameResponse(List<Pair<?, ?>> pairList, String nextPlayer,
			boolean isGameOver) {

		List<Pit> pitList = new ArrayList<>();
		for (Pair<?, ?> pair : pairList) {
			pitList.add((Pit) pair.getValue0());
		}

		logPitList();
		return new MancalaGameResponse(pitList, nextPlayer, isGameOver);
	}

	@SuppressWarnings("unchecked")
	private void logPitList() {

		for (int i = 0; i < 14; i++) {
			logger.log(Level.INFO, "{0}th Pit:", i);
			logger.log(Level.INFO, "Final Number of pieces is : "
					+ ((Pit) (pitStateActionsImpl.getPitStateMachinePairList().get(i).getValue0())).getPieceCount());
			if ((i == 6 || i == 13)) {
				logger.log(Level.INFO, "State is : {0}",
						((StateMachine<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS>) pitStateActionsImpl
								.getPitStateMachinePairList().get(i).getValue1()).getState().getId().name());
			} else {
				logger.log(Level.INFO, "State is : {0}",
						((StateMachine<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS>) pitStateActionsImpl
								.getPitStateMachinePairList().get(i).getValue1()).getState().getId().name());
			}
		}
	}
}
