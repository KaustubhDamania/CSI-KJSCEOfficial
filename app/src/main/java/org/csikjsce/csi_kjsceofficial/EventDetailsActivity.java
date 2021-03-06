package org.csikjsce.csi_kjsceofficial;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.csikjsce.csi_kjsceofficial.POJO.Event;

public class EventDetailsActivity extends AppCompatActivity implements View.OnClickListener{
    String TAG = EventDetailsActivity.class.getSimpleName();
    Event event;
    CollapsingToolbarLayout collapsingToolbar;
    Toolbar toolbar;
    TextView eventTitle;
    TextView eventDate;
    TextView eventCategory;
    TextView eventAudience;
    TextView eventDescrip;
    ImageView eventImage;

    String shareAppName;
    ImageView fbIcon, instaIcon, whatsappIcon, shareIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        toolbar = findViewById(R.id.toolbar);
        collapsingToolbar = findViewById(R.id.event_details_collapsing_toolbar);
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.fui_transparent));
        collapsingToolbar.setCollapsedTitleTypeface(ResourcesCompat
                .getFont(this, R.font.titilium_regular));
        setSupportActionBar(toolbar);

        final Drawable uparrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
        getSupportActionBar().setHomeAsUpIndicator(uparrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fbIcon = findViewById(R.id.facebook_iv);
        instaIcon = findViewById(R.id.instagram_iv);
        whatsappIcon = findViewById(R.id.whatsapp_iv);
        shareIcon = findViewById(R.id.share_iv);

    }

    @Override
    protected void onResume() {
        super.onResume();

        fbIcon.setOnClickListener(this);
        instaIcon.setOnClickListener(this);
        whatsappIcon.setOnClickListener(this);
        shareIcon.setOnClickListener(this);
        Intent intent = getIntent();
        event = intent.getParcelableExtra("Event");
        FloatingActionButton fab = findViewById(R.id.fab);
        if(!URLUtil.isValidUrl(event.getRegister())){
            fab.setVisibility(View.GONE);
        } else fab.setOnClickListener(this);

        eventDate = findViewById(R.id.event_date_textview);
        eventDescrip = findViewById(R.id.eventdetails_textview);
        eventImage = findViewById(R.id.Event_imageview);
        eventTitle = findViewById(R.id.event_title_textview);
        eventCategory = findViewById(R.id.category_tag);
        eventAudience = findViewById(R.id.audience_tag);
        toolbar.setTitle(event.getTitle());
        eventTitle.setText(event.getTitle());
        eventDate.setText(event.getEventdt());
        eventDescrip.setText(Html.fromHtml(event.getDesc()));
        eventCategory.setText(event.getCategory());
        eventAudience.setText(event.getAudience());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.default_event_pic);
        requestOptions.error(R.drawable.default_event_pic);
        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(event.getImg_url())
                .into(eventImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_eventdetail_menu, menu);

        MenuItem feedbackMenuItem = menu.findItem(R.id.feedback_menu);
        if(!URLUtil.isValidUrl(event.getFeedback()))
            feedbackMenuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.home)
            finish();
        else if(item.getItemId()==R.id.feedback_menu){
            String feedBackUrl = event.getFeedback();
            if(URLUtil.isValidUrl(feedBackUrl)){
                Utils.openLinkInCustomTab(this, feedBackUrl);
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.fab:
                String regUrl = event.getRegister();
                if(URLUtil.isValidUrl(regUrl))
                Utils.openLinkInCustomTab(this, regUrl);
                break;
            case R.id.facebook_iv:
                shareAppName = getString(R.string.package_facebook);
                Utils.onShareClick(this, event.getDesc(), shareAppName);
                break;
            case R.id.instagram_iv:
                shareAppName = getString(R.string.package_instagram);
                Utils.onShareClick(this, event.getDesc(), shareAppName);
                break;
            case R.id.whatsapp_iv:
                shareAppName = getString(R.string.package_whatsapp);
                Utils.onShareClick(this, event.getDesc(), shareAppName);
                break;
            case R.id.share_iv:
                intent = new Intent();
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, event.getDesc());
                startActivity(intent);
                break;
        }

    }
}