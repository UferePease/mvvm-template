package com.duyp.architecture.mvvm.test_utils;//package com.duyp.architecture.mvvm.utils;

import android.support.annotation.Nullable;

import com.duyp.androidutils.realm.LiveRealmResultPair;
import com.duyp.androidutils.realm.LiveRealmResults;

import org.powermock.api.mockito.PowerMockito;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by duypham on 10/17/17.
 *
 */

public class RealmTestUtils {
    public static <T extends RealmObject> RealmQuery<T> initRealmQuery(Realm realm, Class<T> tClass) {
        RealmQuery<T> query = mockRealmQuery();
        when(realm.where(tClass)).thenReturn(query);
        when(query.equalTo(anyString(), anyLong())).thenReturn(query);
        return query;
    }

    public static <T extends RealmObject> RealmResults<T> initFindAllSorted(RealmQuery<T> query) throws Exception {
        return initFindAllSorted(query, null);
    }

    public static <T extends RealmObject> RealmResults<T> initFindAllSorted(RealmQuery<T> query, @Nullable List<T> returnValue) throws Exception {
        RealmResults<T> realmResults = mockRealmResults();
        initLiveRealmResults(realmResults);
        when(query.findAll()).thenReturn(realmResults);
        when(query.findAllSorted(anyString(), any())).thenReturn(realmResults);

        if (returnValue != null) {
            // The for(...) loop in Java needs an iterator, so we're giving it one that has items,
            // since the mock RealmResults does not provide an implementation. Therefore, anytime
            // anyone asks for the RealmResults Iterator, give them a functioning iterator from the
            // ArrayList of Persons we created above. This will allow the loop to execute.
            when(realmResults.size()).thenReturn(returnValue.size());
            when(realmResults.iterator()).thenReturn(returnValue.iterator());
        }
        return realmResults;
    }

    public static <T extends RealmObject> LiveRealmResults<T> initLiveRealmResults(RealmResults<T> results) throws Exception {
        // noinspection unchecked
        LiveRealmResultPair<T> pair = mock(LiveRealmResultPair.class);
        PowerMockito.whenNew(LiveRealmResultPair.class).withAnyArguments().thenReturn(pair);
        when(pair.getData()).thenReturn(results);
        return LiveRealmResults.asLiveData(results);
    }

    @SuppressWarnings("unchecked")
    public  static <T extends RealmObject> RealmQuery<T> mockRealmQuery() {
        return mock(RealmQuery.class);
    }

    @SuppressWarnings("unchecked")
    public static <T extends RealmObject> RealmResults<T> mockRealmResults() {
        return mock(RealmResults.class);
    }
}
