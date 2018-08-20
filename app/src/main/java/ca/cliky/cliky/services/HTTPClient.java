package ca.cliky.cliky.services;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class HTTPClient {
    private static final String TAG = "HTTPClient";
    private static final int CONNECTION_TIME_OUT = 0x5dc;

    public static String get(String server, String creds) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        Log.i(TAG, server);
        int code = -0x1;
        String body = "";
        HttpURLConnection connection = null;
        Log.i("CRED",creds);
        try {
            URL url = new URL(server);
            connection = (HttpURLConnection)url.openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setConnectTimeout(CONNECTION_TIME_OUT);
            connection.setRequestProperty("Authorization", "Basic " + creds);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            code = connection.getResponseCode();
            body = readInputStream(connection.getInputStream());
        } catch(MalformedURLException e) {
            body = "URL Error";
        } catch(UnknownHostException e) {
            body = "URL Error";
        } catch(IOException one) {
            one.printStackTrace();
            if(connection != null) {
                try {
                    code = connection.getResponseCode();
                } catch(IOException two) {
                    two.printStackTrace();
                } finally {
                    if(connection != null) {
                    }
                    connection.disconnect();
                }
            }
            Log.i(Integer.toString(code), body);
            return body;
        }
        return body;
    }

    public static Response basic_post(String server, String data) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        Log.i("DATA", data);
        Log.i("POST", server);
        int code = -0x1;
        String body = "";
        HttpURLConnection connection = null;
        try {
            URL url = new URL(server);
            connection = (HttpURLConnection)url.openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setConnectTimeout(CONNECTION_TIME_OUT);
            connection.setRequestProperty("Content-Types", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStream output = connection.getOutputStream();
            output.write(data.getBytes());
            output.close();
            connection.connect();
            code = connection.getResponseCode();
            body = readInputStream(connection.getInputStream());
        } catch(MalformedURLException e) {
            body = "URL Error";
        } catch(UnknownHostException e) {
            body = "URL Error";
        } catch(IOException one) {
            one.printStackTrace();
            if(connection != null) {
                try {
                    code = connection.getResponseCode();
                } catch(IOException two) {
                    two.printStackTrace();
                } finally {
                    if(connection != null) {
                    }
                    connection.disconnect();
                }
            }
            Log.i(Integer.toString(code), body);
            return new Response(code, body);
        }
        // Parsing error may occure here :(
        return new Response(code,body);
    }

    public static Response post(String server, String creds, String data) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        Log.i("DATA", data);
        Log.i("POST", server);
        int code = -0x1;
        String body = "";
        HttpURLConnection connection = null;
        try {
            URL url = new URL(server);
            connection = (HttpURLConnection)url.openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setConnectTimeout(CONNECTION_TIME_OUT);
            if(!creds.isEmpty() && creds != null)
                connection.setRequestProperty("Authorization", "Basic " + creds);
            connection.setRequestProperty("Content-Types", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStream output = connection.getOutputStream();
            output.write(data.getBytes());
            output.close();
            connection.connect();
            code = connection.getResponseCode();
            body = readInputStream(connection.getInputStream());
        } catch(MalformedURLException e) {
            body = "URL Error";
        } catch(UnknownHostException e) {
            body = "URL Error";
        } catch(IOException one) {
            one.printStackTrace();
            if(connection != null) {
                try {
                    code = connection.getResponseCode();
                } catch(IOException two) {
                    two.printStackTrace();
                } finally {
                    if(connection != null) {
                    }
                    connection.disconnect();
                }
            }
            Log.i(Integer.toString(code), body);
            return new Response(code, body);
        }
        // Parsing error may occure here :(
        return new Response(code,body);
    }

    private static String readInputStream(InputStream stream) throws IOException {
        String result = null;
        StringBuffer sb = new StringBuffer();
        InputStream is = null;
        try {
            is = new BufferedInputStream(stream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append(0xa);
            }
            result = sb.toString();
        } catch (Exception e) {
            Log.i(TAG, "Error Reading Input");
            result = null;
        } finally {
            if(is != null) {
                try {
                    is.close();
                }
                catch (IOException e) {
                    Log.i(TAG, "Error Closing InputStream");
                }
            }
        }
        Log.e(TAG, result);
        return result;
    }

    public static boolean isError(int code) {
        return (code != 0xc8);
    }

    public static String getErrorMessage(Context context, int code) {
        switch(code) {
            case 401:
            {
                return "Wrong Username or Password";
            }
            case 404:
            {
                return "Wrong URL";
            }
            case 301:
            {
                return "Wrong URL";
            }
        }
        return "Please Try again";
    }

    private static String convertStreamToString(java.io.InputStream is) {
        try {
            return new java.util.Scanner(is).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        }
    }
}
