package com.vente_en_ligne.plateforme_e_commerce.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * Service de traduction pour le support multilingue Langues supportées:
 * Français, Wolof, Poulard, Arabe
 */
@Service
public class TranslationService {

    private static final Map<String, Map<String, String>> translations = new HashMap<>();

    static {
        // === FRANÇAIS ===
        Map<String, String> french = new HashMap<>();
        french.put("welcome", "Bienvenue sur ShopLine");
        french.put("home", "Accueil");
        french.put("products", "Produits");
        french.put("categories", "Catégories");
        french.put("cart", "Panier");
        french.put("checkout", "Commander");
        french.put("login", "Connexion");
        french.put("register", "S'inscrire");
        french.put("logout", "Déconnexion");
        french.put("search", "Rechercher");
        french.put("search_placeholder", "Rechercher un produit...");
        french.put("add_to_cart", "Ajouter au panier");
        french.put("remove_from_cart", "Retirer du panier");
        french.put("quantity", "Quantité");
        french.put("price", "Prix");
        french.put("total", "Total");
        french.put("subtotal", "Sous-total");
        french.put("shipping", "Livraison");
        french.put("free_shipping", "Livraison gratuite");
        french.put("tax", "TVA");
        french.put("order_summary", "Résumé de la commande");
        french.put("customer_info", "Informations client");
        french.put("name", "Nom complet");
        french.put("email", "Email");
        french.put("phone", "Téléphone");
        french.put("address", "Adresse");
        french.put("city", "Ville");
        french.put("postal_code", "Code postal");
        french.put("confirm_order", "Confirmer la commande");
        french.put("order_confirmed", "Commande confirmée");
        french.put("order_confirmation", "Merci pour votre commande !");
        french.put("order_details", "Détails de votre commande");
        french.put("order_id", "Numéro de commande");
        french.put("empty_cart", "Votre panier est vide");
        french.put("continue_shopping", "Continuer vos achats");
        french.put("view_cart", "Voir le panier");
        french.put("featured_products", "Produits vedettes");
        french.put("new_arrivals", "Nouveautés");
        french.put("all_products", "Tous les produits");
        french.put("product_details", "Détails du produit");
        french.put("description", "Description");
        french.put("contact", "Contact");
        french.put("about", "À propos");
        french.put("faq", "FAQ");
        french.put("terms", "Conditions générales");
        french.put("privacy", "Politique de confidentialité");
        french.put("payment", "Paiement");
        french.put("select_payment", "Choisir le mode de paiement");
        french.put("wave", "Wave (Sénégal/Gambie)");
        french.put("orange_money", "Orange Money");
        french.put("free_money", "Free Money");
        french.put("cash_on_delivery", "Paiement à la livraison");
        french.put("pay_now", "Payer maintenant");
        french.put("payment_success", "Paiement réussi");
        french.put("payment_failed", "Paiement échoué");
        french.put("payment_pending", "Paiement en attente");
        french.put("try_again", "Réessayer");
        french.put("admin", "Administration");
        french.put("manage_products", "Gérer les produits");
        french.put("manage_orders", "Gérer les commandes");
        french.put("manage_categories", "Gérer les catégories");
        french.put("add_product", "Ajouter un produit");
        french.put("edit_product", "Modifier le produit");
        french.put("delete_product", "Supprimer le produit");
        french.put("save", "Enregistrer");
        french.put("cancel", "Annuler");
        french.put("delete", "Supprimer");
        french.put("edit", "Modifier");
        french.put("confirm", "Confirmer");
        french.put("success", "Succès");
        french.put("error", "Erreur");
        french.put("warning", "Attention");
        french.put("info", "Information");
        french.put("loading", "Chargement...");
        french.put("no_products", "Aucun produit trouvé");
        french.put("category_valises", "Valises");
        french.put("category_chaussures", "Chaussures");
        french.put("category_vetements", "Vêtements");
        french.put("category_bassins", "Bassins");
        french.put("category_rideaux", "Rideaux");
        french.put("category_accessoires", "Accessoires");
        french.put("category_other", "Autres produits");
        french.put("valise_ivs", "Valise IVS");
        french.put("valise_original", "Valise Original");
        french.put("valise_petit", "Valise petit modèle");
        french.put("valise_grand", "Valise grand modèle");
        french.put("vetements_grande_taille", "Vêtements grande taille");
        french.put("vetements_petite_taille", "Vêtements petite taille");
        french.put("order_placed", "Commande passée");
        french.put("order_processing", "Commande en cours de traitement");
        french.put("order_shipped", "Commande expédiée");
        french.put("order_delivered", "Commande livrée");
        french.put("order_cancelled", "Commande annulée");
        french.put("new_order_received", "Nouvelle commande reçue");
        french.put("contact_customer", "Contacter le client");
        french.put("select_language", "Choisir la langue");
        translations.put("fr", french);

        // === WOLOF ===
        Map<String, String> wolof = new HashMap<>();
        wolof.put("welcome", "Baal ma nu ShopLine");
        wolof.put("home", "Kaa");
        wolof.put("products", "Lii");
        wolof.put("categories", "Tab");
        wolof.put("cart", "Kaay");
        wolof.put("checkout", "Sopp");
        wolof.put("login", "Tapp");
        wolof.put("register", "Mbooloo");
        wolof.put("logout", "Tappout");
        wolof.put("search", "Wutte");
        wolof.put("search_placeholder", "Wutte lii...");
        wolof.put("add_to_cart", "Ngi add ci kaay");
        wolof.put("remove_from_cart", "Dipp ci kaay");
        wolof.put("quantity", "Tonn");
        wolof.put("price", "Nett");
        wolof.put("total", "Yok");
        wolof.put("subtotal", "Nett ji");
        wolof.put("shipping", "Taler");
        wolof.put("free_shipping", "Taler la");
        wolof.put("tax", "Taxe");
        wolof.put("order_summary", "Ajami commande");
        wolof.put("customer_info", "Xam-xam klien");
        wolof.put("name", "Tur");
        wolof.put("email", "Email");
        wolof.put("phone", "Telefon");
        wolof.put("address", "Adres");
        wolof.put("city", "Vil");
        wolof.put("postal_code", "Kod postal");
        wolof.put("confirm_order", "Deflo commande bi");
        wolof.put("order_confirmed", "Commande deflo");
        wolof.put("order_confirmation", "Mero ci commande gi!");
        wolof.put("order_details", "Ajami commande gi");
        wolof.put("order_id", "Nomero commande");
        wolof.put("empty_cart", "Kaay bi la");
        wolof.put("continue_shopping", "Dank am");
        wolof.put("view_cart", "Kaay");
        wolof.put("featured_products", "Lii gen a");
        wolof.put("new_arrivals", "Lii new");
        wolof.put("all_products", "Yeneeni lii");
        wolof.put("product_details", "Ajami lii");
        wolof.put("description", "Ndigel");
        wolof.put("contact", "Ndimbal");
        wolof.put("about", "Ci aw");
        wolof.put("faq", "FAQ");
        wolof.put("terms", "Ndigal");
        wolof.put("privacy", "Niyel");
        wolof.put("payment", "Tale");
        wolof.put("select_payment", "Talo");
        wolof.put("wave", "Wave");
        wolof.put("orange_money", "Orange Money");
        wolof.put("free_money", "Free Money");
        wolof.put("cash_on_delivery", "Tale ci seen");
        wolof.put("pay_now", "Talele");
        wolof.put("payment_success", "Tale gena");
        wolof.put("payment_failed", "Tale man");
        wolof.put("payment_pending", "Tale ci");
        wolof.put("try_again", "Teyyin");
        wolof.put("admin", "Ndimbal");
        wolof.put("manage_products", "Nangay lii");
        wolof.put("manage_orders", "Nangay commande");
        wolof.put("manage_categories", "Nangay tab");
        wolof.put("add_product", "Amar lii");
        wolof.put("edit_product", "Hejjel lii");
        wolof.put("delete_product", "Supprime lii");
        wolof.put("save", "Eb");
        wolof.put("cancel", "Anul");
        wolof.put("delete", "Supprime");
        wolof.put("edit", "Hejjel");
        wolof.put("confirm", "Deflo");
        wolof.put("success", "Succes");
        wolof.put("error", "Misse");
        wolof.put("warning", "Nettet");
        wolof.put("info", "Info");
        wolof.put("loading", "Yeb...");
        wolof.put("no_products", "Ay lii");
        wolof.put("category_valises", "Valiz");
        wolof.put("category_chaussures", "Sapat");
        wolof.put("category_vetements", "Yer");
        wolof.put("category_bassins", "Basen");
        wolof.put("category_rideaux", "Rido");
        wolof.put("category_accessoires", "Akseswar");
        wolof.put("category_other", "Yeneeni");
        wolof.put("valise_ivs", "Valiz IVS");
        wolof.put("valise_original", "Valiz Original");
        wolof.put("valise_petit", "Valiz petit");
        wolof.put("valise_grand", "Valiz grand");
        wolof.put("vetements_grande_taille", "Yer grand");
        wolof.put("vetements_petite_taille", "Yer petit");
        wolof.put("order_placed", "Commande deflo");
        wolof.put("order_processing", "Commande tii");
        wolof.put("order_shipped", "Commande expedi");
        wolof.put("order_delivered", "Commande wrakk");
        wolof.put("order_cancelled", "Commande anul");
        wolof.put("new_order_received", "Commande faran");
        wolof.put("contact_customer", "Klien");
        wolof.put("select_language", "Tapp language");
        translations.put("wo", wolof);

        // === POULARD / PULAAR ===
        Map<String, String> pulaar = new HashMap<>();
        pulaar.put("welcome", "A soomaama ShopLine");
        pulaar.put("home", "Himo");
        pulaar.put("products", "Kou");
        pulaar.put("categories", "Wallo");
        pulaar.put("cart", "Kaw");
        pulaar.put("checkout", "Haude");
        pulaar.put("login", "Sagata");
        pulaar.put("register", "Huugo");
        pulaar.put("logout", "Sagataa");
        pulaar.put("search", "Heddii");
        pulaar.put("search_placeholder", "Heddii kou...");
        pulaar.put("add_to_cart", "E wooda");
        pulaar.put("remove_from_cart", "Fillaa");
        pulaar.put("quantity", "Nabala");
        pulaar.put("price", "Hunel");
        pulaar.put("total", "Sah");
        pulaar.put("subtotal", "Sah les");
        pulaar.put("shipping", "Patti");
        pulaar.put("free_shipping", "Patti mo");
        pulaar.put("tax", "Tax");
        pulaar.put("order_summary", "Ko tiffa");
        pulaar.put("customer_info", "Ko oo");
        pulaar.put("name", "Sukkarbe");
        pulaar.put("email", "Email");
        pulaar.put("phone", "Tifno");
        pulaar.put("address", "Yeesal");
        pulaar.put("city", "Wuro");
        pulaar.put("postal_code", "Kod post");
        pulaar.put("confirm_order", "Yerdu tiffa");
        pulaar.put("order_confirmed", "Tiffa yerdaa");
        pulaar.put("order_confirmation", "Jaa!");
        pulaar.put("order_details", "Ko tiffa");
        pulaar.put("order_id", "Nomero tiffa");
        pulaar.put("empty_cart", "Kaw baa");
        pulaar.put("continue_shopping", "A woodi");
        pulaar.put("view_cart", "Kaw");
        pulaar.put("featured_products", "Kou uwo");
        pulaar.put("new_arrivals", "Kou ammaa");
        pulaar.put("all_products", "Kou mon");
        pulaar.put("product_details", "Ko kou");
        pulaar.put("description", "Wiyete");
        pulaar.put("contact", "Wiirtiinde");
        pulaar.put("about", "Haa");
        pulaar.put("faq", "FAQ");
        pulaar.put("terms", "Law");
        pulaar.put("privacy", "Sikka");
        pulaar.put("payment", "Yelte");
        pulaar.put("select_payment", "Yelta");
        pulaar.put("wave", "Wave");
        pulaar.put("orange_money", "Orange Money");
        pulaar.put("free_money", "Free Money");
        pulaar.put("cash_on_delivery", "Yel kanko");
        pulaar.put("pay_now", "Yel ndeen");
        pulaar.put("payment_success", "Yel foti");
        pulaar.put("payment_failed", "Yel potii");
        pulaar.put("payment_pending", "Yel haa");
        pulaar.put("try_again", "Kadi");
        pulaar.put("admin", "Wootere");
        pulaar.put("manage_products", "Tawon kou");
        pulaar.put("manage_orders", "Tawon tiffa");
        pulaar.put("manage_categories", "Tawon wallo");
        pulaar.put("add_product", "Wooda kou");
        pulaar.put("edit_product", "Welti kou");
        pulaar.put("delete_product", "Haddi kou");
        pulaar.put("save", "Mbaa");
        pulaar.put("cancel", "Finti");
        pulaar.put("delete", "Haddi");
        pulaar.put("edit", "Welti");
        pulaar.put("confirm", "Yerdu");
        pulaar.put("success", "Foti");
        pulaar.put("error", "Potii");
        pulaar.put("warning", "Hollataa");
        pulaar.put("info", "Info");
        pulaar.put("loading", "Kadi...");
        pulaar.put("no_products", "Kou ambe");
        pulaar.put("category_valises", "Valiz");
        pulaar.put("category_chaussures", "Sapat");
        pulaar.put("category_vetements", "Yer");
        pulaar.put("category_bassins", "Basen");
        pulaar.put("category_rideaux", "Rido");
        pulaar.put("category_accessoires", "Akseswar");
        pulaar.put("category_other", "Kou mon");
        pulaar.put("valise_ivs", "Valiz IVS");
        pulaar.put("valise_original", "Valiz Original");
        pulaar.put("valise_petit", "Valiz petit");
        pulaar.put("valise_grand", "Valiz grand");
        pulaar.put("vetements_grande_taille", "Yer grand");
        pulaar.put("vetements_petite_taille", "Yer petit");
        pulaar.put("order_placed", "Tiffa wooda");
        pulaar.put("order_processing", "Tiffa haa");
        pulaar.put("order_shipped", "Tiffa wari");
        pulaar.put("order_delivered", "Tiffa hulni");
        pulaar.put("order_cancelled", "Tiffa finti");
        pulaar.put("new_order_received", "Tiffa amen");
        pulaar.put("contact_customer", "Oon wiirti");
        pulaar.put("select_language", "Sagata");
        translations.put("ff", pulaar);

        // === ARABE ===
        Map<String, String> arabic = new HashMap<>();
        arabic.put("welcome", "أهلا وسهلا في ShopLine");
        arabic.put("home", "الرئيسية");
        arabic.put("products", "المنتجات");
        arabic.put("categories", "الفئات");
        arabic.put("cart", "السلة");
        arabic.put("checkout", "اطلب الآن");
        arabic.put("login", "تسجيل الدخول");
        arabic.put("register", "إنشاء حساب");
        arabic.put("logout", "تسجيل الخروج");
        arabic.put("search", "بحث");
        arabic.put("search_placeholder", "ابحث عن منتج...");
        arabic.put("add_to_cart", "أضف للسلة");
        arabic.put("remove_from_cart", "أزل من السلة");
        arabic.put("quantity", "الكمية");
        arabic.put("price", "السعر");
        arabic.put("total", "الإجمالي");
        arabic.put("subtotal", "المجموع");
        arabic.put("shipping", "التوصيل");
        arabic.put("free_shipping", "توصيل مجاني");
        arabic.put("tax", "الضريبة");
        arabic.put("order_summary", "ملخص الطلب");
        arabic.put("customer_info", "معلومات العميل");
        arabic.put("name", "الاسم الكامل");
        arabic.put("email", "البريد الإلكتروني");
        arabic.put("phone", "الهاتف");
        arabic.put("address", "العنوان");
        arabic.put("city", "المدينة");
        arabic.put("postal_code", "الرمز البريدي");
        arabic.put("confirm_order", "تأكيد الطلب");
        arabic.put("order_confirmed", "تم تأكيد الطلب");
        arabic.put("order_confirmation", "شكراً لطلبك!");
        arabic.put("order_details", "تفاصيل طلبك");
        arabic.put("order_id", "رقم الطلب");
        arabic.put("empty_cart", "سلتك فارغة");
        arabic.put("continue_shopping", "متابعة التسوق");
        arabic.put("view_cart", "عرض السلة");
        arabic.put("featured_products", "منتجات مميزة");
        arabic.put("new_arrivals", "جديدنا");
        arabic.put("all_products", "جميع المنتجات");
        arabic.put("product_details", "تفاصيل المنتج");
        arabic.put("description", "الوصف");
        arabic.put("contact", "اتصل بنا");
        arabic.put("about", "من نحن");
        arabic.put("faq", "الأسئلة الشائعة");
        arabic.put("terms", "الشروط والأحكام");
        arabic.put("privacy", "سياسة الخصوصية");
        arabic.put("payment", "الدفع");
        arabic.put("select_payment", "اختر طريقة الدفع");
        arabic.put("wave", "Wave");
        arabic.put("orange_money", "أورانج موني");
        arabic.put("free_money", "فري موني");
        arabic.put("cash_on_delivery", "الدفع عند الاستلام");
        arabic.put("pay_now", "ادفع الآن");
        arabic.put("payment_success", "تم الدفع بنجاح");
        arabic.put("payment_failed", "فشل الدفع");
        arabic.put("payment_pending", "في انتظار الدفع");
        arabic.put("try_again", "حاول مرة أخرى");
        arabic.put("admin", "الإدارة");
        arabic.put("manage_products", "إدارة المنتجات");
        arabic.put("manage_orders", "إدارة الطلبات");
        arabic.put("manage_categories", "إدارة الفئات");
        arabic.put("add_product", "إضافة منتج");
        arabic.put("edit_product", "تعديل المنتج");
        arabic.put("delete_product", "حذف المنتج");
        arabic.put("save", "حفظ");
        arabic.put("cancel", "إلغاء");
        arabic.put("delete", "حذف");
        arabic.put("edit", "تعديل");
        arabic.put("confirm", "تأكيد");
        arabic.put("success", "نجاح");
        arabic.put("error", "خطأ");
        arabic.put("warning", "تحذير");
        arabic.put("info", "معلومات");
        arabic.put("loading", "جاري التحميل...");
        arabic.put("no_products", "لا توجد منتجات");
        arabic.put("category_valises", "حقيبة سفر");
        arabic.put("category_chaussures", "أحذية");
        arabic.put("category_vetements", "ملابس");
        arabic.put("category_bassins", "أحواض");
        arabic.put("category_rideaux", "ستائر");
        arabic.put("category_accessoires", "إكسسوارات");
        arabic.put("category_other", "منتجات أخرى");
        arabic.put("valise_ivs", "حقيبة IVS");
        arabic.put("valise_original", "حقيبة أصلية");
        arabic.put("valise_petit", "حقيبة صغيرة");
        arabic.put("valise_grand", "حقيبة كبيرة");
        arabic.put("vetements_grande_taille", "ملابس كبيرة");
        arabic.put("vetements_petite_taille", "ملابس صغيرة");
        arabic.put("order_placed", "تم تقديم الطلب");
        arabic.put("order_processing", "قيد المعالجة");
        arabic.put("order_shipped", "تم الشحن");
        arabic.put("order_delivered", "تم التسليم");
        arabic.put("order_cancelled", "تم الإلغاء");
        arabic.put("new_order_received", "طلب جديد");
        arabic.put("contact_customer", "تواصل مع العميل");
        arabic.put("select_language", "اختر اللغة");
        translations.put("ar", arabic);
    }

