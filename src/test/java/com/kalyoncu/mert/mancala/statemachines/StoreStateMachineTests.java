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
public class StoreStateMachineTests {
	private StateMachine<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> pitStoreMachine;

	@BeforeEach
	public void setup() throws Exception {
		pitStoreMachine = buildMachine();
		pitStoreMachine.start();
	}

	@AfterEach
	public void end() throws Exception {
		pitStoreMachine.stop();
	}

	@Test
	public void testInitializePitState() throws Exception {
		
		StateMachineTestPlan<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> plan =
				StateMachineTestPlanBuilder.<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS>builder()
					.stateMachine(pitStoreMachine)
					.step()
						.expectState(PitStates.PLAYERSTORESTATES.INITIAL)
						.and()
					.build();
		plan.test();
	}
	
	
	@Test
	public void testStoreStateAfterStartGameEvent() throws Exception {
		
		StateMachineTestPlan<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> plan =
				StateMachineTestPlanBuilder.<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS>builder()
					.stateMachine(pitStoreMachine)
					.step()
						.expectState(PitStates.PLAYERSTORESTATES.INITIAL)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERSTOREEVENTS.START_GAME)
						.expectState(PitStates.PLAYERSTORESTATES.EMPTY)
						.expectStateChanged(1)
						.and()
					.build();
		plan.test();
	}
	
	
	@Test
	public void testDepositToEmptyStore() throws Exception {
		
		StateMachineTestPlan<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> plan =
				StateMachineTestPlanBuilder.<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS>builder()
					.stateMachine(pitStoreMachine)
					.step()
						.expectState(PitStates.PLAYERSTORESTATES.INITIAL)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERSTOREEVENTS.START_GAME)
						.expectState(PitStates.PLAYERSTORESTATES.EMPTY)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERSTOREEVENTS.DEPOSIT)
						.expectState(PitStates.PLAYERSTORESTATES.ONE_ITEM)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERSTOREEVENTS.DEPOSIT)
						.expectState(PitStates.PLAYERSTORESTATES.MANY_ITEMS)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERSTOREEVENTS.DEPOSIT)
						.expectState(PitStates.PLAYERSTORESTATES.MANY_ITEMS)
						.expectStateChanged(0)
						.and()
					.build();
		plan.test();
	}
	
	@Test
	public void testDepositMany() throws Exception {
		
		StateMachineTestPlan<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> plan =
				StateMachineTestPlanBuilder.<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS>builder()
					.stateMachine(pitStoreMachine)
					.step()
						.expectState(PitStates.PLAYERSTORESTATES.INITIAL)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERSTOREEVENTS.START_GAME)
						.expectState(PitStates.PLAYERSTORESTATES.EMPTY)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERSTOREEVENTS.DEPOSIT_MANY)
						.expectState(PitStates.PLAYERSTORESTATES.MANY_ITEMS)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERSTOREEVENTS.DEPOSIT_MANY)
						.expectState(PitStates.PLAYERSTORESTATES.MANY_ITEMS)
						.expectStateChanged(0)
						.and()
					.build();
		plan.test();
	}
	
	@Test
	public void testDepositManyToEventWithOneItemStore() throws Exception {
		
		StateMachineTestPlan<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> plan =
				StateMachineTestPlanBuilder.<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS>builder()
					.stateMachine(pitStoreMachine)
					.step()
						.expectState(PitStates.PLAYERSTORESTATES.INITIAL)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERSTOREEVENTS.START_GAME)
						.expectState(PitStates.PLAYERSTORESTATES.EMPTY)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERSTOREEVENTS.DEPOSIT)
						.expectState(PitStates.PLAYERSTORESTATES.ONE_ITEM)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERSTOREEVENTS.DEPOSIT_MANY)
						.expectState(PitStates.PLAYERSTORESTATES.MANY_ITEMS)
						.expectStateChanged(1)
						.and()
					.build();
		plan.test();
	}
	
	@Test
	public void testInitializeStoreWithOneItemAfterReplayEvent() throws Exception {
		
		StateMachineTestPlan<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> plan =
				StateMachineTestPlanBuilder.<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS>builder()
					.stateMachine(pitStoreMachine)
					.step()
						.expectState(PitStates.PLAYERSTORESTATES.INITIAL)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERSTOREEVENTS.START_GAME)
						.expectState(PitStates.PLAYERSTORESTATES.EMPTY)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERSTOREEVENTS.DEPOSIT)
						.expectState(PitStates.PLAYERSTORESTATES.ONE_ITEM)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERSTOREEVENTS.REPLAY)
						.expectState(PitStates.PLAYERSTORESTATES.EMPTY)
						.expectStateChanged(1)
						.and()
						
					.build();
		plan.test();
	}
	
	@Test
	public void testInitializeEmptyStoreAfterReplayEvent() throws Exception {
		
		StateMachineTestPlan<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> plan =
				StateMachineTestPlanBuilder.<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS>builder()
					.stateMachine(pitStoreMachine)
					.step()
						.expectState(PitStates.PLAYERSTORESTATES.INITIAL)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERSTOREEVENTS.START_GAME)
						.expectState(PitStates.PLAYERSTORESTATES.EMPTY)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERSTOREEVENTS.REPLAY)
						.expectState(PitStates.PLAYERSTORESTATES.EMPTY)
						.expectStateChanged(0)
						.and()
						
					.build();
		plan.test();
	}
	
	@Test
	public void testInitializeStoreWithManyItemsAfterReplayEvent() throws Exception {
		
		StateMachineTestPlan<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> plan =
				StateMachineTestPlanBuilder.<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS>builder()
					.stateMachine(pitStoreMachine)
					.step()
						.expectState(PitStates.PLAYERSTORESTATES.INITIAL)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERSTOREEVENTS.START_GAME)
						.expectState(PitStates.PLAYERSTORESTATES.EMPTY)
						.expectStateChanged(1)
						.and()
						.step()
						.sendEvent(PitEvents.PLAYERSTOREEVENTS.DEPOSIT_MANY)
						.expectState(PitStates.PLAYERSTORESTATES.MANY_ITEMS)
						.expectStateChanged(1)
						.and()
					.step()
						.sendEvent(PitEvents.PLAYERSTOREEVENTS.REPLAY)
						.expectState(PitStates.PLAYERSTORESTATES.EMPTY)
						.expectStateChanged(1)
						.and()
						
					.build();
		plan.test();
	} 
	

	private StateMachine<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> buildMachine() throws Exception {
	    StateMachineBuilder.Builder<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> builder = StateMachineBuilder.builder();

	    builder.configureConfiguration()
	        .withConfiguration()
	            .autoStartup(true);

	    builder
	    	.configureStates()
	    		.withStates()
	    			.initial(PitStates.PLAYERSTORESTATES.INITIAL)
	    				.states(EnumSet.allOf(PitStates.PLAYERSTORESTATES.class));
	    
	    builder.configureTransitions()
			.withExternal().source(PitStates.PLAYERSTORESTATES.INITIAL).target(PitStates.PLAYERSTORESTATES.EMPTY).event(PitEvents.PLAYERSTOREEVENTS.START_GAME)
			.and()
			.withExternal().source(PitStates.PLAYERSTORESTATES.EMPTY).target(PitStates.PLAYERSTORESTATES.ONE_ITEM).event(PitEvents.PLAYERSTOREEVENTS.DEPOSIT)
			.and()
			.withExternal().source(PitStates.PLAYERSTORESTATES.EMPTY).target(PitStates.PLAYERSTORESTATES.MANY_ITEMS).event(PitEvents.PLAYERSTOREEVENTS.DEPOSIT_MANY)
			.and()
			.withExternal().source(PitStates.PLAYERSTORESTATES.ONE_ITEM).target(PitStates.PLAYERSTORESTATES.MANY_ITEMS).event(PitEvents.PLAYERSTOREEVENTS.DEPOSIT)
			.and()
			.withExternal().source(PitStates.PLAYERSTORESTATES.ONE_ITEM).target(PitStates.PLAYERSTORESTATES.MANY_ITEMS).event(PitEvents.PLAYERSTOREEVENTS.DEPOSIT_MANY)
			.and()
			.withInternal().source(PitStates.PLAYERSTORESTATES.MANY_ITEMS).event(PitEvents.PLAYERSTOREEVENTS.DEPOSIT)
			.and()
			.withInternal().source(PitStates.PLAYERSTORESTATES.MANY_ITEMS).event(PitEvents.PLAYERSTOREEVENTS.DEPOSIT_MANY)
			.and()
			.withInternal().source(PitStates.PLAYERSTORESTATES.EMPTY).event(PitEvents.PLAYERSTOREEVENTS.REPLAY)
			.and()
			.withExternal().source(PitStates.PLAYERSTORESTATES.ONE_ITEM).target(PitStates.PLAYERSTORESTATES.EMPTY).event(PitEvents.PLAYERSTOREEVENTS.REPLAY)
			.and()
			.withExternal().source(PitStates.PLAYERSTORESTATES.MANY_ITEMS).target(PitStates.PLAYERSTORESTATES.EMPTY).event(PitEvents.PLAYERSTOREEVENTS.REPLAY);
		
	    return builder.build();
	}
}
