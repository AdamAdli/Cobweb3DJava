package cobweb3d.plugins.exchange;

import cobweb3d.core.SimulationTimeSpace;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.impl.logging.SmartDataTable;
import cobweb3d.impl.stats.excel.BaseStatsProvider;
import cobweb3d.plugins.exchange.log.ExchangeDataLogger;
import cobweb3d.plugins.mutators.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ExchangeMutator extends StatefulMutatorBase<ExchangeState> implements ContactMutator,
        LoggingMutator, SpawnMutator, ExcelLoggingMutator, DataLoggingMutator {
    ExchangeParams params;

    private SimulationTimeSpace simulation;
    private ExchangeDataLogger exchangeLogger;

    public ExchangeMutator() {
        super(ExchangeState.class);
    }

    public void setParams(SimulationTimeSpace sim, ExchangeParams exchangeParams, int agentTypes) {
        this.simulation = sim;
        this.params = exchangeParams;
        this.exchangeLogger = new ExchangeDataLogger(params);
    }

    @Override
    public Collection<String> logDataAgent(int agentType) {
        List<String> l = new LinkedList<String>();

        return l;
    }

    @Override
    public Collection<String> logDataTotal() {
        List<String> l = new LinkedList<String>();
        int sum = 0;

        l.add(Integer.toString(sum));
        return l;
    }

    @Override
    public Collection<String> logHeadersAgent() {
        List<String> header = new LinkedList<String>();
        header.add("Exchanges");
        return header;
    }

    @Override
    public Collection<String> logHeaderTotal() {
        List<String> header = new LinkedList<String>();
        header.add("Exchanges");
        return header;
    }

    private void tryExchange(BaseAgent agent, BaseAgent other) {
        ExchangeAgentPairParams pairParams = params.getPairParams(agent.getType(), other.getType());
        if (pairParams.quantXTransfer != 0 || pairParams.quantYTransfer != 0) {
            ExchangeAgentParams agentOneParams;// = params.of(agent);
            ExchangeAgentParams agentTwoParams;// = params.of(other);
            ExchangeState agentOne;// = agent.getType() == pairParams.typeOne ? getAgentState(agent) : getAgentState(other);
            ExchangeState agentTwo;// = agent.getType() == pairParams.typeTwo ? getAgentState(agent) : getAgentState(other);

            if (agent.getType() == pairParams.typeOne) {
                agentOneParams = params.of(agent);
                agentTwoParams = params.of(other);
                agentOne = getAgentState(agent);
                agentTwo = getAgentState(other);

            } else {
                agentOneParams = params.of(other);
                agentTwoParams = params.of(agent);
                agentOne = getAgentState(other);
                agentTwo = getAgentState(agent);
            }

            if (pairParams.dynParams.enabled)
                tryDynamicExchange(agentOne, agentOneParams, agentTwo, agentTwoParams, pairParams);
            else trySimpleExchange(agentOne, agentOneParams, agentTwo, agentTwoParams, pairParams);
        }
    }

    boolean trySimpleExchange(ExchangeState agentOne, ExchangeAgentParams agentOneParams, ExchangeState agentTwo, ExchangeAgentParams agentTwoParams,
                              ExchangeAgentPairParams pairParams) {
        float agentOnePreUtil = agentOneParams.calculateU(agentOne);
        float agentTwoPreUtil = agentTwoParams.calculateU(agentTwo);

        float xTransfer = pairParams.quantXTransfer;
        float yTransfer = pairParams.quantYTransfer;

        // Try giving X from agent one to two.
        float agentOnePostUtil = agentOneParams.calculateU(agentOne.x - xTransfer, agentOne.y + yTransfer, agentOneParams.utilityFunctionParam.varA, agentOneParams.utilityFunctionParam.varB);
        float agentTwoPostUtil = agentTwoParams.calculateU(agentTwo.x + xTransfer, agentTwo.y - yTransfer, agentTwoParams.utilityFunctionParam.varA, agentTwoParams.utilityFunctionParam.varB);

        // If both benefit (post-trade utility is higher than pre-trade utility), trade and exchange complete!
        if (agentOnePostUtil > agentOnePreUtil && agentTwoPostUtil > agentTwoPreUtil) {
            agentOne.x -= xTransfer;
            agentOne.y += yTransfer;
            agentTwo.x += xTransfer;
            agentTwo.y -= yTransfer;
            // System.out.println("First Exchange offeredX: " + offeredX + " returnedY: " + returnedY);
            return true;
        }

        agentOnePostUtil = agentOneParams.calculateU(agentOne.x + xTransfer, agentOne.y - yTransfer, agentOneParams.utilityFunctionParam.varA, agentOneParams.utilityFunctionParam.varB);
        agentTwoPostUtil = agentTwoParams.calculateU(agentTwo.x - xTransfer, agentTwo.y + yTransfer, agentTwoParams.utilityFunctionParam.varA, agentTwoParams.utilityFunctionParam.varB);

        // If both benefit (post-trade utility is higher than pre-trade utility), trade and exchange complete!
        if (agentOnePostUtil > agentOnePreUtil && agentTwoPostUtil > agentTwoPreUtil) {
            agentOne.x += xTransfer;
            agentOne.y -= yTransfer;
            agentTwo.x -= xTransfer;
            agentTwo.y += yTransfer;
            // System.out.println("First Exchange offeredX: " + offeredX + " returnedY: " + returnedY);
            return true;
        }

        // We could not find a suitable y to return for mutually beneficial transaction both ways.
        return false;
    }

    boolean tryDynamicExchange(ExchangeState agentOne, ExchangeAgentParams agentOneParams, ExchangeState agentTwo, ExchangeAgentParams agentTwoParams,
                               ExchangeAgentPairParams pairParams) {
        float agentOnePreUtil = agentOneParams.calculateU(agentOne);
        float agentTwoPreUtil = agentTwoParams.calculateU(agentTwo);
        float lowerBound = pairParams.dynParams.lowerBound;
        float upperBound = pairParams.dynParams.upperBound;
        if (pairParams.dynParams.increment <= 0) pairParams.dynParams.increment = 1;
        int steps = Math.round((upperBound - lowerBound) / pairParams.dynParams.increment);

        float offeredX = pairParams.quantXTransfer;
        for (int i = 0; i < steps; i++) {
            float returnedY = lowerBound + (i * pairParams.dynParams.increment);
            // Calculate the utilities after this hypothetical trade.
            float agentOnePostUtil = agentOneParams.calculateU(agentOne.x - offeredX, agentOne.y + returnedY, agentOneParams.utilityFunctionParam.varA, agentOneParams.utilityFunctionParam.varB);
            float agentTwoPostUtil = agentTwoParams.calculateU(agentTwo.x + offeredX, agentTwo.y - returnedY, agentTwoParams.utilityFunctionParam.varA, agentTwoParams.utilityFunctionParam.varB);

            // If both benefit (post-trade utility is higher than pre-trade utility), trade and exchange complete!
            if (agentOnePostUtil > agentOnePreUtil && agentTwoPostUtil > agentTwoPreUtil) {
                agentOne.x -= offeredX;
                agentOne.y += returnedY;
                agentTwo.x += offeredX;
                agentTwo.y -= returnedY;
                // System.out.println("First Exchange offeredX: " + offeredX + " returnedY: " + returnedY);
                return true;
            }
        }

        // We could not find a suitable y to return for mutually beneficial transaction.
        // Try again, instead agentOne offers dynamic Y, agentTwo returns fixed X.
        float returnedX = pairParams.quantXTransfer;
        for (int i = 0; i < steps; i++) {
            float offeredY = lowerBound + (i * pairParams.dynParams.increment);
            // Calculate the utilities after this hypothetical trade.
            float agentOnePostUtil = agentOneParams.calculateU(agentOne.x + returnedX, agentOne.y - offeredY, agentOneParams.utilityFunctionParam.varA, agentOneParams.utilityFunctionParam.varB);
            float agentTwoPostUtil = agentTwoParams.calculateU(agentTwo.x - returnedX, agentTwo.y + offeredY, agentTwoParams.utilityFunctionParam.varA, agentTwoParams.utilityFunctionParam.varB);

            // If both benefit (post-trade utility is higher than pre-trade utility), trade and exchange complete!
            if (agentOnePostUtil > agentOnePreUtil && agentTwoPostUtil > agentTwoPreUtil) {
                agentOne.x += returnedX;
                agentOne.y -= offeredY;
                agentTwo.x -= returnedX;
                agentTwo.y += offeredY;
                // System.out.println("First Exchange returnedX: " + returnedX + " offeredY: " + offeredY);
                return true;
            }
        }
        // We could not find a suitable y to return for mutually beneficial transaction both ways.
        return false;
    }

    @Override
    protected boolean validState(ExchangeState value) {
        return value != null;
    }

    @Override
    public void onSpawn(BaseAgent agent) {
        setAgentState(agent, new ExchangeState(params.of(agent)));
    }

    @Override
    public void onContact(BaseAgent bumper, BaseAgent bumpee) {
        tryExchange(bumper, bumpee);
    }

    @Override
    public String getName() {
        return exchangeLogger.getName();
    }

    @Override
    public void setWorksheet(XSSFSheet worksheet) {
        // exchangeLogger.setWorksheet(worksheet);
    }

    @Override
    public void logData(BaseStatsProvider statsProvider) {
        exchangeLogger.logData(statsProvider);
    }

    @Override
    public int getTableCount() {
        return exchangeLogger.getTableCount();
    }

    @Override
    public Collection<SmartDataTable> getTables() {
        return exchangeLogger.getTables();
    }
}
