package island.agent;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class MasterAgent extends Agent {

    SequentialBehaviour sequentialBehaviour=new SequentialBehaviour();
    DFAgentDescription dfAgentDescription=new DFAgentDescription();
    ServiceDescription serviceDescription=new ServiceDescription();

    @Override
    protected void setup() {
        serviceDescription.setType("GAisAgent");
        dfAgentDescription.addServices(serviceDescription);

        sequentialBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage message=receive();
                if(message!=null){

                    String content = message.getContent();
                    String[] result = content.split("-");
                    System.out.println("Best Ind of "+result[0]+" : "+result[1] + " Fitness : "+result[2]);
                    if(content.equals("helloworld")){
                        ACLMessage replyMsg=message.createReply();
                        replyMsg.setContent("done");
                        send(replyMsg);
                    }

                }else {
                    block();
                }
            }
        });
        addBehaviour(sequentialBehaviour);
    }
}
