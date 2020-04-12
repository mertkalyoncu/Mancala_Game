package com.kalyoncu.mert.mancala.pit.playerpit.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.guard.Guard;

import com.kalyoncu.mert.mancala.pit.Pit;
import com.kalyoncu.mert.mancala.pit.PitEvents;
import com.kalyoncu.mert.mancala.pit.PitStates;
import com.kalyoncu.mert.mancala.pit.playerpit.PitStateGuards;

@Configuration
public class PitStateGuardsImpl implements PitStateGuards {

	private static final Logger logger = Logger.getLogger(PitStateGuardsImpl.class.getName());

	@Autowired
	PitStateActionsImpl pitStateActionsImpl;

	@Override
	@Bean
	public Guard<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> isEmpty() {
		return ctx -> {
			logger.log(Level.INFO, "inside isEmpty() method");
			return getNumberOfPiecesInPit(ctx.getStateMachine().getId()) == 0;
		};

	}

	@Override
	@Bean
	public Guard<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> hasManyItems() {
		return ctx -> {
			logger.log(Level.INFO, "inside hasManyItems() method");
			return getNumberOfPiecesInPit(ctx.getStateMachine().getId()) > 1;
		};
	}

	@Override
	@Bean
	public Guard<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> hasOneItem() {
		return ctx -> {
			logger.log(Level.INFO, "inside hasOneItem() method");
			return getNumberOfPiecesInPit(ctx.getStateMachine().getId()) == 1;
		};
	}

	private int getNumberOfPiecesInPit(String id) {
		logger.log(Level.INFO, "inside getNumberOfPiecesInPit() method");
		return ((Pit) (pitStateActionsImpl.getPitStateMachinePairList().get(Integer.valueOf(id)).getValue0()))
				.getPieceCount();

	}
}
