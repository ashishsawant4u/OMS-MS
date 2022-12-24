package com.omsms.fulfillment.sm.actions;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import com.omsms.entity.OrderModel;
import com.omsms.fulfillment.sm.FulfillmentEvents;
import com.omsms.fulfillment.sm.FulfillmentStates;
import com.omsms.repository.OrderRepository;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("performResourcingAction")
public class PerformResourcingAction implements Action<FulfillmentStates, FulfillmentEvents>
{
	
	@Resource(name = "orderRepository")
	OrderRepository orderRepository;
	
	@Override
	public void execute(StateContext<FulfillmentStates, FulfillmentEvents> context) 
	{
		log.info("(PerformResourcingAction)");
		
		OrderModel orderModel = (OrderModel)context.getMessage().getHeaders().get("order");
		orderModel.setOrderStatus(FulfillmentStates.SOURICNG_FAIL.name());
		orderRepository.save(orderModel);
	}
}
