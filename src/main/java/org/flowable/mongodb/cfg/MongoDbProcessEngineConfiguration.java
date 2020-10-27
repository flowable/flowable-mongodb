/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.mongodb.cfg;

import java.util.ArrayList;
import java.util.List;

import org.flowable.common.engine.api.scope.ScopeTypes;
import org.flowable.common.engine.impl.interceptor.CommandInterceptor;
import org.flowable.common.engine.impl.persistence.GenericManagerFactory;
import org.flowable.common.engine.impl.persistence.StrongUuidGenerator;
import org.flowable.common.engine.impl.persistence.cache.EntityCache;
import org.flowable.common.engine.impl.persistence.cache.EntityCacheImpl;
import org.flowable.engine.impl.SchemaOperationsProcessEngineBuild;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.eventsubscription.service.EventSubscriptionServiceConfiguration;
import org.flowable.identitylink.service.IdentityLinkServiceConfiguration;
import org.flowable.job.service.JobServiceConfiguration;
import org.flowable.mongodb.persistence.MongoDbSessionFactory;
import org.flowable.mongodb.persistence.manager.AbstractMongoDbDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbActivityInstanceDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbCommentDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbDeploymentDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbEventSubscriptionDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbExecutionDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbExternalWorkerJobDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbHistoricActivityInstanceDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbHistoricDetailDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbHistoricIdentityLinkDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbHistoricProcessInstanceDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbHistoricTaskInstanceDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbHistoricVariableInstanceDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbIdentityLinkDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbJobDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbModelDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbProcessDefinitionDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbProcessDefinitionInfoDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbResourceDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbTaskDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbTimerJobDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbVariableInstanceDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbDeadLetterJobDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbSuspendedJobDataManager;
import org.flowable.mongodb.schema.MongoProcessSchemaManager;
import org.flowable.mongodb.transaction.MongoDbTransactionContextFactory;
import org.flowable.mongodb.transaction.MongoDbTransactionInterceptor;
import org.flowable.task.service.TaskServiceConfiguration;
import org.flowable.variable.service.VariableServiceConfiguration;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

/**
 * @author Joram Barrez
 */
public class MongoDbProcessEngineConfiguration extends ProcessEngineConfigurationImpl {

    protected List<ServerAddress> serverAddresses = new ArrayList<>();
    protected String databaseName = "flowable";
    protected MongoClientOptions mongoClientOptions;
    protected MongoClient mongoClient;
    protected MongoDatabase mongoDatabase;
    protected MongoProcessSchemaManager processSchemaManager;

    protected MongoDbSessionFactory mongoDbSessionFactory;


    public MongoDbProcessEngineConfiguration() {
        this.usingRelationalDatabase = false;
        this.usingSchemaMgmt = true;
        this.databaseSchemaUpdate = DB_SCHEMA_UPDATE_TRUE;

        // No backwards compatibility needed
        this.validateFlowable5EntitiesEnabled = false;

        // Always enabled for mongo db, so no need to validate
        this.performanceSettings.setValidateExecutionRelationshipCountConfigOnBoot(false);
        this.performanceSettings.setValidateTaskRelationshipCountConfigOnBoot(false);

        this.performanceSettings.setEnableEagerExecutionTreeFetching(true);
        this.performanceSettings.setEnableExecutionRelationshipCounts(true);
        this.performanceSettings.setEnableTaskRelationshipCounts(true);

        this.idGenerator = new StrongUuidGenerator();

        this.disableEventRegistry = true;
    }

