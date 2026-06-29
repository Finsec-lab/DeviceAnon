package com.finsec.deviceanon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    static final String GITHUB_URL = "https://github.com/Finsec-lab";

    public static class Profile {
        String id, label, brand, manufacturer, model, name, device, board,
               fingerprint, security_patch, release, sdk, image;
    }

    private final List<Profile> profiles = new ArrayList<>();
    private int selectedPos = -1;
    private ProfileAdapter adapter;

    // pages + nav
    private View pageHome, pageDevices, pageAbout;
    private View navHome, navDevices, navAbout;
    private ImageView icHome, icDevices, icAbout;
    private TextView tlHome, tlDevices, tlAbout;

    // home identity views
    private ImageView homeBadgeBg, homeBadgeLogo, homePhone;
    private TextView homeBadgeMono, homeModel, homeVersion, homeFp, homeStatus;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        pageHome = findViewById(R.id.page_home);
        pageDevices = findViewById(R.id.page_devices);
        pageAbout = findViewById(R.id.page_about);
        navHome = findViewById(R.id.nav_home);
        navDevices = findViewById(R.id.nav_devices);
        navAbout = findViewById(R.id.nav_about);
        icHome = findViewById(R.id.ic_home);
        icDevices = findViewById(R.id.ic_devices);
        icAbout = findViewById(R.id.ic_about);
        tlHome = findViewById(R.id.tl_home);
        tlDevices = findViewById(R.id.tl_devices);
        tlAbout = findViewById(R.id.tl_about);

        homeBadgeBg = findViewById(R.id.home_badge_bg);
        homeBadgeLogo = findViewById(R.id.home_badge_logo);
        homePhone = findViewById(R.id.home_phone);
        homeBadgeMono = findViewById(R.id.home_badge_mono);
        homeModel = findViewById(R.id.home_model);
        homeVersion = findViewById(R.id.home_version);
        homeFp = findViewById(R.id.home_fp);
        homeStatus = findViewById(R.id.home_status);

        ListView list = findViewById(R.id.profiles);
        Button apply = findViewById(R.id.apply);
        Button revert = findViewById(R.id.revert);
        Button github = findViewById(R.id.github);
        TextView count = findViewById(R.id.count);

        loadProfiles();
        count.setText(profiles.size() + " profiles");

        adapter = new ProfileAdapter();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> p, View v, int pos, long id) {
                selectedPos = pos; adapter.notifyDataSetChanged();
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (selectedPos < 0) { toast("Pick a device first"); switchTab(1); return; }
                new ApplyTask().execute(profiles.get(selectedPos));
            }
        });
        revert.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { new RevertTask().execute(); }
        });
        github.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                try { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_URL))); }
                catch (Exception e) { toast("No browser"); }
            }
        });

        navHome.setOnClickListener(new View.OnClickListener() { public void onClick(View v) { switchTab(0); } });
        navDevices.setOnClickListener(new View.OnClickListener() { public void onClick(View v) { switchTab(1); } });
        navAbout.setOnClickListener(new View.OnClickListener() { public void onClick(View v) { switchTab(2); } });

        updateCurrent();
        switchTab(0);
    }

    private void switchTab(int i) {
        pageHome.setVisibility(i == 0 ? View.VISIBLE : View.GONE);
        pageDevices.setVisibility(i == 1 ? View.VISIBLE : View.GONE);
        pageAbout.setVisibility(i == 2 ? View.VISIBLE : View.GONE);

        int on = getResources().getColor(R.color.text_primary);
        int off = getResources().getColor(R.color.text_muted);
        tint(icHome, i == 0 ? on : off); tlHome.setTextColor(i == 0 ? on : off);
        tint(icDevices, i == 1 ? on : off); tlDevices.setTextColor(i == 1 ? on : off);
        tint(icAbout, i == 2 ? on : off); tlAbout.setTextColor(i == 2 ? on : off);
    }

    private static void tint(ImageView iv, int color) {
        if (iv.getDrawable() != null) iv.getDrawable().mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    // ---------- data ----------
    private void loadProfiles() {
        try {
            InputStream is = getAssets().open("profiles.json");
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line; while ((line = br.readLine()) != null) sb.append(line);
            br.close();
            String raw = sb.toString().trim();
            JSONArray arr = raw.startsWith("{") ? new JSONObject(raw).getJSONArray("profiles") : new JSONArray(raw);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                Profile p = new Profile();
                p.id = o.optString("id"); p.label = o.optString("label");
                p.brand = o.optString("brand"); p.manufacturer = o.optString("manufacturer");
                p.model = o.optString("model"); p.name = o.optString("name");
                p.device = o.optString("device"); p.board = o.optString("board");
                p.fingerprint = o.optString("fingerprint"); p.security_patch = o.optString("security_patch");
                p.release = o.optString("release"); p.sdk = o.optString("sdk");
                p.image = o.optString("image");
                profiles.add(p);
            }
        } catch (Exception ignored) {}
    }

    private void updateCurrent() {
        Profile cur = new Profile();
        cur.brand = Build.BRAND; cur.manufacturer = Build.MANUFACTURER; cur.model = Build.MODEL;
        cur.label = Build.MODEL; cur.name = Build.DEVICE;
        applyBadge(cur, homeBadgeBg, homeBadgeMono, homeBadgeLogo);
        setDeviceVisual(cur);
        homeModel.setText(Build.MANUFACTURER + " " + Build.MODEL);
        homeVersion.setText("Android " + Build.VERSION.RELEASE);
        homeFp.setText(Build.FINGERPRINT);
        homeStatus.setText(Build.MODEL);
    }

    /** pick a clean line-art render by form factor */
    private static int deviceDrawable(Profile p) {
        String s = ((p.label == null ? "" : p.label) + " " + (p.model == null ? "" : p.model)
                + " " + (p.name == null ? "" : p.name)).toLowerCase();
        if (s.contains("fold")) return R.drawable.ic_foldable;
        if (s.contains("flip")) return R.drawable.ic_flip;
        if (s.contains("tab") || s.contains("pad")) return R.drawable.ic_tablet;
        return R.drawable.ic_phone;
    }

    /** vector by default; if profile carries an image URL, load it with graceful fallback */
    private void setDeviceVisual(Profile p) {
        int fallback = deviceDrawable(p);
        homePhone.setImageResource(fallback);
        if (p.image != null && p.image.startsWith("http")) {
            new ImgTask(homePhone, fallback).execute(p.image);
        }
    }

    private static class ImgTask extends AsyncTask<String, Void, android.graphics.Bitmap> {
        private final ImageView target; private final int fallback;
        ImgTask(ImageView t, int fb) { target = t; fallback = fb; }
        protected android.graphics.Bitmap doInBackground(String... u) {
            try {
                java.net.HttpURLConnection c = (java.net.HttpURLConnection) new java.net.URL(u[0]).openConnection();
                c.setConnectTimeout(6000); c.setReadTimeout(6000); c.setInstanceFollowRedirects(true);
                InputStream in = c.getInputStream();
                android.graphics.Bitmap bm = android.graphics.BitmapFactory.decodeStream(in);
                in.close(); return bm;
            } catch (Exception e) { return null; }
        }
        protected void onPostExecute(android.graphics.Bitmap bm) {
            if (bm != null) target.setImageBitmap(bm); else target.setImageResource(fallback);
        }
    }

    private void toast(String s) { Toast.makeText(this, s, Toast.LENGTH_SHORT).show(); }

    // ---------- brand ----------
    private static class Badge { int colorRes; int logoRes; String mono; }

    private static Badge badgeFor(Profile p) {
        Badge b = new Badge();
        String brand = p.brand == null ? "" : p.brand.toLowerCase();
        String man = p.manufacturer == null ? "" : p.manufacturer.toLowerCase();
        if (brand.equals("google") || man.equals("google")) { b.colorRes = R.color.brand_google_bg; b.logoRes = R.drawable.ic_brand_google; }
        else if (brand.equals("samsung") || man.equals("samsung")) { b.colorRes = R.color.brand_samsung; b.logoRes = R.drawable.ic_brand_samsung; }
        else if (man.contains("lg")) { b.colorRes = R.color.brand_lg; b.logoRes = R.drawable.ic_brand_lg; }
        else if (man.contains("motorola")) { b.colorRes = R.color.brand_moto; b.logoRes = R.drawable.ic_brand_motorola; }
        else if (man.contains("huawei")) { b.colorRes = R.color.brand_huawei; b.logoRes = R.drawable.ic_brand_huawei; }
        else { b.colorRes = R.color.brand_generic; b.mono = (man.length() > 0 ? man.substring(0,1).toUpperCase() : "?"); }
        return b;
    }

    private void applyBadge(Profile p, ImageView bg, TextView mono, ImageView logo) {
        Badge b = badgeFor(p);
        if (bg.getDrawable() != null) bg.getDrawable().mutate().setColorFilter(getResources().getColor(b.colorRes), PorterDuff.Mode.SRC_IN);
        if (b.logoRes != 0) {
            logo.setVisibility(View.VISIBLE); mono.setVisibility(View.GONE);
            logo.setImageResource(b.logoRes);
        } else {
            logo.setVisibility(View.GONE); mono.setVisibility(View.VISIBLE); mono.setText(b.mono);
        }
    }

    // ---------- adapter ----------
    private class ProfileAdapter extends BaseAdapter {
        public int getCount() { return profiles.size(); }
        public Object getItem(int i) { return profiles.get(i); }
        public long getItemId(int i) { return i; }
        public View getView(int pos, View v, ViewGroup parent) {
            if (v == null) v = LayoutInflater.from(MainActivity.this).inflate(R.layout.row_profile, parent, false);
            Profile p = profiles.get(pos);
            ImageView badgeBg = v.findViewById(R.id.badge_bg);
            TextView badgeMono = v.findViewById(R.id.badge_mono);
            ImageView badgeLogo = v.findViewById(R.id.badge_logo);
            TextView model = v.findViewById(R.id.row_model);
            TextView codename = v.findViewById(R.id.row_codename);
            TextView chip = v.findViewById(R.id.row_chip);
            ImageView check = v.findViewById(R.id.row_check);
            View root = v.findViewById(R.id.row_root);

            model.setText(p.model);
            codename.setText(p.name + " · " + p.device);
            chip.setText("Android " + p.release);
            applyBadge(p, badgeBg, badgeMono, badgeLogo);

            boolean sel = pos == selectedPos;
            root.setSelected(sel);
            check.setVisibility(sel ? View.VISIBLE : View.INVISIBLE);
            return v;
        }
    }

    // ---------- apply / revert ----------
    private class ApplyTask extends AsyncTask<Profile, Void, String> {
        Profile p;
        protected String doInBackground(Profile... ps) {
            p = ps[0];
            StringBuilder c = new StringBuilder();
            // BOOTLOOP-SAFETY: no `set -e` at the top. We want failures to be
            // tolerated step-by-step so a single bad command never wedges the
            // device on next boot. Every operation ends with `|| true`.
            c.append("MOD=/data/adb/modules/deviceanon_props\n");
            c.append("MODUPD=/data/adb/modules_update/deviceanon_props\n");
            // Use modules_update/ which is the canonical staging location —
            // Magisk applies it atomically on next boot.
            c.append("mkdir -p \"$MOD\" \"$MODUPD\" 2>/dev/null || true\n");
            // Disable any conflicting older spoofers so they don't fight us.
            c.append("for m in a05s_props galaxy_props; do\n");
            c.append("  [ -d /data/adb/modules/$m ] && touch /data/adb/modules/$m/disable 2>/dev/null || true\n");
            c.append("done\n");

            // --- module.prop (escape user input) ---
            String label = sh(p.label);
            c.append("cat > \"$MOD/module.prop\" <<'EOF_MP' || true\n");
            c.append("id=deviceanon_props\n");
            c.append("name=DeviceAnon (").append(label).append(")\n");
            c.append("version=1.0\n");
            c.append("versionCode=2\n");
            c.append("author=FinSec\n");
            c.append("description=Spoof ").append(label).append("\n");
            c.append("minMagisk=20400\n");
            c.append("EOF_MP\n");

            // --- system.prop (read at boot by Magisk) ---
            c.append("cat > \"$MOD/system.prop\" <<'EOF_SP' || true\n");
            props(c, p);
            c.append("EOF_SP\n");

            // --- sepolicy.rule so resetprop can write vendor/odm props on locked SELinux ---
            c.append("cat > \"$MOD/sepolicy.rule\" <<'EOF_SE' || true\n");
            c.append("allow magisk default_prop file { write };\n");
            c.append("allow magisk vendor_default_prop file { write };\n");
            c.append("EOF_SE\n");

            // --- post-fs-data.sh — earliest safe point, runs before zygote ---
            // We DO NOT use `set -e` here. Every line tolerates failure.
            c.append("cat > \"$MOD/post-fs-data.sh\" <<'EOF_PFS' || true\n");
            c.append("#!/system/bin/sh\nMODDIR=${0%/*}\n");
            c.append("# Wait briefly for resetprop to be available\n");
            c.append("for i in 1 2 3 4 5; do\n");
            c.append("  command -v resetprop >/dev/null 2>&1 && break\n");
            c.append("  sleep 1\n");
            c.append("done\n");
            // Write all resetprop calls in safe form
            String fp = sh(p.fingerprint);
            appendResetprops(c, p, fp);
            c.append("exit 0\n");
            c.append("EOF_PFS\n");

            // --- service.sh — runs late_start, idempotent re-application ---
            c.append("cat > \"$MOD/service.sh\" <<'EOF_SVC' || true\n");
            c.append("#!/system/bin/sh\nMODDIR=${0%/*}\n");
            c.append("# Wait for boot completion\n");
            c.append("w=0\n");
            c.append("while [ \"$(getprop sys.boot_completed 2>/dev/null)\" != \"1\" ] && [ $w -lt 60 ]; do\n");
            c.append("  sleep 1; w=$((w+1))\n");
            c.append("done\n");
            c.append("sleep 2\n");
            appendResetprops(c, p, fp);
            // pif.json sync (only if installed)
            c.append("if [ -d /data/adb/modules/playintegrityfix ]; then\n");
            c.append("  cat > /data/adb/modules/playintegrityfix/pif.json <<'EOF_PIF'\n");
            c.append("{\n");
            c.append("  \"PRODUCT\": \"").append(sh(p.name)).append("\",\n");
            c.append("  \"DEVICE\": \"").append(sh(p.device)).append("\",\n");
            c.append("  \"MANUFACTURER\": \"").append(sh(p.manufacturer)).append("\",\n");
            c.append("  \"BRAND\": \"").append(sh(p.brand)).append("\",\n");
            c.append("  \"MODEL\": \"").append(sh(p.model)).append("\",\n");
            c.append("  \"FINGERPRINT\": \"").append(fp).append("\",\n");
            c.append("  \"SECURITY_PATCH\": \"").append(sh(p.security_patch)).append("\",\n");
            c.append("  \"DEVICE_INITIAL_SDK_INT\": \"").append(sh(p.sdk)).append("\"\n");
            c.append("}\nEOF_PIF\nfi\n");
            c.append("exit 0\n");
            c.append("EOF_SVC\n");

            c.append("chmod 0755 \"$MOD/post-fs-data.sh\" \"$MOD/service.sh\" 2>/dev/null || true\n");
            // Apply live (non-fatal: if it fails, next boot still picks it up)
            c.append("sh \"$MOD/service.sh\" >/dev/null 2>&1 &\n");
            // Verify module is present
            c.append("if [ -f \"$MOD/module.prop\" ] && [ -f \"$MOD/post-fs-data.sh\" ]; then\n");
            c.append("  echo OK\n");
            c.append("else\n");
            c.append("  echo MISSING_FILES\n");
            c.append("fi\n");
            return runRoot(c.toString());
        }
        protected void onPostExecute(String out) {
            applyBadge(p, homeBadgeBg, homeBadgeMono, homeBadgeLogo);
            setDeviceVisual(p);
            homeModel.setText(p.manufacturer + " " + p.model);
            homeVersion.setText("Android " + p.release);
            homeFp.setText(p.fingerprint);
            homeStatus.setText(p.model);
            switchTab(0);
            toast(out.contains("OK") ? "Applied " + p.model : "Failed — grant root");
        }
    }

    /** Append the live resetprop block. Tolerates errors. */
    private static void appendResetprops(StringBuilder c, Profile p, String fp) {
        rp(c, "ro.product.brand", p.brand);
        rp(c, "ro.product.manufacturer", p.manufacturer);
        rp(c, "ro.product.model", p.model);
        rp(c, "ro.product.name", p.name);
        rp(c, "ro.product.device", p.device);
        rp(c, "ro.product.board", p.board);
        c.append("for q in product system vendor odm system_ext; do\n");
        rpQ(c, "ro.product.\\$q.brand", p.brand);
        rpQ(c, "ro.product.\\$q.manufacturer", p.manufacturer);
        rpQ(c, "ro.product.\\$q.model", p.model);
        rpQ(c, "ro.product.\\$q.name", p.name);
        rpQ(c, "ro.product.\\$q.device", p.device);
        c.append("done\n");
        rp(c, "ro.build.product", p.device);
        rp(c, "ro.build.brand", p.brand);
        rp(c, "ro.build.manufacturer", p.manufacturer);
        rp(c, "ro.build.type", "user");
        rp(c, "ro.build.tags", "release-keys");
        for (String k : new String[]{
                "ro.build.fingerprint", "ro.system.build.fingerprint",
                "ro.vendor.build.fingerprint", "ro.product.build.fingerprint",
                "ro.odm.build.fingerprint", "ro.system_ext.build.fingerprint"}) {
            c.append("resetprop -n ").append(k).append(" \"").append(fp).append("\" 2>/dev/null || true\n");
        }
        rp(c, "ro.build.version.security_patch", p.security_patch);
        rp(c, "ro.build.version.release", p.release);
        rp(c, "ro.boot.verifiedbootstate", "green");
        rp(c, "ro.boot.flash.locked", "1");
        rp(c, "ro.boot.vbmeta.device_state", "locked");
        rp(c, "ro.boot.veritymode", "enforcing");
        rp(c, "ro.kernel.qemu", "0");
        rp(c, "ro.boot.qemu", "0");
    }

    /** Shell-escape: replace single quotes and dangerous metacharacters. */
    private static String sh(String s) {
        if (s == null) return "";
        return s.replace("\\", "")
                .replace("`", "")
                .replace("$", "")
                .replace("\"", "")
                .replace("\n", "")
                .replace("\r", "")
                .replace("'", "");
    }

    private class RevertTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... v) {
            // Disable any DeviceAnon-managed module on next boot. We don't try
            // to live-revert resetprop calls — that's a reboot's job and
            // forcing a system-prop rewrite at runtime is more likely to crash
            // services that have already cached values.
            return runRoot(
                "for m in deviceanon_props galaxy_props a05s_props; do\n" +
                "  [ -d /data/adb/modules/$m ] && touch /data/adb/modules/$m/disable 2>/dev/null || true\n" +
                "done\n" +
                "echo REVERTED\n"
            );
        }
        protected void onPostExecute(String out) { toast("Reverted — reboot to reset"); }
    }

    private static void props(StringBuilder sb, Profile p) {
        kv(sb,"ro.product.brand",p.brand); kv(sb,"ro.product.manufacturer",p.manufacturer);
        kv(sb,"ro.product.model",p.model); kv(sb,"ro.product.name",p.name);
        kv(sb,"ro.product.device",p.device); kv(sb,"ro.product.board",p.board);
        for (String q : new String[]{"product","system","vendor","odm","system_ext"}) {
            kv(sb,"ro.product."+q+".brand",p.brand); kv(sb,"ro.product."+q+".manufacturer",p.manufacturer);
            kv(sb,"ro.product."+q+".model",p.model); kv(sb,"ro.product."+q+".name",p.name); kv(sb,"ro.product."+q+".device",p.device);
        }
        kv(sb,"ro.build.product",p.device); kv(sb,"ro.build.brand",p.brand); kv(sb,"ro.build.manufacturer",p.manufacturer);
        for (String k : new String[]{"ro.build.fingerprint","ro.system.build.fingerprint","ro.vendor.build.fingerprint","ro.product.build.fingerprint","ro.odm.build.fingerprint","ro.system_ext.build.fingerprint"})
            kv(sb,k,p.fingerprint);
        kv(sb,"ro.build.version.security_patch",p.security_patch); kv(sb,"ro.build.version.release",p.release);
        kv(sb,"ro.boot.verifiedbootstate","green"); kv(sb,"ro.boot.flash.locked","1");
        kv(sb,"ro.boot.vbmeta.device_state","locked"); kv(sb,"ro.boot.veritymode","enforcing");
    }
    private static void kv(StringBuilder sb,String k,String v){ sb.append(k).append("=").append(sh(v)).append("\n"); }
    /** Resetprop with safe quoting + tolerated failure. */
    private static void rp(StringBuilder sb,String k,String v){
        sb.append("resetprop -n ").append(k).append(" '").append(sh(v)).append("' 2>/dev/null || true\n");
    }
    /** Resetprop where the key contains a shell variable (already escaped); tolerated failure. */
    private static void rpQ(StringBuilder sb,String k,String v){
        sb.append(" resetprop -n ").append(k).append(" '").append(sh(v)).append("' 2>/dev/null || true\n");
    }

    private static String runRoot(String script) {
        // Try a few su binary locations — on Samsung One UI 7+ the canonical
        // location is /product/bin/su. Some KernelSU forks ship at /system/bin
        // only.
        String[] candidates = {"su", "/product/bin/su", "/system/bin/su", "/sbin/su"};
        for (String su : candidates) {
            try {
                Process p = Runtime.getRuntime().exec(new String[]{su, "-c", "sh"});
                DataOutputStream dos = new DataOutputStream(p.getOutputStream());
                dos.writeBytes(script);
                dos.writeBytes("\nexit 0\n");
                dos.flush();
                dos.close();
                StringBuilder out = new StringBuilder();
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = r.readLine()) != null) out.append(line).append(" ");
                p.waitFor();
                if (p.exitValue() == 0 || out.length() > 0) {
                    return out.toString();
                }
            } catch (Exception ignored) {
                // try next candidate
            }
        }
        return "su failed";
    }
}
