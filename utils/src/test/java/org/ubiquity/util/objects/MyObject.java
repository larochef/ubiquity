/**
 * Copyright 2012 ubiquity-copy

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.ubiquity.util.objects;

import java.util.List;

/**
 *
 */
public class MyObject {

    private String property1;
    private int property2;
    private List<String> property3;

    @WithArguments(enumValue = WithArgumentEnum.VALUE1, stringValue = "toto")
    @WithSimpleArray({"value1", "value2"})
    public String getProperty1() {
        return property1;
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    @Simple
    public int getProperty2() {
        return property2;
    }

    public void setProperty2(int property2) {
        this.property2 = property2;
    }

    public List<String> getProperty3() {
        return property3;
    }

    public void setProperty3(List<String> property3) {
        this.property3 = property3;
    }

    public void doTheMagic() {}
}
