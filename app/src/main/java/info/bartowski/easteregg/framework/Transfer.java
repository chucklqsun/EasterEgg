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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class Transfer {
    private InetAddress address;
    DatagramSocket socket;

    public void init() throws Exception{
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(Config.UDP_TIME_OUT);
            address = InetAddress.getByName(Config.getHost());
        }catch (IOException e){
            throw e;
        }
    }

    public DatagramPacket send(byte[] buf,int revDataSize) throws Exception{
        try {
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, Config.PORT);
            socket.send(packet);

            // get response
            byte[] rev = new byte[Package.DATA_POS + revDataSize];
            packet = new DatagramPacket(rev, rev.length);
            socket.receive(packet);

            return packet;

        }catch (SocketTimeoutException e) {
            throw new Exception("check timeout");
        }catch (IOException e){
            throw new Exception("network error");
        }
    }

    public void close(){
        socket.close();
    }
}
