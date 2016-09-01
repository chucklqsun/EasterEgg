package info.bartowski.easteregg.framework;/*
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

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.bartowski.easteregg.R;

public class Scores extends AsyncTask<Object, String, String> {
    private static Context context;
    private byte curOpt;

    private static ListView lv;
    private static SimpleAdapter simpleAdapter;

    private final static int COLUMN_ID = 1;
    private final static int COLUMN_DEV_SERIAL_NUM = 2;
    private final static int COLUMN_WK_MAX = 3;
    private final static int COLUMN_HISTORY_MAX = 5;
    private final static int COLUMN_NICKNAME = 7;

    public final static int DEV_SERIAL_NUM_SIZE = 15;
    public final static int WK_MAX_SIZE = 4;
    private final static int HISTORY_MAX_SIZE = 4;
    public final static int NICKNAME_SIZE = 24;
    private final static int WK_RANK_SIZE = DEV_SERIAL_NUM_SIZE+WK_MAX_SIZE+NICKNAME_SIZE;
    private final static int HISTORY_RANK_SIZE = DEV_SERIAL_NUM_SIZE+HISTORY_MAX_SIZE+NICKNAME_SIZE;

    private final static int RANK_SIZE = 10; //show top 10

    public final static byte DATA_TYPE_WKMAX = 1;

    public final static byte UPDATE_SCORE_ERROR = 0x1;
    public final static byte UPDATE_SCORE_SUCCESS = 0x0;

    public Scores(Context context) {
        super();
        Scores.context = context;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        switch (curOpt){
            case Config.FUNC.GET_SCORES:
                lv.setAdapter(simpleAdapter);
                break;
            case Config.FUNC.UPDATE_SCORE:
                break;
            default:
                Toast.makeText(context,"lost connect with server",Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] buildUpdWkMax(Package p,int myScore,String IMEI,String myNickname){
        byte[] score = ByteBuffer.allocate(Scores.WK_MAX_SIZE).putInt(myScore).array();

        byte[] pars = new byte[Package.PAR_SIZE];
        System.arraycopy(score, 0, pars, 0, Scores.WK_MAX_SIZE);

        byte[] nickname = myNickname.getBytes();
        nickname = Utility.trimBytes(nickname);

        byte[] devSerialNo = IMEI.getBytes();
        devSerialNo = Utility.trimBytes(devSerialNo);

        byte[] data = new byte[Scores.NICKNAME_SIZE + Scores.DEV_SERIAL_NUM_SIZE];
        System.arraycopy(nickname, 0, data, 0, nickname.length);
        System.arraycopy(devSerialNo, 0, data, Scores.NICKNAME_SIZE, devSerialNo.length);

        return p.build(new byte[]{Config.FUNC.UPDATE_SCORE}, pars, data);
    }

    public static List<Record> unWrapData(byte dataType,byte[] par, byte[] data) {
//        System.out.println(Arrays.toString(par));
//        System.out.println(Arrays.toString(data));

        List<Record> list = new ArrayList<>();
        byte[] cnt = new byte[4];
        System.arraycopy(par,0,cnt,0,4);
        ByteBuffer cnt_b = ByteBuffer.wrap(cnt);
        int count = cnt_b.getInt();

        for (int i = 0; i < count; i++) {
            switch (dataType) {
                case DATA_TYPE_WKMAX:
                    int pos = i * WK_RANK_SIZE;

                    byte[] dnsBytes = new byte[DEV_SERIAL_NUM_SIZE];
                    System.arraycopy(data, pos, dnsBytes, 0, DEV_SERIAL_NUM_SIZE);
                    dnsBytes = Utility.trimBytes(dnsBytes);
                    String dns = new String(dnsBytes);

                    byte[] nnBytes = new byte[NICKNAME_SIZE];
                    System.arraycopy(data, pos+DEV_SERIAL_NUM_SIZE, nnBytes, 0, NICKNAME_SIZE);
                    nnBytes = Utility.trimBytes(nnBytes);
                    String nn = new String(nnBytes);

                    byte[] wmBytes = new byte[WK_MAX_SIZE];
                    System.arraycopy(data, pos+DEV_SERIAL_NUM_SIZE+NICKNAME_SIZE, wmBytes, 0, WK_MAX_SIZE);
                    ByteBuffer wmBuf = ByteBuffer.wrap(wmBytes);

                    Record r = new Record(dns, nn);
                    r.setWkMax(wmBuf.getInt());

                    list.add(r);
                    break;
            }
        }
        return list;
    }

    private static void resolveBuf(byte type, Package p) {
        switch (type) {
            case Config.FUNC.UPDATE_SCORE:
                byte[] ret = p.getPar();
                if(ret[0] == Scores.UPDATE_SCORE_SUCCESS){
                    System.out.println("update score success");
                }else if(ret[0] == Scores.UPDATE_SCORE_ERROR){
                    System.out.println("update score fail");
                }else{
                    System.out.println("unknown return status");
                }
                break;
            case Config.FUNC.GET_SCORES:
                List<Record> list = Scores.unWrapData(Scores.DATA_TYPE_WKMAX, p.getPar(), p.getData());
                List<Map<String,Object>> listItems = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    System.out.print(
                            list.get(i).getDevSerialNum() + "\t" +
                                    list.get(i).getNickname() + "\t" +
                                    list.get(i).getWkMax() + "\n"
                    );

                    Map<String,Object> listItem = new HashMap<>();
                    listItem.put("rank_no",i+1);
                    listItem.put("nickname",list.get(i).getNickname());
                    listItem.put("score",list.get(i).getWkMax());
                    listItems.add(listItem);
                }
                simpleAdapter = new SimpleAdapter(
                        context,
                        listItems,
                        R.layout.lland_rank,
                        new String[]{"rank_no","nickname","score"},
                        new int[]{R.id.rank_no,R.id.nickname,R.id.score}
                );
                break;
            default:
                System.out.println("nothing need resolve:"+type);
        }
    }

    //p1:func
    @Override
    protected String doInBackground(Object... params) {
        DatagramPacket packet;
        Transfer trans = new Transfer();
        Package p = new Package();
        byte actionType;
        if(params.length == 0) {
            return null;
        }

        curOpt = (Byte) params[0];

        byte[] res_buf;
        try {
            trans.init();
            switch (curOpt){
                case Config.FUNC.GET_SCORES:
                    actionType = (byte)params[1]; //data_type:wk or history
                    res_buf = p.build(new byte[]{curOpt}, new byte[]{actionType});
                    break;
                case Config.FUNC.UPDATE_SCORE:
                    res_buf = buildUpdWkMax(p,(int)params[1],(String)params[2],(String)params[3]);
                    break;
                default:
                    System.out.println("nothing need act for score");
                    return null;
            }
            packet = trans.send(res_buf, Package.DATA_SIZE);
            // display response
            p.resolve(packet);

            switch (curOpt) {
                case Config.FUNC.GET_SCORES:
                    Scores.lv = (ListView) params[2];
                    break;
                default:
            }
            resolveBuf(curOpt, p);

        } catch (Exception e) {
            curOpt = 0x0;
            System.out.println(e.getMessage());
        }

        trans.close();
        return null;
    }

}
