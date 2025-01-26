package fr.pantheonsorbonne.ufr27.miage.exception;

public class JoinRequestNotFoundException  extends Exception{
    public JoinRequestNotFoundException(){
        super("You need to join a game before canceling");
    }
}