    @Override
    public void init() {
        super.init();

        mongoDbSessionFactory.registerDataManager(MongoDbEventSubscriptionDataManager.COLLECTION_EVENT_SUBSCRIPTION, (AbstractMongoDbDataManager) eventSubscriptionServiceConfiguration.getEventSubscriptionDataManager());

        mongoDbSessionFactory.registerDataManager(MongoDbIdentityLinkDataManager.COLLECTION_IDENTITY_LINKS, (AbstractMongoDbDataManager) identityLinkServiceConfiguration.getIdentityLinkDataManager());
        mongoDbSessionFactory.registerDataManager(MongoDbHistoricIdentityLinkDataManager.COLLECTION_HISTORIC_IDENTITY_LINKS, (AbstractMongoDbDataManager) identityLinkServiceConfiguration.getHistoricIdentityLinkDataManager());

        mongoDbSessionFactory.registerDataManager(MongoDbJobDataManager.COLLECTION_JOBS, (AbstractMongoDbDataManager) jobServiceConfiguration.getJobDataManager());
        mongoDbSessionFactory.registerDataManager(MongoDbTimerJobDataManager.COLLECTION_TIMER_JOBS, (AbstractMongoDbDataManager) jobServiceConfiguration.getTimerJobDataManager());
        mongoDbSessionFactory.registerDataManager(MongoDbSuspendedJobDataManager.COLLECTION_SUSPENDED_JOBS, (AbstractMongoDbDataManager) jobServiceConfiguration.getSuspendedJobDataManager());
        mongoDbSessionFactory.registerDataManager(MongoDbDeadLetterJobDataManager.COLLECTION_DEADLETTER_JOBS, (AbstractMongoDbDataManager) jobServiceConfiguration.getDeadLetterJobDataManager());
        mongoDbSessionFactory.registerDataManager(MongoDbExternalWorkerJobDataManager.COLLECTION_EXTERNAL_WORKER_JOBS, (AbstractMongoDbDataManager) jobServiceConfiguration.getExternalWorkerJobDataManager());

        mongoDbSessionFactory.registerDataManager(MongoDbDeploymentDataManager.COLLECTION_DEPLOYMENT, (AbstractMongoDbDataManager) getDeploymentDataManager());
        mongoDbSessionFactory.registerDataManager(MongoDbResourceDataManager.COLLECTION_BYTE_ARRAY, (AbstractMongoDbDataManager) getResourceDataManager());
        mongoDbSessionFactory.registerDataManager(MongoDbProcessDefinitionDataManager.COLLECTION_PROCESS_DEFINITIONS, (AbstractMongoDbDataManager) getProcessDefinitionDataManager());
        mongoDbSessionFactory.registerDataManager(MongoDbExecutionDataManager.COLLECTION_EXECUTIONS, (AbstractMongoDbDataManager) getExecutionDataManager());
        mongoDbSessionFactory.registerDataManager(MongoDbProcessDefinitionInfoDataManager.COLLECTION_PROCESS_DEFINITION_INFO, (AbstractMongoDbDataManager) getProcessDefinitionInfoDataManager());
        mongoDbSessionFactory.registerDataManager(MongoDbCommentDataManager.COLLECTION_COMMENTS, (AbstractMongoDbDataManager) getCommentDataManager());
        mongoDbSessionFactory.registerDataManager(MongoDbModelDataManager.COLLECTION_MODELS, (AbstractMongoDbDataManager) getModelDataManager());
        mongoDbSessionFactory.registerDataManager(MongoDbHistoricProcessInstanceDataManager.COLLECTION_HISTORIC_PROCESS_INSTANCES, (AbstractMongoDbDataManager) getHistoricProcessInstanceDataManager());
        mongoDbSessionFactory.registerDataManager(MongoDbActivityInstanceDataManager.COLLECTION_ACTIVITY_INSTANCES, (AbstractMongoDbDataManager) getActivityInstanceDataManager());
        mongoDbSessionFactory.registerDataManager(MongoDbHistoricActivityInstanceDataManager.COLLECTION_HISTORIC_ACTIVITY_INSTANCES, (AbstractMongoDbDataManager) getHistoricActivityInstanceDataManager());
        mongoDbSessionFactory.registerDataManager(MongoDbHistoricDetailDataManager.COLLECTION_HISTORIC_DETAILS, (AbstractMongoDbDataManager) getHistoricDetailDataManager());

        mongoDbSessionFactory.registerDataManager(MongoDbTaskDataManager.COLLECTION_TASKS, (AbstractMongoDbDataManager) taskServiceConfiguration.getTaskDataManager());
        mongoDbSessionFactory.registerDataManager(MongoDbHistoricTaskInstanceDataManager.COLLECTION_HISTORIC_TASK_INSTANCES, (AbstractMongoDbDataManager) taskServiceConfiguration.getHistoricTaskInstanceDataManager());

        mongoDbSessionFactory.registerDataManager(MongoDbVariableInstanceDataManager.COLLECTION_VARIABLES, (AbstractMongoDbDataManager) variableServiceConfiguration.getVariableInstanceDataManager());
        mongoDbSessionFactory.registerDataManager(MongoDbHistoricVariableInstanceDataManager.COLLECTION_HISTORIC_VARIABLE_INSTANCES, (AbstractMongoDbDataManager) variableServiceConfiguration.getHistoricVariableInstanceDataManager());
    }

