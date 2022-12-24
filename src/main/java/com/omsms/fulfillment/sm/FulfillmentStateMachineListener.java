package com.omsms.fulfillment.sm;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("fulfillmentStateMachineListener")
public class FulfillmentStateMachineListener extends StateMachineListenerAdapter<FulfillmentStates, FulfillmentEvents>
{
	@Override
	public void stateEntered(State<FulfillmentStates, FulfillmentEvents> state) {
		//log.info("FulfillmentStateMachineListener(stateEntered) ==> "+state.getId().name());
	}

	@Override
	public void stateExited(State<FulfillmentStates, FulfillmentEvents> state) {
		//log.info("FulfillmentStateMachineListener(stateExited) ==> "+state.getId().name());
	}

	@Override
	public void transitionStarted(Transition<FulfillmentStates, FulfillmentEvents> transition) {
		//log.info("FulfillmentStateMachineListener(transitionStarted) ==> SOURCE: "+transition.getSource().getId().name()+" TARGET: "+transition.getTarget().getId().name());
	}

	@Override
	public void transitionEnded(Transition<FulfillmentStates, FulfillmentEvents> transition) {
		//log.info("FulfillmentStateMachineListener(transitionEnded) ==> SOURCE: "+transition.getSource().getId().name()+" TARGET: "+transition.getTarget().getId().name());
	}

	@Override
	public void stateChanged(State<FulfillmentStates, FulfillmentEvents> from,
			State<FulfillmentStates, FulfillmentEvents> to)
	{
		log.info("FulfillmentStateMachineListener(stateChanged) ==> FROM: "+from.getId().name()+" TO: "+to.getId().name());
	}

	@Override
	public void stateMachineStarted(StateMachine<FulfillmentStates, FulfillmentEvents> stateMachine) 
	{
		log.info("FulfillmentStateMachineListener(stateMachineStarted)");
	}

	@Override
	public void stateMachineStopped(StateMachine<FulfillmentStates, FulfillmentEvents> stateMachine) {
		log.info("FulfillmentStateMachineListener(stateMachineStopped)");
	}

	@Override
	public void stateMachineError(StateMachine<FulfillmentStates, FulfillmentEvents> stateMachine,
			Exception exception) {
		log.info("FulfillmentStateMachineListener(stateMachineError) "+exception.getMessage());
	}
	
	
	
}
