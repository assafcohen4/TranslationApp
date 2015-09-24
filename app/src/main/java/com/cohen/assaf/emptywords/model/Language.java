package com.cohen.assaf.emptywords.model;

import java.io.Serializable;

public class Language implements Serializable{

        public int icon;
        public String title;
        public String initials;

        public Language(){
            super();
        }

        public Language(int icon, String title, String initials) {
            super();
            this.initials = initials;
            this.icon = icon;
            this.title = title;
        }

    @Override public String toString() {
        return title.toLowerCase();
    }
}
