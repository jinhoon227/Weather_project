package com.yogo.pjh.weather_projcect_v10;

        import android.Manifest;
        import android.content.pm.PackageManager;
        import android.graphics.Color;
        import android.os.Build;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import android.graphics.drawable.GradientDrawable;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.LinearLayout;

        import android.os.AsyncTask;
        import org.w3c.dom.Document;
        import org.w3c.dom.Element;
        import org.w3c.dom.Node;
        import org.w3c.dom.NodeList;
        import org.xml.sax.InputSource;
        import org.xml.sax.SAXException;

        import java.io.IOException;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.sql.Date;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;

        import javax.xml.parsers.DocumentBuilder;
        import javax.xml.parsers.DocumentBuilderFactory;
        import javax.xml.parsers.ParserConfigurationException;

        import com.bumptech.glide.Glide;


public class TodayWeatherFragment extends Fragment {

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


    private int time = -100;
    // GPSTracker class
    private GpsInfo gps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_today_weather, container, false);

        return rootView;
    }

    String[] wtstate;
    TextView textView;
    Document doc = null;
    LinearLayout layout = null;
    String location;
    String nowweather;
    ArrayList<String> timeweather;
    TextView temptext;
    TextView[] wttext;
    ImageView[] wtimg;
    ImageView nowwtimg;
    ImageView weatherscreen;

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);
        textView = (TextView) getView().findViewById(R.id.textview2);


        timeweather = new ArrayList<>();
        wttext = new TextView[15];
        wtimg = new ImageView[15];
        nowwtimg = (ImageView)getView().findViewById(R.id.nowwtimg1);
        wttext[0] = (TextView) getView().findViewById(R.id.wttext1);
        wttext[1] = (TextView) getView().findViewById(R.id.wttext2);
        wttext[2] = (TextView) getView().findViewById(R.id.wttext3);
        wttext[3] = (TextView) getView().findViewById(R.id.wttext4);
        wttext[4] = (TextView) getView().findViewById(R.id.wttext5);
        wttext[5] = (TextView) getView().findViewById(R.id.wttext6);
        wttext[6] = (TextView) getView().findViewById(R.id.wttext7);
        wttext[7] = (TextView) getView().findViewById(R.id.wttext8);
        wttext[8] = (TextView) getView().findViewById(R.id.wttext9);
        wttext[9] = (TextView) getView().findViewById(R.id.wttext10);
        wttext[10] = (TextView) getView().findViewById(R.id.wttext11);
        wttext[11] = (TextView) getView().findViewById(R.id.wttext12);
        wttext[12] = (TextView) getView().findViewById(R.id.wttext13);
        wttext[13] = (TextView) getView().findViewById(R.id.wttext14);
        wttext[14] = (TextView) getView().findViewById(R.id.wttext15);
        wtimg[0] = (ImageView) getView().findViewById(R.id.wtimg1);
        wtimg[1] = (ImageView) getView().findViewById(R.id.wtimg2);
        wtimg[2] = (ImageView) getView().findViewById(R.id.wtimg3);
        wtimg[3] = (ImageView) getView().findViewById(R.id.wtimg4);
        wtimg[4] = (ImageView) getView().findViewById(R.id.wtimg5);
        wtimg[5] = (ImageView) getView().findViewById(R.id.wtimg6);
        wtimg[6] = (ImageView) getView().findViewById(R.id.wtimg7);
        wtimg[7] = (ImageView) getView().findViewById(R.id.wtimg8);
        wtimg[8] = (ImageView) getView().findViewById(R.id.wtimg9);
        wtimg[9] = (ImageView) getView().findViewById(R.id.wtimg10);
        wtimg[10] = (ImageView) getView().findViewById(R.id.wtimg11);
        wtimg[11] = (ImageView) getView().findViewById(R.id.wtimg12);
        wtimg[12] = (ImageView) getView().findViewById(R.id.wtimg13);
        wtimg[13] = (ImageView) getView().findViewById(R.id.wtimg14);
        wtimg[14] = (ImageView) getView().findViewById(R.id.wtimg15);
        weatherscreen = (ImageView) getView().findViewById(R.id.WeatherScreen);

    }


}
