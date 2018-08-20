package ca.cliky.cliky.processess;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.cliky.cliky.database.Departments;
import ca.cliky.cliky.database.Retailers;
import ca.cliky.cliky.database.Types;
import ca.cliky.cliky.database.Users;
import ca.cliky.cliky.essentials.Constant;
import ca.cliky.cliky.services.HTTPClient;

public class LoginProcess {
    private static String TAG = "processess.login";
    public static void login(Context context, String creds) {
        Log.i(TAG, "Logging In");
        String url = Constant.API_ME;
        String response = HTTPClient.get(url, creds);
        storeUser(context, creds, response);

        String retail_url = Constant.API_RETAILER;
        String retailer_response = HTTPClient.get(retail_url, creds);
        storeRetailers(context, retailer_response);

        String department_url = Constant.API_DEPARTMENTS;
        String department_response = HTTPClient.get(department_url, creds);
        storeDepartments(context, department_response);

        String type_url = Constant.API_TYPE;
        String type_response = HTTPClient.get(type_url, creds);
        storeTypes(context, type_response);
    }

    public static void storeUser(Context context, String creds, String user) {
        try {
            JSONObject object = new JSONObject(user);
            Users users = new Users(context);
            users.deleteUser();
            users.saveUser(object.has("name")?object.getString("name"):"",creds,object.has("first_name")?object.getString("first_name"):"",object.has("last_name")?object.getString("last_name"):"",object.has("email")?object.getString("email"):"",object.has("phone")?object.getString("phone"):"",object.has("street_address")?object.getString("street_address"):"",object.has("city")?object.getString("city"):"",object.has("zip_code")?object.getString("zip_code"):"",object.has("country")?object.getString("country"):"");
        } catch (JSONException e) {e.printStackTrace();}
    }

    public static void storeRetailers(Context context, String response){
        try{
            Retailers retailers = new Retailers(context);
            retailers.deleteRetailers();
            JSONObject rets = new JSONObject(response);
            JSONArray retailerArray = new JSONArray(rets.getString("retailers"));
            for(int i=0; i<retailerArray.length(); i++){
                JSONObject retailer = retailerArray.getJSONObject(i);
                retailers.saveRetailers(retailer.has("id")?retailer.getString("id"):"",retailer.has("name")?retailer.getString("name"):"",retailer.has("url")?retailer.getString("url"):"",retailer.has("logo")?retailer.getString("logo"):"","NULL");
            }
        } catch(Exception e) {e.printStackTrace(); }
    }

    public static void storeDepartments(Context context, String response) {
        try {
            Departments departments = new Departments(context);
            departments.deleteDepartment();
            JSONObject depts = new JSONObject(response);
            JSONArray deptArray = new JSONArray(depts.getString("departments"));
            for(int i = 0; i<deptArray.length(); i++) {
                JSONObject dept = deptArray.getJSONObject(i);
                departments.saveDepartment(dept.getString("id"), dept.getString("name"));
            }
        } catch(Exception e) {e.printStackTrace();}
    }

    public static void storeTypes(Context context, String response) {
        try {
            Types types = new Types(context);
            types.deleteType();
            JSONObject typObj = new JSONObject(response);
            JSONArray typArray = new JSONArray(typObj.getString("types"));
            for(int i = 0; i<typArray.length(); i++) {
                JSONObject typ = typArray.getJSONObject(i);
                types.saveType(typ.getString("id"), typ.getString("name"));
            }
        } catch(Exception e) {e.printStackTrace();}
    }
}
