package com.kalyoncu.mert.mancala.pit.playerpit.impl;

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

import com.kalyoncu.mert.mancala.entity.MancalaGameResponse;
import com.kalyoncu.mert.mancala.game.GameStateActions;
import com.kalyoncu.mert.mancala.game.enums.GameEvents;
import com.kalyoncu.mert.mancala.impl.MancalaResponseImpl;
import com.kalyoncu.mert.mancala.pit.Pit;
import com.kalyoncu.mert.mancala.pit.PitEvents;
import com.kalyoncu.mert.mancala.pit.PitStates;
import com.kalyoncu.mert.mancala.pit.playerpit.PitStateMachineConfiguration;
import com.kalyoncu.mert.mancala.pit.playerpit.entity.impl.PlayerPitImpl;
import com.kalyoncu.mert.mancala.pit.playerstore.StoreStateActions;

@Configuration
public class PitStateActionsImpl implements com.kalyoncu.mert.mancala.pit.playerpit.PitStateActions {

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

	public enum StateParameters {
		ARE_PIECES_GOING_TO_OPPOSITE_STORE, ARE_PIECES_GOING_TO_OWN_STORE, TOTAL_PIECES_IN_OPPOSITE_PIT,
		NEXT_ROUND_PLAYER, TOTAL_PIECES_TO_DEPOSIT, LAST_PIECE_DEPOSITED
	}

	private static final Logger logger = Logger.getLogger(PitStateActionsImpl.class.getName());

	@Autowired
	private PitStateMachineConfiguration pitStateMachineConfiguration;

	@Autowired
	private StoreStateActions storeStateActions;

	@Autowired
	private GameStateActions gameStateActions;

	@Autowired
	private MancalaResponseImpl mancalaResponseImpl;

	List<Integer> withdrawOpponentsList = new ArrayList<>();

	private List<Pair<?, ?>> pitStateMachinePairList = new ArrayList<>();

	public void setPitStateMachinePairList(List<Pair<?, ?>> pitStateMachinePairList) {
		this.pitStateMachinePairList = pitStateMachinePairList;
	}

	public List<Pair<?, ?>> getPitStateMachinePairList() {
		return pitStateMachinePairList;
	}

	private static final String PLAYER_1 = "Player 1";
	private static final String PLAYER_2 = "Player 2";
	private String currentPlayer = "";

	@Override
	@Bean
	public Action<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> initializePit() {

		return ctx -> initializePitObject(ctx.getStateMachine().getId());

	}

	@SuppressWarnings("unchecked")
	private boolean initializePitObject(String id) {
		logger.log(Level.INFO, "inside initializePitObjects() method");
		Pair<Pit, StateMachine<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS>> p = (Pair<Pit, StateMachine<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS>>) pitStateMachinePairList
				.get(Integer.valueOf(id));
		p.getValue0().initialize();
		logger.log(Level.INFO, "all pits objects are initialized");
		return true;
	}

	@Override
	@Bean
	public Action<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> withdraw() {
		return ctx -> {
			int id = Integer.parseInt(ctx.getStateMachine().getId());
			Pit pitToWithdraw = getPitObject(id);
			int pieceCount = pitToWithdraw.getPieceCount();
			pitToWithdraw.setPieceCount(0);
			updatePitStateMachinePairList(id, pitToWithdraw);

			// This boolean is to check whether withdraw is triggered because of opponents
			// player's pieces at this pit are deposited to current player's store
			boolean arePiecesGoingToOpponentStore = (boolean) ctx.getExtendedState().getVariables()
					.getOrDefault(StateParameters.ARE_PIECES_GOING_TO_OPPOSITE_STORE, false);
			// This boolean is to check whether withdraw is triggered because of current
			// player's pit are deposited to current player's store
			boolean arePiecesGoingToOwnStore = (boolean) ctx.getExtendedState().getVariables()
					.getOrDefault(StateParameters.ARE_PIECES_GOING_TO_OWN_STORE, false);
			// This is the total number of pieces in the opponents pit which are being
			// deposited to current player's store
			int totalPiecesInOppositePit = (int) ctx.getExtendedState().getVariables()
					.getOrDefault(StateParameters.TOTAL_PIECES_IN_OPPOSITE_PIT, 0);

			// initializing the state variables
			ctx.getExtendedState().getVariables().put(StateParameters.ARE_PIECES_GOING_TO_OPPOSITE_STORE, false);
			ctx.getExtendedState().getVariables().put(StateParameters.ARE_PIECES_GOING_TO_OWN_STORE, false);
			ctx.getExtendedState().getVariables().put(StateParameters.TOTAL_PIECES_IN_OPPOSITE_PIT, 0);

			if (arePiecesGoingToOpponentStore) {
				// sending all collected pieces to opponents store
				sendPiecesToOppositePlayerStore(id, totalPiecesInOppositePit);
			} else if (arePiecesGoingToOwnStore) {
				// sending current players one piece to own store
				sendOnePieceToCurrentPlayerStore(id);
			} else {
				// depositing all collected pieces to next Pits
				String nextPlayer = sendEventsForDeposit(id, pieceCount);
				ctx.getExtendedState().getVariables().put(StateParameters.NEXT_ROUND_PLAYER, nextPlayer);
			}
		};
	}

