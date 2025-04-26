package org.asb.service;

import javax.ejb.Local;

import org.asb.model.Attachment;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Local
public interface AttachmentService extends GenericService<Attachment, Integer> {

}
