
package android.app.common.ui;

import android.app.*;
import android.widget.*;

import com.mobeta.android.dslv.*;

public class DragSortListActivity extends ListActivity {

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        if (!(getListView() instanceof DragSortListView)) {
            throw new RuntimeException(
                    "Your content must have a DragSortListView whose id attribute is "
                            + "'android.R.id.list'");
        }
        DragSortListView list = getDragSortListView();
        list.setDropListener(new DragSortListView.DropListener() {
            @Override
            public void drop(int from, int to) {
                onDragDrop(from, to);
            }
        });
    }

    protected void onDragDrop(int from, int to) {
    }

    protected void notifyDataSetChanged() {
        ((BaseAdapter) getListAdapter()).notifyDataSetChanged();
    }

    public DragSortListView getDragSortListView() {
        return (DragSortListView) getListView();
    }
}
