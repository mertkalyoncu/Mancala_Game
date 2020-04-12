package com.kalyoncu.mert.mancala.game.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.guard.Guard;

import com.kalyoncu.mert.mancala.game.GameStateGuards;
import com.kalyoncu.mert.mancala.game.enums.GameEvents.Events;
import com.kalyoncu.mert.mancala.game.enums.GameStates.States;

@Configuration
public class GameStateGuardsImpl implements GameStateGuards {

	private static final Logger logger = Logger.getLogger(GameStateGuardsImpl.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kalyoncu.mert.mancala.game.GameStateGuards#newGameGuard()
	 */
	@Override
	@Bean
	public Guard<States, Events> startGameGuard() {

		return ctx -> {
			logger.log(Level.INFO, "GameStateGuardsImpl: inside startGameGuard() method");
			return ctx.getStateMachine().getState().getId().equals(States.INITIAL);
		};

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kalyoncu.mert.mancala.game.GameStateGuards#endGameGuard()
	 */
	@Override
	@Bean
	public Guard<States, Events> endGameGuard() {
		return ctx -> {
			logger.log(Level.INFO, "GameStateGuardsImpl: inside endGameGuard() method");
			return ctx.getStateMachine().getState().getId().equals(States.IN_PROGRESS);
		};
	}
}
