package com.multiwa.prototype;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.UserManager;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int REQ_PROVISION = 1001;
    private ComponentName admin;
    private DevicePolicyManager dpm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        admin = new ComponentName(this, AdminReceiver.class);
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        render();
    }

    @Override
    protected void onResume() {
        super.onResume();
        render();
    }

    private boolean isManagedProfile() {
        UserManager um = (UserManager) getSystemService(Context.USER_SERVICE);
        return um != null && um.isManagedProfile();
    }

    private void render() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(24), dp(36), dp(24), dp(24));
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setBackgroundColor(Color.WHITE);

        TextView title = text("MultiWA", 32, true);
        root.addView(title);

        TextView subtitle = text("Reklamsız • Root yok • Resmî WhatsApp değiştirilmez", 15, false);
        subtitle.setPadding(0, dp(8), 0, dp(28));
        root.addView(subtitle);

        if (isManagedProfile()) {
            TextView ok = text("✓ İkinci, izole alan aktif", 20, true);
            ok.setPadding(0, 0, 0, dp(16));
            root.addView(ok);

            TextView info = text(
                    "Bu alanın uygulama verileri kişisel profilden ayrıdır. İş profili Play Store içinden WhatsApp'ı kurup farklı numarayla etkinleştir.",
                    16, false);
            root.addView(info);

            Button settings = button("İş profili ayarlarını aç");
            settings.setOnClickListener(v -> {
                try {
                    startActivity(new Intent(Settings.ACTION_SYNC_SETTINGS));
                } catch (Exception e) {
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
            });
            root.addView(settings);
        } else {
            TextView info = text(
                    "Bu prototip Android'in resmi Managed Profile mekanizmasını kullanarak ikinci bağımsız uygulama alanı oluşturur. Telefon destekliyorsa aynı resmi WhatsApp ikinci kez kurulabilir.",
                    16, false);
            info.setPadding(0, 0, 0, dp(18));
            root.addView(info);

            Button create = button("2. WhatsApp alanını oluştur");
            create.setOnClickListener(v -> startProvisioning());
            root.addView(create);

            TextView note = text(
                    "Not: Android standart olarak bu yöntemle tek bir managed/work profile sunar. 5 ayrı kopya için sanallaştırma/container motoru gerekir.",
                    13, false);
            note.setPadding(0, dp(20), 0, 0);
            root.addView(note);
        }

        setContentView(root);
    }

    private void startProvisioning() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_MANAGED_USERS)) {
            Toast.makeText(this, "Bu cihaz Managed Profile özelliğini desteklemiyor.", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(DevicePolicyManager.ACTION_PROVISION_MANAGED_PROFILE);
        intent.putExtra(DevicePolicyManager.EXTRA_PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME, admin);
        intent.putExtra(DevicePolicyManager.EXTRA_PROVISIONING_SKIP_ENCRYPTION, true);

        if (intent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(this, "Cihaz üreticisi profil oluşturma ekranını kullanıma açmamış.", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            startActivityForResult(intent, REQ_PROVISION);
        } catch (Exception e) {
            Toast.makeText(this, "Profil oluşturma başlatılamadı: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private TextView text(String value, int sp, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(sp);
        t.setTextColor(Color.rgb(25, 25, 25));
        if (bold) t.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
        return t;
    }

    private Button button(String label) {
        Button b = new Button(this);
        b.setText(label);
        b.setAllCaps(false);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(56));
        p.setMargins(0, dp(18), 0, 0);
        b.setLayoutParams(p);
        return b;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
