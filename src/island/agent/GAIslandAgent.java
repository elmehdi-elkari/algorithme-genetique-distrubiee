package island.agent;

import island.utils.GeneticAlgorithm;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class GAIslandAgent extends Agent {

    DFAgentDescription dfAgentDescription=new DFAgentDescription();
    ServiceDescription serviceDescription=new ServiceDescription();
    private TickerBehaviour tickerBehaviour;

    private GeneticAlgorithm ga;
    protected void setup() {

        dfAgentDescription=new DFAgentDescription();
        dfAgentDescription.setName(getAID());
        serviceDescription=new ServiceDescription();
        serviceDescription.setType("GAisAgent");
        serviceDescription.setName("GA");
        dfAgentDescription.addServices(serviceDescription);

        tickerBehaviour = new MyTickerBehaviour(this, 1000);
        addBehaviour(tickerBehaviour);

        ga = new GeneticAlgorithm();


        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage receivedMSG = receive();
                if(receivedMSG!=null){
                    switch (receivedMSG.getContent()){
                        case "done": takeDown();break;
                    }
                }else {
                    block();
                }
            }
        });


    }
    @Override
    protected void takeDown() {
        try {
            System.out.println("done");
            removeBehaviour(tickerBehaviour);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean shouldExchangeBestIndividuals() {
        int exchangeInterval = 10;
        return ga.getPopulation().size() % exchangeInterval == 0;
    }

    private void sendBestIndividual() {

        String bestIndividual = ga.getBestIndividual();
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setContent(this.getLocalName()+"-"+bestIndividual+"-"+String.valueOf(ga.fitness(bestIndividual)));
        AID receiver = new AID("masterAgent", AID.ISLOCALNAME);
        message.addReceiver(receiver);
        send(message);
    }


    private class MyTickerBehaviour extends TickerBehaviour {
        int it= 0;
        public MyTickerBehaviour(Agent agent, long period) {
            super(agent, period);
        }

        protected void onTick() {
            ga.run();
            if (shouldExchangeBestIndividuals()) {
                sendBestIndividual();
            }
        }
    }
}
