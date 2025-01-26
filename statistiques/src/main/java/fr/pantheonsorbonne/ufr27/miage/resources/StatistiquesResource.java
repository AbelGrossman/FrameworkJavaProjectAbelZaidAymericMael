package fr.pantheonsorbonne.ufr27.miage.resources;

import fr.pantheonsorbonne.ufr27.miage.dao.StatistiquesDAO;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesJoueur;
import fr.pantheonsorbonne.ufr27.miage.model.StatistiquesParTheme;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/stats")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StatistiquesResource {

    @Inject
    StatistiquesDAO statistiquesDAO;

    @GET
    @Path("/{playerId}")
    public StatistiquesJoueur getStatistiquesGlobales(@PathParam("playerId") Long playerId) {
        StatistiquesJoueur stats = statistiquesDAO.getStatistiquesJoueur(playerId);
        if (stats == null) {
            throw new NotFoundException("Aucune statistique globale trouvée pour l'utilisateur : " + playerId);
        }
        return stats;
    }

    @GET
    @Path("/{playerId}/theme/{theme}")
    public StatistiquesParTheme getStatistiquesParTheme(@PathParam("playerId") Long playerId, @PathParam("theme") String theme) {
        StatistiquesParTheme stats = statistiquesDAO.getStatistiquesParTheme(playerId, theme);
        if (stats == null) {
            throw new NotFoundException("Aucune statistique trouvée pour l'utilisateur " + playerId + " et le thème " + theme);
        }
        return stats;
    }

}
