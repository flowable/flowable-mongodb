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
package org.flowable.mongodb.persistence.entity;

import java.util.Map;

import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;

/**
 * @author Joram Barrez
 */
public class MongoDbProcessDefinitionEntityImpl extends ProcessDefinitionEntityImpl {

    protected boolean latest;

    @Override
    public Object getPersistentState() {
        Map<String, Object> persistentState = (Map<String, Object>) super.getPersistentState();
        persistentState.put("latest", latest);
        return persistentState;
    }

    public boolean isLatest() {
        return latest;
    }

    public void setLatest(boolean latest) {
        this.latest = latest;
    }

}
