package com.omsms.statemachine;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import jakarta.annotation.Resource;

@Configuration
@EnableStateMachineFactory
public class OmsStateMachineConfiguration extends StateMachineConfigurerAdapter<OmsStates, OmsStateEvents>
{
	@Resource(name = "omsStateMachineListener")
	OmsStateMachineListener omsStateMachineListener;
	
	@Resource(name = "omsSourcingAction")
	OmsSourcingAction omsSourcingAction;
	
	/**
	 * Configuring States
	 */
	@Override
	public void configure(StateMachineStateConfigurer<OmsStates,OmsStateEvents> states) throws Exception 
	{
			states.withStates().initial(OmsStates.FULFILLMENT)
			                   .state(OmsStates.SOURCING)
			                   .state(OmsStates.QUALITYCHECK)
			                   .end(OmsStates.DELIVERY)
			                   .end(OmsStates.SOURCINGFAIL)
			                   .end(OmsStates.QCFAIL);
	}
	
	/**
	 * Configuring autoStartup and listener
	 */
	@Override
	public void configure(StateMachineConfigurationConfigurer<OmsStates,OmsStateEvents> config) throws Exception 
	{
	    config.withConfiguration()
	          .autoStartup(true)
	          .listener(omsStateMachineListener);
	}
	
	/**
	 * Configuring state transitions wrt. events
	 */
	@Override
	public void configure(StateMachineTransitionConfigurer<OmsStates, OmsStateEvents> transitions) throws Exception 
	{
		transitions
		.withExternal().source(OmsStates.FULFILLMENT)
		               .target(OmsStates.SOURCING)
		               .event(OmsStateEvents.INIT_SOURCING)
		               .action(omsSourcingAction)
		.and()
		
		.withExternal().source(OmsStates.SOURCING)
				       .target(OmsStates.QUALITYCHECK)
				       .event(OmsStateEvents.INIT_QC)
				       
       .and()
		
	   .withExternal().source(OmsStates.QUALITYCHECK)
				      .target(OmsStates.DELIVERY)
				      .event(OmsStateEvents.INIT_DELIVERY)
				       
       .and()
		
	   .withExternal().source(OmsStates.SOURCING)
				       .target(OmsStates.SOURCINGFAIL)
				       .event(OmsStateEvents.INIT_SOURCINGFAILED)
				       
       .and()
		
	   .withExternal().source(OmsStates.QUALITYCHECK)
				       .target(OmsStates.QCFAIL)
				       .event(OmsStateEvents.INIT_QCFAILED);
		
	}
}
