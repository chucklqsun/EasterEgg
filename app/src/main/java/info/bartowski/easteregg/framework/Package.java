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

import java.net.DatagramPacket;

public class Package {
    private final static String LOG_TAG = Package.class.getSimpleName();
    public final static byte ERROR = 0;
    public final static int MAX_SIZE = 548;

    //call name;feed name
    public static int TYPE_POS = 0;
    public static int TYPE_SIZE = 1;

    //call version for compatible;feed version
    public static int VER_POS = TYPE_POS + TYPE_SIZE;
    public static int VER_SIZE = 1;

    //call params;feed reserved
    public static int PAR_POS = VER_POS + VER_SIZE;
    public static int PAR_SIZE = 10;

    //call key;feed key
    public static int KEY_POS = PAR_POS + PAR_SIZE;
    public static int KEY_SIZE = 16;

    //call reserved;feed data
    public static int DATA_POS = KEY_POS + KEY_SIZE;
    public static int DATA_SIZE = MAX_SIZE - DATA_POS;

    private byte type[] = new byte[TYPE_SIZE];
    private byte ver[] = new byte[VER_SIZE];
    private byte par[] = new byte[PAR_SIZE];
    private byte key[] = new byte[KEY_SIZE];
    private byte data[] = new byte[DATA_SIZE];

    private Error error;

    /**
     * resolve request packet
     *
     * @param packet
     */
    public void resolve(DatagramPacket packet) {
        byte[] input = packet.getData();
        int actual_size = input.length;
        if(actual_size> MAX_SIZE){
            this.type[0] = Package.ERROR;
            error = new Error("Packet Too Large");
        }
        if(actual_size< DATA_POS-1){
            this.type[0] = Package.ERROR;
            error = new Error("Packet Too Small");
        }
        System.arraycopy(input,KEY_POS,this.key,0,KEY_SIZE);
        //todo verify key

        System.arraycopy(input,TYPE_POS,this.type,0,TYPE_SIZE);
        System.arraycopy(input,VER_POS,this.ver,0,VER_SIZE);
        System.arraycopy(input,PAR_POS,this.par,0,PAR_SIZE);
        System.arraycopy(input,DATA_POS,this.data,0,input.length-DATA_POS);
    }


    public byte[] compatBuild(byte[] type, byte[] ver, byte[] par, byte[] data) {
        //protocol section + data section
        byte ret[] = new byte[DATA_POS + data.length];
        System.arraycopy(type, 0, ret, TYPE_POS, TYPE_SIZE);
        System.arraycopy(ver, 0, ret, VER_POS, VER_SIZE);
        System.arraycopy(par, 0, ret, PAR_POS, PAR_SIZE);
        //todo add key

        System.arraycopy(data, 0, ret, DATA_POS, data.length);

        return ret;
    }

    public byte[] build(byte[] type){
        //protocol section + data section
        byte ret[] = new byte[DATA_POS];
        System.arraycopy(type,0,ret,TYPE_POS,type.length);
        //todo add key

        return ret;
    }

    public byte[] build(byte[] type,byte[] par){
        //protocol section + data section
        byte ret[] = new byte[DATA_POS];
        System.arraycopy(type,0,ret,TYPE_POS,type.length);
        System.arraycopy(par,0,ret,PAR_POS,par.length);
        //todo add key

        return ret;
    }

    public byte[] build(byte[] type,byte[] par,byte[] data){
        //protocol section + data section
        byte ret[] = new byte[DATA_POS+data.length];
        System.arraycopy(type,0,ret,TYPE_POS,type.length);
        System.arraycopy(par,0,ret,PAR_POS,par.length);
        System.arraycopy(data,0,ret,DATA_POS,data.length);
        //todo add key

        return ret;
    }


    public byte[] getData() {
        return data;
    }

    public byte getType() {
        return type[0];
    }

    private void setType(byte t) {
        type[0] = t;
    }

    //todo add key algorithm
    private byte[] getKey() {
        return new byte[KEY_SIZE];
    }

    public byte[] getPar(){
        return par;
    }

    public Error getError() {
        return error;
    }

}
