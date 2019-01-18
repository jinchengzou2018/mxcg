package com.mxcg.db.jpa;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class PersistListener {
    
    @PostUpdate
    public void logForUpdate(Object entity) {
    }

    @PostRemove
    public void logForRemove(Object entity) {
    }

    @PostPersist
    public void logForCreate(Object entity) {
    }
    
}
