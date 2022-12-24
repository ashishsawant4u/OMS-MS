package com.omsms.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.stereotype.Component;

@Component("omsStateMachineInterceptor")
public class OmsStateMachineInterceptor extends StateMachineInterceptorAdapter<OmsStates , OmsStateEvents>
{
	Logger log = LoggerFactory.getLogger(OmsStateMachineInterceptor.class);
	
	@Override
	public StateContext<OmsStates , OmsStateEvents> postTransition(StateContext<OmsStates , OmsStateEvents> stateContext) 
	{
		log.info("@@@@@@@@ OmsStateMachineInterceptor @@@@@@@@ ");
		log.info("postTransition ==> "+stateContext.getStateMachine().getState().getId().name());
		
		
		OmsStates currentState = stateContext.getStateMachine().getState().getId();
		
		switch (currentState) 
		{
				case SOURCING : 
				{
					stateContext.getStateMachine().sendEvent(OmsStateEvents.INIT_QC);
					break;
				}
				case QUALITYCHECK : 
				{
//					stateContext.getStateMachine().sendEvent(OmsStateEvents.INIT_DELIVERY);
//					break;
				}
				default:
				{
					log.info("DO NOTHING!");
				}
		}
		
		return stateContext;
	}
}
