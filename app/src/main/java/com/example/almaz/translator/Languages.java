package com.example.almaz.translator;

public class Languages{

    private static String[] langsRU = {"Азербайджанский", "Албанский", "Амхарский", "Английский", "Арабский", "Армянский", "Африкаанс", "Баскский", "Башкирский", "Белорусский", "Бенгальский", "Болгарский", "Боснийский", "Валлийский", "Венгерский", "Вьетнамский", "Гаитянский (креольский)", "Галисийский", "Голландский", "Горномарийский", "Греческий", "Грузинский", "Гуджарати", "Датский", "Иврит", "Идиш", "Индонезийский", "Ирландский", "Итальянский", "Исландский", "Испанский", "Казахский", "Каннада", "Каталанский", "Киргизский", "Китайский", "Корейский", "Коса", "Латынь", "Латышский", "Литовский", "Люксембургский", "Малагасийский", "Малайский", "Малаялам", "Мальтийский", "Македонский", "Маори", "Маратхи", "Марийский", "Монгольский", "Немецкий", "Непальский", "Норвежский", "Панджаби", "Папьяменто", "Персидский", "Польский", "Португальский", "Румынский", "Русский", "Себуанский", "Сербский", "Сингальский", "Словацкий", "Словенский", "Суахили", "Сунданский", "Таджикский", "Тайский", "Тагальский", "Тамильский", "Татарский", "Телугу", "Турецкий", "Удмуртский", "Узбекский", "Украинский", "Урду", "Финский", "Французский", "Хинди", "Хорватский", "Чешский", "Шведский", "Шотландский", "Эстонский", "Эсперанто", "Яванский", "Японский"};
    private static String[] langCodesRU = {"az", "sq", "am", "en", "ar", "hy", "af", "eu", "ba", "be", "bn", "bg", "bs", "cy", "hu", "vi", "ht", "gl", "nl", "mrj", "el", "ka", "gu", "da", "he", "yi", "id", "ga", "it", "is", "es", "kk", "kn", "ca", "ky", "zh", "ko", "xh", "la", "lv", "lt", "lb", "mg", "ms", "ml", "mt", "mk", "mi", "mr", "mhr", "mn", "de", "ne", "no", "pa", "pap", "fa", "pl", "pt", "ro", "ru", "ceb", "sr", "si", "sk", "sl", "sw", "su", "tg", "th", "tl", "ta", "tt", "te", "tr", "udm", "uz", "uk", "ur", "fi", "fr", "hi", "hr", "cs", "sv", "gd", "et", "eo", "jv", "ja"};

    private static String[] langsEN = {"Afrikaans", "Albanian", "Amharic", "Arabic", "Armenian", "Azerbaijan", "Bashkir", "Basque", "Belarussian", "Bengali", "Bosnian", "Bulgarian", "Catalan", "Cebuano", "Chinese", "Croatian", "Czech", "Danish", "Dutch", "English", "Esperanto", "Estonian", "Finnish", "French", "Galician", "Georgian", "German", "Greek", "Gujarati", "Haitian", "Hebrew", "Hindi", "Hungarian", "Icelandic", "Indonesian", "Irish", "Italian", "Japanese", "Javanese", "Kannada", "Kazakh", "Korean", "Kyrgyz", "Latin", "Latvian", "Lithuanian", "Luxembourg", "Macedonian", "Malagasy", "Malay", "Malayalam", "Maltese", "Maori", "Marathi", "Mari", "Mari", "Mongolian", "Nepali", "Norwegian", "Papiamento", "Persian", "Polish", "Portuguese", "Punjabi", "Romanian", "Russian", "Scottish", "Serbian", "Sinhala", "Slovak", "Slovenian", "Spanish", "Spit", "Sundanese", "Swahili", "Swedish", "Tagalog", "Tajik", "Tamil", "Tatar", "Telugu", "Thai", "Turkish", "Udmurt", "Ukrainian", "Urdu", "Uzbek", "Vietnamese", "Welsh", "Yiddish"};
    private static String[] langCodesEN = {"af", "sq", "am", "ar", "hy", "az", "ba", "eu", "be", "bn", "bs", "bg", "ca", "ceb", "zh", "hr", "cs", "da", "nl", "en", "eo", "et", "fi", "fr", "gl", "ka", "de", "el", "gu", "ht", "he", "hi", "hu", "is", "id", "ga", "it", "ja", "jv", "kn", "kk", "ko", "ky", "la", "lv", "lt", "lb", "mk", "mg", "ms", "ml", "mt", "mi", "mr", "mhr", "mrj", "mn", "ne", "no", "pap", "fa", "pl", "pt", "pa", "ro", "ru", "gd", "sr", "si", "sk", "sl", "es", "xh", "su", "sw", "sv", "tl", "tg", "ta", "tt", "te", "th", "tr", "udm", "uk", "ur", "uz", "vi", "cy", "yi"};

    public static String[] getLangsRU(){
        return langsRU;
    }

    public static String[] getLangsEN(){
        return langsEN;
    }

    public static String getLangCodeRU(int i){
        return langCodesRU[i];
    }

	public static String getLangCodeEN(int i){
        return langCodesEN[i];
    }    
};
