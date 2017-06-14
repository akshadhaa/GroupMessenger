package edu.buffalo.cse.cse486586.groupmessenger2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

/**
 * GroupMessengerActivity is the main Activity for the assignment.
 *
 * @author stevko
 */
public class GroupMessengerActivity extends Activity {
    
    static final String TAG = GroupMessengerActivity.class.getSimpleName();
    ArrayList<String> al= new ArrayList<String>();
    static final int SERVER_PORT = 10000;
    double proposed_priority = 0.0;
    int sequence = 0;
    int counter = 0;
    int messagecount = 0;


    String myPort;
    HashMap<Double, String> hm = new HashMap<Double, String>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_messenger);
        TelephonyManager tel = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
        myPort = String.valueOf((Integer.parseInt(portStr) * 2));
        al.add("11108");
        al.add("11112");
        al.add("11116");
        al.add("11120");
        al.add("11124");
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            new ServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Can't create a ServerSocket");
            return;
        }

        /*
         * TODO: Use the TextView to display your messages. Though there is no grading component
         * on how you display the messages, if you implement it, it'll make your debugging easier.
         */
        final EditText editText = (EditText) findViewById(R.id.editText1);
        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setMovementMethod(new ScrollingMovementMethod());

        /*
         * Registers OnPTestClickListener for "button1" in the layout, which is the "PTest" button.
         * OnPTestClickListener demonstrates how to access a ContentProvider.
         */
        findViewById(R.id.button1).setOnClickListener(
                new OnPTestClickListener(tv, getContentResolver()));

        /*
         * TODO: You need to register and implement an OnClickListener for the "Send" button.
         * In your implementation you need to get the message from the input box (EditText)
         * and send it to other AVDs.
         */

        Button btn = (Button) findViewById(R.id.button4);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = editText.getText().toString();
                editText.setText("");
                Log.e(TAG, "send button (Button4) issue");
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, txt, myPort);

            }
        });

        //TODO onclick listener -> on the send button , when the button is clicked , get the text and reset edit text
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_group_messenger, menu);
        return true;
    }


    private Uri buildUri(String scheme, String authority) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(scheme);
        return uriBuilder.build();
    }

    private final Uri mUri = buildUri("content", "edu.buffalo.cse.cse486586.groupmessenger2.provider");

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("GroupMessenger Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class ServerTask extends AsyncTask<ServerSocket, String, Void> {
        Comparator<Message123> comparator = new DoubleComparator();
        PriorityQueue<Message123> pq = new PriorityQueue<Message123>(1000, comparator);
        boolean errorflag1=false;
        int failednode1;
        String errormessage = "";

        Double previous_priority;
        Double agreed_priority = 0.0;
        double HAP = 0.0;


        @Override
        protected Void doInBackground(ServerSocket... sockets) {
            ServerSocket serverSocket = sockets[0];
            boolean error = false;
            Message123 msgobjtoiterate;
            Message123 errorMessageObject;

            String[] initialmessage = {""};
            String[] hppwithmessage = {""};

            String failedPort = "";
            int count = 0;

            while (count < 99999) {
                count++;
                try {
                    serverSocket.setSoTimeout(5000);

                    Socket sock = serverSocket.accept();
                    BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    String message = br.readLine();

                     if (message.contains("#MESSAGEWITHCOUNTER#")) {
                        initialmessage = message.split("#MESSAGEWITHCOUNTER#");
                        Log.e(TAG, "Server - message : " + initialmessage[0]);
                        Message123 msgobj = new Message123();

                        if (messagecount == 0 && message.contains("#MESSAGEWITHCOUNTER#")) {
                            if (myPort.equals("11108")) {
                                msgobj.priority = 1.1;
                                proposed_priority = msgobj.priority;
                                msgobj.message = initialmessage[0];
                                msgobj.deliverable = false;
                                msgobj.clientPortNumber = initialmessage[2];
                                msgobj.serverPortNumber = myPort;
                                pq.offer(msgobj);
                                
                            } else if (myPort.equals("11112")) {
                                msgobj.priority = 1.2;
                                proposed_priority = msgobj.priority;
                                msgobj.message = initialmessage[0];
                                msgobj.deliverable = false;
                                msgobj.clientPortNumber = initialmessage[2];
                                msgobj.serverPortNumber = myPort;
                                pq.offer(msgobj);
                            } else if (myPort.equals("11116")) {
                                msgobj.priority = 1.3;
                                proposed_priority = msgobj.priority;
                                msgobj.message = initialmessage[0];
                                msgobj.deliverable = false;
                                msgobj.clientPortNumber = initialmessage[2];
                                msgobj.serverPortNumber = myPort;
                                pq.offer(msgobj);
                                
                            } else if (myPort.equals("11120")) {
                                msgobj.priority = 1.4;
                                proposed_priority = msgobj.priority;
                                msgobj.message = initialmessage[0];
                                msgobj.deliverable = false;
                                msgobj.clientPortNumber = initialmessage[2];
                                msgobj.serverPortNumber = myPort;
                                pq.offer(msgobj);
                                
                            } else if (myPort.equals("11124")) {
                                msgobj.priority = 1.5;
                                proposed_priority = msgobj.priority;
                                msgobj.message = initialmessage[0];
                                msgobj.deliverable = false;
                                msgobj.clientPortNumber = initialmessage[2];
                                msgobj.serverPortNumber = myPort;
                                pq.offer(msgobj);
                                
                            }

                            messagecount++;
                            String priToSend = String.valueOf(proposed_priority) + "\n";
                            PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF8"));
                            pw1.write(priToSend);
                            pw1.flush();
                            sock.close();
                            continue;

                        } else if (messagecount > 0 && message.contains("MESSAGEWITHCOUNTER")) {

                            if (myPort.equals("11108")) {
                                msgobj.priority = Math.ceil(Math.max(agreed_priority, proposed_priority)) + 1.1;
                                proposed_priority = msgobj.priority;
                                msgobj.message = initialmessage[0];
                                msgobj.clientPortNumber = initialmessage[2];
                                msgobj.serverPortNumber = myPort;
                                msgobj.deliverable = false;
                                pq.offer(msgobj);
                            } else if (myPort.equals("11112")) {
                                msgobj.priority = Math.floor(Math.max(agreed_priority, proposed_priority)) + 1.2;
                                proposed_priority = msgobj.priority;
                                msgobj.message = initialmessage[0];
                                msgobj.deliverable = false;
                                msgobj.clientPortNumber = initialmessage[2];
                                msgobj.serverPortNumber = myPort;
                                pq.offer(msgobj);
                            } else if (myPort.equals("11116")) {
                                msgobj.priority = Math.floor(Math.max(agreed_priority, proposed_priority)) + 1.3;
                                proposed_priority = msgobj.priority;
                                msgobj.message = initialmessage[0];
                                msgobj.deliverable = false;
                                msgobj.clientPortNumber = initialmessage[2];
                                msgobj.serverPortNumber = myPort;
                                pq.offer(msgobj);
                            } else if (myPort.equals("11120")) {
                                msgobj.priority = Math.floor(Math.max(agreed_priority, proposed_priority)) + 1.4;
                                proposed_priority = msgobj.priority;
                                msgobj.message = initialmessage[0];
                                msgobj.deliverable = false;
                                msgobj.clientPortNumber = initialmessage[2];
                                msgobj.serverPortNumber = myPort;
                                pq.offer(msgobj);
                            } else if (myPort.equals("11124")) {
                                msgobj.priority = Math.floor(Math.max(agreed_priority, proposed_priority)) + 1.5;
                                proposed_priority = msgobj.priority;
                                msgobj.message = initialmessage[0];
                                msgobj.deliverable = false;
                                msgobj.clientPortNumber = initialmessage[2];
                                msgobj.serverPortNumber = myPort;
                                pq.offer(msgobj);
                            }

                            messagecount++;

                            String priToSend = String.valueOf(proposed_priority) + "\n";
                            PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF8"));
                            pw1.write(priToSend);
                            pw1.flush();
                            sock.close();
                            continue;

                        }
                    }
                    else if (message.contains("#HPPWITHMESSAGE#")) {
                        hppwithmessage = message.split("#HPPWITHMESSAGE#");
                        agreed_priority = Double.parseDouble(hppwithmessage[0]);

                        Iterator it1 = pq.iterator();
                        while (it1.hasNext()) {
                            msgobjtoiterate = (Message123) it1.next();
                            if (msgobjtoiterate.message.equals(hppwithmessage[1])) {
                                pq.remove(msgobjtoiterate);
                                msgobjtoiterate.priority = Double.parseDouble(hppwithmessage[0]);
                                msgobjtoiterate.deliverable = true;
                                pq.offer(msgobjtoiterate);
                                break;
                            }

                        }

                        sock.close();
                    }
                     else if (message.contains("#ERRORMESSAGE#")) {
                         errormessage = message.split("#ERRORMESSAGE#")[0];
                         error = true;
                     }

                    if(error && pq.peek().clientPortNumber.equals(errormessage)){
                        String messageToInsertString;
                        messageToInsertString = pq.peek().message;
                        pq.poll();
                        //insert to content provider
                        ContentValues keyValueToInsert = new ContentValues();
                        publishProgress(message);
                        //inserting <”key-to-insert”, “value-to-insert”>
                        keyValueToInsert.put("key", sequence++);

                        keyValueToInsert.put("value", messageToInsertString);
                        Uri newUri = getContentResolver().insert(
                                mUri,
                                keyValueToInsert
                        );
                    }

                } catch (IOException e) {
                    while (pq.peek() != null) {
                        String messageToInsertString;
                        messageToInsertString = pq.peek().message;
                        pq.poll();
                        //insert to content provider
                        ContentValues keyValueToInsert = new ContentValues();
                        //inserting <”key-to-insert”, “value-to-insert”>
                        keyValueToInsert.put("key", sequence);
                        sequence++;

                        keyValueToInsert.put("value", messageToInsertString);
                        Uri newUri = getContentResolver().insert(
                                mUri,
                                keyValueToInsert
                        );

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onProgressUpdate(String... strings) {
            /*
             * The following code displays what is received in doInBackground().
             */
            String strReceived = strings[0].trim();
            TextView remoteTextView = (TextView) findViewById(R.id.remote_text_display);
            remoteTextView.append(strReceived + "\t\n");
            TextView localTextView = (TextView) findViewById(R.id.local_text_display);
            localTextView.append("\n");

            /*
             * The following code creates a file in the AVD's internal storage and stores a file.
             *
             * For more information on file I/O on Android, please take a look at
             * http://developer.android.com/training/basics/data-storage/files.html
             */
        }
    }


    class ClientTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... msgs) {
           Double prop_priority = 0.0;
            Double agreedpriority = 0.0;
            String agreedprioritystring = "";
            String failednodestring="";
            int failednode = 0;
            boolean errorflag=false;

            String msgToSend = msgs[0];
            String clientID = msgs[1];

            List<Double> ls = new ArrayList<Double>();
            for (int i = 0; i < al.size(); i++) {
                try {
                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(al.get(i)));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
                    pw.write(msgToSend + "#MESSAGEWITHCOUNTER#" + Integer.toString(counter) + "#MESSAGEWITHCOUNTER#" + clientID + "\n");
                    pw.flush();
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String propPriority = br1.readLine();

                    if(propPriority==null){
                        failednodestring=al.get(i);
                        failednode = Integer.parseInt(al.get(i));
                        errorflag=true;
                        continue;
                    }
                    prop_priority = Double.parseDouble(propPriority);
                    ls.add(prop_priority);

                    socket.close();
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();

                }
            }

            if(errorflag){
                handlingException(failednodestring);
                errorflag=false;
            }

            agreedpriority = Collections.max(ls);


            for (int i = 0; i < al.size(); i++) {
                try {
                    Socket socket2 = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(al.get(i)));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket2.getOutputStream(), "UTF8"));
                    agreedprioritystring = Double.toString(agreedpriority);
                    pw.write(agreedprioritystring + "#HPPWITHMESSAGE#" + msgToSend + "\n");
                    pw.flush();
                    socket2.close();
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    failednodestring=al.get(i);
                    failednode = Integer.parseInt(al.get(i));
                    handlingException(failednodestring);
                    e1.printStackTrace();
                }
            }

            return null;
        }
    }

    class Message123 {

        String message;
        Double priority;
        Boolean deliverable;
        String clientPortNumber;
        String serverPortNumber;


        void Message123(String message, Double priority) {
            this.message = message;
            this.priority = priority;
            this.deliverable = false;
        }
    }


    class DoubleComparator implements Comparator<Message123> {

        @Override
        public int compare(Message123 arg0, Message123 arg1) {
            if (arg1.priority > arg0.priority) {
                return -1;
            }
            if (arg1.priority < arg0.priority) {
                return 1;
            } else
                return 0;
        }
    }
    // METHOD to HANDLE EXCEPTIONS
    protected Void handlingException(String i) {
        if (i.equals("11108")) {
            al.remove("11108");
        }
        else if ( i.equals("11112")) {
            al.remove("11112");
        }
        else if (i.equals("11116")) {
            al.remove("11116");
        }
        else if (i.equals("11120")) {
            al.remove("11120");
        }
        else if (i.equals("11124")) {
            al.remove("11124");
        }

        for (int j = 0; j < al.size(); j++) {
            try {
                Socket socket3 = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                        Integer.parseInt(al.get(j)));
                PrintWriter pw2 = new PrintWriter(new OutputStreamWriter(socket3.getOutputStream(), "UTF8"));
                Log.d(TAG, "Client - Exception handling - failed node " + al.get(j));
                pw2.write(al.get(j) + "#ERRORMESSAGE#" + "\n");
                pw2.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
}












