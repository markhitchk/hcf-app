#!/usr/bin/env python3
from pathlib import Path
import sys

root = Path(sys.argv[1]) if len(sys.argv) > 1 else Path("source code")

checks = {
    root / "res/values-night-v8/colors.xml": [
        '<color name="hcf_bg">#0B0E12</color>',
        '<color name="hcf_cyan">#00B8F0</color>',
        '<color name="hcf_cyan_bright">#00FFFF</color>',
        '<color name="hcf_accent_text">#00D8E7</color>',
        '<color name="hcf_meta">#78D8E4</color>',
        '<color name="hcf_logo_yellow">#FDD22B</color>',
    ],
    root / "res/values/colors.xml": [
        '<color name="hcf_logo_yellow">#FDD22B</color>',
    ],
    root / "res/drawable/dev_badge_background.xml": [
        '@color/hcf_logo_yellow',
    ],
    root / "res/drawable/welcome_dev_badge_background.xml": [
        '@color/hcf_logo_yellow',
    ],
    root / "res/values/styles.xml": [
        'android:alertDialogTheme',
        'android:colorControlNormal',
        'android:colorControlActivated',
        'android:colorControlHighlight',
        'android:buttonStyle',
        'android:imageButtonStyle',
        'android:editTextStyle',
        'android:switchStyle',
        'android:checkboxStyle',
        'android:radioButtonStyle',
        'name="HcfSwitch"',
        '@color/hcf_switch_thumb',
        '@color/hcf_switch_track',
        '@color/hcf_choice_tint',
    ],
    root / "res/values-night-v8/styles.xml": [
        'android:alertDialogTheme',
        'android:colorControlActivated',
        'android:buttonStyle',
        'android:switchStyle',
        'name="HcfDialogButton"',
    ],
    root / "res/values-v31/styles.xml": [
        'android:alertDialogTheme',
        'android:buttonStyle',
        'android:switchStyle',
        'android:windowSplashScreenBackground',
        'android:forceDarkAllowed',
    ],
    root / "res/values-night-v31/styles.xml": [
        'android:alertDialogTheme',
        'android:buttonStyle',
        'android:switchStyle',
        'android:windowSplashScreenBackground',
        'android:forceDarkAllowed',
    ],
    root / "res/color/hcf_switch_thumb.xml": [
        '@color/hcf_cyan_bright',
    ],
    root / "res/color/hcf_switch_track.xml": [
        '@color/hcf_cyan',
    ],
    root / "res/color/hcf_choice_tint.xml": [
        '@color/hcf_cyan_bright',
    ],
    root / "src/com/harleytg/forum/ThemeManager.java": [
        'getString("app_theme", DARK)',
    ],
    root / "src/com/harleytg/forum/SetupCenter.java": [
        'CURRENT_WELCOME_VERSION = 2',
        'CURRENT_SETUP_VERSION = 2',
    ],
}

errors = []
for path, needles in checks.items():
    if not path.exists():
        errors.append(f"missing file: {path}")
        continue
    text = path.read_text(encoding="utf-8")
    for needle in needles:
        if needle not in text:
            errors.append(f"{path}: missing {needle}")

if errors:
    print("HCF theme UI verification: FAIL")
    for error in errors:
        print(" -", error)
    raise SystemExit(1)

print("HCF theme UI verification: PASS (legacy Beta black/cyan + logo-yellow native controls)")
