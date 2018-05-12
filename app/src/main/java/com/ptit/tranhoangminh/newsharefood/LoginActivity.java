package com.ptit.tranhoangminh.newsharefood;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ptit.tranhoangminh.newsharefood.views.HomePageRes.HomePageResActivity;
import com.ptit.tranhoangminh.newsharefood.views.Register.RegisterActivity;

/**
 * Created by Dell on 3/12/2018.
 */

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, FirebaseAuth.AuthStateListener {
    Button btnGoogle, btnLogin, btnDK;
    GoogleApiClient apiClient;
    public static int CODE_GG = 261;
    public static int CHECK_AUTHENTICATION = 0;
    FirebaseAuth firebaseAuth;
    EditText edtEmail, edtPass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        SetControl();
//        btnGoogle.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
//        btnDK.setOnClickListener(this);


        CreateClientLoginGG();


    }

    private void SetControl() {
//        btnGoogle=findViewById(R.id.btnGoogle);
        edtEmail = findViewById(R.id.edtEmailLogin);
        edtPass = findViewById(R.id.edtPassLogin);
        btnLogin = findViewById(R.id.btnLogin);
//        btnDK=findViewById(R.id.btnDangKi);
    }

    public void CreateClientLoginGG() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(this);
    }

    public void LoginGG(GoogleApiClient apiClient) {
        CHECK_AUTHENTICATION = 1;
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(intent, CODE_GG);
    }

    public void FirebaseLoginAuthentication(String token_id) {
        if (CHECK_AUTHENTICATION == 1) {
            AuthCredential authCredential = GoogleAuthProvider.getCredential(token_id, null);
            firebaseAuth.signInWithCredential(authCredential);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_GG) {
            if (resultCode == RESULT_OK) {
                GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                GoogleSignInAccount signInAccount = googleSignInResult.getSignInAccount();
                String tokenID = signInAccount.getIdToken();
                FirebaseLoginAuthentication(tokenID);
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
//            case R.id.btnGoogle:
//                LoginGG(apiClient);
//                break;
            case R.id.btnLogin:
                Login();
                break;
//            case R.id.btnDangKi:
//                Intent iDK=new Intent(this, RegisterActivity.class);
//                startActivity(iDK);
//                break;
            case R.id.idRegister:
                Intent register = new Intent(this, RegisterActivity.class);
                startActivity(register);
                break;


        }
    }

    private void Login() {
        String email = edtEmail.getText().toString();
        String pass = edtPass.getText().toString();
        firebaseAuth.signInWithEmailAndPassword(email, pass);
    }

    //method: know status of user login or log out
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            Intent in = new Intent(LoginActivity.this, HomePageResActivity.class);
            startActivity(in);
        } else {

        }

    }

    public void onRegisterClicked(View view) {
        Intent register = new Intent(this, RegisterActivity.class);
        startActivity(register);

    }
}
