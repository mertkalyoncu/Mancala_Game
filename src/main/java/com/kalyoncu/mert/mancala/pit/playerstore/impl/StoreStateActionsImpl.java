package com.kalyoncu.mert.mancala.pit.playerstore.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;

import com.kalyoncu.mert.mancala.pit.Pit;
import com.kalyoncu.mert.mancala.pit.PitEvents;
import com.kalyoncu.mert.mancala.pit.PitStates;
import com.kalyoncu.mert.mancala.pit.playerpit.PitStateActions;
import com.kalyoncu.mert.mancala.pit.playerpit.impl.PitStateActionsImpl.StateParameters;
import com.kalyoncu.mert.mancala.pit.playerstore.StoreStateActions;
import com.kalyoncu.mert.mancala.pit.playerstore.StoreStateMachineConfiguration;
import com.kalyoncu.mert.mancala.pit.playerstore.entity.impl.PlayerStoreImpl;

@Configuration
public class StoreStateActionsImpl implements StoreStateActions {

	private static final Logger logger = Logger.getLogger(StoreStateActionsImpl.class.getName());

	public enum StoreIndex {
		PLAYER_1_STORE(6), PLAYER_2_STORE(13);

		private final int value;

		private StoreIndex(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public enum StoreInitialPieceCounts {
		PLAYER_PIT(6), PLAYER_STORE(0);

		private final int value;

		private StoreInitialPieceCounts(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
	
	@Autowired
	private StoreStateMachineConfiguration storeStateMachineConfiguration;

	@Autowired
	private PitStateActions pitStateActions;

	public List<Pair<?, ?>> pitStateMachinePairList = new ArrayList<Pair<?, ?>>();

	private List<Pair<Pit, StateMachine<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS>>> storeStateMachinePairList = new ArrayList<>();

	@Override
	@Bean
	public Action<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> initializeStore() {

		return ctx -> initializeStoreObject(ctx.getStateMachine().getId());

	}

	private boolean initializeStoreObject(String id) {
		logger.log(Level.INFO, "inside initializePitObjects() method for id: {0}", id);
		Pit pit = (Pit) pitStateActions.getPitStateMachinePairList().get(Integer.valueOf(id)).getValue0();
		pit.initialize();
		return true;
	}

	@Override
	public Action<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> deposit() {
		return ctx -> {
			logger.log(Level.INFO, "Inside deposit() action of PitStore");
			int id = Integer.parseInt(ctx.getStateMachine().getId());
			Pit pit = getPitObject(id);
			pit.setPieceCount(pit.getPieceCount() + 1);
			updatePitStateMachinePairList(id, pit);
		};
	}

	private Pit getPitObject(int id) {
		return (Pit) (pitStateActions.getPitStateMachinePairList().get(id).getValue0());
	}

	private boolean updatePitStateMachinePairList(int id, Pit pit) {
		pitStateActions.getPitStateMachinePairList().set(id,
				Pair.with(pit, pitStateActions.getPitStateMachinePairList().get(id).getValue1()));
		return true;
	}

	@Override
	public Action<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> depositMany() {
		return ctx -> {
			logger.log(Level.INFO, "Inside depositMany() action of PitStore");
			int id = Integer.parseInt(ctx.getStateMachine().getId());
			int totalPiecesInOppositePit = (int) ctx.getExtendedState().getVariables()
					.getOrDefault(StateParameters.TOTAL_PIECES_TO_DEPOSIT, 0);
			ctx.getExtendedState().getVariables().put(StateParameters.TOTAL_PIECES_TO_DEPOSIT, 0);
			if (totalPiecesInOppositePit > 1) {
				Pit pit = getPitObject(id);
				pit.setPieceCount(pit.getPieceCount() + totalPiecesInOppositePit);
				updatePitStateMachinePairList(id, pit);
			}

		};
	}

	public void createStoreStateMachines(List<Pair<?, ?>> pitStateMachinePairList) {
		Pit[] arrayOfStore = createStoreObjects();

		storeStateMachinePairList.clear();
		logger.log(Level.INFO, "creating Store State Machines");
		for (int i = 0; i < pitStateMachinePairList.size(); i++) {
			StateMachine<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> storeStateMachine = null;
			if (i == StoreIndex.PLAYER_1_STORE.getValue() || i == StoreIndex.PLAYER_2_STORE.getValue()) {
				storeStateMachine = storeStateMachineConfiguration.storeStateMachineFactory
						.getStateMachine(String.valueOf(i));
				storeStateMachine.start();

				int j = (i == StoreIndex.PLAYER_1_STORE.getValue()) ? 0 : 1;
				pitStateMachinePairList.set(i, (Pair.with(arrayOfStore[j], storeStateMachine)));

				storeStateMachinePairList.add(Pair.with(arrayOfStore[j], storeStateMachine));
			}

		}
		this.pitStateMachinePairList = pitStateMachinePairList;
		logger.log(Level.INFO, "all state machines are created...");

	}

	private Pit[] createStoreObjects() {
		Pit[] arrayOfStore = new Pit[2];

		logger.log(Level.INFO, "creating Pit Objects");
		arrayOfStore[0] = new PlayerStoreImpl(StoreIndex.PLAYER_1_STORE.getValue(),
				StoreInitialPieceCounts.PLAYER_STORE.getValue(), "Player 1");
		arrayOfStore[1] = new PlayerStoreImpl(StoreIndex.PLAYER_2_STORE.getValue(),
				StoreInitialPieceCounts.PLAYER_STORE.getValue(), "Player 2");

		logger.log(Level.INFO, " 0th store piece count is : " + arrayOfStore[0].getPieceCount());
		logger.log(Level.INFO, " 0th store name is : " + arrayOfStore[0].getPlayer());

		logger.log(Level.INFO, " 1st store piece count is : " + arrayOfStore[1].getPieceCount());
		logger.log(Level.INFO, " 1st store name is : " + arrayOfStore[1].getPlayer());

		return arrayOfStore;
	}

	public boolean sendBulkEvent(PitEvents.PLAYERSTOREEVENTS event) {

		for (Pair<?, ?> pair : pitStateMachinePairList) {
			if ((Pit) pair.getValue0() != null
					&& (((Pit) pair.getValue0()).getId() == StoreIndex.PLAYER_1_STORE.getValue()
							|| ((Pit) pair.getValue0()).getId() == StoreIndex.PLAYER_2_STORE.getValue())) {
				@SuppressWarnings("unchecked")
				StateMachine<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> sm = (StateMachine<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS>) pair
						.getValue1();
				sm.sendEvent(event);
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean endPitStoreActions() {
		List<Pair<?, ?>> pitStateMachinePair = pitStateActions.getPitStateMachinePairList();
		for (Pair<?, ?> pair : pitStateMachinePair) {
			if (((Pit) pair.getValue0()).getId() == StoreIndex.PLAYER_1_STORE.getValue()
					|| ((Pit) pair.getValue0()).getId() == StoreIndex.PLAYER_2_STORE.getValue()) {
				((StateMachine<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS>) pair.getValue1()).stop();
			}
		}
		return true;
	}

	@Override
	public boolean initializePitStores() {
		sendBulkEvent(PitEvents.PLAYERSTOREEVENTS.REPLAY);
		return true;
	}

}