	private void sendOnePieceToCurrentPlayerStore(int id) {
		int destinationStoreId = -1;
		if (id < StoreIndex.PLAYER_1_STORE.getValue()) {
			destinationStoreId = StoreIndex.PLAYER_1_STORE.getValue();
		} else if (id > StoreIndex.PLAYER_1_STORE.getValue()) {
			destinationStoreId = StoreIndex.PLAYER_2_STORE.getValue();
		} else {
			logger.log(Level.WARNING, "Invalid Id is sent");
		}
		getPlayerStoreStateMachine(destinationStoreId).sendEvent(PitEvents.PLAYERSTOREEVENTS.DEPOSIT);
	}

	private void sendPiecesToOppositePlayerStore(int id, int totalPiecesInOppositePit) {
		int destinationStoreId = -1;
		if (id < StoreIndex.PLAYER_1_STORE.getValue()) {
			destinationStoreId = StoreIndex.PLAYER_2_STORE.getValue();
		} else if (id > StoreIndex.PLAYER_1_STORE.getValue()) {
			destinationStoreId = StoreIndex.PLAYER_1_STORE.getValue();
		} else {
			logger.log(Level.WARNING, "Invalid Id is sent");
		}
		// signal DEPOSIT event to deposit current player's store
		StateMachine<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> sm = getPlayerStoreStateMachine(
				destinationStoreId);
		if (totalPiecesInOppositePit == 1) {
			sm.sendEvent(PitEvents.PLAYERSTOREEVENTS.DEPOSIT);
		}
		// signal DEPOSIT_MANY event to deposit current player's store if there are many
		// pieces in the pit
		else {
			sm.getExtendedState().getVariables().put(StateParameters.TOTAL_PIECES_TO_DEPOSIT, totalPiecesInOppositePit);
			sm.sendEvent(PitEvents.PLAYERSTOREEVENTS.DEPOSIT_MANY);
		}

	}

	private String sendEventsForDeposit(int id, int pieceCount) {
		String nextRoundPlayer = "";
		int selectedPitId = id;
		while (pieceCount > 0) {
			int idToDeposit = (id + 1) % 14;
			// Skip other player's store while depositing
			if ((idToDeposit == StoreIndex.PLAYER_1_STORE.getValue()
					|| idToDeposit == StoreIndex.PLAYER_2_STORE.getValue())
					&& !getPitObject(idToDeposit).getPlayer().equals(getPitObject(selectedPitId).getPlayer())) {
				id++;
				continue;
			}
			// deposite to current player's store.
			else if (idToDeposit == StoreIndex.PLAYER_1_STORE.getValue()
					|| idToDeposit == StoreIndex.PLAYER_2_STORE.getValue()) {
				// signal DEPOSIT event to deposit current player's store
				getPlayerStoreStateMachine(idToDeposit).sendEvent(PitEvents.PLAYERSTOREEVENTS.DEPOSIT);
				// if the last piece is placed in current user's store, then the user plays for
				// another round
				if (pieceCount == 1) {
					nextRoundPlayer = currentPlayer;
					gameStateActions.getGameStateMachine().sendEvent(GameEvents.Events.NEXT_MOVE);
				}
			}
			// deposite one item to pit
			else {
				StateMachine<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> sm = getPlayerPitStateMachine(
						idToDeposit);
				// if the last pit is being deposited, this will be checked if it is empty,
				// so it's and opponent's pieces will be sent to store
				if (pieceCount == 1) {
					// whoever played this tour will wait for the next tour
					nextRoundPlayer = selectedPitId < StoreIndex.PLAYER_1_STORE.getValue() ? PLAYER_2 : PLAYER_1;
					sm.getExtendedState().getVariables().put(StateParameters.LAST_PIECE_DEPOSITED, idToDeposit);
				}

				getPlayerPitStateMachine(idToDeposit).sendEvent(PitEvents.PLAYERPITEVENTS.DEPOSIT);
			}
			id++;
			pieceCount--;
		}

		return nextRoundPlayer;
	}

