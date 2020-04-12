package com.kalyoncu.mert.mancala.pit.playerstore;

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
@EnableStateMachineFactory(name = "StoreStateMachine")
public class StoreStateMachineConfiguration
		extends StateMachineConfigurerAdapter<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> {

	@Autowired
	StoreStateActions storeStateActions;

	@Autowired
	StoreStateGuards storeStateGuards;

	@Autowired
	public StateMachineFactory<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> storeStateMachineFactory;

	@Override
	public void configure(StateMachineStateConfigurer<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> states) throws Exception {
		states
			.withStates()
				.initial(PitStates.PLAYERSTORESTATES.INITIAL)
					.states(EnumSet.allOf(PitStates.PLAYERSTORESTATES.class));
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> transitions) throws Exception {
		transitions
			.withExternal().source(PitStates.PLAYERSTORESTATES.INITIAL).target(PitStates.PLAYERSTORESTATES.EMPTY).event(PitEvents.PLAYERSTOREEVENTS.START_GAME).action(storeStateActions.initializeStore())
			.and()
			.withExternal().source(PitStates.PLAYERSTORESTATES.EMPTY).target(PitStates.PLAYERSTORESTATES.ONE_ITEM).event(PitEvents.PLAYERSTOREEVENTS.DEPOSIT).guard(storeStateGuards.isEmpty()).action(storeStateActions.deposit())
			.and()
			.withExternal().source(PitStates.PLAYERSTORESTATES.EMPTY).target(PitStates.PLAYERSTORESTATES.MANY_ITEMS).event(PitEvents.PLAYERSTOREEVENTS.DEPOSIT_MANY).guard(storeStateGuards.isEmpty()).action(storeStateActions.depositMany())
			.and()
			.withExternal().source(PitStates.PLAYERSTORESTATES.ONE_ITEM).target(PitStates.PLAYERSTORESTATES.MANY_ITEMS).event(PitEvents.PLAYERSTOREEVENTS.DEPOSIT).guard(storeStateGuards.hasOneItem()).action(storeStateActions.deposit())
			.and()
			.withExternal().source(PitStates.PLAYERSTORESTATES.ONE_ITEM).target(PitStates.PLAYERSTORESTATES.MANY_ITEMS).event(PitEvents.PLAYERSTOREEVENTS.DEPOSIT_MANY).guard(storeStateGuards.hasOneItem()).action(storeStateActions.depositMany())
			.and()
			.withInternal().source(PitStates.PLAYERSTORESTATES.MANY_ITEMS).event(PitEvents.PLAYERSTOREEVENTS.DEPOSIT).guard(storeStateGuards.hasManyItems()).action(storeStateActions.deposit())
			.and()
			.withInternal().source(PitStates.PLAYERSTORESTATES.MANY_ITEMS).event(PitEvents.PLAYERSTOREEVENTS.DEPOSIT_MANY).guard(storeStateGuards.hasManyItems()).action(storeStateActions.depositMany())
			.and()
			.withInternal().source(PitStates.PLAYERSTORESTATES.EMPTY).event(PitEvents.PLAYERSTOREEVENTS.REPLAY).action(storeStateActions.initializeStore())
			.and()
			.withExternal().source(PitStates.PLAYERSTORESTATES.ONE_ITEM).target(PitStates.PLAYERSTORESTATES.EMPTY).event(PitEvents.PLAYERSTOREEVENTS.REPLAY).action(storeStateActions.initializeStore())
			.and()
			.withExternal().source(PitStates.PLAYERSTORESTATES.MANY_ITEMS).target(PitStates.PLAYERSTORESTATES.EMPTY).event(PitEvents.PLAYERSTOREEVENTS.REPLAY).action(storeStateActions.initializeStore());
		
	}

}
