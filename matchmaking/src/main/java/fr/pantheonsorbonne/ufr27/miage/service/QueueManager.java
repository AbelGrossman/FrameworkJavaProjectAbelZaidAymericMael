package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.dto.UserWithMmr;
import fr.pantheonsorbonne.ufr27.miage.model.Queue;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import jakarta.ws.rs.*;


@Path("/queue")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class QueueManager {

    @Inject
    TeamEmitter teamEmitter;


    private final Map<String, Queue> queues = new HashMap<>();

    private final Map<String, Long> lastTeamFormedTime = new HashMap<>();

    private final int mmrAdjustmentInterval= 10_000;


    public QueueManager(TeamEmitter teamEmitter) {
        this.teamEmitter = teamEmitter;
    }
    
    public Queue getOrCreateQueue(String theme) {
        Queue queue = queues.get(theme);
        if (queue == null) {
            queue = new Queue(theme);
            queues.put(theme, queue);
        }
        return queue;
    }

    @POST
    @Path("/{theme}createQueue")
    public Response createQueue(@PathParam("theme") String theme) {
        Queue queue = queues.get(theme);
        if (queue != null) {
            return Response.status(Response.Status.CONFLICT).build();
        } 
        queue = new Queue(theme);
        queues.put(theme, queue);
        return Response.ok(queue).build();
    }

    @GET
    @Path("/{theme}getQueue")
    public Response getQueue(@PathParam("theme") String theme) {
        Queue queue = queues.get(theme);
        if (queue == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(queue).build();
    }

    @POST
    @Path("/{theme}/{userId}/{userMmr}/{userTheme}/addPlayerToQueueMapping")
    public Response addPlayerToQueueMapping(@PathParam("userId") Long userId, @PathParam("userMmr") int userMmr, @PathParam("userTheme") String userTheme) {
        UserWithMmr user = new UserWithMmr(userId, userTheme, userMmr);
        Queue queue = getOrCreateQueue(user.theme());
        // synchronized (queue) {
            queue.addPlayer(user);
        // }
        return Response.ok().build();
    }

    public Response addPlayerToQueue(UserWithMmr user) {
        Queue queue = getOrCreateQueue(user.theme());
        // synchronized (queue) {
            queue.addPlayer(user);
        // }
        return Response.ok().build();
    } 

    @POST
    @Path("/{theme}/formTeams")
    public Response formTeams(@PathParam("theme") String theme) {
        Queue queue = getOrCreateQueue(theme);
    
        // synchronized (queue) {
            List<UserWithMmr> players = queue.getPlayers();
            players.sort(Comparator.comparingInt(UserWithMmr::mmr));

            List<List<UserWithMmr>> teams = new ArrayList<>();
            List<UserWithMmr> currentTeam = new ArrayList<>();
            boolean teamFormed = false;
    
            for (UserWithMmr player : players) {
                if (currentTeam.size()<6 && (currentTeam.isEmpty() || 
                    player.mmr() - currentTeam.get(0).mmr() <= queue.getAllowedMmrDifference())) {
                    currentTeam.add(player);
                } else {
                    if (currentTeam.size() == 6) {
                        teams.add(new ArrayList<>(currentTeam));
                        players.removeAll(currentTeam);
                        teamFormed = true;
                    }
                    currentTeam.clear();
                    currentTeam.add(player);
                }
            }
    
            // Final check for the last team
            if (currentTeam.size() == 6) {
                teams.add(new ArrayList<>(currentTeam));
                players.removeAll(currentTeam);
                teamFormed = true;
            }
    
            // Emit all formed teams to creation-partie
            for (List<UserWithMmr> team : teams) {
                teamEmitter.sendTeamToCreationPartie(team);
            }
    
            if (teamFormed) {
                // Reset allowed MMR difference and update last team formation time
                queue.setAllowedMmrDifference(20);
                lastTeamFormedTime.put(theme, System.currentTimeMillis());
            }
        // }
        return Response.ok(teams).build();
    }

    public Response adjustMmrDifferencePeriodically() {
        long currentTime = System.currentTimeMillis();

        for (Map.Entry<String, Queue> entry : queues.entrySet()) {
            String theme = entry.getKey();
            Queue queue = entry.getValue();

            synchronized (queue) {
                long lastFormedTime = lastTeamFormedTime.getOrDefault(theme, 0L);

                // If no teams were formed in the last interval, increase allowed MMR difference
                if (currentTime - lastFormedTime >= mmrAdjustmentInterval) {
                    queue.setAllowedMmrDifference(queue.getAllowedMmrDifference() + 20);
                    lastTeamFormedTime.put(theme, currentTime);
                }
            }
        }
        return Response.ok().build();
    }

    public List<String> getThemes() {
        return new ArrayList<>(queues.keySet());
    }
}