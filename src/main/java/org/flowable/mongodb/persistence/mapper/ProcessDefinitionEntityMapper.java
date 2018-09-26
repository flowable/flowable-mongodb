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
import org.flowable.mongodb.persistence.entity.MongoDbProcessDefinitionEntityImpl;

/**
 * @author Joram Barrez
 */
public class ProcessDefinitionEntityMapper extends AbstractEntityToDocumentMapper<MongoDbProcessDefinitionEntityImpl> {

    @Override
    public MongoDbProcessDefinitionEntityImpl fromDocument(Document document) {
        MongoDbProcessDefinitionEntityImpl processDefinitionEntity = new MongoDbProcessDefinitionEntityImpl();
        processDefinitionEntity.setId(document.getString("_id"));
        processDefinitionEntity.setRevision(document.getInteger("revision"));
        processDefinitionEntity.setName(document.getString("name"));
        processDefinitionEntity.setDescription(document.getString("description"));
        processDefinitionEntity.setKey(document.getString("key"));
        processDefinitionEntity.setVersion(document.getInteger("version", 1));
        processDefinitionEntity.setCategory(document.getString("category"));
        processDefinitionEntity.setDeploymentId(document.getString("deploymentId"));
        processDefinitionEntity.setResourceName(document.getString("resourceName"));
        processDefinitionEntity.setTenantId(document.getString("tenantId"));
        processDefinitionEntity.setHistoryLevel(document.getInteger("historyLevel"));
        processDefinitionEntity.setDiagramResourceName(document.getString("diagramResourceName"));
        processDefinitionEntity.setGraphicalNotationDefined(document.getBoolean("isGraphicalNotationDefined", false));
        processDefinitionEntity.setHasStartFormKey(document.getBoolean("hasStartFormKey"));
        processDefinitionEntity.setSuspensionState(document.getInteger("suspensionState"));
        processDefinitionEntity.setDerivedFrom(document.getString("derivedFrom"));
        processDefinitionEntity.setDerivedFromRoot(document.getString("derivedFromRoot"));
        processDefinitionEntity.setDerivedVersion(document.getInteger("derivedVersion"));
        processDefinitionEntity.setEngineVersion(document.getString("engineVersion"));

        // Mongo impl specific
        processDefinitionEntity.setLatest(document.getBoolean("latest"));

        return processDefinitionEntity;
    }

    @Override
    public Document toDocument(MongoDbProcessDefinitionEntityImpl processDefinitionEntity) {
        Document processDefinitionDocument = new Document();
        appendIfNotNull(processDefinitionDocument, "_id", processDefinitionEntity.getId());
        appendIfNotNull(processDefinitionDocument, "revision", processDefinitionEntity.getRevision());
        appendIfNotNull(processDefinitionDocument, "name", processDefinitionEntity.getName());
        appendIfNotNull(processDefinitionDocument, "description", processDefinitionEntity.getDescription());
        appendIfNotNull(processDefinitionDocument, "key", processDefinitionEntity.getKey());
        appendIfNotNull(processDefinitionDocument, "version", processDefinitionEntity.getVersion());
        appendIfNotNull(processDefinitionDocument, "category", processDefinitionEntity.getCategory());
        appendIfNotNull(processDefinitionDocument, "deploymentId", processDefinitionEntity.getDeploymentId());
        appendIfNotNull(processDefinitionDocument, "resourceName", processDefinitionEntity.getResourceName());
        appendIfNotNull(processDefinitionDocument, "tenantId", processDefinitionEntity.getTenantId());
        appendIfNotNull(processDefinitionDocument, "historyLevel", processDefinitionEntity.getHistoryLevel());
        appendIfNotNull(processDefinitionDocument, "diagramResourceName", processDefinitionEntity.getDiagramResourceName());
        appendIfNotNull(processDefinitionDocument, "isGraphicalNotationDefined", processDefinitionEntity.isGraphicalNotationDefined());
        appendIfNotNull(processDefinitionDocument, "hasStartFormKey", processDefinitionEntity.getHasStartFormKey());
        appendIfNotNull(processDefinitionDocument, "suspensionState", processDefinitionEntity.getSuspensionState());
        appendIfNotNull(processDefinitionDocument, "derivedFrom", processDefinitionEntity.getDerivedFrom());
        appendIfNotNull(processDefinitionDocument, "derivedFromRoot", processDefinitionEntity.getDerivedFromRoot());
        appendIfNotNull(processDefinitionDocument, "derivedVersion", processDefinitionEntity.getDerivedVersion());
        appendIfNotNull(processDefinitionDocument, "engineVersion", processDefinitionEntity.getEngineVersion());

        // Mongo impl specific
        appendIfNotNull(processDefinitionDocument, "latest", processDefinitionEntity.isLatest());

        return processDefinitionDocument;
    }

}
