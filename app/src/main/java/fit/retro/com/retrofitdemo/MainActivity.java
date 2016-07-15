package fit.retro.com.retrofitdemo;

import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    Gson builder;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;



    Button btnGet, btnPost,btnPostBosy,btnPut,btnUpload;
    TextView txtResponse;
    ProgressBar prg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builder=new GsonBuilder().create();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
        initListeners();




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    private void initViews(){
        btnGet=(Button)findViewById(R.id.btnGet);
        btnPost=(Button)findViewById(R.id.btnPost);
        btnPostBosy=(Button)findViewById(R.id.btnPostBody);
        btnPut=(Button)findViewById(R.id.btnPutBody);
        btnUpload=(Button)findViewById(R.id.btnUpload);
        txtResponse=(TextView)findViewById(R.id.txtResponse);
        prg=(ProgressBar)findViewById(R.id.prgBar);
    }

    private void initListeners(){
        btnGet.setOnClickListener(this);
        btnPost.setOnClickListener(this);
        btnPostBosy.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnPut.setOnClickListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {



        switch (v.getId()){

            case R.id.btnGet:
                prg.setVisibility(View.VISIBLE);
                ApiBuilder.getInstance().getUser().enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        txtResponse.setText(builder.toJson(response.body(),User.class));
                        prg.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        prg.setVisibility(View.GONE);
                    }
                });
                break;

            case R.id.btnPost:
                prg.setVisibility(View.VISIBLE);
                ApiBuilder.getInstance().createUserWithFieldParam("raam","laal").enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        txtResponse.setText(builder.toJson(response.body(),User.class));
                        prg.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        prg.setVisibility(View.GONE);
                    }
                });
                break;

            case R.id.btnPostBody:
                prg.setVisibility(View.VISIBLE);
                User user=new User();
                user.setFirst_name("Tom");
                user.setLast_name("Hannes");
                ApiBuilder.getInstance().createUser(user).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        txtResponse.setText(builder.toJson(response.body(),User.class));
                        prg.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        prg.setVisibility(View.GONE);
                    }
                });
                break;
            case R.id.btnPutBody:
                prg.setVisibility(View.VISIBLE);
                User user1=new User();
                user1.setFirst_name("Tom");
                user1.setLast_name("Hannes");
                ApiBuilder.getInstance().updateUser(user1).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        txtResponse.setText(builder.toJson(response.body(),User.class));
                        prg.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        prg.setVisibility(View.GONE);
                    }
                });
                break;
            case R.id.btnUpload:
                dispatchTakePictureIntent();
                break;


        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
               // ...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_IMAGE_CAPTURE){
            File f=new File(mCurrentPhotoPath);
            uploadFile(f);
        }
    }


    private void uploadFile(File file) {



        prg.setVisibility(View.VISIBLE);
        String st=file.getName();
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", st, requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);


        ApiBuilder.getInstance().upload(description,body).enqueue(new Callback<PicData>() {
            @Override
            public void onResponse(Call<PicData> call, Response<PicData> response) {

                txtResponse.setText(builder.toJson(response.body(), PicData.class));
                prg.setVisibility(View.GONE);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(response.body().getUrl()));
                startActivity(i);

            }

            @Override
            public void onFailure(Call<PicData> call, Throwable t) {

                prg.setVisibility(View.GONE);
            }
        });

    }
}
