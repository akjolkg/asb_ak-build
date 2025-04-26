package org.asb.service;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import org.asb.data.DebtorsDTO;
import org.asb.model.Schedule;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Local
public interface ScheduleService extends GenericService<Schedule, Integer> {
	List<DebtorsDTO> getModel(Integer limit, Integer offset, List<String> expressionsList, Map<Integer, Object> parameters, String extraOrder);
	Map<String, Object> getModelTotal(List<String> expressionsList, Map<Integer, Object> parameters);

}
