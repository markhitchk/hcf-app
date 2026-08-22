package com.harleytg.forum.dev;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/** First-run welcome gate shown before the optional App Setup Center. */
public final class WelcomeActivity extends ThemedActivity {
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        ThemeManager.apply(this);
        prefs = getSharedPreferences(AppPrefs.FILE, 0);

        int bg = ThemeManager.isAmoled(this) ? Color.BLACK : getColor(R.color.hcf_bg);
        getWindow().setStatusBarColor(bg);
        getWindow().setNavigationBarColor(bg);
        setContentView(buildUi());

        boolean automatic = getIntent() != null
                && getIntent().getBooleanExtra(SetupCenter.EXTRA_AUTO_LAUNCHED, false);
        AppLogger.info(this, "app_welcome_open", "v" + SetupCenter.CURRENT_WELCOME_VERSION
                + (automatic ? " | first-run" : " | manual"));
    }

    @Override
    public void onBackPressed() {
        continueWithoutSetup("back");
    }

    private View buildUi() {
        int bg = ThemeManager.isAmoled(this) ? Color.BLACK : getColor(R.color.hcf_bg);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(bg);

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setGravity(Gravity.CENTER_HORIZONTAL);
        page.setPadding(dp(16), dp(20), dp(16), dp(24));
        scroll.addView(page, new ScrollView.LayoutParams(-1, -1));

        LinearLayout hero = new LinearLayout(this);
        hero.setOrientation(LinearLayout.VERTICAL);
        hero.setGravity(Gravity.CENTER_HORIZONTAL);
        hero.setBackgroundResource(R.drawable.settings_hero_background);
        hero.setPadding(dp(20), dp(20), dp(20), dp(20));
        LinearLayout.LayoutParams heroLp = new LinearLayout.LayoutParams(-1, -2);
        heroLp.topMargin = dp(8);
        page.addView(hero, heroLp);

        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.htg_app_logo);
        logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        logo.setContentDescription("Harley's Clan Forum logo");
        hero.addView(logo, new LinearLayout.LayoutParams(dp(92), dp(92)));

        TextView eyebrow = text("HARLEY'S STUDIOS", 10, getColor(R.color.hcf_meta));
        eyebrow.setGravity(Gravity.CENTER);
        eyebrow.setTypeface(null, 1);
        LinearLayout.LayoutParams eyebrowLp = new LinearLayout.LayoutParams(-1, -2);
        eyebrowLp.topMargin = dp(9);
        hero.addView(eyebrow, eyebrowLp);

        TextView welcomeTitle = text("Welcome to", 21, getColor(R.color.hcf_text));
        welcomeTitle.setGravity(Gravity.CENTER);
        welcomeTitle.setTypeface(null, 1);
        LinearLayout.LayoutParams welcomeTitleLp = new LinearLayout.LayoutParams(-1, -2);
        welcomeTitleLp.topMargin = dp(4);
        hero.addView(welcomeTitle, welcomeTitleLp);

        TextView forumTitle = text("Harley's Clan Forum", 25, getColor(R.color.hcf_cyan_bright));
        forumTitle.setGravity(Gravity.CENTER);
        forumTitle.setTypeface(null, 1);
        LinearLayout.LayoutParams forumTitleLp = new LinearLayout.LayoutParams(-1, -2);
        forumTitleLp.topMargin = dp(1);
        hero.addView(forumTitle, forumTitleLp);

        if (isDevelopmentBuild()) {
            TextView badge = text("Development Build / Beta", 10, Color.rgb(6, 16, 19));
            badge.setGravity(Gravity.CENTER);
            badge.setTypeface(null, 1);
            badge.setBackgroundResource(R.drawable.welcome_dev_badge_background);
            badge.setPadding(dp(13), dp(5), dp(13), dp(5));
            LinearLayout.LayoutParams badgeLp = new LinearLayout.LayoutParams(-2, -2);
            badgeLp.topMargin = dp(10);
            hero.addView(badge, badgeLp);
        }

        TextView welcome = text(
                "Thanks for using the Harley's Forum app. App Setup can help get Android features ready for this device, but it is completely optional.",
                12,
                getColor(R.color.hcf_muted)
        );
        welcome.setGravity(Gravity.CENTER);
        welcome.setLineSpacing(0.0f, 1.12f);
        LinearLayout.LayoutParams welcomeLp = new LinearLayout.LayoutParams(-1, -2);
        welcomeLp.topMargin = dp(14);
        hero.addView(welcome, welcomeLp);

        LinearLayout setupInfo = new LinearLayout(this);
        setupInfo.setOrientation(LinearLayout.VERTICAL);
        setupInfo.setBackgroundResource(R.drawable.card_background);
        setupInfo.setPadding(dp(12), dp(13), dp(12), dp(13));
        LinearLayout.LayoutParams infoLp = new LinearLayout.LayoutParams(-1, -2);
        infoLp.topMargin = dp(14);
        page.addView(setupInfo, infoLp);

        TextView setupTitle = text("What App Setup helps with", 14, getColor(R.color.hcf_cyan_bright));
        setupTitle.setGravity(Gravity.CENTER);
        setupTitle.setTypeface(null, 1);
        setupInfo.addView(setupTitle);

        LinearLayout features = new LinearLayout(this);
        features.setOrientation(LinearLayout.HORIZONTAL);
        features.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams featuresLp = new LinearLayout.LayoutParams(-1, -2);
        featuresLp.topMargin = dp(10);
        setupInfo.addView(features, featuresLp);

        features.addView(featureTile("Forum\nNotifications", R.drawable.fa_bell),
                new LinearLayout.LayoutParams(0, -2, 1.0f));
        features.addView(featureTile("Open Forum\nLinks in App", R.drawable.fa_globe),
                new LinearLayout.LayoutParams(0, -2, 1.0f));
        features.addView(featureTile("Secure App\nUpdates", R.drawable.fa_shield),
                new LinearLayout.LayoutParams(0, -2, 1.0f));
        features.addView(featureTile("Background\nAlert Health", R.drawable.fa_circle_info),
                new LinearLayout.LayoutParams(0, -2, 1.0f));

        LinearLayout notePanel = new LinearLayout(this);
        notePanel.setOrientation(LinearLayout.HORIZONTAL);
        notePanel.setGravity(Gravity.CENTER_VERTICAL);
        notePanel.setBackgroundResource(R.drawable.quick_action_background);
        notePanel.setPadding(dp(13), dp(10), dp(13), dp(10));
        LinearLayout.LayoutParams notePanelLp = new LinearLayout.LayoutParams(-1, -2);
        notePanelLp.topMargin = dp(10);
        page.addView(notePanel, notePanelLp);

        ImageView noteIcon = new ImageView(this);
        noteIcon.setImageResource(R.drawable.fa_circle_info);
        noteIcon.setImageTintList(ColorStateList.valueOf(getColor(R.color.hcf_cyan_bright)));
        noteIcon.setContentDescription(null);
        LinearLayout.LayoutParams noteIconLp = new LinearLayout.LayoutParams(dp(24), dp(24));
        noteIconLp.rightMargin = dp(10);
        notePanel.addView(noteIcon, noteIconLp);

        TextView note = text(
                "No worries — you can always open App Setup later from the app drawer → App Setup.",
                11,
                getColor(R.color.hcf_muted)
        );
        note.setLineSpacing(0.0f, 1.08f);
        notePanel.addView(note, new LinearLayout.LayoutParams(0, -2, 1.0f));

        Button startSetup = primaryButton("Start App Setup");
        startSetup.setOnClickListener(v -> startAppSetup());
        LinearLayout.LayoutParams startLp = new LinearLayout.LayoutParams(-1, dp(52));
        startLp.topMargin = dp(12);
        page.addView(startSetup, startLp);

        Button continueButton = secondaryButton("Continue Without App Setup");
        continueButton.setOnClickListener(v -> continueWithoutSetup("button"));
        LinearLayout.LayoutParams continueLp = new LinearLayout.LayoutParams(-1, dp(50));
        continueLp.topMargin = dp(9);
        page.addView(continueButton, continueLp);

        TextView footer = text(BuildInfo.VERSION_BUILD_LINE, 9, getColor(R.color.hcf_hint));
        footer.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams footerLp = new LinearLayout.LayoutParams(-1, -2);
        footerLp.topMargin = dp(14);
        page.addView(footer, footerLp);

        return scroll;
    }

    private View featureTile(String label, int iconRes) {
        LinearLayout tile = new LinearLayout(this);
        tile.setOrientation(LinearLayout.VERTICAL);
        tile.setGravity(Gravity.CENTER_HORIZONTAL);
        tile.setPadding(dp(3), dp(2), dp(3), dp(2));

        ImageView icon = new ImageView(this);
        icon.setImageResource(iconRes);
        icon.setImageTintList(ColorStateList.valueOf(getColor(R.color.hcf_cyan_bright)));
        icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        icon.setContentDescription(label.replace("\n", " "));
        tile.addView(icon, new LinearLayout.LayoutParams(dp(28), dp(28)));

        TextView labelView = text(label, 9, getColor(R.color.hcf_text));
        labelView.setGravity(Gravity.CENTER);
        labelView.setTypeface(null, 1);
        LinearLayout.LayoutParams labelLp = new LinearLayout.LayoutParams(-1, -2);
        labelLp.topMargin = dp(5);
        tile.addView(labelView, labelLp);
        return tile;
    }

    private void startAppSetup() {
        SetupCenter.markWelcomeSeen(this);
        AppLogger.info(this, "app_welcome", "start_setup_v" + SetupCenter.CURRENT_WELCOME_VERSION);
        Intent intent = new Intent(this, SetupActivity.class);
        intent.putExtra(SetupCenter.EXTRA_AUTO_LAUNCHED, true);
        startActivity(intent);
        finish();
    }

    private void continueWithoutSetup(String source) {
        SetupCenter.markWelcomeSeen(this);

        // Only create a skipped Setup Center state on a truly fresh install.
        // Existing beta users who already completed setup keep that completion.
        if (!prefs.getBoolean(AppPrefs.SETUP_SEEN, false)) {
            SetupCenter.markSkipped(this);
        }

        AppLogger.info(this, "app_welcome", "continue_without_setup_" + source
                + "_v" + SetupCenter.CURRENT_WELCOME_VERSION);
        finish();
    }

    private boolean isDevelopmentBuild() {
        return "dev".equalsIgnoreCase(BuildInfo.DEFAULT_UPDATE_CHANNEL)
                || "Dev".equalsIgnoreCase(BuildInfo.CHANNEL);
    }

    private TextView text(String value, int sp, int color) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(sp);
        view.setTextColor(color);
        view.setLineSpacing(0.0f, 1.06f);
        return view;
    }

    private Button primaryButton(String label) {
        Button button = new Button(this);
        UiButtons.normalizeText(button);
        button.setText(label);
        button.setTextSize(13.0f);
        button.setTextColor(getColor(R.color.hcf_on_accent));
        button.setBackgroundResource(R.drawable.error_primary_button_background);
        button.setGravity(Gravity.CENTER);
        button.setStateListAnimator(null);
        return button;
    }

    private Button secondaryButton(String label) {
        Button button = new Button(this);
        UiButtons.normalizeText(button);
        button.setText(label);
        button.setTextSize(12.0f);
        button.setTextColor(getColor(R.color.hcf_cyan_bright));
        button.setBackgroundResource(R.drawable.error_secondary_button_background);
        button.setGravity(Gravity.CENTER);
        button.setStateListAnimator(null);
        return button;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
