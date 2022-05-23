package cn.astrodooog.idea.plugin.model;

import java.util.List;

public class TransferRes {
    private List<String> translation;
    private String query;

    private int errorCode;

    private List<Web> web;
    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }
    public List<String> getTranslation() {
        return translation;
    }

    public void setQuery(String query) {
        this.query = query;
    }
    public String getQuery() {
        return query;
    }

    public void setErrorcode(int errorCode) {
        this.errorCode = errorCode;
    }
    public int getErrorcode() {
        return errorCode;
    }

    public void setWeb(List<Web> web) {
        this.web = web;
    }
    public List<Web> getWeb() {
        return web;
    }

    public class Web {

        private List<String> value;
        private String key;
        public void setValue(List<String> value) {
            this.value = value;
        }
        public List<String> getValue() {
            return value;
        }

        public void setKey(String key) {
            this.key = key;
        }
        public String getKey() {
            return key;
        }

    }
}
