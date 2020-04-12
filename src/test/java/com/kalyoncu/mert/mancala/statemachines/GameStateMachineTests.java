package com.kalyoncu.mert.mancala.statemachines;

import java.util.EnumSet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.test.StateMachineTestPlan;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kalyoncu.mert.mancala.game.GameStateActions;
import com.kalyoncu.mert.mancala.game.enums.GameEvents.Events;
import com.kalyoncu.mert.mancala.game.enums.GameStates.States;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GameStateMachineTests {

	@Autowired
	private GameStateActions gameStateAction;
	
	private StateMachine<States, Events> gameStateMachine;

	
	@BeforeEach
	public void setup() throws Exception {
		gameStateMachine =  buildMachine();
	}
	
	@AfterEach
	public void end() throws Exception {
		gameStateMachine.stop();
	}
	
	@Test
	public void testCompletingGame() throws Exception {
		
		StateMachineTestPlan<States, Events> plan =
				StateMachineTestPlanBuilder.<States, Events>builder()
					.stateMachine(gameStateMachine)
					.step()
						.expectState(States.INITIAL)
						.and()
					.step()
						.sendEvent(Events.START_GAME)
						.expectState(States.IN_PROGRESS)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(Events.NEXT_MOVE)
						.expectState(States.IN_PROGRESS)
						.expectStateChanged(0)
						.and()
					.step()
						.sendEvent(Events.END_GAME)
						.expectState(States.COMPLETED)
						.expectStateChanged(1)
						.and()
					.build();
		plan.test();
	}
	
	@Test
	public void testReplayGameBeforeGameCompleted() throws Exception {
		StateMachineTestPlan<States, Events> plan =
				StateMachineTestPlanBuilder.<States, Events>builder()
					.stateMachine(gameStateMachine)
					.step()
						.expectState(States.INITIAL)
						.and()
					.step()
						.sendEvent(Events.START_GAME)
						.expectState(States.IN_PROGRESS)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(Events.REPLAY)
						.expectState(States.INITIAL)
						.expectStateChanged(1)
						.and()
					.build();
		plan.test();
	}
	
	@Test
	public void testReplayGameAfterGameCompleted() throws Exception {
		StateMachineTestPlan<States, Events> plan =
				StateMachineTestPlanBuilder.<States, Events>builder()
					.stateMachine(gameStateMachine)
					.step()
						.expectState(States.INITIAL)
						.and()
					.step()
						.sendEvent(Events.START_GAME)
						.expectState(States.IN_PROGRESS)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(Events.END_GAME)
						.expectState(States.COMPLETED)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(Events.REPLAY)
						.expectState(States.INITIAL)
						.expectStateChanged(1)
						.and()
					.build();
		plan.test();
	}
	
	private StateMachine<States, Events> buildMachine() throws Exception {
	    StateMachineBuilder.Builder<States, Events> builder = StateMachineBuilder.builder();

	    builder.configureConfiguration()
	        .withConfiguration()
	            .autoStartup(true);

	    builder.configureStates()
	    	.withStates()
	    		.initial(States.INITIAL)
	    			.states(EnumSet.allOf(States.class))
	    				.state(States.INITIAL, gameStateAction.initialAction());

	    builder.configureTransitions()
		    .withExternal().source(States.INITIAL).target(States.IN_PROGRESS).event(Events.START_GAME)
			.and()
			.withExternal().source(States.IN_PROGRESS).target(States.COMPLETED).event(Events.END_GAME)//.guard(gameStateGuards.endGameGuard()).action(gameStateAction.endGameAction())
			.and()
			.withInternal().source(States.IN_PROGRESS).event(Events.NEXT_MOVE)
			.and()
			.withExternal().source(States.COMPLETED).target(States.INITIAL).event(Events.REPLAY)
			.and()
			.withExternal().source(States.IN_PROGRESS).target(States.INITIAL).event(Events.REPLAY);

	    return builder.build();
	}
}