	private Pit getPitObject(int id) {
		return (Pit) (pitStateMachinePairList.get(id).getValue0());
	}

	@SuppressWarnings("unchecked")
	private StateMachine<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> getPlayerPitStateMachine(int id) {
		return (StateMachine<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS>) pitStateMachinePairList.get(id)
				.getValue1();
	}

	@SuppressWarnings("unchecked")
	private StateMachine<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS> getPlayerStoreStateMachine(int id) {
		return (StateMachine<PitStates.PLAYERSTORESTATES, PitEvents.PLAYERSTOREEVENTS>) pitStateMachinePairList.get(id)
				.getValue1();
	}

	private boolean updatePitStateMachinePairList(int id, Pit pit) {
		pitStateMachinePairList.set(id, Pair.with(pit, pitStateMachinePairList.get(id).getValue1()));
		return true;
	}

	@Override
	@Bean
	public Action<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> deposit() {
		return ctx -> {
			logger.log(Level.INFO, "Inside deposit action of PitState");
			int id = Integer.parseInt(ctx.getStateMachine().getId());
			Pit pitToDeposit = getPitObject(id);
			int pieceCount = pitToDeposit.getPieceCount();
			pitToDeposit.setPieceCount(pieceCount + 1);
			updatePitStateMachinePairList(id, pitToDeposit);

			int lastDepositedPiece = (int) ctx.getExtendedState().getVariables()
					.getOrDefault(StateParameters.LAST_PIECE_DEPOSITED, -1);
			ctx.getExtendedState().getVariables().put(StateParameters.LAST_PIECE_DEPOSITED, -1);
			// If there is only one piece is left in current user's pit and there are pieces
			// on the opponents pit, then all current user's and his opponnent's pieces are
			// collected to store
			if (pieceCount == 0 && lastDepositedPiece > -1 && currentPlayer.equals(pitToDeposit.getPlayer())) {
				withdrawOpponentsList.add(Integer.valueOf(id));
			}
		};
	}

	public void createPitStateMachines() {
		Pit[] arrayOfPits = createPitObjects();

		// Pit state machines are to be created for the first time
		logger.log(Level.INFO, "creating Pit State Machines");
		for (int i = 0; i < 14; i++) {
			StateMachine<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> pitStateMachine = pitStateMachineConfiguration.pitStateMachineFactory
					.getStateMachine(String.valueOf(i));
			if (!(i == StoreIndex.PLAYER_1_STORE.getValue() || i == StoreIndex.PLAYER_2_STORE.getValue())) {

				pitStateMachine.start();
			}

			pitStateMachinePairList.add(Pair.with(arrayOfPits[i], pitStateMachine));

		}

		storeStateActions.createStoreStateMachines(pitStateMachinePairList);
	}

	private Pit[] createPitObjects() {
		// Pit objects are to be created for the first time
		Pit[] arrayOfPits = new Pit[14];

		logger.log(Level.INFO, "creating Pit Objects");
		for (int i = 0; i < 14; i++) {
			if (i == StoreIndex.PLAYER_1_STORE.getValue() || i == StoreIndex.PLAYER_2_STORE.getValue()) {
				arrayOfPits[i] = null;
			} else {
				arrayOfPits[i] = new PlayerPitImpl(i, StoreInitialPieceCounts.PLAYER_PIT.getValue(),
						i < 7 ? PLAYER_1 : PLAYER_2);
			}
		}

		return arrayOfPits;
	}

