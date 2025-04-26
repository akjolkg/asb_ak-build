package org.asb.service;

import javax.ejb.Local;

import org.asb.model.CommentLog;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Local
public interface CommentLogService extends GenericService<CommentLog, Integer> {

}
