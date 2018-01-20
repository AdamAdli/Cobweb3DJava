package cobweb3d.plugins.mutators;

import java.util.Collection;
import java.util.Collections;


public interface LoggingMutator extends AgentMutator {

    Collection<String> NO_DATA = Collections.emptyList();

    Collection<String> logDataAgent(int agentType);

    Collection<String> logDataTotal();

    Collection<String> logHeadersAgent();

    Collection<String> logHeaderTotal();
}
