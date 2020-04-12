package com.kalyoncu.mert.mancala.pit.playerstore.impl;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.guard.Guard;

import com.kalyoncu.mert.mancala.pit.Pit;
import com.kalyoncu.mert.mancala.pit.PitEvents;
import com.kalyoncu.mert.mancala.pit.PitStates;
import com.kalyoncu.mert.mancala.pit.playerpit.impl.PitStateActionsImpl;
import com.kalyoncu.mert.mancala.pit.playerstore.StoreStateGuards;

@Configuration
public class StoreStateGuardsImpl implements StoreStateGuards {

	private static final Logger logger = Logger.getLogger(StoreStateGuardsImpl.class.getName());

	@Autowired
	PitStateActionsImpl pitStateActionsImpl;

	@Override
	public Guard<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> isEmpty() {
		return ctx -> {
			return getNumberOfPiecesInPit(ctx.getStateMachine().getId()) == 0;
		};
	}

	@Override
	public Guard<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> hasOneItem() {
		return ctx -> {
			logger.log(Level.INFO, "StoreStateGuardsImpl:inside hasOneItems() method");
			return getNumberOfPiecesInPit(ctx.getStateMachine().getId()) == 1;
		};
	}

	@Override
	public Guard<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> hasManyItems() {
		return ctx -> {
			logger.log(Level.INFO, "StoreStateGuardsImpl:inside hasManyItems() method");
			return getNumberOfPiecesInPit(ctx.getStateMachine().getId()) > 1;
		};
	}

	private int getNumberOfPiecesInPit(String id) {
		logger.log(Level.INFO, "getNumberOfPiecesInPit: {0}", id);
		return ((Pit) (pitStateActionsImpl.getPitStateMachinePairList().get(Integer.valueOf(id)).getValue0()))
				.getPieceCount();
	}

}
