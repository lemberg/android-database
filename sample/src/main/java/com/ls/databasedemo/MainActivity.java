/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Lemberg Solutions
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.ls.databasedemo;

import com.ls.databasedemo.model.db.DatabaseManager;
import com.ls.databasedemo.model.db.entity.Contact;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private ArrayAdapter<Contact> contactAdapter;

    private AsyncTask<List<Contact>, Void, List<Contact>> generatorTask;
    private AsyncTask<Void, Void, List<Contact>> loaderTask;
    private AsyncTask<Void, Void, Void> clearTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);

        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();

        startLoaderTask();
    }

    @Override
    protected void onDestroy() {
        stopTask(generatorTask);
        stopTask(loaderTask);
        stopTask(clearTask);

        super.onDestroy();
    }

    private void initViews() {
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(contactAdapter);

        findViewById(R.id.generate).setOnClickListener(this);
        findViewById(R.id.clear).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.generate:
                startGeneratorTask();
                break;

            case R.id.clear:
                startClearTask();
                break;
        }
    }

    private void startGeneratorTask() {
        stopTask(generatorTask);

        generatorTask = new ContactGeneratorTask();
        generatorTask.execute(generateContacts());
    }

    private void startLoaderTask() {
        stopTask(loaderTask);

        loaderTask = new ContactLoaderTask();
        loaderTask.execute();
    }

    private void startClearTask() {
        stopTask(clearTask);

        clearTask = new ContactClearTask();
        clearTask.execute();
    }

    private void stopTask(AsyncTask task) {
        if (task != null) {
            task.cancel(false);
        }
    }

    private void showProgress() {
        findViewById(R.id.list).setVisibility(View.GONE);
        findViewById(R.id.progressHolder).setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        findViewById(R.id.list).setVisibility(View.VISIBLE);
        findViewById(R.id.progressHolder).setVisibility(View.GONE);
    }

    private void lockButtons() {
        findViewById(R.id.generate).setEnabled(false);
        findViewById(R.id.clear).setEnabled(false);
    }

    private void unlockButtons() {
        findViewById(R.id.generate).setEnabled(true);
        findViewById(R.id.clear).setEnabled(true);
    }

    private List<Contact> generateContacts() {
        List<Contact> contacts = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Contact contact = new Contact();
            contact.setFirstName("Test");
            contact.setLastName("User_" + (i + 1));
            contact.setEmail("test_user" + (i + 1) + "@test.ts");

            contacts.add(contact);
        }

        return contacts;
    }

    private class ContactGeneratorTask extends AsyncTask<List<Contact>, Void, List<Contact>> {

        @Override
        protected void onPreExecute() {
            showProgress();
            lockButtons();
        }

        @Override
        protected List<Contact> doInBackground(List<Contact>... list) {
            if (list.length != 0 ||  list[0] != null) {
                List<Contact> contacts = list[0];
                DatabaseManager.getInstance().addContacts(contacts);

                return contacts;
            }
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(List<Contact> list) {
            hideProgress();
            unlockButtons();

            for (Contact contact : list) {
                contactAdapter.add(contact);
            }
        }
    }

    private class ContactLoaderTask extends AsyncTask<Void, Void, List<Contact>> {

        @Override
        protected void onPreExecute() {
            showProgress();
            lockButtons();
        }

        @Override
        protected List<Contact> doInBackground(Void... params) {
            return DatabaseManager.getInstance().loadContacts();
        }

        @Override
        protected void onPostExecute(List<Contact> list) {
            hideProgress();
            unlockButtons();

            contactAdapter.clear();

            for (Contact contact : list) {
                contactAdapter.add(contact);
            }
        }
    }

    private class ContactClearTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            showProgress();
            lockButtons();
        }

        @Override
        protected Void doInBackground(Void... params) {
            DatabaseManager.getInstance().clearContacts();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            hideProgress();
            unlockButtons();

            contactAdapter.clear();
        }
    }
}
