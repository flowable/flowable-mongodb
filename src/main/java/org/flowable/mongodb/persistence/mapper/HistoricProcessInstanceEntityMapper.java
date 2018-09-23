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
import org.flowable.engine.impl.persistence.entity.HistoricProcessInstanceEntityImpl;
import org.flowable.mongodb.persistence.EntityToDocumentMapper;

/**
 * @author Tijs Rademakers
 */
public class HistoricProcessInstanceEntityMapper extends AbstractEntityToDocumentMapper<HistoricProcessInstanceEntityImpl> {

    @Override
    public HistoricProcessInstanceEntityImpl fromDocument(Document document) {
        HistoricProcessInstanceEntityImpl historicProcessInstanceEntity = new HistoricProcessInstanceEntityImpl();
        historicProcessInstanceEntity.setId(document.getString("_id"));
        historicProcessInstanceEntity.setRevision(document.getInteger("revision"));
        historicProcessInstanceEntity.setProcessInstanceId(document.getString("processInstanceId"));
        historicProcessInstanceEntity.setBusinessKey(document.getString("businessKey"));
        historicProcessInstanceEntity.setProcessDefinitionId(document.getString("processDefinitionId"));
        historicProcessInstanceEntity.setCallbackId(document.getString("callbackId"));
        historicProcessInstanceEntity.setCallbackType(document.getString("callbackType"));
        historicProcessInstanceEntity.setDeleteReason(document.getString("deleteReason"));
        historicProcessInstanceEntity.setDurationInMillis(document.getLong("durationInMillis"));
        historicProcessInstanceEntity.setEndActivityId(document.getString("endActivityId"));
        historicProcessInstanceEntity.setEndTime(document.getDate("endTime"));
        historicProcessInstanceEntity.setName(document.getString("name"));
        historicProcessInstanceEntity.setRevision(document.getInteger("revision"));
        historicProcessInstanceEntity.setStartActivityId(document.getString("startActivityId"));
        historicProcessInstanceEntity.setStartTime(document.getDate("startTime"));
        historicProcessInstanceEntity.setStartUserId(document.getString("startUserId"));
        historicProcessInstanceEntity.setSuperProcessInstanceId(document.getString("superProccessInstanceId"));
        historicProcessInstanceEntity.setTenantId(document.getString("tenantId"));
        
        return historicProcessInstanceEntity;
    }

    @Override
    public Document toDocument(HistoricProcessInstanceEntityImpl historicProcessInstanceEntity) {
        Document historicProcessInstanceDocument = new Document();
        appendIfNotNull(historicProcessInstanceDocument, "_id", historicProcessInstanceEntity.getId());
        appendIfNotNull(historicProcessInstanceDocument, "revision", historicProcessInstanceEntity.getRevision());
        appendIfNotNull(historicProcessInstanceDocument, "processInstanceId", historicProcessInstanceEntity.getProcessInstanceId());
        appendIfNotNull(historicProcessInstanceDocument, "businessKey", historicProcessInstanceEntity.getBusinessKey());
        appendIfNotNull(historicProcessInstanceDocument, "processDefinitionId", historicProcessInstanceEntity.getProcessDefinitionId());
        appendIfNotNull(historicProcessInstanceDocument, "callbackId", historicProcessInstanceEntity.getCallbackId());
        appendIfNotNull(historicProcessInstanceDocument, "callbackType", historicProcessInstanceEntity.getCallbackType());
        appendIfNotNull(historicProcessInstanceDocument, "deleteReason", historicProcessInstanceEntity.getDeleteReason());
        appendIfNotNull(historicProcessInstanceDocument, "durationInMillis", historicProcessInstanceEntity.getDurationInMillis());
        appendIfNotNull(historicProcessInstanceDocument, "endActivityId", historicProcessInstanceEntity.getEndActivityId());
        appendIfNotNull(historicProcessInstanceDocument, "endTime", historicProcessInstanceEntity.getEndTime());
        appendIfNotNull(historicProcessInstanceDocument, "name", historicProcessInstanceEntity.getName());
        appendIfNotNull(historicProcessInstanceDocument, "revision", historicProcessInstanceEntity.getRevision());
        appendIfNotNull(historicProcessInstanceDocument, "startActivityId", historicProcessInstanceEntity.getStartActivityId());
        appendIfNotNull(historicProcessInstanceDocument, "startTime", historicProcessInstanceEntity.getStartTime());
        appendIfNotNull(historicProcessInstanceDocument, "startUserId", historicProcessInstanceEntity.getStartUserId());
        appendIfNotNull(historicProcessInstanceDocument, "superProccessInstanceId", historicProcessInstanceEntity.getSuperProcessInstanceId());
        appendIfNotNull(historicProcessInstanceDocument, "tenantId", historicProcessInstanceEntity.getTenantId());
        
        return historicProcessInstanceDocument;
    }

}
