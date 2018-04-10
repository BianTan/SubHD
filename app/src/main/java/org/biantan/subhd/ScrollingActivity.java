package org.biantan.subhd;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScrollingActivity extends AppCompatActivity {


    private List<Zmxx> zmxxList = new ArrayList<>();
    private String dz1;
    private String dzname;
    private int dz1a;
    private String dz2;
    private String dz3;
    private String dz4;
    private String xinxi1;
    private String[] xinxi;
    private ProgressDialog dialog;
    private String nf;
    private String lx;
    private String dy;
    private String yy;
    private String js;
    private String fs;
    private int fsa;
    private String rs;
    private String zyxz = "1";
    private String zmxx;
    private OkHttpClient client;
    private String wb;
    private String zydown;
    public static final int UPDATE_TEXT2 = 1;
    public static final int ZM_DOWN = 2;
    private String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //初始化
        initFruits();
        initOkHttp();

        // RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //点击下载按钮
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] items = {"字幕下载", "资源下载"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(ScrollingActivity.this);
                dialog.setTitle("选择下载内容")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("选择: ", items[which]);
                                if (items[which] == "字幕下载") {
                                    if (isConnectIsNomarl()) { //判断网络是否连接
                                        new Thread(zmdown).start();  // 启动子线程获取连接
                                    } else {
                                    }
                                } else if (items[which] == "资源下载") {
                                    if (zyxz != "1") {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(zydown));
                                        startActivity(intent);
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ScrollingActivity.this);
                                        builder.setTitle("提示")
                                                .setMessage("此资源暂无下载_(:3...")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .show();
                                    }
                                }
                            }
                        });
                dialog.show();
            }
        });
    }

    //按下Back
    @Override
    public void onBackPressed() {
        finish();
    }

    Runnable zmdown = new Runnable() {
        @Override
        public void run() {
            wb = dz3.substring(dz3.indexOf("ar0") + 4);

            Message message = new Message();
            message.what = ZM_DOWN;
            handler.sendMessage(message); // 将 Message 对象发送出去
        }
    };

    //初始化Post
    private void initOkHttp() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    //Post
    public void doPost(String value) {
        RequestBody requestBodyPost = new FormBody.Builder()
                .add("sub_id", value)
                .build();
        Request requestPost = new Request.Builder()
                .url("http://subhd.com/ajax/down_ajax")
                .post(requestBodyPost)
                .build();
        executeRequest(requestPost);
    }

    //html处理
    private void executeRequest(Request requestPost) {
        client.newCall(requestPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String zmdownurl = response.body().string();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                int a1 = zmdownurl.indexOf("\":\"");
                int a2 = zmdownurl.indexOf("\"}");
                String zmdownurl2 = zmdownurl.substring(a1 + 3, a2);
                int jc = zmdownurl2.indexOf("http");
                if (jc >= 0) {
                    intent.setData(Uri.parse(zmdownurl2));
                    startActivity(intent);
                } else {
                    intent.setData(Uri.parse(dz3));
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 加载数据线程
     **/
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //获取传递的信息
            Intent intent = getIntent();
            String dz1 = intent.getStringExtra("dz1"); //标题 1
            dz2 = intent.getStringExtra("dz2"); //标题 2
            dz3 = intent.getStringExtra("dz3"); //文章地址
            dz4 = intent.getStringExtra("dz4"); //图片地址
            dz1a = dz1.indexOf("+");
            dzname = dz1.substring(0, dz1a); //标题 1
            Connection conn = Jsoup.connect(dz3);
            // 修改http包中的header,伪装成浏览器进行抓取
            conn.header("User-Agent", userAgent);
            Document doc = null;
            try {
                doc = conn.get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //获取信息咯
            Element elementsUl = doc.getElementsByClass("numbers").first();
            Elements elements = elementsUl.getElementsByClass("td2");
            for (Element element : elements) {
                Elements elements1 = element.children();
                String xinxi = elements1.get(0).getElementsByClass("bnum").text();
                xinxi1 = xinxi1 + "," + xinxi;
            }
            xinxi = xinxi1.split(",");
            //字幕信息
            String jc;
            String jc2;
            if (dz1a > 0) {
                jc = doc.getElementsByClass("col-sm-10").first().children().get(1).child(5).text();
                jc2 = doc.getElementsByClass("col-sm-10").first().children().get(1).child(6).text();
            } else {
                jc = doc.getElementsByClass("col-sm-10").first().children().get(2).child(5).text();
                jc2 = doc.getElementsByClass("col-sm-10").first().children().get(2).child(6).text();
            }
            if (jc.indexOf("字幕文件") >= 0 || jc2.indexOf("字幕文件") >= 0) {
                Elements zmelements = doc.getElementsByClass("table-responsive").first().getElementsByTag("tr");
                for (Element zmelement : zmelements) {
                    Elements zmelements1 = zmelement.children();
                    String a1 = zmelements1.get(0).text();
                    String a2 = zmelements1.get(2).text();
                    Zmxx zr = new Zmxx(a1, a2);
                    zmxxList.add(zr);
                }
            } else {
                zmxx = "1";
            }
            //资源下载地址
            if (dz1a > 0) {
                Element zydownclass = doc.getElementsByClass("s_poster").first();
                if (zydownclass.text().indexOf("资源下载") >= 0) {
                    zydown = zydownclass.child(1).attr("href");
                } else {
                    zyxz = "1";
                }
                //影片介绍信息
                String jj = doc.getElementsByClass("s_detail").text();
                int jj1 = jj.indexOf("豆瓣：");
                int jj2 = jj.indexOf("类型：");
                int jj3 = jj.indexOf("导演：");
                int jj4 = jj.indexOf("演员：");
                int jj5 = jj.indexOf("介绍：");
                nf = jj.substring(0, jj1);
                lx = jj.substring(jj2, jj3);
                dy = jj.substring(jj3, jj4);
                yy = jj.substring(jj4, jj5);
                js = jj.substring(jj5);
                Elements elementsUl3 = doc.getElementsByClass("s_poster");
                if (elementsUl3.text() != "") {
                    zyxz = elementsUl3.first().children().get(1).attr("href");
                }
                //去豆瓣获取信息啦
                String db = doc.getElementsByClass("s_detail").get(0).getElementsByTag("a").attr("href");
                Connection conn2 = Jsoup.connect(db);
                // 修改http包中的header,伪装成浏览器进行抓取
                conn2.header("User-Agent", userAgent);
                Document doc2 = null;
                try {
                    doc2 = conn2.get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Element elementsUl2 = doc2.getElementsByClass("rating_self").first();
                rs = elementsUl2.child(2).getElementsByTag("a").first().getElementsByTag("span").text(); //人数
                fs = elementsUl2.child(0).text();         //      分数格式  a.b
                Log.d("fs",fs);
                int fs2 = fs.indexOf(".");                //      获取 "."
                String fsq = fs.substring(0, fs2);        //      截取前面数字a
                String fsh = fs.substring(fs2 + 1);
                int intfs = Integer.parseInt(fsq);        //      转换为int格式
                int intfsh = Integer.parseInt(fsh);
                if (fsh == "0") {
                    fsa = intfs;
                } else if (intfsh <= 9) {
                    intfs = intfs + 1;
                    fsa = intfs;
                } else if (intfs > 9) {
                    fsa = intfs;
                }
            }

            Message message = new Message();
            message.what = UPDATE_TEXT2;
            handler.sendMessage(message); // 将 Message 对象发送出去
        }
    };

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT2:
                    show();
                    // 在这里可以进行 UI 操作
                    break;
                case ZM_DOWN:
                    doPost(wb);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 获取填入信息
     **/
    private void show() {
        if (zmxx == "1") {
            android.support.v7.widget.CardView linearlayout = findViewById(R.id.zmwj);
            linearlayout.setVisibility(View.GONE);
        } else {
            RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.recycler_view2);
            ZmxxAdapter adapter = new ZmxxAdapter(this, zmxxList);
            recyclerView2.setAdapter(adapter);
        }
        ImageView imageView = (ImageView) findViewById(R.id.imageView4);
        TextView down = (TextView) findViewById(R.id.textView3);
        TextView file = (TextView) findViewById(R.id.textView4);
        TextView time = (TextView) findViewById(R.id.textView5);
        TextView textView7 = (TextView) findViewById(R.id.textView7);
        TextView textView8 = (TextView) findViewById(R.id.textView8);
        TextView textView9 = (TextView) findViewById(R.id.textView9);
        TextView textView10 = (TextView) findViewById(R.id.textView10);
        TextView textView12 = (TextView) findViewById(R.id.textView12);
        TextView textView13 = (TextView) findViewById(R.id.textView13);
        TextView textView14 = (TextView) findViewById(R.id.textView14);
        //设置背景图片
        Glide.with(ScrollingActivity.this)
                .load(dz4)
                //.placeholder(R.drawable.movie_default_large)//图片加载出来前，显示的图片
                .error(R.drawable.movie_default_large)//图片加载失败后，显示的图片
                .crossFade()//淡入淡出动画
                .fitCenter()//等比拉伸
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存源资源和转换后的资源
                .into(imageView);
        //设置信息
        down.setText(xinxi[2]);
        file.setText(xinxi[3]);
        time.setText(xinxi[4]);
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        if (dz1a > 0) {
            RatingBar RatingBar = (RatingBar) findViewById(R.id.ratingBar);
            mCollapsingToolbarLayout.setTitle(dzname); //设置标题 1
            textView7.setText(nf); //设置年份
            textView8.setText(lx); //设置类型
            textView9.setText(dy); //设置导演
            textView10.setText(yy); //设置演员
            textView14.setText(js); //设置介绍
            textView12.setText(fs); //设置分数 1
            textView13.setText(rs + "人"); //设置人数
            RatingBar.setProgress(fsa); //设置分数 2
        } else {
            mCollapsingToolbarLayout.setTitle(dz2); //设置标题 2
            //隐藏
            android.support.v7.widget.CardView linearlayout = findViewById(R.id.zpxx);
            android.support.v7.widget.CardView linearlayout2 = findViewById(R.id.dbpf);
            android.support.v7.widget.CardView linearlayout3 = findViewById(R.id.zpjs);
            linearlayout.setVisibility(View.GONE);
            linearlayout2.setVisibility(View.GONE);
            linearlayout3.setVisibility(View.GONE);
        }

        //显示
        LinearLayout linearlayout = (LinearLayout) findViewById(R.id.app_bar);
        LinearLayout linearlayout2 = (LinearLayout) findViewById(R.id.steam);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        linearlayout.setVisibility(View.VISIBLE);
        linearlayout2.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
        dialog.dismiss();
    }

    /**
     * 初始化 加载数据
     **/
    private void initFruits() {
        LinearLayout linearlayout = (LinearLayout) findViewById(R.id.app_bar);
        LinearLayout linearlayout2 = (LinearLayout) findViewById(R.id.steam);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        linearlayout.setVisibility(View.INVISIBLE);
        linearlayout2.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.INVISIBLE);
        //显示正在加载窗口
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在获取请稍后...");
        dialog.setCancelable(false);
        dialog.show();
        if (isConnectIsNomarl()) { //判断网络是否连接
            new Thread(runnable).start();  // 启动子线程获取数据
        } else {
            dialog.dismiss();
        }
    }

    /**
     * 判断网络是否连接
     **/
    private boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String intentName = info.getTypeName();
            return true;
        } else {
            return false;
        }
    }

}