	@SuppressWarnings("unchecked")
	public boolean sendBulkEvent(PitEvents.PLAYERPITEVENTS event) {
		for (int i = 0; i < pitStateMachinePairList.size(); i++) {

			Pair<?, ?> pair = pitStateMachinePairList.get(i);

			// Event will only be sent for pit objects, prevented for store objects
			if (!(i == StoreIndex.PLAYER_1_STORE.getValue() || i == StoreIndex.PLAYER_2_STORE.getValue())) {
				StateMachine<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> sm = (StateMachine<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS>) pair
						.getValue1();
				sm.sendEvent(event);
			}
		}
		return true;

	}

	@SuppressWarnings("unchecked")
	public boolean withdrawOpponents(int id) {

		Pair<?, ?> currentUserPair = getPitStateMachinePairList().get(id);

		Pair<?, ?> opponentPair = getPitStateMachinePairList().get(12 - id);

		Pit pitToDeposit = (Pit) currentUserPair.getValue0();
		Pit opponentsPit = (Pit) opponentPair.getValue0();

		// Send opponent's pit WITHDRAW event to make it's state EMPTY
		StateMachine<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> opponentStateMachine = (StateMachine<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS>) opponentPair
				.getValue1();
		opponentStateMachine.getExtendedState().getVariables().put(StateParameters.ARE_PIECES_GOING_TO_OWN_STORE,
				false);
		opponentStateMachine.getExtendedState().getVariables().put(StateParameters.ARE_PIECES_GOING_TO_OPPOSITE_STORE,
				true);
		opponentStateMachine.getExtendedState().getVariables().put(StateParameters.TOTAL_PIECES_IN_OPPOSITE_PIT,
				opponentsPit.getPieceCount());
		opponentStateMachine.sendEvent(PitEvents.PLAYERPITEVENTS.WITHDRAW);

		// Send current user's pit WITHDRAW event to make it's state EMPTY
		StateMachine<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> currentStateMachine = (StateMachine<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS>) currentUserPair
				.getValue1();
		currentStateMachine.getExtendedState().getVariables().put(StateParameters.ARE_PIECES_GOING_TO_OWN_STORE, true);
		currentStateMachine.getExtendedState().getVariables().put(StateParameters.ARE_PIECES_GOING_TO_OPPOSITE_STORE,
				false);
		currentStateMachine.sendEvent(PitEvents.PLAYERPITEVENTS.WITHDRAW);

		// update the State Machine Pair List with pit objects with no pieces in
		pitToDeposit.setPieceCount(0);
		updatePitStateMachinePairList(id, pitToDeposit);

		opponentsPit.setPieceCount(0);
		updatePitStateMachinePairList(opponentsPit.getId(), opponentsPit);

		return true;
	}

	public void completeUnfinishedWithdrawPairs() {
		if (!withdrawOpponentsList.isEmpty()) {
			for (int i = 0; i < withdrawOpponentsList.size(); i++) {
				withdrawOpponents(withdrawOpponentsList.get(i));
			}
			withdrawOpponentsList.clear();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public MancalaGameResponse processWithdraw(int id) {
		StateMachine<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS> playerPitStateMachine = (StateMachine<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS>) getPitStateMachinePairList()
				.get(id).getValue1();
		currentPlayer = id < StoreIndex.PLAYER_1_STORE.getValue() ? PLAYER_1 : PLAYER_2;
		playerPitStateMachine.sendEvent(PitEvents.PLAYERPITEVENTS.WITHDRAW);
		completeUnfinishedWithdrawPairs();

		String nextRoundPlayer = (String) playerPitStateMachine.getExtendedState().getVariables()
				.getOrDefault(StateParameters.NEXT_ROUND_PLAYER, "");

		return mancalaResponseImpl.createMancalaGameResponse(getPitStateMachinePairList(), nextRoundPlayer,
				gameStateActions.isGameCompleted());

	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean endPitStateActions() {
		List<Pair<?, ?>> pitStateMachinePair = getPitStateMachinePairList();
		for (Pair<?, ?> pair : pitStateMachinePair) {
			if (((Pit) pair.getValue0()).getId() != StoreIndex.PLAYER_1_STORE.getValue()
					&& ((Pit) pair.getValue0()).getId() != StoreIndex.PLAYER_2_STORE.getValue()) {
				((StateMachine<PitStates.PLAYERPITSTATES, PitEvents.PLAYERPITEVENTS>) pair.getValue1()).stop();
			}

		}

		return true;
	}

	@Override
	public boolean initializePitStates() {
		sendBulkEvent(PitEvents.PLAYERPITEVENTS.REPLAY);
		return true;
	}

}
