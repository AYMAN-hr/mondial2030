package com.mondial.ticket.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Service de traduction pour le support multi-langue.
 */
public class LanguageService {

    private static LanguageService instance;
    private String currentLanguage = "FR";
    private Map<String, Map<String, String>> translations = new HashMap<>();

    private LanguageService() {
        initTranslations();
    }

    public static LanguageService getInstance() {
        if (instance == null) {
            instance = new LanguageService();
        }
        return instance;
    }

    private void initTranslations() {
        // French translations
        Map<String, String> fr = new HashMap<>();
        fr.put("welcome", "Bienvenue");
        fr.put("login", "Connexion");
        fr.put("logout", "Déconnexion");
        fr.put("username", "Nom d'utilisateur");
        fr.put("password", "Mot de passe");
        fr.put("tickets", "Tickets");
        fr.put("matches", "Matchs");
        fr.put("search", "Rechercher");
        fr.put("add", "Ajouter");
        fr.put("delete", "Supprimer");
        fr.put("buy", "Acheter");
        fr.put("price", "Prix");
        fr.put("category", "Catégorie");
        fr.put("status", "Statut");
        fr.put("available", "Disponible");
        fr.put("sold", "Vendu");
        fr.put("buyer", "Acheteur");
        fr.put("statistics", "Statistiques");
        fr.put("export", "Exporter");
        fr.put("settings", "Paramètres");
        fr.put("help", "Aide");
        fr.put("about", "À propos");
        fr.put("lottery", "Tirage au sort");
        fr.put("payment", "Paiement");
        fr.put("promo_codes", "Codes promo");
        fr.put("dark_mode", "Mode sombre");
        fr.put("language", "Langue");
        fr.put("notifications", "Notifications");
        fr.put("my_tickets", "Mes Tickets");
        fr.put("favorites", "Favoris");
        fr.put("refund", "Remboursement");
        fr.put("chat", "Chat Support");
        translations.put("FR", fr);

        // English translations
        Map<String, String> en = new HashMap<>();
        en.put("welcome", "Welcome");
        en.put("login", "Login");
        en.put("logout", "Logout");
        en.put("username", "Username");
        en.put("password", "Password");
        en.put("tickets", "Tickets");
        en.put("matches", "Matches");
        en.put("search", "Search");
        en.put("add", "Add");
        en.put("delete", "Delete");
        en.put("buy", "Buy");
        en.put("price", "Price");
        en.put("category", "Category");
        en.put("status", "Status");
        en.put("available", "Available");
        en.put("sold", "Sold");
        en.put("buyer", "Buyer");
        en.put("statistics", "Statistics");
        en.put("export", "Export");
        en.put("settings", "Settings");
        en.put("help", "Help");
        en.put("about", "About");
        en.put("lottery", "Lottery");
        en.put("payment", "Payment");
        en.put("promo_codes", "Promo Codes");
        en.put("dark_mode", "Dark Mode");
        en.put("language", "Language");
        en.put("notifications", "Notifications");
        en.put("my_tickets", "My Tickets");
        en.put("favorites", "Favorites");
        en.put("refund", "Refund");
        en.put("chat", "Chat Support");
        translations.put("EN", en);

        // Arabic translations
        Map<String, String> ar = new HashMap<>();
        ar.put("welcome", "مرحبا");
        ar.put("login", "تسجيل الدخول");
        ar.put("logout", "تسجيل الخروج");
        ar.put("username", "اسم المستخدم");
        ar.put("password", "كلمة المرور");
        ar.put("tickets", "التذاكر");
        ar.put("matches", "المباريات");
        ar.put("search", "بحث");
        ar.put("add", "إضافة");
        ar.put("delete", "حذف");
        ar.put("buy", "شراء");
        ar.put("price", "السعر");
        ar.put("category", "الفئة");
        ar.put("status", "الحالة");
        ar.put("available", "متاح");
        ar.put("sold", "مباع");
        ar.put("buyer", "المشتري");
        ar.put("statistics", "الإحصائيات");
        ar.put("export", "تصدير");
        ar.put("settings", "الإعدادات");
        ar.put("help", "مساعدة");
        ar.put("about", "حول");
        ar.put("lottery", "السحب");
        ar.put("payment", "الدفع");
        ar.put("promo_codes", "رموز الخصم");
        ar.put("dark_mode", "الوضع الداكن");
        ar.put("language", "اللغة");
        ar.put("notifications", "الإشعارات");
        ar.put("my_tickets", "تذاكري");
        ar.put("favorites", "المفضلة");
        ar.put("refund", "استرداد");
        ar.put("chat", "الدعم");
        translations.put("AR", ar);
    }

    public String get(String key) {
        Map<String, String> langMap = translations.get(currentLanguage);
        if (langMap != null && langMap.containsKey(key)) {
            return langMap.get(key);
        }
        return key; // Return key if translation not found
    }

    public void setLanguage(String lang) {
        if (translations.containsKey(lang)) {
            currentLanguage = lang;
        }
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public String[] getAvailableLanguages() {
        return new String[] {"FR", "EN", "AR"};
    }

    public String getLanguageName(String code) {
        switch (code) {
            case "FR": return "🇫🇷 Français";
            case "EN": return "🇬🇧 English";
            case "AR": return "🇲🇦 العربية";
            default: return code;
        }
    }
}

