package com.kalyoncu.mert.mancala.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class MancalaControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@AfterEach
	public void end() throws Exception {
		this.mockMvc.perform(get("/endGame"));
	}
	
	@Test
	public void testStartGameAction() throws Exception {
		this.mockMvc.perform(get("/new"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.*", Matchers.hasSize(3)))
		
		.andExpect(jsonPath("$.nextPlayer").value("Player 1"))
		.andExpect(jsonPath("$.gameOver").value(false))
		
		.andExpect(jsonPath("$.pitList[0].id").value(0))
		.andExpect(jsonPath("$.pitList[0].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[0].player").value("Player 1"))
		
		.andExpect(jsonPath("$.pitList[1].id").value(1))
		.andExpect(jsonPath("$.pitList[1].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[1].player").value("Player 1"))
		
		.andExpect(jsonPath("$.pitList[2].id").value(2))
		.andExpect(jsonPath("$.pitList[2].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[2].player").value("Player 1"))
		
		.andExpect(jsonPath("$.pitList[3].id").value(3))
		.andExpect(jsonPath("$.pitList[3].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[3].player").value("Player 1"))
		
		.andExpect(jsonPath("$.pitList[4].id").value(4))
		.andExpect(jsonPath("$.pitList[4].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[4].player").value("Player 1"))
		
		.andExpect(jsonPath("$.pitList[5].id").value(5))
		.andExpect(jsonPath("$.pitList[5].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[5].player").value("Player 1"))
		
		.andExpect(jsonPath("$.pitList[6].id").value(6))
		.andExpect(jsonPath("$.pitList[6].pieceCount").value(0))
		.andExpect(jsonPath("$.pitList[6].player").value("Player 1"))
		
		.andExpect(jsonPath("$.pitList[7].id").value(7))
		.andExpect(jsonPath("$.pitList[7].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[7].player").value("Player 2"))
		
		.andExpect(jsonPath("$.pitList[8].id").value(8))
		.andExpect(jsonPath("$.pitList[8].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[8].player").value("Player 2"))
		
		.andExpect(jsonPath("$.pitList[9].id").value(9))
		.andExpect(jsonPath("$.pitList[9].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[9].player").value("Player 2"))
		
		.andExpect(jsonPath("$.pitList[10].id").value(10))
		.andExpect(jsonPath("$.pitList[10].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[10].player").value("Player 2"))
		
		.andExpect(jsonPath("$.pitList[11].id").value(11))
		.andExpect(jsonPath("$.pitList[11].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[11].player").value("Player 2"))
		
		.andExpect(jsonPath("$.pitList[12].id").value(12))
		.andExpect(jsonPath("$.pitList[12].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[12].player").value("Player 2"))
		
		.andExpect(jsonPath("$.pitList[13].id").value(13))
		.andExpect(jsonPath("$.pitList[13].pieceCount").value(0))
		.andExpect(jsonPath("$.pitList[13].player").value("Player 2"));

	}
	
	
	@Test
	public void testWithdraw() throws Exception {
		
		this.mockMvc.perform(get("/new"));
		
		this.mockMvc.perform(get("/withdraw/{id}",1))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.*", Matchers.hasSize(3)))
		
		.andExpect(jsonPath("$.gameOver").value(false))
		.andExpect(jsonPath("$.nextPlayer").value("Player 2"))
		
		.andExpect(jsonPath("$.pitList[0].id").value(0))
		.andExpect(jsonPath("$.pitList[0].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[0].player").value("Player 1"))
		
		.andExpect(jsonPath("$.pitList[1].id").value(1))
		.andExpect(jsonPath("$.pitList[1].pieceCount").value(0))
		.andExpect(jsonPath("$.pitList[1].player").value("Player 1"))
		
		.andExpect(jsonPath("$.pitList[2].id").value(2))
		.andExpect(jsonPath("$.pitList[2].pieceCount").value(7))
		.andExpect(jsonPath("$.pitList[2].player").value("Player 1"))
		
		.andExpect(jsonPath("$.pitList[3].id").value(3))
		.andExpect(jsonPath("$.pitList[3].pieceCount").value(7))
		.andExpect(jsonPath("$.pitList[3].player").value("Player 1"))
		
		.andExpect(jsonPath("$.pitList[4].id").value(4))
		.andExpect(jsonPath("$.pitList[4].pieceCount").value(7))
		.andExpect(jsonPath("$.pitList[4].player").value("Player 1"))
		
		.andExpect(jsonPath("$.pitList[5].id").value(5))
		.andExpect(jsonPath("$.pitList[5].pieceCount").value(7))
		.andExpect(jsonPath("$.pitList[5].player").value("Player 1"))
		
		.andExpect(jsonPath("$.pitList[6].id").value(6))
		.andExpect(jsonPath("$.pitList[6].pieceCount").value(1))
		.andExpect(jsonPath("$.pitList[6].player").value("Player 1"))
		
		.andExpect(jsonPath("$.pitList[7].id").value(7))
		.andExpect(jsonPath("$.pitList[7].pieceCount").value(7))
		.andExpect(jsonPath("$.pitList[7].player").value("Player 2"))
		
		.andExpect(jsonPath("$.pitList[8].id").value(8))
		.andExpect(jsonPath("$.pitList[8].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[8].player").value("Player 2"))
		
		.andExpect(jsonPath("$.pitList[9].id").value(9))
		.andExpect(jsonPath("$.pitList[9].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[9].player").value("Player 2"))
		
		.andExpect(jsonPath("$.pitList[10].id").value(10))
		.andExpect(jsonPath("$.pitList[10].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[10].player").value("Player 2"))
		
		.andExpect(jsonPath("$.pitList[11].id").value(11))
		.andExpect(jsonPath("$.pitList[11].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[11].player").value("Player 2"))
		
		.andExpect(jsonPath("$.pitList[12].id").value(12))
		.andExpect(jsonPath("$.pitList[12].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[12].player").value("Player 2"))
		
		.andExpect(jsonPath("$.pitList[13].id").value(13))
		.andExpect(jsonPath("$.pitList[13].pieceCount").value(0))
		.andExpect(jsonPath("$.pitList[13].player").value("Player 2"));
		
}
	
	@Test
	public void testReplayGame() throws Exception {
		this.mockMvc.perform(get("/new"));
		this.mockMvc.perform(get("/replayGame"))
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.*", Matchers.hasSize(3)))
		
		.andExpect(jsonPath("$.nextPlayer").value("Player 1"))
		.andExpect(jsonPath("$.gameOver").value(false))
		
		.andExpect(jsonPath("$.pitList[0].id").value(0))
		.andExpect(jsonPath("$.pitList[0].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[0].player").value("Player 1"))
		
		.andExpect(jsonPath("$.pitList[1].id").value(1))
		.andExpect(jsonPath("$.pitList[1].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[1].player").value("Player 1"))
		
		.andExpect(jsonPath("$.pitList[2].id").value(2))
		.andExpect(jsonPath("$.pitList[2].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[2].player").value("Player 1"))
		
		.andExpect(jsonPath("$.pitList[3].id").value(3))
		.andExpect(jsonPath("$.pitList[3].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[3].player").value("Player 1"))
		
		.andExpect(jsonPath("$.pitList[4].id").value(4))
		.andExpect(jsonPath("$.pitList[4].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[4].player").value("Player 1"))
		
		.andExpect(jsonPath("$.pitList[5].id").value(5))
		.andExpect(jsonPath("$.pitList[5].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[5].player").value("Player 1"))
		
		.andExpect(jsonPath("$.pitList[6].id").value(6))
		.andExpect(jsonPath("$.pitList[6].pieceCount").value(0))
		.andExpect(jsonPath("$.pitList[6].player").value("Player 1"))
		
		.andExpect(jsonPath("$.pitList[7].id").value(7))
		.andExpect(jsonPath("$.pitList[7].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[7].player").value("Player 2"))
		
		.andExpect(jsonPath("$.pitList[8].id").value(8))
		.andExpect(jsonPath("$.pitList[8].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[8].player").value("Player 2"))
		
		.andExpect(jsonPath("$.pitList[9].id").value(9))
		.andExpect(jsonPath("$.pitList[9].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[9].player").value("Player 2"))
		
		.andExpect(jsonPath("$.pitList[10].id").value(10))
		.andExpect(jsonPath("$.pitList[10].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[10].player").value("Player 2"))
		
		.andExpect(jsonPath("$.pitList[11].id").value(11))
		.andExpect(jsonPath("$.pitList[11].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[11].player").value("Player 2"))
		
		.andExpect(jsonPath("$.pitList[12].id").value(12))
		.andExpect(jsonPath("$.pitList[12].pieceCount").value(6))
		.andExpect(jsonPath("$.pitList[12].player").value("Player 2"))
		
		.andExpect(jsonPath("$.pitList[13].id").value(13))
		.andExpect(jsonPath("$.pitList[13].pieceCount").value(0))
		.andExpect(jsonPath("$.pitList[13].player").value("Player 2"));
		
	}
}
