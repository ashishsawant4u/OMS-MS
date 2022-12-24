package com.omsms.cronjobs;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.statemachine.StateMachine;

import com.omsms.entity.OrderModel;
import com.omsms.fulfillment.sm.FulfillmentEvents;
import com.omsms.fulfillment.sm.FulfillmentStateMachineHandler;
import com.omsms.fulfillment.sm.FulfillmentStates;
import com.omsms.repository.OrderRepository;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableScheduling
public class ResourcingAttemptJob 
{	
	@Resource(name = "orderRepository")
	OrderRepository orderRepository;
	
	 @Resource(name = "fulfillmentStateMachineHandler")
	 FulfillmentStateMachineHandler fulfillmentStateMachineHandler;
	
	@Scheduled(fixedDelay = 20000)
	public void reAttemptSourcing() 
	{
	    log.info("Re-Attempting Sorcing... ");
	    
	    //List<OrderModel> orders =  orderRepository.findByOrderStatus(FulfillmentStates.SOURICNG_FAIL.name());
	    List<OrderModel> orders =  orderRepository.findByOrderStatusIn(Arrays.asList(FulfillmentStates.SOURICNG_FAIL.name(),FulfillmentStates.QC_FAIL.name()));
	    log.info(" Orders found ===> "+orders.size());
	    
	    OrderModel orderModel = orders.stream().findFirst().get();
	    
	    log.info("triggering state machine for ====> "+orderModel.getOrderid());
	    
//	    StateMachine<FulfillmentStates, FulfillmentEvents> stateMachine = fulfillmentStateMachineHandler.getInstnce(FulfillmentStates.READY_FOR_SOURCING);
//		
//	    stateMachine.getExtendedState().getVariables().put("orderid", orderModel.getOrderid());
//		stateMachine.getExtendedState().getVariables().put("failSourcing", false);
//		stateMachine.getExtendedState().getVariables().put("failQC", false);
//	    
//	    Message<FulfillmentEvents> sourcingMsg =  MessageBuilder.withPayload(FulfillmentEvents.INIT_SOURCING).setHeader("order",orderModel).build();
//	    
//	    stateMachine.sendEvent(sourcingMsg); 
	}
}