    @Override
    public void initNonRelationalDataSource() {
        if (this.mongoClientOptions == null) {
            this.mongoClientOptions = MongoClientOptions.builder().build();
        }
        if (this.mongoClient == null) {
            this.mongoClient = new com.mongodb.MongoClient(serverAddresses, mongoClientOptions);
        }

        if (this.mongoDatabase == null)  {
            this.mongoDatabase = this.mongoClient.getDatabase(databaseName);
        }
    }

    @Override
    public void initSchemaManager() {
        this.schemaManager = new MongoProcessSchemaManager();
    }

    public void initSchemaManagementCommand() {
        // Impl note: the schemaMgmtCmd of the regular impl is reused, as it will delegate to the MongoProcessSchemaManager class
        if (schemaManagementCmd == null) {
            this.schemaManagementCmd = new SchemaOperationsProcessEngineBuild();
        }
    }

    @Override
    public CommandInterceptor createTransactionInterceptor() {
        return null;
//        return new MongoDbTransactionInterceptor(mongoClient); --> commented out, transaction is started in MongoDbSession
    }

    @Override
    public void initTransactionContextFactory() {
        if (transactionContextFactory == null) {
            transactionContextFactory = new MongoDbTransactionContextFactory();
        }
    }

    @Override
    public void initDataManagers() {
        MongoDbDeploymentDataManager mongoDeploymentDataManager = new MongoDbDeploymentDataManager(this);
        this.deploymentDataManager = mongoDeploymentDataManager;

        MongoDbResourceDataManager mongoDbResourceDataManager = new MongoDbResourceDataManager(this);
        this.resourceDataManager = mongoDbResourceDataManager;

        MongoDbProcessDefinitionDataManager mongoDbProcessDefinitionDataManager = new MongoDbProcessDefinitionDataManager(this);
        this.processDefinitionDataManager = mongoDbProcessDefinitionDataManager;

        MongoDbExecutionDataManager mongoDbExecutionDataManager = new MongoDbExecutionDataManager(this);
        this.executionDataManager = mongoDbExecutionDataManager;

        MongoDbProcessDefinitionInfoDataManager mongoDbProcessDefinitionInfoDataManager = new MongoDbProcessDefinitionInfoDataManager(this);
        this.processDefinitionInfoDataManager = mongoDbProcessDefinitionInfoDataManager;

        MongoDbCommentDataManager mongoDbCommentDataManager = new MongoDbCommentDataManager(this);
        this.commentDataManager = mongoDbCommentDataManager;

        MongoDbModelDataManager mongoDbModelDataManager = new MongoDbModelDataManager(this);
        this.modelDataManager = mongoDbModelDataManager;

        MongoDbHistoricProcessInstanceDataManager mongoDbHistoricProcessInstanceDataManager = new MongoDbHistoricProcessInstanceDataManager(this);
        this.historicProcessInstanceDataManager = mongoDbHistoricProcessInstanceDataManager;

        MongoDbActivityInstanceDataManager mongoDbActivityInstanceDataManager = new MongoDbActivityInstanceDataManager(this);
        this.activityInstanceDataManager = mongoDbActivityInstanceDataManager;

        MongoDbHistoricActivityInstanceDataManager mongoDbHistoricActivityInstanceDataManager = new MongoDbHistoricActivityInstanceDataManager(this);
        this.historicActivityInstanceDataManager = mongoDbHistoricActivityInstanceDataManager;

        MongoDbHistoricDetailDataManager mongoDbHistoricDetailDataManager = new MongoDbHistoricDetailDataManager(this);
        this.historicDetailDataManager = mongoDbHistoricDetailDataManager;

    }

