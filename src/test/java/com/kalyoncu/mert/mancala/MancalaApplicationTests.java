package com.kalyoncu.mert.mancala;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.List;

import org.javatuples.Pair;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.kalyoncu.mert.mancala.game.GameStateActions;
import com.kalyoncu.mert.mancala.pit.Pit;
import com.kalyoncu.mert.mancala.pit.playerpit.PitStateActions;
import com.kalyoncu.mert.mancala.pit.playerpit.entity.impl.PlayerPitImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class MancalaApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private GameStateActions gameStateActions;

	@Autowired
	private PitStateActions pitStateActions;

	@Test
	public void testWithdrawFromPit() throws Exception {

		// this test will check if all the pieces in a pit are removed completely after
		// the pit is selected to play
		this.mockMvc.perform(get("/new"));
		this.mockMvc.perform(get("/withdraw/{id}", 1)).andExpect(jsonPath("$.pitList[1].pieceCount").value(0));
		this.mockMvc.perform(get("/endGame"));
	}

	@Test
	public void testLastDepositOnOwnStore() throws Exception {

		// this test will check if a player deposit his last piece to his own deposit,
		// he gets another round of play
		this.mockMvc.perform(get("/new"));
		this.mockMvc.perform(get("/withdraw/{id}", 0)).andExpect(jsonPath("$.nextPlayer").value("Player 1"));
		this.mockMvc.perform(get("/endGame"));
	}

	@Test
	public void testGameOverControl() throws Exception {
		
		// this test will check if the game ends when a player has his all pits empty, continues otherwise
		List<Pair<?,?>> player1ToFinish = Arrays.asList(Pair.with(new PlayerPitImpl(0,0,"Player 1"), ""),	
				Pair.with(new PlayerPitImpl(1,0,"Player 1"), ""),Pair.with(new PlayerPitImpl(2,0,"Player 1"), ""),
				Pair.with(new PlayerPitImpl(3,0,"Player 1"), ""),Pair.with(new PlayerPitImpl(4,0,"Player 1"), ""),
				Pair.with(new PlayerPitImpl(5,0,"Player 1"), ""),Pair.with(new PlayerPitImpl(6,10,"Player 1"), ""),
				Pair.with(new PlayerPitImpl(7,10,"Player 1"), ""),Pair.with(new PlayerPitImpl(8,0,"Player 1"), ""),
				Pair.with(new PlayerPitImpl(9,2,"Player 2"), ""),Pair.with(new PlayerPitImpl(10,0,"Player 1"), ""),
				Pair.with(new PlayerPitImpl(11,0,"Player 2"), ""),Pair.with(new PlayerPitImpl(12,0,"Player 1"), ""),
				Pair.with(new PlayerPitImpl(13,20,"Player 2"), ""));
		
		List<Pair<?,?>> player2ToFinish = Arrays.asList(Pair.with(new PlayerPitImpl(0,0,"Player 1"), ""),	
				Pair.with(new PlayerPitImpl(1,0,"Player 1"), ""),Pair.with(new PlayerPitImpl(2,0,"Player 1"), ""),
				Pair.with(new PlayerPitImpl(3,0,"Player 1"), ""),Pair.with(new PlayerPitImpl(4,0,"Player 1"), ""),
				Pair.with(new PlayerPitImpl(5,1,"Player 1"), ""),Pair.with(new PlayerPitImpl(6,10,"Player 1"), ""),
				Pair.with(new PlayerPitImpl(7,0,"Player 1"), ""),Pair.with(new PlayerPitImpl(8,0,"Player 2"), ""),
				Pair.with(new PlayerPitImpl(9,0,"Player 2"), ""),Pair.with(new PlayerPitImpl(10,0,"Player 2"), ""),
				Pair.with(new PlayerPitImpl(11,0,"Player 2"), ""),Pair.with(new PlayerPitImpl(12,0,"Player 2"), ""),
				Pair.with(new PlayerPitImpl(13,20,"Player 2"), ""));
		
		List<Pair<?,?>> nobodyFinishes = Arrays.asList(Pair.with(new PlayerPitImpl(0,0,"Player 1"), ""),	
				Pair.with(new PlayerPitImpl(1,0,"Player 1"), ""),Pair.with(new PlayerPitImpl(2,0,"Player 1"), ""),
				Pair.with(new PlayerPitImpl(3,0,"Player 1"), ""),Pair.with(new PlayerPitImpl(4,0,"Player 1"), ""),
				Pair.with(new PlayerPitImpl(5,1,"Player 1"), ""),Pair.with(new PlayerPitImpl(6,10,"Player 1"), ""),
				Pair.with(new PlayerPitImpl(7,20,"Player 1"), ""),Pair.with(new PlayerPitImpl(8,0,"Player 2"), ""),
				Pair.with(new PlayerPitImpl(9,2,"Player 2"), ""),Pair.with(new PlayerPitImpl(10,0,"Player 2"), ""),
				Pair.with(new PlayerPitImpl(11,0,"Player 2"), ""),Pair.with(new PlayerPitImpl(12,0,"Player 2"), ""),
				Pair.with(new PlayerPitImpl(13,20,"Player 2"), ""));
				
			
			Assert.assertTrue(gameStateActions.gameOverControl(player1ToFinish));	
			Assert.assertTrue(gameStateActions.gameOverControl(player2ToFinish));
			Assert.assertFalse(gameStateActions.gameOverControl(nobodyFinishes));
	
	}
	
	@Test
	public void testPlayerCannotDepositToOpponentStore() throws Exception {
		
		// this test will check if a player can not deposit on opponent's store condition is satisfied
		this.mockMvc.perform(get("/new"));
		
		List<Pit> pitList = Arrays.asList(new PlayerPitImpl(0,15,"Player 1"),	
				new PlayerPitImpl(1,0,"Player 1"),new PlayerPitImpl(2,0,"Player 1"),
				new PlayerPitImpl(3,0,"Player 1"),new PlayerPitImpl(4,0,"Player 1"),
				new PlayerPitImpl(5,0,"Player 1"),new PlayerPitImpl(6,10,"Player 1"),
				new PlayerPitImpl(7,10,"Player 1"),new PlayerPitImpl(8,0,"Player 2"),
				new PlayerPitImpl(9,2,"Player 2"),new PlayerPitImpl(10,0,"Player 2"),
				new PlayerPitImpl(11,0,"Player 2"),new PlayerPitImpl(12,0,"Player 2"),
				new PlayerPitImpl(13,0,"Player 2"));
		
		List<Pair<?, ?>> pitStateMachinePairList = pitStateActions.getPitStateMachinePairList();
		List<Pair<?, ?>> originalPitStateMachinePairList = pitStateMachinePairList;

		for (Pit pit : pitList) {
			pitStateMachinePairList.set(pit.getId(),
					Pair.with(pit, pitStateActions.getPitStateMachinePairList().get(pit.getId()).getValue1()));
			pitStateActions.setPitStateMachinePairList(pitStateMachinePairList);
		}

		this.mockMvc.perform(get("/withdraw/{id}", 0)).andExpect(jsonPath("$.pitList[13].pieceCount").value(0));

		pitStateActions.setPitStateMachinePairList(originalPitStateMachinePairList);
		this.mockMvc.perform(get("/endGame"));
		
	}
	
	
	@Test
	public void testOnePieceAtOwnPitTakesOpponentPits() throws Exception {
		
		// this test will check if a player's move ends in his own pit with only one piece, that pit and the opposite pit of oppenent are both withdrawn to player's store
		this.mockMvc.perform(get("/new"));
		
		this.mockMvc.perform(get("/withdraw/{id}",3));
		
		List<Pit> pitList = Arrays.asList(new PlayerPitImpl(0,3,"Player 1"),	
				new PlayerPitImpl(1,2,"Player 1"),new PlayerPitImpl(2,2,"Player 1"),
				new PlayerPitImpl(3,0,"Player 1"),new PlayerPitImpl(4,2,"Player 1"),
				new PlayerPitImpl(5,2,"Player 1"),new PlayerPitImpl(6,1,"Player 1"),
				new PlayerPitImpl(7,2,"Player 1"),new PlayerPitImpl(8,2,"Player 2"),
				new PlayerPitImpl(9,9,"Player 2"),new PlayerPitImpl(10,2,"Player 2"),
				new PlayerPitImpl(11,2,"Player 2"),new PlayerPitImpl(12,2,"Player 2"),
				new PlayerPitImpl(13,0,"Player 2"));
		
		List<Pair<?, ?>> pitStateMachinePairList = pitStateActions.getPitStateMachinePairList();
		List<Pair<?, ?>> originalPitStateMachinePairList = pitStateMachinePairList;
		
		for (Pit pit : pitList) {
			pitStateMachinePairList.set(pit.getId(), Pair.with(pit, pitStateActions.getPitStateMachinePairList().get(pit.getId()).getValue1()));
			pitStateActions.setPitStateMachinePairList(pitStateMachinePairList);
		}
		
		this.mockMvc.perform(get("/withdraw/{id}",0))
		.andExpect(jsonPath("$.pitList[6].pieceCount").value(11));
		pitStateActions.setPitStateMachinePairList(originalPitStateMachinePairList);
		this.mockMvc.perform(get("/endGame"));
		
	}
	
}
