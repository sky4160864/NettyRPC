/**
 * Copyright (C) 2018 Newland Group Holding Limited
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newlandframework.rpc.core;

import com.newlandframework.rpc.model.MessageRequest;

/**
 * @author tangjie<https://github.com/tang-jie>
 * @filename:DefaultModular.java
 * @description:DefaultModular功能模块
 * @blogs http://www.cnblogs.com/jietang/
 * @since 2018/2/1
 */
public class DefaultModular implements Modular {
    @Override
    public <T> ModuleProvider<T> invoke(final ModuleInvoker<T> invoker, MessageRequest request) {
        return new ModuleProvider<T>() {
            @Override
            public ModuleInvoker<T> getInvoker() {
                return invoker;
            }

            @Override
            public void destoryInvoker() {
                invoker.destroy();
            }
        };
    }
}

