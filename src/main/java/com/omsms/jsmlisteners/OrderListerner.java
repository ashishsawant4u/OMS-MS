package com.omsms.jsmlisteners;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import com.omsms.dto.OrderDto;
import com.omsms.entity.OrderEntryModel;
import com.omsms.entity.OrderModel;
import com.omsms.fulfillment.sm.FulfillmentEvents;
import com.omsms.fulfillment.sm.FulfillmentStateMachineHandler;
import com.omsms.fulfillment.sm.FulfillmentStates;
import com.omsms.repository.OrderRepository;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@EnableJms
@Component
@Slf4j
public class OrderListerner 
{
	  @Resource(name = "orderRepository")
	  OrderRepository orderRepository;
	  
	  @Resource(name = "fulfillmentStateMachineHandler")
	  FulfillmentStateMachineHandler fulfillmentStateMachineHandler;
	 	
	  @JmsListener(destination = "orderpipe", containerFactory = "myFactory")
	  public void receiveMessage(OrderDto order) 
	  {
		  		log.info("Received in JMS Listener <" + order + ">");
			    OrderModel orderModel = prepareOrderModel(order);
			    orderRepository.save(orderModel);
			    
			    //StateMachine<FulfillmentStates, FulfillmentEvents> stateMachine =fulfillmentStateMachineHandler.getInstnce(FulfillmentStates.READY_FOR_SOURCING);
			   // log.info("BEFORE STATE ===> "+stateMachine.getState().getId().name());
			   // stateMachine.sendEvent(FulfillmentEvents.INIT_SOURCING);
			   // log.info("AFTER STATE ===> "+stateMachine.getState().getId().name());
			    
			    StateMachine<FulfillmentStates, FulfillmentEvents> stateMachine = fulfillmentStateMachineHandler.getInstnce(FulfillmentStates.READY_FOR_SOURCING);
				
				stateMachine.getExtendedState().getVariables().put("orderid", orderModel.getOrderid());
				stateMachine.getExtendedState().getVariables().put("failSourcing", false);
				stateMachine.getExtendedState().getVariables().put("failQC", false);
				
			    Message<FulfillmentEvents> sourcingMsg =  MessageBuilder.withPayload(FulfillmentEvents.INIT_SOURCING).setHeader("order",orderModel).build();
			    
			    stateMachine.sendEvent(sourcingMsg); 
	  }

	private OrderModel prepareOrderModel(OrderDto order) 
	{
		OrderModel orderModel = new OrderModel();
		orderModel.setUserId(order.getUserId());
		orderModel.setOrderStatus(FulfillmentStates.READY_FOR_SOURCING.name());
		
		List<OrderEntryModel> entries  = new ArrayList<>();
		order.getProductCodes().forEach(p->{
			OrderEntryModel entry = new OrderEntryModel();
			entry.setProductCode(p);
			entries.add(entry);
		});
		orderModel.setOrderEntries(entries);
		return orderModel;
	}
}
