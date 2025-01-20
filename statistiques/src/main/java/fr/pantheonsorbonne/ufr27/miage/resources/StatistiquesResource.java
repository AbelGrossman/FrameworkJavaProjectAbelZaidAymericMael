package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.dao.StatistiquesDAO;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesJoueur;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesParTheme;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/stats")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StatistiquesResource {

    @Inject
    StatistiquesDAO statistiquesDAO;

    @GET
    @Path("/{userId}")
    public StatistiquesJoueur getStatistiquesGlobales(@PathParam("userId") String userId) {
        StatistiquesJoueur stats = statistiquesDAO.getStatistiquesJoueur(userId);
        if (stats == null) {
            throw new NotFoundException("Aucune statistique globale trouvée pour l'utilisateur : " + userId);
        }
        return stats;
    }

    @GET
    @Path("/{userId}/theme/{theme}")
    public StatistiquesParTheme getStatistiquesParTheme(@PathParam("userId") String userId, @PathParam("theme") String theme) {
        StatistiquesParTheme stats = statistiquesDAO.getStatistiquesParTheme(userId, theme);
        if (stats == null) {
            throw new NotFoundException("Aucune statistique trouvée pour l'utilisateur " + userId + " et le thème " + theme);
        }
        return stats;
    }

}