    @Override
    protected JobServiceConfiguration instantiateJobServiceConfiguration() {
        MongoDbJobServiceConfiguration mongoDbJobServiceConfiguration = new MongoDbJobServiceConfiguration(ScopeTypes.BPMN);
        return mongoDbJobServiceConfiguration;
    }

    @Override
    protected TaskServiceConfiguration instantiateTaskServiceConfiguration() {
        MongoDbTaskServiceConfiguration mongoDbTaskServiceConfiguration = new MongoDbTaskServiceConfiguration(ScopeTypes.BPMN);
        return mongoDbTaskServiceConfiguration;
    }

    @Override
    protected IdentityLinkServiceConfiguration instantiateIdentityLinkServiceConfiguration() {
        MongoDbIdentityLinkServiceConfiguration mongoDbIdentityLinkServiceConfiguration = new MongoDbIdentityLinkServiceConfiguration(ScopeTypes.BPMN);
        return mongoDbIdentityLinkServiceConfiguration;
    }

    @Override
    protected VariableServiceConfiguration instantiateVariableServiceConfiguration() {
        MongoDbVariableServiceConfiguration mongoDbVariableServiceConfiguration = new MongoDbVariableServiceConfiguration(ScopeTypes.BPMN);
        return mongoDbVariableServiceConfiguration;
    }

    @Override
    protected EventSubscriptionServiceConfiguration instantiateEventSubscriptionServiceConfiguration() {
        MongoDbEventSubscriptionServiceConfiguration mongoDbEventSubscriptionServiceConfiguration = new MongoDbEventSubscriptionServiceConfiguration(ScopeTypes.BPMN);
        return mongoDbEventSubscriptionServiceConfiguration;
    }

    @Override
    public void initSessionFactories() {

        if (this.customSessionFactories == null) {
            this.customSessionFactories = new ArrayList<>();
        }

        this.customSessionFactories.add(new GenericManagerFactory(EntityCache.class, EntityCacheImpl.class));

        initMongoDbSessionFactory();
        this.customSessionFactories.add(mongoDbSessionFactory);

        super.initSessionFactories();
    }

    public void initMongoDbSessionFactory() {
        if (this.mongoDbSessionFactory == null) {
            this.mongoDbSessionFactory = new MongoDbSessionFactory(mongoClient, mongoDatabase);
        }
    }

    public List<ServerAddress> getServerAddresses() {
        return serverAddresses;
    }

    public MongoDbProcessEngineConfiguration setServerAddresses(List<ServerAddress> serverAddresses) {
        this.serverAddresses = serverAddresses;
        return this;
    }

    /**
     * server addresses in the form of "host:port, host:port, ..."
     */
    public MongoDbProcessEngineConfiguration setConnectionUrl(String connectionUrl) {
        List<ServerAddress> result = new ArrayList<>();

        String[] addresses = connectionUrl.split(",");
        for (String address : addresses) {
            String[] splittedAddress = address.split(":");
            String host = splittedAddress[0].trim();
            int port = Integer.valueOf(splittedAddress[1].trim());

            result.add(new ServerAddress(host, port));
        }

        setServerAddresses(result);
        return this;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public MongoDbProcessEngineConfiguration setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
        return this;
    }

    public MongoClientOptions getMongoClientOptions() {
        return mongoClientOptions;
    }

    public void setMongoClientOptions(MongoClientOptions mongoClientOptions) {
        this.mongoClientOptions = mongoClientOptions;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoDbProcessEngineConfiguration setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        return this;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    public MongoProcessSchemaManager getProcessSchemaManager() {
        return processSchemaManager;
    }

    public MongoDbProcessEngineConfiguration setProcessSchemaManager(MongoProcessSchemaManager processSchemaManager) {
        this.processSchemaManager = processSchemaManager;
        return this;
    }

}
