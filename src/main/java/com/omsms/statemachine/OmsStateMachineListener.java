package com.omsms.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

@Component("omsStateMachineListener")
public class OmsStateMachineListener extends StateMachineListenerAdapter<OmsStates, OmsStateEvents>
{
	Logger log = LoggerFactory.getLogger(OmsStateMachineListener.class);
	
	@Override
	public void stateChanged(State<OmsStates, OmsStateEvents> from, State<OmsStates, OmsStateEvents> to) 
	{
		if(null!=from)
		{
			log.info("====================================================================");
			log.info("state changed from "+from.getId().name() +"  =====>>  "+to.getId().name());
			log.info("====================================================================");
		}
		
	}
}
