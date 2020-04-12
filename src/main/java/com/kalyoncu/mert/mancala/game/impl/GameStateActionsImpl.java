package com.kalyoncu.mert.mancala.game.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;

import com.kalyoncu.mert.mancala.entity.MancalaGameResponse;
import com.kalyoncu.mert.mancala.game.GameStateActions;
import com.kalyoncu.mert.mancala.game.enums.GameEvents;
import com.kalyoncu.mert.mancala.game.enums.GameStates;
import com.kalyoncu.mert.mancala.game.enums.GameEvents.Events;
import com.kalyoncu.mert.mancala.game.enums.GameStates.States;
import com.kalyoncu.mert.mancala.impl.MancalaResponseImpl;
import com.kalyoncu.mert.mancala.pit.Pit;
import com.kalyoncu.mert.mancala.pit.PitEvents;
import com.kalyoncu.mert.mancala.pit.playerpit.PitStateActions;
import com.kalyoncu.mert.mancala.pit.playerstore.StoreStateActions;

@Configuration
public class GameStateActionsImpl implements GameStateActions {

	private static final Logger logger = Logger.getLogger(GameStateActionsImpl.class.getName());

	@Autowired
	PitStateActions pitStateActions;

	@Autowired
	MancalaResponseImpl mancalaResponseImpl;

	@Autowired
	private StateMachine<GameStates.States, GameEvents.Events> gameStateMachine;

	private static final String IS_REPLAY = "isReplay";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kalyoncu.mert.mancala.game.GameStateActions#initialAction()
	 */
	@Override
	@Bean
	public Action<States, Events> initialAction() {
		return ctx -> {
			logger.log(Level.INFO, "inside initialAction() method :" + ctx.getTarget().getId());

			boolean isReplay = (boolean) ctx.getExtendedState().getVariables().getOrDefault(IS_REPLAY, false);

			// If the game is being restarted, then START_GAME action sent to initialize the
			// pits and stores
			if (isReplay) {
				logger.log(Level.INFO, "Player wants to replay, invoking new game :" + ctx.getTarget().getId());
				ctx.getStateMachine().sendEvent(Events.START_GAME);
				logger.log(Level.INFO, "START_GAME Game event sent :");
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kalyoncu.mert.mancala.game.GameStateActions#startGameAction()
	 */
	@Override
	@Bean
	public Action<States, Events> startGameAction(StoreStateActions storeStateActions) {
		return ctx -> {

			logger.log(Level.INFO, "inside startGameAction() method");
			boolean isReplay = (boolean) ctx.getExtendedState().getVariables().getOrDefault(IS_REPLAY, false);

			// Check if this is the first time, then create all pitstate machines
			if (!isReplay) {
				pitStateActions.createPitStateMachines();
			}

			ctx.getExtendedState().getVariables().put(IS_REPLAY, false);
			// START_GAME event is sent to all pits and stores to change their state
			pitStateActions.sendBulkEvent(PitEvents.PLAYERPITEVENTS.START_GAME);
			storeStateActions.sendBulkEvent(PitEvents.PLAYERSTOREEVENTS.START_GAME);
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kalyoncu.mert.mancala.game.GameStateActions#endGameAction()
	 */

	public boolean endGameAction() {
		gameStateMachine.sendEvent(GameEvents.Events.REPLAY);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kalyoncu.mert.mancala.game.GameStateActions#replayAction()
	 */
	@Override
	@Bean
	public Action<States, Events> replayAction() {
		return ctx -> {
			logger.log(Level.INFO, "inside replayAction :" + ctx.getTarget().getId());
			ctx.getExtendedState().getVariables().put(IS_REPLAY, true);
		};
	}

	public boolean sendBulkEvent(GameEvents.Events event) {
		logger.log(Level.INFO, "Inside sendBulkEvent method for GameState, event is : {0} ", event);
		gameStateMachine.sendEvent(event);
		return true;
	}

	public MancalaGameResponse startTheGame() {
		startGameStateMachine();
		return mancalaResponseImpl.createMancalaGameResponse(pitStateActions.getPitStateMachinePairList(), "Player 1",
				false);

	}

	@Override
	public boolean startGameStateMachine() {
		logger.log(Level.INFO, "inside startGameStateMachine() method");
		gameStateMachine.start();

		gameStateMachine.sendEvent(GameEvents.Events.START_GAME);
		logger.log(Level.INFO, "gameStateMachine is started");
		return true;
	}

	public StateMachine<GameStates.States, GameEvents.Events> getGameStateMachine() {
		return gameStateMachine;
	}

	@Override
	public boolean isGameCompleted() {
		logger.log(Level.INFO, "Inside the Method checksAfterLastMove()");
		if (isGameOver()) {
			gameStateMachine.sendEvent(GameEvents.Events.END_GAME);
			return true;
		}
		return false;
	}

	public boolean isGameOver() {
		List<Pair<?, ?>> pitStateMachinePair = pitStateActions.getPitStateMachinePairList();
		return gameOverControl(pitStateMachinePair);
	}

	public boolean gameOverControl(List<Pair<?, ?>> pitStateMachinePair) {
		boolean arePlayersAllPitsEmpty = true;
		for (int i = 0; i < pitStateMachinePair.size() - 1; i++) {
			if (i == 6 && arePlayersAllPitsEmpty) {
				return true;
			} else if (i == 6 && !arePlayersAllPitsEmpty) {
				arePlayersAllPitsEmpty = true;
				continue;
			}
			Pit pit = (Pit) pitStateMachinePair.get(i).getValue0();
			if (pit.getPieceCount() > 0) {
				arePlayersAllPitsEmpty = false;
			}
		}
		return arePlayersAllPitsEmpty;
	}

}