    private String currentLanguage = "fr";

    public void setLanguage(String languageCode) {
        if (translations.containsKey(languageCode)) {
            this.currentLanguage = languageCode;
        }
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public String translate(String key) {
        Map<String, String> langMap = translations.get(currentLanguage);
        if (langMap != null && langMap.containsKey(key)) {
            return langMap.get(key);
        }
        // Fallback to French
        Map<String, String> frenchMap = translations.get("fr");
        return frenchMap != null ? frenchMap.getOrDefault(key, key) : key;
    }

    public String translate(String key, String languageCode) {
        Map<String, String> langMap = translations.get(languageCode);
        if (langMap != null && langMap.containsKey(key)) {
            return langMap.get(key);
        }
        return translate(key);
    }

    public Map<String, String> getAllTranslations() {
        return translations.getOrDefault(currentLanguage, translations.get("fr"));
    }

    public Map<String, String> getAllTranslations(String languageCode) {
        return translations.getOrDefault(languageCode, translations.get("fr"));
    }

    public static Map<String, String> getSupportedLanguages() {
        Map<String, String> languages = new HashMap<>();
        languages.put("fr", "Français");
        languages.put("wo", "Wolof");
        languages.put("ff", "Poulard");
        languages.put("ar", "العربية");
        return languages;
    }
}
