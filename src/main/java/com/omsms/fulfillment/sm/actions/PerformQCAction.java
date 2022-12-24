package com.omsms.fulfillment.sm.actions;

import java.util.Map;

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
@Component("performQCAction")
public class PerformQCAction implements Action<FulfillmentStates, FulfillmentEvents>
{
	@Resource(name = "orderRepository")
	OrderRepository orderRepository;
	
	@Override
	public void execute(StateContext<FulfillmentStates, FulfillmentEvents> context) 
	{
		log.info("(PerformQCAction)");
		
		Map<Object, Object> machineVars = context.getStateMachine().getExtendedState().getVariables();
		log.info("ALL VARS ====> "+machineVars);
		
		OrderModel orderModel = (OrderModel)context.getMessage().getHeaders().get("order");
		if(null!=orderModel)
		{
			log.info("ORDER IN ACTION ==> "+orderModel.getOrderid());
			
			if((boolean)machineVars.get("failQC"))
			{
				log.info("product damaged :(");
				throw new RuntimeException("unable to source :(");
			}
			else
			{
				orderModel.setOrderStatus(FulfillmentStates.QC_PASS.name());
				orderRepository.save(orderModel);
				context.getStateMachine().getExtendedState().getVariables().put("qcFlag", true);
			}
		}
	}
}
