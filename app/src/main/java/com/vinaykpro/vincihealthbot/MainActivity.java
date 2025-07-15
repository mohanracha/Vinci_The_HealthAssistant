package com.vinaykpro.vincihealthbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TextView main;
    Toast t1;
    RecyclerView recyclerView;
    ChatAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<String> messageList = new ArrayList<>();
    String div = "{vin}";
    EditText editText;
    TextView typingStatus;
    String expecting = "";
    String presentText = "";
    List<String> currentOptions;
    RelativeLayout sendButton;
    Thread t;
    String wordsArray[] = {"Hello! This is Vinci, an advanced HealthBot designed to help you diagnose your health problems.","What's your name ?","Now tell me your age","What's your gender","Tell me about the health problems you're currently facing"};
    List<String> symptoms;
    List<String> choiceids;
    String currentitemid = "";
    TextToSpeech vinci;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expecting = "";
        expecting = "";
        currentOptions = new ArrayList<>();
        symptoms = new ArrayList<>();
        choiceids = new ArrayList<>();
        //main = findViewById(R.id.main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        editText = findViewById(R.id.editText);
        typingStatus = findViewById(R.id.status);
        sendButton = findViewById(R.id.relativeLayout);
        vinci = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                vinci.setLanguage(Locale.ENGLISH);
            }
        });
        //Toast.makeText(this, getTime(), Toast.LENGTH_SHORT).show();

        initRecyclerView();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                editText.setText("");
                //editText.setText("");
                //Toast.makeText(MainActivity.this, getTime(), Toast.LENGTH_SHORT).show();
                /*int i = 0,j = (int) (Math.ceil(Math.random()*15)+1);
                if(j > 5 && j<10) { i = 1; }
                if(j>10) {i = 2;}
                i=2;
                messageList.add(i+div+editText.getText()+div+getTime());*/
                if(!text.equals(""))
                 messageList.add(0+div+text+div+getTime());
                refresh();
                typingStatus.setText("typing...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        typingStatus.setText("online");
                        if(expecting == "name")
                        {
                            presentText = text;
                            //expecting = "age";
                            messageList.add(2+div+"Is \""+text+"\" your name ?"+div+"Yes|No");
                            vinci.speak("Is \""+text+"\" your name ?",TextToSpeech.QUEUE_FLUSH,null);
                            currentOptions.clear();
                            currentOptions.add("Yes");
                            currentOptions.add("No");
                            refresh();
                        } else if(expecting == "age")
                        {
                            int age = 18;
                            try { age = Integer.parseInt(text); } catch (Exception e) {
                            //Toast.makeText(MainActivity.this, e.toString()+"", Toast.LENGTH_SHORT).show();
                                String temps = "Please send your age in numerical form like '18' not 'eighteen'";
                            normalReply(temps);
                                vinci.speak("Please send your age in numerical form like number 18 not the word 'eighteen'",TextToSpeech.QUEUE_FLUSH,null);
                            return;
                            }
                            if(age  > 100)
                             age = 60;
                            putData("age",age);
                            expecting = "gender";
                            messageList.add(2+div+"Choose your gender ?"+div+"Male|Female");
                            vinci.speak("Choose your gender ?",TextToSpeech.QUEUE_FLUSH,null);
                            currentOptions.clear();
                            currentOptions.add("Male");
                            currentOptions.add("Female");
                            refresh();
                        } else if(expecting == "symptom") {
                            presentText = text;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    new WebThread(MainActivity.this).execute("parse",presentText);
                                }
                            }).start();
                        }
                    }
                },500);
            }
        });

        reply(wordsArray[0]);

        //t.start();
        //t.run();

        /*AndroidNetworking.initialize(getApplicationContext());

        AndroidNetworking.post("https://api.infermedica.com/v3/concepts?type=symptoms")
                .addHeaders("App-Id", "85483e12")
                .addHeaders("App-Key", "210ed69f6527610d571a883331fb8582")
                .addHeaders("Content-Type", "application/json")
                *//*.addHeaders("Interview-Id", "r8oK9tf83dEtwZm9bBJU")*//*
                .addBodyParameter("sex","male")
                .addBodyParameter("age","{\"value\": 30}")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        main.setText(response.toString());
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        //main.setText(error.toString());
                    }
                });*/

        /*AndroidNetworking.get("https://young-retreat-26153.herokuapp.com/chat")
                *//*.addHeaders("App-Id", "85483e12")
                .addHeaders("App-Key", "210ed69f6527610d571a883331fb8582")*//*
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response
                        //main.setText(response.toString());
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        //main.setText(error.toString());
                    }
                });*/
    }

    public void setText(String s)
    {
        /*//main.setText(s);
        JSONObject obj;
        try {
            obj = new JSONObject("{\"mentions\":[{\"id\":\"s_50\",\"name\":\"Chest pain\",\"common_name\":\"Chest pain\",\"orth\":\"chest pain\",\"choice_id\":\"present\",\"type\":\"symptom\"},{\"id\":\"s_1190\",\"name\":\"Back pain\",\"common_name\":\"Back pain\",\"orth\":\"back pain\",\"choice_id\":\"absent\",\"type\":\"symptom\"}],\"obvious\":true}");
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        if(expecting.equals("symptom")) {
            JSONObject obj = new JSONObject();
            try {
                obj = new JSONObject(s);
                JSONArray array = obj.getJSONArray("mentions");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject j = array.getJSONObject(i);
                    String id = j.getString("id");
                    String name = j.getString("name");
                    String commonname = j.getString("common_name");
                    String choice = j.getString("choice_id");
                    String fullstring = id+"|"+name+"|"+commonname+"|"+choice+"|"+"initial";
                    if(!symptoms.contains(fullstring))
                        symptoms.add(fullstring);
                }
                String list = "";
                String quote = "";
                if(symptoms.size() != 0) {
                    for (String sp : symptoms) {
                        String emoji = "✅";
                        String[] arr = sp.split("\\|");
                        if (arr[3].equals("absent")) {
                            emoji = "❌";
                        }
                        if (arr[1].equalsIgnoreCase(arr[2]))
                            list += "\n" + arr[1] + " " + emoji;
                        else
                            list += "\n" + arr[1] + "(" + arr[2] + ")" + emoji;
                    }
                    quote = "These are the symptoms identified my me:" + list+"\n\nYou can tell me more about your issues to add them into the above list and after you've finished, Click 'Proceed'";
                    messageList.add(2+div+quote+div+"Proceed");
                } else {
                    quote = "Sorry, I haven't found any symptoms based on your explanation. Please tell me about your health issues only not other stuff";
                    messageList.add(1+div+quote+div+getTime());
                }
                vinci.speak(quote.replaceAll("✅","").replaceAll("❌","not present"),TextToSpeech.QUEUE_FLUSH,null);

                refresh();
                //Toast.makeText(MainActivity.this, obj.getJSONArray("mentions").getJSONObject(), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, e.toString() + "", Toast.LENGTH_SHORT).show();
            }
        } else if(expecting.equals("interview"))
        {
            try {
                JSONObject obj = new JSONObject(s);
                JSONObject question = obj.getJSONObject("question");
                JSONArray items = question.getJSONArray("items");
                String symptomid = items.getJSONObject(0).getString("id");
                JSONArray options = items.getJSONObject(0).getJSONArray("choices");
                boolean shouldtop = obj.getBoolean("should_stop");
                currentOptions.clear();
                choiceids.clear();
                currentitemid = symptomid;
                String optionstext = "";
                if(!shouldtop) {
                    for (int i = 0; i < options.length(); i++) {
                        choiceids.add(options.getJSONObject(i).getString("id"));
                        currentOptions.add(options.getJSONObject(i).getString("label"));
                        optionstext += "|" + options.getJSONObject(i).getString("label");
                    }
                    messageList.add(2 + div + question.getString("text") + div + optionstext.substring(1));
                    vinci.speak(question.getString("text"),TextToSpeech.QUEUE_FLUSH,null);
                } else {
                    String name = obj.getJSONArray("conditions").getJSONObject(0).getString("name");
                    String commonname = obj.getJSONArray("conditions").getJSONObject(0).getString("common_name");
                    if(name.equalsIgnoreCase(commonname))
                    { messageList.add(1+div+"You might be suffering with '"+name+"'"+div+getTime()); vinci.speak("You might be suffering with '"+name+"'",TextToSpeech.QUEUE_FLUSH,null); }
                    else
                    { messageList.add(1+div+"You might be suffering with '"+name+"' commonly called as '"+commonname+"'"+div+getTime()); vinci.speak("You might be suffering with '"+name+"' commonly called as '"+commonname+"'",TextToSpeech.QUEUE_FLUSH,null); }
                }
                refresh();
                //messageList.add(2+div+question.getString("text"))+div+

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {

        }

        //.d("string",s);
        //Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public String getTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("hh:mm a");
        return simpleDateFormat.format(calendar.getTime()).toLowerCase();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ChatAdapter(messageList,this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
        recyclerView.post(() -> recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1));
    }

    private void reply(String s)
    {
        SharedPreferences sharedPreferences = this.getSharedPreferences("SP",MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                typingStatus.setText("typing...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        typingStatus.setText("online");
                        messageList.add("1" + div + wordsArray[0] + div + getTime());
                        vinci.speak(wordsArray[0],TextToSpeech.QUEUE_ADD,null);
                        refresh();
                        if (true/*sharedPreferences.getString("name", "").equals("")*/) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    typingStatus.setText("typing...");
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            typingStatus.setText("online");
                                            messageList.add("1" + div + wordsArray[1] + div + getTime());
                                            vinci.speak(wordsArray[1 ],TextToSpeech.QUEUE_ADD,null);
                                            expecting = "name";
                                            refresh();
                                        }
                                    }, (long) Math.ceil(Math.random() * 1000) + 500);
                                }
                            }, (long) Math.ceil(Math.random() * 500) + 500);
                        }
                    }
                },(long)Math.ceil(Math.random()*1000)+1000);
            }
        },(long)Math.ceil(Math.random()*500)+500);
    }

    public void choosenOption(int i)
    {
        if(expecting == "name")
        {
            typingStatus.setText("typing");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(expecting == "name")
                    {
                        typingStatus.setText("online");
                        messageList.add(0+div+currentOptions.get(i)+div+getTime());
                        if(i == 0) {
                            messageList.add(1 + div + wordsArray[2]+", " + presentText + " ?" + div + getTime());
                            vinci.speak(wordsArray[2]+", " + presentText + " ?",TextToSpeech.QUEUE_FLUSH,null);
                            putData("name",presentText);
                            expecting = "age";
                        } else {
                            typingStatus.setText("typing");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    typingStatus.setText("online");
                                    messageList.add("1" + div + wordsArray[1] + div + getTime());
                                    vinci.speak(wordsArray[1],TextToSpeech.QUEUE_FLUSH,null);
                                    expecting = "name";
                                    refresh();
                                    }
                            },500);
                        }
                        refresh();
                    }
                }
            },500);
        } else if(expecting == "gender")
        {
            SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("SP",MODE_PRIVATE);
            typingStatus.setText("online");
            messageList.add(0+div+currentOptions.get(i)+div+getTime());
            refresh();
            String gender = currentOptions.get(i);
            if(gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female"))
                putData("gender",gender);
            else
                putData("gender","male");
            String temps = "Thank you "+sharedPreferences.getString("name","")+", Now describe your health problems/symptoms you noticed";
            normalReply(temps);
            vinci.speak(temps,TextToSpeech.QUEUE_FLUSH,null);
            expecting = "symptom";
        } else if(expecting == "symptom")
        {
            expecting = "interview";
            messageList.add(0+div+"Proceed"+div+getTime());
            refresh();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String bytes = "";
                    for (String s : symptoms) {
                        String[] arr = s.split("\\|");
                        bytes += arr[0]+"\\|"+arr[3]+"\\|"+arr[4]+div;
                    }
                    bytes = bytes.substring(0,bytes.lastIndexOf(div));
                    new WebThread(MainActivity.this).execute("diagnosis",bytes);
                }
            }).start();
        } else if(expecting == "interview") {
            messageList.add(0+div+currentOptions.get(i)+div+getTime());
            refresh();
            //id+"|"+name+"|"+commonname+"|"+choice+"|"+"initial";
            String s = currentitemid+"|"+currentOptions.get(i)+"|noneed|"+choiceids.get(i)+"|noninitial";
            if(!symptoms.contains(s))
             symptoms.add(s);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String bytes = "";
                    for (String s : symptoms) {
                        String[] arr = s.split("\\|");
                        bytes += arr[0]+"\\|"+arr[3]+"\\|"+arr[4]+div;
                    }
                    bytes = bytes.substring(0,bytes.lastIndexOf(div));
                    new WebThread(MainActivity.this).execute("diagnosis",bytes);
                }
            }).start();
        }
        //Toast.makeText(this,i+ "", Toast.LENGTH_SHORT).show();
    }

    public void putData(String key,String value)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("SP",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public void putData(String key,Integer value)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("SP",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.apply();
    }

    void normalReply(String s)
    {
        typingStatus.setText("typing...");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                typingStatus.setText("online");
                messageList.add("1" + div +s + div + getTime());
                refresh();
            }
        },700);
    }
}