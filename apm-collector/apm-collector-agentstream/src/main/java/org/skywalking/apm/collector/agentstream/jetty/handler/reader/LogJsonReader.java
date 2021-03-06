/*
 * Copyright 2017, OpenSkywalking Organization All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Project repository: https://github.com/OpenSkywalking/skywalking
 */

package org.skywalking.apm.collector.agentstream.jetty.handler.reader;

import com.google.gson.stream.JsonReader;
import java.io.IOException;
import org.skywalking.apm.network.proto.LogMessage;

/**
 * @author peng-yongsheng
 */
public class LogJsonReader implements StreamJsonReader<LogMessage> {

    private KeyWithStringValueJsonReader keyWithStringValueJsonReader = new KeyWithStringValueJsonReader();

    private static final String TIME = "ti";
    private static final String LOG_DATA = "ld";

    @Override public LogMessage read(JsonReader reader) throws IOException {
        LogMessage.Builder builder = LogMessage.newBuilder();

        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case TIME:
                    builder.setTime(reader.nextLong());
                case LOG_DATA:
                    reader.beginArray();
                    while (reader.hasNext()) {
                        builder.addData(keyWithStringValueJsonReader.read(reader));
                    }
                    reader.endArray();
                default:
                    reader.skipValue();
            }
        }

        return builder.build();
    }
}
