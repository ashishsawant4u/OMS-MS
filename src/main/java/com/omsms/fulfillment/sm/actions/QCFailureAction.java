package com.omsms.fulfillment.sm.actions;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import com.omsms.entity.OrderModel;
import com.omsms.fulfillment.sm.FulfillmentEvents;
import com.omsms.fulfillment.sm.FulfillmentStates;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("qcFailureAction")
public class QCFailureAction  implements Action<FulfillmentStates, FulfillmentEvents>
{
	
	@Override
	public void execute(StateContext<FulfillmentStates, FulfillmentEvents> context) 
	{
		log.info("(QCFailureAction)");
		
		OrderModel orderModel = (OrderModel)context.getMessage().getHeaders().get("order");
		
		Message<FulfillmentEvents> qcFailMsg =  MessageBuilder.withPayload(FulfillmentEvents.INIT_QCFAIL).setHeader("order",orderModel).build();
		context.getStateMachine().sendEvent(qcFailMsg);	
	}
}