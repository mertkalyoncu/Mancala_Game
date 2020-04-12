package com.kalyoncu.mert.mancala.game;

import org.springframework.statemachine.guard.Guard;

import com.kalyoncu.mert.mancala.game.enums.GameEvents.Events;
import com.kalyoncu.mert.mancala.game.enums.GameStates.States;

public interface GameStateGuards {

	Guard<States, Events> startGameGuard();

	Guard<States, Events> endGameGuard();

}