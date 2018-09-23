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

import java.util.List;

import org.flowable.common.engine.impl.persistence.entity.Entity;
import org.flowable.job.service.impl.persistence.entity.JobByteArrayEntity;
import org.flowable.job.service.impl.persistence.entity.JobByteArrayEntityImpl;
import org.flowable.job.service.impl.persistence.entity.data.JobByteArrayDataManager;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;

/**
 * @author Joram Barrez
 */
public class MongoDbJobByteArrayDataManager extends AbstractMongoDbDataManager<JobByteArrayEntity> implements JobByteArrayDataManager {

    // In the regular persistency, all byte arrays go to one specific table
    // This is probably not the best idea for MongoDB.
    // Mimicking it for now, but this is probably not the best solution.
    public static final String COLLECTION_JOB_BYTE_ARRAY = MongoDbResourceDataManager.COLLECTION_BYTE_ARRAY;

    @Override
    public String getCollection() {
        return COLLECTION_JOB_BYTE_ARRAY;
    }

    @Override
    public JobByteArrayEntity create() {
        return new JobByteArrayEntityImpl();
    }

    @Override
    public BasicDBObject createUpdateObject(Entity entity) {
        return null;
    }

    @Override
    public List<JobByteArrayEntity> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteByteArrayNoRevisionCheck(String byteArrayEntityId) {
        getMongoDbSession().bulkDelete(COLLECTION_JOB_BYTE_ARRAY, Filters.eq("_id", byteArrayEntityId));
    }

}