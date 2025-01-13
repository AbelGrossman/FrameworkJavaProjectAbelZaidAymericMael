package fr.pantheonsorbonne.ufr27.miage.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import fr.pantheonsorbonne.ufr27.miage.dto.LobbyDTO;
import fr.pantheonsorbonne.ufr27.miage.model.Lobby;
import fr.pantheonsorbonne.ufr27.miage.service.LobbyService;
import jakarta.inject.Inject;

@Path("/lobby")
public class LobbyResource {
    @Inject
    LobbyService lobbyService;

    @POST
    @Path("/create")
    @Consumes("application/json")
    @Produces("application/json")
    public Response createLobby(LobbyDTO lobbyDTO) {
        Lobby lobby = lobbyService.createLobby(lobbyDTO.theme(), lobbyDTO.level(), lobbyDTO.players());
        return Response.ok(lobby).build();
    }
}
