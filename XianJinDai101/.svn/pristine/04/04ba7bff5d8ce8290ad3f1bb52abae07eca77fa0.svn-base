package com.ryx.ryxcredit.beans.bussiness.addressbook;

import com.ryx.ryxcredit.beans.bussiness.CbaseRequest;

import java.util.List;

/**
 * Created by laomao on 16/12/13.
 */

public class ContactsRequest extends CbaseRequest{

    /**
     * name : test
     * phone_nums : ["123"," ","234",""]
     */

    private List<ContactsBean> contacts;

    public List<ContactsBean> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactsBean> contacts) {
        this.contacts = contacts;
    }

    public static class ContactsBean {
        private String name;
        private List<String> phone_nums;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getPhone_nums() {
            return phone_nums;
        }

        public void setPhone_nums(List<String> phone_nums) {
            this.phone_nums = phone_nums;
        }
    }
}
