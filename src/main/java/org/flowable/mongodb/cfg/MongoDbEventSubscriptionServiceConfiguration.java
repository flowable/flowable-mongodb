package org.flowable.mongodb.cfg;

import org.flowable.eventsubscription.service.EventSubscriptionServiceConfiguration;
import org.flowable.mongodb.persistence.MongoDbSessionFactory;
import org.flowable.mongodb.persistence.manager.MongoDbEventSubscriptionDataManager;

public class MongoDbEventSubscriptionServiceConfiguration extends EventSubscriptionServiceConfiguration {

    public MongoDbEventSubscriptionServiceConfiguration(String engineName) {
        super(engineName);
    }

    @Override
    public void initDataManagers() {
        MongoDbEventSubscriptionDataManager mongoDbEventSubscriptionDataManager = new MongoDbEventSubscriptionDataManager();
        this.eventSubscriptionDataManager = mongoDbEventSubscriptionDataManager;
    }

}
