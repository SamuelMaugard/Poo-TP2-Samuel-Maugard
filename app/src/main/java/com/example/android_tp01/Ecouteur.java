package com.example.android_tp01;

import android.graphics.Color;
import android.view.View;
import android.widget.CompoundButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.WebSocket;

public class Ecouteur implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, Emitter.Listener {
    Chat chat;
    Preference preferences;
    Socket mSocket;

    public Ecouteur(Chat c,Preference p) {
        this.chat = c;
        this.preferences = p;
        try {
            mSocket = IO.socket("http://78.243.124.47:10101");
            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) { System.out.println("Connnecté");
                }
            });
            mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) { System.out.println("Déconnecté");

                }
            });
            mSocket.on("chatevent", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    String userName = "";
                    String message = "";
                    System.out.println(args[0].toString());
                    try {
                        JSONObject obj = new JSONObject(args[0].toString());
                        userName = obj.getString("userName");
                        message = obj.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    chat.ajouterMessage(userName, message);
                }
            });
            mSocket.on("connected list", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        JSONArray connected = data.getJSONArray("connected");
                        chat.ajouterMessage("-------Liste des Participants -------",Color.RED);
                        System.out.println(args[0].toString());
                        for (int i = 0; i < connected.length() ; i++) {
                            chat.ajouterMessage(connected.get(i).toString(), Color.RED);
                        }
                        chat.ajouterMessage("-------Fin de la Liste des Participants -------",Color.RED);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(mSocket.connected()) {
            JSONObject msg = new JSONObject();
            try {
                msg.accumulate("userName", preferences.obtenirSurnom());
                msg.accumulate("message", chat.obtenirTextTape());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mSocket.emit("chatevent", msg);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {connexion();
        } else { deconnexion(); }
    }

    void connexion() {
        mSocket.connect();
    }

    void deconnexion() {
        mSocket.disconnect();
    }

    void emitMessage(String msg) {
        mSocket.emit("chatevent", msg);
    }

    @Override
    public void call(Object... args) {

    }

    public void demandeListesConnectés() {
        JSONObject msg = new JSONObject();
        mSocket.emit("queryconnected", msg);
    }
}
