package org.biantan.subhd;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mintmedical.refresh.PullRefreshLayout;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private List<Fruit> fruitList = new ArrayList<>();
    FruitAdapter adapter = new FruitAdapter(this, fruitList);
    private ProgressDialog dialog;
    private String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36";
    private String url = "http://subhd.com/search0/";
    private String page1 = "1";
    private String page2 = "1";
    boolean firstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        //显示正在加载窗口
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在获取请稍后...");
        dialog.setCancelable(false);
        dialog.show();
        initFruits();//初始化 抓取数据
        xjsx(); //上拉下拉

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void xjsx(){
        final PullRefreshLayout layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() { //下拉
            @Override
            public void onRefresh() {
                layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        layout.setOnLoadListener(new PullRefreshLayout.OnLoadListener() {  //上拉
            @Override
            public void onLoad() {
                initFruits();//抓取数据
            }
        });
    }

    /**
     * 加载数据线程
     **/
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = getIntent();
            String SearchName = intent.getStringExtra("extra_data");
            if (SearchName != null && firstLoad) {
                Connection conn = Jsoup.connect(url + SearchName + "/all/page/" + page2);
                // 修改http包中的header,伪装成浏览器进行抓取
                conn.header("User-Agent", userAgent);
                Document doc = null;
                try {
                    doc = conn.get();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //获取分页
                Element elementsPages = doc.getElementsByClass("list").first();
                Elements elementsPageB = elementsPages.getElementsByTag("small").first().getElementsByTag("b");
                int i = 1;
                for (Element pagexx : elementsPageB) {
                    if (i == 2) {
                        page1 = pagexx.text();
                    } else if (i == 3) {
                        if (Integer.parseInt(page2) < Integer.parseInt(page1)){
                            int a = Integer.parseInt(pagexx.text()) + 1;
                            page2 = a + "";
                        } else {
                            firstLoad = false;
                        }
                    }
                    i++;
                }

                //获取信息咯
                Element elementsUl = doc.getElementsByClass("col-md-9").first();
                Elements elements = elementsUl.getElementsByClass("box");
                for (Element element : elements) {
                    Elements elements1 = element.children();
                    String img1 = elements1.get(0).getElementsByTag("img").first().attr("src");//电影封面地址
                    String title = elements1.get(1).child(0).getElementsByTag("a").text();//电影名称
                    String targetUrl1 = elements1.get(1).getElementsByClass("d_title").first().getElementsByTag("a").attr("href");//文章地址
                    String name = elements1.get(1).getElementsByClass("d_title").get(0).text();//字幕名称
                    String info = elements1.get(1).getElementsByClass("label-info").text();//info
                    String success = elements1.get(1).getElementsByClass("label-success").text();//success
                    String primary = elements1.get(1).getElementsByClass("label-primary").text();//primary
                    String img2 = img1;
                    if (img1.indexOf("http") < 0) { //判断图片地址是否完整
                        img2 = "http://subhd.com" + img1;//电影封面地址
                    }
                    String targetUrl2 = "http://subhd.com" + targetUrl1;
                    Fruit zr = new Fruit(title, name, info, primary, success, img2, targetUrl2);
                    fruitList.add(zr);
                }

                Message message = new Message();
                handler.sendMessage(message); // 将 Message 对象发送出去
            } else if(SearchName == null) {
                Log.d("test","搜索词为空");
            } else if(!firstLoad) {
                Log.d("test","加载完了哦！");
                final PullRefreshLayout layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
                layout.setLoading(false);
            }
            dialog.dismiss();
        }
    };

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            show(); //将数据填充到RecyclerView中
        }
    };

    /**
     * 将数据填充到RecyclerView中
     **/
    private void show() {
        final PullRefreshLayout layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        layout.setLoading(false);
        //adapter.notifyDataSetChanged();
        adapter.notifyItemRemoved(adapter.getItemCount());
    }

    /**
     * 初始化 加载数据
     **/
    private void initFruits() {
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
            Log.d("Intent", "当前网络名称：" + intentName);
            return true;
        } else {
            Log.d("Intent", "没有可用网络");
            return false;
        }
    }


}