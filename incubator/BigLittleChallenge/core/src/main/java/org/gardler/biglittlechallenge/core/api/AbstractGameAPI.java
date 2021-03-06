package org.gardler.biglittlechallenge.core.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.gardler.biglittlechallenge.core.AbstractGame;
import org.gardler.biglittlechallenge.core.api.model.GameStatusResponse;
import org.gardler.biglittlechallenge.core.api.model.GameTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractGameAPI {
	private static Logger logger = LoggerFactory.getLogger(AbstractGameAPI.class);
	
	private AbstractGame game;
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	public AbstractGameAPI(AbstractGame game) {
		logger.debug("Creating game api server for " + game.toString());
		this.game = game;
	}
	
	@Path("join")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON})
    @Consumes({ MediaType.APPLICATION_JSON})
	public GameStatusResponse postJoinGame(GameTicket ticket) {
		logger.info("Player requested to join game: " + ticket.getPlayerName());
		
		if (game.addTicket(ticket)) {
			return getStatus();
		} else {
			logger.error("FIXME: return an HTTP response that says \"not available\", player should find a different engine.");
			return null;
		}
	}
	
	@Path("status")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public GameStatusResponse getStatus() {
		GameStatusResponse response = new GameStatusResponse(game.getStatus());
		return response;
	}

	@Path("abort")
    @PUT
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED})
	public void abortGame(String id) {
		logger.debug("Request to abort game with ID " + id);
		if (id.equals(game.getID())) {
			game.abortGame();
		} else {
			logger.error("FIXME: handle situation where id of game to abort is not the ID of the current game. Game ID is '" + game.getID() + "', requested abort of '" + id + "'");
		}
	}

}
