package org.asb.model;

import java.io.Serializable;

/**
 * @param <PK>
 * @author Akzholbek Omorov
 */

public interface PersistentEntity<PK extends Serializable> extends Serializable {

    PK getId();

    void setId(PK pk);

}