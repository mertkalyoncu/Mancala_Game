package com.kalyoncu.mert.mancala.statemachines;

import java.util.EnumSet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.test.StateMachineTestPlan;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kalyoncu.mert.mancala.pit.PitEvents;
import com.kalyoncu.mert.mancala.pit.PitStates;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PitStateMachineTests {
	private StateMachine<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> pitStateMachine;

	@BeforeEach
	public void setup() throws Exception {
		pitStateMachine = buildMachine();
		pitStateMachine.start();
	}

	@AfterEach
	public void end() throws Exception {
		pitStateMachine.stop();
	}
	
	@Test
	public void testInitializePitState() throws Exception {
		
		StateMachineTestPlan<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> plan =
				StateMachineTestPlanBuilder.<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS>builder()
					.stateMachine(pitStateMachine)
					.step()
						.expectState(PitStates.PLAYERPITSTATES.INITIAL)
						.and()
					.build();
		plan.test();
	}
	
	@Test
	public void testPitStateAfterStartGameEvent() throws Exception {
		
		StateMachineTestPlan<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> plan =
				StateMachineTestPlanBuilder.<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS>builder()
					.stateMachine(pitStateMachine)
					.step()
						.expectState(PitStates.PLAYERPITSTATES.INITIAL)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERPITEVENTS.START_GAME)
						.expectState(PitStates.PLAYERPITSTATES.MANY_ITEMS)
						.expectStateChanged(1)
						.and()
					.build();
		plan.test();
	}
	
	
	@Test
	public void testWithdrawWithManyItemsInPit() throws Exception {
		
		StateMachineTestPlan<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> plan =
				StateMachineTestPlanBuilder.<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS>builder()
					.stateMachine(pitStateMachine)
					.step()
						.expectState(PitStates.PLAYERPITSTATES.INITIAL)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERPITEVENTS.START_GAME)
						.expectState(PitStates.PLAYERPITSTATES.MANY_ITEMS)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERPITEVENTS.WITHDRAW)
						.expectState(PitStates.PLAYERPITSTATES.EMPTY)
						.expectStateChanged(1)
						.and()
					.build();
		plan.test();
	}
	
	@Test
	public void testWithdrawWithOneItemInPit() throws Exception {
		
		StateMachineTestPlan<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> plan =
				StateMachineTestPlanBuilder.<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS>builder()
					.stateMachine(pitStateMachine)
					.step()
						.expectState(PitStates.PLAYERPITSTATES.INITIAL)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERPITEVENTS.START_GAME)
						.expectState(PitStates.PLAYERPITSTATES.MANY_ITEMS)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERPITEVENTS.WITHDRAW)
						.expectState(PitStates.PLAYERPITSTATES.EMPTY)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERPITEVENTS.DEPOSIT)
						.expectState(PitStates.PLAYERPITSTATES.ONE_ITEM)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERPITEVENTS.WITHDRAW)
						.expectState(PitStates.PLAYERPITSTATES.EMPTY)
						.expectStateChanged(1)
						.and()
					.build();
		plan.test();
	}
	
	@Test
	public void testDeposit() throws Exception {
		
		StateMachineTestPlan<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> plan =
				StateMachineTestPlanBuilder.<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS>builder()
					.stateMachine(pitStateMachine)
					.step()
						.expectState(PitStates.PLAYERPITSTATES.INITIAL)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERPITEVENTS.START_GAME)
						.expectState(PitStates.PLAYERPITSTATES.MANY_ITEMS)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERPITEVENTS.WITHDRAW)
						.expectState(PitStates.PLAYERPITSTATES.EMPTY)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERPITEVENTS.DEPOSIT)
						.expectState(PitStates.PLAYERPITSTATES.ONE_ITEM)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERPITEVENTS.DEPOSIT)
						.expectState(PitStates.PLAYERPITSTATES.MANY_ITEMS)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERPITEVENTS.DEPOSIT)
						.expectState(PitStates.PLAYERPITSTATES.MANY_ITEMS)
						.expectStateChanged(0)
						.and()
						
					.build();
		plan.test();
	}
	
	@Test
	public void testInitializePitWithOneItemAfterReplayEvent() throws Exception {
		
		StateMachineTestPlan<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> plan =
				StateMachineTestPlanBuilder.<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS>builder()
					.stateMachine(pitStateMachine)
					.step()
						.expectState(PitStates.PLAYERPITSTATES.INITIAL)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERPITEVENTS.START_GAME)
						.expectState(PitStates.PLAYERPITSTATES.MANY_ITEMS)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERPITEVENTS.WITHDRAW)
						.expectState(PitStates.PLAYERPITSTATES.EMPTY)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERPITEVENTS.DEPOSIT)
						.expectState(PitStates.PLAYERPITSTATES.ONE_ITEM)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERPITEVENTS.REPLAY)
						.expectState(PitStates.PLAYERPITSTATES.MANY_ITEMS)
						.expectStateChanged(1)
						.and()
					.build();
		plan.test();
	}
	
	@Test
	public void testInitializeEmptyPitAfterReplayEvent() throws Exception {
		
		StateMachineTestPlan<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> plan =
				StateMachineTestPlanBuilder.<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS>builder()
					.stateMachine(pitStateMachine)
					.step()
						.expectState(PitStates.PLAYERPITSTATES.INITIAL)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERPITEVENTS.START_GAME)
						.expectState(PitStates.PLAYERPITSTATES.MANY_ITEMS)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERPITEVENTS.WITHDRAW)
						.expectState(PitStates.PLAYERPITSTATES.EMPTY)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERPITEVENTS.REPLAY)
						.expectState(PitStates.PLAYERPITSTATES.MANY_ITEMS)
						.expectStateChanged(1)
						.and()
					.build();
		plan.test();
	}
	
	@Test
	public void testInitializePitWithManyItemsAfterReplayEvent() throws Exception {
		
		StateMachineTestPlan<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> plan =
				StateMachineTestPlanBuilder.<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS>builder()
					.stateMachine(pitStateMachine)
					.step()
						.expectState(PitStates.PLAYERPITSTATES.INITIAL)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERPITEVENTS.START_GAME)
						.expectState(PitStates.PLAYERPITSTATES.MANY_ITEMS)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERPITEVENTS.REPLAY)
						.expectState(PitStates.PLAYERPITSTATES.MANY_ITEMS)
						.expectStateChanged(0)
						.and()
					.build();
		plan.test();
	}
	

	private StateMachine<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> buildMachine() throws Exception {
	    StateMachineBuilder.Builder<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> builder = StateMachineBuilder.builder();

	    builder.configureConfiguration()
	        .withConfiguration()
	            .autoStartup(true);

	    builder
	    	.configureStates()
		    	.withStates()
		    		.initial(PitStates.PLAYERPITSTATES.INITIAL)
		    			.states(EnumSet.allOf(PitStates.PLAYERPITSTATES.class));

	    builder.configureTransitions()
			.withExternal().source(PitStates.PLAYERPITSTATES.INITIAL).target(PitStates.PLAYERPITSTATES.MANY_ITEMS).event(PitEvents.PLAYERPITEVENTS.START_GAME)
			.and()
			.withExternal().source(PitStates.PLAYERPITSTATES.MANY_ITEMS).target(PitStates.PLAYERPITSTATES.EMPTY).event(PitEvents.PLAYERPITEVENTS.WITHDRAW)
			.and()
			.withInternal().source(PitStates.PLAYERPITSTATES.MANY_ITEMS).event(PitEvents.PLAYERPITEVENTS.DEPOSIT)
			.and()
			.withExternal().source(PitStates.PLAYERPITSTATES.EMPTY).target(PitStates.PLAYERPITSTATES.ONE_ITEM).event(PitEvents.PLAYERPITEVENTS.DEPOSIT)
			.and()
			.withExternal().source(PitStates.PLAYERPITSTATES.ONE_ITEM).target(PitStates.PLAYERPITSTATES.EMPTY).event(PitEvents.PLAYERPITEVENTS.WITHDRAW)
			.and()
			.withExternal().source(PitStates.PLAYERPITSTATES.ONE_ITEM).target(PitStates.PLAYERPITSTATES.MANY_ITEMS).event(PitEvents.PLAYERPITEVENTS.DEPOSIT)
			.and()
			.withExternal().source(PitStates.PLAYERPITSTATES.EMPTY).target(PitStates.PLAYERPITSTATES.MANY_ITEMS).event(PitEvents.PLAYERPITEVENTS.REPLAY)
			.and()
			.withExternal().source(PitStates.PLAYERPITSTATES.ONE_ITEM).target(PitStates.PLAYERPITSTATES.MANY_ITEMS).event(PitEvents.PLAYERPITEVENTS.REPLAY)
			.and()
			.withInternal().source(PitStates.PLAYERPITSTATES.MANY_ITEMS).event(PitEvents.PLAYERPITEVENTS.REPLAY);
		
	    return builder.build();
	}
}
