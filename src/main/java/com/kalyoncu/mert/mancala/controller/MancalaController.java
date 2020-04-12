package com.kalyoncu.mert.mancala.controller;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.kalyoncu.mert.mancala.entity.MancalaGameResponse;
import com.kalyoncu.mert.mancala.game.GameStateActions;
import com.kalyoncu.mert.mancala.game.impl.GameStateActionsImpl;
import com.kalyoncu.mert.mancala.pit.playerpit.PitStateActions;
import com.kalyoncu.mert.mancala.pit.playerstore.StoreStateActions;

@RestController
public class MancalaController {

	private static final Logger logger = Logger.getLogger(GameStateActionsImpl.class.getName());

	@Autowired
	GameStateActions gameStateAction;

	@Autowired
	private PitStateActions pitStateActions;

	@Autowired
	private StoreStateActions pitStoreActions;

	@GetMapping(value = "/new")
	public MancalaGameResponse startGame() {
		logger.log(Level.INFO, "newGame() is called in Mancala controller");
		return gameStateAction.startTheGame();
	}

	@GetMapping(value = "/withdraw/{id}")
	public MancalaGameResponse withdraw(@PathVariable("id") int id) {
		logger.log(Level.INFO, "withdraw() is called in Mancala controller");
		return pitStateActions.processWithdraw(id);
	}

	@GetMapping(value = "/replayGame")
	public MancalaGameResponse replayGame() throws InterruptedException {
		logger.log(Level.INFO, "restartGame() is called in Mancala controller");

		gameStateAction.endGameAction();
		Thread.sleep(250);
		pitStateActions.initializePitStates();
		pitStoreActions.initializePitStores();

		return gameStateAction.startTheGame();
	}

	@GetMapping(value = "/endGame")
	public boolean endGame() {
		logger.log(Level.INFO, "restartGame() is called in Mancala controller");
		gameStateAction.getGameStateMachine().stop();
		pitStateActions.endPitStateActions();
		pitStoreActions.endPitStoreActions();
		pitStateActions.setPitStateMachinePairList(new ArrayList<>());
		return true;
	}

}
