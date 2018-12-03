/*
 * Copyright (C) 2015 Bilibili
 * Copyright (C) 2015 Zhang Rui <bbcallen@gmail.com>
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

package tv.danmaku.ijk.media.example.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Response;
import tv.danmaku.ijk.media.example.R;
import tv.danmaku.ijk.media.example.activities.VideoActivity;
import tv.danmaku.ijk.media.example.util.ConstHttp;
import tv.danmaku.ijk.media.example.util.HttpUtil;
import tv.danmaku.ijk.media.example.util.JsonData;
import tv.danmaku.ijk.media.example.util.MakeRequestMap;

/**
 * 高空摄像头10.13.17.11; 10.13.25.11
 */
public class SampleMediaListFragment extends Fragment implements View.OnClickListener{
    private ListView mFileListView;
    private SampleMediaAdapter mAdapter;

    public static SampleMediaListFragment newInstance() {
        SampleMediaListFragment f = new SampleMediaListFragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_file_list, container, false);
        mFileListView = (ListView) viewGroup.findViewById(R.id.file_list_view);
        return viewGroup;
    }

    private void jumpActivityByInputIp()
    {
        EditText editText = new EditText(getActivity());
        editText.setText("10.13.27.7"); //25.11   17.11 正常：10.13.19.11
        editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        new AlertDialog.Builder(getActivity())
                .setTitle("请输入摄像头IP")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(editText )
                .setPositiveButton("确定", (dialog, which) -> {
                    String ip = editText.getText().toString();
                    if (TextUtils.isEmpty(ip))
                    {
                        Toast.makeText(getContext(),"请录入IP",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String url = "rtsp://admin:admin@"+ip+":554";
                    doJumpActivity("liuyi test play over VPN",url);

                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void doJumpActivity(String name,String url)
    {
        VideoActivity.intentTo(getActivity(), url, name);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //final Activity activity = getActivity();
        mAdapter = new SampleMediaAdapter(getActivity());
        mFileListView.setAdapter(mAdapter);
        mFileListView.setOnItemClickListener((parent, view, position, id) -> {
            if (position==0){
                jumpActivityByInputIp();
            }else {
                SampleMediaItem item = mAdapter.getItem(position);
                doJumpActivity(item.mUrl, item.mName);
            }
        });

        //mAdapter.addItem("rtsp://admin:admin@10.13.27.7:554","liuyi test play over VPN");
        mAdapter.addItem("rtsp://admin:admin@youIp:554","liuyi test play over VPN");
        mAdapter.addItem("rtsp://nvs:nvs@nvs.3322.org:554","vs.3322.org:");
        mAdapter.addItem("rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov","rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov");
        mAdapter.addItem("rtsp://113.136.42.45:554/PLTV/88888888/224/3221226087/10000100000000060000000001759104_0.smil"," test play ");

        mAdapter.addItem("rtmp://pull-g.kktv8.com/livekktv/100987038", "liuyi test play rtmp");

        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8", "bipbop basic master playlist");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear1/prog_index.m3u8", "bipbop basic 400x300 @ 232 kbps");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear2/prog_index.m3u8", "bipbop basic 640x480 @ 650 kbps");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear3/prog_index.m3u8", "bipbop basic 640x480 @ 1 Mbps");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear4/prog_index.m3u8", "bipbop basic 960x720 @ 2 Mbps");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear0/prog_index.m3u8", "bipbop basic 22.050Hz stereo @ 40 kbps");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/bipbop_16x9_variant.m3u8", "bipbop advanced master playlist");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/gear1/prog_index.m3u8", "bipbop advanced 416x234 @ 265 kbps");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/gear2/prog_index.m3u8", "bipbop advanced 640x360 @ 580 kbps");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/gear3/prog_index.m3u8", "bipbop advanced 960x540 @ 910 kbps");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/gear4/prog_index.m3u8", "bipbop advanced 1289x720 @ 1 Mbps");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/gear5/prog_index.m3u8", "bipbop advanced 1920x1080 @ 2 Mbps");
        mAdapter.addItem("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/gear0/prog_index.m3u8", "bipbop advanced 22.050Hz stereo @ 40 kbps");
    }

    @Override
    public void onClick(View v) {
        doGetRtspUrl();
    }


    private void doGetRtspUrl() {
        JsonData para = JsonData.create("{}");
        para.put("userName","tongfu001");
        para.put("password","login");
        Map<String,String> map = MakeRequestMap.makeRequestMap("getRtspURL",para);
        HttpUtil.postFormBody(this,ConstHttp.HOST, map, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                getActivity().runOnUiThread(() -> {
                    try {
                        String resultString = response.body().string();
                        if (resultString==null)
                        {
                          Toast.makeText(getContext(),"返回 null",Toast.LENGTH_SHORT).show();
                            return ;
                        }

                        if (resultString.contains("rtsp"))
                        {
                            VideoActivity.intentTo(SampleMediaListFragment.this.getActivity(), resultString, "Huawei RTSP");
                        }else{
                            Toast.makeText(getContext(),"没有 rtsp 返回 :"+resultString,Toast.LENGTH_SHORT).show();
                        }
                        Log.v("---->", resultString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });


    }

    final class SampleMediaItem {
        String mUrl;
        String mName;

        public SampleMediaItem(String url, String name) {
            mUrl = url;
            mName = name;
        }
    }

    final class SampleMediaAdapter extends ArrayAdapter<SampleMediaItem> {
        public SampleMediaAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2);
        }

        public void addItem(String url, String name) {
            add(new SampleMediaItem(url, name));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
            }

            ViewHolder viewHolder = (ViewHolder) view.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder();
                viewHolder.mNameTextView = (TextView) view.findViewById(android.R.id.text1);
                viewHolder.mUrlTextView = (TextView) view.findViewById(android.R.id.text2);
            }

            SampleMediaItem item = getItem(position);
            viewHolder.mNameTextView.setText(item.mName);
            viewHolder.mUrlTextView.setText(item.mUrl);

            return view;
        }

        final class ViewHolder {
            public TextView mNameTextView;
            public TextView mUrlTextView;
        }
    }
}
