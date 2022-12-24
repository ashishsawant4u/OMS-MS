package com.omsms.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;


@Component("omsSourcingAction")
public class OmsSourcingAction implements Action<OmsStates, OmsStateEvents>
{
	Logger log = LoggerFactory.getLogger(OmsSourcingAction.class);
	
	@Override
	public void execute(StateContext<OmsStates, OmsStateEvents> context) 
	{
		log.info("###### OmsSourcingAction ######");
		log.info("PAYLOAD ==> "+context.getMessage().getPayload().name());
		log.info("HEADER ==> "+context.getMessage().getHeaders().entrySet());
		log.info("EXTENDED VAR ==> "+context.getExtendedState().getVariables());
	}

}
