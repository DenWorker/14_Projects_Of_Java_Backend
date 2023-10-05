package ru.Denis.JavaLibraryHibernateSpringJPA_Boot.util;

/*
     [
        {

        "detectedLanguage": {
        "language": "ru",
        "score": 1.0
        },

        "translations": [
        {
        "text": "Hello",
        "to": "en"
        }
        ]

        }
     ]
 */

import java.util.List;

public class TranslationResponse {
    private DetectedLanguage detectedLanguage;
    private List<Translation> translations;

    public DetectedLanguage getDetectedLanguage() {
        return detectedLanguage;
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    public static class DetectedLanguage {
        private String language;
        private double score;

        public String getLanguage() {
            return language;
        }

        public double getScore() {
            return score;
        }
    }

    public static class Translation {
        private String text;
        private String to;

        public String getText() {
            return text;
        }

        public String getTo() {
            return to;
        }
    }
}
