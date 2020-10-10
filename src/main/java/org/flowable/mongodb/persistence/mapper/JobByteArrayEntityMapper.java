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
package org.flowable.mongodb.persistence.mapper;

import org.bson.Document;
import org.bson.types.Binary;
import org.flowable.common.engine.impl.persistence.entity.ByteArrayEntityImpl;

/**
 * @author Joram Barrez
 */
public class JobByteArrayEntityMapper extends AbstractJobEntityMapper<ByteArrayEntityImpl> {

    @Override
    public ByteArrayEntityImpl fromDocument(Document document) {
        ByteArrayEntityImpl jobByteArrayEntity = new ByteArrayEntityImpl();
        jobByteArrayEntity.setId(document.getString("_id"));
        jobByteArrayEntity.setName(document.getString("name"));
        jobByteArrayEntity.setDeploymentId(document.getString("deploymentId"));

        Binary binary = (Binary) document.get("bytes");
        jobByteArrayEntity.setBytes(binary.getData());
        return jobByteArrayEntity;
    }

    @Override
    public Document toDocument(ByteArrayEntityImpl jobByteArrayEntity) {
        Document document = new Document();
        appendIfNotNull(document, "_id", jobByteArrayEntity.getId());
        appendIfNotNull(document, "name", jobByteArrayEntity.getName());
        appendIfNotNull(document, "deploymentId", jobByteArrayEntity.getDeploymentId());
        appendIfNotNull(document, "bytes", jobByteArrayEntity.getBytes());
        return document;
    }

}
