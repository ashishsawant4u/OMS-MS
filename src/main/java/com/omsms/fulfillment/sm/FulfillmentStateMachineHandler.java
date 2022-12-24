package com.omsms.fulfillment.sm;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

@Component("fulfillmentStateMachineHandler")
public class FulfillmentStateMachineHandler 
{
	@Resource(name = "fulfillmentSMConfigurationRef")
	StateMachineFactory<FulfillmentStates, FulfillmentEvents> fulfillmentSMConfigurationRef;
	
	@Resource(name = "fulfillmentStateMachineListener")
	FulfillmentStateMachineListener fulfillmentStateMachineListener;
	
	@Resource(name = "fulfillmentStateMachineInterceptor")
	FulfillmentStateMachineInterceptor fulfillmentStateMachineInterceptor;
	
	public StateMachine<FulfillmentStates, FulfillmentEvents> getInstnce(FulfillmentStates atState)
	{
		StateMachine<FulfillmentStates, FulfillmentEvents> stateMachine = fulfillmentSMConfigurationRef.getStateMachine();
			
		stateMachine.stop();
		
		stateMachine.addStateListener(fulfillmentStateMachineListener);
		
		stateMachine.getStateMachineAccessor().doWithAllRegions(sm->{
			sm.addStateMachineInterceptor(fulfillmentStateMachineInterceptor);
			sm.resetStateMachine(new DefaultStateMachineContext<>(atState, null, null, null));
		});
		
		stateMachine.start();
		
		return stateMachine;
	}
}
