package examproject.group22.roominator.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

import examproject.group22.roominator.Models.User;
import examproject.group22.roominator.R;

public class SignUpActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    ImageView avatar;
    Button createBtn;
    EditText name;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        avatar = (ImageView)findViewById(R.id.avatar);
        createBtn = (Button)findViewById(R.id.singUp_button);
        name = (EditText)findViewById(R.id.signup_name_txt);
        password = (EditText)findViewById(R.id.singUp_password_txt);

    }
   // https://developer.android.com/training/camera/photobasics.html
    public void onClickTakePicture(View view){
        Intent  takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePhotoIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takePhotoIntent,REQUEST_IMAGE_CAPTURE);
        }
    }
    public void onClickCreate(View view){
        Intent createIntent = new Intent(SignUpActivity.this, OverviewActivity.class);
        String n = name.getText().toString();
        String p = password.getText().toString();
        byte[] img = convertImgToByteArray(avatar); //TODO: Den skal være et billede her og først konverteres senere
        User u = new User(n, p, null);
        createIntent.putExtra("newUser",u);
        startActivity(createIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            avatar.setImageBitmap(photo);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
   // http://stackoverflow.com/questions/20700181/convert-imageview-in-bytes-android
    public byte[] convertImgToByteArray(ImageView imageView){
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap b = imageView.getDrawingCache();
        ByteArrayOutputStream BAPS = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG,100, BAPS);
        byte[] byteArray = BAPS.toByteArray();
        return  byteArray;
    }
    public void saveUserToDataBase(User user){

    }
}
