package com.kalyoncu.mert.mancala.pit.playerpit;

import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import com.kalyoncu.mert.mancala.pit.PitEvents;
import com.kalyoncu.mert.mancala.pit.PitStates;

@Configuration
@EnableStateMachineFactory(name = "PitStateMachine")
public class PitStateMachineConfiguration
		extends StateMachineConfigurerAdapter<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> {

	@Autowired
	PitStateActions pitStateActions;

	@Autowired
	PitStateGuards pitStateGuards;

	@Autowired
	public StateMachineFactory<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> pitStateMachineFactory;

	
	@Override
	public void configure(StateMachineStateConfigurer<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> states) throws Exception {
		states
			.withStates()
				.initial(PitStates.PLAYERPITSTATES.INITIAL)
					.states(EnumSet.allOf(PitStates.PLAYERPITSTATES.class));
	}
	



	@Override
	public void configure(StateMachineTransitionConfigurer<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> transitions) throws Exception {
		transitions
			.withExternal().source(PitStates.PLAYERPITSTATES.INITIAL).target(PitStates.PLAYERPITSTATES.MANY_ITEMS).event(PitEvents.PLAYERPITEVENTS.START_GAME).action(pitStateActions.initializePit())
			.and()
			.withExternal().source(PitStates.PLAYERPITSTATES.MANY_ITEMS).target(PitStates.PLAYERPITSTATES.EMPTY).event(PitEvents.PLAYERPITEVENTS.WITHDRAW).guard(pitStateGuards.hasManyItems()).action(pitStateActions.withdraw())
			.and()
			.withInternal().source(PitStates.PLAYERPITSTATES.MANY_ITEMS).event(PitEvents.PLAYERPITEVENTS.DEPOSIT).guard(pitStateGuards.hasManyItems()).action(pitStateActions.deposit())
			.and()
			.withExternal().source(PitStates.PLAYERPITSTATES.EMPTY).target(PitStates.PLAYERPITSTATES.ONE_ITEM).event(PitEvents.PLAYERPITEVENTS.DEPOSIT).guard(pitStateGuards.isEmpty()).action(pitStateActions.deposit())
			.and()
			.withExternal().source(PitStates.PLAYERPITSTATES.ONE_ITEM).target(PitStates.PLAYERPITSTATES.EMPTY).event(PitEvents.PLAYERPITEVENTS.WITHDRAW).guard(pitStateGuards.hasOneItem()).action(pitStateActions.withdraw())
			.and()
			.withExternal().source(PitStates.PLAYERPITSTATES.ONE_ITEM).target(PitStates.PLAYERPITSTATES.MANY_ITEMS).event(PitEvents.PLAYERPITEVENTS.DEPOSIT).guard(pitStateGuards.hasOneItem()).action(pitStateActions.deposit())
			.and()
			.withExternal().source(PitStates.PLAYERPITSTATES.EMPTY).target(PitStates.PLAYERPITSTATES.MANY_ITEMS).event(PitEvents.PLAYERPITEVENTS.REPLAY).action(pitStateActions.initializePit())
			.and()
			.withExternal().source(PitStates.PLAYERPITSTATES.ONE_ITEM).target(PitStates.PLAYERPITSTATES.MANY_ITEMS).event(PitEvents.PLAYERPITEVENTS.REPLAY).action(pitStateActions.initializePit())
			.and()
			.withInternal().source(PitStates.PLAYERPITSTATES.MANY_ITEMS).event(PitEvents.PLAYERPITEVENTS.REPLAY).action(pitStateActions.initializePit());
		
	}

}
