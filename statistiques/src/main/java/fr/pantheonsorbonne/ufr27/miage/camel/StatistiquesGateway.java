package fr.pantheonsorbonne.ufr27.miage.camel;

import fr.pantheonsorbonne.ufr27.miage.dto.PartieDetails;
import org.apache.camel.ProducerTemplate;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class StatistiquesGateway {

    @Inject
    ProducerTemplate producerTemplate;

    public void sendStatistiquesUpdate(PartieDetails partieDetails) {
        producerTemplate.sendBody("direct:sendStatistiques", partieDetails);
    }
}
