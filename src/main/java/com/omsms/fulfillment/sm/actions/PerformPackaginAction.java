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
@Component("performPackaginAction")
public class PerformPackaginAction implements Action<FulfillmentStates, FulfillmentEvents>
{
	@Resource(name = "orderRepository")
	OrderRepository orderRepository;
	
	@Override
	public void execute(StateContext<FulfillmentStates, FulfillmentEvents> context) 
	{
		log.info("(PerformPackaginAction)");
		OrderModel orderModel = (OrderModel)context.getMessage().getHeaders().get("order");
		if(null!=orderModel)
		{
			log.info("ORDER IN ACTION ==> "+orderModel.getOrderid());
			orderModel.setOrderStatus(FulfillmentStates.PACKED.name());
			orderRepository.save(orderModel);
			context.getStateMachine().getExtendedState().getVariables().put("packagingFlag", true);
		}
	}
}
