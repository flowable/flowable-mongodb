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
package org.flowable.mongodb.persistence.manager;

import java.util.Collections;
import java.util.List;

import org.bson.conversions.Bson;
import org.flowable.common.engine.impl.Page;
import org.flowable.common.engine.impl.persistence.entity.Entity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.job.api.ExternalWorkerJob;
import org.flowable.job.api.Job;
import org.flowable.job.service.impl.ExternalWorkerJobAcquireBuilderImpl;
import org.flowable.job.service.impl.ExternalWorkerJobQueryImpl;
import org.flowable.job.service.impl.SuspendedJobQueryImpl;
import org.flowable.job.service.impl.persistence.entity.ExternalWorkerJobEntity;
import org.flowable.job.service.impl.persistence.entity.ExternalWorkerJobEntityImpl;
import org.flowable.job.service.impl.persistence.entity.SuspendedJobEntity;
import org.flowable.job.service.impl.persistence.entity.data.ExternalWorkerJobDataManager;
import org.flowable.job.service.impl.persistence.entity.data.SuspendedJobDataManager;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;

/**
 * @author Joram Barrez
 */
public class MongoDbExternalWorkerJobDataManager extends AbstractMongoDbDataManager<ExternalWorkerJobEntity> implements ExternalWorkerJobDataManager {

    public static final String COLLECTION_EXTERNAL_WORKER_JOBS = "externalWorkerJobs";
    
    @Override
    public String getCollection() {
        return COLLECTION_EXTERNAL_WORKER_JOBS;
    }

    @Override
    public BasicDBObject createUpdateObject(Entity entity) {
        return null;
    }

    @Override
    public ExternalWorkerJobEntity findJobByCorrelationId(String correlationId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ExternalWorkerJob> findJobsByQueryCriteria(ExternalWorkerJobQueryImpl jobQuery) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long findJobCountByQueryCriteria(ExternalWorkerJobQueryImpl jobQuery) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteJobsByExecutionId(String executionId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ExternalWorkerJobEntity> findExternalJobsToExecute(ExternalWorkerJobAcquireBuilderImpl builder, int numberOfJobs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ExternalWorkerJobEntity> findJobsByScopeIdAndSubScopeId(String scopeId, String subScopeId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ExternalWorkerJobEntity> findJobsToExecute(List<String> enabledCategories, Page page) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ExternalWorkerJobEntity> findJobsByExecutionId(String executionId) {
        Bson filter = Filters.eq("executionId", executionId);
        return getMongoDbSession().find(COLLECTION_EXTERNAL_WORKER_JOBS, filter);
    }

    @Override
    public List<ExternalWorkerJobEntity> findJobsByProcessInstanceId(String processInstanceId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ExternalWorkerJobEntity> findExpiredJobs(List<String> enabledCategories, Page page) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateJobTenantIdForDeployment(String deploymentId, String newTenantId) {

    }

    @Override
    public void resetExpiredJob(String jobId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExternalWorkerJobEntity create() {
        return new ExternalWorkerJobEntityImpl();
    }
}
