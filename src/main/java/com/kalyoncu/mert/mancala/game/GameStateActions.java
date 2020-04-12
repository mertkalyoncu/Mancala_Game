package com.kalyoncu.mert.mancala.game;

import java.util.List;

import org.javatuples.Pair;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;

import com.kalyoncu.mert.mancala.entity.MancalaGameResponse;
import com.kalyoncu.mert.mancala.game.enums.GameEvents;
import com.kalyoncu.mert.mancala.game.enums.GameStates;
import com.kalyoncu.mert.mancala.game.enums.GameEvents.Events;
import com.kalyoncu.mert.mancala.game.enums.GameStates.States;
import com.kalyoncu.mert.mancala.pit.playerstore.StoreStateActions;

public interface GameStateActions {

	Action<States, Events> initialAction();

	Action<States, Events> startGameAction(StoreStateActions storeStateActions);

	Action<States, Events> replayAction();

	MancalaGameResponse startTheGame();

	boolean endGameAction();

	boolean startGameStateMachine();

	boolean sendBulkEvent(GameEvents.Events event);

	StateMachine<GameStates.States, GameEvents.Events> getGameStateMachine();

	boolean isGameCompleted();

	boolean gameOverControl(List<Pair<?, ?>> pitStateMachinePair);

}