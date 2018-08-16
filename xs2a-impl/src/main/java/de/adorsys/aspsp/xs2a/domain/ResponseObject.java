/*
 * Copyright 2018-2018 adorsys GmbH & Co KG
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

package de.adorsys.aspsp.xs2a.domain;

import de.adorsys.psd2.model.TppMessageGeneric;
import de.adorsys.psd2.model.TppMessages;
import lombok.Getter;

import java.util.List;

/**
 * Response Object passing the information about performed operation
 *
 * @see de.adorsys.psd2.model.TppMessages
 */
@Getter
public class ResponseObject<T> {
    private final T body;
    private final List<TppMessageGeneric> error;

    private ResponseObject(ResponseBuilder<T> builder) {
        this.body = builder.body;
        this.error = builder.error;
    }

    public static <T> ResponseBuilder<T> builder(){
        return new ResponseBuilder<>();
    }

    public static class ResponseBuilder<T> {
        private T body;
        private List<TppMessageGeneric> error;

        public ResponseBuilder<T> body(T body){
            this.body = body;
            return this;
        }

        public ResponseBuilder<T> fail(List<TppMessageGeneric> messages){
            this.error = messages;
            return this;
        }

        public ResponseObject<T> build() {
            return new ResponseObject<>(this);
        }
    }

    public boolean hasError(){
        return error != null;
    }
}
