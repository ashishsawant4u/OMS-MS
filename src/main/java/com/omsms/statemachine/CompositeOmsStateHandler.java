package com.omsms.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

@Component("compositeOmsStateHandler")
public class CompositeOmsStateHandler 
{
	Logger log = LoggerFactory.getLogger(CherryPickStateHandler.class);
	
	@Autowired
	StateMachineFactory<OmsStates,OmsStateEvents> stateMachineFactory;
	
	public StateMachine<OmsStates, OmsStateEvents> createNewStateMachineInstance(String referenceId)
	{
		StateMachine<OmsStates, OmsStateEvents> stateMachine = stateMachineFactory.getStateMachine(referenceId);
		
		stateMachine.stop();
		
		stateMachine.getStateMachineAccessor().doWithAllRegions(sm->{
			sm.resetStateMachine(new DefaultStateMachineContext<OmsStates, OmsStateEvents>(OmsStates.SOURCING, null, null, null));
		});
		
		stateMachine.start();
		
		return stateMachine;
	}
	
	public StateMachine<OmsStates, OmsStateEvents> initQC(String referenceId) 
	{
		StateMachine<OmsStates, OmsStateEvents> stateMachine = createNewStateMachineInstance(referenceId);
		
		stateMachine.sendEvent(OmsStateEvents.INIT_QC);
		
		return stateMachine;
	}
	
	public StateMachine<OmsStates, OmsStateEvents> initDelivery(String referenceId,StateMachine<OmsStates, OmsStateEvents> stateMachine) 
	{
		//StateMachine<OmsStates, OmsStateEvents> stateMachine = createNewStateMachineInstance(referenceId);
		
		stateMachine.sendEvent(OmsStateEvents.INIT_DELIVERY);
		
		return stateMachine;
	}
	
}
