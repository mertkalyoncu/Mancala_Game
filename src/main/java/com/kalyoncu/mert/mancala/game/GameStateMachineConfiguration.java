package com.kalyoncu.mert.mancala.game;

import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import com.kalyoncu.mert.mancala.game.enums.GameEvents.Events;
import com.kalyoncu.mert.mancala.game.enums.GameStates.States;
import com.kalyoncu.mert.mancala.pit.playerstore.StoreStateActions;

@Configuration
@EnableStateMachine(name="GameStateMachine")
public class GameStateMachineConfiguration extends StateMachineConfigurerAdapter<States, Events> {
	
	@Autowired
	GameStateActions gameStateAction;
	
	@Autowired
	GameStateGuards gameStateGuards;
	
	@Autowired
	StoreStateActions storeStateActions;
	
	
	@Override
	public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
		states
			.withStates()
				.initial(States.INITIAL)
					.states(EnumSet.allOf(States.class))
						.state(States.INITIAL,gameStateAction.initialAction());

	}

	@Override
	public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
		transitions
			.withExternal().source(States.INITIAL).target(States.IN_PROGRESS).event(Events.START_GAME).guard(gameStateGuards.startGameGuard()).action(gameStateAction.startGameAction(storeStateActions))
			.and()
			.withExternal().source(States.IN_PROGRESS).target(States.COMPLETED).event(Events.END_GAME)//.guard(gameStateGuards.endGameGuard()).action(gameStateAction.endGameAction())
			.and()
			.withInternal().source(States.IN_PROGRESS).event(Events.NEXT_MOVE)
			.and()
			.withExternal().source(States.COMPLETED).target(States.INITIAL).event(Events.REPLAY).action(gameStateAction.replayAction())
			.and()
			.withExternal().source(States.IN_PROGRESS).target(States.INITIAL).event(Events.REPLAY).action(gameStateAction.replayAction());
	}

}
