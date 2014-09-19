/*
* Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.wso2.developerstudio.cloud.core.client.ui.dashboard;

import com.codenvy.ide.api.ui.action.Action;

public class DashboardItem {

    private DashboardCategory category;
    private String  name;
    private Action action;

    public DashboardItem(DashboardCategory category, String name, Action action) {
        this.category = category;
        this.name = name;
        this.action = action;
    }

    public DashboardCategory getCategory() {
        return category;
    }

    public void setCategory(DashboardCategory category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
