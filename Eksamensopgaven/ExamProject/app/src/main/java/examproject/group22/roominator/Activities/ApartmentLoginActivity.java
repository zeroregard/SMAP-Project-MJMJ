package examproject.group22.roominator.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import examproject.group22.roominator.DatabaseService;
import examproject.group22.roominator.Models.ApartmentModel;
import examproject.group22.roominator.Models.UserModel;
import examproject.group22.roominator.R;

public class ApartmentLoginActivity extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;


    EditText name;
    EditText password;
    DatabaseService db;
    UserModel currentUser;
    int aId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_log_in);

        name = (EditText)findViewById(R.id.apartment_name_txt);
        password = (EditText)findViewById(R.id.apartment_password_txt);
        db = DatabaseService.getInstance(getApplicationContext());
        LocalBroadcastManager.getInstance(this).registerReceiver(mReciever,new IntentFilter(DatabaseService.INTENT_APARTMENT_AUTHENTICATION));
        LocalBroadcastManager.getInstance(this).registerReceiver(userReciever,new IntentFilter(DatabaseService.INTENT_USER));
        FetchUser();
    }

    private void load_from_sp()
    {
        pref = ApartmentLoginActivity.this.getPreferences(MODE_PRIVATE);
        prefEditor = pref.edit();
    }

    private void save_to_sp(ApartmentModel a)
    {
        prefEditor.putInt("aId",a.id);
        prefEditor.apply();
    }


    public void FetchUser()
    {
        Intent i = getIntent();
        UserModel u = (UserModel)i.getSerializableExtra("UserModel");
        currentUser = u;
        //Log.v("Ah", "oh");
        //Check if user is in apartment ?
        //If yes, cool
        //If not, add him to apartment... somehow :'(
    }

    public void onClickLogInApartment(View view){
        String username = name.getText().toString();
        String upassword = password.getText().toString();
        if(username.equals(""))
            LoginError("Username must be at least 1 character");
        else if(upassword.equals(""))
            LoginError("Password must be at least 1 character");
        else
            db.get_CheckPassWithApartmentName(name.getText().toString(), password.getText().toString());

    }

    public void LoginError(String error)
    {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    private BroadcastReceiver mReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            aId = intent.getIntExtra("apartmentID", 0);
            boolean passwordOK = intent.getBooleanExtra("apartmentOK", false);
            if(aId == 0) //the apartment doesn't exist
                makePopMessage();
            else if(passwordOK == false)
                LoginError("That apartment exists, but the password is wrong!"); //TODO: Externalize
            else {
                FetchUser();
               int d =  currentUser.id;
                db.get_user(currentUser.id);
            }
        }
    };
   private BroadcastReceiver userReciever = new BroadcastReceiver() {
       @Override
       public void onReceive(Context context, Intent intent) {
              try {
                  UserModel u = (UserModel) intent.getSerializableExtra("UserModel");
                  if (u.ApartmentID != 0) {
                      LogIn(u.ApartmentID);
                  }else{
                    db.put_userToApartment(u,aId);
                      LogIn(aId);
                  }
              }catch (Exception e){
                 Log.v("Debug",e.toString());
              }
       }
   };

    public void LogIn(int apartment_id)
    {
        Intent loggedInIntent = new Intent(ApartmentLoginActivity.this, OverviewActivity.class);
        loggedInIntent.putExtra("apartmentID", apartment_id);
        loggedInIntent.putExtra("UserModel", currentUser);
        startActivity(loggedInIntent);
        finish();
    }

    public void makePopMessage() {
        final AlertDialog alertDialog = new AlertDialog.Builder(ApartmentLoginActivity.this).create();
        alertDialog.setTitle("New ApartmentModel?"); //TODO: Externalize
        alertDialog.setMessage("ApartmentModel not found, create a new one with this name and password?"); //TODO: Externalize
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Create", new DialogInterface.OnClickListener(){
           public void onClick(DialogInterface dialog, int which){
               ApartmentModel a = new ApartmentModel(name.getText().toString(),password.getText().toString());
               db.post_NewApartment(a);
           }
        });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which)
            {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }
}
