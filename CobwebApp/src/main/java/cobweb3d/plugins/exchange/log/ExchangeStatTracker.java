package cobweb3d.plugins.exchange.log;

import cobweb3d.core.agent.BaseAgent;
import cobweb3d.impl.agent.Agent;
import cobweb3d.impl.stats.excel.BaseStatsProvider;
import cobweb3d.plugins.exchange.ExchangeParams;
import cobweb3d.plugins.exchange.ExchangeState;

public class ExchangeStatTracker {

    public static float getTotalX(BaseStatsProvider statsProvider) {
        float totalX = 0;
        for (BaseAgent a : statsProvider.getAgents()) {
            if (a instanceof Agent) {
                ExchangeState state = ((Agent) a).getState(ExchangeState.class);
                if (state != null) {
                    totalX += state.x;
                }
            }
        }
        return totalX;
    }

    public static float getTotalY(BaseStatsProvider statsProvider) {
        float totalY = 0;
        for (BaseAgent a : statsProvider.getAgents()) {
            if (a instanceof Agent) {
                ExchangeState state = ((Agent) a).getState(ExchangeState.class);
                if (state != null) {
                    totalY += state.y;
                }
            }
        }
        return totalY;
    }

    public static float getTotalUtility(BaseStatsProvider statsProvider, ExchangeParams exchangeParams) {
        float totalU = 0;
        for (BaseAgent a : statsProvider.getAgents()) {
            if (a instanceof Agent) {
                ExchangeState state = ((Agent) a).getState(ExchangeState.class);
                if (state != null) {
                    totalU += exchangeParams.getAgentParams(a).calculateU(state);
                }
            }
        }
        return totalU;
    }

    public static float getUtilityForAgent(BaseStatsProvider statsProvider, ExchangeParams exchangeParams, int type) {
        float totalU = 0;
        for (BaseAgent a : statsProvider.getAgents()) {
            if (a.getType() == type && a instanceof Agent) {
                ExchangeState state = ((Agent) a).getState(ExchangeState.class);
                if (state != null) {
                    totalU += exchangeParams.getAgentParams(a).calculateU(state);
                }
            }
        }
        return totalU;
    }
}
