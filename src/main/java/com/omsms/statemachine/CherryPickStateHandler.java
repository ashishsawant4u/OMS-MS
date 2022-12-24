package com.omsms.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

@Component("cherryPickStateHandler")
public class CherryPickStateHandler 
{	
	Logger log = LoggerFactory.getLogger(CherryPickStateHandler.class);
	
	@Autowired
	StateMachineFactory<OmsStates,OmsStateEvents> stateMachineFactory;
	
	public void resetStateTo(String state)
	{
		StateMachine<OmsStates,OmsStateEvents>  stateMachine = stateMachineFactory.getStateMachine(StateConstants.MACHINE_ID);
		
		log.info("!!!!! CherryPickStateHandler ======> "+ stateMachine.getUuid() +" is @ "+stateMachine.getState().getId().name());
		
		OmsStates fromState  = OmsStates.valueOf(state);
		stateMachine.stop();
		
		stateMachine.getStateMachineAccessor().doWithAllRegions(sm->{
			sm.resetStateMachine(new DefaultStateMachineContext<OmsStates, OmsStateEvents>(fromState, null, null, null));
		});
		stateMachine.start();
		
		log.info("!!!!! CherryPickStateHandler ======> "+ stateMachine.getUuid()+" is @ "+stateMachine.getState().getId().name());
	}
}
