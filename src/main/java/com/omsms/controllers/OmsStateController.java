package com.omsms.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.omsms.statemachine.CherryPickStateHandler;
import com.omsms.statemachine.CompositeOmsStateHandler;
import com.omsms.statemachine.OmsStateEvents;
import com.omsms.statemachine.OmsStateMachineInterceptor;
import com.omsms.statemachine.OmsStates;
import com.omsms.statemachine.StateConstants;

import jakarta.annotation.Resource;

@RequestMapping("/statemachine")
@Controller
public class OmsStateController 
{
	Logger log = LoggerFactory.getLogger(OmsStateController.class);
	
	@Autowired
	StateMachineFactory<OmsStates,OmsStateEvents> stateMachineFactory;
	
	@Resource(name = "omsStateMachineInterceptor")
	OmsStateMachineInterceptor omsStateMachineInterceptor;

	@Resource(name = "cherryPickStateHandler")
	CherryPickStateHandler cherryPickStateHandler;
	
	@Resource(name = "compositeOmsStateHandler")
	CompositeOmsStateHandler compositeOmsStateHandler;

	@GetMapping("/event")
	@ResponseBody
	public ResponseEntity<String> publishEvent()
	{
		StateMachine<OmsStates,OmsStateEvents>  stateMachine = stateMachineFactory.getStateMachine(StateConstants.MACHINE_ID);
		
		stateMachine.sendEvent(OmsStateEvents.INIT_SOURCING);
		
		return new ResponseEntity("OK",HttpStatus.OK);
	}
	
	@GetMapping("/event-msg")
	@ResponseBody
	public ResponseEntity<String> publishEventWithMsg()
	{
		StateMachine<OmsStates,OmsStateEvents>  stateMachine = stateMachineFactory.getStateMachine(StateConstants.MACHINE_ID);
		
		Message<OmsStateEvents> fullfillMessage = 
                MessageBuilder.withPayload(OmsStateEvents.INIT_SOURCING)
                              .setHeader("randomkey","randomvalue")
                              .build();
		
		stateMachine.getExtendedState().getVariables().put("randomExtendedVariable", "randomExtendedVariableValue");
		
		stateMachine.getStateMachineAccessor().doWithAllRegions(sm -> sm.addStateMachineInterceptor(omsStateMachineInterceptor));
		
		stateMachine.sendEvent(fullfillMessage);
		
		return new ResponseEntity("OK",HttpStatus.OK);
	}
	
	@GetMapping("/reset/{fromstate}")
	@ResponseBody
	public ResponseEntity<String> resetStateTo(@PathVariable("fromstate") String fromstate)
	{
		cherryPickStateHandler.resetStateTo(fromstate);
		
		return new ResponseEntity("OK",HttpStatus.OK);
	}
	
	@GetMapping("/composite-sm")
	@ResponseBody
	public ResponseEntity<String> compositeSm()
	{
		StateMachine<OmsStates, OmsStateEvents> qcMachine = compositeOmsStateHandler.initQC(StateConstants.MACHINE_ID);
		
		log.info("called initQC() "+qcMachine.getState().getId().name());
		
		StateMachine<OmsStates, OmsStateEvents> delMachine = compositeOmsStateHandler.initDelivery(StateConstants.MACHINE_ID,qcMachine);
		
		log.info("called initDelivery() "+delMachine.getState().getId().name());
		
		return new ResponseEntity("OK",HttpStatus.OK);
	}
}
