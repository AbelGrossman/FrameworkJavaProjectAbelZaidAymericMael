package fr.pantheonsorbonne.ufr27.miage.dao;

import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.dto.UserWithMmr;
import fr.pantheonsorbonne.ufr27.miage.model.Team;
import fr.pantheonsorbonne.ufr27.miage.gateway.TeamGateway;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class TeamDAO {

    @Inject
    EntityManager entityManager;

    @Inject
    TeamGateway teamGateway;
    
    @POST
    @Path("/addTeamToDatabase")
    @Transactional
    public Response addTeamToDatabase(List<UserWithMmr> users) {
        int sumMmr=0;
        for(int i=0;i<users.size();i++){
            sumMmr+=users.get(i).mmr();
        }
        int meanMmmr = sumMmr/users.size();
        String difficulty="";
        if (meanMmmr<2000){
            difficulty= "easy";
        }
        else if(meanMmmr<3000){
            difficulty= "medium";
        }
        else{
            difficulty= "hard";
        }
        List<Long> userIds = users.stream()
            .map(UserWithMmr::playerId)
            .toList();
        Team team = new Team(users.get(0).theme(), userIds, difficulty);
        entityManager.persist(team);
        teamGateway.sendTeamToCreationPartie(team);
        return Response.ok().build();
    }
}
