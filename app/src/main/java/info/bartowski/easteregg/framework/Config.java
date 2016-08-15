/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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

package info.bartowski.easteregg.framework;

import info.bartowski.easteregg.BuildConfig;

public class Config {
    public final static String HOST = BuildConfig.HOST;
    public final static int PORT = BuildConfig.PORT;

    public class FUNC {
        public final static byte CHECK_APP_VERSION = 1;
    }

    public class Uri {
        public final static String SCHEME = "https";
        public final static String TCP_PORT = "80";
        public final static String DOMAIN = "bartowski.info";
        public final static String SUB_DOMAIN = "www";

        public final static String APP_SITE = SCHEME + "://" + SUB_DOMAIN + "." + DOMAIN + "/";


    }
}
