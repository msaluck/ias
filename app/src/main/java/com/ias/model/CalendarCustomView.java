package com.ias.model;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ias.handler.Constant;
import com.ias.handler.RequestHandler;
import com.ias.iaslogin.R;
//import com.inducesmile.androidcustomcalendar.database.DatabaseQuery;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Timestamp;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
public class CalendarCustomView extends LinearLayout {
    private static final String TAG = CalendarCustomView.class.getSimpleName();
    public static final String EVENT_URL = Constant.URL_STRING + "/EventServlet";
    private ImageView previousButton, nextButton;
    private TextView currentDate;
    private GridView calendarGridView;
    private Button addEventButton;
    private static final int MAX_CALENDAR_COLUMN = 42;
    private int month, year;
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private Context context;
    private GridAdapter mAdapter;

    //    private DatabaseQuery mQuery;
    public CalendarCustomView(Context context) {
        super(context);
    }

    public CalendarCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeUILayout();
        setUpCalendarAdapter();
        setPreviousButtonClickEvent();
        setNextButtonClickEvent();
        setGridCellClickEvents();
        Log.d(TAG, "I need to call this method");
    }

    public CalendarCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initializeUILayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        previousButton = (ImageView) view.findViewById(R.id.previous_month);
        nextButton = (ImageView) view.findViewById(R.id.next_month);
        currentDate = (TextView) view.findViewById(R.id.display_current_date);
//        addEventButton = (Button) view.findViewById(R.id.add_calendar_event);
        calendarGridView = (GridView) view.findViewById(R.id.calendar_grid);
    }

    private void setPreviousButtonClickEvent() {
        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, -1);
                setUpCalendarAdapter();
            }
        });
    }

    private void setNextButtonClickEvent() {
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, 1);
                setUpCalendarAdapter();
            }
        });
    }

    private void setGridCellClickEvents() {
        calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, mAdapter.getEventInfo(position).toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
//
//    private void setUpCalendarAdapter() {
//
//    }

    public void setUpCalendarAdapter() {
        final String user = "trci";
        final String password = "cihuy123!";
        final List<EventObjects> listOfEvent = new ArrayList<EventObjects>();

        class setUpCalendarAdapter extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getContext(), "Please wait...", "uploading", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                List<Date> dayValueInCells = new ArrayList<Date>();
//        mQuery = new DatabaseQuery(context);

//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DATE, 5);
//        Date date = calendar.getTime();
//
//        EventObjects eventObjects = new EventObjects("Testing lah", date);
//        List<EventObjects> mEvents = new ArrayList<EventObjects>();
//        mEvents.add(eventObjects);
//        List<EventObjects> mEvents = mQuery.getAllFutureEvents();
                List<EventObjects> mEvents = listOfEvent;
                Calendar mCal = (Calendar) cal.clone();
                mCal.set(Calendar.DAY_OF_MONTH, 1);
                int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
                mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
                while (dayValueInCells.size() < MAX_CALENDAR_COLUMN) {
                    dayValueInCells.add(mCal.getTime());
                    mCal.add(Calendar.DAY_OF_MONTH, 1);
                }
                Log.d(TAG, "Number of date " + dayValueInCells.size());
                String sDate = formatter.format(cal.getTime());
                currentDate.setText(sDate);
                mAdapter = new GridAdapter(context, dayValueInCells, cal, mEvents);
                calendarGridView.setAdapter(mAdapter);

            }


            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String, String> param = new HashMap<String, String>();

                param.put(Constant.USER, user);
                param.put(Constant.PWD, password);
                String result = rh.sendPostRequest(EVENT_URL, param);
                try {
                    JSONObject jsonObj = new JSONObject(result);

                    // Getting JSON Array node
                    JSONArray listEvent = jsonObj.getJSONArray("event");

                    // looping through All Contacts
                    for (int i = 0; i < listEvent.length(); i++) {
                        JSONObject c = listEvent.getJSONObject(i);

                        String dateInString = c.getString("event_date") == null ? null : c.getString("event_date");
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = null;
                        try {
                            date = formatter.parse(dateInString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        // adding contact to contact list
                        EventObjects eo = new EventObjects(c.getString("event_info") == null ? null : c.getString("event_info"), date);
                        listOfEvent.add(eo);
                    }
                } catch (final JSONException e) {
                    Log.e("TAG", "Json parsing error: " + e.getMessage());


                }


                return result;
            }
        }
        setUpCalendarAdapter u = new setUpCalendarAdapter();
        u.execute();

    }

}