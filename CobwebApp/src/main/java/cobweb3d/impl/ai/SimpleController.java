package cobweb3d.impl.ai;

import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.agent.Controller;
import cobweb3d.core.agent.ControllerListener;
import cobweb3d.impl.agent.Agent;
import util.RandomNoGenerator;

public class SimpleController implements Controller {

    RandomNoGenerator random;

    public SimpleController() {
        random = new RandomNoGenerator();
    }

    @Override
    public void controlAgent(BaseAgent agent, ControllerListener inputCallback) {
        if (agent instanceof Agent) {
            Agent theAgent = (Agent) agent;
            System.out.println("Update 1.2.1 - " + theAgent.id());
            int action = random.nextIntRange(1, 10);

            switch (action) {
                case 1: //xPos
                    System.out.println("Left - " + theAgent.id());
                    theAgent.turnLeft();
                    break;
                case 2:
                    System.out.println("Right - " + theAgent.id());
                    theAgent.turnRight();
                    break;
                case 3:
                    System.out.println("Up - " + theAgent.id());
                    theAgent.turnUp();
                    break;
                case 4:
                    System.out.println("Down - " + theAgent.id());
                    theAgent.turnDown();
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    System.out.println("Step - " + theAgent.id() + " " + theAgent.position.toString());
                    theAgent.step();
                    break;
            }
            System.out.println("Update 1.2.2 - " + theAgent.id());
        }
    }

    @Override
    public Controller createChildAsexual() {
        return new SimpleController();
    }

    @Override
    public Controller createChildSexual(Controller parent2) {
        return new SimpleController();
    }
}
