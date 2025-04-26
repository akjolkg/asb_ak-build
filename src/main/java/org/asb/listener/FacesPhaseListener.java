package org.asb.listener;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

public class FacesPhaseListener implements PhaseListener {

	private static final long serialVersionUID = -6662233786609884435L;

	@Override
	public void afterPhase(PhaseEvent event) {
		
	} 

	@Override
	public void beforePhase(PhaseEvent event) {
		 
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}
	
}
