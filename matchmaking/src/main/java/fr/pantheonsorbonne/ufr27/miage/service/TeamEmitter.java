package fr.pantheonsorbonne.ufr27.miage.service;
import fr.pantheonsorbonne.ufr27.miage.dto.UserWithMmr;
import fr.pantheonsorbonne.ufr27.miage.model.Team;

import java.util.List;

import org.apache.camel.ProducerTemplate;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TeamEmitter {

    @Inject
    ProducerTemplate producerTemplate;

    @Inject
    EntityManager entityManager;

    @POST
    @Path("/sendTeamToCreationPartie")
    @Transactional
    public Response sendTeamToCreationPartie(List<UserWithMmr> users) {
            int sumMmr=0;
            for(int i=0;i<users.size();i++){
                sumMmr+=users.get(i).getMmr();
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
            Team team = new Team(users.get(0).getTheme(), users, difficulty);
            entityManager.persist(team);
            producerTemplate.sendBody("direct:newTeam", team);
            return Response.ok().build();
        }
}

