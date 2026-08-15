# MultiWA Prototype v0.1

Amaç: WhatsApp APK'sını modlamadan, Android'in Managed Profile / Work Profile özelliğini kullanarak ikinci bağımsız uygulama alanı oluşturmak.

## Ne yapar?
- Reklam yok
- Üyelik/abonelik yok
- Root istemez
- Resmi WhatsApp APK'sını değiştirmez
- Android'in profil izolasyonunu kullanır

## Sınır
Bu sürüm 5 WhatsApp klonu değildir. Standart Managed Profile yaklaşımı bir ek izole profil sağlar. 5+ kopya için ayrı bir uygulama sanallaştırma/container motoru gerekir.

## Derleme
1. Android Studio'da klasörü açın.
2. Gerekirse SDK 35'i yükleyin.
3. Build > Build APK(s) seçin.
4. `app/build/outputs/apk/debug/app-debug.apk` oluşur.

Komut satırında Gradle wrapper eklediyseniz:
`./gradlew assembleDebug`

## Test
Cihazda daha önce iş profili (Company Portal, Shelter, Island vb.) varsa Android yeni bir managed profile oluşturmayı reddedebilir. Önce mevcut iş profilini kaldırmak gerekebilir.
