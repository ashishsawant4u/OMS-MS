package com.omsms.fulfillment.sm;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.stereotype.Component;

import com.omsms.entity.OrderModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("fulfillmentStateMachineInterceptor")
public class FulfillmentStateMachineInterceptor extends StateMachineInterceptorAdapter<FulfillmentStates, FulfillmentEvents>
{@Override
	public StateContext<FulfillmentStates, FulfillmentEvents> postTransition(
			StateContext<FulfillmentStates, FulfillmentEvents> stateContext) 
	{
		log.info("FulfillmentStateMachineInterceptor(postTransition)");
		
		StateMachine<FulfillmentStates, FulfillmentEvents> stateMachine = stateContext.getStateMachine();
		
		OrderModel orderModel = (OrderModel)stateContext.getMessage().getHeaders().get("order");
		
		FulfillmentStates orderState = stateMachine.getState().getId();
		
		log.info("CURRENT STATE ===> "+orderState.name());
		
	    switch(orderState) 
	    {
	    	case SOURCED :
	    	{
	    		Message<FulfillmentEvents> qcMsg =  MessageBuilder.withPayload(FulfillmentEvents.INIT_QC).setHeader("order",orderModel).build();
	    		stateMachine.sendEvent(qcMsg);
	    		break;
	    	}
	    	case QC_PASS :
	    	{
	    		Message<FulfillmentEvents> packMsg =  MessageBuilder.withPayload(FulfillmentEvents.INIT_PACKAGING).setHeader("order",orderModel).build();
	    		stateMachine.sendEvent(packMsg);
	    		break;
	    	}
	    	default :
	    	{
	    		log.info("do nothing as state is "+orderState.name());
	    	}
	    }
	    
	    log.info("ALL VARS ====> "+stateMachine.getExtendedState().getVariables());
		
		return stateContext;
	}

	@Override
	public Exception stateMachineError(StateMachine<FulfillmentStates, FulfillmentEvents> stateMachine,
			Exception exception) 
	{
		log.info("FulfillmentStateMachineInterceptor(postTransition) "+exception.getMessage());
		return super.stateMachineError(stateMachine, exception);
	}
	
}
