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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.common.engine.impl.persistence.entity.Entity;
import org.flowable.variable.service.impl.InternalVariableInstanceQueryImpl;
import org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntity;
import org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntityImpl;
import org.flowable.variable.service.impl.persistence.entity.data.VariableInstanceDataManager;
import org.flowable.variable.service.impl.persistence.entity.data.impl.cachematcher.VariableInstanceByExecutionIdMatcher;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;

/**
 * @author Joram Barrez
 */
public class MongoDbVariableInstanceDataManager extends AbstractMongoDbDataManager<VariableInstanceEntity> implements VariableInstanceDataManager {

    public static final String COLLECTION_VARIABLES = "variables";

    protected VariableInstanceByExecutionIdMatcher variableInstanceByExecutionIdMatcher = new VariableInstanceByExecutionIdMatcher();

    @Override
    public String getCollection() {
        return COLLECTION_VARIABLES;
    }

    @Override
    public VariableInstanceEntity create() {
        return new VariableInstanceEntityImpl();
    }

    @Override
    public BasicDBObject createUpdateObject(Entity entity) {
        VariableInstanceEntity variableEntity = (VariableInstanceEntity) entity;
        BasicDBObject updateObject = null;
        updateObject = setUpdateProperty(variableEntity, "textValue", variableEntity.getTextValue(), updateObject);
        updateObject = setUpdateProperty(variableEntity, "textValue2", variableEntity.getTextValue2(), updateObject);
        updateObject = setUpdateProperty(variableEntity, "doubleValue", variableEntity.getDoubleValue(), updateObject);
        updateObject = setUpdateProperty(variableEntity, "longValue", variableEntity.getLongValue(), updateObject);
        updateObject = setUpdateProperty(variableEntity, "typeName", variableEntity.getTypeName(), updateObject);
        return updateObject;
    }


    @Override
    public List<VariableInstanceEntity> findVariablesInstancesByQuery(InternalVariableInstanceQueryImpl internalVariableInstanceQuery) {

        List<Bson> filters = new ArrayList<>();

        String name = internalVariableInstanceQuery.getName();
        if (name != null) {
            filters.add(Filters.eq("name", name));
        }

        String scopeId = internalVariableInstanceQuery.getScopeId();
        String scopeType = internalVariableInstanceQuery.getScopeType();
        if (scopeId != null && scopeType != null) {
            filters.add(Filters.eq("scopeId", scopeId));
            filters.add(Filters.eq("scopeType", scopeType));
        }

        String taskId = internalVariableInstanceQuery.getTaskId();
        if (taskId != null) {
            filters.add(Filters.eq("taskId", taskId));
        }

        String executionId = internalVariableInstanceQuery.getExecutionId();
        if (executionId != null) {
            filters.add(Filters.eq("executionId", executionId));
        }

        if (!filters.isEmpty()) {
            Bson finalFilter = Filters.and(filters.toArray(new Bson[0]));
            return getMongoDbSession().find(COLLECTION_VARIABLES, finalFilter);
        } else {
            return getMongoDbSession().find(COLLECTION_VARIABLES, null);
        }
    }

    @Override
    public VariableInstanceEntity findVariablesInstanceByQuery(InternalVariableInstanceQueryImpl internalVariableInstanceQuery) {
        List<VariableInstanceEntity> variableInstanceEntities = findVariablesInstancesByQuery(internalVariableInstanceQuery);
        if (variableInstanceEntities.size() > 1) {
            throw new FlowableIllegalArgumentException("Query returned more than one result: " + variableInstanceEntities);
        }
        if (variableInstanceEntities.size() == 1) {
            return variableInstanceEntities.get(0);
        }
        return null;
    }

    @Override
    public void deleteVariablesByTaskId(String taskId) {
        throw new UnsupportedOperationException();
    }

    protected List<VariableInstanceEntity> findVariableInstancesByExecutionId(String executionId) {
        return getMongoDbSession().find(COLLECTION_VARIABLES, Filters.eq("executionId", executionId), executionId,
            VariableInstanceEntityImpl.class, variableInstanceByExecutionIdMatcher, true);
    }

    @Override
    public void deleteVariablesByExecutionId(String executionId) {
        List<VariableInstanceEntity> variables = findVariableInstancesByExecutionId(executionId);
        if (variables != null) {
            for (VariableInstanceEntity variableInstanceEntity : variables) {
                getMongoDbSession().delete(COLLECTION_VARIABLES, variableInstanceEntity);
            }
        }
    }

    @Override
    public void deleteByScopeIdAndScopeType(String scopeId, String scopeType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteByScopeIdAndScopeTypes(String scopeId, Collection<String> scopeTypes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteBySubScopeIdAndScopeTypes(String subScopeId, Collection<String> scopeTypes) {
        // TODO
    }

    public VariableInstanceEntityImpl transformToEntity(Document document) {
        throw new UnsupportedOperationException();
    }

}
