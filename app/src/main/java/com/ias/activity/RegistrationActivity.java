package com.ias.activity;

import android.Manifest;
import android.support.v7.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ias.handler.Constant;
import com.ias.handler.RequestHandler;
import com.ias.iaslogin.R;
import com.ias.model.RegistrationMember;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CAMERA_REQUEST = 1888;
    private TextView screenTitle;
    private Spinner titleSpinner;
    private TextView nameTitleLabel;
    private EditText firstNameField;
    private EditText lastNameField;
    private TextView fullAddressLabel;
    private EditText firstLineAddressField;
    private EditText secondLineAddressField;
    private EditText cityField;
    private EditText stateProvinceRegionField;
    private EditText postalZipCodeField;
    private TextView mobilePhoneLabel;
    private EditText mobilePhoneField;
    private TextView emailLabel;
    private EditText emailField;
    private TextView birthPlaceLabel;
    private EditText birthPlaceField;
    private TextView birthDayLabel;
    private EditText birthDayField;
    private TextView vehicleLicensePlateLabel;
    private EditText vehicleLicensePlateField;
    private TextView vehicleYearAssemblyLabel;
    private EditText vehicleYearAssemblyField;
    private TextView vehicleTypeLabel;
    private Spinner vehicleTypeSpinner;
    private TextView vehicleGearTypeLabel;
    private RadioGroup vehicleGearRadioGroup;
    private RadioButton vehicleGearRadioButton;
    private TextView vehicleColorLabel;
    private EditText vehicleColorField;
    private TextView shirtSizeLabel;
    private Spinner shirtSizeSpinner;
    private TextView driverLicenseLabel;
    private EditText driverLicenseField;
    private TextView validTimeDriverLicenseLabel;
    private EditText validTimeLicenseField;
    private TextView driverLicensePhotoLabel;
    private ImageView driverLicensePhotoImage;
    private TextView vehicleLicensePhotoLabel;
    private ImageView vehicleLicensePhotoImage;
    private TextView memberPhotoLabel;
    private ImageView memberPhotoImage;
    private TextView evidenceTransferPhotoLabel;
    private ImageView evidenceTransferPhotoImage;
    private Bitmap driverLicensePhotoBitmap;
    private Integer selectedImage;
    private Bitmap vehicleLicensePhotoBitmap;
    private Bitmap memberPhotoBitmap;
    private Bitmap evidenceTransferPhotoBitmap;
    private Button submitButton;
    private DatePickerDialog birthDateFieldDatePickerDialog;
    private DatePickerDialog vehicleYearAssemblyDatePickerDialog;
    private DatePickerDialog validTimeLicenseDatePickerDialog;
    private SimpleDateFormat fullFormatSimpleDateFormat;
    private SimpleDateFormat yearSimpleDateFormat;
    private SharedPreferences sharedPreferences;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.registration_activity);

        fullFormatSimpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        yearSimpleDateFormat = new SimpleDateFormat("yyyy", Locale.US);

        findViewsById();

        setDateTimeField();

        //getMobilePhoneInfo();
    }

    private void findViewsById() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        screenTitle = (TextView) findViewById(R.id.screenTitle);
        screenTitle.setText(Html.fromHtml(getResources().getString(R.string.title)));

        nameTitleLabel = (TextView) findViewById(R.id.nameTitleLabel);
        nameTitleLabel.setText(Html.fromHtml(getResources().getString(R.string.name_label)));

        titleSpinner = (Spinner) findViewById(R.id.titleSpinner);
        firstNameField = (EditText) findViewById(R.id.firstNameField);
        lastNameField = (EditText) findViewById(R.id.lastNameField);

        fullAddressLabel = (TextView) findViewById(R.id.fullAddressLabel);
        fullAddressLabel.setText(Html.fromHtml(getResources().getString(R.string.full_address_label)));

        firstLineAddressField = (EditText) findViewById(R.id.firstLineAddressField);
        secondLineAddressField = (EditText) findViewById(R.id.secondLineAddressField);
        cityField = (EditText) findViewById(R.id.cityField);
        stateProvinceRegionField = (EditText) findViewById(R.id.stateProvinceRegionField);
        postalZipCodeField = (EditText) findViewById(R.id.postalZipCodeField);

        mobilePhoneLabel = (TextView) findViewById(R.id.mobilePhoneLabel);
        mobilePhoneLabel.setText(Html.fromHtml(getResources().getString(R.string.mobile_phone_label)));

        mobilePhoneField = (EditText) findViewById(R.id.mobilePhoneField);

        emailLabel = (TextView) findViewById(R.id.emailLabel);
        emailLabel.setText(Html.fromHtml(getResources().getString(R.string.email_label)));

        emailField = (EditText) findViewById(R.id.emailField);

        birthPlaceLabel = (TextView) findViewById(R.id.birthPlaceLabel);
        birthPlaceLabel.setText(Html.fromHtml(getResources().getString(R.string.birthplace_label)));

        birthPlaceField = (EditText) findViewById(R.id.birthPlaceField);

        birthDayLabel = (TextView) findViewById(R.id.birthDayLabel);
        birthDayLabel.setText(Html.fromHtml(getResources().getString(R.string.birthday_label)));

        birthDayField = (EditText) findViewById(R.id.birthDayField);
        birthDayField.setInputType(InputType.TYPE_NULL);

        vehicleLicensePlateLabel = (TextView) findViewById(R.id.vehicleLicensePlateLabel);
        vehicleLicensePlateLabel.setText(Html.fromHtml(getResources().getString(R.string.vehicleLicensePlateLabel)));

        vehicleLicensePlateField = (EditText) findViewById(R.id.vehicleLicensePlateField);

        vehicleYearAssemblyLabel = (TextView) findViewById(R.id.vehicleYearAssemblyLabel);
        vehicleYearAssemblyLabel.setText(Html.fromHtml(getResources().getString(R.string.vehicleLicensePlateLabel)));

        vehicleYearAssemblyField = (EditText) findViewById(R.id.vehicleYearAssemblyField);

        vehicleTypeLabel = (TextView) findViewById(R.id.vehicleTypeLabel);
        vehicleTypeLabel.setText(Html.fromHtml(getResources().getString(R.string.vehicleGearTypeLabel)));

        vehicleTypeSpinner = (Spinner) findViewById(R.id.vehicleTypeSpinner);

        vehicleGearTypeLabel = (TextView) findViewById(R.id.vehicleGearTypeLabel);
        vehicleGearTypeLabel.setText(Html.fromHtml(getResources().getString(R.string.vehicleGearTypeLabel)));

        vehicleGearRadioGroup = (RadioGroup) findViewById(R.id.vehicleGearRadioGroup);

        vehicleColorLabel = (TextView) findViewById(R.id.vehicleColorLabel);
        vehicleColorLabel.setText(Html.fromHtml(getResources().getString(R.string.vehicleColorLabel)));

        vehicleColorField = (EditText) findViewById(R.id.vehicleColorField);

        shirtSizeLabel = (TextView) findViewById(R.id.shirtSizeLabel);
        shirtSizeLabel.setText(Html.fromHtml(getResources().getString(R.string.shirtSizeLabel)));

        shirtSizeSpinner = (Spinner) findViewById(R.id.shirtSizeSpinner);

        driverLicenseLabel = (TextView) findViewById(R.id.driverLicenseLabel);
        driverLicenseLabel.setText(Html.fromHtml(getResources().getString(R.string.driverLicenseLabel)));

        driverLicenseField = (EditText) findViewById(R.id.driverLicenseField);

        validTimeDriverLicenseLabel = (TextView) findViewById(R.id.validTimeDriverLicenseLabel);
        validTimeDriverLicenseLabel.setText(Html.fromHtml(getResources().getString(R.string.validTimeDriverLicenseLabel)));

        validTimeLicenseField = (EditText) findViewById(R.id.validTimeLicenseField);

        driverLicensePhotoLabel = (TextView) findViewById(R.id.driverLicensePhotoLabel);
        driverLicensePhotoLabel.setText(Html.fromHtml(getResources().getString(R.string.driverLicensePhotoLabel)));

        driverLicensePhotoImage = (ImageView) findViewById(R.id.driverLicensePhotoImage);

        vehicleLicensePhotoLabel = (TextView) findViewById(R.id.vehicleLicensePhotoLabel);
        vehicleLicensePhotoLabel.setText(Html.fromHtml(getResources().getString(R.string.vehicleLicensePhotoLabel)));

        vehicleLicensePhotoImage = (ImageView) findViewById(R.id.vehicleLicensePhotoImage);

        memberPhotoLabel = (TextView) findViewById(R.id.memberPhotoLabel);
        memberPhotoLabel.setText(Html.fromHtml(getResources().getString(R.string.memberPhotoLabel)));

        memberPhotoImage = (ImageView) findViewById(R.id.memberPhotoImage);

        evidenceTransferPhotoLabel = (TextView) findViewById(R.id.evidenceTransferPhotoLabel);
        evidenceTransferPhotoLabel.setText(Html.fromHtml(getResources().getString(R.string.evidenceTransferPhotoLabel)));

        evidenceTransferPhotoImage = (ImageView) findViewById(R.id.evidenceTransferPhotoImage);

        driverLicensePhotoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImage = 1;
                showFileChooser();
            }
        });

        vehicleLicensePhotoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImage = 2;
                showFileChooser();
            }
        });

        memberPhotoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImage = 3;
                showFileChooser();
            }
        });

        evidenceTransferPhotoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImage = 4;
                showFileChooser();
            }
        });

        submitButton = (Button) findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateField();
                try {
                    Context context = getApplicationContext();

                    StringBuffer sb = new StringBuffer();
                    sb.append("Title : ").append(titleSpinner.getSelectedItem().toString()).append("\n");
                    sb.append("First : ").append(firstNameField.getText()).append("\n");
                    sb.append("Last : ").append(lastNameField.getText()).append("\n");
                    sb.append("First Line Address : ").append(firstLineAddressField.getText()).append("\n");
                    sb.append("Second Line Address : ").append(secondLineAddressField.getText()).append("\n");
                    sb.append("City : ").append(cityField.getText()).append("\n");
                    sb.append("State/Province/Region : ").append(stateProvinceRegionField.getText()).append("\n");
                    sb.append("Postal/Zip Code : ").append(postalZipCodeField.getText()).append("\n");
                    sb.append("Mobile Phone : ").append(mobilePhoneField.getText()).append("\n");
                    sb.append("Email : ").append(emailField.getText()).append("\n");
                    sb.append("Birthplace : ").append(birthPlaceField.getText()).append("\n");
                    sb.append("Birthday : ").append(birthDayField.getText()).append("\n");
                    sb.append("Vehicle License : ").append(vehicleLicensePlateField.getText()).append("\n");
                    sb.append("Vehicle Year Assembly : ").append(vehicleYearAssemblyField.getText()).append("\n");
                    sb.append("Tipe Rush : ").append(vehicleTypeSpinner.getSelectedItem().toString()).append("\n");

                    sb.append("Driver Licence Image String : ").append(getStringImage(driverLicensePhotoBitmap)).append("\n");
                    sb.append("Vehicle License Image String : ").append(getStringImage(vehicleLicensePhotoBitmap)).append("\n");
                    sb.append("Member Image String : ").append(getStringImage(memberPhotoBitmap)).append("\n");
                    sb.append("Evidence Transfer Image String : ").append(getStringImage(evidenceTransferPhotoBitmap)).append("\n");

                    int selectedId = vehicleGearRadioGroup.getCheckedRadioButtonId();

                    System.out.println("yang dipilih : " + selectedId);

                    vehicleGearRadioButton = (RadioButton) findViewById(selectedId);

                    System.out.println("radiobutton : " + vehicleGearRadioButton.getText());
                    sb.append("Tipe Transmisi : ").append(vehicleGearRadioButton.getText()).append("\n");
                    sb.append("Warna Mobil : ").append(vehicleColorField.getText()).append("\n");
                    sb.append("Ukuran Baju : ").append(shirtSizeSpinner.getSelectedItem().toString()).append("\n");
                    sb.append("Nomer SIM : ").append(driverLicenseField.getText()).append("\n");
                    sb.append("Masa berlaku SIM : ").append(validTimeLicenseField.getText()).append("\n");

                    RegistrationMember registrationMember = new RegistrationMember();
                    registrationMember.setTitle(titleSpinner.getSelectedItem().toString());
                    registrationMember.setFirstName(firstNameField.getText().toString());
                    registrationMember.setLastName(lastNameField.getText().toString());
                    registrationMember.setFirstLineAddress(firstLineAddressField.getText().toString());
                    registrationMember.setSecondLineAddress(secondLineAddressField.getText().toString());
                    registrationMember.setCity(cityField.getText().toString());
                    registrationMember.setStateProvinceRegion(stateProvinceRegionField.getText().toString());
                    registrationMember.setPostalZipCode(postalZipCodeField.getText().toString());
                    registrationMember.setMobilePhone(mobilePhoneField.getText().toString());
                    registrationMember.setEmail(emailField.getText().toString());
                    registrationMember.setBirthPlace(birthPlaceField.getText().toString());
                    registrationMember.setBirthDay(fullFormatSimpleDateFormat.parse(birthDayField.getText().toString()));
                    registrationMember.setVehicleLicensePlate(vehicleLicensePlateField.getText().toString());
                    registrationMember.setVehicleYearAssembly(vehicleYearAssemblyField.getText().toString());
                    registrationMember.setVehicleType(vehicleTypeSpinner.getSelectedItem().toString());
                    registrationMember.setVehicleGear(vehicleGearRadioButton.getText().toString());
                    registrationMember.setVehicleColor(vehicleColorField.getText().toString());
                    registrationMember.setShirtSize(shirtSizeSpinner.getSelectedItem().toString());
                    registrationMember.setDriverLicense(driverLicenseField.getText().toString());
                    registrationMember.setValidTimeLicense(fullFormatSimpleDateFormat.parse(validTimeLicenseField.getText().toString()));
                    registrationMember.setDriverLicenseImage(getStringImage(driverLicensePhotoBitmap));
                    registrationMember.setVehicleLicenseImage(getStringImage(vehicleLicensePhotoBitmap));
                    registrationMember.setMemberImage(getStringImage(memberPhotoBitmap));
                    registrationMember.setEvidenceTransferImage(getStringImage(evidenceTransferPhotoBitmap));

                    /*TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                    System.out.println(hasPermissions(context,Manifest.permission.READ_PHONE_STATE));

                    try {
                        String IMEINumber=telephonyManager.getDeviceId();
                        String subscriberID=telephonyManager.getDeviceId();
                        String SIMSerialNumber=telephonyManager.getSimSerialNumber();
                        String networkCountryISO=telephonyManager.getNetworkCountryIso();
                        String SIMCountryISO=telephonyManager.getSimCountryIso();
                        String softwareVersion=telephonyManager.getDeviceSoftwareVersion();
                        String voiceMailNumber=telephonyManager.getVoiceMailNumber();
                        String deviceId = telephonyManager.getDeviceId();
                        String phoneNumber = telephonyManager.getLine1Number();

                        String strphoneType="";

                        int phoneType=telephonyManager.getPhoneType();

                        switch (phoneType)
                        {
                            case (TelephonyManager.PHONE_TYPE_CDMA):
                                strphoneType="CDMA";
                                break;
                            case (TelephonyManager.PHONE_TYPE_GSM):
                                strphoneType="GSM";
                                break;
                            case (TelephonyManager.PHONE_TYPE_NONE):
                                strphoneType="NONE";
                                break;
                        }

                        boolean isRoaming=telephonyManager.isNetworkRoaming();

                        String info="Phone Details:\n";
                        info+="\n IMEI Number:"+IMEINumber;
                        info+="\n SubscriberID:"+subscriberID;
                        info+="\n Sim Serial Number:"+SIMSerialNumber;
                        info+="\n Network Country ISO:"+networkCountryISO;
                        info+="\n SIM Country ISO:"+SIMCountryISO;
                        info+="\n Software Version:"+softwareVersion;
                        info+="\n Voice Mail Number:"+voiceMailNumber;
                        info+="\n Phone Network Type:"+strphoneType;
                        info+="\n In Roaming? :"+isRoaming;
                        info+="\n Device ID : "+deviceId;
                        info+="\n Phone Number : "+phoneNumber;

                        System.out.println("INFO : "+info);

                        Toast toast = Toast.makeText(context,info,Toast.LENGTH_LONG);
                        toast.show();
                    } catch (SecurityException e) {
                        Log.e("TAG", "getMobilePhoneInfo: ", e);
                    }*/

//                    Toast toast = Toast.makeText(context,sb.toString(),Toast.LENGTH_LONG);
//                    toast.show();
                    saveToDatabase(registrationMember);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    private void validateField() {
        if (firstNameField.getText().length() == 0) {
            firstNameField.setError("Field cannot be left blank.");
        }
    }

    private void setDateTimeField() {
        birthDayField.setOnClickListener(this);
        vehicleYearAssemblyField.setOnClickListener(this);
        validTimeLicenseField.setOnClickListener(this);

        Calendar birthdayCalender = Calendar.getInstance();
        birthDateFieldDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar date = Calendar.getInstance();
                date.set(year, monthOfYear, dayOfMonth);
                birthDayField.setText(fullFormatSimpleDateFormat.format(date.getTime()));
            }
        }, birthdayCalender.get(Calendar.YEAR), birthdayCalender.get(Calendar.MONTH), birthdayCalender.get(Calendar.DAY_OF_MONTH));

        Calendar vehicleYearAssemblyCalender = Calendar.getInstance();
        vehicleYearAssemblyDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar vehicleYearAssemly = Calendar.getInstance();
                vehicleYearAssemly.set(year, monthOfYear, dayOfMonth);
                vehicleYearAssemblyField.setText(yearSimpleDateFormat.format(vehicleYearAssemly.getTime()));
            }
        }, vehicleYearAssemblyCalender.get(Calendar.YEAR), vehicleYearAssemblyCalender.get(Calendar.MONTH), vehicleYearAssemblyCalender.get(Calendar.DAY_OF_MONTH));

        Calendar validTimeLicenseCalender = Calendar.getInstance();
        validTimeLicenseDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar validTimeLicense = Calendar.getInstance();
                validTimeLicense.set(year, monthOfYear, dayOfMonth);
                validTimeLicenseField.setText(fullFormatSimpleDateFormat.format(validTimeLicense.getTime()));
            }
        }, validTimeLicenseCalender.get(Calendar.YEAR), validTimeLicenseCalender.get(Calendar.MONTH), validTimeLicenseCalender.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View v) {
        if (v == birthDayField) {
            birthDateFieldDatePickerDialog.show();
        } else if (v == vehicleYearAssemblyField) {
            vehicleYearAssemblyDatePickerDialog.show();
        } else if (v == validTimeLicenseField) {
            validTimeLicenseDatePickerDialog.show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                if (selectedImage == 1) {
                    driverLicensePhotoBitmap = bitmap;
                    driverLicensePhotoImage.setImageBitmap(driverLicensePhotoBitmap);
                } else if (selectedImage == 2) {
                    vehicleLicensePhotoBitmap = bitmap;
                    vehicleLicensePhotoImage.setImageBitmap(vehicleLicensePhotoBitmap);
                } else if (selectedImage == 3) {
                    memberPhotoBitmap = bitmap;
                    memberPhotoImage.setImageBitmap(memberPhotoBitmap);
                } else if (selectedImage == 4) {
                    evidenceTransferPhotoBitmap = bitmap;
                    evidenceTransferPhotoImage.setImageBitmap(evidenceTransferPhotoBitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == this.RESULT_OK) {

            bitmap = (Bitmap) data.getExtras().get("data");
            if (selectedImage == 1) {
                driverLicensePhotoBitmap = bitmap;
                driverLicensePhotoImage.setImageBitmap(driverLicensePhotoBitmap);
            } else if (selectedImage == 2) {
                vehicleLicensePhotoBitmap = bitmap;
                vehicleLicensePhotoImage.setImageBitmap(vehicleLicensePhotoBitmap);
            } else if (selectedImage == 3) {
                memberPhotoBitmap = bitmap;
                memberPhotoImage.setImageBitmap(memberPhotoBitmap);
            } else if (selectedImage == 4) {
                evidenceTransferPhotoBitmap = bitmap;
                evidenceTransferPhotoImage.setImageBitmap(evidenceTransferPhotoBitmap);
            }

        }
    }

    /*public Bitmap getImageBitmap(String image){
        byte[] decodedBytes = Base64.decode(image, 0);
        Bitmap bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        return bmp;
    }*/

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }


    /*public String getStringImage(Bitmap bmp){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;
    }*/

    private void showFileChooser() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                RegistrationActivity.this);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                });
        myAlertDialog.show();
    }

    public void saveToDatabase(RegistrationMember rm) {
        final RegistrationMember registrationMember = rm;
        final String UPLOAD_URL = Constant.URL_STRING + "/RegistrationMemberServlet";

        class saveToDatabase extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RegistrationActivity.this, "Please wait...", "uploading", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                System.out.println(s);
                loading.dismiss();
                if (s.equalsIgnoreCase("success")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "save successfully", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "save failed", Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                HashMap<String, String> param = new HashMap<>();
                param.put(Constant.USER, Constant.USERNAMEAPP);
                param.put(Constant.PWD, Constant.PWDAPP);
                param.put(Constant.TITLE, registrationMember.getTitle());
                param.put(Constant.FIRSTNAME, registrationMember.getFirstName());
                param.put(Constant.LASTNAME, registrationMember.getLastName());
                param.put(Constant.FIRSTLINEADDRESS, registrationMember.getFirstLineAddress());
                param.put(Constant.SECONDLINEADDRESS, registrationMember.getSecondLineAddress());
                param.put(Constant.CITY, registrationMember.getCity());
                param.put(Constant.STATEPROVINCEREGION, registrationMember.getStateProvinceRegion());
                param.put(Constant.POSTALZIPCODE, registrationMember.getPostalZipCode());
                param.put(Constant.MOBILEPHONE, registrationMember.getMobilePhone());
                param.put(Constant.EMAIL, registrationMember.getEmail());
                param.put(Constant.BIRTHPLACE, registrationMember.getBirthPlace());
                param.put(Constant.BIRTHDAY, registrationMember.getBirthDay().toString());
                param.put(Constant.VEHICLELICENSEPLATE, registrationMember.getVehicleLicensePlate());
                param.put(Constant.VEHICLEYEARASSEMBLY, registrationMember.getVehicleYearAssembly());
                param.put(Constant.VEHICLETYPE, registrationMember.getVehicleType());
                param.put(Constant.VEHICLEGEAR, registrationMember.getVehicleGear());
                param.put(Constant.VEHICLECOLOR, registrationMember.getVehicleColor());
                param.put(Constant.SHIRTSIZE, registrationMember.getShirtSize());
                param.put(Constant.DRIVERLICENSE, registrationMember.getDriverLicense());
                param.put(Constant.VALIDTIMELICENSE, registrationMember.getValidTimeLicense().toString());
                param.put(Constant.DRIVERLICENSEIMAGE, registrationMember.getDriverLicenseImage());
                param.put(Constant.VEHICLELICENSEIMAGE, registrationMember.getVehicleLicenseImage());
                param.put(Constant.MEMBERIMAGE, registrationMember.getMemberImage());
                param.put(Constant.EVIDENCETRANSFERIMAGE, registrationMember.getEvidenceTransferImage());

                return rh.sendPostRequest(UPLOAD_URL, param);
            }
        }
        saveToDatabase u = new saveToDatabase();
        u.execute();
    }

    private void getMobilePhoneInfo() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        Context context = getApplicationContext();

        checkSelfPermission(Manifest.permission.READ_PHONE_STATE);

        try {
            String IMEINumber = telephonyManager.getDeviceId();
            String subscriberID = telephonyManager.getDeviceId();
            String SIMSerialNumber = telephonyManager.getSimSerialNumber();
            String networkCountryISO = telephonyManager.getNetworkCountryIso();
            String SIMCountryISO = telephonyManager.getSimCountryIso();
            String softwareVersion = telephonyManager.getDeviceSoftwareVersion();
            String voiceMailNumber = telephonyManager.getVoiceMailNumber();

            StringBuffer sb = new StringBuffer();
            sb.append("IMEINumber : ").append(IMEINumber);
            sb.append("subscriberID").append(subscriberID);
            sb.append("SIMSerialNumber").append(SIMSerialNumber);
            sb.append("networkCountryISO").append(networkCountryISO);
            sb.append("SIMCountryISO").append(SIMCountryISO);
            sb.append("softwareVersion").append(softwareVersion);
            sb.append("voiceMailNumber").append(voiceMailNumber);

            Toast toast = Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG);
            toast.show();
        } catch (SecurityException e) {
            Log.e("TAG", "getMobilePhoneInfo: ", e);
        }
    }
}