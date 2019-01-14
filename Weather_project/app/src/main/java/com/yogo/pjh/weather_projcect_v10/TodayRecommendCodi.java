package com.yogo.pjh.weather_projcect_v10;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;



public class TodayRecommendCodi extends Fragment {

    public static int TO_GRID = 0;
    public static int TO_GPS = 1;
    private Button btnShowLocation;
    private TextView wttxet;

    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;
    public static int temp;
    public static String weatherstate;
    public static boolean checkedGPS = false;
    Boolean loginChecked;

    private int time = -100;
    // GPSTracker class
    private GpsInfo gps;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragemnt_today_recommend, container, false);

        return rootView;
    }
    private String userName;
    private String userPassword;
    private String userID;
    private int userAge;
    private String userGender;
    private AlertDialog dialog;
    private String outeruri;
    private String upuri;
    private String downuri;
    private boolean check = false;
    LinearLayout linearLayout;
    TextView temperature;
    ImageView tempimg;
    Button okbt;
    ImageView cloImage1;
    ImageView cloImage2;
    ImageView cloImage3;
    GradientDrawable drawble;
    ImageView mainimg;

    String[] wtstate;
    TextView textView;
    Document doc = null;
    LinearLayout layout = null;
    String location;
    String nowweather;

    TextView temptext;

    ImageView nowwtimg;
    ImageView weatherscreen;
    //GradientDrawable drawble;



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle=getArguments();
        userName=bundle.getString("userName");
        userID=bundle.getString("userID");
        userGender=bundle.getString("userGender");
        userPassword=bundle.getString("userPassword");
        userAge=bundle.getInt("userAge",-1);
        cloImage1=(ImageView)getView().findViewById(R.id.cloImage1);
        cloImage2=(ImageView)getView().findViewById(R.id.cloImage2);
        cloImage3=(ImageView)getView().findViewById(R.id.cloImage3);
        nowwtimg=(ImageView)getView().findViewById(R.id.nowwtimgTRC);
        //temperature=(TextView)getView().findViewById(R.id.temperature);
        //tempimg=(ImageView)getView().findViewById(R.id.tempimg);
        //mainimg = (ImageView)getView().findViewById(R.id.twofragimg);
        linearLayout = (LinearLayout)getView().findViewById(R.id.recommendmain);
       //String tmp = Integer.toString(TodayWeatherFragment.temp);
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("HH");
        String formatDate = sdfNow.format(date);
        Log.d("시간 : ", formatDate);

        textView = (TextView) getView().findViewById(R.id.textWeather);
        btnShowLocation = (Button) getView().findViewById(R.id.button5);

        weatherscreen = (ImageView) getView().findViewById(R.id.weatherImg);
        //drawble = (GradientDrawable) getContext().getDrawable(R.drawable.rounding);
        //weatherscreen.setBackground(drawble);
        //weatherscreen.setClipToOutline(true);


        GetXMLTask task = new GetXMLTask();

        // 권한 요청을 해야 함
        if (!isPermission) {
            callPermission();
            if(isPermission) {
                gps = new GpsInfo(getActivity());
                // GPS 사용유무 가져오기
                if (gps.isGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    LatXLngY grid = convertGRID_GPS(TO_GRID, latitude, longitude);
                    Log.d("GPS 클릭else","1");
                    Toast.makeText(
                            getActivity().getApplicationContext(),
                            "당신의 위치 - " + grid.x + "    " + grid.y,
                            Toast.LENGTH_LONG).show();
                    task.execute("http://www.kma.go.kr/wid/queryDFS.jsp?gridx=" + grid.x + "&gridy=" + grid.y);
                } else {
                    // GPS 를 사용할수 없으rru므로
                    gps.showSettingsAlert();
                }
            }
        }
        else {
            Log.d("GPS 클릭else","2");
            gps = new GpsInfo(getActivity());
            // GPS 사용유무 가져오기
            if (gps.isGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                LatXLngY grid = convertGRID_GPS(TO_GRID, latitude, longitude);
                Toast.makeText(
                        getActivity().getApplicationContext(),
                        "당신의 위치 - " + grid.x + "    " + grid.y,
                        Toast.LENGTH_LONG).show();
                task.execute("http://www.kma.go.kr/wid/queryDFS.jsp?gridx=" + grid.x + "&gridy=" + grid.y);

            } else {
                // GPS 를 사용할수 없으rru므로
                gps.showSettingsAlert();
            }

        }
        //http://mainia.tistory.com/1153 참고 ////////////////////////////////////////////////////////////////버튼 누를시 gps 실행 및 날씨
        btnShowLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                GetXMLTask task = new GetXMLTask();
                // 권한 요청을 해야 함
                if (!isPermission) {
                    callPermission();
                    if(isPermission) {
                        Log.d("GPS 클릭10","");
                        gps = new GpsInfo(getActivity());
                        // GPS 사용유무 가져오기
                        if (gps.isGetLocation()) {

                            double latitude = gps.getLatitude();
                            double longitude = gps.getLongitude();

                            LatXLngY grid = convertGRID_GPS(TO_GRID, latitude, longitude);
                            Toast.makeText(
                                    getActivity().getApplicationContext(),
                                    "당신의 위치 - " + grid.x + "    " + grid.y,
                                    Toast.LENGTH_LONG).show();
                            task.execute("http://www.kma.go.kr/wid/queryDFS.jsp?gridx=" + grid.x + "&gridy=" + grid.y);
                        } else {
                            // GPS 를 사용할수 없으rru므로
                            gps.showSettingsAlert();
                        }
                    }
                    else
                        return;
                }
                else {
                    Log.d("GPS 클릭else","");
                    gps = new GpsInfo(getActivity());
                    // GPS 사용유무 가져오기
                    if (gps.isGetLocation()) {

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        LatXLngY grid = convertGRID_GPS(TO_GRID, latitude, longitude);
                        Toast.makeText(
                                getActivity().getApplicationContext(),
                                "당신의 위치 - " + grid.x + "    " + grid.y,
                                Toast.LENGTH_LONG).show();
                        task.execute("http://www.kma.go.kr/wid/queryDFS.jsp?gridx=" + grid.x + "&gridy=" + grid.y);




                    } else {
                        // GPS 를 사용할수 없으rru므로
                        gps.showSettingsAlert();
                    }
                }
                Log.d("false 체크", Boolean.toString(isAccessCoarseLocation));
            }
        });


        new GetData().execute();

    }

    // 전화번호 권한 요청
    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("GPS 클릭여긴가","-1");
            isAccessFineLocation = true;

        } else if (requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.d("GPS 클릭여긴가","0");
            isAccessCoarseLocation = true;
        }

        if (isAccessFineLocation && isAccessCoarseLocation) {
            Log.d("GPS 클릭여긴가","1");
            isPermission = true;
        }
        if(isAccessFineLocation){
            GetXMLTask task = new GetXMLTask();
            gps = new GpsInfo(getActivity());
            // GPS 사용유무 가져오기
            if (gps.isGetLocation()) {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                LatXLngY grid = convertGRID_GPS(TO_GRID, latitude, longitude);
                Log.d("GPS 클릭else","1");
                Toast.makeText(
                        getActivity().getApplicationContext(),
                        "당신의 위치 - " + grid.x + "    " + grid.y,
                        Toast.LENGTH_LONG).show();
                task.execute("http://www.kma.go.kr/wid/queryDFS.jsp?gridx=" + grid.x + "&gridy=" + grid.y);
            } else {
                // GPS 를 사용할수 없으rru므로
                gps.showSettingsAlert();
            }
        }
    }



    int CombCount=0;
    int allComb;
    public void spreadCodi() {

        String a = Integer.toString(recommand_clothes.size());
        Log.d("Dfdf", a);

        allComb=recommand_clothes.size();
        if(allComb==0 ){
            return;
        }


        if(recommand_clothes.get(CombCount).size()<4) {
            for (int i = 0; i < recommand_clothes.get(CombCount).size(); i++) {

                if (recommand_clothes.get(CombCount).get(i).getDef().equals("아우터")) {
                    Glide.with(this).load(recommand_clothes.get(CombCount).get(i).getUri_image())
                            .apply(new RequestOptions().override(300, 300)).into(cloImage1);
                } else if (recommand_clothes.get(CombCount).get(i).getDef().equals("상의")) {
                    Glide.with(this).load(recommand_clothes.get(CombCount).get(i).getUri_image())
                            .apply(new RequestOptions().override(300, 300)).into(cloImage2);
                } else if (recommand_clothes.get(CombCount).get(i).getDef().equals("하의")) {
                    Glide.with(this).load(recommand_clothes.get(CombCount).get(i).getUri_image())
                            .apply(new RequestOptions().override(300, 300)).into(cloImage3);
                }

            }
        }
    }

    final String TAG_JSON="webnautes";
    final String TAG_ID = "clotheinfo";
    final String TAG_NAME = "def";
    final String TAG_ADDRESS ="img";
    final String TAG_CLOTHES_ID="id";

    final static ArrayList<ClothesItem> cloBottom=new ArrayList<>();
    final static ArrayList<ClothesItem> cloUp=new ArrayList<>();

    static ArrayList<ArrayList<ClothesItem>> recommand_clothes = new ArrayList<ArrayList<ClothesItem>>();

    String clothesType;
    String clothesDetailType;
    String clothesImageAddress;
    int clothesid;

    menClothesAlgor mCA;
    womenClothesAlgor wCA;


    private class GetData extends AsyncTask<String, Void, String> {

        String errorString = null;

        String target;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                target = "pjtakeclothe.php";
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    clothesType = item.getString(TAG_ID);
                    clothesDetailType= item.getString(TAG_NAME);
                    clothesImageAddress = item.getString(TAG_ADDRESS);
                    clothesid=item.getInt(TAG_CLOTHES_ID);
                    Uri imageUri=Uri.parse("http://g22206.cafe24.com/"+clothesImageAddress);

                    if(clothesType.equals("아우터")){
                        cloUp.add(new ClothesItem(imageUri,clothesDetailType,clothesType,clothesid));
                    }
                    else if(clothesType.equals("상의")){
                        cloUp.add(new ClothesItem(imageUri,clothesDetailType,clothesType,clothesid));

                    }else if(clothesType.equals("하의")){
                        cloBottom.add(new ClothesItem(imageUri,clothesDetailType,clothesType,clothesid));
                    }

                }
                Log.d("크기", Integer.toString(cloUp.size()));
                Log.d("바지크기", Integer.toString(cloBottom.size()));

                if(userGender.equals("남자")) {
                    mCA = new menClothesAlgor(cloUp, cloBottom);
                    mCA.submit();

                    if (temp >= 28) {
                        recommand_clothes = mCA.hotsummer();
                    } else if (temp >= 23) {

                        recommand_clothes = mCA.summer();
                        Log.d("조합 숫", Integer.toString(recommand_clothes.size()));

                    } else if (temp >= 17) {
                        recommand_clothes = mCA.coolweather();
                    } else if (temp >= 10) {
                        recommand_clothes = mCA.spring();
                    } else if (temp >= 4) {
                        recommand_clothes = mCA.winter();
                    } else if (temp >= -30){
                        recommand_clothes = mCA.freeze();
                        Log.d("설마", "ㅁㅁㅁㅁㅁ");
                    } else{
                        if(check == true) {
                            Toast.makeText(
                                    getActivity().getApplicationContext(),
                                    "날씨를 설정후 해주세유",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }else if(userGender.equals("여자")){
                    wCA = new womenClothesAlgor(cloUp, cloBottom);
                    wCA.submit();
                    if (temp >= 28) {
                        recommand_clothes = wCA.hotsummer();
                    } else if (temp >= 23) {
                        recommand_clothes = wCA.summer();
                    } else if (temp >= 17) {
                        recommand_clothes = wCA.coolweather();
                    } else if (temp >= 10) {
                        recommand_clothes = wCA.spring();
                    } else if (temp >= 4) {
                        recommand_clothes = wCA.winter();
                    } else if (temp >= -30){
                        recommand_clothes = wCA.freeze();
                    } else{
                        if(check == true) {
                            Toast.makeText(
                                    getActivity().getApplicationContext(),
                                    "날씨를 설정후 해주세유",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                String a = Integer.toString(temp);
                Log.d("Dfdsssf",a);

                spreadCodi();

            }
            catch(Exception e){
                e.printStackTrace();
            }
            check = true;
        }
        @Override
        protected String doInBackground(String... params) {


            try {

                //URL 설정및 접속
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                //전송 모드 설정
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                //서버로 전송
                StringBuffer buffer = new StringBuffer();
                buffer.append("userID").append("=").append(userID);                 // php 변수에 값 대입

                OutputStreamWriter outStream = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                PrintWriter writer = new PrintWriter(outStream);
                writer.write(buffer.toString());
                writer.flush();




                int responseStatusCode = httpURLConnection.getResponseCode();

                InputStream inputStream;

                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();

            } catch (Exception e) {

                errorString = e.toString();

                return null;
            }

        }
    }

    void CheckTempChanged(){
        Log.d("크기2", Integer.toString(cloUp.size()));
        if(userGender.equals("남자")) {


            if (temp >= 28) {
                recommand_clothes = mCA.hotsummer();
            } else if (temp >= 23) {

                recommand_clothes = mCA.summer();
                Log.d("조합 숫", Integer.toString(recommand_clothes.size()));
            } else if (temp >= 17) {
                recommand_clothes = mCA.coolweather();
            } else if (temp >= 10) {
                recommand_clothes = mCA.spring();
            } else if (temp >= 4) {
                recommand_clothes = mCA.winter();
            } else if(temp >= -30 && temp!=0){
                recommand_clothes = mCA.freeze();
                Log.d("설마", Integer.toString(temp));
            } else{
                if(check == true) {
                    Toast.makeText(
                            getActivity().getApplicationContext(),
                            "날씨를 설정후 해주세유",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }else if(userGender.equals("여자")){


            if (temp >= 28) {
                recommand_clothes = wCA.hotsummer();
            } else if (temp >= 23) {
                recommand_clothes = wCA.summer();
            } else if (temp >= 17) {
                recommand_clothes = wCA.coolweather();
            } else if (temp >= 10) {
                recommand_clothes = wCA.spring();
            } else if (temp >= 4) {
                recommand_clothes = wCA.winter();
            } else if(temp >= -30){
                recommand_clothes = wCA.freeze();
            } else{
                if(check == true) {
                    Toast.makeText(
                            getActivity().getApplicationContext(),
                            "날씨를 설정후 해주세유",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
        allComb = recommand_clothes.size();
    }


    public void changeUiTemp(int temp_get, String weatherstate, int time) {

        drawble = (GradientDrawable) getContext().getDrawable(R.drawable.rounding);
        mainimg.setBackground(drawble);
        mainimg.setClipToOutline(true);
        temperature.setText(Integer.toString(temp_get) + "°C");


        if(weatherstate.equals("manycloude")) {
            tempimg.setImageResource(R.drawable.wcloude);
            if(time >= 18 || time<6)
                Glide.with(getContext()).load(R.drawable.cloudenight).into(mainimg);
            else
                Glide.with(getContext()).load(R.drawable.cloudesunny).into(mainimg);
        }
        else if(weatherstate.equals("nightcloude")) {
            tempimg.setImageResource(R.drawable.wnightcloude);
            Glide.with(getContext()).load(R.drawable.cloudenight).into(mainimg);
        }
        else if(weatherstate.equals("sunnycloude")) {
            tempimg.setImageResource(R.drawable.wsunnycloude);
            Glide.with(getContext()).load(R.drawable.cloudesunny).into(mainimg);
        }
        else if(weatherstate.equals("sunny")) {
            tempimg.setImageResource(R.drawable.wsunny);
            Glide.with(getContext()).load(R.drawable.sunnygi).into(mainimg);
        }
        else if(weatherstate.equals("night")) {
            tempimg.setImageResource(R.drawable.wnightsunny);
            Glide.with(getContext()).load(R.drawable.cloudenight).into(mainimg);
        }
        else if(weatherstate.equals("rainy")) {
            tempimg.setImageResource(R.drawable.wrainy);
            if(time >= 18 || time<6)
                Glide.with(getContext()).load(R.drawable.rainynight).into(mainimg);
            else
                Glide.with(getContext()).load(R.drawable.rainysunny).into(mainimg);
        }
        else if(weatherstate.equals("snow")) {
            tempimg.setImageResource(R.drawable.wsnow);
            if(time >= 18 || time<6)
                Glide.with(getContext()).load(R.drawable.snownight).into(mainimg);
            else
                Glide.with(getContext()).load(R.drawable.snowsunny).into(mainimg);
        }
    }


    private class GetXMLTask extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... urls) {
            URL url;
            try {
                url = new URL(urls[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder(); //XML문서 빌더 객체를 생성
                doc = db.parse(new InputSource(url.openStream())); //XML문서를 파싱한다.
                doc.getDocumentElement().normalize();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return doc;
        }

        protected void onPostExecute(Document doc) {
            super.onPostExecute(doc);//이 부분에서 날씨 이미지를 출력해줌
            String s = "";
            wtstate = new String[15];
            int nowTime = -100;

            //dara 태그가 있는 노드를 찾아서 리스트 형태로 만들어서 반환
            NodeList nodeList = doc.getElementsByTagName("data");
            //data 태그를 가지는 노드를 찾음, 계층적인 노드 구조를 반환


            for (int i = 0; i < 15; i++) {////////////////////////////////for문 시작
                //날씨 데이터를 추출
                s = "";
                Node node = nodeList.item(i); //data엘리먼트 노드
                Element fstElmnt = (Element) node;

                NodeList timeList = fstElmnt.getElementsByTagName("hour");          //시간 timeList
                s += timeList.item(0).getChildNodes().item(0).getNodeValue() + "시 ";
                time = Integer.parseInt(timeList.item(0).getChildNodes().item(0).getNodeValue());
                NodeList nameList = fstElmnt.getElementsByTagName("temp");          //이름
                Element nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();

                if(i==0)        //현재 날시
                {
                    nowweather = ((Node) nameList.item(0)).getNodeValue();
                    temp = (int)Float.parseFloat(nowweather);
                    nowweather = Integer.toString(temp)+"°C";


                }
                s += ((Node) nameList.item(0)).getNodeValue() + "°C\n\n";


                NodeList websiteList = fstElmnt.getElementsByTagName("wfKor");

                // s += websiteList.item(0).getChildNodes().item(0).getNodeValue() + "\n";


                if(websiteList.item(0).getChildNodes().item(0).getNodeValue().equals("구름 많음") || websiteList.item(0).getChildNodes().item(0).getNodeValue().equals("흐림"))
                {

                    if(i==0) {
                        nowwtimg.setImageResource(R.drawable.wcloude);
                        weatherstate = "manycloude";
                       if(time >= 18 || time<6) {
                            Glide.with(getContext()).load(R.drawable.cloudenight).into(weatherscreen);
                            nowTime=time;
                        }
                        else {
                            Glide.with(getContext()).load(R.drawable.cloudesunny).into(weatherscreen);
                            nowTime=time;
                        }

                    }
                }
                else if(websiteList.item(0).getChildNodes().item(0).getNodeValue().equals("구름 조금") && (time >= 18 || time<6))
                {

                    if(i==0 && time != 18) {
                        nowwtimg.setImageResource(R.drawable.wnightcloude);
                        weatherstate = "nightcloude";
                        Glide.with(getContext()).load(R.drawable.cloudenight).into(weatherscreen);
                        nowTime=time;
                    }
                    else if(i==0 && time==18) {
                        nowwtimg.setImageResource(R.drawable.wsunnycloude);
                        weatherstate = "sunnycloude";
                        Glide.with(getContext()).load(R.drawable.cloudesunny).into(weatherscreen);
                        nowTime=time;
                    }

                }
                else if(websiteList.item(0).getChildNodes().item(0).getNodeValue().equals("구름 조금") && (time >= 6 && time <18))
                {

                    if(i==0 && time != 6) {
                        nowwtimg.setImageResource(R.drawable.wsunnycloude);
                        weatherstate = "sunnycloude";
                        Glide.with(getContext()).load(R.drawable.cloudesunny).into(weatherscreen);
                        nowTime=time;
                    }
                    else if(i==0 && time==6) {
                        nowwtimg.setImageResource(R.drawable.wnightcloude);
                        Glide.with(getContext()).load(R.drawable.cloudenight).into(weatherscreen);
                        weatherstate = "nightcloude";
                        nowTime=time;
                    }

                }
                else if(websiteList.item(0).getChildNodes().item(0).getNodeValue().equals("맑음") && (time >= 6 && time<18))
                {

                    if(i==0 && time != 6) {
                        nowwtimg.setImageResource(R.drawable.wsunny);
                        Glide.with(getContext()).load(R.drawable.sunnygi).into(weatherscreen);
                        weatherstate = "sunny";
                        nowTime=time;
                    }
                    else if(i==0 && time==6) {
                        nowwtimg.setImageResource(R.drawable.night);
                        Glide.with(getContext()).load(R.drawable.nightgi).into(weatherscreen);
                        weatherstate = "night";
                        nowTime=time;
                    }

                }
                else if(websiteList.item(0).getChildNodes().item(0).getNodeValue().equals("맑음") && (time >= 18 || time<6))
                {

                    if(i==0 && time != 18) {
                        nowwtimg.setImageResource(R.drawable.wnightsunny);
                        Glide.with(getContext()).load(R.drawable.nightgi).into(weatherscreen);
                        weatherstate = "night";
                        nowTime=time;
                    }
                    else if(i==0 && time==18) {
                        nowwtimg.setImageResource(R.drawable.wsunny);
                        Glide.with(getContext()).load(R.drawable.sunnygi).into(weatherscreen);
                        weatherstate = "sunny";
                        nowTime=time;
                    }

                }
                else if(websiteList.item(0).getChildNodes().item(0).getNodeValue().equals("비"))
                {

                    if(i==0) {
                        nowwtimg.setImageResource(R.drawable.wrainy);
                        weatherstate = "rainy";
                        if(time >= 18 || time<6)
                            Glide.with(getContext()).load(R.drawable.rainynight).into(weatherscreen);
                        else
                            Glide.with(getContext()).load(R.drawable.rainysunny).into(weatherscreen);
                        nowTime=time;
                    }
                }
                else if(websiteList.item(0).getChildNodes().item(0).getNodeValue().equals("눈") || websiteList.item(0).getChildNodes().item(0).getNodeValue().equals("눈/비"))
                {

                    if(i==0) {
                        nowwtimg.setImageResource(R.drawable.wsnow);
                        weatherstate = "snow";
                        if(time >= 18 || time<6)
                            Glide.with(getContext()).load(R.drawable.snownight).into(weatherscreen);
                        else
                            Glide.with(getContext()).load(R.drawable.snowsunny).into(weatherscreen);
                    }
                }

            }////////////////////////////for문 종료

            //액티비티함수호출
            // ((MainActivity)getActivity()).setCodiUiTemp(temp, weatherstate, nowTime);



            textView.setText(nowweather);
            textView.setTextColor(Color.WHITE);

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdfNow = new SimpleDateFormat("HH");
            String formatDate = sdfNow.format(date);
            Log.d("시간 : ", formatDate);


        }
    }
    private LatXLngY convertGRID_GPS(int mode, double lat_X, double lng_Y )
    {
        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 43; // 기준점 X좌표(GRID)
        double YO = 136; // 기1준점 Y좌표(GRID)

        //
        // LCC DFS 좌표변환 ( code : "TO_GRID"(위경도->좌표, lat_X:위도,  lng_Y:경도), "TO_GPS"(좌표->위경도,  lat_X:x, lng_Y:y) )
        //


        double DEGRAD = Math.PI / 180.0;
        double RADDEG = 180.0 / Math.PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);
        LatXLngY rs = new LatXLngY();

        if (mode == TO_GRID) {
            rs.lat = lat_X;
            rs.lng = lng_Y;
            double ra = Math.tan(Math.PI * 0.25 + (lat_X) * DEGRAD * 0.5);
            ra = re * sf / Math.pow(ra, sn);
            double theta = lng_Y * DEGRAD - olon;
            if (theta > Math.PI) theta -= 2.0 * Math.PI;
            if (theta < -Math.PI) theta += 2.0 * Math.PI;
            theta *= sn;
            rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
            rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
        }
        else {
            rs.x = lat_X;
            rs.y = lng_Y;
            double xn = lat_X - XO;
            double yn = ro - lng_Y + YO;
            double ra = Math.sqrt(xn * xn + yn * yn);
            if (sn < 0.0) {
                ra = -ra;
            }
            double alat = Math.pow((re * sf / ra), (1.0 / sn));
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

            double theta = 0.0;
            if (Math.abs(xn) <= 0.0) {
                theta = 0.0;
            }
            else {
                if (Math.abs(yn) <= 0.0) {
                    theta = Math.PI * 0.5;
                    if (xn < 0.0) {
                        theta = -theta;
                    }
                }
                else theta = Math.atan2(xn, yn);
            }
            double alon = theta / sn + olon;
            rs.lat = alat * RADDEG;
            rs.lng = alon * RADDEG;
        }
        return rs;
    }



    class LatXLngY
    {
        public double lat;
        public double lng;

        public double x;
        public double y;

    }
}
