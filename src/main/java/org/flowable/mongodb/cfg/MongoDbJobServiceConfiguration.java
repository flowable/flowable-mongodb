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

import org.flowable.job.service.JobServiceConfiguration;
import org.flowable.mongodb.persistence.manager.MongoDbExecutionDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbExternalWorkerJobDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbJobDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbTimerJobDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbDeadLetterJobDataManager;
import org.flowable.mongodb.persistence.manager.MongoDbSuspendedJobDataManager;

import com.mongodb.Mongo;

/**
 * @author Joram Barrez
 */
public class MongoDbJobServiceConfiguration extends JobServiceConfiguration {

    public MongoDbJobServiceConfiguration(String engineName) {
        super(engineName);
    }

    @Override
    public void initDataManagers() {
        // TODO: other data managers
        if (jobDataManager == null) {
            MongoDbJobDataManager mongoDbJobDataManager = new MongoDbJobDataManager(this);
            this.jobDataManager = mongoDbJobDataManager;
        }
        if (timerJobDataManager == null) {
            MongoDbTimerJobDataManager mongoDbTimerJobDataManager = new MongoDbTimerJobDataManager(this);
            this.timerJobDataManager = mongoDbTimerJobDataManager;
        }
        if (suspendedJobDataManager == null) {
            MongoDbSuspendedJobDataManager mongoDbSuspendedJobDataManager = new MongoDbSuspendedJobDataManager();
            this.suspendedJobDataManager = mongoDbSuspendedJobDataManager;
        }
        if (deadLetterJobDataManager == null) {
            MongoDbDeadLetterJobDataManager mongoDbDeadLetterJobDataManager = new MongoDbDeadLetterJobDataManager();
            this.deadLetterJobDataManager = mongoDbDeadLetterJobDataManager;
        }
        if (externalWorkerJobDataManager == null) {
            MongoDbExternalWorkerJobDataManager mongoDbExternalWorkerJobDataManager = new MongoDbExternalWorkerJobDataManager();
            this.externalWorkerJobDataManager = mongoDbExternalWorkerJobDataManager;
        }

        // TODO: history
    }

}
